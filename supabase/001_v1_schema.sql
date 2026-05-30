-- RentalConnect V1 schema
-- Expected by the current shared KMP app code.

create extension if not exists pgcrypto;

do $$
begin
    if not exists (
        select 1 from pg_type where typname = 'user_role'
    ) then
        create type user_role as enum ('owner', 'tenant');
    end if;

    if not exists (
        select 1 from pg_type where typname = 'property_status'
    ) then
        create type property_status as enum ('available', 'occupied');
    end if;
end $$;

create table if not exists public.users (
    id uuid primary key references auth.users(id) on delete cascade,
    name text not null,
    email text not null unique,
    phone text not null unique,
    hometown text not null default '',
    aadhar_id text not null default '',
    role user_role not null,
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now()
);

create table if not exists public.properties (
    id uuid primary key default gen_random_uuid(),
    owner_id uuid not null references public.users(id) on delete cascade,
    name text not null,
    address text not null,
    hno text not null,
    type text not null default '',
    status property_status not null default 'available',
    photos text[] not null default '{}',
    owner_name text not null default '',
    owner_phone text not null default '',
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now()
);

create table if not exists public.tenancies (
    id uuid primary key default gen_random_uuid(),
    property_id uuid not null references public.properties(id) on delete cascade,
    tenant_id uuid not null references public.users(id) on delete cascade,
    flat_number text not null default '',
    since text not null default '',
    created_at timestamptz not null default now(),
    unique (property_id, tenant_id)
);

create or replace function public.set_updated_at()
returns trigger
language plpgsql
as $$
begin
    new.updated_at = now();
    return new;
end;
$$;

create or replace function public.handle_new_auth_user()
returns trigger
language plpgsql
security definer
set search_path = public
as $$
begin
    insert into public.users (
        id,
        name,
        email,
        phone,
        hometown,
        aadhar_id,
        role
    )
    values (
        new.id,
        coalesce(new.raw_user_meta_data ->> 'name', split_part(new.email, '@', 1)),
        new.email,
        coalesce(new.raw_user_meta_data ->> 'phone', ''),
        coalesce(new.raw_user_meta_data ->> 'hometown', ''),
        coalesce(new.raw_user_meta_data ->> 'aadhar_id', ''),
        coalesce((new.raw_user_meta_data ->> 'role')::user_role, 'tenant'::user_role)
    )
    on conflict (id) do update
    set
        name = excluded.name,
        email = excluded.email,
        phone = excluded.phone,
        hometown = excluded.hometown,
        aadhar_id = excluded.aadhar_id,
        role = excluded.role;

    return new;
end;
$$;

drop trigger if exists users_set_updated_at on public.users;
create trigger users_set_updated_at
before update on public.users
for each row execute function public.set_updated_at();

drop trigger if exists properties_set_updated_at on public.properties;
create trigger properties_set_updated_at
before update on public.properties
for each row execute function public.set_updated_at();

drop trigger if exists on_auth_user_created on auth.users;
create trigger on_auth_user_created
after insert on auth.users
for each row execute function public.handle_new_auth_user();

create or replace view public.properties_with_counts as
select
    p.*,
    coalesce(count(t.id), 0)::int as tenant_count
from public.properties p
left join public.tenancies t on t.property_id = p.id
group by p.id;

alter table public.users enable row level security;
alter table public.properties enable row level security;
alter table public.tenancies enable row level security;

drop policy if exists "users_select_self" on public.users;
create policy "users_select_self"
on public.users
for select
using (auth.uid() = id);

drop policy if exists "users_select_related_tenants_for_owner" on public.users;
create policy "users_select_related_tenants_for_owner"
on public.users
for select
using (
    exists (
        select 1
        from public.properties p
        where p.owner_id = auth.uid()
          and (
              users.role = 'tenant'
              or users.id = auth.uid()
          )
    )
);

drop policy if exists "users_insert_self" on public.users;
create policy "users_insert_self"
on public.users
for insert
with check (auth.uid() = id);

drop policy if exists "users_update_self" on public.users;
create policy "users_update_self"
on public.users
for update
using (auth.uid() = id)
with check (auth.uid() = id);

drop policy if exists "properties_select_owner_or_tenant" on public.properties;
create policy "properties_select_owner_or_tenant"
on public.properties
for select
using (
    owner_id = auth.uid()
    or exists (
        select 1
        from public.tenancies t
        where t.property_id = properties.id
          and t.tenant_id = auth.uid()
    )
);

drop policy if exists "properties_insert_owner" on public.properties;
create policy "properties_insert_owner"
on public.properties
for insert
with check (
    owner_id = auth.uid()
    and exists (
        select 1 from public.users u
        where u.id = auth.uid() and u.role = 'owner'
    )
);

drop policy if exists "properties_update_owner" on public.properties;
create policy "properties_update_owner"
on public.properties
for update
using (owner_id = auth.uid())
with check (owner_id = auth.uid());

drop policy if exists "tenancies_select_owner_or_tenant" on public.tenancies;
create policy "tenancies_select_owner_or_tenant"
on public.tenancies
for select
using (
    tenant_id = auth.uid()
    or exists (
        select 1
        from public.properties p
        where p.id = tenancies.property_id
          and p.owner_id = auth.uid()
    )
);

drop policy if exists "tenancies_insert_owner" on public.tenancies;
create policy "tenancies_insert_owner"
on public.tenancies
for insert
with check (
    exists (
        select 1
        from public.properties p
        where p.id = tenancies.property_id
          and p.owner_id = auth.uid()
    )
);

drop policy if exists "tenancies_delete_owner" on public.tenancies;
create policy "tenancies_delete_owner"
on public.tenancies
for delete
using (
    exists (
        select 1
        from public.properties p
        where p.id = tenancies.property_id
          and p.owner_id = auth.uid()
    )
);

insert into storage.buckets (id, name, public)
values ('property-photos', 'property-photos', true)
on conflict (id) do nothing;

drop policy if exists "property_photos_public_read" on storage.objects;
create policy "property_photos_public_read"
on storage.objects
for select
using (bucket_id = 'property-photos');

drop policy if exists "property_photos_owner_write" on storage.objects;
create policy "property_photos_owner_write"
on storage.objects
for insert
with check (
    bucket_id = 'property-photos'
    and auth.role() = 'authenticated'
);

drop policy if exists "property_photos_owner_update" on storage.objects;
create policy "property_photos_owner_update"
on storage.objects
for update
using (
    bucket_id = 'property-photos'
    and auth.role() = 'authenticated'
)
with check (
    bucket_id = 'property-photos'
    and auth.role() = 'authenticated'
);

drop policy if exists "property_photos_owner_delete" on storage.objects;
create policy "property_photos_owner_delete"
on storage.objects
for delete
using (
    bucket_id = 'property-photos'
    and auth.role() = 'authenticated'
);

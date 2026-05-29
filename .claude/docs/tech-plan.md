# RentalConnect V1 — Tech Plan

---

## Stack

- **Frontend**: Compose Multiplatform (CMP) + Kotlin Multiplatform (KMP) — single codebase for iOS & Android
- **Backend**: Supabase — auth, database, and storage out of the box
- **Language**: Kotlin

---

## Data Model

```
users          — id, name, phone, role (owner | tenant), hometown, aadhar_id
properties     — id, owner_id, address, hno, photos[], status (available | occupied)
tenancies      — id, property_id, tenant_id (joins tenants to properties)
```

---

## Screens

| Screen | Who |
|---|---|
| Register / Login | Both |
| My Properties (list) | Owner |
| Add / Edit Property | Owner |
| Property Detail + Tenants | Owner |
| Add Tenant to Property | Owner |
| My Rentals (list) | Tenant |
| Rental Detail + Owner Info | Tenant |
| Profile | Both |

---

## Auth Flow

- Email/password via Supabase Auth
- Role selected at registration, stored on the user record
- Row-level security in Supabase so owners only see their data, tenants only see theirs

---

## Implementation Order

1. Supabase project setup — tables, RLS policies
2. Auth screens — register, login, role selection
3. Owner flow — properties CRUD, tenant assignment
4. Tenant flow — view rentals, view owner profile
5. Photo uploads via Supabase Storage
6. Polish — loading states, empty states, basic error handling

---

## Out of Scope for V1

- Maps integration
- Push notifications
- Complaints / maintenance (Phase 2)
- Payments

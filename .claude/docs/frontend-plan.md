# RentalConnect — Frontend Plan

---

## Navigation Structure

Two separate nav graphs, selected after auth based on role.

```
Root
├── Auth Graph
│   ├── Login
│   └── Register  (name, phone, hometown, aadhar_id, role picker)
│
├── Owner Graph  (bottom nav: Properties | Profile)
│   ├── PropertyList
│   ├── AddEditProperty
│   ├── PropertyDetail
│   │   └── AddTenant  (search by phone → confirm → attach)
│   └── OwnerProfile
│
└── Tenant Graph  (bottom nav: My Rentals | Profile)
    ├── RentalList
    ├── RentalDetail  (property info + owner card)
    └── TenantProfile
```

---

## Screen Breakdown

### Auth

| Screen | Inputs | Action |
|---|---|---|
| Login | Email, Password | Sign in → route to role graph |
| Register | Name, Phone, Hometown, Aadhar ID, Email, Password, Role toggle | Sign up + insert profile → route to role graph |

---

### Owner

**PropertyList**
- List of property cards (address, hno, status badge, tenant count)
- FAB → AddEditProperty

**AddEditProperty**
- Fields: Address, House No, Status toggle (Available / Occupied)
- Photo picker (multi, upload to Storage)
- Save → back to list

**PropertyDetail**
- Property photos (horizontal scroll), address, status badge
- Tenant list section — each row: avatar, name, phone
- Add Tenant button → AddTenant sheet
- Tap tenant row → tenant profile read-only view

**AddTenant**
- Phone number search field
- Result card (name, hometown) with Confirm button
- On confirm → insert tenancy

**OwnerProfile**
- Name, Phone, Hometown, Aadhar ID (read)
- Edit button → inline edit
- Sign out

---

### Tenant

**RentalList**
- List of property cards (address, hno, status badge, owner name)

**RentalDetail**
- Property photos, address, hno
- Owner card: name, phone (tap to call)
- "Your Home" badge if only one rental

**TenantProfile**
- Same shape as OwnerProfile

---

## State Management

One ViewModel per screen. No shared global state store — each VM fetches what it needs.

```
AuthViewModel         → handles sign-in / sign-up / session
PropertyListViewModel → loads owner's properties
PropertyDetailViewModel → loads single property + its tenants
AddTenantViewModel    → phone search + tenancy creation
RentalListViewModel   → loads tenant's tenancies + property joins
RentalDetailViewModel → loads single tenancy + property + owner profile
ProfileViewModel      → loads + updates own profile
```

ViewModels expose a single `UiState` sealed class:
```kotlin
sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}
```

---

## Folder Structure

```
commonMain/kotlin/com/harsh/rentalconnect/
├── ui/
│   ├── theme/           ← Color, Type, Theme  (done)
│   ├── components/      ← AppButton, etc.     (done)
│   ├── auth/
│   │   ├── LoginScreen.kt
│   │   ├── RegisterScreen.kt
│   │   └── AuthViewModel.kt
│   ├── owner/
│   │   ├── propertylist/
│   │   ├── addeditproperty/
│   │   ├── propertydetail/
│   │   └── addtenant/
│   ├── tenant/
│   │   ├── rentallist/
│   │   └── rentaldetail/
│   └── profile/
│       └── ProfileScreen.kt
├── data/
│   ├── model/           ← data classes (Profile, Property, Tenancy)
│   └── repository/      ← one repository per domain (Auth, Property, Tenancy, Profile)
└── App.kt               ← nav host entry point
```

---

## Implementation Order

1. Nav scaffold — root nav host, owner/tenant bottom nav shells
2. Auth screens — Login, Register, role routing
3. Owner flow — PropertyList → AddEditProperty → PropertyDetail → AddTenant
4. Tenant flow — RentalList → RentalDetail
5. Profile screen (shared shape, both roles)
6. Photo upload + display (Coil)

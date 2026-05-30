package com.harsh.rentalconnect.data

import com.harsh.rentalconnect.domain.model.AuthUser
import com.harsh.rentalconnect.domain.model.Property
import com.harsh.rentalconnect.domain.model.Tenant
import com.harsh.rentalconnect.ui.models.Role
import kotlinx.coroutines.flow.MutableStateFlow

internal object AppStore {
    val accounts = MutableStateFlow(
        listOf(
            AccountRecord(
                user = AuthUser(
                    id = "owner-demo",
                    name = "Rajesh Kumar",
                    email = "owner@rentalconnect.app",
                    phone = "+91 99887 76655",
                    role = Role.Owner,
                    hometown = "Bengaluru, Karnataka",
                    aadharId = "1234 5678 9001",
                ),
                password = "Owner@123",
            ),
            AccountRecord(
                user = AuthUser(
                    id = "tenant-demo",
                    name = "Arjun Khanna",
                    email = "tenant@rentalconnect.app",
                    phone = "+91 98765 43210",
                    role = Role.Tenant,
                    hometown = "Chandigarh, Punjab",
                    aadharId = "1234 9876 5432",
                    linkedTenantIds = listOf("t1"),
                ),
                password = "Tenant@123",
            ),
            AccountRecord(
                user = AuthUser(
                    id = "tenant-priya",
                    name = "Priya Menon",
                    email = "priya@rentalconnect.app",
                    phone = "+91 99123 45678",
                    role = Role.Tenant,
                    hometown = "Kochi, Kerala",
                    aadharId = "4567 1234 9876",
                    linkedTenantIds = listOf("t2"),
                ),
                password = "Tenant@123",
            ),
            AccountRecord(
                user = AuthUser(
                    id = "tenant-rahul",
                    name = "Rahul Nair",
                    email = "rahul@rentalconnect.app",
                    phone = "+91 97654 32109",
                    role = Role.Tenant,
                    hometown = "Thiruvananthapuram, Kerala",
                    aadharId = "7890 1234 4567",
                    linkedTenantIds = listOf("t3"),
                ),
                password = "Tenant@123",
            ),
        )
    )

    val properties = MutableStateFlow(MockDataSource.properties)
    val tenants = MutableStateFlow(MockDataSource.tenants)
}

internal data class AccountRecord(
    val user: AuthUser,
    val password: String,
)

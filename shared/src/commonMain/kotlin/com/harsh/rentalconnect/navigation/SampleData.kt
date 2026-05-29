package com.harsh.rentalconnect.navigation

import com.harsh.rentalconnect.ui.models.OwnerInfo
import com.harsh.rentalconnect.ui.models.OwnerStats
import com.harsh.rentalconnect.ui.models.PropertyDetail
import com.harsh.rentalconnect.ui.models.PropertySummary
import com.harsh.rentalconnect.ui.models.Role
import com.harsh.rentalconnect.ui.models.TenantDetail
import com.harsh.rentalconnect.ui.models.TenantOwnerInfo
import com.harsh.rentalconnect.ui.models.TenantPropertyDetail
import com.harsh.rentalconnect.ui.models.TenantPropertyInfo
import com.harsh.rentalconnect.ui.models.TenantSummary

object SampleData {

    // ── Owner ─────────────────────────────────────────────────────────────────

    val ownerName = "Rajesh Kumar"
    val ownerInitials = "RK"

    val ownerStats = OwnerStats(
        propertyCount = 4,
        tenantCount = 7,
        vacantCount = 2,
    )

    val tenantSummaries = listOf(
        TenantSummary(id = "t1", name = "Arjun Khanna",  initials = "AK", flatNumber = "Flat 2A", since = "Jan 2024"),
        TenantSummary(id = "t2", name = "Priya Menon",   initials = "PM", flatNumber = "Flat 2B", since = "Mar 2024"),
        TenantSummary(id = "t3", name = "Rahul Nair",    initials = "RN", flatNumber = "Flat 3A", since = "Jun 2023"),
    )

    val properties = listOf(
        PropertySummary(
            id = "p1",
            name = "Sunrise Apartments",
            address = "14B, MG Road, Bengaluru",
            tenantCount = 3,
            type = "2BHK",
            isOccupied = true,
        ),
        PropertySummary(
            id = "p2",
            name = "Green Villa",
            address = "22, Koramangala, Bengaluru",
            tenantCount = 0,
            type = "3BHK",
            isOccupied = false,
        ),
    )

    fun propertyDetailFor(propertyId: String): PropertyDetail = when (propertyId) {
        "p1" -> PropertyDetail(
            id = "p1",
            name = "Sunrise Apartments",
            address = "14B, MG Road, Bengaluru 560001",
            type = "2BHK Apartment",
            houseNumber = "HNO-14B",
            isOccupied = true,
            tenants = tenantSummaries,
        )
        else -> PropertyDetail(
            id = "p2",
            name = "Green Villa",
            address = "22, Koramangala, Bengaluru 560034",
            type = "3BHK Apartment",
            houseNumber = "HNO-22",
            isOccupied = false,
            tenants = emptyList(),
        )
    }

    fun tenantDetailFor(tenantId: String): TenantDetail = when (tenantId) {
        "t1" -> TenantDetail(
            id = "t1",
            name = "Arjun Khanna",
            initials = "AK",
            propertyName = "Sunrise Apartments",
            flatNumber = "Flat 2A",
            phone = "+91 98765 43210",
            hometown = "Chandigarh, Punjab",
            aadharId = "XXXX XXXX 4321",
            tenantSince = "January 12, 2024",
            assignedPropertyName = "Sunrise Apartments",
            assignedPropertyAddress = "14B, MG Road, Bengaluru",
        )
        "t2" -> TenantDetail(
            id = "t2",
            name = "Priya Menon",
            initials = "PM",
            propertyName = "Sunrise Apartments",
            flatNumber = "Flat 2B",
            phone = "+91 99123 45678",
            hometown = "Kochi, Kerala",
            aadharId = "XXXX XXXX 8765",
            tenantSince = "March 5, 2024",
            assignedPropertyName = "Sunrise Apartments",
            assignedPropertyAddress = "14B, MG Road, Bengaluru",
        )
        else -> TenantDetail(
            id = "t3",
            name = "Rahul Nair",
            initials = "RN",
            propertyName = "Sunrise Apartments",
            flatNumber = "Flat 3A",
            phone = "+91 97654 32109",
            hometown = "Thiruvananthapuram, Kerala",
            aadharId = "XXXX XXXX 5678",
            tenantSince = "June 1, 2023",
            assignedPropertyName = "Sunrise Apartments",
            assignedPropertyAddress = "14B, MG Road, Bengaluru",
        )
    }

    // ── Tenant ────────────────────────────────────────────────────────────────

    val tenantName = "Arjun Khanna"
    val tenantInitials = "AK"

    val tenantPropertyInfo = TenantPropertyInfo(
        propertyName = "Sunrise Apartments",
        address = "14B, MG Road, Bengaluru",
        flatNumber = "Flat 2A",
        type = "2BHK",
    )

    val tenantOwnerInfo = OwnerInfo(
        name = "Rajesh Kumar",
        initials = "RK",
        role = Role.Owner,
    )

    val tenantPropertyDetail = TenantPropertyDetail(
        propertyName = "Sunrise Apartments",
        address = "14B, MG Road, Bengaluru 560001",
        flatNumber = "2A",
        type = "2BHK Apartment",
        houseNumber = "HNO-14B",
    )

    val tenantOwnerContact = TenantOwnerInfo(
        name = "Rajesh Kumar",
        initials = "RK",
        phone = "+91 99887 76655",
    )
}

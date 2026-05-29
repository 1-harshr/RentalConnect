package com.harsh.rentalconnect.data

import com.harsh.rentalconnect.domain.model.Property
import com.harsh.rentalconnect.domain.model.Tenant

internal object MockDataSource {

    val tenants = listOf(
        Tenant(
            id = "t1", name = "Arjun Khanna", phone = "+91 98765 43210",
            flatNumber = "Flat 2A", since = "January 12, 2024",
            propertyId = "p1", propertyName = "Sunrise Apartments",
            propertyAddress = "14B, MG Road, Bengaluru",
            hometown = "Chandigarh, Punjab", aadharId = "XXXX XXXX 4321",
        ),
        Tenant(
            id = "t2", name = "Priya Menon", phone = "+91 99123 45678",
            flatNumber = "Flat 2B", since = "March 5, 2024",
            propertyId = "p1", propertyName = "Sunrise Apartments",
            propertyAddress = "14B, MG Road, Bengaluru",
            hometown = "Kochi, Kerala", aadharId = "XXXX XXXX 8765",
        ),
        Tenant(
            id = "t3", name = "Rahul Nair", phone = "+91 97654 32109",
            flatNumber = "Flat 3A", since = "June 1, 2023",
            propertyId = "p1", propertyName = "Sunrise Apartments",
            propertyAddress = "14B, MG Road, Bengaluru",
            hometown = "Thiruvananthapuram, Kerala", aadharId = "XXXX XXXX 5678",
        ),
    )

    val properties = listOf(
        Property(
            id = "p1", name = "Sunrise Apartments",
            address = "14B, MG Road, Bengaluru 560001",
            type = "2BHK Apartment", houseNumber = "HNO-14B",
            isOccupied = true, tenantCount = 3,
            ownerName = "Rajesh Kumar", ownerPhone = "+91 99887 76655",
        ),
        Property(
            id = "p2", name = "Green Villa",
            address = "22, Koramangala, Bengaluru 560034",
            type = "3BHK Apartment", houseNumber = "HNO-22",
            isOccupied = false, tenantCount = 0,
            ownerName = "Rajesh Kumar", ownerPhone = "+91 99887 76655",
        ),
    )
}

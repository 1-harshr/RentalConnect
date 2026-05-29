package com.harsh.rentalconnect.di

import com.harsh.rentalconnect.data.repository.PropertyRepositoryImpl
import com.harsh.rentalconnect.data.repository.TenantRepositoryImpl
import com.harsh.rentalconnect.domain.usecase.GetPropertiesUseCase
import com.harsh.rentalconnect.domain.usecase.GetPropertyDetailUseCase
import com.harsh.rentalconnect.domain.usecase.GetTenantDetailUseCase
import com.harsh.rentalconnect.domain.usecase.ValidateNameUseCase
import com.harsh.rentalconnect.domain.usecase.ValidatePhoneUseCase

object AppModule {
    private val propertyRepository = PropertyRepositoryImpl()
    private val tenantRepository = TenantRepositoryImpl()

    val getPropertiesUseCase = GetPropertiesUseCase(propertyRepository)
    val getPropertyDetailUseCase = GetPropertyDetailUseCase(propertyRepository)
    val getTenantDetailUseCase = GetTenantDetailUseCase(tenantRepository)
    val validateNameUseCase = ValidateNameUseCase()
    val validatePhoneUseCase = ValidatePhoneUseCase()
}

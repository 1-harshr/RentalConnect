package com.harsh.rentalconnect.di

import com.harsh.rentalconnect.config.loadSupabaseRuntimeConfig
import com.harsh.rentalconnect.data.remote.SupabaseAuthGateway
import com.harsh.rentalconnect.data.remote.SupabaseConfig
import com.harsh.rentalconnect.data.remote.SupabaseDataGateway
import com.harsh.rentalconnect.data.repository.AuthRepositoryImpl
import com.harsh.rentalconnect.data.repository.PropertyRepositoryImpl
import com.harsh.rentalconnect.data.repository.TenantRepositoryImpl
import com.harsh.rentalconnect.domain.usecase.AddPropertyUseCase
import com.harsh.rentalconnect.domain.usecase.ValidateAadharUseCase
import com.harsh.rentalconnect.domain.usecase.GetPropertiesUseCase
import com.harsh.rentalconnect.domain.usecase.GetPropertyDetailUseCase
import com.harsh.rentalconnect.domain.usecase.GetTenantAssignmentsUseCase
import com.harsh.rentalconnect.domain.usecase.GetTenantDetailUseCase
import com.harsh.rentalconnect.domain.usecase.UpdatePropertyUseCase
import com.harsh.rentalconnect.domain.usecase.ValidateEmailUseCase
import com.harsh.rentalconnect.domain.usecase.ValidateNameUseCase
import com.harsh.rentalconnect.domain.usecase.ValidatePasswordUseCase
import com.harsh.rentalconnect.domain.usecase.ValidatePhoneUseCase
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object AppModule {
    private val runtimeConfig = loadSupabaseRuntimeConfig()
    private val supabaseConfig = SupabaseConfig(
        url = runtimeConfig.url,
        anonKey = runtimeConfig.anonKey,
    )
    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                }
            )
        }
    }
    private val supabaseAuthGateway = SupabaseAuthGateway(httpClient, supabaseConfig)
    private val supabaseDataGateway = SupabaseDataGateway(httpClient, supabaseConfig)

    val authRepository = AuthRepositoryImpl(
        supabaseAuthGateway = supabaseAuthGateway,
        supabaseDataGateway = supabaseDataGateway,
        useSupabase = supabaseConfig.isConfigured,
    )
    val propertyRepository = PropertyRepositoryImpl(
        supabaseDataGateway = supabaseDataGateway,
        useSupabase = supabaseConfig.isConfigured,
        sessionTokenProvider = { authRepository.session.value?.accessToken },
    )
    val tenantRepository = TenantRepositoryImpl(
        supabaseDataGateway = supabaseDataGateway,
        useSupabase = supabaseConfig.isConfigured,
        sessionTokenProvider = { authRepository.session.value?.accessToken },
    )

    val getPropertiesUseCase = GetPropertiesUseCase(propertyRepository)
    val getPropertyDetailUseCase = GetPropertyDetailUseCase(propertyRepository)
    val getTenantDetailUseCase = GetTenantDetailUseCase(tenantRepository)
    val getTenantAssignmentsUseCase = GetTenantAssignmentsUseCase(tenantRepository)
    val addPropertyUseCase = AddPropertyUseCase(propertyRepository)
    val updatePropertyUseCase = UpdatePropertyUseCase(propertyRepository)
    val validateAadharUseCase = ValidateAadharUseCase()
    val validateEmailUseCase = ValidateEmailUseCase()
    val validateNameUseCase = ValidateNameUseCase()
    val validatePasswordUseCase = ValidatePasswordUseCase()
    val validatePhoneUseCase = ValidatePhoneUseCase()
}

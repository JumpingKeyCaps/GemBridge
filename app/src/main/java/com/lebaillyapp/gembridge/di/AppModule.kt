package com.lebaillyapp.gembridge.di

import com.lebaillyapp.gembridge.data.repository.GeminiRepository
import com.lebaillyapp.gembridge.data.service.GeminiService
import com.lebaillyapp.gembridge.data.service.MockGeminiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) // Le module vit aussi longtemps que l'app
object AppModule {

    /**
     * Fournit l'implémentation concrète du service.
     * Pour l'instant on retourne le Mock.
     */
    @Provides
    @Singleton
    fun provideGeminiService(): GeminiService {
        return MockGeminiService() // on utilise notre mock pour le moment !
    }

    /**
     * Fournit le repository.
     * Hilt voit qu'il a besoin d'un GeminiService et utilise celui fourni au-dessus.
     */
    @Provides
    @Singleton
    fun provideGeminiRepository(service: GeminiService): GeminiRepository {
        return GeminiRepository(service)
    }
}
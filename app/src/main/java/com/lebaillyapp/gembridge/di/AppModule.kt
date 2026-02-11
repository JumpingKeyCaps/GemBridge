package com.lebaillyapp.gembridge.di

import com.lebaillyapp.gembridge.data.repository.GeminiRepository
import com.lebaillyapp.gembridge.data.service.GeminiService
import com.lebaillyapp.gembridge.data.service.MockGeminiService
import com.lebaillyapp.gembridge.domain.usecase.GetChatResponseUseCase
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



}
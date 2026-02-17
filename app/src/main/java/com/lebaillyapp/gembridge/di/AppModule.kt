package com.lebaillyapp.gembridge.di

import com.google.ai.client.generativeai.GenerativeModel
import com.lebaillyapp.gembridge.BuildConfig
import com.lebaillyapp.gembridge.data.repository.GeminiRepository
import com.lebaillyapp.gembridge.data.service.GeminiService
import com.lebaillyapp.gembridge.data.service.generativeAi.GoogleGeminiService
import com.lebaillyapp.gembridge.data.service.mock.MockGeminiService
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
     * Provides the API Key from BuildConfig.
     * Using a named provider or qualifier is a best practice to avoid
     * confusion with other String injections.
     */
    @Provides
    fun provideApiKey(): String = BuildConfig.GEMINI_API_KEY


    //MODE 0 - Mock service (used to debug app architecture pipeline)
    /**
     * Fournit l'implémentation concrète du service.
     * Pour l'instant on retourne le Mock.
     */
    @Provides
    @Singleton
    fun provideGeminiService(apiKey: String): GeminiService {
        // --- PILLAR 0: Architecture Debugging ---
         return MockGeminiService()

        // --- PILLAR 2: Official Google SDK ---
        // Now using the dynamic version of GoogleGeminiService
       // return GoogleGeminiService(apiKey)

        // --- PILLAR 1: Retrofit (Coming Soon) ---
        // return ...
    }

    // MODE 2 - GenerativeAI Google SDK
    @Provides
    @Singleton
    fun provideGenerativeModel(): GenerativeModel {
        // Configuration du moteur Gemini
        return GenerativeModel(
            modelName = "gemini-1.5-flash", // Version rapide et efficace pour un PoC
            apiKey = BuildConfig.GEMINI_API_KEY
        )
    }


}
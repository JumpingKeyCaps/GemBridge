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

    //MODE 0 - Mock service (used to debug app architecture pipeline)
    /**
     * Fournit l'implémentation concrète du service.
     * Pour l'instant on retourne le Mock.
     */
    @Provides
    @Singleton
    fun provideGeminiService(): GeminiService {
        return MockGeminiService() // on utilise notre mock pour le moment !
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

    @Provides
    @Singleton
    fun provideGeminiService(generativeModel: GenerativeModel): GeminiService {
        // Pour l'instant, on branche l'implémentation Google.
        // Plus tard, on pourra switcher ici sur Retrofit ou Firebase.
        return GoogleGeminiService(generativeModel)
    }

}
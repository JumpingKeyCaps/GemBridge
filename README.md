# GemBridge - Remote AI Integration (Android / Kotlin / MVVM)

A clean and modular **Proof of Concept** demonstrating remote AI integration with **Gemini Flash**, built in a scalable **MVVM** architecture using modern Android best practices.

---

## Overview

**GemBridge** acts as a bridge between Android and Google’s **Gemini Flash** API.  
It provides a streamlined, testable, and production-ready foundation for integrating AI-driven features into Android apps, without relying on on-device inference (TPU / AICore).

This PoC focuses on **clean architecture**, **developer productivity**, and **reusability**.  
It showcases two complementary integration paths:

1. **Custom Retrofit Implementation** : full control and flexibility.  
2. **Official Google Generative AI SDK** : simplified integration through the `com.google.ai.client.generativeai` package.

---

## Tech Stack

| Layer | Components | Purpose |
|-------|-------------|----------|
| **UI** | Jetpack Compose | Declarative and reactive UI layer |
| **Logic** | ViewModel + StateFlow | Reactive state management |
| **Network** | Retrofit + OkHttp | Custom REST client for Gemini API |
| **AI SDK** | Google Generative AI SDK | Official SDK for Gemini Flash |
| **Async** | Kotlin Coroutines | Structured concurrency |
| **Serialization** | Kotlinx Serialization / Moshi | JSON encoding & decoding |
| **DI (optional)** | Hilt / Koin | Dependency injection & scalability |
| **Language** | Kotlin | Modern, concise, safe |

---

## Architecture

UI (Compose)
↓
ViewModel
↓
Repository (GeminiRepository)
↓
DataSource (Retrofit / Google SDK)
↓
Gemini Flash API



Each layer is **loosely coupled**, **unit-testable**, and adheres to **Clean Architecture** principles.  
Two interchangeable data sources are provided for experimentation and benchmarking.

---

## API Integration

### 1. Retrofit-Based Client

The custom Retrofit implementation demonstrates low-level control over request/response handling.  
It’s ideal for developers who want to inspect payloads, log requests, or customize behavior.

Configuration uses `local.properties` for the API key, injected into `BuildConfig` for secure access.

### 2. Google Generative AI SDK

GemBridge also integrates the **official Google SDK**: `com.google.ai.client.generativeai:generativeai`

This SDK abstracts the REST layer, enabling developers to instantiate Gemini models directly via a high-level API.  
It provides out-of-the-box support for text, chat, and multimodal interactions, with internal authentication and serialization handled automatically.

Both integrations share the same **ViewModel** and **UI** layer, ensuring a consistent developer experience.

---

## Features

- Dual AI integration paths (Retrofit & Google SDK)  
- Modular and scalable MVVM architecture  
- Reactive UI with Jetpack Compose  
- Coroutine-based async execution  
- Clean separation of concerns  
- Testable repository pattern  
- Ready for multimodal AI (text, image, audio)

---

## Project Goals

- [ ] Retrofit integration with Gemini Flash  
- [ ] Google Generative AI SDK integration  
- [ ] Unified MVVM architecture  
- [ ] Shared repository abstraction  
- [ ] Multimodal AI support (image, audio)  
- [ ] Persistent caching & offline handling  
- [ ] Unit tests for Repository and ViewModel  

---

## Requirements

- **Android Studio Ladybug (or newer)**  
- **Kotlin 2.x**  
- **Min SDK 33+**  
- **Internet permission**  
- **Gemini Flash API key** from [Google AI Studio](https://aistudio.google.com/)  
- (Optional) Add the official Google AI SDK dependency:  implementation "com.google.ai.client.generativeai:generativeai:<latest_version>"

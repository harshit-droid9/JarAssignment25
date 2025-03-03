# Onboarding Animation Project

This repository contains an Android application implementing an onboarding animation screen as specified in the Figma design and prototype. The primary goal is to demonstrate modern Android development practices (Jetpack Compose, MVVM, Coroutines, Retrofit) along with smooth animations and transitions.

## Table of Contents
- Project Overview
- Architecture
- Screens & Features
- Libraries & Dependencies
- Setup / Installation
- Usage
- API Reference
- Future Scope

## Project Overview

This application fetches onboarding content (images, text, and animation details) from a remote API and displays a series of animated onboarding cards. The animations and transitions are implemented in Jetpack Compose, matching the provided Figma design as closely as possible.

### Key Features

1. **Network Fetch**: Retrieves onboarding data from a Mocky endpoint.
2. **Jetpack Compose UI**: Entirely built using Compose, ensuring modern, declarative UI patterns.
3. **Animations**: Includes slide-in transitions, fade-ins, and Lottie-based animations.
4. **MVVM Architecture**: Separation of concerns with ViewModels, repository, and data layer.
5. **Coroutines**: Asynchronous calls to fetch data from the network.
6. **Dependency Injection (Dagger)**: Manages app-wide dependencies such as Repository, Retrofit, etc.

## Architecture

This project follows an MVVM (Model-View-ViewModel) approach with a repository pattern for data management:

```
app/
┣ data/
┃ ┣ remote/      # Retrofit API service definitions
┃ ┣ model/       # Data models (DTOs)
┃ ┗ repository/  # Repository implementation
┣ domain
┃ ┗ repository/ # Respository interface
┣ di/            # Dagger modules and component(s)
┣ presentation/
┃ ┣ onboarding/  # Onboarding UI screens & composables
┃ ┣ MainViewModel/ # Example of a ViewModel with state flows
┃ ┣ MainActivity.kt
┗ App
```

### Flow of Data

1. **UI (Jetpack Compose)**
   - Observes StateFlow or LiveData in the ViewModel.
2. **ViewModel**
   - Calls methods from the Repository (suspend functions).
3. **Repository**
   - Fetches data from the Remote Data Source (Retrofit calls).
4. **Retrofit**
   - Retrieves JSON from the provided endpoint.

## Screens & Features

1. **Onboarding Screen**
   - Displays multiple cards from the fetched JSON.
   - Animations triggered in sequence: fade, slide, Lottie animation, etc.
2. **MainActivity**
   - Hosts the Compose content and sets up the application theme.
3. **Error / Loading States**
   - Show a loading indicator (or placeholder) while fetching data.
   - Show an error state if the network request fails.

## Libraries & Dependencies

Here's a list of major libraries used in the project (some version references are placeholders—ensure they match your actual libs.versions.toml or your Gradle dependencies):

- **Jetpack Compose**
  - implementation "androidx.compose.ui:ui:<version>"
  - implementation "androidx.compose.material3:material3:<version>"
  - implementation "androidx.compose.ui:ui-tooling-preview:<version>"
- **AndroidX**
  - implementation "androidx.core:core-ktx:<version>"
  - implementation "androidx.lifecycle:lifecycle-runtime-ktx:<version>"
  - implementation "androidx.activity:activity-compose:<version>"
- **Coroutines**
  - implementation "org.jetbrains.kotlinx:kotlinx-coroutines-core:<version>"
  - implementation "org.jetbrains.kotlinx:kotlinx-coroutines-android:<version>"
- **Retrofit & OkHttp**
  - implementation "com.squareup.retrofit2:retrofit:<version>"
  - implementation "com.squareup.retrofit2:converter-gson:<version>"
  - implementation "com.squareup.okhttp3:okhttp:<version>"
- **Dagger**
  - implementation "com.google.dagger:dagger:<version>"
  - kapt "com.google.dagger:dagger-compiler:<version>"
- **Coil** (for image loading)
  - implementation "io.coil-kt:coil-compose:<version>"
- **Lottie**
  - implementation "com.airbnb.android:lottie-compose:<version>"

## Setup / Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/harshit-droid9/JarAssignment25.git
   cd OnboardingAnimation
   ```

2. **Open in Android Studio**
   - Android Studio Giraffe or newer recommended.
   - Make sure you have the correct SDK and Compose versions installed.

3. **Sync & Build**
   - Let Gradle sync and Build the project (Build -> Make Project).

4. **Run**
   - Connect an Android device or use an emulator.
   - Press Run or use the Gradle task assembleDebug.

## Usage
- **On Launch**: The app immediately fetches the onboarding data from the API.
- **UI**: Cards with text, images, and an animated button (or other Lottie-based animation) will appear in sequence.
- **User Interaction**: The user can tap the primary CTA to proceed, or simply watch the animations.

## API Reference
- **Endpoint**: https://run.mocky.io/v3/0a095cf2-a081-44af-965a-953b0fa6499b
- **Data Model**: The JSON typically includes a list of "education cards" with images, text, and some animation parameters.

```json
{
  "success": true,
  "data": {
    "onboardingData": {
      "toolBarText": "Onboarding",
      "introTitle": "Introducing",
      "introSubtitle": "One-time save",
      "educationCardList": [
        {
          "image": "https://cdn.myjar.app/Homefeed/engagement_card/G2.png",
          "collapsedStateText": "Gold beats FD",
          "expandStateText": "Get better returns with gold!",
          "backGroundColor": "992D7E",
          "strokeStartColor": "#33FFFFFF",
          "strokeEndColor": "#CCFFFFFF",
          "startGradient": "#713A65",
          "endGradient": "#713A65"
        }
      ]
    }
  }
}
```

## Future Scope
- Cards should tilt diagonally when shrinks
- Cards click listeners, when clicking on the card, should reopening, cards should realigned themselves
- Move Dagger to Hilt, to reduce boilerplate code
- Deeplink support
- Intro screen

Onboarding Animation Project

This repository contains an Android application implementing an onboarding animation screen as
specified in the Figma design and prototype. The primary goal is to demonstrate modern Android
development practices (Jetpack Compose, MVVM, Coroutines, Retrofit) along with smooth animations and
transitions.

Table of Contents
• Project Overview
• Architecture
• Screens & Features
• Libraries & Dependencies
• Setup / Installation
• Usage
• API Reference
• Contributing
• License

Project Overview

This application fetches onboarding content (images, text, and animation details) from a remote API
and displays a series of animated onboarding cards. The animations and transitions are implemented
in Jetpack Compose, matching the provided Figma design as closely as possible.

Key Features

1. Network Fetch: Retrieves onboarding data from a Mocky endpoint.
2. Jetpack Compose UI: Entirely built using Compose, ensuring modern, declarative UI patterns.
3. Animations: Includes slide-in transitions, fade-ins, and Lottie-based animations.
4. MVVM Architecture: Separation of concerns with ViewModels, repository, and data layer.
5. Coroutines: Asynchronous calls to fetch data from the network.
6. Dependency Injection (Dagger): Manages app-wide dependencies such as Repository, Retrofit, etc.

Architecture

This project follows an MVVM (Model-View-ViewModel) approach with a repository pattern for data management:
app/
┣ data/
┃  ┣ remote/         # Retrofit API service definitions
┃  ┣ model/          # Data models (DTOs)
┃  ┗ repository/     # Repository interface and implementation
┣ di/                # Dagger modules and component(s)
┣ presentation/
┃  ┣ onboarding/     # Onboarding UI screens & composables
┃  ┗ MainViewModel/  # Example of a ViewModel with state flows
┣ presentation/      # Theming, shared UI components
┣ MainActivity.kt
┗ App
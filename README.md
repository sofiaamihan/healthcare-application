# Healthcare Application
A **centralised and secure mobile application** designed for individuals to conveniently **monitor, track, and log their health-related information**, built using **Kotlin** and **Jetpack Compose**. Users can access scheduling tools, view trends, and get reliable health insights from our admins. This application integrates a local Room Database for offline storage and Amazon Web Services (AWS) for remote data management, ensuring accessibility, reliability, security, and data integrity. Modern Android development practices have been followed, including MVVM architecture, Jetpack components, and efficient state management. 

Inclusive of 24 CRUD APIs (14 Locally Cached), Sensor Readings from a Microcontroller for Accelerometer & Gyroscope, Backend Test Scripts (19/24 APIs), a CI/CD Flow via GitHub Actions, as well as Offline Access, this repository serves as the Final Source Code Submission for CMC2C16	IoT Application Development, CIT2C22	DevOps Essentials, and CIT2C20	Full Stack Web Development. Other deliverables include my [Project Proposal](https://github.com/sofiaamihan/full-healthcare-application/blob/main/data/proposal.pdf) as well as my [Room Database Research](https://github.com/sofiaamihan/full-healthcare-application/blob/main/data/self-directed-learning-rooms-research.pdf).

![Main](https://github.com/sofiaamihan/full-healthcare-application/blob/main/data/main.png)

## Features
- **User Authentication**: Secure login and registration system provided by `AWS Lambda Authorizer`.
- **Health Data Management**: Store, retrieve, update, and delete user health records.
- **Offline Support**: Utilises Room Database for `offline data caching`, allowing users to read their recently accessed information.
- **Modern UI/UX**: Implemented using modern interfaces and design components using Jetpack Compose for a smooth and interactive experience.
- **Permissions Handling**: Accommodates necessary permissions for sensor-based functionalities.
- **Paging Support**: Provides efficient data loading with paging components.
- **AWS Cloud Integration**: Ensures secure remote storage and data synchronisation.
- **Locked Elements**: Only Admins are allowed to create, edit, and delete health insights provided by the Blogs in the Discover section.
- **Sensor Integration**: Information obtained via Accelerometer and Gyroscope is displayed live through Jetpack Compose Graphs that have been custom-made via `Canvas`.
- **Camera Integration**: Users can customise their profile photo by picking a picture from their gallery or using the camera.

## Architecture
Follows a clean MVVM (Model-View-ViewModel) Architecture consisting of:
- **Entity**: Represents the data model for the notes.
- **DAO (Data Access Object)**: Provides methods for accessing the database.
- **Database**: The Room database that holds the notes data.
- **Repository**:  Acts as the single source of truth, managing data flow from local (Room) and remote (AWS) sources, ensuring the mediation between the data sources (DAO) and the ViewModel.
- **ViewModel**: Manages UI-related data in a lifecycle-conscious way.
- **ViewModel Factory**: Creates instances of the ViewModel with the required parameters.

## Authentication & Onboarding System
![Authentication](https://github.com/sofiaamihan/full-healthcare-application/blob/main/data/auth.png)

## Navigation & Component System
<p align="center"> <img src="https://github.com/sofiaamihan/full-healthcare-application/blob/main/data/nav.png" alt="Battle Screen" width="45%" /> <img src="https://github.com/sofiaamihan/full-healthcare-application/blob/main/data/components.png" alt="Home Screen" width="45%" /> </p>

## Tech Stack
Kotlin, Jetpack Compose, Room Database, Respective Extensions and Coroutines Support, Android Architecture Components (ViewModel, LiveData)

### Frontend (Android - Kotlin Jetpack Compose)
- **ViewModel**: ```androidx.lifecycle.viewmodel.compose```
- **Splash Screen**: ```androidx.core.splashscreen```
- **Navigation**: ```androidx.navigation.compose```
- **Paging Compose**: ```accompanist.pager```, ```accompanist.pager.indicators```
- **Datastore Preferences**: ```androidx.datastore.preferences```
- **Typography**: ```androidx.ui.text.google.fonts```
- **Icons**: ```androidx.material.icons.extended```
- **Permissions Handling**: ```accompanist.permissions```
- **Local Database (Rooms)**: 
  - ```androidx.room.runtime```
  - ```androidx.room.compiler```
  - ```androidx.runtime.livedata```
  - ```androidx.room.ktx```
- **Coil Image Upload**: ```libs.coil.compose```

### Backend (AWS Services) - 3 MicroServices
- **Authentication**: AWS Lambda Authorizer
- **Database & Storage**: AWS DynamoDB, AWS Relational Database (MySQL)
- **Requests & Business Logic**: Amazon API Gateway, AWS Lambda


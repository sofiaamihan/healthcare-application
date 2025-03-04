# Full Healthcare Application
A centralised and secure mobile application designed for individuals to conveniently monitor, track, and log their health-related information built using Kotlin and Jetpack Compose. Users can access scheduling tools, view trends, and get reliable health insights from our admins. This application integrates a local Room Database for offline storage and AWS for remote data management, ensuring accessibility, reliability, security, and data integrity. Modern Android development practices have been followed, including MVVM architecture, Jetpack components, and efficient state management.

## Features
- **User Authentication**: Secure login and registration system provided by AWS Lambda Authorizer.
- **Health Data Management**: Store, retrieve, update, and delete user health records.
- **Offline Support**: Utilises Room Database for offline data caching, allowing users to read their recently accessed information.
- **Modern UI/UX**: Implemented using modern interfaces and design components using Jetpack Compose for a smooth and interactive experience.
- **Permissions Handling**: Accommodates necessary permissions for sensor-based functionalities.
- **Paging Support**: Provides efficient data loading with paging components.
- **AWS Cloud Integration**: Ensures secure remote storage and data synchronisation.
- **Locked Elements**: Only Admins are allowed to create, edit, and delete health insights provided by the Blogs in the Discover section.
- **Sensor Integration**: Information obtained via Accelerometer and Gyroscope is displayed live through Jetpack Compose Graphs that have been custom made via Canvas.

## Architecture
Follows a clean MVVM (Model-View-ViewModel) Architecture consisting of:
- **Entity**: Represents the data model for the notes.
- **DAO (Data Access Object)**: Provides methods for accessing the database.
- **Database**: The Room database that holds the notes data.
- **Repository**:  Acts as the single source of truth, managing data flow from local (Room) and remote (AWS) sources, ensuring the mediation between the data sources (DAO) and the ViewModel.
- **ViewModel**:: Manages UI-related data in a lifecycle-conscious way.
- **ViewModel Factory**:: Creates instances of the ViewModel with the required parameters.

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

### Backend (AWS Services)
- **Authentication**: AWS Lambda Authorizer
- **Database & Storage**: AWS DynamoDB, AWS Relational Database (mySQL)
- **API Gateway & Lambda**: Handles requests and business logic


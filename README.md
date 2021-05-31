# SpaceXData

*An Android app as the assignment to fetch the data from the given API. 
*Designed with Repository Pattern and MVVM architectural pattern with Android Architecture Components.

<a href="https://imgur.com/qvLcA1Y"><img src="https://i.imgur.com/qvLcA1Ym.mp4" title="source: imgur.com" /></a>

##Prerequisite
Before running the project, please check if your gradle version matches the project's version

*package-name: com.demo.spacexdata
*compileSdkVersion = 30
*targetSdkVersion = 30
*minSdkVersion = 21
*buildToolsVersion = "30.0.3"

##Development Environment
*Android Studio 4.1.2
*Build #AI-201.8743.12.41.7042882,
*Runtime version: 1.8.0_242-release-1644-b3-6915495 x86_64
*VM: OpenJDK 64-Bit Server VM by JetBrains s.r.o
*macOS 10.15.2
*GC: ParNew, ConcurrentMarkSweep
*Memory: 4029M

##Architecture
The Application is built in MVVM Architecture.
MVVM have 3 main component called Model — View — View Model. View component will show data and manage the user interactions, View Model will handle all request
and provide all data needed for View,and Model will storing data and handle the business logic.

![MVVM](https://upload.wikimedia.org/wikipedia/commons/8/87/MVVMPattern.png)


##Testing
*app/test/ - Unit tests - test -> Right click on package name(com.demo.spacexdata) -> Run Test In 'com.demo.spacexdata'
*app/androidTest/ - Instrumentation tests - androidTest -> Right click on package name(com.demo.spacexdata) -> Run Test In 'com.demo.spacexdata'

##Libraries
Following are the Libraries used:
* Kotlin 💎
*Material Design - Google material design UIs.
*Hilt - Dependency Injection library
*Retrofit - Network Http Client
*Viewmodel - Channel between use cases and UI
*View Binding - For binding of UI components in layouts to data sources, and coroutines support.
*Gson - Data, Model & Entity JSON Parser that understands Kotlin non-nullable and default parameters
*okhttp-logging-interceptor - logs HTTP request and response data.
*Mockito - Mocking framework used in unit tests.
*kotlinx.coroutines - Library Support for coroutines, provides runBlocking coroutine builder used in tests
*Espresso - Test framework to write UI Tests
*Room Persistence Library - Robust database access while harnessing the full power of SQLite
*Truth - Provides fluent assertions for Java and Android

# Speak Take-home Project

The purpose of this repository is to create a simple app that consists of two main screens: `CourseScreen` & `RecordScreen`.

The `CourseScreen` shows the `Course` data, which includes title and subtitle, thumbnail image of the user, as well as a list of units and their respective day lessons.  Additionally, the lessons are connected by a timeline stating which days they belong to.  

> I must've spend between 2-3 hours on the `CourseScreen`, including UI/UX & data setup.

The `RecordScreen`, on the other hand, consist of a simple enclosing box and a button.  Such box is meant to show the data that this screen is meant to get from a web socket connection.

> I must've spend between 3-4 hours on the `RecordScreen`, including UI/UX, data setup, web socket setup and testing, plus the little extras I added into it for usability and presentation purposes.

Lastly, this web socket connection should be opened for the following reasons:

- To send a 'start' message to the backend, meaning that we want to start streaming data
- To send `AudioChunk`s to the backend for processing, with the expectation that we'll receive text responses back

## Architecture

I decided to organize the application using a **DATA**/**DOMAIN**/**FEATURE** architecture; as seen from the root project's `data`/`domain`/`features` + `app` directories.

This architecture ensures a good separation of concerns, and keeps the different layers of the app isolated from each other.

Moreover, this architecture is a direct interpretation of Android's own recommendations for *modern Android development*, as found in their [official documentation](https://developer.android.com/topic/architecture#recommended-app-arch).

![android-architecture.png](assets/android-architecture.png)

Lastly, I'm using separate Gradle modules to further separate the different layers of the application; with some layers consisting of more than one Gradle module.

This approach is meant to optimize the build process, where each Gradle module may be build independently and increasingly.  Meaning that in non-first, subsequent builds, only the modules that have been changed will require a re-build. 

### UI Layer

The **UI Layer** is divided into two main components: the main `:app` Gradle module, and both of the feature Gradle modules inside the `features` dir: `:feature:course` & `:feature:record`.

The `:app` module is the "main" module of the app, containing the `LAUNCHER` `Activity`.

Both of the `feature` modules, on the other hand, contain Composable `Screen` implementations, plus their companion `ViewModel`s.

### Domain Layer

For the **Domain Layer**, I decided to only create a single Gradle module, `:domain:record`, for its accompanying `:feature:record` feature module.

The reason why I decided to only create a *partial* **Domain Layer** was for demonstration purposes, and because the `course` feature or data was not hefty enough to require it for a sample application.

In a bigger or more fleshed out application, we should probably have a fuller **Domain Layer** containing interface classes for all the data repositories, as well as further use cases for the **UI Layer** to utilize. 

### Data Layer

For the `data` layer, I decided to use a monolithic Gradle module containing *all* of the application's data in order to keep it simple, and to not spend an immense amount of time setting up individual Gradle modules per data type.

In a bigger or more fleshed out application, we could further break down this monolith into individual Gradle modules in order to make a steeper separation of concerns, and to leverage Gradle's incremental build system even better.

## UI/UX

When it comes to the UI/UX, I attempted to recreate the screens that were given to me using simply screenshots to the best of my ability.  

The colors might not be 100% correct, nor the sizes or margins between elements, but I did the best I could without a design file.

Moreover, I took the time to implement an app-wide theme, consisting of a pre-defined Color palette that was included into the `MaterialTheme`, as well as Typography.

All of the theme-related code is inside the `:resources` Gradle module; more specifically, see the `Theme.kt`.

Here's a breakdown of how I analyzed, dissected and executed some of the theme-oriented decisions: 

![screen-theme-breakdown.png](assets/screen-theme-breakdown.png)

Moreover, I made sure to implement Dark Mode, as well as a Splash Screen with an Animated Vector Drawable (AVD) with a little animation -- the Splash Screen has a Dark Mode variant as well -- and I even included a Theme Icon implementation just for the fun of it.

## What I'm Most Proud About

There are a few things I'm proud about with respect to this little project.  

Some of these include things I hadn't done before, or I hadn't done to the extent I did it here.

Some others are simply a showcase of the extend of the little toolbelt of tricks and peculiarities I've developed in the last few years.

Here's a list of the things I would like to highlight:

1. Most of the `:data` module contains **unit tests**; the only classes that I did not create test classes for are the networking classes due to complexity.
2. Dependency Injection using Koin: As a *Koin Community Lead*, it is (kind of) my duty to promote its use, and show how simple it is to get it started.
3. Dark mode: While new projects *almost* give you this for free nowadays, getting it to work properly does require some adjustments.
4. Comprehensive theme across the app: In a similar vein as Dark Mode, setting up colors, typography and such is half-baked into new projects, but requires some adjustments to get it to work.
5. Splash Screen: This is a personal favorite of mine, and I've written extensively about it in my Medium blog -- see my posts on [how to set up a splash screen](https://proandroiddev.com/inquiry-into-the-android-splashscreen-api-2023-e955946573b1), and [how to create an animated vector drawable for it](https://medium.com/geekculture/introduction-to-animated-vector-drawable-avd-for-android-8db301ad0776).
6. Theme Icon: Another topic that I've written on before, this is another one of my preferred small details -- see my post on [how to set up theme icons](https://medium.com/proandroiddev/android-13-implementing-themed-icons-into-your-app-e7002f2c4e04).
7. Most importantly, developing a multi-module project, with such a vast, and demarcated separation of concerns is something I've been wanting to do for some time.  This is probably what I enjoyed the most, and it's allowed me to showcase my understanding of Gradle (perhaps) to its full extent.  

## Trade-Offs

I took the reservation doing a few trade-offs for this particular sample application.

Some of them have been listed already in this document, but here's a comprehensive list of most (all?) of the trade-offs that I took:

1. The **Domain Layer** is thin, and mainly encompasses the `record` screen feature in order to showcase the full architecture.
2. The **Data Layer** is monolithic in order to keep it simple, rather than it being fully broken down for even further separation of concerns.
3. There's a little bit of *bloat* overall in the project that was added through the project and module creation processes.  Some of these files could be removed or cleaned up further for a more production-ready app.
4. While I attempted to create unit test classes for the entire `:data` Gradle module, I had to skip the network classes due to their complexity; these are the kind of classes you can spend a good chunk of time making sure the tests are written correctly!
5. When it comes to git, I decided to commit directly into the `main` branch, rather than create a parallel `develop` branch for development.  In a production app, would leave `main` only for release pushes, and develop on top of a `develop` branch; more akin to [Gitflow Workflow](https://www.atlassian.com/git/tutorials/comparing-workflows/gitflow-workflow).

## What's Next

If I was to take this project and attempt to make it into a 'real' and complete production app, these are the things I would do next:

1. Set up ProGuard correctly: While ProGuard is almost always a pain, I've got plenty of experience dealing with it for both client and SDK products.
2. Bring in a crash reporting tool, such as Crashlytics: This is of out-most importance for any production app, and should be high on any list for apps that don't have it already.
3. Setting up CI/CD: While I did add a small, GitHub Action file to simply build the project on pushes to the `main` branch.
4. Update all dependencies: I'm (a little bit) obsessed with keeping my dependencies as up-to-date as possible. 
   - This ensures that I stay on the vanguard of the different technologies we're using, and avoid falling behind and risk something loosing support without me knowing so.
5. Lastly, I would create a Play Console app, and start getting everything ready for an imminent release.  
   - Given my Google [Play Console Listing Certificate](https://playacademy.withgoogle.com/certificate/), am I vastly familiar with this process, and have gone through it more than once. 
   

------------------

## How To Run & Set up



Before building the project, add the following to your global gradle.properties file
(located at ~/.gradle/gradle.properties):
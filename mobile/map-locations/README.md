# Take-Home Coding Exercise

Welcome to the coding exercise! The goal of this project is to demonstrate your skills by building an app that displays a set of locations on a map in a performant manner. Please follow the instructions below to complete the exercise.

## Project Overview

This exercise involves fetching data from a JSON file stored in this repository and implementing a map view to display locations. You'll need to implement features like filtering locations by type and displaying additional information when a location is tapped.

## Instructions

1. **Fork the Repository:**
    - Please begin by forking this repository into a private repository.
    - All of your changes should be committed to your fork.
    - Optional: Use pull requests with short descriptions to help explain as you go through

2. **Setup Project:**
    - Create an `android` or `ios` folder respectively in the root of this folder. This is where your project will live.

3. **Fetch Location Data:**
    - The repository contains a JSON file, `locations.json`, that lists various locations.
    - Use the raw file content feature of GitHub for fetching data (think of it as a mock API). Please point it to your forked repository, not the Voze repository.
        - `https://raw.githubusercontent.com/<GITHUB_ACCOUNT_NAME>/coding-exercises/master/mobile/map-locations/locations.json`
    - Your task is to parse this file and use the data within the app.

4. **Display Locations on a Map View:**
    - Create a map view in the app as the main view.
        - The data is centered around San Francisco. Please set the default location to there.
    - Plot the locations from the JSON file on the map using pins or markers.

5. **Filtering Locations:**
    - Implement a way to filter the displayed locations by `location_type`.
        - The assumption can be made the location_types are a static set and will not change.
        - You can provide a UI (e.g., dropdown menu, segmented control, etc) that allows users to select which types of locations to display on the map.
        - Please feel free to "steal" existing UI from other application such as Google Maps, Apple Maps, Zillow, etc for filtering or craft your own.
        - Optional: Distinguish between location types through the presentation of pin or markers.

6. **Additional Details on Tap:**
   - When a user taps on a location pin, show a view with more detailed information about that location.
        - Display all `attributes` inside this detail view.
        - Please feel free to "steal" existing UI from other application such as Google Maps, Apple Maps, Zillow, etc for details or craft your own.

## Requirements

- **Documentation:**
    - Please add any contextual implementation information into Implementation section at the bottom of this `README`.
    - Add code comments when relevant
    - Add information on how to get the application up and running into the Getting Started section at the bottom of this `README`.
- **Programming Language:**
    - iOS: Please focus on using Swift
    - Android: Please focus on using Kotlin (Java is still acceptable, however Kotlin is preferred)
- **Data Fetching:**
    - You may use the std library or a 3rd party library for making HTTP request.
        - If using a 3rd party library please explain why inside the Implementation section at the bottom of this `README`.
- **Map Integration:**
    - You may use any mapping library, such as MapKit
    - Please do not use a mapping library which requires an API key
- **UI/UX:**
    - Design a user-friendly interface to interact with the map and filter locations.
    - Keep it simple. This isn't a exercise to test your design skill.
- **Filtering:**
    - Implement efficient filtering of locations by their type.

## Submission

1. Once you have completed the exercise, make the repository public and email a link to the forked repository.

## Evaluation Criteria

- **Correctness:** Does the app fetch and display data correctly?
- **UI/UX:** Is the map view intuitive and easy to use (within reason given lack of design criteria)?
- **Code Quality:** Is the code clean, readable, follow modern architecture per environment, and well-structured?
- **Filtering:** Is filtering by location type implemented efficiently?
- **Attention to Detail:** Does the app show detailed information when a pin is tapped?

Feel free to reach out if you have any questions. Good luck, and happy coding!

Do not edit any lines above this line break.

---

## Getting Started

Open the iOS project in Xcode and run it on an iOS simulator. I did not test it on a physical device.

Open the Android project in Android Studio. You'll need to add a `secrets.properties` file use the format of and at the same level as `local.defaults.properties`. It should contain your Google Maps API key. There isn't a way to use Google Maps without this key now, so I included it despite the instructions to the contrary. You should be able to build and run the project on an emulator from there. 


## Implementation

I'm not sure what is meant by "efficiently" filtering the locations by their type - the number of locations and types does not appear to be large so it probably isn't worth bothering with anything complicated. 

I made the assumption that the location attributes are always the same, and built that into the data model, but that isn't guaranteed by the data's apparent schema. 

There is some relic code for putting the locations into SwiftData with the hope of feeding the views from there, but I ran into some issues with threading. I felt that learning threading in Swift was beyond the scope of this exercise (what happened to Grand Central Dispatch?) so I stored them in memory.

SwiftUI has many analogues with React and I designed in a way that is idiomatic to React, it may not be idiomatic Swift code. The styling system seems oddly cumbersome. Same goes for Jetpack Compose.

The Android project desperately needs some animations to feel right, but there isn't anything quite as convenient as `withAnimation` and the basic animation framework doesn't play well with the map markers apparently. 

Claude and Copilot were very useful in this exercise. To a point. 


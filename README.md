# Boli Pocia Główka

Android application for tracking headache episodes and contextual factors.  
Built in **Kotlin** as a practical project to solve a real-life problem.

---

# The Story Behind the Project

My daughter has suffered from severe headaches for several years.  
During a neurological consultation we were advised to keep a **headache diary** in order to identify possible triggers.

Instead of maintaining a paper notebook, I decided to build a small Android application that allows structured tracking of headache episodes and surrounding conditions.

The project also served as an opportunity to:

- refresh my **Android development skills**
- learn **Kotlin**
- experiment with **modern Android architecture**
- build something genuinely useful for my family

The app has been **used daily by a real user for over a year**.

---

# Problem

Neurologists often recommend keeping a headache diary.  
However, paper notes make it difficult to:

- remember to record data regularly
- track periods **without headaches**
- collect contextual information (weather, cycle phase, location)
- export data for further analysis

---

# Solution

This application allows the user to quickly record headache episodes together with contextual factors and automatically reminds the user to enter data if no measurement has been recorded for a certain period.

Collected data can later be exported to Excel for further analysis.

---

# Main Features

## Headache recording

The user can record:

- date and time
- headache intensity
- weather conditions
- phase of menstrual cycle
- current location (home, school, etc.)
- duration of symptoms
- additional notes

## Reminder system

If no entry has been recorded for a configurable period (e.g. **3 hours**), the application sends a notification encouraging the user to log the current state.

This is important because **tracking headache-free periods is as important as tracking pain episodes**.

## History view

Users can:

- browse previous records
- quickly review recent episodes
- edit existing entries
- delete incorrect entries

## Data export

All collected data can be exported to **Excel**, enabling deeper analysis outside the application.

For example:

- identifying triggers
- checking correlations with weather
- correlating headaches with the menstrual cycle

---

# Screenshots

## Recording a headache entry

![Entry Screen](screenshots/entry.png)

## History view

![History Screen](screenshots/history.png)

*(Place screenshots inside a `/screenshots` directory in the repository.)*

---

# Tech Stack

- **Kotlin**
- **Android SDK**
- **Room** (local database)
- **Coroutines**
- Android notification system
- Excel export functionality

The application stores all data **locally on the device**.

---

# Architecture

The application follows a layered architecture with clear separation of responsibilities:

- UI layer
- ViewModels
- Repository layer
- Local persistence (Room database)

Main components include:

- database entities and DAOs
- repositories managing data access
- services responsible for notifications
- scheduled checks that trigger reminders when no data has been recorded

---

# Data Privacy

All data is stored **locally on the device**.  
No data is transmitted to external servers.

---

# Current Status

The application is actively used by a single real user.

Development so far focused on:

- reliable data entry
- simple UX
- stable local storage
- export functionality

Further analysis of the collected data is currently performed in **Excel**.

---

# Future Improvements

Possible directions for future development include:

- built-in data visualisation (charts)
- trigger detection algorithms
- cloud synchronization
- multiple user profiles
- automated weather integration
- improved reminder customization
- full CI pipeline and automated testing

---

# Build

Clone the repository and open the project in **Android Studio**.

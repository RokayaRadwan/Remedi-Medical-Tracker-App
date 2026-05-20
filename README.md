# Remedi

Remedi is a native Android medication tracker application built with **Kotlin**, **XML layouts**, **MVVM architecture**, and a local **Room Database**. The app helps users manage medications, schedule reminders, view today's medicines, track dose history, and manage profile/settings information.

## Project Overview

The goal of Remedi is to support users who need a simple mobile tool for organizing their daily medication routine. After creating an account and logging in, each user can manage their own medication records. The app stores the logged-in user's session locally using `SharedPreferences`, then uses the saved `userId` to filter medication data from the Room database so each user only sees their own records.

## Main Features

- Welcome, registration, and login screens
- Local user authentication using Room
- Session storage using SharedPreferences
- Dashboard summary screen
- Medication list screen
- Add and edit medication screen
- Today's medications screen
- Reminder schedule screen
- Dose history screen
- Profile/settings screen
- Logout functionality
- Local data persistence using Room Database
- Fragment-based navigation using Android Navigation Component
- RecyclerView-based list displays
- ViewBinding enabled for safe view access

## Technology Stack

- **Language:** Kotlin
- **UI:** XML layouts
- **Architecture:** MVVM
- **Database:** Room Database
- **Session Storage:** SharedPreferences
- **Navigation:** Android Navigation Component
- **Async Operations:** Kotlin Coroutines
- **UI Lists:** RecyclerView
- **Build Tool:** Gradle Kotlin DSL
- **Minimum SDK:** API 26, Android 8.0+
- **Target SDK:** API 36
- **Compile SDK:** API 36

## Project Structure

```text
Remedi/
├── app/
│   ├── src/main/
│   │   ├── java/com/example/remedi/
│   │   │   ├── data/
│   │   │   │   ├── local/
│   │   │   │   │   ├── dao/
│   │   │   │   │   ├── entity/
│   │   │   │   │   └── AppDatabase.kt
│   │   │   │   └── repository/
│   │   │   ├── ui/
│   │   │   │   ├── dashboard/
│   │   │   │   ├── history/
│   │   │   │   ├── medication/
│   │   │   │   ├── profile/
│   │   │   │   ├── registration/
│   │   │   │   └── reminderschedule/
│   │   │   ├── utils/
│   │   │   ├── viewmodel/
│   │   │   └── MainActivity.kt
│   │   └── res/
│   │       ├── layout/
│   │       ├── navigation/
│   │       ├── menu/
│   │       ├── drawable/
│   │       └── values/
│   ├── build.gradle.kts
│   └── proguard-rules.pro
├── gradle/
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
├── gradlew
├── gradlew.bat
└── README.md
```

## MVVM Architecture

The app follows the MVVM pattern required for the project:

```text
View Layer
Fragments + XML layouts
        ↓ observes LiveData / calls ViewModel functions
ViewModel Layer
Holds UI logic and survives configuration changes
        ↓ calls repository methods
Repository Layer
Provides a clean API between ViewModel and database
        ↓ calls DAO methods
Room Database Layer
Entities + DAO interfaces + AppDatabase
```

### Layer Responsibilities

| Layer | Responsibility |
|---|---|
| View | Displays UI, handles user actions, observes ViewModel data |
| ViewModel | Holds UI state and business logic, calls repositories |
| Repository | Separates data access from ViewModel and manages DAO calls |
| Room DAO | Defines database queries and CRUD operations |
| Room Entities | Define database tables |
| AppDatabase | Main Room database class |

## Room Database Schema

The Room database is named:

```text
remedi_database
```

The main database class is:

```text
app/src/main/java/com/example/remedi/data/local/AppDatabase.kt
```

### Entities

| Entity | Table | Purpose |
|---|---|---|
| `UserEntity` | `users` | Stores registered users |
| `MedicationEntity` | `medications` | Stores medication records for each user |
| `ReminderEntity` | `reminders` | Stores reminder information for medications |
| `DoseHistoryEntity` | `dose_history` | Stores medication dose history |
| `UserProfileEntity` | `user_profile` | Stores profile/settings data |

### Relationships

- One user can have many medications.
- One medication can have many reminders.
- One medication can have many dose history records.
- Deleting a user deletes their medications because of foreign key cascade behavior.
- Deleting a medication deletes its reminders and dose history records because of foreign key cascade behavior.

## Fragment Navigation

Navigation is defined in:

```text
app/src/main/res/navigation/nav_graph.xml
```

Main fragments:

| Fragment | Purpose |
|---|---|
| `WelcomeFragment` | First screen shown to the user |
| `LoginFragment` | Allows existing users to log in |
| `RegisterFragment` | Allows new users to create an account |
| `DashboardFragment` | Main home dashboard after login |
| `MedicationListFragment` | Displays all saved medications |
| `AddEditMedicationFragment` | Adds or edits medication records |
| `TodayMedicationsFragment` | Shows medications scheduled for today |
| `ReminderScheduleFragment` | Displays medication reminders |
| `DoseHistoryFragment` | Displays dose history records |
| `ProfileSettingsFragment` | Shows profile/settings and logout |

## SharedPreferences Usage

SharedPreferences is used only for lightweight session data after login. It stores the logged-in user's:

- user ID
- full name
- email

The saved user ID is important because medication records are filtered by `userId`, ensuring each user only sees their own medications.

Session data is saved in `LoginFragment` and cleared during logout in `ProfileSettingsFragment`.

Example session file name:

```text
user_session
```

## Setup Instructions

### Requirements

Before running the project, install:

- Android Studio
- Android SDK API 36
- Android device or emulator running Android 8.0/API 26 or higher
- JDK 17

### How to Run

1. Clone the repository:

```bash
git clone https://github.com/YOUR-USERNAME/Remedi.git
```

2. Open the project in Android Studio.

3. Wait for Gradle sync to finish.

4. Select an emulator or physical Android device.

5. Click **Run**.

No API keys or external server setup are required because the application uses a local Room database.

## Build APK

To build a debug APK from Android Studio:

```text
Build > Build Bundle(s) / APK(s) > Build APK(s)
```

Or from the terminal:

```bash
./gradlew assembleDebug
```

The generated APK will be located at:

```text
app/build/outputs/apk/debug/app-debug.apk
```

## Recommended GitHub Submission Structure

For final submission, the GitHub repository should contain the full Android Studio project source code. Additional deliverables can be placed in a separate `Deliverables` folder:

```text
Remedi/
├── app/
├── gradle/
├── build.gradle.kts
├── settings.gradle.kts
├── gradlew
├── gradlew.bat
├── README.md
└── Deliverables/
    ├── APK/
    │   └── app-debug.apk
    ├── Report/
    │   └── Remedi_Report.pdf
    ├── Presentation/
    │   └── Remedi_Presentation.pdf
    └── Video/
        └── walkthrough_link.txt
```

## Files/Folders Not to Upload

The following files and folders should not be committed to GitHub:

```text
.gradle/
build/
app/build/
local.properties
.idea/workspace.xml
.idea/caches/
*.iml
.DS_Store
```

Make sure your `.gitignore` includes these entries.

## Suggested User Flow for Demo

1. Open the app.
2. Start from the welcome screen.
3. Register a new user.
4. Return to the login screen.
5. Log in using the created account.
6. View the dashboard.
7. Add a medication.
8. View the medication in the medication list.
9. Open today's medications or reminder schedule.
10. Check profile/settings.
11. Log out.

## Team Contribution Log

| Team Member | Role | Contribution |
|---|---|---|
| Member 1 | Project Lead | Project organization, integration, testing |
| Member 2 | UI/UX & XML Layouts | XML screens, colors, layouts, navigation UI |
| Member 3 | Room Database & Repository | Entities, DAO interfaces, database, repositories |
| Member 4 | ViewModel & Business Logic | ViewModels, coroutines, session flow, app logic |

Update this table with the actual names and contributions before final submission.

## Notes

- This project is fully local and does not require a backend server.
- Medication data is stored in Room Database.
- Login session data is stored in SharedPreferences.
- The app uses ViewBinding instead of `findViewById()`.
- The app is designed to be opened and built directly in Android Studio.

# AI Trust Ledger Admin

An Android administration client for managing users, investment plans, deposits, withdrawals, announcements, and support conversations in the AI Trust Ledger Firebase project.

> **Project status:** This is a legacy, project-specific admin application rather than a reusable production template. It depends on an existing Firebase data model and contains credential-handling issues that must be addressed before the code is reused or distributed.

## Overview

The app gives an administrator a native Android dashboard backed by Firebase Authentication, Cloud Firestore, Cloud Storage, and Firebase Cloud Messaging. Its screens expose operational data from the companion AI Trust Ledger system and allow direct updates to user accounts, plans, team settings, transaction statuses, and announcements.

Room provides a local cache for several Firestore-backed models, while Android Navigation connects the XML/view-binding screens. Some payment and notification operations are performed directly from the Android client and therefore require additional security work before real-world use.

## Implemented Features

- Sign in administrators with Firebase Authentication and retain the local admin session.
- Show dashboard counts for active, inactive, and total users.
- Browse, search, and filter users with their balances, deposits, earnings, contact details, and referral codes.
- Block a user and mark that user's related transactions as blocked.
- Create Firebase users from the admin flow and apply an initial account deposit.
- Create and edit investment plans in Stocks, Medicine, and Forex categories.
- View and update team-level requirements and profit percentages.
- Review pending withdrawal requests, approve or reject them, copy wallet addresses, and notify users.
- Publish and delete text announcements, with FCM alerts sent to registered devices.
- Upload and remove image announcements using Firebase Storage and Firestore.
- Read user conversations and send admin replies through the Firestore chat collection.
- Store received notification history locally for display in the app.
- Cache users, accounts, plans, team settings, and withdrawal data with Room.
- Check pending deposit transaction status through CoinPayments-related client logic.

## How It Works

1. `LauncherActivity` checks for a stored admin ID and routes the user to login or the main admin area.
2. `LoginActivity` authenticates with Firebase, stores the admin ID, and updates the admin's FCM token.
3. `MainActivity` hosts the Navigation component graph, with `HomeFragment` acting as the dashboard and feature launcher.
4. ViewModels and repositories synchronize selected Firestore collections into Room-backed local models.
5. Feature fragments write administrative changes directly to Firestore or Firebase Storage.
6. Chat, announcement, and withdrawal actions use FCM data messages to notify user devices.

## Tech Stack

- **Kotlin** and Android XML layouts with view binding
- **AndroidX** AppCompat, Navigation, Lifecycle, LiveData, ViewModel, and SwipeRefreshLayout
- **Room** with KSP-generated persistence code
- **Firebase Authentication** for administrator and user account operations
- **Cloud Firestore** for users, accounts, plans, transactions, chats, settings, and announcements
- **Firebase Cloud Storage** for announcement images
- **Firebase Cloud Messaging** for device notifications
- **Kotlin coroutines** for asynchronous database and network work
- **OkHttp**, Google Auth libraries, Gson, Glide, Lottie, ZXing, and OpenCSV
- **Gradle Kotlin DSL**, Android Gradle Plugin 8.9.2, and Kotlin 2.1.10

## Project Structure

```text
app/src/main/
|-- java/com/trustledger/adminaitrust/
|   |-- ui/                 # Launcher, login, and main activities
|   |-- fragments/          # Dashboard and admin feature screens
|   |-- ViewModel/          # Screen-facing state and operations
|   |-- repository/         # Firebase access, Room database, and chat data
|   |-- Dao/                # Room data-access interfaces
|   |-- models/             # Firestore and Room data models
|   |-- notifications/      # FCM receiving and sending helpers
|   `-- adapter/            # RecyclerView adapters
|-- res/navigation/         # Fragment navigation graph
`-- res/layout/             # XML activity, fragment, item, and dialog layouts
```

## Getting Started

### Prerequisites

- Android Studio with Android SDK 35 installed
- JDK 11
- An Android device or emulator running Android 7.0 (API 24) or newer
- Access to a compatible Firebase project and its expected Firestore collections

### Firebase Configuration

The application ID is `com.trustledger.adminaitrust`. A compatible Firebase Android configuration must be available at `app/google-services.json`, with Authentication, Firestore, Storage, and Cloud Messaging configured for the same backend data model.

The app expects collections including `Admin`, `users`, `accounts`, `plans`, `teamSettings`, `transactions`, `announcements`, `announcement_images`, and `chats`. Their document fields must match the Kotlin models in `app/src/main/java/com/trustledger/adminaitrust/models/`.

### Build

On Windows:

```powershell
.\gradlew.bat assembleDebug
```

On macOS or Linux:

```bash
./gradlew assembleDebug
```

Open the project in Android Studio to run the `app` configuration on a device or emulator.

## Current Limitations and Security Notes

- Server-side Firebase messaging credentials and payment API credentials are embedded in the Android source. They should be treated as compromised, rotated, removed from the client, and replaced with authenticated backend operations.
- Administrative writes are performed directly from the app, so the safety of the system also depends on correctly restricted Firebase security rules.
- The app is tightly coupled to an existing Firebase schema and does not include backend provisioning or seed data.
- Deposit processing includes client-side CoinPayments status logic and should not be treated as a secure payment-verification implementation.
- Automated coverage is limited to the default generated unit and instrumentation examples.
- Generated build outputs and machine-specific configuration are currently tracked in the repository, and no license file is present.

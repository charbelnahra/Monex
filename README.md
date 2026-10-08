# 💱 Monex — Currency Converter & Social Android App

The application combines a practical **currency conversion tool** with user authentication, profile management, and a shared chat room. It demonstrates core Android development concepts including Material Design, activity navigation, Firebase integration, RecyclerView, input validation, and responsive UI design.

---

## 📱 Overview

Monex provides users with a simple and interactive environment where they can:

* 🔐 Sign in securely using Firebase Authentication
* 💱 Convert amounts between multiple currencies
* 👤 View and edit their profile
* 🖼️ Upload and change their profile picture
* 💬 Communicate with other users through a shared chat room
* 🌙 Use the application in light or dark mode
* 🧭 Navigate easily between application features

The application uses **Firebase Authentication**, **Cloud Firestore**, and **Firebase Storage** to provide authentication, cloud-based user data, real-time messaging, and profile image management.

---

## ✨ Features

### 🔐 User Authentication

* Firebase Authentication using email and password
* Input validation for login fields
* Error handling for invalid credentials
* Authentication-based access to application features
* Logout functionality
* Activity stack cleared after logout

### 💱 Currency Converter

The currency converter is the core feature of Monex.

Users can:

* Select a source currency
* Select a target currency
* Enter an amount
* Convert between currencies
* View the converted result
* Receive validation feedback for invalid or empty input

The application supports **26 predefined currencies**, including:

* USD — US Dollar
* EUR — Euro
* GBP — British Pound
* JPY — Japanese Yen
* AUD — Australian Dollar
* LBP — Lebanese Pound
* CAD — Canadian Dollar
* CHF — Swiss Franc
* CNY — Chinese Yuan
* INR — Indian Rupee
* And other major international currencies

> Currency conversion in the current version uses predefined exchange rates.

### 👤 User Profile

Users can view and manage their personal information.

The profile includes:

* Username
* Email
* Country
* Profile picture

Users can:

* Edit their profile information
* Change their profile picture
* Save updated information to Firebase
* View their current account information

Profile images are managed using **Firebase Storage**.

### 💬 Chat Pool

Monex includes a shared chat room where authenticated users can communicate with each other.

The chat functionality includes:

* Real-time message synchronization
* Message timestamps
* Sender identification
* Sender profile pictures
* RecyclerView-based message display
* Automatic scrolling to the latest message
* Message validation
* Firebase Firestore storage

Each message contains information such as:

```text
senderId
text
time
profileImage
timestamp
```

Messages are stored in the Firestore `chat` collection and synchronized using Firestore listeners.

### 🌙 Dark Mode

Monex supports both **light and dark themes**.

Separate resources are used for:

```text
res/values/colors.xml
res/values-night/colors.xml
```

The application automatically adapts its appearance according to the selected system theme.

---

## 🛠️ Technologies & Tools

| Technology                  | Purpose                              |
| --------------------------- | ------------------------------------ |
| **Java**                    | Android application development      |
| **Android Studio**          | Development environment              |
| **Firebase Authentication** | User authentication                  |
| **Cloud Firestore**         | User data and chat messages          |
| **Firebase Storage**        | Profile image storage                |
| **Material Design**         | UI components and styling            |
| **ConstraintLayout**        | Responsive layouts                   |
| **RecyclerView**            | Dynamic lists and chat messages      |
| **Spinner**                 | Currency selection                   |
| **Intent**                  | Activity navigation and data passing |
| **SharedPreferences**       | Local data persistence               |
| **Glide**                   | Image loading                        |
| **XML**                     | Android UI layouts                   |

---

## 🏗️ Application Structure

The application is divided into several main activities:

```text
Monex
│
├── Login Activity
│   └── Firebase Authentication
│
├── Currency Converter Activity
│   ├── Currency Selection
│   ├── Amount Input
│   ├── Conversion
│   └── Navigation Drawer
│
├── Profile Activity
│   ├── User Information
│   ├── Profile Image
│   └── Edit Profile
│
├── Edit Profile Activity
│   ├── Username
│   ├── Email
│   ├── Country
│   └── Save Changes
│
└── Chat Pool Activity
    ├── RecyclerView
    ├── Message Input
    ├── Send Message
    └── Firestore Synchronization
```

---

## 🔥 Firebase Integration

Monex uses Firebase for cloud-based application functionality.

### Firebase Authentication

Firebase Authentication manages:

* User login
* Email/password authentication
* User identification
* Logout

### Cloud Firestore

Firestore is used for:

* User profile information
* Chat messages
* Real-time synchronization

Chat messages are stored in the:

```text
chat
```

collection.

### Firebase Storage

Firebase Storage is used to store user profile images.

The application retrieves the stored image URL and displays the image in the user's profile and chat messages.

---

## 🧭 Navigation

The main Currency Converter screen provides a navigation drawer with access to:

```text
Profile
Chat Pool
Logout
```

Navigation between activities is implemented using explicit Android **Intents**.

Logout clears the current activity stack and returns the user to the Login screen.

---

## 🎨 UI & UX

Monex follows Android and Material Design principles to provide a consistent user experience.

The application uses:

* Material TextInputLayout
* TextInputEditText
* AppCompat Toolbar
* Material components
* ConstraintLayout
* DrawerLayout
* NavigationView
* RecyclerView
* ScrollView / NestedScrollView
* Responsive spacing and layouts
* Light and dark themes

Input validation and feedback are provided through:

* Toast messages
* TextInputLayout errors
* Input type restrictions
* Empty-field validation

---

## 📲 Main Screens

### Login

Provides secure access to registered users through Firebase Authentication.

### Currency Converter

Allows users to select currencies, enter an amount, and calculate the converted value.

### Profile

Displays the user's username, email, country, and profile picture.

### Edit Profile

Allows users to update their personal information and save changes to Firebase.

### Chat Pool

Provides a shared real-time communication space for authenticated users.

---

## ⚙️ Installation & Setup

### Prerequisites

Before running Monex, install:

* Android Studio
* Android SDK
* Java / JDK compatible with the project's Android Gradle configuration
* A Firebase account
* An Android emulator or physical Android device

### 1. Clone the repository

```bash
git clone YOUR_REPOSITORY_URL
```

Navigate into the project:

```bash
cd Monex
```

### 2. Open the project

Open the project using **Android Studio**.

Allow Gradle to synchronize and download the required dependencies.

### 3. Configure Firebase

Create a Firebase project and connect the Android application to it.

Enable:

* Firebase Authentication
* Cloud Firestore
* Firebase Storage

Add the Firebase configuration file:

```text
google-services.json
```

to the appropriate Android app module directory.

> Do not commit sensitive Firebase configuration or credentials if your project configuration contains secrets.

### 4. Run the application

Connect an Android device or start an Android emulator.

Then click:

```text
Run ▶
```

in Android Studio.

---

## 🔑 Test Users

The application includes Firebase users for testing.

| Email             | Password     |
| ----------------- | ------------ |
| `user1@gmail.com` | `useruser`   |
| `user2@gmail.com` | `user2user2` |

> These are development/testing credentials. They should not be used for production deployments.

---

## 🧪 Input Validation

Monex validates user input in several parts of the application.

### Login

* Email cannot be empty
* Password cannot be empty
* Invalid credentials produce an error message

### Currency Converter

* Amount cannot be empty
* Only numeric/decimal input is accepted
* Invalid values are handled before conversion

### Chat

* Empty messages cannot be submitted
* Messages are validated before being stored in Firestore

### Profile

* Profile information is validated before being saved

---

## 🧩 Challenges & Solutions

### Responsive UI

Different Android devices have different screen sizes and densities.

**Solution:**

* ConstraintLayout
* ScrollView / NestedScrollView
* Proper constraints
* Responsive spacing
* Testing across different configurations

### Firebase Integration

Authentication, Firestore synchronization, and profile image storage required careful handling of asynchronous operations and network-related issues.

**Solution:**

Firebase Authentication, Firestore listeners, and Firebase Storage were integrated with appropriate error handling and asynchronous operations.

### RecyclerView

The chat interface required efficient handling of dynamically changing messages.

**Solution:**

A RecyclerView Adapter and ViewHolder pattern were used to efficiently display messages and update the interface when new messages arrive.

### Activity Navigation

Managing navigation between Login, Converter, Profile, Edit Profile, and Chat activities required careful back-stack handling.

**Solution:**

Explicit Intents and appropriate activity flags were used to provide predictable navigation behavior.

### Image Handling

Selecting and displaying profile images required handling Android media access and Firebase Storage.

**Solution:**

Profile images are selected from the device and uploaded to Firebase Storage. Glide is used to efficiently load images into the application.

---



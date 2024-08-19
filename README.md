Final Project - E-commerce Application
Overview
This project is a comprehensive e-commerce application designed for Android, utilizing Firebase for backend services. The application supports user authentication, product browsing, cart management, wishlist functionalities, and a user profile section. Additionally, it features background music functionality to enhance the user experience.

Features

Splash Screen: Animated introduction to the app.
User Authentication (Email, Phone, Google)
Browse Products by Category
Search Functionality
Add to Cart and Wishlist
View and Manage User Profile
Persistent Cart and Wishlist using Firebase Realtime Database
Background Music with Sound Control
Banner Slider for Promotions

Technologies Used

Android SDK
Firebase Auth: User Authentication
Firebase Realtime Database: Data Storage
Glide: Image Loading
RecyclerView: List and Grid Layouts
ViewPager2: Banner Slider
Data Binding: Efficient UI Updates
SoundPlayer: Background Music Management

Installation
Clone the repository:

git clone https://github.com/EliorMauda/Final_Project.git
cd finalproject
Open the project in Android Studio.
Sync the project with Gradle files.

Usage
Sign In: Users can sign in using email, phone, or Google authentication.

Browse Products: Users can browse products by categories and view popular items.

Search: Use the search functionality to find specific products.

Add to Cart/Wishlist: Products can be added to the cart or wishlist for future purchases.

Profile Management: Users can view and manage their profile information.

Background Music: Users can toggle background music on and off.



Firebase Setup
Create a Firebase project in the Firebase Console.
Add your Android app to the Firebase project.
Download the google-services.json file and place it in the app/ directory.
Enable Firebase Authentication for Email, Phone, and Google sign-in methods.
Configure Firebase Realtime Database with the necessary rules and structure.


Sound Player Integration
The SoundPlayer utility class is used to manage background music in the application.
Usage

To play music:
SoundPlayer.getInstance(context).playSound(R.raw.backgroundmusic);

To stop music:
SoundPlayer.getInstance(context).stopSound();

MVVM Architecture
The project follows the MVVM architecture pattern to ensure a clear separation of concerns and facilitate easier maintenance and testing.

Main Components
View: Activities
ViewModel: MainViewModel, ProfileViewModel, etc.
Model: Data classes and Firebase Repositories

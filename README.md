# drawmatic
A handy drawing and guessing party game for everyone.
Players can easily play with their friends quickly on the phone, where player prgress will be timed and stored on the cloud automatically.
<br /><br />[<img src="https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png" width="200">](https://play.google.com/store/apps/details?id=com.justinlee.drawmatic)


# Features

  * Authentication
    * Firebase Authencaon that allows users to login anonymously to play games quickly 
    * Developed based on MVP architectural paern for easier maintenance and future expandability
    * Interfaces for callbacks to allow clear and flexible programming style

  * Main Menu
    * Gaming main page for players to either create a new room for others to join or join an existing room created be other players
    * Instructions Page with 2 layers of recyclerview that allows beginners to learn how to play the game
    * Settings Page for App information and player renaming
    
  * Joining Games
    * Creator of a game can set up rules for the game i.e. the time allowed for each step and maximum players allowed
    * Room and player information are updated to Firebase Firestore in realtime
    * Partial search is allowed when searching for rooms to join
    * Searched rooms are shown in recyclerview withlinear layout
    * After joining rooms, players in the room is shown in recyclerview with grid layout 
    
  * In games
    * Each step in the game will be timed with coundown timer to keep everyone on the same page
    * After each step, data (guessing and drawing) will be uploaded to Firebase Firestore or Storage, and each player will tell the server that he/she finishes the step
    * The next step will begin only when everyone finishes, and the correct data will be retrieved from Firebase asyncronouly
    * If anyone leaves the game by pressing the "Leave" button, all players will leave the game
    * If anyone leaves the game without proper procedures, all players will leave the game in 15 seconds after finishing a step
    * Player drawings are captured and drawn onto a new Bitmap with Canvas (translated to ByteArrayOutputStream when necessary)
    
  * After Games
    * Each player's topic and related guessing and drawings will be retrieved and shown with viewpager, in the order they are created
    * All rooms, players, and gaming data will be deleted after game finishes
  
# Screenshot

<img src="https://lh3.googleusercontent.com/RiEX3w4yDrY_LXWX6AQESRAsVYblIMyjbVMpaBoicdAAKQMTeXqYg11KGJBp8-uUfGo=w1440-h620-rw" width="210"> <img src="https://lh3.googleusercontent.com/wqTNySJuRX6SuIKmzc07lIjG6BFTJXDtu6VWFaZaD93ddLcx6HdLl6HreYd5XEW6qg=w1440-h620-rw" width="210"> <img src="https://lh3.googleusercontent.com/e_xNq9m-eh1qeNp64noluAykDMgWZV_tW7JPMWkNMxZy6c5W-SV2y6HrKsWBxFpfXw=w1440-h620-rw" width="210"> <img src="https://lh3.googleusercontent.com/DyDdyxPSy5qGAlazgyffzpx-v2gwNzd30q-uvZp1BhtjqdmxLApzAG3dXFvvovbVnWU=w1440-h620-rw" width="210"> 

# Implemented
  
  * Design Patterns 
    * Object-Oreinted Programming
    * Model-View-Presenter (MVP) Model
    * Dependency Injection
    
  * Core Functions
    * Firebase Authentication
    * Game syncronization
    * Interfaces and Callbacks
    * Capturing images of a view
    
  * User Interface
    * Activity
    * Fragment
    * RecyclerView 
    * Bottom Navigation
    * ViewPager
    * Dialog
    
  * Database
    * Firebase Firestore to store game information online
    * Firebase Storage to store images online
    
  * Analysis
    * Crashlytics 	
    
  * Unit Test
    * JUnit
    * Mockito
    * Espresso

# Requirement
* Android Studio 3.0+
* Android SDK 21+
* Gradle 3.2.1+

# Version
* 1.1.7 - 2018/11/17
    First release

  

# Contact

justinlee.archer@gmail.com 

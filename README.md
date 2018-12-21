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
    * Room and player information are updated to Firebase Firestore in realtime
    * Partial search is allowed when searching for rooms to join
    * Searched rooms are shown in recyclerview withlinear layout
    * After joining rooms, players in the room is shown in recyclerview with grid layout 
    

  * In game
    * 按字母排列所有已加入的群組
    * 個別群組頁面會列出所有群組花費與個人在群組內的帳務
    
  
  * [新增拆帳清單]
    * 選擇群組後會自動篩選群組內的成員作為可拆帳的人員
    * 有四種拆帳模式「全部均分」「部份均分」「比例分攤」「自由分配」可以選擇，也可以輸入服務費
   
  
  * [結清帳務] 
    * 如果與好友之間不是帳務結清關係雙方都會出現結清帳務按紐
    * 可以輸入當次還錢的金額
    
  
  * [快速拆帳]  
    * 可以輸入金額與人數快速算出拆帳結果，也可以在選擇拆帳模式後更換人員名稱好做區分
    * 可以一鍵清空重新輸入
    

  * [Navigation Drawer]  
    * 點擊左上方drawer icon 或側滑可以開啟 Drawer 切換不同頁面或是「登出」
  
# Screenshot

<img src="https://lh3.googleusercontent.com/RiEX3w4yDrY_LXWX6AQESRAsVYblIMyjbVMpaBoicdAAKQMTeXqYg11KGJBp8-uUfGo=w1440-h620-rw" width="210"> <img src="https://lh3.googleusercontent.com/wqTNySJuRX6SuIKmzc07lIjG6BFTJXDtu6VWFaZaD93ddLcx6HdLl6HreYd5XEW6qg=w1440-h620-rw" width="210"> <img src="https://lh3.googleusercontent.com/e_xNq9m-eh1qeNp64noluAykDMgWZV_tW7JPMWkNMxZy6c5W-SV2y6HrKsWBxFpfXw=w1440-h620-rw" width="210"> <img src="https://lh3.googleusercontent.com/DyDdyxPSy5qGAlazgyffzpx-v2gwNzd30q-uvZp1BhtjqdmxLApzAG3dXFvvovbVnWU=w1440-h620-rw" width="210"> 

# Implemented
  
  * Design Patterns 
    * Object-Oreinted Programming
    * Model-View-Presenter (MVP) 
    
  * Core Functions
    * Firebase Authentication
    * Firebase Firestore to store game information online
    * Firebase Storage to store images online
    
  * User Interface
    * Fragment 
    * RecyclerView 
    * Bottom Navigation
    * ViewPager
    * Dialog
    
  * Database
    * Firebase Cloud Firestore
    
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
* 1.0.0 - 2018/11/04
    First release

  

# Contact

justinlee.archer@gmail.com 

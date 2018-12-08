# CTube
<p align="center">
  <a href="https://github.com/makkhay/CameraTranslate2">
    <img alt="daug" src="https://github.com/makkhay/FirebaseAI-Android-Chat-App/blob/master/app/src/main/res/mipmap-hdpi/ic_launcher.png" width="250">
  </a>
</p>


# What's CTube app? 
A simple youtube client app, where you can filter videos, save videos to favorite, comment, and more... 



## Functionality
- Users can sign up using Gmail
- Login credentials are stored in firebase database for authentication. 
- Use Realm to store comment data locally. 
- Store favorited videos to firebase database
- Watch youtube Video 
- Submit comment ( only local supported) 
- Filter videos

## Demo and Screenshots

![chat_demo](https://github.com/makkhay/CTube/blob/master/Screenshots/Ctube.gif)

<div style={{display: flex; flex-direction: row}}>
  <img src="https://github.com/makkhay/CTube/blob/master/Screenshots/1.png" width="270" />
  <img src="https://github.com/makkhay/CTube/blob/master/Screenshots/2.png" width="270" />
    <img src="https://github.com/makkhay/CTube/blob/master/Screenshots/3.png" width="270" />

</div>

<div style={{display: flex; flex-direction: row}}>
  <img src="https://github.com/makkhay/CTube/blob/master/Screenshots/4.png" width="270" />
  <img src="https://github.com/makkhay/CTube/blob/master/Screenshots/5.png" width="270" />
  <img src="https://github.com/makkhay/CTube/blob/master/Screenshots/6.png" width="270" />
 
</div>

<div style={{display: flex; flex-direction: row}}>
  <img src="https://github.com/makkhay/CTube/blob/master/Screenshots/signIn.png" width="270" />
  <img src="https://github.com/makkhay/CTube/blob/master/Screenshots/database.png" width="270" />
</div>




 ## Firebase registered user database 
<div style={{display: flex; flex-direction: row}}> 
  <img src="https://github.com/makkhay/CTube/blob/master/Screenshots/signIn.png" width="790" height="400" />
</div>  

 ## Firebase real timetime favorited user database 
<div style={{display: flex; flex-direction: row}}> 
  <img src="https://github.com/makkhay/CTube/blob/master/Screenshots/database.png" width="790" height="400" />
</div> 

### TODO Part 1
- [x] Credentials must be a Username + Password combination. Username can only be 1) a valid US Phone number or 2) a valid      email address
- [x] If the user already has an account: Valid credentials à Access to the app. Invalid credentials à UI prompt indicating that the entered credentials are not valid
- [x] If the user already does not have an account à UI prompt indicating account doesn’t exist
- [x] Add a signup UX flow and logic to your app (e.g., if user doesn’t have an account, they can
create one). The signup screen should be presented with an animation.

### TODO Part 1 Extra credit
- [x] Add a forgot/reset password UX flow or logic to your app (e.g., if user entered the wrong
credentials or forgot their password, then they can reset their credentials)
- [x] Connect your app to a back-end API service that can store credentials in a database ( connected with firebase)

### TODO Part 2 
- [x] Keyboard will show up and hide based on the user’s behavior. User wants to start typing (clicks on message box) à     keyboard will show up
- [x] Use dynamic height of the chat message box (e.g., where user enters the message he wants to
send)
- [x] User can copy / paste text from the chat log into the message box
- [x] Your chatroom must be interactive.
- [ ] Allow users to post videos and photos in chat using some basic animation ( The online chat API doesn't accept anything other than a string so picture and video cannot be displayed but users can pick. Animation is also added. ) 
- [x] Add a chat shortcut button with various shortcut options and some basic animations

### TODO Part 2 Extra credit
- [x] Connect your chatroom to some back-end of your choice using SocketIO or any tool ( Connected to online chat bot API)

### TODO Part 3 
- [x] navigation bar or menu 
- [x] dashboard must be able to display 5 variety of charts

### TODO Part 3 Extra credit
- [x] Make your charts look good!








## Getting started

```
git clone https://github.com/makkhay/FirebaseAI-Android-Chat-App.git

Open it using android studio and run on the emulator 
```
```
Sign up for firebase add your google json file. 



## Feedback

In case you have any feedback or questions, feel free to open a new issues on this repo or reach out to me [**@makkhay**](https://github.com/makkhay) on Github. Thanks






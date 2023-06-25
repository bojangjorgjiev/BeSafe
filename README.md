# Be Safe! 📱
Welcome to Be Safe! Be Safe is a small android application developed for my Android class. Made with Java and Firebase. Be Safe uses Firebase Authentication to authenticate users, this app uses Phone Provider to validate user Phone with OTP, CRUD operations are done with Firebase Realtime Database, Firebase Cloud Messaging is used to share location data between the users, Google Maps Platform is used as maps interface. ✨


# How to use this? 🤔
If your want to try this app on your android phone, follow these steps: 

1. Clone this repository in your local environment

2. Create your own Firebase project <br>
```https://console.firebase.google.com/```

3. On your Firebase project add sign-in providers: <br>
```Phone```

4. Download ```google-services.json``` and place it in the app/directory (at the root of the Android Studio app module).

5. Go to ```https://console.cloud.google.com/``` and create project, to use Google Maps Platform, you must enable the APIs (I use Geolocation API), after that go to Credentials page and create credidentials (API key).

6. Create Maps Activity, add your Google Maps API key to the ```google_maps_api.xml``` file.

7. Activate developer options on your android device, connect it to your PC and run the project. It will take few minutes to install.

# How it works? 🛠
This app will help you in tracking your loved ones who are in danger and also allows you to share your location when you are in danger, to find a nearby police station around you with just one tap. You can play Police siren to scare away the intruders if you are in danger and also you can share suspicious activity photos with your friends. 

# Inspiration 💡
Project for my Android Programming Course.

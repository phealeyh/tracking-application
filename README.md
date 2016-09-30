# tracking-application
This application will allow the user to keep a track of their reminders that are based on locations. It can also track your locations with a given time interval and duration; making it a fully fledged real-time gps tracker. If you don't want to get into the hassle of compiling the code then please check the screenshots to give you a visual idea of what the application looks like.

# Installation
Install by downloading the raw app-release.apk file and installing it onto your phone. Remember to have your locations settings turned to on before using the app.

# Developer Notes:

- The map features won't work unless you use your own API Key and register it with your SHA generated from your computer.
Please head to: https://console.developers.google.com and sign up for a google maps api key. Change the key in the string found in the google_maps_api.xml file with the key you receive from google and then generate a SHA from gradle and list that SHA in the credentials page. The package name should be listed as tracking.id11723222.com.trackingapplication 

- Set your location settings to 'on' found within your phone's settings before running the application.

- When adding an reminder -- please add the address number, street name, suburb and state in that order.

Failure to do so will result in crashes and run-time exceptions.

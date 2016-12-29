# Tracking Application
This application will allow the user to keep a track of their reminders that are based on locations. It can also track your locations with a given time interval and duration; making it a fully fledged real-time gps tracker. 

# Installation
Install by downloading the raw app-release.apk file and installing it onto your phone.

Or, head to the playstore and install it to your phone by clicking on the listing:
https://play.google.com/store/apps/details?id=tracking.id11723222.com.trackingapplication

#Usage - Create Entry
Click on the options pane and click on 'Create Entry'.Fill in the location, date, time and reason text fields. Press the green add button so that it will add the entry to the SQLite database.
The entries will be viewable in the 'Show Timetable' or 'Show Timetable /w Map' activities.

#Usage - Show Timetable
Click on the Light-Blue 'Show Timetable' activity found in the home section. A list of reminders will be shown along with the location, date, time and reason given. It will also have a location button that will -- when pressed -- bring you to the location associated with the reminder. The delete button will -- when pressed -- will delete the reminder from the database.

#Usage - Show Timetable w/ Map
Click on the Red 'Show Timetable w/ Map' activity found in the home section. A map along with the first reminder (if any) will be shown with the pin dropped at the location designated by the reminder. If you click on the marker, it will display the reason why you need to be there. It will also show the reminder's information which includes the location, time and reason for being there. It also includes a back and next button which will allow you to retrieve the next/previous reminder entered.

#Usage - Start Tracking
Click on the Green 'Start Tracking' activity found on the home page. A new tracking page will appear that will display the tracker's current activity (INACTIVE/ACTIVE) and will show how frequently it is updating it's location and for how long. It will include the 'start tracking' and 'reset values' buttons that will start the GPS tracker and remove all the listed locations, respectively.

Once the tracker is active, a timer (chronometer) will activate and count upwards. It will show how long the tracker has been active for and will keep going until the timer hit the maximum amount of time it needs to be on for.

Finally, the 'Email Locations' button will advance you to a new page that will fill in the email's body with the locations logged. You will need to fill in the 'To:' body so that it will send these contents to a particular recipient.

Note: Once the GPS tracker has been active, it will only stop logging your positions until it either:
- Hits the maximum amount of seconds it needs to record for
- The process/application is killed.

#Usage - Go To User Location
Click on the Blue 'Go To User Location' section found in the home page. It will give you a map that will show your current location along with the marker placed on top of it. If the marker is clicked, it will display the postal address associated with that location. If clicked again, then it will display the latitude/longitude coordinates associated with that location. It will switch back and forth if clicked repeatedtly.

# Developer Notes:

- If you want to use your own API key then please head to: https://console.developers.google.com and sign up for a google maps api key. Change the key in the string found in the google_maps_api.xml file with the key you receive from google and then generate a SHA from gradle and list that SHA in the credentials page. The package name should be listed as tracking.id11723222.com.trackingapplication 

# Privacy Policy:
- This mobile application will collect personal data in the form of your geographical location, email, cellular network information and making sure your account is synced. It will use this information in order to display where you are and record that information into the application itself. In regards to the email, when you decide to email your recorded geographical locations then it will preload your email into the 'from' field so that you won't have to type it in yourself. GET_ACCOUNT is to verify if user synced Google account in mobile, and generate the key value for each user(each Google account). This is required if the device is running a version lower than Android 4.0.4. And lastly, READ_PHONE_STATE is used to retrieve information about your cellular network (and check if it is enabled) so that it can retrieve information on where you are.

These are the only information that are collected within the mobile application and they will not be disclosed to any external server.

By installing this application, you will be granting and acknowleding the application's use of your geographical location and email.


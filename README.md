# MoveMedical tech assignment application #
 ![](movemedical.gif) 

## User stories ##

- I would like to create appointments with a date, time, location, and description. The location should be a dropdown/select with the following options: San Diego, St. George, Park City, Dallas, Memphis, and Orlando. Appointments can only be scheduled in future, and previous dates are constrainted.
- I would like to see a list of my appointments
- I would like to edit my appointments. Editing is done by swiping the appointment to the left
- I would like to be able to cancel (delete) an appointment. Removing is done by swiping the appointment to the right
- I would like the user interface to be simple yet elegant (i.e., has some quick, light styling)


## Tech Stack ##

The app is written with Model-View-ViewModel and Kotlin as a programming language of choice. DI is handled with a Dagger Hilt and concurrency solution uses Coroutines and database calls are handled on IO thread.
The code is structured in packages by feature.
All images and either vectors or webp for additional lossless quality compression

## Extra features ##
* Dark Mode -> App works in dark mode when changed from settings
* Pressing on location shows Google Maps

## Info ##
* Tested on Android 9 and 13
* .apk included for faster install

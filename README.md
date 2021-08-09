# FeverWeather

## APP DESCRIPTION

### Data
To implement this layer I'm using Retrofit and Room libraries to get the data from the API and save it in a database.

#### Get data from API (Retrofit)
The method 'getCurrentWeather' from 'WeatherAPI' get the response from the endpoint:

https://api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={API key}

For this call I added the 'units' parameter to change the units of measurement for the data.
The response is serialized by GSon and in the 'NetworkDataSource' map to transform in a simple class ('Weather') to use in database and in the view.

#### Save data in Database (Room)
The database is created with the name 'fever' and only one entity 'weather'.
The methods for the SQL queries are to get, delete and insert the weather.

I have created other functions like 'deleteAndInsert' to delete the old weather and add the new one.
And 'getCount' to help me in the testing.

#### Repository
The repository 'WeatherRepository' only has a public function 'getCurrentWeather' to return the weather or an error.
To obtain the data from network or database, this class has a data class to set all the needed params (lat, lon, forceRefresh, units), using the param 'forceRefresh' to decide if the data is get from network or database.

Then we have 2 cases:
 - 'forceRefresh' = false : Try to get the data from the database. If the response is 'null', because the database is empty, it do a call to the API to get the data from network (This case is normal when the app is open by the first time).
 - 'forceRefresh' = true : Try to get the data from API network. It handle the errors, and if it detect one then it calls to the local database to try to get the data.

For everytime the app call to the API, the correct response is saved in database (removing the old data and inserting the new).

#### Models
Exist a data class for any data from the response of get current weather API call.

There are:

CurrentWeather - Main - Sys - Clouds - Coord - WeatherItem - Wind

Being 'CurrentWeather' the main data class.

### DI
The dependency injection is managed by Dagger Hilt, for it was created a module ('AppModule') to set all the Provides needed to inject all the data from network and database.
An Application was created too, the fragment and viewmodel are Hilt components, and the Repository class is set like a Singleton.

### ViewModel
To create the ViemModel structure I'm using a base to handle Events and States and do the view binding.
It was created one for Activities and other for Fragments, the only difference is that the Activity are not using the States.
But at the final, I'm only using this ViewModel structure at the fragment, because the Activity is only to host the fragment and implements the option menu.

#### WeatherFragmentViewModel
This ViewModel is used to get the weather (by Network or Database) using the injected Repository.
By default the State is Loading and with the correct response or an error the State changes to Loaded (with null or the weather data).
If the call return an error, the State changes to Loaded but it's sended an Event to show an error message (OnShowError).
At this ViewModel is implemented the private function to create the parameters to send to the repository, with the functions to get the random Longitude and Latitude (I found this implementation on Internet, sincerely, I don't know if it's the best option to get it).

#### States
At the sealed class 'WeatherViewState' is implemented the 2 States.

· Loading -> To show the loading view and hide the data from the UI.

· Loaded  -> With the weather data mapped from Network or get in the database.

### View
The application only has an Activity and a Fragment.
Exist a navigation component but is very simple.

#### Activity
The activity host the fragment and has an option menu to select the 'units' for the weather data.
It is saved in SharedPreferences because it's only a string.

#### Fragment
The 'WeatherFragment' handle the States and Events, and changes the UI data.
All the data changes are implemented in the fragment to show the data, for example the loading component or the data content for the weather.
For the weather icons I'm using a special font, I think is faster and lighter than images. A lot of functionality is at the 'Extension' file to do more reusable and easy to change at the future.

#### Design
I tried to do the design very clean and clear, only selecting the most important weather data to show (saved in the 'Weather' object) and with only a button at the bottom to refresh. And at the top right exist and option menu to select the 'unit'.

![alt text](https://github.com/E7-Company/FeverWeather/blob/main/Screenshot_20210809_205858.png?raw=true)

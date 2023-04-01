JStrava Extension 1.1
=======

Java Library for the Strava API

Objective
=======
From the JStrava branch:  
The objective of the project is to provide a Java wrapper which mirrors as closely as possible the Strava API in it's latest version.

In my branch:  
This project extends the JStrava project to make it easier to manage the first connection and the following.

StravaFirstConfigurationDialog  
This JDialog allows to easily initialise the Strava access token and refresh token. It saves and encode the values in Base64 for being used later.

StravaConfigurationDialog  
This JDialog allows to easily initialise the configuration file with access token and refresh token. It saves and encode the values in Base64 for being used later.

StravaConnection and FileIdentificationStorage  
By using these 2 objects, the access token will be managed and refreshed. With the getStrava() method, the API is accessible.


Example Usage
=======

See file: Examples.java

Required External libraries
=======

commons-net-3.0.1.jar  
gson-2.10.1.jar  
miglayout-4.0-swing.jar  

These libraries can be found in the lib directory.
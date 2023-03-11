JStrava Extension 1.0
=======

Java Libraries for the Strava API

Objective
=======
The objective of the project is to provide a Java wrapper which mirrors as closely as possible the Strava API in it's latest version.
This project extends the JStrava project to make it easier to manage the first connection and the following.
By using the StravaConnection object, it's possible:
 - to get the first token via a small Java Swing interface and the usage of the web browser
 - to keep and automatically refresh the access token needed to browse the API


Example Usage
=======

    JStravaV3 strava = new JStravaV3(accessToken);
    Athlete athlete = strava.getCurrentAthlete();
    Athlete anotherAthlete = strava.findAthlete(athleteId);


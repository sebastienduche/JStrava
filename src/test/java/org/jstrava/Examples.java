package org.jstrava;

import org.jstrava.user.FileIdentificationStorage;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public class Examples {

    public static void main(String[] args) {
        exampleThree();
    }

    private static void exampleOne() {
        // This example is to initialise the Strava tokens when you don't have any tokens yet.
        StravaFirstConfigurationDialog stravaFirstConfigurationDialog = new StravaFirstConfigurationDialog();

        if (stravaFirstConfigurationDialog.getIdentificationStorage() != null) {
            StravaConnection stravaConnection;
            try {
                stravaConnection = new StravaConnection(stravaFirstConfigurationDialog.getIdentificationStorage());
            } catch (IOException | URISyntaxException e) {
                throw new RuntimeException(e);
            }
            stravaConnection.getStrava().getCurrentAthleteActivities().forEach(System.out::println);
        }
        System.exit(0);
    }

    public static void exampleTwo() {
        // This example reads the existing settings in the file to connect to Strava
        File file = new File("test.txt");
        FileIdentificationStorage fileIdentificationStorage = new FileIdentificationStorage(file);

        StravaConnection stravaConnection;
        try {
            stravaConnection = new StravaConnection(fileIdentificationStorage);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
        stravaConnection.getStrava().getCurrentAthleteActivities().forEach(System.out::println);
        System.exit(0);
    }

    public static void exampleThree() {
        // This example allows you to set up the Strava connection by filling your existing tokens
        StravaConfigurationDialog stravaConfigurationDialog = new StravaConfigurationDialog();

        if (stravaConfigurationDialog.getIdentificationStorage() != null) {
            StravaConnection stravaConnection;
            try {
                stravaConnection = new StravaConnection(stravaConfigurationDialog.getIdentificationStorage());
            } catch (IOException | URISyntaxException e) {
                throw new RuntimeException(e);
            }
            stravaConnection.getStrava().getCurrentAthleteActivities().forEach(System.out::println);
        }
        System.exit(0);
    }
}
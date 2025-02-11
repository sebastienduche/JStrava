package org.jstrava.api;

import org.jstrava.entities.*;

import org.jstrava.exception.StravaException;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


/**
 * Created by roberto on 12/26/13.
 */
public class JStravaV3Test {


    //todo:setup your ids before testing
    String accessToken;
    int athleteId;
    int activityId;
    int updateActivityId;
    int clubId;
    String gearId;
    long segmentId;


    @Test
    public void testFailedConnection() {
        JStravaV3 strava = new JStravaV3();
        strava.init("xxxxxxxx");
    }

    @Test
    public void testJStravaV3() throws StravaException {
        JStravaV3 strava = new JStravaV3();
        strava.init(accessToken);
        Athlete athlete = strava.getCurrentAthlete();
        System.out.println(athlete.getFirstname());
        assertNotNull(athlete);
    }

    @Test
    public void testFindAthlete() throws StravaException {
        JStravaV3 strava = new JStravaV3();
        strava.init(accessToken);

        Athlete athlete = strava.findAthlete(athleteId);
        assertNotNull(athlete);
        assertFalse(athlete.getBikes().isEmpty());
        assertFalse(athlete.getShoes().isEmpty());
        assertTrue(athlete.getClubs().isEmpty());
    }

    @Test
    public void testUpdateAthlete() throws StravaException {
        JStravaV3 strava = new JStravaV3();
        strava.init(accessToken);

        HashMap optionalParameters = new HashMap();

        double weight = 71;
        optionalParameters.put("weight", weight);
        Athlete athlete = strava.updateAthlete(optionalParameters);
        assertNotNull(athlete);
    }

    @Test
    public void testFindAthleteKOMs() throws StravaException {
        JStravaV3 strava = new JStravaV3();
        strava.init(accessToken);
        List<SegmentEffort> efforts = strava.findAthleteKOMs(athleteId);

        assertFalse(efforts.isEmpty());
        for (SegmentEffort effort : efforts) {
            System.out.println("Segment Effort KOM " + effort.toString());
        }
    }

    @Test
    public void testFindAthleteKOMsWithPagination() throws StravaException {
        JStravaV3 strava = new JStravaV3();
        strava.init(accessToken);
        List<SegmentEffort> efforts = strava.findAthleteKOMs(athleteId, 2, 1);

        assertFalse(efforts.isEmpty());
        assertTrue(efforts.size() == 1);
        for (SegmentEffort effort : efforts) {
            System.out.println("Segment Effort KOM " + effort.toString());
        }
    }

    @Test
    public void testGetCurrentAthleteFriends() throws StravaException {

        JStravaV3 strava = new JStravaV3();
        strava.init(accessToken);
        List<Athlete> athletes = strava.getCurrentAthleteFriends();
        assertFalse(athletes.isEmpty());
        for (Athlete athlete : athletes) {
            System.out.println("Current Athlete Friends " + athlete.toString());
        }
    }

    @Test
    public void testGetCurrentAthleteFriendsWithPagination() throws StravaException {
        JStravaV3 strava = new JStravaV3();
        strava.init(accessToken);
        List<Athlete> athletes = strava.getCurrentAthleteFriends(2, 1);
        assertFalse(athletes.isEmpty());
        assertTrue(athletes.size() == 1);
        for (Athlete athlete : athletes) {
            System.out.println("Current Athlete Friends " + athlete.toString());
        }
    }

    @Test
    public void testFindAthleteFriends() throws StravaException {
        JStravaV3 strava = new JStravaV3();
        strava.init(accessToken);
        List<Athlete> athletes = strava.findAthleteFriends(athleteId);
        assertFalse(athletes.isEmpty());
        for (Athlete athlete : athletes) {
            System.out.println("Athlete Friends " + athlete.toString());
        }
    }

    @Test
    public void testFindAthleteFriendsWithPagination() throws StravaException {
        JStravaV3 strava = new JStravaV3();
        strava.init(accessToken);
        List<Athlete> athletes = strava.findAthleteFriends(athleteId, 2, 1);
        assertFalse(athletes.isEmpty());
        assertTrue(athletes.size() == 1);
        for (Athlete athlete : athletes) {
            System.out.println("Athlete Friends with pagination " + athlete.toString());
        }
    }

    @Test
    public void testGetCurrentAthleteFollowers() throws StravaException {
        JStravaV3 strava = new JStravaV3();
        strava.init(accessToken);
        List<Athlete> athletes = strava.getCurrentAthleteFollowers();
        assertFalse(athletes.isEmpty());
        for (Athlete athlete : athletes) {
            System.out.println("Athlete Followers " + athlete.toString());
        }
    }

    @Test
    public void testGetCurrentAthleteFollowersWithPagination() throws StravaException {
        JStravaV3 strava = new JStravaV3();
        strava.init(accessToken);
        List<Athlete> athletes = strava.getCurrentAthleteFollowers(2, 1);
        assertTrue(athletes.size() == 1);
        assertFalse(athletes.isEmpty());
        for (Athlete athlete : athletes) {
            System.out.println("Athlete Followers " + athlete.toString());
        }
    }

    @Test
    public void testFindAthleteFollowers() throws StravaException {

        JStravaV3 strava = new JStravaV3();
        strava.init(accessToken);
        List<Athlete> athletes = strava.findAthleteFollowers(athleteId);
        assertFalse(athletes.isEmpty());
        for (Athlete athlete : athletes) {
            System.out.println("Athlete Followers " + athlete.toString());
        }
    }

    @Test
    public void testFindAthleteFollowersWithPagination() throws StravaException {
        JStravaV3 strava = new JStravaV3();
        strava.init(accessToken);
        List<Athlete> athletes = strava.findAthleteFollowers(athleteId, 2, 1);
        assertTrue(athletes.size() == 1);
        assertFalse(athletes.isEmpty());
        for (Athlete athlete : athletes) {
            System.out.println("Athlete Followers " + athlete.toString());
        }
    }

    @Test
    public void testFindAthleteBothFollowing() throws StravaException {
        JStravaV3 strava = new JStravaV3();
        strava.init(accessToken);
        List<Athlete> athletes = strava.findAthleteBothFollowing(athleteId);
        assertFalse(athletes.isEmpty());
        for (Athlete athlete : athletes) {
            System.out.println("Athletes Both Following " + athlete.toString());
        }
    }

    @Test
    public void testFindAthleteBothFollowingWithPagination() throws StravaException {
        JStravaV3 strava = new JStravaV3();
        strava.init(accessToken);
        List<Athlete> athletes = strava.findAthleteBothFollowing(athleteId, 2, 1);
        assertTrue(athletes.size() == 1);
        assertFalse(athletes.isEmpty());
        for (Athlete athlete : athletes) {
            System.out.println("Athletes Both Following " + athlete.toString());
        }
    }

    @Test
    public void testCreateAndDeleteActivity() throws StravaException {
        JStravaV3 strava = new JStravaV3();
        strava.init(accessToken);

        Activity activity = strava.createActivity("Test Manual Activity", "ride", "2014-03-14T09:00:00Z", 10);
        assertNotNull(activity);
        System.out.println("Activity Name " + activity);
        Activity activityExtra = strava.createActivity("Test Manual Activity", "ride", "2014-03-14T09:00:00Z", 10, "Testing manual creation", 100);
        assertNotNull(activityExtra);
        System.out.println("Activity Name " + activityExtra);
        strava.deleteActivity(activity.getId());
        strava.deleteActivity(activityExtra.getId());
    }

    @Test
    public void testFindActivity() throws StravaException {
        JStravaV3 strava = new JStravaV3();
        strava.init(accessToken);

        Activity activity = strava.findActivity(activityId);
        assertNotNull(activity);
        System.out.println("Activity Name " + activity);
        assertNotNull(activity.getAthlete());
        System.out.println("Athlete " + activity.getAthlete().getId());
        System.out.println("MAP" + activity.getMap().toString());

        assertFalse(activity.getSegmentEfforts().isEmpty());
        for (SegmentEffort segmentEffort : activity.getSegmentEfforts()) {
            System.out.println("Segment Effort " + segmentEffort.toString());
            System.out.println("  Segment Effort Athlete" + segmentEffort.getAthlete().getId());
            assertNotNull(segmentEffort.getSegment());
            System.out.println("        Matching Segment " + segmentEffort.getSegment().toString());
        }
    }

    @Test
    public void testUpdateActivity() throws StravaException {
        JStravaV3 strava = new JStravaV3();
        strava.init(accessToken);

        HashMap optionalParameters = new HashMap();

        double weight = 71;
        String description = "Autodromo mañanero";
        String name = "Autodromo en la mañana";
        optionalParameters.put("description", description);
        optionalParameters.put("name", name);
        Activity activity = strava.updateActivity(updateActivityId, optionalParameters);
        assertNotNull(activity);
    }

    @Test
    public void testGetCurrentAthleteActivities() throws StravaException {
        JStravaV3 strava = new JStravaV3();
        strava.init(accessToken);
        List<Activity> activities = strava.getCurrentAthleteActivities();
        assertFalse(activities.isEmpty());
        for (Activity activity : activities) {
            System.out.println("Current Athlete Activity " + activity.toString());
        }
    }

    @Test
    public void testGetCurrentAthleteActivitiesWithPagination() throws StravaException {
        JStravaV3 strava = new JStravaV3();
        strava.init(accessToken);
        List<Activity> activities = strava.getCurrentAthleteActivities(2, 1);
        assertTrue(activities.size() == 1);
        assertFalse(activities.isEmpty());
        for (Activity activity : activities) {
            System.out.println("Current Athlete Activity With Pagination " + activity.toString());
        }
    }

    @Test
    public void testGetCurrentFriendsActivities() throws StravaException {
        JStravaV3 strava = new JStravaV3();
        strava.init(accessToken);
        List<Activity> activities = strava.getCurrentFriendsActivities();
        assertFalse(activities.isEmpty());
        for (Activity activity : activities) {
            System.out.println("Friend Activity " + activity.toString());
        }
    }

    @Test
    public void testGetCurrentFriendsActivitiesWithPagination() throws StravaException {
        JStravaV3 strava = new JStravaV3();
        strava.init(accessToken);
        List<Activity> activities = strava.getCurrentFriendsActivities(2, 1);
        assertTrue(activities.size() == 1);
        assertFalse(activities.isEmpty());
        for (Activity activity : activities) {
            System.out.println("Friend Activity " + activity.toString());
        }
    }

    @Test
    public void testFindActivityLaps() throws StravaException {
        JStravaV3 strava = new JStravaV3();
        strava.init(accessToken);
        List<LapsItem> laps = strava.findActivityLaps(activityId);

        assertFalse(laps.isEmpty());

        for (LapsItem lap : laps) {
            System.out.println("Lap " + lap.toString());
        }
    }

    @Test
    public void testFindActivityComments() throws StravaException {
        JStravaV3 strava = new JStravaV3();
        strava.init(accessToken);
        List<Comment> comments = strava.findActivityComments(activityId);
        assertFalse(comments.isEmpty());
        for (Comment comment : comments) {
            System.out.println(comment.getText());
        }
    }

    @Test
    public void testFindActivityCommentsWithPagination() throws StravaException {
        JStravaV3 strava = new JStravaV3();
        strava.init(accessToken);
        List<Comment> comments = strava.findActivityComments(activityId, false, 2, 1);
        assertTrue(comments.size() == 1);
        assertFalse(comments.isEmpty());
        for (Comment comment : comments) {
            System.out.println(comment.getText());
        }
    }

    @Test
    public void testFindActivityKudos() throws StravaException {
        JStravaV3 strava = new JStravaV3();
        strava.init(accessToken);
        List<Athlete> athletes = strava.findActivityKudos(activityId);
        assertFalse(athletes.isEmpty());
        for (Athlete athlete : athletes) {
            System.out.println(athlete.toString());
        }
    }

    @Test
    public void testFindActivityKudosWithPagination() throws StravaException {
        JStravaV3 strava = new JStravaV3();
        strava.init(accessToken);
        List<Athlete> athletes = strava.findActivityKudos(activityId, 2, 1);
        assertTrue(athletes.size() == 1);
        assertFalse(athletes.isEmpty());
        for (Athlete athlete : athletes) {
            System.out.println(athlete.toString());
        }
    }

    @Test
    public void testFindClubMembers() throws StravaException {
        JStravaV3 strava = new JStravaV3();
        strava.init(accessToken);
        List<Athlete> athletes = strava.findClubMembers(clubId);
        assertFalse(athletes.isEmpty());
        for (Athlete athlete : athletes) {
            System.out.println("Club Member " + athlete.toString());
        }
    }

    @Test
    public void testFindClubMembersWithPagination() throws StravaException {
        JStravaV3 strava = new JStravaV3();
        strava.init(accessToken);
        List<Athlete> athletes = strava.findClubMembers(clubId, 2, 1);
        assertTrue(athletes.size() == 1);
        assertFalse(athletes.isEmpty());
        for (Athlete athlete : athletes) {
            System.out.println("Club Member " + athlete.toString());
        }
    }

    ////////Remove EXPECTED annotation if you point to a club you are member of.
    @Test
    public void testFindClubActivities() throws StravaException {
        JStravaV3 strava = new JStravaV3();
        strava.init(accessToken);
        List<Activity> activities = strava.findClubActivities(clubId);
        assertFalse(activities.isEmpty());
        for (Activity activity : activities) {
            System.out.println("Club Activity Name " + activity.toString());
        }
    }

    ////////Remove EXPECTED annotation if you point to a club you are member of.
    @Test
    public void testFindClubActivitiesWithPagination() throws StravaException {
        JStravaV3 strava = new JStravaV3();
        strava.init(accessToken);
        List<Activity> activities = strava.findClubActivities(clubId, 2, 1);
        assertTrue(activities.size() == 1);
        assertFalse(activities.isEmpty());
        for (Activity activity : activities) {
            System.out.println("Club Activity Name " + activity.toString());
        }
    }

    @Test
    public void testFindClub() throws StravaException {
        JStravaV3 strava = new JStravaV3();
        strava.init(accessToken);

        Club club = strava.findClub(clubId);
        assertNotNull(club);
        System.out.println("Club Name " + club);
    }

    ////////Change assert if you do have clubs
    @Test
    public void testGetCurrentAthleteClubs() throws StravaException {
        JStravaV3 strava = new JStravaV3();
        strava.init(accessToken);
        List<Club> clubs = strava.getCurrentAthleteClubs();
        assertTrue(clubs.isEmpty());
        for (Club club : clubs) {
            System.out.println("Club Name " + club.toString());
        }
    }


    @Test
    public void testFindGear() throws StravaException {
        JStravaV3 strava = new JStravaV3();
        strava.init(accessToken);

        Gear gear = strava.findGear(gearId);
        assertNotNull(gear);
        System.out.println("Gear Name " + gear);
    }

    @Test
    public void testFindSegment() throws StravaException {
        JStravaV3 strava = new JStravaV3();
        strava.init(accessToken);
        Segment segment = strava.findSegment(segmentId);
        assertNotNull(segment);

        System.out.println("SEGMENT " + segment);
    }

    @Test
    public void testFindCurrentStarredSegments() throws StravaException {
        JStravaV3 strava = new JStravaV3();
        strava.init(accessToken);
        List<Segment> segments = strava.getCurrentStarredSegment();

        assertFalse(segments.isEmpty());

        for (Segment segment : segments) {
            System.out.println("Starred Segment " + segment);
        }
    }

    @Test
    public void testFindSegmentLeaderBoard() throws StravaException {
        JStravaV3 strava = new JStravaV3();
        strava.init(accessToken);
        SegmentLeaderBoard board = strava.findSegmentLeaderBoard(segmentId);
        assertNotNull(board);
        for (EntriesItem entry : board.getEntries()) {
            System.out.println("Segment LeaderBoard " + entry.toString());
        }
    }

    @Test
    public void testFindSegmentLeaderBoardWithParameters() throws StravaException {
        JStravaV3 strava = new JStravaV3();
        strava.init(accessToken);
        HashMap optionalParameters = new HashMap();
        optionalParameters.put("gender", "F");
        optionalParameters.put("page", 1);
        optionalParameters.put("per_page", 3);
        SegmentLeaderBoard board = strava.findSegmentLeaderBoard(segmentId, optionalParameters);
        assertNotNull(board);

        assertTrue(board.getEntries().size() == 3);
    }

    @Test
    public void testFindSegmentExplorer() throws StravaException {
        JStravaV3 strava = new JStravaV3();
        strava.init(accessToken);
        double[] bounds = new double[]{37.821362, -122.505373, 37.842038, -122.465977};
        List<Segment> segments = strava.findSegments(bounds);
        assertNotNull(segments);

        for (Segment segment : segments) {
            System.out.println("Segment Explorer " + segment.toString());
            System.out.println("CLIMB CATEGORY DESCRIPTION" + segment.getClimbCategory());
        }
    }

    @Test
    public void testFindActivityStreams() throws StravaException {
        JStravaV3 strava = new JStravaV3();
        strava.init(accessToken);
        List<Stream> streams = strava.findActivityStreams(activityId, new String[]{"latlng", "time", "distance"});
        assertNotNull(streams);

        for (Stream stream : streams) {
            System.out.println("STREAM TYPE " + stream.getType());
            for (int i = 0; i < stream.getData().size(); i++) {
                System.out.println("STREAM " + stream.getData().get(i));
            }
        }
    }

    @Test
    public void testFindActivityStreamsWithResolution() throws StravaException {
        JStravaV3 strava = new JStravaV3();
        strava.init(accessToken);
        List<Stream> streams = strava.findActivityStreams(activityId, new String[]{"latlng", "time", "distance"}, "low", null);
        assertNotNull(streams);

        for (Stream stream : streams) {
            System.out.println("STREAM TYPE " + stream.getType());
            for (int i = 0; i < stream.getData().size(); i++) {
                assertEquals("low", stream.getResolution());
                System.out.println("STREAM " + stream.getData().get(i));
            }
        }
    }

    @Test
    public void testFindEffortStreams() throws StravaException {
        JStravaV3 strava = new JStravaV3();
        strava.init(accessToken);
        List<Stream> streams = strava.findEffortStreams(activityId, new String[]{"latlng", "time", "distance"});
        assertNotNull(streams);

        for (Stream stream : streams) {
            System.out.println("STREAM TYPE " + stream.getType());
            for (int i = 0; i < stream.getData().size(); i++) {
                System.out.println("STREAM " + stream.getData().get(i));
            }
        }
    }

    @Test
    public void testFindEffortStreamsWithResolution() throws StravaException {
        JStravaV3 strava = new JStravaV3();
        strava.init(accessToken);
        List<Stream> streams = strava.findEffortStreams(activityId, new String[]{"latlng", "time", "distance"}, "low", null);
        assertNotNull(streams);

        for (Stream stream : streams) {
            System.out.println("STREAM TYPE " + stream.getType());
            for (int i = 0; i < stream.getData().size(); i++) {
                assertEquals("low", stream.getResolution());
                System.out.println("STREAM " + stream.getData().get(i));
            }
        }
    }

    @Test
    public void testFindSegmentStreams() throws StravaException {
        JStravaV3 strava = new JStravaV3();
        strava.init(accessToken);
        List<Stream> streams = strava.findActivityStreams(activityId, new String[]{"latlng", "time", "distance"});
        assertNotNull(streams);

        for (Stream stream : streams) {
            System.out.println("STREAM TYPE " + stream.getType());
            for (int i = 0; i < stream.getData().size(); i++) {
                System.out.println("STREAM " + stream.getData().get(i));
            }
        }
    }

    @Test
    public void testFindSegmentStreamsWithResolution() throws StravaException {
        JStravaV3 strava = new JStravaV3();
        strava.init(accessToken);
        List<Stream> streams = strava.findActivityStreams(activityId, new String[]{"latlng", "time", "distance"}, "low", null);
        assertNotNull(streams);

        for (Stream stream : streams) {
            System.out.println("STREAM TYPE " + stream.getType());
            for (int i = 0; i < stream.getData().size(); i++) {
                assertEquals("low", stream.getResolution());
                System.out.println("STREAM " + stream.getData().get(i));
            }
        }
    }
}

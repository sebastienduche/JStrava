package org.jstrava.api;


import java.io.File;
import java.util.HashMap;
import java.util.List;

import org.jstrava.entities.*;
import org.jstrava.exception.StravaException;

/**
 * Created by roberto on 12/26/13.
 */
public interface JStrava {

    Athlete getCurrentAthlete() throws StravaException;
    Athlete updateAthlete(HashMap optionalParameters) throws StravaException;
    Athlete findAthlete(int id) throws StravaException;
    List<SegmentEffort> findAthleteKOMs(int athleteId) throws StravaException;
    List<SegmentEffort> findAthleteKOMs(int athleteId, int page, int per_page) throws StravaException;
    List<Athlete> getCurrentAthleteFriends() throws StravaException;
    List<Athlete> getCurrentAthleteFriends(int page, int per_page) throws StravaException;
    List<Athlete> findAthleteFriends(int id) throws StravaException;
    List<Athlete> findAthleteFriends(int id, int page, int per_page) throws StravaException;
    List<Athlete> getCurrentAthleteFollowers() throws StravaException;
    List<Athlete> getCurrentAthleteFollowers(int page, int per_page) throws StravaException;
    List<Athlete> findAthleteFollowers(int id) throws StravaException;
    List<Athlete> findAthleteFollowers(int id, int page, int per_page) throws StravaException;
    List<Athlete> findAthleteBothFollowing(int id) throws StravaException;
    List<Athlete> findAthleteBothFollowing(int id, int page, int per_page) throws StravaException;
    Activity createActivity(String name, String type, String start_date_local, int elapsed_time) throws StravaException;
    Activity createActivity(String name, String type, String start_date_local, int elapsed_time, String description, float distance) throws StravaException;
    void deleteActivity(long activityId) throws StravaException;
    Activity findActivity(long id) throws StravaException;
    Activity findActivity(long id, boolean include_all_efforts) throws StravaException;
    Activity updateActivity(long activityId, HashMap optionalParameters) throws StravaException;
    List<Activity> getCurrentAthleteActivitiesAll() throws StravaException;
    List<Activity> getCurrentAthleteActivities() throws StravaException;
    List<Route> getCurrentAthleteRoutes() throws StravaException;
    List<Activity> getCurrentAthleteActivities(int page, int per_page) throws StravaException;
    List<Activity> getCurrentAthleteActivitiesBeforeDate(long before) throws StravaException;
    List<Activity> getCurrentAthleteActivitiesAfterDate(long after) throws StravaException;
    List<Activity> getCurrentFriendsActivities() throws StravaException;
    List<Activity> getCurrentFriendsActivities(int page, int per_page) throws StravaException;
    List<ActivityZone> getActivityZones(long activityId) throws StravaException;
    List<LapsItem> findActivityLaps(long activityId) throws StravaException;
    List<Comment> findActivityComments(long activityId) throws StravaException;
    List<Comment> findActivityComments(long activityId, boolean markdown, int page, int per_page) throws StravaException;
    List<Athlete> findActivityKudos(long activityId) throws StravaException;
    List<Athlete> findActivityKudos(long activityId, int page, int per_page) throws StravaException;
    List<Athlete> findClubMembers(int clubId) throws StravaException;
    List<Athlete> findClubMembers(int clubId, int page, int per_page) throws StravaException;
    List<Activity> findClubActivities(int clubId) throws StravaException;
    List<Activity> findClubActivities(int clubId, int page, int per_page) throws StravaException;
    Club findClub(int id) throws StravaException;
    List<Club> getCurrentAthleteClubs() throws StravaException;
    Gear findGear(String id) throws StravaException;

    Route findRoute(long routeId) throws StravaException;
    String getRouteAsGPX(long routeId) throws StravaException;
    /*
    Strava API doesn't support the export_gpx path for activities
    You need to open this url in the browser to have the gpx file download.
    You need to be logged in to your Strava account
     */
    String getActivityAsGPX(long activityId);

    List<Route> findAthleteRoutes(int athleteId) throws StravaException;

    Segment findSegment(long segmentId) throws StravaException;
    List<Segment> getCurrentStarredSegment() throws StravaException;
    SegmentLeaderBoard findSegmentLeaderBoard(long segmentId) throws StravaException;
    SegmentLeaderBoard findSegmentLeaderBoard(long segmentId, int page, int per_page) throws StravaException;
    SegmentLeaderBoard findSegmentLeaderBoard(long segmentId, HashMap optionalParameters) throws StravaException;
    List<Segment>findSegments(double[] bounds) throws StravaException;
    List<Segment>findSegments(double[] bounds, HashMap optionalParameters) throws StravaException;
    SegmentEffort findSegmentEffort(int id) throws StravaException;
    List<Stream>findActivityStreams(long activityId, String[] types) throws StravaException;
    List<Stream>findActivityStreams(long activityId, String[] types, String resolution, String series_type) throws StravaException;
    List<Stream>findEffortStreams(int id, String[] types) throws StravaException;
    List<Stream>findEffortStreams(long activityId, String[] types, String resolution, String series_type) throws StravaException;
    List<Stream>findSegmentStreams(long activityId, String[] types) throws StravaException;
    List<Stream>findSegmentStreams(long activityId, String[] types, String resolution, String series_type) throws StravaException;
    UploadStatus uploadActivity(String data_type, File file) throws StravaException;
    UploadStatus uploadActivity(String activity_type, String name, String description, int is_private, int trainer, String data_type, String external_id, File file);
    UploadStatus checkUploadStatus(int uploadId) throws StravaException;

}

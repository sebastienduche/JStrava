package org.jstrava.api;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.jstrava.exception.GenericStravaException;
import org.jstrava.exception.StravaException;
import org.jstrava.entities.Activity;
import org.jstrava.entities.ActivityZone;
import org.jstrava.entities.Athlete;
import org.jstrava.entities.Club;
import org.jstrava.entities.Comment;
import org.jstrava.entities.Gear;
import org.jstrava.entities.LapsItem;
import org.jstrava.entities.Route;
import org.jstrava.entities.Segment;
import org.jstrava.entities.SegmentEffort;
import org.jstrava.entities.SegmentLeaderBoard;
import org.jstrava.entities.Stream;
import org.jstrava.entities.UploadStatus;
import org.jstrava.exception.StravaRequestException;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

public class JStravaV3 implements JStrava {

    private static final String BASE_URL = "https://www.strava.com/api/v3";
    private static final String ACTIVITIES = "/activities";
    private static final String ATHLETE_URL = BASE_URL + "/athlete";
    private static final String ATHLETES_URL = BASE_URL + "/athletes";
    private static final String ATHLETE_ACTIVITIES_URL = ATHLETE_URL + ACTIVITIES;
    private static final String ROUTES = "/routes";
    private static final String ATHLETE_ROUTES_URL = ATHLETE_URL + ROUTES;
    private static final String ATHLETE_CLUBS_URL = ATHLETE_URL + "/clubs";
    private static final String ACTIVITIES_URL = BASE_URL + ACTIVITIES;
    private static final String CLUBS_URL = BASE_URL + "/clubs";
    private static final String SEGMENTS_URL = BASE_URL + "/segments";
    private static final String FOLLOWERS = "/followers";
    private static final String ATHLETE_FOLLOWERS_URL = ATHLETE_URL + FOLLOWERS;
    private static final String FRIENDS = "/friends";
    private static final String ATHLETE_FRIENDS_URL =  ATHLETE_URL + FRIENDS;
    private static final String ROUTE_URL = BASE_URL + "/route";
    private static final String GEAR_URL = BASE_URL + "/gear";
    private static final String KOMS = "/koms";
    private static final String PARAMETER_PAGE = "?page=";
    private static final String PARAMETER_PER_PAGE = "&per_page=";
    private static final String LEADERBOARD = "/leaderboard";
    private static final String BOTH_FOLLOWING = "/both-following";
    private static final String PARAMETER_NAME = "?name=";
    private static final String PARAMETER_TYPE = "&type=";
    private static final String PARAMETER_START_DATE_LOCAL = "&start_date_local=";
    private static final String PARAMETER_ELAPSED_TIME = "&elapsed_time=";
    private static final String PARAMETER_DESCRIPTION = "&description=";
    private static final String PARAMETER_DISTANCE = "&distance=";
    private static final String PARAMETER_INCLUDE_ALL_EFFORTS = "?include_all_efforts=";
    private static final String PARAMETER_BEFORE = "?before=";
    private static final String PARAMETER_AFTER = "?after=";
    private static final String FOLLOWING = "/following";
    private static final String ZONES = "/zones";
    private static final String LAPS = "/laps";
    private static final String COMMENTS = "/comments";
    private static final String PARAMETER_AND_PAGE = "&page=";
    private static final String PARAMETER_MARKDOWN = "?markdown=";
    private static final String KUDOS = "/kudos";
    private static final String MEMBERS = "/members";
    private static final String EXPORT_GPX = "/export_gpx";
    private static final String STARRED = "/starred";
    private static final String EXPLORE_BOUNDS = "/explore?bounds=";
    private static final String SEGMENT_EFFORTS = "/segment_efforts/";
    private static final String STREAMS = "/streams/";
    private static final String PARAMETER_RESOLUTION = "?resolution=";
    private static final String PARAMETER_SERIES_TYPE = "&series_type=";
    private static final String UPLOADS = "/uploads";

    private String accessToken;
    private String refreshToken;
    private Athlete currentAthlete;

    private String latestGetURL;
    private boolean withBearer;
    private boolean jsonFormat;
    private final Gson gson = new Gson();

    private boolean init = false;
    public void init(String accessToken) {
        init = true;
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    @Override
    public Athlete getCurrentAthlete() throws StravaException {
        if (currentAthlete == null) {
            String result = getResult(ATHLETE_URL);
            currentAthlete = fromJson(result, Athlete.class);
        }
        return currentAthlete;
    }

    @Override
    public Athlete updateAthlete(HashMap optionalParameters) throws StravaException {
        String result = putResult(ATHLETE_URL, optionalParameters);
        return fromJson(result, Athlete.class);
    }

    @Override
    public Athlete findAthlete(int id) throws StravaException {
        String URL = ATHLETE_URL + "/" + id;
        String result = getResult(URL);

        return fromJson(result, Athlete.class);
    }

    @Override
    public List<SegmentEffort> findAthleteKOMs(int athleteId) throws StravaException {
        String URL = ATHLETE_URL + "/" + athleteId + KOMS;
        String result = getResult(URL);

        SegmentEffort[] segmentEffortArray = fromJson(result, SegmentEffort[].class);
        return Arrays.asList(segmentEffortArray);
    }

    @Override
    public List<SegmentEffort> findAthleteKOMs(int athleteId, int page, int per_page) throws StravaException {
        String URL = ATHLETE_URL + "/" + athleteId + KOMS + PARAMETER_PAGE + page + PARAMETER_PER_PAGE + per_page;
        String result = getResult(URL);

        SegmentEffort[] segmentEffortArray = fromJson(result, SegmentEffort[].class);
        return Arrays.asList(segmentEffortArray);
    }

    @Override
    public List<Athlete> getCurrentAthleteFriends() throws StravaException {
        String result = getResult(ATHLETE_FRIENDS_URL);

        Athlete[] athletesArray = fromJson(result, Athlete[].class);
        return Arrays.asList(athletesArray);
    }

    @Override
    public List<Athlete> getCurrentAthleteFriends(int page, int per_page) throws StravaException {
        String URL = ATHLETE_FRIENDS_URL + PARAMETER_PAGE + page + PARAMETER_PER_PAGE + per_page;
        String result = getResult(URL);

        Athlete[] athletesArray = fromJson(result, Athlete[].class);
        return Arrays.asList(athletesArray);
    }

    @Override
    public List<Athlete> findAthleteFriends(int id) throws StravaException {
        String URL = ATHLETE_URL + "/" + id + FRIENDS;
        String result = getResult(URL);

        Athlete[] athletesArray = fromJson(result, Athlete[].class);
        return Arrays.asList(athletesArray);
    }

    @Override
    public List<Athlete> findAthleteFriends(int id, int page, int per_page) throws StravaException {
        String URL = ATHLETE_URL + "/" + id + FRIENDS + PARAMETER_PAGE + page + PARAMETER_PER_PAGE + per_page;
        String result = getResult(URL);

        Athlete[] athletesArray = fromJson(result, Athlete[].class);
        return Arrays.asList(athletesArray);
    }

    @Override
    public List<Athlete> getCurrentAthleteFollowers() throws StravaException {
        String result = getResult(ATHLETE_FOLLOWERS_URL);

        Athlete[] athletesArray = fromJson(result, Athlete[].class);
        return Arrays.asList(athletesArray);
    }

    @Override
    public List<Athlete> getCurrentAthleteFollowers(int page, int per_page) throws StravaException {
        String URL = ATHLETE_FOLLOWERS_URL + PARAMETER_PAGE + page + PARAMETER_PER_PAGE + per_page;
        String result = getResult(URL);

        Athlete[] athletesArray = fromJson(result, Athlete[].class);
        return Arrays.asList(athletesArray);
    }

    @Override
    public List<Athlete> findAthleteFollowers(int id) throws StravaException {
        String URL = ATHLETES_URL + "/" + id + FOLLOWERS;
        String result = getResult(URL);

        Athlete[] athletesArray = fromJson(result, Athlete[].class);

        return Arrays.asList(athletesArray);
    }

    @Override
    public List<Athlete> findAthleteFollowers(int id, int page, int per_page) throws StravaException {
        String URL = ATHLETES_URL + "/" + id + FOLLOWERS + PARAMETER_PAGE + page + PARAMETER_PER_PAGE + per_page;
        String result = getResult(URL);

        Athlete[] athletesArray = fromJson(result, Athlete[].class);
        return Arrays.asList(athletesArray);
    }

    @Override
    public List<Athlete> findAthleteBothFollowing(int id) throws StravaException {
        String URL = ATHLETES_URL + "/" + id + BOTH_FOLLOWING;
        String result = getResult(URL);

        Athlete[] athletesArray = fromJson(result, Athlete[].class);
        return Arrays.asList(athletesArray);
    }

    @Override
    public List<Athlete> findAthleteBothFollowing(int id, int page, int per_page) throws StravaException {
        String URL = ATHLETES_URL + "/" + id + BOTH_FOLLOWING + PARAMETER_PAGE + page + PARAMETER_PER_PAGE + per_page;
        String result = getResult(URL);

        Athlete[] athletesArray = fromJson(result, Athlete[].class);
        return Arrays.asList(athletesArray);
    }

    @Override
    public Activity createActivity(String name, String type, String start_date_local, int elapsed_time) throws StravaException {
        String URL = ACTIVITIES_URL + PARAMETER_NAME + name + PARAMETER_TYPE + type + PARAMETER_START_DATE_LOCAL + start_date_local + PARAMETER_ELAPSED_TIME + elapsed_time;
        String result = postResult(URL);

        return fromJson(result, Activity.class);
    }

    @Override
    public Activity createActivity(String name, String type, String start_date_local, int elapsed_time, String description, float distance) throws StravaException {
        String URL = ACTIVITIES_URL + PARAMETER_NAME + name + PARAMETER_TYPE + type + PARAMETER_START_DATE_LOCAL + start_date_local + PARAMETER_ELAPSED_TIME + elapsed_time + PARAMETER_DESCRIPTION + description + PARAMETER_DISTANCE + distance;
        String result = postResult(URL);

        return fromJson(result, Activity.class);
    }

    @Override
    public void deleteActivity(long activityId) throws StravaException {
        String URL = ACTIVITIES_URL + "/" + activityId;
        String result = deleteResult(URL);

        fromJson(result, String.class);
    }

    @Override
    public Activity findActivity(long id) throws StravaException {
        String URL = ACTIVITIES_URL + "/" + id;
        String result = getResult(URL);

        return fromJson(result, Activity.class);
    }

    private <T> T fromJson(String json, Class<T> classOfT) throws StravaException {
        if (json == null || json.isEmpty()) {
            throw new GenericStravaException("Error: " + (json == null ? "json is null" : "json is empty"), null);
        }
        try {
        return gson.fromJson(json, classOfT);
        } catch (JsonSyntaxException e) {
            throw new GenericStravaException("Error with json:\n" + json, e);
        }
    }

    @Override
    public Activity findActivity(long id, boolean include_all_efforts) throws StravaException {
        String URL = ACTIVITIES_URL + "/" + id + PARAMETER_INCLUDE_ALL_EFFORTS + include_all_efforts;
        String result = getResult(URL);

        return fromJson(result, Activity.class);
    }

    @Override
    public Activity updateActivity(long activityId, HashMap optionalParameters) throws StravaException {
        String URL = ACTIVITIES_URL + "/" + activityId;
        String result = putResult(URL, optionalParameters);

        return fromJson(result, Activity.class);
    }

    @Override
    public List<Activity> getCurrentAthleteActivitiesAll() throws StravaException {
        int resultsPerPage = 100;
        int page = 1;
        List<Activity> currentActivities = new ArrayList<>();
        List<Activity> activitiesPerPage;

        while (!(activitiesPerPage = this.getCurrentAthleteActivities(page, resultsPerPage)).isEmpty()) {
            currentActivities.addAll(activitiesPerPage);
            page++;
        }
        return currentActivities;
    }

    @Override
    public List<Activity> getCurrentAthleteActivities() throws StravaException {
        String result = getResult(ATHLETE_ACTIVITIES_URL);

        Activity[] activitiesArray = fromJson(result, Activity[].class);
        return Arrays.asList(activitiesArray);
    }

    @Override
    public List<Route> getCurrentAthleteRoutes() throws StravaException {
        String result = getResult(ATHLETE_ROUTES_URL);

        Route[] routesArray = fromJson(result, Route[].class);
        return Arrays.asList(routesArray);
    }

    @Override
    public List<Activity> getCurrentAthleteActivities(int page, int per_page) throws StravaException {
        String URL = ATHLETE_ACTIVITIES_URL + PARAMETER_PAGE + page + PARAMETER_PER_PAGE + per_page;
        String result = getResult(URL);

        Activity[] activitiesArray = fromJson(result, Activity[].class);
        return Arrays.asList(activitiesArray);
    }

    @Override
    public List<Activity> getCurrentAthleteActivitiesBeforeDate(long before) throws StravaException {
        String URL = ATHLETE_ACTIVITIES_URL + PARAMETER_BEFORE + before;
        String result = getResult(URL);

        Activity[] activitiesArray = fromJson(result, Activity[].class);
        return Arrays.asList(activitiesArray);
    }

    @Override
    public List<Activity> getCurrentAthleteActivitiesAfterDate(long after) throws StravaException {
        String URL = ATHLETE_ACTIVITIES_URL + PARAMETER_AFTER + after;
        String result = getResult(URL);

        Activity[] activitiesArray = fromJson(result, Activity[].class);
        return Arrays.asList(activitiesArray);
    }

    public List<Activity> getCurrentFriendsActivities() throws StravaException {
        String URL = ACTIVITIES_URL + FOLLOWING;
        String result = getResult(URL);

        Activity[] activitiesArray = fromJson(result, Activity[].class);
        return Arrays.asList(activitiesArray);
    }

    public List<Activity> getCurrentFriendsActivities(int page, int per_page) throws StravaException {
        String URL = ACTIVITIES_URL + FOLLOWING + PARAMETER_PAGE + page + PARAMETER_PER_PAGE + per_page;
        String result = getResult(URL);

        Activity[] activitiesArray = fromJson(result, Activity[].class);
        return Arrays.asList(activitiesArray);
    }

    @Override
    public List<ActivityZone> getActivityZones(long activityId) throws StravaException {
        String URL = ACTIVITIES_URL + "/" + activityId + ZONES;
        String result = getResult(URL);

        ActivityZone[] zonesArray = fromJson(result, ActivityZone[].class);
        return Arrays.asList(zonesArray);
    }

    @Override
    public List<LapsItem> findActivityLaps(long activityId) throws StravaException {
        String URL = ACTIVITIES_URL + "/" + activityId + LAPS;
        String result = getResult(URL);

        LapsItem[] LapsItemsArray = fromJson(result, LapsItem[].class);
        return Arrays.asList(LapsItemsArray);
    }

    @Override
    public List<Comment> findActivityComments(long activityId) throws StravaException {
        String URL = ACTIVITIES_URL + "/" + activityId + COMMENTS;
        String result = getResult(URL);

        Comment[] commentsArray = fromJson(result, Comment[].class);
        return Arrays.asList(commentsArray);
    }

    @Override
    public List<Comment> findActivityComments(long activityId, boolean markdown, int page, int per_page) throws StravaException {
        String URL = ACTIVITIES_URL + "/" + activityId + COMMENTS + PARAMETER_MARKDOWN + markdown + PARAMETER_AND_PAGE + page + PARAMETER_PER_PAGE + per_page;
        String result = getResult(URL);

        Comment[] commentsArray = fromJson(result, Comment[].class);
        return Arrays.asList(commentsArray);
    }

    @Override
    public List<Athlete> findActivityKudos(long activityId) throws StravaException {
        String URL = ACTIVITIES_URL + "/" + activityId + KUDOS;
        String result = getResult(URL);

        Athlete[] athletesArray = fromJson(result, Athlete[].class);
        return Arrays.asList(athletesArray);
    }

    @Override
    public List<Athlete> findActivityKudos(long activityId, int page, int per_page) throws StravaException {
        String URL = ACTIVITIES_URL + "/" + activityId + KUDOS + PARAMETER_PAGE + page + PARAMETER_PER_PAGE + per_page;
        String result = getResult(URL);

        Athlete[] athletesArray = fromJson(result, Athlete[].class);
        return Arrays.asList(athletesArray);
    }

    @Override
    public List<Athlete> findClubMembers(int clubId) throws StravaException {
        String URL = CLUBS_URL + "/" + clubId + MEMBERS;
        String result = getResult(URL);

        Athlete[] athletesArray = fromJson(result, Athlete[].class);
        return Arrays.asList(athletesArray);
    }

    @Override
    public List<Athlete> findClubMembers(int clubId, int page, int per_page) throws StravaException {
        String URL = CLUBS_URL + "/" + clubId + MEMBERS + PARAMETER_PAGE + page + PARAMETER_PER_PAGE + per_page;
        String result = getResult(URL);

        Athlete[] athletesArray = fromJson(result, Athlete[].class);
        return Arrays.asList(athletesArray);
    }

    @Override
    public List<Activity> findClubActivities(int clubId) throws StravaException {
        String URL = CLUBS_URL + "/" + clubId + ACTIVITIES;
        String result = getResult(URL);

        Activity[] activitiesArray = fromJson(result, Activity[].class);
        return Arrays.asList(activitiesArray);
    }

    @Override
    public List<Activity> findClubActivities(int clubId, int page, int per_page) throws StravaException {
        String URL = CLUBS_URL + "/" + clubId + ACTIVITIES + PARAMETER_PAGE + page + PARAMETER_PER_PAGE + per_page;
        String result = getResult(URL);

        Activity[] activitiesArray = fromJson(result, Activity[].class);
        return Arrays.asList(activitiesArray);
    }

    @Override
    public Club findClub(int id) throws StravaException {
        String URL = CLUBS_URL + "/" + id;
        String result = getResult(URL);

        return fromJson(result, Club.class);
    }

    public List<Club> getCurrentAthleteClubs() throws StravaException {
        String result = getResult(ATHLETE_CLUBS_URL);

        Club[] clubsArray = fromJson(result, Club[].class);
        return Arrays.asList(clubsArray);
    }

    @Override
    public Gear findGear(String id) throws StravaException {
        String URL = GEAR_URL + "/" + id;
        String result = getResult(URL);

        return fromJson(result, Gear.class);
    }

    @Override
    public Route findRoute(long routeId) throws StravaException {
        String URL = ROUTE_URL + "/" + routeId;
        String result = getResult(URL);

        return fromJson(result, Route.class);
    }

    @Override
    public String getRouteAsGPX(long routeId) throws StravaException {
        String URL = ROUTE_URL + "/" + routeId + EXPORT_GPX;
        List<String> result = getResult(URL, true, false);
        return result.get(1);
    }

    /*
    Strava API doesn't support the export_gpx path for activities
    You need to open this url in the browser to have the gpx file download.
    You need to be logged in to your Strava account
     */
    @Override
    public String getActivityAsGPX(long activityId) {
        return "https://www.strava.com/activities/" + activityId + EXPORT_GPX;
    }

    @Override
    public List<Route> findAthleteRoutes(int athleteId) throws StravaException {
        String URL = ATHLETES_URL + "/" + athleteId + ROUTES;
        String result = getResult(URL);

        Route[] routesArray = fromJson(result, Route[].class);
        return Arrays.asList(routesArray);
    }

    @Override
    public Segment findSegment(long segmentId) throws StravaException {
        String URL = SEGMENTS_URL + "/" + segmentId;
        String result = getResult(URL);

        return fromJson(result, Segment.class);
    }

    public List<Segment> getCurrentStarredSegment() throws StravaException {
        String URL = SEGMENTS_URL + STARRED;
        String result = getResult(URL);

        Segment[] segmentsArray = fromJson(result, Segment[].class);
        return Arrays.asList(segmentsArray);
    }

    @Override
    public SegmentLeaderBoard findSegmentLeaderBoard(long segmentId) throws StravaException {
        String URL = SEGMENTS_URL + "/" + segmentId + LEADERBOARD;
        String result = getResult(URL);

        return fromJson(result, SegmentLeaderBoard.class);
    }

    @Override
    public SegmentLeaderBoard findSegmentLeaderBoard(long segmentId, int page, int per_page) throws StravaException {
        String URL = SEGMENTS_URL + "/" + segmentId + LEADERBOARD + PARAMETER_PAGE + page + PARAMETER_PER_PAGE + per_page;
        String result = getResult(URL);

        return fromJson(result, SegmentLeaderBoard.class);
    }

    @Override
    public SegmentLeaderBoard findSegmentLeaderBoard(long segmentId, HashMap optionalParameters) throws StravaException {
        String URL = SEGMENTS_URL + "/" + segmentId + LEADERBOARD;
        String result = getResult(URL, optionalParameters);

        return fromJson(result, SegmentLeaderBoard.class);
    }

    @Override
    public List<Segment> findSegments(double[] bound) throws StravaException {
        String URL = SEGMENTS_URL + EXPLORE_BOUNDS + bound.toString();
        String result = getResult(URL);

        //////////UGLY HACK TO ALLOW GSON TO PARSE THE JSON STRING AND RETURN A LIST OF SEGMENTS

        String segmentString = "\\{\"segments\":";
        result = result.replaceFirst(segmentString, "");
        result = result.substring(0, result.lastIndexOf("}"));

        Segment[] segmentsArray = fromJson(result, Segment[].class);
        return Arrays.asList(segmentsArray);
    }

    @Override
    public List<Segment> findSegments(double[] bounds, HashMap optionalParameters) throws StravaException {
        String URL = SEGMENTS_URL + EXPLORE_BOUNDS + bounds.toString();
        String result = getResult(URL, optionalParameters);

        //////////UGLY HACK TO ALLOW GSON TO PARSE THE JSON STRING AND RETURN A LIST OF SEGMENTS

        String segmentString = "\\{\"segments\":";
        if (result.contains(segmentString)) {
            result = result.replaceFirst(segmentString, "");
            result = result.substring(0, result.lastIndexOf("}"));
        }

        Segment[] segmentsArray = fromJson(result, Segment[].class);
        return Arrays.asList(segmentsArray);
    }

    @Override
    public SegmentEffort findSegmentEffort(int id) throws StravaException {
        String URL = BASE_URL + SEGMENT_EFFORTS + id;
        String result = getResult(URL);

        return fromJson(result, SegmentEffort.class);
    }

    @Override
    public List<Stream> findActivityStreams(long activityId, String[] types) throws StravaException {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < types.length; i++) {
            if (i != 0) {
                builder.append(",");
            }
            builder.append(types[i]);
        }

        String URL = ACTIVITIES_URL + "/" + activityId + STREAMS + builder;
        String result = getResult(URL);

        Stream[] streamsArray = fromJson(result, Stream[].class);
        return Arrays.asList(streamsArray);
    }

    @Override
    public List<Stream> findActivityStreams(long activityId, String[] types, String resolution, String series_type) throws StravaException {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < types.length; i++) {
            if (i != 0) {
                builder.append(",");
            }
            builder.append(types[i]);
        }

        String URL = ACTIVITIES_URL + "/" + activityId + STREAMS + builder + PARAMETER_RESOLUTION + resolution;

        if (series_type != null && !series_type.isEmpty()) {
            URL += PARAMETER_SERIES_TYPE + series_type;
        }

        String result = getResult(URL);

        Stream[] streamsArray = fromJson(result, Stream[].class);
        return Arrays.asList(streamsArray);
    }

    @Override
    public List<Stream> findEffortStreams(int id, String[] types) throws StravaException {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < types.length; i++) {
            if (i != 0) {
                builder.append(",");
            }
            builder.append(types[i]);
        }

        String URL = BASE_URL + SEGMENT_EFFORTS + id + STREAMS + builder;
        String result = getResult(URL);

        Stream[] streamsArray = fromJson(result, Stream[].class);
        return Arrays.asList(streamsArray);
    }

    @Override
    public List<Stream> findEffortStreams(long id, String[] types, String resolution, String series_type) throws StravaException {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < types.length; i++) {
            if (i != 0) {
                builder.append(",");
            }
            builder.append(types[i]);
        }

        String URL = BASE_URL + SEGMENT_EFFORTS + id + STREAMS + builder + PARAMETER_RESOLUTION + resolution;

        if (series_type != null && !series_type.isEmpty()) {
            URL += PARAMETER_SERIES_TYPE + series_type;
        }

        String result = getResult(URL);

        Stream[] streamsArray = fromJson(result, Stream[].class);
        return Arrays.asList(streamsArray);
    }

    @Override
    public List<Stream> findSegmentStreams(long id, String[] types) throws StravaException {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < types.length; i++) {
            if (i != 0) {
                builder.append(",");
            }
            builder.append(types[i]);
        }

        String URL = SEGMENTS_URL + "/" + id + STREAMS + builder;
        String result = getResult(URL);

        Stream[] streamsArray = fromJson(result, Stream[].class);
        return Arrays.asList(streamsArray);
    }

    @Override
    public List<Stream> findSegmentStreams(long id, String[] types, String resolution, String series_type) throws StravaException {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < types.length; i++) {
            if (i != 0) {
                builder.append(",");
            }
            builder.append(types[i]);
        }

        String URL = SEGMENTS_URL + "/" + id + STREAMS + builder + PARAMETER_RESOLUTION + resolution;

        if (series_type != null && !series_type.isEmpty()) {
            URL += PARAMETER_SERIES_TYPE + series_type;
        }

        String result = getResult(URL);

        Stream[] streamsArray = fromJson(result, Stream[].class);
        return Arrays.asList(streamsArray);
    }

    @Override
    public UploadStatus uploadActivity(String data_type, File file) throws StravaException {
        String URL = BASE_URL + UPLOADS;
        String result = getResultUploadActivity(URL, file, data_type);
        Gson gson = new Gson();

        return fromJson(result, UploadStatus.class);
    }

    @Override
    public UploadStatus uploadActivity(String activity_type, String name, String description, int is_private, int trainer, String data_type, String external_id, File file) {
        return null;
    }

    @Override
    public UploadStatus checkUploadStatus(int uploadId) throws StravaException {
        String URL = BASE_URL + UPLOADS + "/" + uploadId;
        String result = getResult(URL);

        return fromJson(result, UploadStatus.class);
    }

    private String getExtension(String fileName) {
        String extension = "";

        int i = fileName.lastIndexOf('.');
        int p = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));

        if (i > p) {
            extension = fileName.substring(i + 1);
        }
        return extension;
    }

    private String getResultUploadActivity(String URL, File activityFile, String type) {
        StringBuilder sb = new StringBuilder();
        String ext = getExtension(activityFile.getName());
        String boundary = "===" + System.currentTimeMillis() + "===";// UNIQUE
        String LINE_FEED = "\r\n";

        try {
            URL url = new URL(URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Cache-Control", "no-cache");
            conn.setRequestProperty("Authorization", "Bearer " + getAccessToken());
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            OutputStream out = conn.getOutputStream();
            DataOutputStream request = new DataOutputStream(out);

            addField(type, "activity_type", boundary, LINE_FEED, request);

            addField(ext, "data_type", boundary, LINE_FEED, request);

            addFilePart("file", activityFile, boundary, LINE_FEED, request);

            request.writeBytes(LINE_FEED);
            request.writeBytes("--" + boundary + "--" + LINE_FEED);
            request.flush();
            request.close();

            if (conn.getResponseCode() != 201) {
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode() + " - " + conn.getErrorStream());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            String output;
            while ((output = br.readLine()) != null) {
                sb.append(output);
            }

            conn.disconnect();

        } catch (IOException e) {

            e.printStackTrace();
            return null;
        }

        return sb.toString();
    }

    private void addField(String value, String name, String boundary, String LINE_FEED, DataOutputStream request) throws IOException {
        request.writeBytes("--" + boundary + LINE_FEED);
        request.writeBytes("Content-Disposition: form-data; name=\"" + name + "\"" + LINE_FEED);
        request.writeBytes(LINE_FEED);
        request.writeBytes(value + LINE_FEED);
        request.flush();
    }

    public void addFilePart(String fieldName, File uploadFile, String boundary, String LINE_FEED, DataOutputStream request) throws IOException {
        String fileName = uploadFile.getName();
        request.writeBytes("--" + boundary + LINE_FEED);
        request.writeBytes("Content-Disposition: form-data; name=\"" + fieldName + "\"; filename=\"" + fileName + "\"" + LINE_FEED);
        request.writeBytes(LINE_FEED);
        request.flush();

        FileInputStream inputStream = new FileInputStream(uploadFile);
        byte[] buffer = new byte[4096];
        int bytesRead = -1;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            request.write(buffer, 0, bytesRead);
        }
        inputStream.close();
        request.writeBytes(LINE_FEED);
        request.flush();
    }


    private String getResult(String URL) throws StravaException {
        List<String> result = getResult(URL, true, true);
        return result.get(1);
    }

    private List<String> getResult(String URL, boolean withBearer, boolean jsonFormat) throws StravaException {
        if (!init && !withBearer) {
            throw new RuntimeException("Class not initialized. Missing access_token");
        }
        StringBuilder sb = new StringBuilder();
        java.net.URL resultUrl;
        latestGetURL = URL;
        this.withBearer = withBearer;
        this.jsonFormat = jsonFormat;
        try {
            URL url = new URL(URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            if (jsonFormat) {
                conn.setRequestProperty("Accept", "application/json");
            }
            if (withBearer) {
                conn.setRequestProperty("Authorization", "Bearer " + getAccessToken());
            }

            if (conn.getResponseCode() != 200) {
                throw new StravaRequestException(conn.getResponseCode(), "Failed : HTTP error code : "
                        + conn.getResponseCode() + " - " + conn.getResponseMessage());
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output;
            while ((output = br.readLine()) != null) {
                sb.append(output);
            }

            resultUrl = conn.getURL();
            conn.disconnect();

        } catch (IOException e) {
            throw new GenericStravaException("Error in getResult :", e);
        }

        return List.of(resultUrl.toString(), sb.toString());
    }

    public <T> T retryGet(Class<T> classOfT) {
        String s = getResult(latestGetURL, withBearer, jsonFormat).get(1);
        if (jsonFormat) {
            return fromJson(s, classOfT);
        }
        return (T) s;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    private String getResult(String URL, HashMap optionalParameters) {
        StringBuilder sb = new StringBuilder();
        sb.append(URL);
        try {

            Iterator iterator = optionalParameters.keySet().iterator();

            int index = 0;
            while (iterator.hasNext()) {
                if (index == 0) {
                    sb.append("?");
                } else {
                    sb.append("&");
                }
                String key = (String) iterator.next();
                sb.append(key);
                sb.append("=");
                sb.append(URLEncoder.encode(optionalParameters.get(key).toString(), UTF_8));
                index++;
            }

            URI uri = new URI(String.format(sb.toString()));
            URL url = uri.toURL();


            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + getAccessToken());
            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode() + " - " + conn.getResponseMessage());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            String output;
            sb = new StringBuilder();
            while ((output = br.readLine()) != null) {
                sb.append(output);
            }
            conn.disconnect();
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
        return sb.toString();
    }

    private String postResult(String URL) {
        StringBuilder sb = new StringBuilder();
        try {
            String[] parsedUrl = URL.split("\\?");
            String params = URLEncoder.encode(parsedUrl[1], UTF_8).replace("%3D", "=").replace("%26", "&");

            URL url = new URL(parsedUrl[0] + "?" + params);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + getAccessToken());

            conn.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.writeBytes(params);
            wr.flush();
            wr.close();

            boolean redirect = false;
            // normally, 3xx is redirect
            int status = conn.getResponseCode();
            if (status != HttpURLConnection.HTTP_OK) {
                if (status == HttpURLConnection.HTTP_MOVED_TEMP
                        || status == HttpURLConnection.HTTP_MOVED_PERM
                        || status == HttpURLConnection.HTTP_SEE_OTHER)
                    redirect = true;
            }

            if (redirect) {
                // get redirect url from "location" header field
                String newUrl = conn.getHeaderField("Location");

                // open the new connection again
                conn = (HttpURLConnection) new URL(newUrl).openConnection();
                conn.setRequestProperty("Accept", "application/json");
                conn.setRequestProperty("Authorization", "Bearer " + getAccessToken());
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                sb.append(inputLine);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    private String putResult(String URL) {
        StringBuilder sb;

        try {
            String finalUrl = "";
            if (URL.contains("?")) {
                String[] parsedUrl = URL.split("\\?");
                String params = URLEncoder.encode(parsedUrl[1], UTF_8);
                finalUrl = parsedUrl[0] + "?" + params;
            } else {
                finalUrl = URL;
            }

            URL url = new URL(finalUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + getAccessToken());
            if (conn.getResponseCode() != 200 | conn.getResponseCode() != 201) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode() + " - " + conn.getResponseMessage());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            String output;
            sb = new StringBuilder();
            while ((output = br.readLine()) != null) {
                sb.append(output);
            }

            conn.disconnect();
        } catch (IOException e) {

            e.printStackTrace();
            return null;
        }
        return sb.toString();
    }

    private String putResult(String URL, HashMap optionalParameters) {
        StringBuilder sb = new StringBuilder();
        sb.append(URL);
        try {
            Iterator iterator = optionalParameters.keySet().iterator();

            int index = 0;
            while (iterator.hasNext()) {
                if (index == 0) {
                    sb.append("?");
                } else {
                    sb.append("&");
                }
                String key = (String) iterator.next();
                sb.append(key);
                sb.append("=");
                sb.append(URLEncoder.encode(optionalParameters.get(key).toString(), UTF_8));
                index++;
            }

            URI uri = new URI(sb.toString());
            URL url = uri.toURL();

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + getAccessToken());
            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode() + " - " + conn.getResponseMessage());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            String output;
            sb = new StringBuilder();
            while ((output = br.readLine()) != null) {
                sb.append(output);
            }
            conn.disconnect();
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
        return sb.toString();
    }

    private String deleteResult(String URL) {
        StringBuilder sb = new StringBuilder();
        sb.append(URL);
        try {
            URI uri = new URI(String.format(sb.toString()));
            URL url = uri.toURL();

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + getAccessToken());
            if (conn.getResponseCode() != 204) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode() + " - " + conn.getResponseMessage());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            String output;
            sb = new StringBuilder();
            while ((output = br.readLine()) != null) {
                sb.append(output);
            }
            conn.disconnect();
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
        return sb.toString();
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}

package org.jstrava.api;

import com.google.gson.Gson;
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
    private static final String ATHLETE_URL = BASE_URL + "/athlete";
    private static final String ATHLETES_URL = BASE_URL + "/athletes";
    private static final String ATHLETE_ACTIVITIES_URL = ATHLETE_URL + "/activities";
    private static final String ATHLETE_CLUBS_URL = ATHLETE_URL + "/clubs";
    private static final String ACTIVITIES_URL = BASE_URL + "/activities";
    private static final String CLUBS_URL = BASE_URL + "/clubs";
    private static final String SEGMENTS_URL = BASE_URL + "/segments";
    private static final String ATHLETE_FOLLOWERS_URL = ATHLETE_URL + "/followers";
    private static final String ATHLETE_FRIENDS_URL =  ATHLETE_URL + "/friends";

    private String accessToken;
    private String refreshToken;
    private Athlete currentAthlete;
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
    public Athlete getCurrentAthlete() {
        if (currentAthlete == null) {
            String result = getResult(ATHLETE_URL);
            currentAthlete = gson.fromJson(result, Athlete.class);
        }
        return currentAthlete;
    }

    @Override
    public Athlete updateAthlete(HashMap optionalParameters) {
        String result = putResult(ATHLETE_URL, optionalParameters);
        return gson.fromJson(result, Athlete.class);
    }

    @Override
    public Athlete findAthlete(int id) {
        String URL = ATHLETE_URL + "/" + id;
        String result = getResult(URL);

        return gson.fromJson(result, Athlete.class);
    }

    @Override
    public List<SegmentEffort> findAthleteKOMs(int athleteId) {
        String URL = ATHLETE_URL + "/" + athleteId + "/koms";
        String result = getResult(URL);

        SegmentEffort[] segmentEffortArray = gson.fromJson(result, SegmentEffort[].class);
        return Arrays.asList(segmentEffortArray);
    }

    @Override
    public List<SegmentEffort> findAthleteKOMs(int athleteId, int page, int per_page) {
        String URL = ATHLETE_URL + "/" + athleteId + "/koms?page=" + page + "&per_page=" + per_page;
        String result = getResult(URL);

        SegmentEffort[] segmentEffortArray = gson.fromJson(result, SegmentEffort[].class);
        return Arrays.asList(segmentEffortArray);
    }

    @Override
    public List<Athlete> getCurrentAthleteFriends() {
        String result = getResult(ATHLETE_FRIENDS_URL);

        Athlete[] athletesArray = gson.fromJson(result, Athlete[].class);
        return Arrays.asList(athletesArray);
    }

    @Override
    public List<Athlete> getCurrentAthleteFriends(int page, int per_page) {
        String URL = ATHLETE_FRIENDS_URL + "?page=" + page + "&per_page=" + per_page;
        String result = getResult(URL);

        Athlete[] athletesArray = gson.fromJson(result, Athlete[].class);
        return Arrays.asList(athletesArray);
    }

    @Override
    public List<Athlete> findAthleteFriends(int id) {
        String URL = ATHLETE_URL + "/" + id + "/friends";
        String result = getResult(URL);

        Athlete[] athletesArray = gson.fromJson(result, Athlete[].class);
        return Arrays.asList(athletesArray);
    }

    @Override
    public List<Athlete> findAthleteFriends(int id, int page, int per_page) {
        String URL = ATHLETE_URL + "/" + id + "/friends?page=" + page + "&per_page=" + per_page;
        String result = getResult(URL);

        Athlete[] athletesArray = gson.fromJson(result, Athlete[].class);
        return Arrays.asList(athletesArray);
    }

    @Override
    public List<Athlete> getCurrentAthleteFollowers() {
        String result = getResult(ATHLETE_FOLLOWERS_URL);

        Athlete[] athletesArray = gson.fromJson(result, Athlete[].class);
        return Arrays.asList(athletesArray);
    }

    @Override
    public List<Athlete> getCurrentAthleteFollowers(int page, int per_page) {
        String URL = ATHLETE_FOLLOWERS_URL + "?page=" + page + "&per_page=" + per_page;
        String result = getResult(URL);

        Athlete[] athletesArray = gson.fromJson(result, Athlete[].class);
        return Arrays.asList(athletesArray);
    }

    @Override
    public List<Athlete> findAthleteFollowers(int id) {
        String URL = ATHLETES_URL + "/" + id + "/followers";
        String result = getResult(URL);

        Athlete[] athletesArray = gson.fromJson(result, Athlete[].class);

        return Arrays.asList(athletesArray);
    }

    @Override
    public List<Athlete> findAthleteFollowers(int id, int page, int per_page) {
        String URL = ATHLETES_URL + "/" + id + "/followers?page=" + page + "&per_page=" + per_page;
        String result = getResult(URL);

        Athlete[] athletesArray = gson.fromJson(result, Athlete[].class);
        return Arrays.asList(athletesArray);
    }

    @Override
    public List<Athlete> findAthleteBothFollowing(int id) {
        String URL = ATHLETES_URL + "/" + id + "/both-following";
        String result = getResult(URL);

        Athlete[] athletesArray = gson.fromJson(result, Athlete[].class);
        return Arrays.asList(athletesArray);
    }

    @Override
    public List<Athlete> findAthleteBothFollowing(int id, int page, int per_page) {
        String URL = ATHLETES_URL + "/" + id + "/both-following?page=" + page + "&per_page=" + per_page;
        String result = getResult(URL);

        Athlete[] athletesArray = gson.fromJson(result, Athlete[].class);
        return Arrays.asList(athletesArray);
    }

    @Override
    public Activity createActivity(String name, String type, String start_date_local, int elapsed_time) {
        String URL = ACTIVITIES_URL + "?name=" + name + "&type=" + type + "&start_date_local=" + start_date_local + "&elapsed_time=" + elapsed_time;
        String result = postResult(URL);

        return gson.fromJson(result, Activity.class);
    }

    @Override
    public Activity createActivity(String name, String type, String start_date_local, int elapsed_time, String description, float distance) {
        String URL = ACTIVITIES_URL + "?name=" + name + "&type=" + type + "&start_date_local=" + start_date_local + "&elapsed_time=" + elapsed_time + "&description=" + description + "&distance=" + distance;
        String result = postResult(URL);

        return gson.fromJson(result, Activity.class);
    }

    @Override
    public void deleteActivity(long activityId) {
        String URL = ACTIVITIES_URL + "/" + activityId;
        String result = deleteResult(URL);

        gson.fromJson(result, String.class);
    }

    @Override
    public Activity findActivity(long id) {
        String URL = ACTIVITIES_URL + "/" + id;
        String result = getResult(URL);

        return gson.fromJson(result, Activity.class);
    }

    @Override
    public Activity findActivity(long id, boolean include_all_efforts) {
        String URL = ACTIVITIES_URL + "/" + id + "?include_all_efforts=" + include_all_efforts;
        String result = getResult(URL);

        return gson.fromJson(result, Activity.class);
    }

    @Override
    public Activity updateActivity(long activityId, HashMap optionalParameters) {
        String URL = ACTIVITIES_URL + "/" + activityId;
        String result = putResult(URL, optionalParameters);

        return gson.fromJson(result, Activity.class);
    }

    @Override
    public List<Activity> getCurrentAthleteActivitiesAll() {
        int resultsPerPage = 30;
        int page = 1;
        List<Activity> currentActivities = new ArrayList<>();
        List<Activity> activitiesPerPage;

        while ((activitiesPerPage = this.getCurrentAthleteActivities(page, resultsPerPage)).size() > 0) {
            currentActivities.addAll(activitiesPerPage);
            page++;
        }
        return currentActivities;
    }

    @Override
    public List<Activity> getCurrentAthleteActivities() {
        String result = getResult(ATHLETE_ACTIVITIES_URL);

        Activity[] activitiesArray = gson.fromJson(result, Activity[].class);
        return Arrays.asList(activitiesArray);
    }

    @Override
    public List<Activity> getCurrentAthleteActivities(int page, int per_page) {
        String URL = ATHLETE_ACTIVITIES_URL + "?page=" + page + "&per_page=" + per_page;
        String result = getResult(URL);

        Activity[] activitiesArray = gson.fromJson(result, Activity[].class);
        return Arrays.asList(activitiesArray);
    }

    @Override
    public List<Activity> getCurrentAthleteActivitiesBeforeDate(long before) {
        String URL = ATHLETE_ACTIVITIES_URL + "?before=" + before;
        String result = getResult(URL);

        Activity[] activitiesArray = gson.fromJson(result, Activity[].class);
        return Arrays.asList(activitiesArray);
    }

    @Override
    public List<Activity> getCurrentAthleteActivitiesAfterDate(long after) {
        String URL = ATHLETE_ACTIVITIES_URL + "?after=" + after;
        String result = getResult(URL);

        Activity[] activitiesArray = gson.fromJson(result, Activity[].class);
        return Arrays.asList(activitiesArray);
    }

    public List<Activity> getCurrentFriendsActivities() {
        String URL = ACTIVITIES_URL + "/following";
        String result = getResult(URL);

        Activity[] activitiesArray = gson.fromJson(result, Activity[].class);
        return Arrays.asList(activitiesArray);
    }

    public List<Activity> getCurrentFriendsActivities(int page, int per_page) {
        String URL = ACTIVITIES_URL + "/following?page=" + page + "&per_page=" + per_page;
        String result = getResult(URL);

        Activity[] activitiesArray = gson.fromJson(result, Activity[].class);
        return Arrays.asList(activitiesArray);
    }

    @Override
    public List<ActivityZone> getActivityZones(long activityId) {
        String URL = ACTIVITIES_URL + "/" + activityId + "/zones";
        String result = getResult(URL);

        ActivityZone[] zonesArray = gson.fromJson(result, ActivityZone[].class);
        return Arrays.asList(zonesArray);
    }

    @Override
    public List<LapsItem> findActivityLaps(long activityId) {
        String URL = ACTIVITIES_URL + "/" + activityId + "/laps";
        String result = getResult(URL);

        LapsItem[] LapsItemsArray = gson.fromJson(result, LapsItem[].class);
        return Arrays.asList(LapsItemsArray);
    }

    @Override
    public List<Comment> findActivityComments(long activityId) {
        String URL = ACTIVITIES_URL + "/" + activityId + "/comments";
        String result = getResult(URL);

        Comment[] commentsArray = gson.fromJson(result, Comment[].class);
        return Arrays.asList(commentsArray);
    }

    @Override
    public List<Comment> findActivityComments(long activityId, boolean markdown, int page, int per_page) {
        String URL = ACTIVITIES_URL + "/" + activityId + "/comments?markdown=" + markdown + "&page=" + page + "&per_page=" + per_page;
        String result = getResult(URL);

        Comment[] commentsArray = gson.fromJson(result, Comment[].class);
        return Arrays.asList(commentsArray);
    }

    @Override
    public List<Athlete> findActivityKudos(long activityId) {
        String URL = ACTIVITIES_URL + "/" + activityId + "/kudos";
        String result = getResult(URL);

        Athlete[] athletesArray = gson.fromJson(result, Athlete[].class);
        return Arrays.asList(athletesArray);
    }

    @Override
    public List<Athlete> findActivityKudos(long activityId, int page, int per_page) {
        String URL = ACTIVITIES_URL + "/" + activityId + "/kudos?page=" + page + "&per_page=" + per_page;
        String result = getResult(URL);

        Athlete[] athletesArray = gson.fromJson(result, Athlete[].class);
        return Arrays.asList(athletesArray);
    }

    @Override
    public List<Athlete> findClubMembers(int clubId) {
        String URL = CLUBS_URL + "/" + clubId + "/members";
        String result = getResult(URL);

        Athlete[] athletesArray = gson.fromJson(result, Athlete[].class);
        return Arrays.asList(athletesArray);
    }

    @Override
    public List<Athlete> findClubMembers(int clubId, int page, int per_page) {
        String URL = CLUBS_URL + "/" + clubId + "/members?page=" + page + "&per_page=" + per_page;
        String result = getResult(URL);

        Athlete[] athletesArray = gson.fromJson(result, Athlete[].class);
        return Arrays.asList(athletesArray);
    }

    @Override
    public List<Activity> findClubActivities(int clubId) {
        String URL = CLUBS_URL + "/" + clubId + "/activities";
        String result = getResult(URL);

        Activity[] activitiesArray = gson.fromJson(result, Activity[].class);
        return Arrays.asList(activitiesArray);
    }

    @Override
    public List<Activity> findClubActivities(int clubId, int page, int per_page) {
        String URL = CLUBS_URL + "/" + clubId + "/activities" + "?page=" + page + "&per_page=" + per_page;
        String result = getResult(URL);

        Activity[] activitiesArray = gson.fromJson(result, Activity[].class);
        return Arrays.asList(activitiesArray);
    }

    @Override
    public Club findClub(int id) {
        String URL = CLUBS_URL + "/" + id;
        String result = getResult(URL);

        return gson.fromJson(result, Club.class);
    }

    public List<Club> getCurrentAthleteClubs() {
        String result = getResult(ATHLETE_CLUBS_URL);

        Club[] clubsArray = gson.fromJson(result, Club[].class);
        return Arrays.asList(clubsArray);
    }

    @Override
    public Gear findGear(String id) {
        String URL = BASE_URL + "/gear/" + id;
        String result = getResult(URL);

        return gson.fromJson(result, Gear.class);
    }

    @Override
    public Route findRoute(int routeId) {
        String URL = BASE_URL + "/route/" + routeId;
        String result = getResult(URL);

        return gson.fromJson(result, Route.class);
    }

    @Override
    public List<Route> findAthleteRoutes(int athleteId) {
        String URL = ATHLETES_URL + "/" + athleteId + "/routes";
        String result = getResult(URL);

        Route[] routesArray = gson.fromJson(result, Route[].class);
        return Arrays.asList(routesArray);
    }

    @Override
    public Segment findSegment(long segmentId) {
        String URL = SEGMENTS_URL + "/" + segmentId;
        String result = getResult(URL);

        return gson.fromJson(result, Segment.class);
    }

    public List<Segment> getCurrentStarredSegment() {
        String URL = SEGMENTS_URL + "/starred";
        String result = getResult(URL);

        Segment[] segmentsArray = gson.fromJson(result, Segment[].class);
        return Arrays.asList(segmentsArray);
    }

    @Override
    public SegmentLeaderBoard findSegmentLeaderBoard(long segmentId) {
        String URL = SEGMENTS_URL + "/" + segmentId + "/leaderboard";
        String result = getResult(URL);

        return gson.fromJson(result, SegmentLeaderBoard.class);
    }

    @Override
    public SegmentLeaderBoard findSegmentLeaderBoard(long segmentId, int page, int per_page) {
        String URL = SEGMENTS_URL + "/" + segmentId + "/leaderboard?page=" + page + "&per_page=" + per_page;
        String result = getResult(URL);

        return gson.fromJson(result, SegmentLeaderBoard.class);
    }

    @Override
    public SegmentLeaderBoard findSegmentLeaderBoard(long segmentId, HashMap optionalParameters) {
        String URL = SEGMENTS_URL + "/" + segmentId + "/leaderboard";
        String result = getResult(URL, optionalParameters);

        return gson.fromJson(result, SegmentLeaderBoard.class);
    }

    @Override
    public List<Segment> findSegments(double[] bound) {
        String URL = SEGMENTS_URL + "/explore?bounds=" + bound.toString();
        String result = getResult(URL);

        //////////UGLY HACK TO ALLOW GSON TO PARSE THE JSON STRING AND RETURN A LIST OF SEGMENTS

        String segmentString = "\\{\"segments\":";
        result = result.replaceFirst(segmentString, "");
        result = result.substring(0, result.lastIndexOf("}"));

        Segment[] segmentsArray = gson.fromJson(result, Segment[].class);
        return Arrays.asList(segmentsArray);
    }

    @Override
    public List<Segment> findSegments(double[] bounds, HashMap optionalParameters) {
        String URL = SEGMENTS_URL + "/explore?bounds=" + bounds.toString();
        String result = getResult(URL, optionalParameters);

        //////////UGLY HACK TO ALLOW GSON TO PARSE THE JSON STRING AND RETURN A LIST OF SEGMENTS

        String segmentString = "\\{\"segments\":";
        if (result.contains(segmentString)) {
            result = result.replaceFirst(segmentString, "");
            result = result.substring(0, result.lastIndexOf("}"));
        }

        Segment[] segmentsArray = gson.fromJson(result, Segment[].class);
        return Arrays.asList(segmentsArray);
    }

    @Override
    public SegmentEffort findSegmentEffort(int id) {
        String URL = BASE_URL + "/segment_efforts/" + id;
        String result = getResult(URL);

        return gson.fromJson(result, SegmentEffort.class);
    }

    @Override
    public List<Stream> findActivityStreams(long activityId, String[] types) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < types.length; i++) {
            if (i != 0) {
                builder.append(",");
            }
            builder.append(types[i]);
        }

        String URL = ACTIVITIES_URL + "/" + activityId + "/streams/" + builder;
        String result = getResult(URL);

        Stream[] streamsArray = gson.fromJson(result, Stream[].class);
        return Arrays.asList(streamsArray);
    }

    @Override
    public List<Stream> findActivityStreams(long activityId, String[] types, String resolution, String series_type) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < types.length; i++) {
            if (i != 0) {
                builder.append(",");
            }
            builder.append(types[i]);
        }

        String URL = ACTIVITIES_URL + "/" + activityId + "/streams/" + builder + "?resolution=" + resolution;

        if (series_type != null && !series_type.isEmpty()) {
            URL += "&series_type=" + series_type;
        }

        String result = getResult(URL);

        Stream[] streamsArray = gson.fromJson(result, Stream[].class);
        return Arrays.asList(streamsArray);
    }

    @Override
    public List<Stream> findEffortStreams(int id, String[] types) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < types.length; i++) {
            if (i != 0) {
                builder.append(",");
            }
            builder.append(types[i]);
        }

        String URL = BASE_URL + "/segment_efforts/" + id + "/streams/" + builder;
        String result = getResult(URL);

        Stream[] streamsArray = gson.fromJson(result, Stream[].class);
        return Arrays.asList(streamsArray);
    }

    @Override
    public List<Stream> findEffortStreams(long id, String[] types, String resolution, String series_type) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < types.length; i++) {
            if (i != 0) {
                builder.append(",");
            }
            builder.append(types[i]);
        }

        String URL = BASE_URL + "/segment_efforts/" + id + "/streams/" + builder + "?resolution=" + resolution;

        if (series_type != null && !series_type.isEmpty()) {
            URL += "&series_type=" + series_type;
        }

        String result = getResult(URL);

        Stream[] streamsArray = gson.fromJson(result, Stream[].class);
        return Arrays.asList(streamsArray);
    }

    @Override
    public List<Stream> findSegmentStreams(long id, String[] types) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < types.length; i++) {
            if (i != 0) {
                builder.append(",");
            }
            builder.append(types[i]);
        }

        String URL = SEGMENTS_URL + "/" + id + "/streams/" + builder;
        String result = getResult(URL);

        Stream[] streamsArray = gson.fromJson(result, Stream[].class);
        return Arrays.asList(streamsArray);
    }

    @Override
    public List<Stream> findSegmentStreams(long id, String[] types, String resolution, String series_type) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < types.length; i++) {
            if (i != 0) {
                builder.append(",");
            }
            builder.append(types[i]);
        }

        String URL = SEGMENTS_URL + "/" + id + "/streams/" + builder + "?resolution=" + resolution;

        if (series_type != null && !series_type.isEmpty()) {
            URL += "&series_type=" + series_type;
        }

        String result = getResult(URL);

        Stream[] streamsArray = gson.fromJson(result, Stream[].class);
        return Arrays.asList(streamsArray);
    }

    @Override
    public UploadStatus uploadActivity(String data_type, File file) {
        String URL = BASE_URL + "/uploads";
        String result = getResultUploadActivity(URL, file, data_type);
        Gson gson = new Gson();

        return gson.fromJson(result, UploadStatus.class);
    }

    @Override
    public UploadStatus uploadActivity(String activity_type, String name, String description, int is_private, int trainer, String data_type, String external_id, File file) {
        return null;
    }

    @Override
    public UploadStatus checkUploadStatus(int uploadId) {
        String URL = BASE_URL + "/uploads/" + uploadId;
        String result = getResult(URL);

        return gson.fromJson(result, UploadStatus.class);
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


    private String getResult(String URL) {
        List<String> result = getResult(URL, true);
        if (result == null) {
            return null;
        }
        return result.get(1);
    }

    private List<String> getResult(String URL, boolean withBearer) {
        if (!init && !withBearer) {
            throw new RuntimeException("Class not initialized. Missing access_token");
        }
        StringBuilder sb = new StringBuilder();
        java.net.URL resultUrl;

        try {
            URL url = new URL(URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            if (withBearer) {
                conn.setRequestProperty("Authorization", "Bearer " + getAccessToken());
            }

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
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

            e.printStackTrace();
            return null;
        }

        return List.of(resultUrl.toString(), sb.toString());
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

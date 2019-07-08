/*
 * UserWebServiceClientAdapterBase.java, Edycem Android
 *
 * Copyright 2019
 * Description : 
 * Author(s)   : Harmony
 * Licence     : 
 * Last update : Jul 8, 2019
 *
 */

package com.imie.edycem.data.base;

import java.util.List;
import java.util.ArrayList;

import org.joda.time.format.DateTimeFormatter;
import com.imie.edycem.harmony.util.DateUtils;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.joda.time.format.DateTimeFormat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;

import com.imie.edycem.data.*;
import com.imie.edycem.entity.User;
import com.imie.edycem.data.RestClient.Verb;
import com.imie.edycem.provider.contract.UserContract;

import com.imie.edycem.entity.Job;
import com.imie.edycem.entity.WorkingTime;
import com.imie.edycem.entity.Project;


/**
 *
 * <b><i>This class will be overwrited whenever you regenerate the project with Harmony.
 * You should edit UserWebServiceClientAdapter class instead of this one or you will lose all your modifications.</i></b>
 *
 */
public abstract class UserWebServiceClientAdapterBase
        extends WebServiceClientAdapter<User> {
    /** UserWebServiceClientAdapterBase TAG. */
    protected static final String TAG = "UserWSClientAdapter";

    /** JSON Object User pattern. */
    protected static String JSON_OBJECT_USER = "User";
    /** JSON_ID attributes. */
    protected static String JSON_ID = "id";
    /** JSON_IDSERVER attributes. */
    protected static String JSON_IDSERVER = "id";
    /** JSON_FIRSTNAME attributes. */
    protected static String JSON_FIRSTNAME = "firstname";
    /** JSON_LASTNAME attributes. */
    protected static String JSON_LASTNAME = "lastname";
    /** JSON_EMAIL attributes. */
    protected static String JSON_EMAIL = "email";
    /** JSON_ISELIGIBLE attributes. */
    protected static String JSON_ISELIGIBLE = "isEligible";
    /** JSON_IDSMARTPHONE attributes. */
    protected static String JSON_IDSMARTPHONE = "smartphone_id";
    /** JSON_DATERGPD attributes. */
    protected static String JSON_DATERGPD = "dateRgpd";
    /** JSON_TOKEN attributes. */
    protected static String JSON_TOKEN = "api_token";
    /** JSON_JOB attributes. */
    protected static String JSON_JOB = "job";
    /** JSON_USERWORKINGTIMES attributes. */
    protected static String JSON_USERWORKINGTIMES = "userWorkingTimes";
    /** JSON_CREATEDPROJECTS attributes. */
    protected static String JSON_CREATEDPROJECTS = "createdProjects";

    /** Rest Date Format pattern. */
    public static final String REST_UPDATE_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";

    /** Time pattern.*/
    public static final String TIME_FORMAT = "HH:mm:ss";

    /**
     * Constructor with overriden port and host.
     *
     * @param context The context
     */
    public UserWebServiceClientAdapterBase(Context context) {
        this(context, null);
    }

    /**
     * Constructor with overriden port.
     *
     * @param context The context
     * @param port The overriden port
     */
    public UserWebServiceClientAdapterBase(Context context,
        Integer port) {
        this(context, null, port);
    }

    /**
     * Constructor with overriden port and host.
     *
     * @param context The context
     * @param host The overriden host
     * @param port The overriden port
     */
    public UserWebServiceClientAdapterBase(Context context,
            String host, Integer port) {
        this(context, host, port, null);
    }

    /**
     * Constructor with overriden port, host and scheme.
     *
     * @param context The context
     * @param host The overriden host
     * @param port The overriden port
     * @param scheme The overriden scheme
     */
    public UserWebServiceClientAdapterBase(Context context,
            String host, Integer port, String scheme) {
        this(context, host, port, scheme, null);
    }

    /**
     * Constructor with overriden port, host, scheme and prefix.
     *
     * @param context The context
     * @param host The overriden host
     * @param port The overriden port
     * @param scheme The overriden scheme
     * @param prefix The overriden prefix
     */
    public UserWebServiceClientAdapterBase(Context context,
            String host, Integer port, String scheme, String prefix) {
        super(context, host, port, scheme, prefix);

        
    }

    /**
     * Retrieve all the Users in the given list. Uses the route : User.
     * @param users : The list in which the Users will be returned
     * @return The number of Users returned
     */
    public int getAll(List<User> users) {
        int result = -1;
        String response = this.invokeRequest(
                    Verb.GET,
                    String.format(
                        this.getUri() + "%s",
                        REST_FORMAT),
                    null);

        if (this.isValidResponse(response) && this.isValidRequest()) {
            try {
                JSONObject json = new JSONObject(response);
                result = extractItems(json, "Users", users);
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
            }
        }

        return result;
    }

    /**
     * @return the URI.
     */
    public String getUri() {
        return "user";
    }

    /**
     * Retrieve one User. Uses the route : User/%id%.
     * @param user : The User to retrieve (set the ID)
     * @return -1 if an error has occurred. 0 if not.
     */
    public int get(User user) {
        int result = -1;
        String response = this.invokeRequest(
                    Verb.GET,
                    String.format(
                        this.getUri() + "/%s%s",
                        user.getId(),
                        REST_FORMAT),
                    null);

        if (this.isValidResponse(response) && this.isValidRequest()) {
            try {
                JSONObject json = new JSONObject(response);
                if (extract(json, user)) {
                    result = 0;
                }
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
            }
        }

        return result;
    }

    /**
     * Update a User. Uses the route : User/%id%.
     * @param user : The User to update
     * @return -1 if an error has occurred. 0 if not.
     */
    public int update(User user) {
        int result = -1;
        String response = this.invokeRequest(
                    Verb.PUT,
                    String.format(
                        this.getUri() + "/%s%s",
                        user.getId(),
                        REST_FORMAT),
                    itemToJson(user));

        if (this.isValidResponse(response) && this.isValidRequest()) {
            try {
                JSONObject json = new JSONObject(response);
                this.extract(json, user);
                result = 0;
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
            }
        }

        return result;
    }

    /**
     * Delete a User. Uses the route : User/%id%.
     * @param user : The User to delete (only the id is necessary)
     * @return -1 if an error has occurred. 0 if not.
     */
    public int delete(User user) {
        int result = -1;
        String response = this.invokeRequest(
                    Verb.DELETE,
                    String.format(
                        this.getUri() + "/%s%s",
                        user.getId(),
                        REST_FORMAT),
                    null);

        if (this.isValidResponse(response) && this.isValidRequest()) {
            result = 0;
        }

        return result;
    }

    /**
     * Get the Users associated with a Job. Uses the route : job/%Job_id%/user.
     * @param users : The list in which the Users will be returned
     * @param job : The associated job
     * @return The number of Users returned
     */
    public int getByJob(List<User> users, Job job) {
        int result = -1;
        String response = this.invokeRequest(
                    Verb.GET,
                    String.format(
                        this.getUri() + "/%s%s",
                        job.getId(),
                        REST_FORMAT),
                    null);

        if (this.isValidResponse(response) && this.isValidRequest()) {
            try {
                JSONObject json = new JSONObject(response);
                result = this.extractItems(json, "Users", users);
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
            }
        }

        return result;
    }




    /**
     * Tests if the json is a valid User Object.
     *
     * @param json The json
     *
     * @return True if valid
     */
    public boolean isValidJSON(JSONObject json) {
        boolean result = true;
        result = result && json.has(UserWebServiceClientAdapter.JSON_ID);

        return result;
    }

    /**
     * Extract a User from a JSONObject describing a User.
     * @param json The JSONObject describing the User
     * @param user The returned User
     * @return true if a User was found. false if not
     */
    public boolean extract(JSONObject json, User user) {
        boolean result = this.isValidJSON(json);
        if (result) {
            try {

                if (json.has(UserWebServiceClientAdapter.JSON_ID)
                        && !json.isNull(UserWebServiceClientAdapter.JSON_ID)) {
                    user.setId(
                            json.getInt(UserWebServiceClientAdapter.JSON_ID));
                }

                if (json.has(UserWebServiceClientAdapter.JSON_IDSERVER)
                        && !json.isNull(UserWebServiceClientAdapter.JSON_IDSERVER)) {
                    user.setIdServer(
                            json.getInt(UserWebServiceClientAdapter.JSON_IDSERVER));
                }

                if (json.has(UserWebServiceClientAdapter.JSON_FIRSTNAME)
                        && !json.isNull(UserWebServiceClientAdapter.JSON_FIRSTNAME)) {
                    user.setFirstname(
                            json.getString(UserWebServiceClientAdapter.JSON_FIRSTNAME));
                }

                if (json.has(UserWebServiceClientAdapter.JSON_LASTNAME)
                        && !json.isNull(UserWebServiceClientAdapter.JSON_LASTNAME)) {
                    user.setLastname(
                            json.getString(UserWebServiceClientAdapter.JSON_LASTNAME));
                }

                if (json.has(UserWebServiceClientAdapter.JSON_EMAIL)
                        && !json.isNull(UserWebServiceClientAdapter.JSON_EMAIL)) {
                    user.setEmail(
                            json.getString(UserWebServiceClientAdapter.JSON_EMAIL));
                }

                if (json.has(UserWebServiceClientAdapter.JSON_ISELIGIBLE)
                        && !json.isNull(UserWebServiceClientAdapter.JSON_ISELIGIBLE)) {
                    user.setIsEligible(
                            json.getBoolean(UserWebServiceClientAdapter.JSON_ISELIGIBLE));
                }

                if (json.has(UserWebServiceClientAdapter.JSON_IDSMARTPHONE)
                        && !json.isNull(UserWebServiceClientAdapter.JSON_IDSMARTPHONE)) {
                    user.setIdSmartphone(
                            json.getString(UserWebServiceClientAdapter.JSON_IDSMARTPHONE));
                }

                if (json.has(UserWebServiceClientAdapter.JSON_DATERGPD)
                        && !json.isNull(UserWebServiceClientAdapter.JSON_DATERGPD)) {
                    DateTimeFormatter dateRgpdFormatter = DateTimeFormat.forPattern(
                            UserWebServiceClientAdapter.REST_UPDATE_DATE_FORMAT);
                    try {
                        user.setDateRgpd(
                                dateRgpdFormatter.withOffsetParsed().parseDateTime(
                                        json.getString(
                                        UserWebServiceClientAdapter.JSON_DATERGPD)));
                    } catch (IllegalArgumentException e) {
                        Log.e(TAG, e.getMessage());
                    }
                }

                if (json.has(UserWebServiceClientAdapter.JSON_TOKEN)
                        && !json.isNull(UserWebServiceClientAdapter.JSON_TOKEN)) {
                    user.setToken(
                            json.getString(UserWebServiceClientAdapter.JSON_TOKEN));
                }

                if (json.has(UserWebServiceClientAdapter.JSON_JOB)
                        && !json.isNull(UserWebServiceClientAdapter.JSON_JOB)) {

                    try {
                        JobWebServiceClientAdapter jobAdapter =
                                new JobWebServiceClientAdapter(this.context);
                        Job job =
                                new Job();

                        if (jobAdapter.extract(
                                json.optJSONObject(
                                        UserWebServiceClientAdapter.JSON_JOB),
                                        job)) {
                            user.setJob(job);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Json doesn't contains Job data");
                    }
                }

                if (json.has(UserWebServiceClientAdapter.JSON_USERWORKINGTIMES)
                        && !json.isNull(UserWebServiceClientAdapter.JSON_USERWORKINGTIMES)) {
                    ArrayList<WorkingTime> userWorkingTimes =
                            new ArrayList<WorkingTime>();
                    WorkingTimeWebServiceClientAdapter userWorkingTimesAdapter =
                            new WorkingTimeWebServiceClientAdapter(this.context);

                    try {
                        //.optJSONObject(UserWebServiceClientAdapter.JSON_USERWORKINGTIMES);
                        userWorkingTimesAdapter.extractItems(
                                json, UserWebServiceClientAdapter.JSON_USERWORKINGTIMES,
                                userWorkingTimes);
                        user.setUserWorkingTimes(userWorkingTimes);
                    } catch (JSONException e) {
                        Log.e(TAG, e.getMessage());
                    }
                }

                if (json.has(UserWebServiceClientAdapter.JSON_CREATEDPROJECTS)
                        && !json.isNull(UserWebServiceClientAdapter.JSON_CREATEDPROJECTS)) {
                    ArrayList<Project> createdProjects =
                            new ArrayList<Project>();
                    ProjectWebServiceClientAdapter createdProjectsAdapter =
                            new ProjectWebServiceClientAdapter(this.context);

                    try {
                        //.optJSONObject(UserWebServiceClientAdapter.JSON_CREATEDPROJECTS);
                        createdProjectsAdapter.extractItems(
                                json, UserWebServiceClientAdapter.JSON_CREATEDPROJECTS,
                                createdProjects);
                        user.setCreatedProjects(createdProjects);
                    } catch (JSONException e) {
                        Log.e(TAG, e.getMessage());
                    }
                }
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
            }
        }

        return result;
    }

    /**
     * Convert a User to a JSONObject.
     * @param user The User to convert
     * @return The converted User
     */
    public JSONObject itemToJson(User user) {
        JSONObject params = new JSONObject();
        try {
            params.put(UserWebServiceClientAdapter.JSON_ID,
                    user.getId());
            params.put(UserWebServiceClientAdapter.JSON_IDSERVER,
                    user.getIdServer());
            params.put(UserWebServiceClientAdapter.JSON_FIRSTNAME,
                    user.getFirstname());
            params.put(UserWebServiceClientAdapter.JSON_LASTNAME,
                    user.getLastname());
            params.put(UserWebServiceClientAdapter.JSON_EMAIL,
                    user.getEmail());
            params.put(UserWebServiceClientAdapter.JSON_ISELIGIBLE,
                    user.isIsEligible());
            params.put(UserWebServiceClientAdapter.JSON_IDSMARTPHONE,
                    user.getIdSmartphone());

            if (user.getDateRgpd() != null) {
                params.put(UserWebServiceClientAdapter.JSON_DATERGPD,
                        user.getDateRgpd().toString(REST_UPDATE_DATE_FORMAT));
            }
            params.put(UserWebServiceClientAdapter.JSON_TOKEN,
                    user.getToken());

            if (user.getJob() != null) {
                JobWebServiceClientAdapter jobAdapter =
                        new JobWebServiceClientAdapter(this.context);

                params.put(UserWebServiceClientAdapter.JSON_JOB,
                        jobAdapter.itemIdToJson(user.getJob()));
            }

            if (user.getUserWorkingTimes() != null) {
                WorkingTimeWebServiceClientAdapter userWorkingTimesAdapter =
                        new WorkingTimeWebServiceClientAdapter(this.context);

                params.put(UserWebServiceClientAdapter.JSON_USERWORKINGTIMES,
                        userWorkingTimesAdapter.itemsIdToJson(user.getUserWorkingTimes()));
            }

            if (user.getCreatedProjects() != null) {
                ProjectWebServiceClientAdapter createdProjectsAdapter =
                        new ProjectWebServiceClientAdapter(this.context);

                params.put(UserWebServiceClientAdapter.JSON_CREATEDPROJECTS,
                        createdProjectsAdapter.itemsIdToJson(user.getCreatedProjects()));
            }
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }

        return params;
    }


    /**
     * Convert a <T> to a JSONObject.
     * @param item The <T> to convert
     * @return The converted <T>
     */
    public JSONObject itemIdToJson(User item) {
        JSONObject params = new JSONObject();
        try {
            params.put(UserWebServiceClientAdapter.JSON_ID, item.getId());
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }

        return params;
    }


    /**
     * Converts a content value reprensenting a User to a JSONObject.
     * @param values The content values
     * @return The JSONObject
     */
    public JSONObject contentValuesToJson(ContentValues values) {
        JSONObject params = new JSONObject();

        try {
            params.put(UserWebServiceClientAdapter.JSON_ID,
                    values.get(UserContract.COL_ID));
            params.put(UserWebServiceClientAdapter.JSON_IDSERVER,
                    values.get(UserContract.COL_IDSERVER));
            params.put(UserWebServiceClientAdapter.JSON_FIRSTNAME,
                    values.get(UserContract.COL_FIRSTNAME));
            params.put(UserWebServiceClientAdapter.JSON_LASTNAME,
                    values.get(UserContract.COL_LASTNAME));
            params.put(UserWebServiceClientAdapter.JSON_EMAIL,
                    values.get(UserContract.COL_EMAIL));
            params.put(UserWebServiceClientAdapter.JSON_ISELIGIBLE,
                    values.get(UserContract.COL_ISELIGIBLE));
            params.put(UserWebServiceClientAdapter.JSON_IDSMARTPHONE,
                    values.get(UserContract.COL_IDSMARTPHONE));
            params.put(UserWebServiceClientAdapter.JSON_DATERGPD,
                    new DateTime(values.get(
                            UserContract.COL_DATERGPD)).toString(REST_UPDATE_DATE_FORMAT));
            params.put(UserWebServiceClientAdapter.JSON_TOKEN,
                    values.get(UserContract.COL_TOKEN));
            JobWebServiceClientAdapter jobAdapter =
                    new JobWebServiceClientAdapter(this.context);

            params.put(UserWebServiceClientAdapter.JSON_JOB,
                    jobAdapter.contentValuesToJson(values));
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }

        return params;
    }

    /**
     * Extract a list of <T> from a JSONObject describing an array of <T> given the array name.
     * @param json The JSONObject describing the array of <T>
     * @param items The returned list of <T>
     * @param paramName The name of the array
     * @param limit Limit the number of items to parse
     * @return The number of <T> found in the JSON
     */
    public int extractItems(JSONObject json,
            String paramName,
            List<User> items,
            int limit) throws JSONException {

        JSONArray itemArray = json.optJSONArray(paramName);

        int result = -1;

        if (itemArray != null) {
            int count = itemArray.length();

            if (limit > 0 && count > limit) {
                count = limit;
            }

            for (int i = 0; i < count; i++) {
                JSONObject jsonItem = itemArray.getJSONObject(i);
                User item = new User();
                this.extract(jsonItem, item);
                items.add(item);
            }
        }

        if (!json.isNull("Meta")) {
            JSONObject meta = json.optJSONObject("Meta");
            result = meta.optInt("nbt",0);
        }

        return result;
    }

    /**
     * Extract a list of <T> from a JSONObject describing an array of <T> given the array name.
     * @param json The JSONObject describing the array of <T>
     * @param items The returned list of <T>
     * @param paramName The name of the array
     * @return The number of <T> found in the JSON
     */
    public int extractItems(JSONObject json,
            String paramName,
            List<User> items) throws JSONException {

        return this.extractItems(json, paramName, items, 0);
    }

}

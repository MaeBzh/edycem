/*
 * JobWebServiceClientAdapterBase.java, Edycem Android
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
import com.imie.edycem.entity.Job;
import com.imie.edycem.data.RestClient.Verb;
import com.imie.edycem.provider.contract.JobContract;

import com.imie.edycem.entity.User;
import com.imie.edycem.entity.Project;


/**
 *
 * <b><i>This class will be overwrited whenever you regenerate the project with Harmony.
 * You should edit JobWebServiceClientAdapter class instead of this one or you will lose all your modifications.</i></b>
 *
 */
public abstract class JobWebServiceClientAdapterBase
        extends WebServiceClientAdapter<Job> {
    /** JobWebServiceClientAdapterBase TAG. */
    protected static final String TAG = "JobWSClientAdapter";

    /** JSON Object Job pattern. */
    protected static String JSON_OBJECT_JOB = "Job";
    /** JSON_ID attributes. */
    protected static String JSON_ID = "id";
    /** JSON_IDSERVER attributes. */
    protected static String JSON_IDSERVER = "id";
    /** JSON_NAME attributes. */
    protected static String JSON_NAME = "name";
    /** JSON_USERS attributes. */
    protected static String JSON_USERS = "users";
    /** JSON_PROJECTS attributes. */
    protected static String JSON_PROJECTS = "projects";

    /** Rest Date Format pattern. */
    public static final String REST_UPDATE_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";

    /** Time pattern.*/
    public static final String TIME_FORMAT = "HH:mm:ss";

    /**
     * Constructor with overriden port and host.
     *
     * @param context The context
     */
    public JobWebServiceClientAdapterBase(Context context) {
        this(context, null);
    }

    /**
     * Constructor with overriden port.
     *
     * @param context The context
     * @param port The overriden port
     */
    public JobWebServiceClientAdapterBase(Context context,
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
    public JobWebServiceClientAdapterBase(Context context,
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
    public JobWebServiceClientAdapterBase(Context context,
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
    public JobWebServiceClientAdapterBase(Context context,
            String host, Integer port, String scheme, String prefix) {
        super(context, host, port, scheme, prefix);

        
    }

    /**
     * Retrieve all the Jobs in the given list. Uses the route : Job.
     * @param jobs : The list in which the Jobs will be returned
     * @return The number of Jobs returned
     */
    public int getAll(List<Job> jobs) {
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
                result = extractItems(json, "Jobs", jobs);
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
        return "job";
    }

    /**
     * Retrieve one Job. Uses the route : Job/%id%.
     * @param job : The Job to retrieve (set the ID)
     * @return -1 if an error has occurred. 0 if not.
     */
    public int get(Job job) {
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
                if (extract(json, job)) {
                    result = 0;
                }
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
            }
        }

        return result;
    }

    /**
     * Update a Job. Uses the route : Job/%id%.
     * @param job : The Job to update
     * @return -1 if an error has occurred. 0 if not.
     */
    public int update(Job job) {
        int result = -1;
        String response = this.invokeRequest(
                    Verb.PUT,
                    String.format(
                        this.getUri() + "/%s%s",
                        job.getId(),
                        REST_FORMAT),
                    itemToJson(job));

        if (this.isValidResponse(response) && this.isValidRequest()) {
            try {
                JSONObject json = new JSONObject(response);
                this.extract(json, job);
                result = 0;
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
            }
        }

        return result;
    }

    /**
     * Delete a Job. Uses the route : Job/%id%.
     * @param job : The Job to delete (only the id is necessary)
     * @return -1 if an error has occurred. 0 if not.
     */
    public int delete(Job job) {
        int result = -1;
        String response = this.invokeRequest(
                    Verb.DELETE,
                    String.format(
                        this.getUri() + "/%s%s",
                        job.getId(),
                        REST_FORMAT),
                    null);

        if (this.isValidResponse(response) && this.isValidRequest()) {
            result = 0;
        }

        return result;
    }




    /**
     * Tests if the json is a valid Job Object.
     *
     * @param json The json
     *
     * @return True if valid
     */
    public boolean isValidJSON(JSONObject json) {
        boolean result = true;
        result = result && json.has(JobWebServiceClientAdapter.JSON_ID);

        return result;
    }

    /**
     * Extract a Job from a JSONObject describing a Job.
     * @param json The JSONObject describing the Job
     * @param job The returned Job
     * @return true if a Job was found. false if not
     */
    public boolean extract(JSONObject json, Job job) {
        boolean result = this.isValidJSON(json);
        if (result) {
            try {

                if (json.has(JobWebServiceClientAdapter.JSON_ID)
                        && !json.isNull(JobWebServiceClientAdapter.JSON_ID)) {
                    job.setId(
                            json.getInt(JobWebServiceClientAdapter.JSON_ID));
                }

                if (json.has(JobWebServiceClientAdapter.JSON_IDSERVER)
                        && !json.isNull(JobWebServiceClientAdapter.JSON_IDSERVER)) {
                    job.setIdServer(
                            json.getInt(JobWebServiceClientAdapter.JSON_IDSERVER));
                }

                if (json.has(JobWebServiceClientAdapter.JSON_NAME)
                        && !json.isNull(JobWebServiceClientAdapter.JSON_NAME)) {
                    job.setName(
                            json.getString(JobWebServiceClientAdapter.JSON_NAME));
                }

                if (json.has(JobWebServiceClientAdapter.JSON_USERS)
                        && !json.isNull(JobWebServiceClientAdapter.JSON_USERS)) {
                    ArrayList<User> users =
                            new ArrayList<User>();
                    UserWebServiceClientAdapter usersAdapter =
                            new UserWebServiceClientAdapter(this.context);

                    try {
                        //.optJSONObject(JobWebServiceClientAdapter.JSON_USERS);
                        usersAdapter.extractItems(
                                json, JobWebServiceClientAdapter.JSON_USERS,
                                users);
                        job.setUsers(users);
                    } catch (JSONException e) {
                        Log.e(TAG, e.getMessage());
                    }
                }

                if (json.has(JobWebServiceClientAdapter.JSON_PROJECTS)
                        && !json.isNull(JobWebServiceClientAdapter.JSON_PROJECTS)) {
                    ArrayList<Project> projects =
                            new ArrayList<Project>();
                    ProjectWebServiceClientAdapter projectsAdapter =
                            new ProjectWebServiceClientAdapter(this.context);

                    try {
                        //.optJSONObject(JobWebServiceClientAdapter.JSON_PROJECTS);
                        projectsAdapter.extractItems(
                                json, JobWebServiceClientAdapter.JSON_PROJECTS,
                                projects);
                        job.setProjects(projects);
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
     * Convert a Job to a JSONObject.
     * @param job The Job to convert
     * @return The converted Job
     */
    public JSONObject itemToJson(Job job) {
        JSONObject params = new JSONObject();
        try {
            params.put(JobWebServiceClientAdapter.JSON_ID,
                    job.getId());
            params.put(JobWebServiceClientAdapter.JSON_IDSERVER,
                    job.getIdServer());
            params.put(JobWebServiceClientAdapter.JSON_NAME,
                    job.getName());

            if (job.getUsers() != null) {
                UserWebServiceClientAdapter usersAdapter =
                        new UserWebServiceClientAdapter(this.context);

                params.put(JobWebServiceClientAdapter.JSON_USERS,
                        usersAdapter.itemsIdToJson(job.getUsers()));
            }

            if (job.getProjects() != null) {
                ProjectWebServiceClientAdapter projectsAdapter =
                        new ProjectWebServiceClientAdapter(this.context);

                params.put(JobWebServiceClientAdapter.JSON_PROJECTS,
                        projectsAdapter.itemsIdToJson(job.getProjects()));
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
    public JSONObject itemIdToJson(Job item) {
        JSONObject params = new JSONObject();
        try {
            params.put(JobWebServiceClientAdapter.JSON_ID, item.getId());
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }

        return params;
    }


    /**
     * Converts a content value reprensenting a Job to a JSONObject.
     * @param values The content values
     * @return The JSONObject
     */
    public JSONObject contentValuesToJson(ContentValues values) {
        JSONObject params = new JSONObject();

        try {
            params.put(JobWebServiceClientAdapter.JSON_ID,
                    values.get(JobContract.COL_ID));
            params.put(JobWebServiceClientAdapter.JSON_IDSERVER,
                    values.get(JobContract.COL_IDSERVER));
            params.put(JobWebServiceClientAdapter.JSON_NAME,
                    values.get(JobContract.COL_NAME));
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
            List<Job> items,
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
                Job item = new Job();
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
            List<Job> items) throws JSONException {

        return this.extractItems(json, paramName, items, 0);
    }

}

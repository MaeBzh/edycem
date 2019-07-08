/*
 * ActivityWebServiceClientAdapterBase.java, Edycem Android
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
import com.imie.edycem.entity.Activity;
import com.imie.edycem.data.RestClient.Verb;
import com.imie.edycem.provider.contract.ActivityContract;

import com.imie.edycem.entity.Task;


/**
 *
 * <b><i>This class will be overwrited whenever you regenerate the project with Harmony.
 * You should edit ActivityWebServiceClientAdapter class instead of this one or you will lose all your modifications.</i></b>
 *
 */
public abstract class ActivityWebServiceClientAdapterBase
        extends WebServiceClientAdapter<Activity> {
    /** ActivityWebServiceClientAdapterBase TAG. */
    protected static final String TAG = "ActivityWSClientAdapter";

    /** JSON Object Activity pattern. */
    protected static String JSON_OBJECT_ACTIVITY = "Activity";
    /** JSON_ID attributes. */
    protected static String JSON_ID = "id";
    /** JSON_IDSERVER attributes. */
    protected static String JSON_IDSERVER = "id";
    /** JSON_NAME attributes. */
    protected static String JSON_NAME = "name";
    /** JSON_TASKS attributes. */
    protected static String JSON_TASKS = "tasks";

    /** Rest Date Format pattern. */
    public static final String REST_UPDATE_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";

    /** Time pattern.*/
    public static final String TIME_FORMAT = "HH:mm:ss";

    /**
     * Constructor with overriden port and host.
     *
     * @param context The context
     */
    public ActivityWebServiceClientAdapterBase(Context context) {
        this(context, null);
    }

    /**
     * Constructor with overriden port.
     *
     * @param context The context
     * @param port The overriden port
     */
    public ActivityWebServiceClientAdapterBase(Context context,
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
    public ActivityWebServiceClientAdapterBase(Context context,
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
    public ActivityWebServiceClientAdapterBase(Context context,
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
    public ActivityWebServiceClientAdapterBase(Context context,
            String host, Integer port, String scheme, String prefix) {
        super(context, host, port, scheme, prefix);

        
    }

    /**
     * Retrieve all the Activitys in the given list. Uses the route : /activies.
     * @param activitys : The list in which the Activitys will be returned
     * @return The number of Activitys returned
     */
    public int getAll(List<Activity> activitys) {
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
                result = extractItems(json, "Activitys", activitys);
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
        return "activity";
    }

    /**
     * Retrieve one Activity. Uses the route : Activity/%id%.
     * @param activity : The Activity to retrieve (set the ID)
     * @return -1 if an error has occurred. 0 if not.
     */
    public int get(Activity activity) {
        int result = -1;
        String response = this.invokeRequest(
                    Verb.GET,
                    String.format(
                        this.getUri() + "/%s%s",
                        activity.getId(),
                        REST_FORMAT),
                    null);

        if (this.isValidResponse(response) && this.isValidRequest()) {
            try {
                JSONObject json = new JSONObject(response);
                if (extract(json, activity)) {
                    result = 0;
                }
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
            }
        }

        return result;
    }

    /**
     * Update a Activity. Uses the route : Activity/%id%.
     * @param activity : The Activity to update
     * @return -1 if an error has occurred. 0 if not.
     */
    public int update(Activity activity) {
        int result = -1;
        String response = this.invokeRequest(
                    Verb.PUT,
                    String.format(
                        this.getUri() + "/%s%s",
                        activity.getId(),
                        REST_FORMAT),
                    itemToJson(activity));

        if (this.isValidResponse(response) && this.isValidRequest()) {
            try {
                JSONObject json = new JSONObject(response);
                this.extract(json, activity);
                result = 0;
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
            }
        }

        return result;
    }

    /**
     * Delete a Activity. Uses the route : Activity/%id%.
     * @param activity : The Activity to delete (only the id is necessary)
     * @return -1 if an error has occurred. 0 if not.
     */
    public int delete(Activity activity) {
        int result = -1;
        String response = this.invokeRequest(
                    Verb.DELETE,
                    String.format(
                        this.getUri() + "/%s%s",
                        activity.getId(),
                        REST_FORMAT),
                    null);

        if (this.isValidResponse(response) && this.isValidRequest()) {
            result = 0;
        }

        return result;
    }



    /**
     * Tests if the json is a valid Activity Object.
     *
     * @param json The json
     *
     * @return True if valid
     */
    public boolean isValidJSON(JSONObject json) {
        boolean result = true;
        result = result && json.has(ActivityWebServiceClientAdapter.JSON_ID);

        return result;
    }

    /**
     * Extract a Activity from a JSONObject describing a Activity.
     * @param json The JSONObject describing the Activity
     * @param activity The returned Activity
     * @return true if a Activity was found. false if not
     */
    public boolean extract(JSONObject json, Activity activity) {
        boolean result = this.isValidJSON(json);
        if (result) {
            try {

                if (json.has(ActivityWebServiceClientAdapter.JSON_ID)
                        && !json.isNull(ActivityWebServiceClientAdapter.JSON_ID)) {
                    activity.setId(
                            json.getInt(ActivityWebServiceClientAdapter.JSON_ID));
                }

                if (json.has(ActivityWebServiceClientAdapter.JSON_IDSERVER)
                        && !json.isNull(ActivityWebServiceClientAdapter.JSON_IDSERVER)) {
                    activity.setIdServer(
                            json.getInt(ActivityWebServiceClientAdapter.JSON_IDSERVER));
                }

                if (json.has(ActivityWebServiceClientAdapter.JSON_NAME)
                        && !json.isNull(ActivityWebServiceClientAdapter.JSON_NAME)) {
                    activity.setName(
                            json.getString(ActivityWebServiceClientAdapter.JSON_NAME));
                }

                if (json.has(ActivityWebServiceClientAdapter.JSON_TASKS)
                        && !json.isNull(ActivityWebServiceClientAdapter.JSON_TASKS)) {
                    ArrayList<Task> tasks =
                            new ArrayList<Task>();
                    TaskWebServiceClientAdapter tasksAdapter =
                            new TaskWebServiceClientAdapter(this.context);

                    try {
                        //.optJSONObject(ActivityWebServiceClientAdapter.JSON_TASKS);
                        tasksAdapter.extractItems(
                                json, ActivityWebServiceClientAdapter.JSON_TASKS,
                                tasks);
                        activity.setTasks(tasks);
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
     * Convert a Activity to a JSONObject.
     * @param activity The Activity to convert
     * @return The converted Activity
     */
    public JSONObject itemToJson(Activity activity) {
        JSONObject params = new JSONObject();
        try {
            params.put(ActivityWebServiceClientAdapter.JSON_ID,
                    activity.getId());
            params.put(ActivityWebServiceClientAdapter.JSON_IDSERVER,
                    activity.getIdServer());
            params.put(ActivityWebServiceClientAdapter.JSON_NAME,
                    activity.getName());

            if (activity.getTasks() != null) {
                TaskWebServiceClientAdapter tasksAdapter =
                        new TaskWebServiceClientAdapter(this.context);

                params.put(ActivityWebServiceClientAdapter.JSON_TASKS,
                        tasksAdapter.itemsIdToJson(activity.getTasks()));
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
    public JSONObject itemIdToJson(Activity item) {
        JSONObject params = new JSONObject();
        try {
            params.put(ActivityWebServiceClientAdapter.JSON_ID, item.getId());
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }

        return params;
    }


    /**
     * Converts a content value reprensenting a Activity to a JSONObject.
     * @param values The content values
     * @return The JSONObject
     */
    public JSONObject contentValuesToJson(ContentValues values) {
        JSONObject params = new JSONObject();

        try {
            params.put(ActivityWebServiceClientAdapter.JSON_ID,
                    values.get(ActivityContract.COL_ID));
            params.put(ActivityWebServiceClientAdapter.JSON_IDSERVER,
                    values.get(ActivityContract.COL_IDSERVER));
            params.put(ActivityWebServiceClientAdapter.JSON_NAME,
                    values.get(ActivityContract.COL_NAME));
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
            List<Activity> items,
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
                Activity item = new Activity();
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
            List<Activity> items) throws JSONException {

        return this.extractItems(json, paramName, items, 0);
    }

}

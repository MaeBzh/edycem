/*
 * TaskWebServiceClientAdapterBase.java, Edycem Android
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
import com.imie.edycem.entity.Task;
import com.imie.edycem.data.RestClient.Verb;
import com.imie.edycem.provider.contract.TaskContract;

import com.imie.edycem.entity.Activity;
import com.imie.edycem.entity.WorkingTime;


/**
 *
 * <b><i>This class will be overwrited whenever you regenerate the project with Harmony.
 * You should edit TaskWebServiceClientAdapter class instead of this one or you will lose all your modifications.</i></b>
 *
 */
public abstract class TaskWebServiceClientAdapterBase
        extends WebServiceClientAdapter<Task> {
    /** TaskWebServiceClientAdapterBase TAG. */
    protected static final String TAG = "TaskWSClientAdapter";

    /** JSON Object Task pattern. */
    protected static String JSON_OBJECT_TASK = "Task";
    /** JSON_ID attributes. */
    protected static String JSON_ID = "id";
    /** JSON_IDSERVER attributes. */
    protected static String JSON_IDSERVER = "id";
    /** JSON_NAME attributes. */
    protected static String JSON_NAME = "name";
    /** JSON_ACTIVITY attributes. */
    protected static String JSON_ACTIVITY = "activity";
    /** JSON_TASKWORKINGTIMES attributes. */
    protected static String JSON_TASKWORKINGTIMES = "taskWorkingTimes";

    /** Rest Date Format pattern. */
    public static final String REST_UPDATE_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";

    /** Time pattern.*/
    public static final String TIME_FORMAT = "HH:mm:ss";

    /**
     * Constructor with overriden port and host.
     *
     * @param context The context
     */
    public TaskWebServiceClientAdapterBase(Context context) {
        this(context, null);
    }

    /**
     * Constructor with overriden port.
     *
     * @param context The context
     * @param port The overriden port
     */
    public TaskWebServiceClientAdapterBase(Context context,
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
    public TaskWebServiceClientAdapterBase(Context context,
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
    public TaskWebServiceClientAdapterBase(Context context,
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
    public TaskWebServiceClientAdapterBase(Context context,
            String host, Integer port, String scheme, String prefix) {
        super(context, host, port, scheme, prefix);

        
    }

    /**
     * Retrieve all the Tasks in the given list. Uses the route : /tasks.
     * @param tasks : The list in which the Tasks will be returned
     * @return The number of Tasks returned
     */
    public int getAll(List<Task> tasks) {
        int result = -1;
        String response = this.invokeRequest(
                    Verb.GET,
                "tasks",
                    null);

        if (this.isValidResponse(response) && this.isValidRequest()) {
            try {
                JSONObject json = new JSONObject(response);
                result = extractItems(json, "Tasks", tasks);
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
        return "task";
    }

    /**
     * Retrieve one Task. Uses the route : Task/%id%.
     * @param task : The Task to retrieve (set the ID)
     * @return -1 if an error has occurred. 0 if not.
     */
    public int get(Task task) {
        int result = -1;
        String response = this.invokeRequest(
                    Verb.GET,
                    String.format(
                        this.getUri() + "/%s%s",
                        task.getId(),
                        REST_FORMAT),
                    null);

        if (this.isValidResponse(response) && this.isValidRequest()) {
            try {
                JSONObject json = new JSONObject(response);
                if (extract(json, task)) {
                    result = 0;
                }
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
            }
        }

        return result;
    }

    /**
     * Update a Task. Uses the route : Task/%id%.
     * @param task : The Task to update
     * @return -1 if an error has occurred. 0 if not.
     */
    public int update(Task task) {
        int result = -1;
        String response = this.invokeRequest(
                    Verb.PUT,
                    String.format(
                        this.getUri() + "/%s%s",
                        task.getId(),
                        REST_FORMAT),
                    itemToJson(task));

        if (this.isValidResponse(response) && this.isValidRequest()) {
            try {
                JSONObject json = new JSONObject(response);
                this.extract(json, task);
                result = 0;
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
            }
        }

        return result;
    }

    /**
     * Delete a Task. Uses the route : Task/%id%.
     * @param task : The Task to delete (only the id is necessary)
     * @return -1 if an error has occurred. 0 if not.
     */
    public int delete(Task task) {
        int result = -1;
        String response = this.invokeRequest(
                    Verb.DELETE,
                    String.format(
                        this.getUri() + "/%s%s",
                        task.getId(),
                        REST_FORMAT),
                    null);

        if (this.isValidResponse(response) && this.isValidRequest()) {
            result = 0;
        }

        return result;
    }

    /**
     * Get the Tasks associated with a Activity. Uses the route : activity/%Activity_id%/task.
     * @param tasks : The list in which the Tasks will be returned
     * @param activity : The associated activity
     * @return The number of Tasks returned
     */
    public int getByActivity(List<Task> tasks, Activity activity) {
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
                result = this.extractItems(json, "Tasks", tasks);
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
            }
        }

        return result;
    }



    /**
     * Tests if the json is a valid Task Object.
     *
     * @param json The json
     *
     * @return True if valid
     */
    public boolean isValidJSON(JSONObject json) {
        boolean result = true;
        result = result && json.has(TaskWebServiceClientAdapter.JSON_ID);

        return result;
    }

    /**
     * Extract a Task from a JSONObject describing a Task.
     * @param json The JSONObject describing the Task
     * @param task The returned Task
     * @return true if a Task was found. false if not
     */
    public boolean extract(JSONObject json, Task task) {
        boolean result = this.isValidJSON(json);
        if (result) {
            try {

                if (json.has(TaskWebServiceClientAdapter.JSON_ID)
                        && !json.isNull(TaskWebServiceClientAdapter.JSON_ID)) {
                    task.setId(
                            json.getInt(TaskWebServiceClientAdapter.JSON_ID));
                }

                if (json.has(TaskWebServiceClientAdapter.JSON_IDSERVER)
                        && !json.isNull(TaskWebServiceClientAdapter.JSON_IDSERVER)) {
                    task.setIdServer(
                            json.getInt(TaskWebServiceClientAdapter.JSON_IDSERVER));
                }

                if (json.has(TaskWebServiceClientAdapter.JSON_NAME)
                        && !json.isNull(TaskWebServiceClientAdapter.JSON_NAME)) {
                    task.setName(
                            json.getString(TaskWebServiceClientAdapter.JSON_NAME));
                }

                if (json.has(TaskWebServiceClientAdapter.JSON_ACTIVITY)
                        && !json.isNull(TaskWebServiceClientAdapter.JSON_ACTIVITY)) {

                    try {
                        ActivityWebServiceClientAdapter activityAdapter =
                                new ActivityWebServiceClientAdapter(this.context);
                        Activity activity =
                                new Activity();

                        if (activityAdapter.extract(
                                json.optJSONObject(
                                        TaskWebServiceClientAdapter.JSON_ACTIVITY),
                                        activity)) {
                            task.setActivity(activity);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Json doesn't contains Activity data");
                    }
                }

                if (json.has(TaskWebServiceClientAdapter.JSON_TASKWORKINGTIMES)
                        && !json.isNull(TaskWebServiceClientAdapter.JSON_TASKWORKINGTIMES)) {
                    ArrayList<WorkingTime> taskWorkingTimes =
                            new ArrayList<WorkingTime>();
                    WorkingTimeWebServiceClientAdapter taskWorkingTimesAdapter =
                            new WorkingTimeWebServiceClientAdapter(this.context);

                    try {
                        //.optJSONObject(TaskWebServiceClientAdapter.JSON_TASKWORKINGTIMES);
                        taskWorkingTimesAdapter.extractItems(
                                json, TaskWebServiceClientAdapter.JSON_TASKWORKINGTIMES,
                                taskWorkingTimes);
                        task.setTaskWorkingTimes(taskWorkingTimes);
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
     * Convert a Task to a JSONObject.
     * @param task The Task to convert
     * @return The converted Task
     */
    public JSONObject itemToJson(Task task) {
        JSONObject params = new JSONObject();
        try {
            params.put(TaskWebServiceClientAdapter.JSON_ID,
                    task.getId());
            params.put(TaskWebServiceClientAdapter.JSON_IDSERVER,
                    task.getIdServer());
            params.put(TaskWebServiceClientAdapter.JSON_NAME,
                    task.getName());

            if (task.getActivity() != null) {
                ActivityWebServiceClientAdapter activityAdapter =
                        new ActivityWebServiceClientAdapter(this.context);

                params.put(TaskWebServiceClientAdapter.JSON_ACTIVITY,
                        activityAdapter.itemIdToJson(task.getActivity()));
            }

            if (task.getTaskWorkingTimes() != null) {
                WorkingTimeWebServiceClientAdapter taskWorkingTimesAdapter =
                        new WorkingTimeWebServiceClientAdapter(this.context);

                params.put(TaskWebServiceClientAdapter.JSON_TASKWORKINGTIMES,
                        taskWorkingTimesAdapter.itemsIdToJson(task.getTaskWorkingTimes()));
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
    public JSONObject itemIdToJson(Task item) {
        JSONObject params = new JSONObject();
        try {
            params.put(TaskWebServiceClientAdapter.JSON_ID, item.getId());
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }

        return params;
    }


    /**
     * Converts a content value reprensenting a Task to a JSONObject.
     * @param values The content values
     * @return The JSONObject
     */
    public JSONObject contentValuesToJson(ContentValues values) {
        JSONObject params = new JSONObject();

        try {
            params.put(TaskWebServiceClientAdapter.JSON_ID,
                    values.get(TaskContract.COL_ID));
            params.put(TaskWebServiceClientAdapter.JSON_IDSERVER,
                    values.get(TaskContract.COL_IDSERVER));
            params.put(TaskWebServiceClientAdapter.JSON_NAME,
                    values.get(TaskContract.COL_NAME));
            ActivityWebServiceClientAdapter activityAdapter =
                    new ActivityWebServiceClientAdapter(this.context);

            params.put(TaskWebServiceClientAdapter.JSON_ACTIVITY,
                    activityAdapter.contentValuesToJson(values));
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
            List<Task> items,
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
                Task item = new Task();
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
            List<Task> items) throws JSONException {

        return this.extractItems(json, paramName, items, 0);
    }

}

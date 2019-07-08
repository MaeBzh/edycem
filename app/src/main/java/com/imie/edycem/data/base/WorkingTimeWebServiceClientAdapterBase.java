/*
 * WorkingTimeWebServiceClientAdapterBase.java, Edycem Android
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
import com.imie.edycem.entity.WorkingTime;
import com.imie.edycem.data.RestClient.Verb;
import com.imie.edycem.provider.contract.WorkingTimeContract;

import com.imie.edycem.entity.User;
import com.imie.edycem.entity.Project;
import com.imie.edycem.entity.Task;


/**
 *
 * <b><i>This class will be overwrited whenever you regenerate the project with Harmony.
 * You should edit WorkingTimeWebServiceClientAdapter class instead of this one or you will lose all your modifications.</i></b>
 *
 */
public abstract class WorkingTimeWebServiceClientAdapterBase
        extends WebServiceClientAdapter<WorkingTime> {
    /** WorkingTimeWebServiceClientAdapterBase TAG. */
    protected static final String TAG = "WorkingTimeWSClientAdapter";

    /** JSON Object WorkingTime pattern. */
    protected static String JSON_OBJECT_WORKINGTIME = "WorkingTime";
    /** JSON_ID attributes. */
    protected static String JSON_ID = "id";
    /** JSON_DATE attributes. */
    protected static String JSON_DATE = "date";
    /** JSON_SPENTTIME attributes. */
    protected static String JSON_SPENTTIME = "spentTime";
    /** JSON_DESCRIPTION attributes. */
    protected static String JSON_DESCRIPTION = "description";
    /** JSON_USER attributes. */
    protected static String JSON_USER = "user";
    /** JSON_PROJECT attributes. */
    protected static String JSON_PROJECT = "project";
    /** JSON_TASK attributes. */
    protected static String JSON_TASK = "task";

    /** Rest Date Format pattern. */
    public static final String REST_UPDATE_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";

    /** Time pattern.*/
    public static final String TIME_FORMAT = "HH:mm:ss";

    /**
     * Constructor with overriden port and host.
     *
     * @param context The context
     */
    public WorkingTimeWebServiceClientAdapterBase(Context context) {
        this(context, null);
    }

    /**
     * Constructor with overriden port.
     *
     * @param context The context
     * @param port The overriden port
     */
    public WorkingTimeWebServiceClientAdapterBase(Context context,
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
    public WorkingTimeWebServiceClientAdapterBase(Context context,
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
    public WorkingTimeWebServiceClientAdapterBase(Context context,
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
    public WorkingTimeWebServiceClientAdapterBase(Context context,
            String host, Integer port, String scheme, String prefix) {
        super(context, host, port, scheme, prefix);

        
    }

    /**
     * Retrieve all the WorkingTimes in the given list. Uses the route : WorkingTime.
     * @param workingTimes : The list in which the WorkingTimes will be returned
     * @return The number of WorkingTimes returned
     */
    public int getAll(List<WorkingTime> workingTimes) {
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
                result = extractItems(json, "WorkingTimes", workingTimes);
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
        return "workingtime";
    }

    /**
     * Retrieve one WorkingTime. Uses the route : WorkingTime/%id%.
     * @param workingTime : The WorkingTime to retrieve (set the ID)
     * @return -1 if an error has occurred. 0 if not.
     */
    public int get(WorkingTime workingTime) {
        int result = -1;
        String response = this.invokeRequest(
                    Verb.GET,
                    String.format(
                        this.getUri() + "/%s%s",
                        workingTime.getId(),
                        REST_FORMAT),
                    null);

        if (this.isValidResponse(response) && this.isValidRequest()) {
            try {
                JSONObject json = new JSONObject(response);
                if (extract(json, workingTime)) {
                    result = 0;
                }
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
            }
        }

        return result;
    }

    /**
     * Update a WorkingTime. Uses the route : WorkingTime/%id%.
     * @param workingTime : The WorkingTime to update
     * @return -1 if an error has occurred. 0 if not.
     */
    public int update(WorkingTime workingTime) {
        int result = -1;
        String response = this.invokeRequest(
                    Verb.PUT,
                    String.format(
                        this.getUri() + "/%s%s",
                        workingTime.getId(),
                        REST_FORMAT),
                    itemToJson(workingTime));

        if (this.isValidResponse(response) && this.isValidRequest()) {
            try {
                JSONObject json = new JSONObject(response);
                this.extract(json, workingTime);
                result = 0;
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
            }
        }

        return result;
    }

    /**
     * Delete a WorkingTime. Uses the route : WorkingTime/%id%.
     * @param workingTime : The WorkingTime to delete (only the id is necessary)
     * @return -1 if an error has occurred. 0 if not.
     */
    public int delete(WorkingTime workingTime) {
        int result = -1;
        String response = this.invokeRequest(
                    Verb.DELETE,
                    String.format(
                        this.getUri() + "/%s%s",
                        workingTime.getId(),
                        REST_FORMAT),
                    null);

        if (this.isValidResponse(response) && this.isValidRequest()) {
            result = 0;
        }

        return result;
    }

    /**
     * Get the WorkingTimes associated with a User. Uses the route : user/%User_id%/workingtime.
     * @param workingTimes : The list in which the WorkingTimes will be returned
     * @param user : The associated user
     * @return The number of WorkingTimes returned
     */
    public int getByUser(List<WorkingTime> workingTimes, User user) {
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
                result = this.extractItems(json, "WorkingTimes", workingTimes);
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
            }
        }

        return result;
    }

    /**
     * Get the WorkingTimes associated with a Project. Uses the route : project/%Project_id%/workingtime.
     * @param workingTimes : The list in which the WorkingTimes will be returned
     * @param project : The associated project
     * @return The number of WorkingTimes returned
     */
    public int getByProject(List<WorkingTime> workingTimes, Project project) {
        int result = -1;
        String response = this.invokeRequest(
                    Verb.GET,
                    String.format(
                        this.getUri() + "/%s%s",
                        project.getId(),
                        REST_FORMAT),
                    null);

        if (this.isValidResponse(response) && this.isValidRequest()) {
            try {
                JSONObject json = new JSONObject(response);
                result = this.extractItems(json, "WorkingTimes", workingTimes);
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
            }
        }

        return result;
    }

    /**
     * Get the WorkingTimes associated with a Task. Uses the route : task/%Task_id%/workingtime.
     * @param workingTimes : The list in which the WorkingTimes will be returned
     * @param task : The associated task
     * @return The number of WorkingTimes returned
     */
    public int getByTask(List<WorkingTime> workingTimes, Task task) {
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
                result = this.extractItems(json, "WorkingTimes", workingTimes);
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
            }
        }

        return result;
    }


    /**
     * Tests if the json is a valid WorkingTime Object.
     *
     * @param json The json
     *
     * @return True if valid
     */
    public boolean isValidJSON(JSONObject json) {
        boolean result = true;
        result = result && json.has(WorkingTimeWebServiceClientAdapter.JSON_ID);

        return result;
    }

    /**
     * Extract a WorkingTime from a JSONObject describing a WorkingTime.
     * @param json The JSONObject describing the WorkingTime
     * @param workingTime The returned WorkingTime
     * @return true if a WorkingTime was found. false if not
     */
    public boolean extract(JSONObject json, WorkingTime workingTime) {
        boolean result = this.isValidJSON(json);
        if (result) {
            try {

                if (json.has(WorkingTimeWebServiceClientAdapter.JSON_ID)
                        && !json.isNull(WorkingTimeWebServiceClientAdapter.JSON_ID)) {
                    workingTime.setId(
                            json.getInt(WorkingTimeWebServiceClientAdapter.JSON_ID));
                }

                if (json.has(WorkingTimeWebServiceClientAdapter.JSON_DATE)
                        && !json.isNull(WorkingTimeWebServiceClientAdapter.JSON_DATE)) {
                    DateTimeFormatter dateFormatter = DateTimeFormat.forPattern(
                            WorkingTimeWebServiceClientAdapter.REST_UPDATE_DATE_FORMAT);
                    try {
                        workingTime.setDate(
                                dateFormatter.withOffsetParsed().parseDateTime(
                                        json.getString(
                                        WorkingTimeWebServiceClientAdapter.JSON_DATE)));
                    } catch (IllegalArgumentException e) {
                        Log.e(TAG, e.getMessage());
                    }
                }

                if (json.has(WorkingTimeWebServiceClientAdapter.JSON_SPENTTIME)
                        && !json.isNull(WorkingTimeWebServiceClientAdapter.JSON_SPENTTIME)) {
                    workingTime.setSpentTime(
                            json.getInt(WorkingTimeWebServiceClientAdapter.JSON_SPENTTIME));
                }

                if (json.has(WorkingTimeWebServiceClientAdapter.JSON_DESCRIPTION)
                        && !json.isNull(WorkingTimeWebServiceClientAdapter.JSON_DESCRIPTION)) {
                    workingTime.setDescription(
                            json.getString(WorkingTimeWebServiceClientAdapter.JSON_DESCRIPTION));
                }

                if (json.has(WorkingTimeWebServiceClientAdapter.JSON_USER)
                        && !json.isNull(WorkingTimeWebServiceClientAdapter.JSON_USER)) {

                    try {
                        UserWebServiceClientAdapter userAdapter =
                                new UserWebServiceClientAdapter(this.context);
                        User user =
                                new User();

                        if (userAdapter.extract(
                                json.optJSONObject(
                                        WorkingTimeWebServiceClientAdapter.JSON_USER),
                                        user)) {
                            workingTime.setUser(user);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Json doesn't contains User data");
                    }
                }

                if (json.has(WorkingTimeWebServiceClientAdapter.JSON_PROJECT)
                        && !json.isNull(WorkingTimeWebServiceClientAdapter.JSON_PROJECT)) {

                    try {
                        ProjectWebServiceClientAdapter projectAdapter =
                                new ProjectWebServiceClientAdapter(this.context);
                        Project project =
                                new Project();

                        if (projectAdapter.extract(
                                json.optJSONObject(
                                        WorkingTimeWebServiceClientAdapter.JSON_PROJECT),
                                        project)) {
                            workingTime.setProject(project);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Json doesn't contains Project data");
                    }
                }

                if (json.has(WorkingTimeWebServiceClientAdapter.JSON_TASK)
                        && !json.isNull(WorkingTimeWebServiceClientAdapter.JSON_TASK)) {

                    try {
                        TaskWebServiceClientAdapter taskAdapter =
                                new TaskWebServiceClientAdapter(this.context);
                        Task task =
                                new Task();

                        if (taskAdapter.extract(
                                json.optJSONObject(
                                        WorkingTimeWebServiceClientAdapter.JSON_TASK),
                                        task)) {
                            workingTime.setTask(task);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Json doesn't contains Task data");
                    }
                }
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
            }
        }

        return result;
    }

    /**
     * Convert a WorkingTime to a JSONObject.
     * @param workingTime The WorkingTime to convert
     * @return The converted WorkingTime
     */
    public JSONObject itemToJson(WorkingTime workingTime) {
        JSONObject params = new JSONObject();
        try {
            params.put(WorkingTimeWebServiceClientAdapter.JSON_ID,
                    workingTime.getId());

            if (workingTime.getDate() != null) {
                params.put(WorkingTimeWebServiceClientAdapter.JSON_DATE,
                        workingTime.getDate().toString(REST_UPDATE_DATE_FORMAT));
            }
            params.put(WorkingTimeWebServiceClientAdapter.JSON_SPENTTIME,
                    workingTime.getSpentTime());
            params.put(WorkingTimeWebServiceClientAdapter.JSON_DESCRIPTION,
                    workingTime.getDescription());

            if (workingTime.getUser() != null) {
                UserWebServiceClientAdapter userAdapter =
                        new UserWebServiceClientAdapter(this.context);

                params.put(WorkingTimeWebServiceClientAdapter.JSON_USER,
                        userAdapter.itemIdToJson(workingTime.getUser()));
            }

            if (workingTime.getProject() != null) {
                ProjectWebServiceClientAdapter projectAdapter =
                        new ProjectWebServiceClientAdapter(this.context);

                params.put(WorkingTimeWebServiceClientAdapter.JSON_PROJECT,
                        projectAdapter.itemIdToJson(workingTime.getProject()));
            }

            if (workingTime.getTask() != null) {
                TaskWebServiceClientAdapter taskAdapter =
                        new TaskWebServiceClientAdapter(this.context);

                params.put(WorkingTimeWebServiceClientAdapter.JSON_TASK,
                        taskAdapter.itemIdToJson(workingTime.getTask()));
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
    public JSONObject itemIdToJson(WorkingTime item) {
        JSONObject params = new JSONObject();
        try {
            params.put(WorkingTimeWebServiceClientAdapter.JSON_ID, item.getId());
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }

        return params;
    }


    /**
     * Converts a content value reprensenting a WorkingTime to a JSONObject.
     * @param values The content values
     * @return The JSONObject
     */
    public JSONObject contentValuesToJson(ContentValues values) {
        JSONObject params = new JSONObject();

        try {
            params.put(WorkingTimeWebServiceClientAdapter.JSON_ID,
                    values.get(WorkingTimeContract.COL_ID));
            params.put(WorkingTimeWebServiceClientAdapter.JSON_DATE,
                    new DateTime(values.get(
                            WorkingTimeContract.COL_DATE)).toString(REST_UPDATE_DATE_FORMAT));
            params.put(WorkingTimeWebServiceClientAdapter.JSON_SPENTTIME,
                    values.get(WorkingTimeContract.COL_SPENTTIME));
            params.put(WorkingTimeWebServiceClientAdapter.JSON_DESCRIPTION,
                    values.get(WorkingTimeContract.COL_DESCRIPTION));
            UserWebServiceClientAdapter userAdapter =
                    new UserWebServiceClientAdapter(this.context);

            params.put(WorkingTimeWebServiceClientAdapter.JSON_USER,
                    userAdapter.contentValuesToJson(values));
            ProjectWebServiceClientAdapter projectAdapter =
                    new ProjectWebServiceClientAdapter(this.context);

            params.put(WorkingTimeWebServiceClientAdapter.JSON_PROJECT,
                    projectAdapter.contentValuesToJson(values));
            TaskWebServiceClientAdapter taskAdapter =
                    new TaskWebServiceClientAdapter(this.context);

            params.put(WorkingTimeWebServiceClientAdapter.JSON_TASK,
                    taskAdapter.contentValuesToJson(values));
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
            List<WorkingTime> items,
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
                WorkingTime item = new WorkingTime();
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
            List<WorkingTime> items) throws JSONException {

        return this.extractItems(json, paramName, items, 0);
    }

}

/*
 * TaskWebServiceClientAdapter.java, Edycem Android
 *
 * Copyright 2019
 * Description : 
 * Author(s)   : Harmony
 * Licence     : 
 * Last update : Jul 5, 2019
 *
 */
package com.imie.edycem.data;

import android.content.Context;
import android.util.Log;

import com.imie.edycem.data.base.TaskWebServiceClientAdapterBase;
import com.imie.edycem.entity.Activity;
import com.imie.edycem.entity.Task;
import com.imie.edycem.entity.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Rest class for {@link Task} WebServiceClient adapters.
 */
public class TaskWebServiceClientAdapter
        extends TaskWebServiceClientAdapterBase {

    /**
     * Constructor with overriden port and host.
     *
     * @param context The context
     */
    public TaskWebServiceClientAdapter(Context context) {
        super(context);
    }

    /**
     * Constructor with overriden port.
     *
     * @param context The context
     * @param port The overriden port
     */
    public TaskWebServiceClientAdapter(Context context,
            Integer port) {
        super(context, port);
    }

    /**
     * Constructor with overriden port and host.
     *
     * @param context The context
     * @param host The overriden host
     * @param port The overriden port
     */
    public TaskWebServiceClientAdapter(Context context,
            String host, Integer port) {
        super(context, host, port);
    }
    

    /**
     * Constructor with overriden port, host and scheme.
     *
     * @param context The context
     * @param host The overriden host
     * @param port The overriden port
     * @param scheme The overriden scheme
     */
    public TaskWebServiceClientAdapter(Context context,
            String host, Integer port, String scheme) {
        super(context, host, port, scheme);
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
    public TaskWebServiceClientAdapter(Context context,
            String host, Integer port, String scheme, String prefix) {
        super(context, host, port, scheme, prefix);
    }

    public ArrayList<Task> getAllTasks(User user) {

        ArrayList<Task> tasks = new ArrayList<>();
        String response = this.invokeRequest(
                RestClient.Verb.GET,
                String.format(
                        this.getUri() + "?access_token=%s",
                        user.getToken()),
                null);

        if (this.isValidResponse(response) && this.isValidRequest()) {
            try {
                JSONArray json = new JSONArray(response);
                tasks = extractFromJsonArray(json, tasks);
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
                tasks = null;
            }
        }

        return tasks;
    }

    /**
     * Extract object from JSON's response.
     *
     * @param jsonArray  The response retrieve by the HTTRequest
     * @param tasks List of missions extracted by this function
     * @return Return 0 if the extraction was succesfull, -1 if it wasn't.
     */
    public ArrayList<Task> extractFromJsonArray(JSONArray jsonArray, ArrayList<Task> tasks) {

        try {
            int size = jsonArray.length();

            for (int i = 0; i < size; i++) {
                JSONObject jItem = jsonArray.getJSONObject(i);
                Task task = new Task();
                task = this.getTaskFromJson(jItem, task);

                if (task != null) {
                    synchronized (tasks) {
                        tasks.add(task);
                    }
                }
            }
        } catch (JSONException e) {

            Log.e(TAG, "Extraction JSON Array failed" + e.getMessage());

        }
        return tasks;
    }


    /**
     * If json has a 'task' object, extract it before extracting attributes.
     *
     * @param jsonObject the json object
     * @return a task
     */
    public Task getTaskFromJson(JSONObject jsonObject, Task task) {

        TaskWebServiceClientAdapter taskWebserviceAdapter = new TaskWebServiceClientAdapter(this.context);
        task = new Task();
        taskWebserviceAdapter.extract(jsonObject, task);
        return task;
    }
}

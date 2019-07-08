/*
 * ActivityWebServiceClientAdapter.java, Edycem Android
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

import com.imie.edycem.data.base.ActivityWebServiceClientAdapterBase;
import com.imie.edycem.entity.Activity;
import com.imie.edycem.entity.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Rest class for {@link Activity} WebServiceClient adapters.
 */
public class ActivityWebServiceClientAdapter
        extends ActivityWebServiceClientAdapterBase {

    /**
     * Constructor with overriden port and host.
     *
     * @param context The context
     */
    public ActivityWebServiceClientAdapter(Context context) {
        super(context);
    }

    /**
     * Constructor with overriden port.
     *
     * @param context The context
     * @param port    The overriden port
     */
    public ActivityWebServiceClientAdapter(Context context,
                                           Integer port) {
        super(context, port);
    }

    /**
     * Constructor with overriden port and host.
     *
     * @param context The context
     * @param host    The overriden host
     * @param port    The overriden port
     */
    public ActivityWebServiceClientAdapter(Context context,
                                           String host, Integer port) {
        super(context, host, port);
    }


    /**
     * Constructor with overriden port, host and scheme.
     *
     * @param context The context
     * @param host    The overriden host
     * @param port    The overriden port
     * @param scheme  The overriden scheme
     */
    public ActivityWebServiceClientAdapter(Context context,
                                           String host, Integer port, String scheme) {
        super(context, host, port, scheme);
    }

    /**
     * Constructor with overriden port, host, scheme and prefix.
     *
     * @param context The context
     * @param host    The overriden host
     * @param port    The overriden port
     * @param scheme  The overriden scheme
     * @param prefix  The overriden prefix
     */
    public ActivityWebServiceClientAdapter(Context context,
                                           String host, Integer port, String scheme, String prefix) {
        super(context, host, port, scheme, prefix);
    }

    public ArrayList<Activity> getAllActivities(User user) {

        ArrayList<Activity> activities = new ArrayList<>();
        String response = this.invokeRequest(
                RestClient.Verb.GET,
                String.format(
                        this.getUri() + "?access_token=%s",
                        user.getToken()),
                null);

        if (this.isValidResponse(response) && this.isValidRequest()) {
            try {
                JSONArray json = new JSONArray(response);
                activities = extractFromJsonArray(json, activities);
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
                activities = null;
            }
        }

        return activities;
    }

    /**
     * Extract object from JSON's response.
     *
     * @param jsonArray The response retrieve by the HTTRequest
     * @param activities List of missions extracted by this function
     * @return Return 0 if the extraction was succesfull, -1 if it wasn't.
     */
    public ArrayList<Activity> extractFromJsonArray(JSONArray jsonArray, ArrayList<Activity> activities) {

        try {
            int size = jsonArray.length();

            for (int i = 0; i < size; i++) {
                JSONObject jItem = jsonArray.getJSONObject(i);
                Activity activity = new Activity();
                activity = this.getActivityFromJson(jItem, activity);

                if (activity != null) {
                    synchronized (activities) {
                        activities.add(activity);
                    }
                }
            }
        } catch (JSONException e) {

            Log.e(TAG, "Extraction JSON Array failed" + e.getMessage());

        }
        return activities;
    }


    /**
     * If json has a 'activity' object, extract it before extracting attributes.
     *
     * @param jsonObject the json object
     * @return an activity
     */
    public Activity getActivityFromJson(JSONObject jsonObject, Activity activity) {

        ActivityWebServiceClientAdapter activityWebserviceAdapter = new ActivityWebServiceClientAdapter(this.context);
        activity = new Activity();
        activityWebserviceAdapter.extract(jsonObject, activity);
        return activity;
    }
}

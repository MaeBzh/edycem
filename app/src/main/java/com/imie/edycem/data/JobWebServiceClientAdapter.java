/*
 * JobWebServiceClientAdapter.java, Edycem Android
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

import com.imie.edycem.data.base.JobWebServiceClientAdapterBase;
import com.imie.edycem.entity.Activity;
import com.imie.edycem.entity.Job;
import com.imie.edycem.entity.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Rest class for {@link Job} WebServiceClient adapters.
 */
public class JobWebServiceClientAdapter
        extends JobWebServiceClientAdapterBase {

    /**
     * Constructor with overriden port and host.
     *
     * @param context The context
     */
    public JobWebServiceClientAdapter(Context context) {
        super(context);
    }

    /**
     * Constructor with overriden port.
     *
     * @param context The context
     * @param port The overriden port
     */
    public JobWebServiceClientAdapter(Context context,
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
    public JobWebServiceClientAdapter(Context context,
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
    public JobWebServiceClientAdapter(Context context,
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
    public JobWebServiceClientAdapter(Context context,
            String host, Integer port, String scheme, String prefix) {
        super(context, host, port, scheme, prefix);
    }

    public ArrayList<Job> getAllJobs(User user) {

        ArrayList<Job> jobs = new ArrayList<>();
        String response = this.invokeRequest(
                RestClient.Verb.GET,
                String.format(
                        this.getUri() + "?access_token=%s",
                        user.getToken()),
                null);

        if (this.isValidResponse(response) && this.isValidRequest()) {
            try {
                JSONArray json = new JSONArray(response);
                jobs = extractFromJsonArray(json, jobs);
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
                jobs = null;
            }
        }

        return jobs;
    }

    /**
     * Extract object from JSON's response.
     *
     * @param jsonArray  The response retrieve by the HTTRequest
     * @param jobs List of missions extracted by this function
     * @return Return 0 if the extraction was succesfull, -1 if it wasn't.
     */
    public ArrayList<Job> extractFromJsonArray(JSONArray jsonArray, ArrayList<Job> jobs) {

        try {
            int size = jsonArray.length();

            for (int i = 0; i < size; i++) {
                JSONObject jItem = jsonArray.getJSONObject(i);
                Job job = new Job();
                job = this.getActivityFromJson(jItem, job);

                if (job != null) {
                    synchronized (jobs) {
                        jobs.add(job);
                    }
                }
            }
        } catch (JSONException e) {

            Log.e(TAG, "Extraction JSON Array failed" + e.getMessage());

        }
        return jobs;
    }


    /**
     * If json has a 'activity' object, extract it before extracting attributes.
     *
     * @param jsonObject the json object
     * @return a job
     */
    public Job getActivityFromJson(JSONObject jsonObject, Job job) {

        JobWebServiceClientAdapter jobWebserviceAdapter = new JobWebServiceClientAdapter(this.context);
        job = new Job();
        jobWebserviceAdapter.extract(jsonObject, job);
        return job;
    }
}

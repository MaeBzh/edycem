/*
 * ProjectWebServiceClientAdapter.java, Edycem Android
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
import android.os.Parcelable;
import android.util.Log;

import com.imie.edycem.data.base.ProjectWebServiceClientAdapterBase;
import com.imie.edycem.entity.Activity;
import com.imie.edycem.entity.Project;
import com.imie.edycem.entity.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Rest class for {@link Project} WebServiceClient adapters.
 */
public class ProjectWebServiceClientAdapter
        extends ProjectWebServiceClientAdapterBase {

    /**
     * Constructor with overriden port and host.
     *
     * @param context The context
     */
    public ProjectWebServiceClientAdapter(Context context) {
        super(context);
    }

    /**
     * Constructor with overriden port.
     *
     * @param context The context
     * @param port The overriden port
     */
    public ProjectWebServiceClientAdapter(Context context,
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
    public ProjectWebServiceClientAdapter(Context context,
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
    public ProjectWebServiceClientAdapter(Context context,
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
    public ProjectWebServiceClientAdapter(Context context,
            String host, Integer port, String scheme, String prefix) {
        super(context, host, port, scheme, prefix);
    }

    public ArrayList<Project> getAllProjects(User user) {

        ArrayList<Project> projects = new ArrayList<>();
        String response = this.invokeRequest(
                RestClient.Verb.GET,
                String.format(
                        this.getUri() + "?access_token=%s",
                        user.getToken()),
                null);

        if (this.isValidResponse(response) && this.isValidRequest()) {
            try {
                JSONArray json = new JSONArray(response);
                projects = extractFromJsonArray(json, projects);
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
                projects = null;
            }
        }

        return projects;
    }

    public ArrayList<Project> getProjectList(User user) {

        ArrayList<Project> projects = new ArrayList<>();
        String response = this.invokeRequest(
                RestClient.Verb.GET,
                String.format(
                        this.getUri() + "?action=lastProjects&id=%s&access_token=%s",
                        user.getId(),
                        user.getToken()),
                null);

        if (this.isValidResponse(response) && this.isValidRequest()) {
            try {
                JSONArray json = new JSONArray(response);
                projects = extractFromJsonArray(json, projects);
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
                projects = null;
            }
        }

        return projects;
    }

    /**
     * Extract object from JSON's response.
     *
     * @param jsonArray  The response retrieve by the HTTRequest
     * @param projects List of missions extracted by this function
     * @return Return 0 if the extraction was succesfull, -1 if it wasn't.
     */
    public ArrayList<Project> extractFromJsonArray(JSONArray jsonArray, ArrayList<Project> projects) {

        try {
            int size = jsonArray.length();

            for (int i = 0; i < size; i++) {
                JSONObject jItem = jsonArray.getJSONObject(i);
                Project project = new Project();
                project = this.getProjectFromJson(jItem, project);

                if (project != null) {
                    synchronized (projects) {
                        projects.add(project);
                    }
                }
            }
        } catch (JSONException e) {

            Log.e(TAG, "Extraction JSON Array failed" + e.getMessage());

        }
        return projects;
    }


    /**
     * If json has a 'project' object, extract it before extracting attributes.
     *
     * @param jsonObject the json object
     * @return a project
     */
    public Project getProjectFromJson(JSONObject jsonObject, Project project) {

        ProjectWebServiceClientAdapter projectWebServiceClientAdapter = new ProjectWebServiceClientAdapter(this.context);
        project = new Project();
        projectWebServiceClientAdapter.extract(jsonObject, project);
        return project;
    }
}

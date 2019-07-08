/*
 * ProjectWebServiceClientAdapterBase.java, Edycem Android
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
import com.imie.edycem.entity.Project;
import com.imie.edycem.data.RestClient.Verb;
import com.imie.edycem.provider.contract.ProjectContract;

import com.imie.edycem.entity.WorkingTime;
import com.imie.edycem.entity.Job;
import com.imie.edycem.entity.User;


/**
 * <b><i>This class will be overwrited whenever you regenerate the project with Harmony.
 * You should edit ProjectWebServiceClientAdapter class instead of this one or you will lose all your modifications.</i></b>
 */
public abstract class ProjectWebServiceClientAdapterBase
        extends WebServiceClientAdapter<Project> {
    /**
     * ProjectWebServiceClientAdapterBase TAG.
     */
    protected static final String TAG = "ProjectWSClientAdapter";

    /**
     * JSON Object Project pattern.
     */
    protected static String JSON_OBJECT_PROJECT = "Project";
    /**
     * JSON_ID attributes.
     */
    protected static String JSON_ID = "id";
    /**
     * JSON_IDSERVER attributes.
     */
    protected static String JSON_IDSERVER = "id";
    /**
     * JSON_NAME attributes.
     */
    protected static String JSON_NAME = "name";
    /**
     * JSON_DESCRIPTION attributes.
     */
    protected static String JSON_DESCRIPTION = "description";
    /**
     * JSON_CREATEDAT attributes.
     */
    protected static String JSON_CREATEDAT = "createdAt";
    /**
     * JSON_COMPANY attributes.
     */
    protected static String JSON_COMPANY = "company";
    /**
     * JSON_CLAIMANTNAME attributes.
     */
    protected static String JSON_CLAIMANTNAME = "claimantName";
    /**
     * JSON_RELEVANTSITE attributes.
     */
    protected static String JSON_RELEVANTSITE = "relevantSite";
    /**
     * JSON_ELIGIBLECIR attributes.
     */
    protected static String JSON_ELIGIBLECIR = "eligibleCir";
    /**
     * JSON_ASPARTOFPULPIT attributes.
     */
    protected static String JSON_ASPARTOFPULPIT = "asPartOfPulpit";
    /**
     * JSON_DEADLINE attributes.
     */
    protected static String JSON_DEADLINE = "deadline";
    /**
     * JSON_DOCUMENTS attributes.
     */
    protected static String JSON_DOCUMENTS = "documents";
    /**
     * JSON_ACTIVITYTYPE attributes.
     */
    protected static String JSON_ACTIVITYTYPE = "activityType";
    /**
     * JSON_ISVALIDATE attributes.
     */
    protected static String JSON_ISVALIDATE = "isValidate";
    /**
     * JSON_PROJECTWORKINGTIMES attributes.
     */
    protected static String JSON_PROJECTWORKINGTIMES = "projectWorkingTimes";
    /**
     * JSON_JOB attributes.
     */
    protected static String JSON_JOB = "job";
    /**
     * JSON_CREATOR attributes.
     */
    protected static String JSON_CREATOR = "creator";

    /**
     * Rest Date Format pattern.
     */
    public static final String REST_UPDATE_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";

    /**
     * Time pattern.
     */
    public static final String TIME_FORMAT = "HH:mm:ss";

    /**
     * Constructor with overriden port and host.
     *
     * @param context The context
     */
    public ProjectWebServiceClientAdapterBase(Context context) {
        this(context, null);
    }

    /**
     * Constructor with overriden port.
     *
     * @param context The context
     * @param port    The overriden port
     */
    public ProjectWebServiceClientAdapterBase(Context context,
                                              Integer port) {
        this(context, null, port);
    }

    /**
     * Constructor with overriden port and host.
     *
     * @param context The context
     * @param host    The overriden host
     * @param port    The overriden port
     */
    public ProjectWebServiceClientAdapterBase(Context context,
                                              String host, Integer port) {
        this(context, host, port, null);
    }

    /**
     * Constructor with overriden port, host and scheme.
     *
     * @param context The context
     * @param host    The overriden host
     * @param port    The overriden port
     * @param scheme  The overriden scheme
     */
    public ProjectWebServiceClientAdapterBase(Context context,
                                              String host, Integer port, String scheme) {
        this(context, host, port, scheme, null);
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
    public ProjectWebServiceClientAdapterBase(Context context,
                                              String host, Integer port, String scheme, String prefix) {
        super(context, host, port, scheme, prefix);


    }

    /**
     * Retrieve all the Projects in the given list. Uses the route : /projects.
     *
     * @param projects : The list in which the Projects will be returned
     * @return The number of Projects returned
     */
    public int getAll(List<Project> projects) {
        int result = -1;
        String response = this.invokeRequest(
                Verb.GET,
                "projects",
                null);

        if (this.isValidResponse(response) && this.isValidRequest()) {
            try {
                JSONObject json = new JSONObject(response);
                result = extractItems(json, "Projects", projects);
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
        return "project";
    }

    /**
     * Retrieve one Project. Uses the route : Project/%id%.
     *
     * @param project : The Project to retrieve (set the ID)
     * @return -1 if an error has occurred. 0 if not.
     */
    public int get(Project project) {
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
                if (extract(json, project)) {
                    result = 0;
                }
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
            }
        }

        return result;
    }

    /**
     * Update a Project. Uses the route : Project/%id%.
     *
     * @param project : The Project to update
     * @return -1 if an error has occurred. 0 if not.
     */
    public int update(Project project) {
        int result = -1;
        String response = this.invokeRequest(
                Verb.PUT,
                String.format(
                        this.getUri() + "/%s%s",
                        project.getId(),
                        REST_FORMAT),
                itemToJson(project));

        if (this.isValidResponse(response) && this.isValidRequest()) {
            try {
                JSONObject json = new JSONObject(response);
                this.extract(json, project);
                result = 0;
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
            }
        }

        return result;
    }

    /**
     * Delete a Project. Uses the route : Project/%id%.
     *
     * @param project : The Project to delete (only the id is necessary)
     * @return -1 if an error has occurred. 0 if not.
     */
    public int delete(Project project) {
        int result = -1;
        String response = this.invokeRequest(
                Verb.DELETE,
                String.format(
                        this.getUri() + "/%s%s",
                        project.getId(),
                        REST_FORMAT),
                null);

        if (this.isValidResponse(response) && this.isValidRequest()) {
            result = 0;
        }

        return result;
    }


    /**
     * Get the Projects associated with a Job. Uses the route : job/%Job_id%/project.
     *
     * @param projects : The list in which the Projects will be returned
     * @param job      : The associated job
     * @return The number of Projects returned
     */
    public int getByJob(List<Project> projects, Job job) {
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
                result = this.extractItems(json, "Projects", projects);
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
            }
        }

        return result;
    }

    /**
     * Get the Projects associated with a User. Uses the route : user/%User_id%/project.
     *
     * @param projects : The list in which the Projects will be returned
     * @param user     : The associated user
     * @return The number of Projects returned
     */
    public int getByCreator(List<Project> projects, User user) {
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
                result = this.extractItems(json, "Projects", projects);
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
            }
        }

        return result;
    }


    /**
     * Tests if the json is a valid Project Object.
     *
     * @param json The json
     * @return True if valid
     */
    public boolean isValidJSON(JSONObject json) {
        boolean result = true;
        result = result && json.has(ProjectWebServiceClientAdapter.JSON_ID);

        return result;
    }

    /**
     * Extract a Project from a JSONObject describing a Project.
     *
     * @param json    The JSONObject describing the Project
     * @param project The returned Project
     * @return true if a Project was found. false if not
     */
    public boolean extract(JSONObject json, Project project) {
        boolean result = this.isValidJSON(json);
        if (result) {
            try {

                if (json.has(ProjectWebServiceClientAdapter.JSON_ID)
                        && !json.isNull(ProjectWebServiceClientAdapter.JSON_ID)) {
                    project.setId(
                            json.getInt(ProjectWebServiceClientAdapter.JSON_ID));
                }

                if (json.has(ProjectWebServiceClientAdapter.JSON_IDSERVER)
                        && !json.isNull(ProjectWebServiceClientAdapter.JSON_IDSERVER)) {
                    project.setIdServer(
                            json.getInt(ProjectWebServiceClientAdapter.JSON_IDSERVER));
                }

                if (json.has(ProjectWebServiceClientAdapter.JSON_NAME)
                        && !json.isNull(ProjectWebServiceClientAdapter.JSON_NAME)) {
                    project.setName(
                            json.getString(ProjectWebServiceClientAdapter.JSON_NAME));
                }

                if (json.has(ProjectWebServiceClientAdapter.JSON_DESCRIPTION)
                        && !json.isNull(ProjectWebServiceClientAdapter.JSON_DESCRIPTION)) {
                    project.setDescription(
                            json.getString(ProjectWebServiceClientAdapter.JSON_DESCRIPTION));
                }

                if (json.has(ProjectWebServiceClientAdapter.JSON_CREATEDAT)
                        && !json.isNull(ProjectWebServiceClientAdapter.JSON_CREATEDAT)) {
                    DateTimeFormatter createdAtFormatter = DateTimeFormat.forPattern(
                            ProjectWebServiceClientAdapter.REST_UPDATE_DATE_FORMAT);
                    try {
                        project.setCreatedAt(
                                createdAtFormatter.withOffsetParsed().parseDateTime(
                                        json.getString(
                                                ProjectWebServiceClientAdapter.JSON_CREATEDAT)));
                    } catch (IllegalArgumentException e) {
                        Log.e(TAG, e.getMessage());
                    }
                }

                if (json.has(ProjectWebServiceClientAdapter.JSON_COMPANY)
                        && !json.isNull(ProjectWebServiceClientAdapter.JSON_COMPANY)) {
                    project.setCompany(
                            json.getString(ProjectWebServiceClientAdapter.JSON_COMPANY));
                }

                if (json.has(ProjectWebServiceClientAdapter.JSON_CLAIMANTNAME)
                        && !json.isNull(ProjectWebServiceClientAdapter.JSON_CLAIMANTNAME)) {
                    project.setClaimantName(
                            json.getString(ProjectWebServiceClientAdapter.JSON_CLAIMANTNAME));
                }

                if (json.has(ProjectWebServiceClientAdapter.JSON_RELEVANTSITE)
                        && !json.isNull(ProjectWebServiceClientAdapter.JSON_RELEVANTSITE)) {
                    project.setRelevantSite(
                            json.getString(ProjectWebServiceClientAdapter.JSON_RELEVANTSITE));
                }

                if (json.has(ProjectWebServiceClientAdapter.JSON_ELIGIBLECIR)
                        && !json.isNull(ProjectWebServiceClientAdapter.JSON_ELIGIBLECIR)) {
                    project.setEligibleCir(
                            json.getInt(ProjectWebServiceClientAdapter.JSON_ELIGIBLECIR));
                }

                if (json.has(ProjectWebServiceClientAdapter.JSON_ASPARTOFPULPIT)
                        && !json.isNull(ProjectWebServiceClientAdapter.JSON_ASPARTOFPULPIT)) {
                    project.setAsPartOfPulpit(
                            json.getBoolean(ProjectWebServiceClientAdapter.JSON_ASPARTOFPULPIT));
                }

                if (json.has(ProjectWebServiceClientAdapter.JSON_DEADLINE)
                        && !json.isNull(ProjectWebServiceClientAdapter.JSON_DEADLINE)) {
                    DateTimeFormatter deadlineFormatter = DateTimeFormat.forPattern(
                            ProjectWebServiceClientAdapter.REST_UPDATE_DATE_FORMAT);
                    try {
                        project.setDeadline(
                                deadlineFormatter.withOffsetParsed().parseDateTime(
                                        json.getString(
                                                ProjectWebServiceClientAdapter.JSON_DEADLINE)));
                    } catch (IllegalArgumentException e) {
                        Log.e(TAG, e.getMessage());
                    }
                }

                if (json.has(ProjectWebServiceClientAdapter.JSON_DOCUMENTS)
                        && !json.isNull(ProjectWebServiceClientAdapter.JSON_DOCUMENTS)) {
                    project.setDocuments(
                            json.getString(ProjectWebServiceClientAdapter.JSON_DOCUMENTS));
                }

                if (json.has(ProjectWebServiceClientAdapter.JSON_ACTIVITYTYPE)
                        && !json.isNull(ProjectWebServiceClientAdapter.JSON_ACTIVITYTYPE)) {
                    project.setActivityType(
                            json.getString(ProjectWebServiceClientAdapter.JSON_ACTIVITYTYPE));
                }

                if (json.has(ProjectWebServiceClientAdapter.JSON_ISVALIDATE)
                        && !json.isNull(ProjectWebServiceClientAdapter.JSON_ISVALIDATE)) {
                    project.setIsValidate(
                            json.getBoolean(ProjectWebServiceClientAdapter.JSON_ISVALIDATE));
                }

                if (json.has(ProjectWebServiceClientAdapter.JSON_PROJECTWORKINGTIMES)
                        && !json.isNull(ProjectWebServiceClientAdapter.JSON_PROJECTWORKINGTIMES)) {
                    ArrayList<WorkingTime> projectWorkingTimes =
                            new ArrayList<WorkingTime>();
                    WorkingTimeWebServiceClientAdapter projectWorkingTimesAdapter =
                            new WorkingTimeWebServiceClientAdapter(this.context);

                    try {
                        //.optJSONObject(ProjectWebServiceClientAdapter.JSON_PROJECTWORKINGTIMES);
                        projectWorkingTimesAdapter.extractItems(
                                json, ProjectWebServiceClientAdapter.JSON_PROJECTWORKINGTIMES,
                                projectWorkingTimes);
                        project.setProjectWorkingTimes(projectWorkingTimes);
                    } catch (JSONException e) {
                        Log.e(TAG, e.getMessage());
                    }
                }

                if (json.has(ProjectWebServiceClientAdapter.JSON_JOB)
                        && !json.isNull(ProjectWebServiceClientAdapter.JSON_JOB)) {

                    try {
                        JobWebServiceClientAdapter jobAdapter =
                                new JobWebServiceClientAdapter(this.context);
                        Job job =
                                new Job();

                        if (jobAdapter.extract(
                                json.optJSONObject(
                                        ProjectWebServiceClientAdapter.JSON_JOB),
                                job)) {
                            project.setJob(job);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Json doesn't contains Job data");
                    }
                }

                if (json.has(ProjectWebServiceClientAdapter.JSON_CREATOR)
                        && !json.isNull(ProjectWebServiceClientAdapter.JSON_CREATOR)) {

                    try {
                        UserWebServiceClientAdapter creatorAdapter =
                                new UserWebServiceClientAdapter(this.context);
                        User creator =
                                new User();

                        if (creatorAdapter.extract(
                                json.optJSONObject(
                                        ProjectWebServiceClientAdapter.JSON_CREATOR),
                                creator)) {
                            project.setCreator(creator);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Json doesn't contains User data");
                    }
                }
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
            }
        }

        return result;
    }

    /**
     * Convert a Project to a JSONObject.
     *
     * @param project The Project to convert
     * @return The converted Project
     */
    public JSONObject itemToJson(Project project) {
        JSONObject params = new JSONObject();
        try {
            params.put(ProjectWebServiceClientAdapter.JSON_ID,
                    project.getId());
            params.put(ProjectWebServiceClientAdapter.JSON_IDSERVER,
                    project.getIdServer());
            params.put(ProjectWebServiceClientAdapter.JSON_NAME,
                    project.getName());
            params.put(ProjectWebServiceClientAdapter.JSON_DESCRIPTION,
                    project.getDescription());

            if (project.getCreatedAt() != null) {
                params.put(ProjectWebServiceClientAdapter.JSON_CREATEDAT,
                        project.getCreatedAt().toString(REST_UPDATE_DATE_FORMAT));
            }
            params.put(ProjectWebServiceClientAdapter.JSON_COMPANY,
                    project.getCompany());
            params.put(ProjectWebServiceClientAdapter.JSON_CLAIMANTNAME,
                    project.getClaimantName());
            params.put(ProjectWebServiceClientAdapter.JSON_RELEVANTSITE,
                    project.getRelevantSite());
            params.put(ProjectWebServiceClientAdapter.JSON_ELIGIBLECIR,
                    project.getEligibleCir());
            params.put(ProjectWebServiceClientAdapter.JSON_ASPARTOFPULPIT,
                    project.isAsPartOfPulpit());

            if (project.getDeadline() != null) {
                params.put(ProjectWebServiceClientAdapter.JSON_DEADLINE,
                        project.getDeadline().toString(REST_UPDATE_DATE_FORMAT));
            }
            params.put(ProjectWebServiceClientAdapter.JSON_DOCUMENTS,
                    project.getDocuments());
            params.put(ProjectWebServiceClientAdapter.JSON_ACTIVITYTYPE,
                    project.getActivityType());
            params.put(ProjectWebServiceClientAdapter.JSON_ISVALIDATE,
                    project.isIsValidate());

            if (project.getProjectWorkingTimes() != null) {
                WorkingTimeWebServiceClientAdapter projectWorkingTimesAdapter =
                        new WorkingTimeWebServiceClientAdapter(this.context);

                params.put(ProjectWebServiceClientAdapter.JSON_PROJECTWORKINGTIMES,
                        projectWorkingTimesAdapter.itemsIdToJson(project.getProjectWorkingTimes()));
            }

            if (project.getJob() != null) {
                JobWebServiceClientAdapter jobAdapter =
                        new JobWebServiceClientAdapter(this.context);

                params.put(ProjectWebServiceClientAdapter.JSON_JOB,
                        jobAdapter.itemIdToJson(project.getJob()));
            }

            if (project.getCreator() != null) {
                UserWebServiceClientAdapter creatorAdapter =
                        new UserWebServiceClientAdapter(this.context);

                params.put(ProjectWebServiceClientAdapter.JSON_CREATOR,
                        creatorAdapter.itemIdToJson(project.getCreator()));
            }
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }

        return params;
    }


    /**
     * Convert a <T> to a JSONObject.
     *
     * @param item The <T> to convert
     * @return The converted <T>
     */
    public JSONObject itemIdToJson(Project item) {
        JSONObject params = new JSONObject();
        try {
            params.put(ProjectWebServiceClientAdapter.JSON_ID, item.getId());
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }

        return params;
    }


    /**
     * Converts a content value reprensenting a Project to a JSONObject.
     *
     * @param values The content values
     * @return The JSONObject
     */
    public JSONObject contentValuesToJson(ContentValues values) {
        JSONObject params = new JSONObject();

        try {
            params.put(ProjectWebServiceClientAdapter.JSON_ID,
                    values.get(ProjectContract.COL_ID));
            params.put(ProjectWebServiceClientAdapter.JSON_IDSERVER,
                    values.get(ProjectContract.COL_IDSERVER));
            params.put(ProjectWebServiceClientAdapter.JSON_NAME,
                    values.get(ProjectContract.COL_NAME));
            params.put(ProjectWebServiceClientAdapter.JSON_DESCRIPTION,
                    values.get(ProjectContract.COL_DESCRIPTION));
            params.put(ProjectWebServiceClientAdapter.JSON_CREATEDAT,
                    new DateTime(values.get(
                            ProjectContract.COL_CREATEDAT)).toString(REST_UPDATE_DATE_FORMAT));
            params.put(ProjectWebServiceClientAdapter.JSON_COMPANY,
                    values.get(ProjectContract.COL_COMPANY));
            params.put(ProjectWebServiceClientAdapter.JSON_CLAIMANTNAME,
                    values.get(ProjectContract.COL_CLAIMANTNAME));
            params.put(ProjectWebServiceClientAdapter.JSON_RELEVANTSITE,
                    values.get(ProjectContract.COL_RELEVANTSITE));
            params.put(ProjectWebServiceClientAdapter.JSON_ELIGIBLECIR,
                    values.get(ProjectContract.COL_ELIGIBLECIR));
            params.put(ProjectWebServiceClientAdapter.JSON_ASPARTOFPULPIT,
                    values.get(ProjectContract.COL_ASPARTOFPULPIT));
            params.put(ProjectWebServiceClientAdapter.JSON_DEADLINE,
                    new DateTime(values.get(
                            ProjectContract.COL_DEADLINE)).toString(REST_UPDATE_DATE_FORMAT));
            params.put(ProjectWebServiceClientAdapter.JSON_DOCUMENTS,
                    values.get(ProjectContract.COL_DOCUMENTS));
            params.put(ProjectWebServiceClientAdapter.JSON_ACTIVITYTYPE,
                    values.get(ProjectContract.COL_ACTIVITYTYPE));
            params.put(ProjectWebServiceClientAdapter.JSON_ISVALIDATE,
                    values.get(ProjectContract.COL_ISVALIDATE));
            JobWebServiceClientAdapter jobAdapter =
                    new JobWebServiceClientAdapter(this.context);

            params.put(ProjectWebServiceClientAdapter.JSON_JOB,
                    jobAdapter.contentValuesToJson(values));
            UserWebServiceClientAdapter creatorAdapter =
                    new UserWebServiceClientAdapter(this.context);

            params.put(ProjectWebServiceClientAdapter.JSON_CREATOR,
                    creatorAdapter.contentValuesToJson(values));
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }

        return params;
    }

    /**
     * Extract a list of <T> from a JSONObject describing an array of <T> given the array name.
     *
     * @param json      The JSONObject describing the array of <T>
     * @param items     The returned list of <T>
     * @param paramName The name of the array
     * @param limit     Limit the number of items to parse
     * @return The number of <T> found in the JSON
     */
    public int extractItems(JSONObject json,
                            String paramName,
                            List<Project> items,
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
                Project item = new Project();
                this.extract(jsonItem, item);
                items.add(item);
            }
        }

        if (!json.isNull("Meta")) {
            JSONObject meta = json.optJSONObject("Meta");
            result = meta.optInt("nbt", 0);
        }

        return result;
    }

    /**
     * Extract a list of <T> from a JSONObject describing an array of <T> given the array name.
     *
     * @param json      The JSONObject describing the array of <T>
     * @param items     The returned list of <T>
     * @param paramName The name of the array
     * @return The number of <T> found in the JSON
     */
    public int extractItems(JSONObject json,
                            String paramName,
                            List<Project> items) throws JSONException {

        return this.extractItems(json, paramName, items, 0);
    }

}

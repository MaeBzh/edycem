/*
 * SettingsWebServiceClientAdapterBase.java, Edycem Android
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
import com.imie.edycem.entity.Settings;
import com.imie.edycem.data.RestClient.Verb;
import com.imie.edycem.provider.contract.SettingsContract;



/**
 *
 * <b><i>This class will be overwrited whenever you regenerate the project with Harmony.
 * You should edit SettingsWebServiceClientAdapter class instead of this one or you will lose all your modifications.</i></b>
 *
 */
public abstract class SettingsWebServiceClientAdapterBase
        extends WebServiceClientAdapter<Settings> {
    /** SettingsWebServiceClientAdapterBase TAG. */
    protected static final String TAG = "SettingsWSClientAdapter";

    /** JSON Object Settings pattern. */
    protected static String JSON_OBJECT_SETTINGS = "Settings";
    /** JSON_ID attributes. */
    protected static String JSON_ID = "id";
    /** JSON_IDSERVER attributes. */
    protected static String JSON_IDSERVER = "id";
    /** JSON_RGPD attributes. */
    protected static String JSON_RGPD = "rgpd";

    /** Rest Date Format pattern. */
    public static final String REST_UPDATE_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";

    /** Time pattern.*/
    public static final String TIME_FORMAT = "HH:mm:ss";

    /**
     * Constructor with overriden port and host.
     *
     * @param context The context
     */
    public SettingsWebServiceClientAdapterBase(Context context) {
        this(context, null);
    }

    /**
     * Constructor with overriden port.
     *
     * @param context The context
     * @param port The overriden port
     */
    public SettingsWebServiceClientAdapterBase(Context context,
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
    public SettingsWebServiceClientAdapterBase(Context context,
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
    public SettingsWebServiceClientAdapterBase(Context context,
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
    public SettingsWebServiceClientAdapterBase(Context context,
            String host, Integer port, String scheme, String prefix) {
        super(context, host, port, scheme, prefix);

        
    }

    /**
     * Retrieve all the Settingss in the given list. Uses the route : Settings.
     * @param settingss : The list in which the Settingss will be returned
     * @return The number of Settingss returned
     */
    public int getAll(List<Settings> settingss) {
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
                result = extractItems(json, "Settingss", settingss);
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
        return "settings";
    }

    /**
     * Retrieve one Settings. Uses the route : Settings/%id%.
     * @param settings : The Settings to retrieve (set the ID)
     * @return -1 if an error has occurred. 0 if not.
     */
    public int get(Settings settings) {
        int result = -1;
        String response = this.invokeRequest(
                    Verb.GET,
                    String.format(
                        this.getUri() + "/%s%s",
                        settings.getId(),
                        REST_FORMAT),
                    null);

        if (this.isValidResponse(response) && this.isValidRequest()) {
            try {
                JSONObject json = new JSONObject(response);
                if (extract(json, settings)) {
                    result = 0;
                }
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
            }
        }

        return result;
    }

    /**
     * Update a Settings. Uses the route : Settings/%id%.
     * @param settings : The Settings to update
     * @return -1 if an error has occurred. 0 if not.
     */
    public int update(Settings settings) {
        int result = -1;
        String response = this.invokeRequest(
                    Verb.PUT,
                    String.format(
                        this.getUri() + "/%s%s",
                        settings.getId(),
                        REST_FORMAT),
                    itemToJson(settings));

        if (this.isValidResponse(response) && this.isValidRequest()) {
            try {
                JSONObject json = new JSONObject(response);
                this.extract(json, settings);
                result = 0;
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
            }
        }

        return result;
    }

    /**
     * Delete a Settings. Uses the route : Settings/%id%.
     * @param settings : The Settings to delete (only the id is necessary)
     * @return -1 if an error has occurred. 0 if not.
     */
    public int delete(Settings settings) {
        int result = -1;
        String response = this.invokeRequest(
                    Verb.DELETE,
                    String.format(
                        this.getUri() + "/%s%s",
                        settings.getId(),
                        REST_FORMAT),
                    null);

        if (this.isValidResponse(response) && this.isValidRequest()) {
            result = 0;
        }

        return result;
    }


    /**
     * Tests if the json is a valid Settings Object.
     *
     * @param json The json
     *
     * @return True if valid
     */
    public boolean isValidJSON(JSONObject json) {
        boolean result = true;
        result = result && json.has(SettingsWebServiceClientAdapter.JSON_ID);

        return result;
    }

    /**
     * Extract a Settings from a JSONObject describing a Settings.
     * @param json The JSONObject describing the Settings
     * @param settings The returned Settings
     * @return true if a Settings was found. false if not
     */
    public boolean extract(JSONObject json, Settings settings) {
        boolean result = this.isValidJSON(json);
        if (result) {
            try {

                if (json.has(SettingsWebServiceClientAdapter.JSON_ID)
                        && !json.isNull(SettingsWebServiceClientAdapter.JSON_ID)) {
                    settings.setId(
                            json.getInt(SettingsWebServiceClientAdapter.JSON_ID));
                }

                if (json.has(SettingsWebServiceClientAdapter.JSON_IDSERVER)
                        && !json.isNull(SettingsWebServiceClientAdapter.JSON_IDSERVER)) {
                    settings.setIdServer(
                            json.getInt(SettingsWebServiceClientAdapter.JSON_IDSERVER));
                }

                if (json.has(SettingsWebServiceClientAdapter.JSON_RGPD)
                        && !json.isNull(SettingsWebServiceClientAdapter.JSON_RGPD)) {
                    settings.setRgpd(
                            json.getString(SettingsWebServiceClientAdapter.JSON_RGPD));
                }
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
            }
        }

        return result;
    }

    /**
     * Convert a Settings to a JSONObject.
     * @param settings The Settings to convert
     * @return The converted Settings
     */
    public JSONObject itemToJson(Settings settings) {
        JSONObject params = new JSONObject();
        try {
            params.put(SettingsWebServiceClientAdapter.JSON_ID,
                    settings.getId());
            params.put(SettingsWebServiceClientAdapter.JSON_IDSERVER,
                    settings.getIdServer());
            params.put(SettingsWebServiceClientAdapter.JSON_RGPD,
                    settings.getRgpd());
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
    public JSONObject itemIdToJson(Settings item) {
        JSONObject params = new JSONObject();
        try {
            params.put(SettingsWebServiceClientAdapter.JSON_ID, item.getId());
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }

        return params;
    }


    /**
     * Converts a content value reprensenting a Settings to a JSONObject.
     * @param values The content values
     * @return The JSONObject
     */
    public JSONObject contentValuesToJson(ContentValues values) {
        JSONObject params = new JSONObject();

        try {
            params.put(SettingsWebServiceClientAdapter.JSON_ID,
                    values.get(SettingsContract.COL_ID));
            params.put(SettingsWebServiceClientAdapter.JSON_IDSERVER,
                    values.get(SettingsContract.COL_IDSERVER));
            params.put(SettingsWebServiceClientAdapter.JSON_RGPD,
                    values.get(SettingsContract.COL_RGPD));
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
            List<Settings> items,
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
                Settings item = new Settings();
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
            List<Settings> items) throws JSONException {

        return this.extractItems(json, paramName, items, 0);
    }

}

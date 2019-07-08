/*
 * ResourceWebServiceClientAdapterBase.java, Edycem Android
 *
 * Copyright 2019
 * Description : 
 * Author(s)   : Harmony
 * Licence     : 
 * Last update : Jul 5, 2019
 *
 */

package com.imie.edycem.data.base;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.common.base.Strings;

import android.database.Cursor;
import android.content.ContentValues;
import android.content.Context;
import android.database.MatrixCursor;
import android.media.Image;
import android.util.Log;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;

import com.imie.edycem.data.ResourceWebServiceClientAdapter;
import com.imie.edycem.data.RestClient.Verb;
import com.imie.edycem.data.WebServiceClientAdapter;
import com.imie.edycem.entity.base.EntityResourceBase;
import com.imie.edycem.harmony.util.ImageUtils;
import com.imie.edycem.provider.contract.ResourceContract;


/**
 *
 * <b><i>This class will be overwrited whenever you regenerate the project with Harmony.
 * You should edit WebServiceClientAdapter class instead of this one or you will lose all your modifications.</i></b>
 * @param <T> Generic Type
 */
public abstract class ResourceWebServiceClientAdapterBase extends WebServiceClientAdapter<EntityResourceBase>{
    /** Class Tag. */
    private static final String TAG = "ResourceWebServiceClientAdapterBase";
    /** JSON Object Image pattern. */
    protected static String JSON_OBJECT_RESOURCE = "Resource";
    /** JSON_ID attributes. */
    protected static String JSON_ID = "id";
    /** JSON_PATH attributes. */
    protected static String JSON_PATH = "path";


    /**
     * Constructor with overriden port and host.
     *
     * @param context The context
     */
    public ResourceWebServiceClientAdapterBase(Context context) {
        this(context, null);
    }

    /**
     * Constructor with overriden port.
     *
     * @param context The context
     * @param port The overriden port
     */
    public ResourceWebServiceClientAdapterBase(Context context,
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
    public ResourceWebServiceClientAdapterBase(Context context,
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
    public ResourceWebServiceClientAdapterBase(Context context,
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
    public ResourceWebServiceClientAdapterBase(Context context,
            String host, Integer port, String scheme, String prefix) {
        super(context, host, port, scheme, prefix);
    }

    /** Resource REST Columns. */
    public static String[] REST_COLS = new String[]{
            ResourceContract.COL_ID,
            ResourceContract.COL_PATH
    };

    public String getUri() {
        return "resource";
    }

    public JSONObject itemToJson(EntityResourceBase item) {
       JSONObject params = new JSONObject();
        try {
            params.put(ResourceWebServiceClientAdapter.JSON_ID,
                    item.getId());
            params.put(ResourceWebServiceClientAdapter.JSON_PATH,
                    item.getPath());
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }

        return params;
    }

    public JSONObject contentValuesToJson(ContentValues values) {
        JSONObject params = new JSONObject();

        try {
            params.put(ResourceWebServiceClientAdapter.JSON_ID,
                    values.get(ResourceContract.COL_ID));
            params.put(ResourceWebServiceClientAdapter.JSON_PATH,
                    values.get(ResourceContract.COL_PATH));
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }

        return params;
    }

    public int extractItems(JSONObject json, String paramName,
            List<EntityResourceBase> items) throws JSONException {
        return this.extractItems(json, paramName, items, 0);
    }

    /**
     * Extract a list of <T> from a JSONObject describing an array of <T> given the array name.
     * @param json The JSONObject describing the array of <T>
     * @param items The returned list of <T>
     * @param paramName The name of the array
     * @param limit Limit the number of items to parse
     * @return The number of <T> found in the JSON
     */
    public int extractItems(JSONObject json, String paramName,
            List<EntityResourceBase> items, int limit) throws JSONException {
        JSONArray itemArray = json.optJSONArray(paramName);

        int result = -1;

        if (itemArray != null) {
            int count = itemArray.length();

            if (limit > 0 && count > limit) {
                count = limit;
            }

            for (int i = 0; i < count; i++) {
                JSONObject jsonItem = itemArray.getJSONObject(i);
                EntityResourceBase item = new EntityResourceBase();
                this.extract(jsonItem, item);
                if (item!=null) {
                    synchronized (items) {
                        items.add(item);
                    }
                }
            }
        }

        if (!json.isNull("Meta")) {
            JSONObject meta = json.optJSONObject("Meta");
            result = meta.optInt("nbt",0);
        }

        return result;
    }

    /**
     * Delete a Resource. Uses the route : resource/%id%.
     * @param resource : The Resource to delete (only the id is necessary)
     * @return -1 if an error has occurred. 0 if not.
     */
    public int delete(EntityResourceBase resource) {
        int result = -1;
        String response = this.invokeRequest(
                    Verb.DELETE,
                    String.format(
                        this.getUri() + "/%s%s",
                        resource.getId(),
                        REST_FORMAT),
                    null);

        if (this.isValidResponse(response) && this.isValidRequest()) {
            result = 0;
        }

        return result;
    }

    public int getAll(List<EntityResourceBase> resources) {
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
                result = extractItems(json, "Resources", resources);
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
                resources = null;
            }
        }

        return result;
    }

    public int get(EntityResourceBase item) {

        int result = -1;
        String response = this.invokeRequest(
                    Verb.GET,
                    String.format(
                        this.getUri() + "/%s%s",
                        item.getId(),
                        REST_FORMAT),
                    null);

        if (this.isValidResponse(response) && this.isValidRequest()) {
            try {
                JSONObject json = new JSONObject(response);
                if (extract(json, item)) {
                    result = 0;
                }
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
                item = null;
            }
        }

        return result;
    }

    public int insert(EntityResourceBase item) {
        int result = -1;

        JSONObject json = this.itemToJson(item);

        String response = this.invokeRequest(
                Verb.POST,
                String.format(
                    "%s%s",
                    this.getUri(),
                    REST_FORMAT),
                    json);

        if (this.isValidResponse(response) && this.isValidRequest()) {
            try {
                String originalPath = item.getPath();
                this.extract(new JSONObject(response), item);

                String upload = this.upload(item);

                if (!Strings.isNullOrEmpty(upload)) {
                    this.extract(new JSONObject(upload), item);
                    result = 0;
                } else {
                    item.setPath(originalPath);
                    
                }
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
            }
        }

        return result;
    }

    public int update(EntityResourceBase item) {
        int result = -1;

        if (item != null) {
            if (item.getId() == 0) {
                this.insert(item);
            } else  {
                String response = this.invokeRequest(
                        Verb.PUT,
                        String.format(
                                this.getUri() + "/%s%s",
                                item.getId(),
                                REST_FORMAT),
                                this.itemToJson(item));

                if (this.isValidResponse(response) && this.isValidRequest()) {
                    try {
                        String originalPath = item.getPath();
                        this.extract(new JSONObject(response), item);

                        String upload = this.upload(item);

                        if (!Strings.isNullOrEmpty(upload)) {
                            this.extract(new JSONObject(upload), item);
                            result = 0;

                            MemoryCacheUtils.removeFromCache(ImageUtils.getImageUri(
                                    this.context, item.getPath()),
                                    ImageLoader.getInstance().getMemoryCache());
                            DiskCacheUtils.removeFromCache(ImageUtils.getImageUri(
                                    this.context, item.getPath()),
                                    ImageLoader.getInstance().getDiskCache());
                        } else {
                            item.setPath(originalPath);
                            
                        }
                    } catch (JSONException e) {
                        Log.e(TAG, e.getMessage());
                    }
                }
            }
        }

        return result;
    }

    public String upload(EntityResourceBase item) {
        String result = null;

        this.headers.clear();
        JSONObject json = this.itemToJson(item);

        try {
            json.put(ImageUtils.IMAGE_KEY_JSON, item.getPath());
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }

        String response = this.invokeRequest(
                Verb.POST,
                String.format(
                    "%s/upload/%s%s",
                    this.getUri(),
                    item.getId(),
                    REST_FORMAT),
                    json);

        if (this.isValidResponse(response) && this.isValidRequest()) {
            result = response;
        }

        return result;
    }

    public boolean extract(JSONObject json, EntityResourceBase resource) {
        boolean result = this.isValidJSON(json);

        if (result) {
            try {
                if (json.has(ResourceWebServiceClientAdapter.JSON_PATH)
                        && !json.isNull(ResourceWebServiceClientAdapter.JSON_PATH)) {
                    resource.setPath(json.getString(ResourceWebServiceClientAdapter.JSON_PATH));
                }
                if (json.has(ResourceWebServiceClientAdapter.JSON_ID)) {
                    int id = json.optInt(ResourceWebServiceClientAdapter.JSON_ID);

                    if (id != 0) {
                        resource.setId(id);
                    }
                }

            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
            }
        }

        return result;
    }

    /**
     * Tests if the json is a valid Image Object.
     *
     * @param json The json
     *
     * @return True if valid
     */
    public boolean isValidJSON(JSONObject json) {
        boolean result = true;
        result = result && json.has(ResourceWebServiceClientAdapter.JSON_ID);

        return result;
    }

    public JSONObject itemIdToJson(EntityResourceBase resource) {
        JSONObject params = new JSONObject();

        try {
            String path = resource.getPath();

            if (Strings.isNullOrEmpty(path)) {
                path = resource.getPath();
            }

            params.put(ResourceWebServiceClientAdapter.JSON_ID, resource.getId());
            params.put(ResourceWebServiceClientAdapter.JSON_PATH, path);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }

        return params;
    }

    public boolean extractResource(JSONObject json, EntityResourceBase resource) {
        boolean result = false;

        try {
            if (json.has(ResourceWebServiceClientAdapter.JSON_PATH)
                    && !json.isNull(ResourceWebServiceClientAdapter.JSON_PATH)) {
                resource.setPath(json.getString(ResourceWebServiceClientAdapter.JSON_PATH));
                result = true;
            }

        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }

        return result;
    }

}

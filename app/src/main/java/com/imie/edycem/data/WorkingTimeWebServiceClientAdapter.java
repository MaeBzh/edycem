/*
 * WorkingTimeWebServiceClientAdapter.java, Edycem Android
 *
 * Copyright 2019
 * Description : 
 * Author(s)   : Harmony
 * Licence     : 
 * Last update : Jul 8, 2019
 *
 */
package com.imie.edycem.data;

import android.content.Context;
import android.util.Log;

import com.imie.edycem.data.base.WorkingTimeWebServiceClientAdapterBase;
import com.imie.edycem.entity.User;
import com.imie.edycem.entity.WorkingTime;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Rest class for {@link WorkingTime} WebServiceClient adapters.
 */
public class WorkingTimeWebServiceClientAdapter
        extends WorkingTimeWebServiceClientAdapterBase {

    /**
     * Constructor with overriden port and host.
     *
     * @param context The context
     */
    public WorkingTimeWebServiceClientAdapter(Context context) {
        super(context);
    }

    /**
     * Constructor with overriden port.
     *
     * @param context The context
     * @param port The overriden port
     */
    public WorkingTimeWebServiceClientAdapter(Context context,
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
    public WorkingTimeWebServiceClientAdapter(Context context,
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
    public WorkingTimeWebServiceClientAdapter(Context context,
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
    public WorkingTimeWebServiceClientAdapter(Context context,
            String host, Integer port, String scheme, String prefix) {
        super(context, host, port, scheme, prefix);
    }

    public int insertWorkingTime(User user, JSONObject jsonObject) {

        int result = -1;
        String response = this.invokeRequest(
                RestClient.Verb.POST,
                String.format(
                        "working_time?access_token=%s",
                        user.getToken()),
                jsonObject);

        if (this.isValidResponse(response) && this.isValidRequest()) {
//            try {
//                JSONObject json = new JSONObject(response);
//                this.extract(json, user);
                result = 0;
//            } catch (JSONException e) {
//                Log.e(TAG, e.getMessage());
//            }
        }

        return result;
    }
}

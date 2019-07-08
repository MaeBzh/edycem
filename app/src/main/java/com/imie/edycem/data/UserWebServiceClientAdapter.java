/*
 * UserWebServiceClientAdapter.java, Edycem Android
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

import com.imie.edycem.data.base.UserWebServiceClientAdapterBase;
import com.imie.edycem.entity.Activity;
import com.imie.edycem.entity.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Rest class for {@link User} WebServiceClient adapters.
 */
public class UserWebServiceClientAdapter
        extends UserWebServiceClientAdapterBase {

    /**
     * Constructor with overriden port and host.
     *
     * @param context The context
     */
    public UserWebServiceClientAdapter(Context context) {
        super(context);
    }

    /**
     * Constructor with overriden port.
     *
     * @param context The context
     * @param port    The overriden port
     */
    public UserWebServiceClientAdapter(Context context,
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
    public UserWebServiceClientAdapter(Context context,
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
    public UserWebServiceClientAdapter(Context context,
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
    public UserWebServiceClientAdapter(Context context,
                                       String host, Integer port, String scheme, String prefix) {
        super(context, host, port, scheme, null);
    }

    /**
     * Retrieve a user. Uses the route : user?email={email%}
     *
     * @param user : The user to retrieve
     * @return the matching user
     */
    public User getMatchingUser(User user) {

        User connectedUser = new User();
        String response = this.invokeRequest(
                RestClient.Verb.GET,
                String.format(
                        this.getUri() + "?email=%s",
                        user.getEmail()),
                null);

        if (this.isValidResponse(response) && this.isValidRequest()) {
            try {
                JSONObject json = new JSONObject(response);
                connectedUser = this.getUserFromJson(json);
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
                connectedUser = null;
            }
        }
        return connectedUser;
    }

    /**
     * If json has a 'user' object, extract it before extracting attributes.
     *
     * @param jsonObject the json object
     * @return a user
     */
    public User getUserFromJson(JSONObject jsonObject) {

        UserWebServiceClientAdapter userWebserviceAdapter = new UserWebServiceClientAdapter(this.context);
        User user = new User();
        userWebserviceAdapter.extract(jsonObject, user);
        return user;
    }

    public ArrayList<User> getAllUsers(User user) {

        ArrayList<User> users = new ArrayList<>();
        String response = this.invokeRequest(
                RestClient.Verb.GET,
                String.format(
                        this.getUri() + "?access_token=%s",
                        user.getToken()),
                null);

        if (this.isValidResponse(response) && this.isValidRequest()) {
            try {
                JSONArray json = new JSONArray(response);
                users = extractFromJsonArray(json, users);
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
                users = null;
            }
        }

        return users;
    }

    /**
     * Extract object from JSON's response.
     *
     * @param jsonArray  The response retrieve by the HTTRequest
     * @param users List of missions extracted by this function
     * @return Return 0 if the extraction was succesfull, -1 if it wasn't.
     */
    public ArrayList<User> extractFromJsonArray(JSONArray jsonArray, ArrayList<User> users) {

        try {
            int size = jsonArray.length();

            for (int i = 0; i < size; i++) {
                JSONObject jItem = jsonArray.getJSONObject(i);
                User user = new User();
                user = this.getUserFromJson(jItem, user);

                if (user != null) {
                    synchronized (users) {
                        users.add(user);
                    }
                }
            }
        } catch (JSONException e) {

            Log.e(TAG, "Extraction JSON Array failed" + e.getMessage());

        }
        return users;
    }


    /**
     * If json has a 'user' object, extract it before extracting attributes.
     *
     * @param jsonObject the json object
     * @return a user
     */
    public User getUserFromJson(JSONObject jsonObject, User user) {

        UserWebServiceClientAdapter userWebserviceAdapter = new UserWebServiceClientAdapter(this.context);
        user = new User();
        userWebserviceAdapter.extract(jsonObject, user);
        return user;
    }
}

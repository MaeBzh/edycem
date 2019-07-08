/*
 * ResourceWebServiceClientAdapter.java, Edycem Android
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

import com.imie.edycem.data.base.ResourceWebServiceClientAdapterBase;

/**
 * Class for all your ResourceWebServiceClient adapters.
 * You can add your own/customize your methods here.
 */
public class ResourceWebServiceClientAdapter
        extends ResourceWebServiceClientAdapterBase {

    /**
     * Default Constructor for resource entity.
     *
     * @param context The context
     * @param host The overriden host
     * @param port The overriden port
     * @param scheme The overriden scheme
     * @param prefix The overriden prefix
     */
    public ResourceWebServiceClientAdapter(Context context, String host,
            Integer port, String scheme, String prefix) {
        super(context, host, port, scheme, prefix);
    }
}


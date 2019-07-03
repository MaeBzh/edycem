/*
 * ProjectProviderAdapter.java, Edycem Android
 *
 * Copyright 2019
 * Description : 
 * Author(s)   : Harmony
 * Licence     : 
 * Last update : Jul 3, 2019
 *
 */
package com.imie.edycem.provider;

import com.imie.edycem.provider.base.ProjectProviderAdapterBase;

/**
 * ProjectProviderAdapter.
 *
 * A provider adapter is used to separate your provider requests for
 * each entity of your application.
 * You will find here basic methods for database manipulation.
 * Feel free to override any method here.
 */
public class ProjectProviderAdapter extends ProjectProviderAdapterBase {

    /**
     * Constructor.
     * @param provider Android application provider of Edycem
     */
    public ProjectProviderAdapter(final EdycemProvider provider) {
        super(provider);
    }
}


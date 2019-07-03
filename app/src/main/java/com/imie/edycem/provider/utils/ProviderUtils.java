/*
 * ProviderUtils.java, Edycem Android
 *
 * Copyright 2019
 * Description : 
 * Author(s)   : Harmony
 * Licence     : 
 * Last update : Jul 3, 2019
 *
 */
package com.imie.edycem.provider.utils;

import com.imie.edycem.provider.utils.base.ProviderUtilsBase;



/**
 * Generic Proxy class for the provider calls.
 *
 * Feel free to modify it and your own generic methods in it.
 *
 * @param <T>     The entity type
 */
public abstract class ProviderUtils<T> extends ProviderUtilsBase<T> {

    /**
     * Constructor.
     * @param context android.content.Context
     */
    public ProviderUtils(android.content.Context context) {
        super(context);
    }
}


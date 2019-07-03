/*
 * ProviderAdapter.java, Edycem Android
 *
 * Copyright 2019
 * Description : 
 * Author(s)   : Harmony
 * Licence     : 
 * Last update : Jul 3, 2019
 *
 */
package com.imie.edycem.provider;

import com.imie.edycem.provider.base.ProviderAdapterBase;
import com.imie.edycem.data.SQLiteAdapter;

/**
 * ProviderAdapter<T>. 
 *
 * Feel free to add your custom generic methods here.
 *
 * @param <T> must extends Serializable
 */
public abstract class ProviderAdapter<T> extends ProviderAdapterBase<T> {

    /**
     * Provider Adapter Base constructor.
     *
     * @param provider Provider of the application
     * @param adapter Sql adapter for current entity
     */
    public ProviderAdapter(
                final EdycemProvider provider,
                final SQLiteAdapter<T> adapter) {
        super(provider, adapter);
    }
}

/*
 * EntityResourceBase.java, Edycem Android
 *
 * Copyright 2019
 * Description : 
 * Author(s)   : Harmony
 * Licence     : 
 * Last update : Jul 8, 2019
 *
 */

package com.imie.edycem.entity.base;

import com.imie.edycem.entity.base.RestResource;

public class EntityResourceBase implements  RestResource {

    protected String path;

    private int id;

    private String localPath;

    @Override
    public int getId() {
         return this.id;
    }

    @Override
    public void setId(final int value) {
         this.id = value;
    }

    @Override
    public String getLocalPath() {
         return this.localPath;
    }

    @Override
    public void setLocalPath(final String value) {
         this.localPath = value;
    }

    @Override
    public String getPath() {
         return this.path;
    }

    @Override
    public void setPath(final String value) {
         this.path = value;
    }

}
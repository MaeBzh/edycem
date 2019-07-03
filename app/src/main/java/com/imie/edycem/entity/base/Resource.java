

/*
 * Resource.java, Edycem Android
 *
 * Copyright 2019
 * Description : 
 * Author(s)   : Harmony
 * Licence     : 
 * Last update : Jul 3, 2019
 *
 */
package com.imie.edycem.entity.base;

import java.io.Serializable;

import org.joda.time.DateTime;

public interface Resource {
    /**
     * @return the id
     */
    int getId();

    /**
     * @param value the id to set
     */
    void setId(final int value);

    /**
     * @return the path
     */
    String getPath();

    /**
     * @param value the path to set
     */
    void setPath(final String value);
}
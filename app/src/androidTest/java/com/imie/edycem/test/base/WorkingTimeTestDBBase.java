/*
 * WorkingTimeTestDBBase.java, Edycem Android
 *
 * Copyright 2019
 * Description : 
 * Author(s)   : Harmony
 * Licence     : 
 * Last update : Jul 3, 2019
 *
 */
package com.imie.edycem.test.base;

import java.util.ArrayList;

import android.test.suitebuilder.annotation.SmallTest;

import com.imie.edycem.data.WorkingTimeSQLiteAdapter;
import com.imie.edycem.entity.WorkingTime;


import com.imie.edycem.test.utils.*;

import junit.framework.Assert;

/** WorkingTime database test abstract class <br/>
 * <b><i>This class will be overwrited whenever you regenerate the project with Harmony.
 * You should edit WorkingTimeTestDB class instead of this one or you will lose all your modifications.</i></b>
 */
public abstract class WorkingTimeTestDBBase extends TestDBBase {
    protected android.content.Context ctx;

    protected WorkingTimeSQLiteAdapter adapter;

    protected WorkingTime entity;
    protected ArrayList<WorkingTime> entities;
    protected int nbEntities = 0;
    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();

        this.ctx = this.getContext();

        this.adapter = new WorkingTimeSQLiteAdapter(this.ctx);
        this.adapter.open();

    }

    /* (non-Javadoc)
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        this.adapter.close();

        super.tearDown();
    }

    /** Test case Create Entity */
    @SmallTest
    public void testCreate() {
        int result = -1;
        if (this.entity != null) {
            WorkingTime workingTime = WorkingTimeUtils.generateRandom(this.ctx);

            result = (int)this.adapter.insert(workingTime);

            Assert.assertTrue(result >= 0);
        }
    }

    /** Test case Read Entity */
    @SmallTest
    public void testRead() {
        WorkingTime result = null;
        if (this.entity != null) {
            result = this.adapter.getByID(this.entity.getId());

            WorkingTimeUtils.equals(this.entity, result);
        }
    }

    /** Test case Update Entity */
    @SmallTest
    public void testUpdate() {
        int result = -1;
        if (this.entity != null) {
            WorkingTime workingTime = WorkingTimeUtils.generateRandom(this.ctx);
            workingTime.setId(this.entity.getId());

            result = (int) this.adapter.update(workingTime);

            Assert.assertTrue(result >= 0);
        }
    }

    /** Test case Update Entity */
    @SmallTest
    public void testDelete() {
        int result = -1;
        if (this.entity != null) {
            result = (int) this.adapter.remove(this.entity.getId());
            Assert.assertTrue(result >= 0);
        }
    }
    
    /** Test the get all method. */
    @SmallTest
    public void testAll() {
        int result = this.adapter.getAll().size();
        int expectedSize = this.nbEntities;
        Assert.assertEquals(expectedSize, result);
    }
}

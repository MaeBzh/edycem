/*
 * ActivityTestDBBase.java, Edycem Android
 *
 * Copyright 2019
 * Description : 
 * Author(s)   : Harmony
 * Licence     : 
 * Last update : Jul 5, 2019
 *
 */
package com.imie.edycem.test.base;

import java.util.ArrayList;

import android.test.suitebuilder.annotation.SmallTest;

import com.imie.edycem.data.ActivitySQLiteAdapter;
import com.imie.edycem.entity.Activity;

import com.imie.edycem.fixture.ActivityDataLoader;

import com.imie.edycem.test.utils.*;

import junit.framework.Assert;

/** Activity database test abstract class <br/>
 * <b><i>This class will be overwrited whenever you regenerate the project with Harmony.
 * You should edit ActivityTestDB class instead of this one or you will lose all your modifications.</i></b>
 */
public abstract class ActivityTestDBBase extends TestDBBase {
    protected android.content.Context ctx;

    protected ActivitySQLiteAdapter adapter;

    protected Activity entity;
    protected ArrayList<Activity> entities;
    protected int nbEntities = 0;
    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();

        this.ctx = this.getContext();

        this.adapter = new ActivitySQLiteAdapter(this.ctx);
        this.adapter.open();

        this.entities = new ArrayList<Activity>();        
        this.entities.addAll(ActivityDataLoader.getInstance(this.ctx).getMap().values());
        if (entities.size()>0){
            this.entity = this.entities.get(TestUtils.generateRandomInt(0,entities.size()-1));
        }

        this.nbEntities += ActivityDataLoader.getInstance(this.ctx).getMap().size();
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
            Activity activity = ActivityUtils.generateRandom(this.ctx);

            result = (int)this.adapter.insert(activity);

            Assert.assertTrue(result >= 0);
        }
    }

    /** Test case Read Entity */
    @SmallTest
    public void testRead() {
        Activity result = null;
        if (this.entity != null) {
            result = this.adapter.getByID(this.entity.getId());

            ActivityUtils.equals(this.entity, result);
        }
    }

    /** Test case Update Entity */
    @SmallTest
    public void testUpdate() {
        int result = -1;
        if (this.entity != null) {
            Activity activity = ActivityUtils.generateRandom(this.ctx);
            activity.setId(this.entity.getId());

            result = (int) this.adapter.update(activity);

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

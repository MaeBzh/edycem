/*
 * JobTestDBBase.java, Edycem Android
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

import com.imie.edycem.data.JobSQLiteAdapter;
import com.imie.edycem.entity.Job;

import com.imie.edycem.fixture.JobDataLoader;

import com.imie.edycem.test.utils.*;

import junit.framework.Assert;

/** Job database test abstract class <br/>
 * <b><i>This class will be overwrited whenever you regenerate the project with Harmony.
 * You should edit JobTestDB class instead of this one or you will lose all your modifications.</i></b>
 */
public abstract class JobTestDBBase extends TestDBBase {
    protected android.content.Context ctx;

    protected JobSQLiteAdapter adapter;

    protected Job entity;
    protected ArrayList<Job> entities;
    protected int nbEntities = 0;
    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();

        this.ctx = this.getContext();

        this.adapter = new JobSQLiteAdapter(this.ctx);
        this.adapter.open();

        this.entities = new ArrayList<Job>();        
        this.entities.addAll(JobDataLoader.getInstance(this.ctx).getMap().values());
        if (entities.size()>0){
            this.entity = this.entities.get(TestUtils.generateRandomInt(0,entities.size()-1));
        }

        this.nbEntities += JobDataLoader.getInstance(this.ctx).getMap().size();
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
            Job job = JobUtils.generateRandom(this.ctx);

            result = (int)this.adapter.insert(job);

            Assert.assertTrue(result >= 0);
        }
    }

    /** Test case Read Entity */
    @SmallTest
    public void testRead() {
        Job result = null;
        if (this.entity != null) {
            result = this.adapter.getByID(this.entity.getId());

            JobUtils.equals(this.entity, result);
        }
    }

    /** Test case Update Entity */
    @SmallTest
    public void testUpdate() {
        int result = -1;
        if (this.entity != null) {
            Job job = JobUtils.generateRandom(this.ctx);
            job.setId(this.entity.getId());

            result = (int) this.adapter.update(job);

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

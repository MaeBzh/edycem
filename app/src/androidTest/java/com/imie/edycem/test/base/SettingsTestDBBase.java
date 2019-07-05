/*
 * SettingsTestDBBase.java, Edycem Android
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

import com.imie.edycem.data.SettingsSQLiteAdapter;
import com.imie.edycem.entity.Settings;

import com.imie.edycem.fixture.SettingsDataLoader;

import com.imie.edycem.test.utils.*;

import junit.framework.Assert;

/** Settings database test abstract class <br/>
 * <b><i>This class will be overwrited whenever you regenerate the project with Harmony.
 * You should edit SettingsTestDB class instead of this one or you will lose all your modifications.</i></b>
 */
public abstract class SettingsTestDBBase extends TestDBBase {
    protected android.content.Context ctx;

    protected SettingsSQLiteAdapter adapter;

    protected Settings entity;
    protected ArrayList<Settings> entities;
    protected int nbEntities = 0;
    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();

        this.ctx = this.getContext();

        this.adapter = new SettingsSQLiteAdapter(this.ctx);
        this.adapter.open();

        this.entities = new ArrayList<Settings>();        
        this.entities.addAll(SettingsDataLoader.getInstance(this.ctx).getMap().values());
        if (entities.size()>0){
            this.entity = this.entities.get(TestUtils.generateRandomInt(0,entities.size()-1));
        }

        this.nbEntities += SettingsDataLoader.getInstance(this.ctx).getMap().size();
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
            Settings settings = SettingsUtils.generateRandom(this.ctx);

            result = (int)this.adapter.insert(settings);

            Assert.assertTrue(result >= 0);
        }
    }

    /** Test case Read Entity */
    @SmallTest
    public void testRead() {
        Settings result = null;
        if (this.entity != null) {
            result = this.adapter.getByID(this.entity.getId());

            SettingsUtils.equals(this.entity, result);
        }
    }

    /** Test case Update Entity */
    @SmallTest
    public void testUpdate() {
        int result = -1;
        if (this.entity != null) {
            Settings settings = SettingsUtils.generateRandom(this.ctx);
            settings.setId(this.entity.getId());

            result = (int) this.adapter.update(settings);

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

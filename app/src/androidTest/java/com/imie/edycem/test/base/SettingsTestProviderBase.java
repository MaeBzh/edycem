/*
 * SettingsTestProviderBase.java, Edycem Android
 *
 * Copyright 2019
 * Description : 
 * Author(s)   : Harmony
 * Licence     : 
 * Last update : Jul 5, 2019
 *
 */
package com.imie.edycem.test.base;

import android.test.suitebuilder.annotation.SmallTest;

import com.imie.edycem.provider.SettingsProviderAdapter;
import com.imie.edycem.provider.utils.SettingsProviderUtils;
import com.imie.edycem.provider.contract.SettingsContract;

import com.imie.edycem.data.SettingsSQLiteAdapter;

import com.imie.edycem.entity.Settings;

import com.imie.edycem.fixture.SettingsDataLoader;

import java.util.ArrayList;
import com.imie.edycem.test.utils.*;


import android.content.ContentResolver;
import android.content.ContentValues;


import android.net.Uri;

import junit.framework.Assert;

/** Settings database test abstract class <br/>
 * <b><i>This class will be overwrited whenever you regenerate the project with Harmony.
 * You should edit SettingsTestDB class instead of this one or you will lose all your modifications.</i></b>
 */
public abstract class SettingsTestProviderBase extends TestDBBase {
    protected android.content.Context ctx;

    protected SettingsSQLiteAdapter adapter;

    protected Settings entity;
    protected ContentResolver provider;
    protected SettingsProviderUtils providerUtils;

    protected ArrayList<Settings> entities;

    protected int nbEntities = 0;
    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();

        this.ctx = this.getContext();

        this.adapter = new SettingsSQLiteAdapter(this.ctx);

        this.entities = new ArrayList<Settings>();
        this.entities.addAll(SettingsDataLoader.getInstance(this.ctx).getMap().values());
        if (this.entities.size()>0) {
            this.entity = this.entities.get(TestUtils.generateRandomInt(0,entities.size()-1));
        }

        this.nbEntities += SettingsDataLoader.getInstance(this.ctx).getMap().size();
        this.provider = this.getContext().getContentResolver();
        this.providerUtils = new SettingsProviderUtils(this.getContext());
    }

    /* (non-Javadoc)
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /********** Direct Provider calls. *******/

    /** Test case Create Entity */
    @SmallTest
    public void testCreate() {
        Uri result = null;
        if (this.entity != null) {
            Settings settings = SettingsUtils.generateRandom(this.ctx);

            try {
                ContentValues values = SettingsContract.itemToContentValues(settings);
                values.remove(SettingsContract.COL_ID);
                result = this.provider.insert(SettingsProviderAdapter.SETTINGS_URI, values);

            } catch (Exception e) {
                e.printStackTrace();
            }

            Assert.assertNotNull(result);
            Assert.assertTrue(Integer.parseInt(result.getPathSegments().get(1)) > 0);

        }
    }

    /** Test case Read Entity */
    @SmallTest
    public void testRead() {
        Settings result = null;

        if (this.entity != null) {
            try {
                android.database.Cursor c = this.provider.query(Uri.parse(
                        SettingsProviderAdapter.SETTINGS_URI
                                + "/"
                                + this.entity.getId()),
                        this.adapter.getCols(),
                        null,
                        null,
                        null);
                c.moveToFirst();
                result = SettingsContract.cursorToItem(c);
                c.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            SettingsUtils.equals(this.entity, result);
        }
    }

    /** Test case ReadAll Entity */
    @SmallTest
    public void testReadAll() {
        ArrayList<Settings> result = null;
        try {
            android.database.Cursor c = this.provider.query(SettingsProviderAdapter.SETTINGS_URI, this.adapter.getCols(), null, null, null);
            result = SettingsContract.cursorToItems(c);
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Assert.assertNotNull(result);
        if (result != null) {
            Assert.assertEquals(result.size(), this.nbEntities);
        }
    }

    /** Test case Update Entity */
    @SmallTest
    public void testUpdate() {
        int result = -1;
        if (this.entity != null) {
            Settings settings = SettingsUtils.generateRandom(this.ctx);

            try {
                settings.setId(this.entity.getId());

                ContentValues values = SettingsContract.itemToContentValues(settings);
                result = this.provider.update(
                    Uri.parse(SettingsProviderAdapter.SETTINGS_URI
                        + "/"
                        + settings.getId()),
                    values,
                    null,
                    null);

            } catch (Exception e) {
                e.printStackTrace();
            }

            Assert.assertTrue(result > 0);
        }
    }

    /** Test case UpdateAll Entity */
    @SmallTest
    public void testUpdateAll() {
        int result = -1;
        if (this.entities != null) {
            if (this.entities.size() > 0) {
                Settings settings = SettingsUtils.generateRandom(this.ctx);

                try {
                    ContentValues values = SettingsContract.itemToContentValues(settings);
                    values.remove(SettingsContract.COL_ID);

                    result = this.provider.update(SettingsProviderAdapter.SETTINGS_URI, values, null, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Assert.assertEquals(result, this.nbEntities);
            }
        }
    }

    /** Test case Delete Entity */
    @SmallTest
    public void testDelete() {
        int result = -1;
        if (this.entity != null) {
            try {
                result = this.provider.delete(
                        Uri.parse(SettingsProviderAdapter.SETTINGS_URI
                            + "/"
                            + this.entity.getId()),
                        null,
                        null);

            } catch (Exception e) {
                e.printStackTrace();
            }
            Assert.assertTrue(result >= 0);
        }

    }

    /** Test case DeleteAll Entity */
    @SmallTest
    public void testDeleteAll() {
        int result = -1;
        if (this.entities != null) {
            if (this.entities.size() > 0) {

                try {
                    result = this.provider.delete(SettingsProviderAdapter.SETTINGS_URI, null, null);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                Assert.assertEquals(result, this.nbEntities);
            }
        }
    }

    /****** Provider Utils calls ********/

    /** Test case Read Entity by provider utils. */
    @SmallTest
    public void testUtilsRead() {
        Settings result = null;

        if (this.entity != null) {
            result = this.providerUtils.query(this.entity);

            SettingsUtils.equals(this.entity, result);
        }
    }

    /** Test case ReadAll Entity by provider utils. */
    @SmallTest
    public void testUtilsReadAll() {
        ArrayList<Settings> result = null;
        result = this.providerUtils.queryAll();

        Assert.assertNotNull(result);
        if (result != null) {
            Assert.assertEquals(result.size(), this.nbEntities);
        }
    }

    /** Test case Update Entity by provider utils. */
    @SmallTest
    public void testUtilsUpdate() {
        int result = -1;
        if (this.entity != null) {
            Settings settings = SettingsUtils.generateRandom(this.ctx);

            settings.setId(this.entity.getId());
            result = this.providerUtils.update(settings);

            Assert.assertTrue(result > 0);
        }
    }


    /** Test case Delete Entity by provider utils. */
    @SmallTest
    public void testUtilsDelete() {
        int result = -1;
        if (this.entity != null) {
            result = this.providerUtils.delete(this.entity);
            Assert.assertTrue(result >= 0);
        }

    }
}

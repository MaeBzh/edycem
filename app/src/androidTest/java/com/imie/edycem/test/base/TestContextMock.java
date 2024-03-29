/*
 * TestContextMock.java, Edycem Android
 *
 * Copyright 2019
 * Description : 
 * Author(s)   : Harmony
 * Licence     : 
 * Last update : Jul 5, 2019
 *
 */
package com.imie.edycem.test.base;

import java.io.File;

import com.imie.edycem.provider.EdycemProvider;
import com.imie.edycem.EdycemApplication;
import com.imie.edycem.fixture.DataLoader;
import com.imie.edycem.harmony.util.DatabaseUtil;
import com.imie.edycem.data.EdycemSQLiteOpenHelper;
import com.imie.edycem.data.base.SQLiteAdapterBase;

import android.content.BroadcastReceiver;
import android.content.ContentProvider;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.test.IsolatedContext;
import android.test.RenamingDelegatingContext;
import android.test.mock.MockContentResolver;


/** android.content.Context mock for tests.<br/>
 * <b><i>This class will be overwrited whenever
 * you regenerate the project with Harmony.</i></b>
 */
public class TestContextMock {
    private final static String CONTEXT_PREFIX = "test.";
    private final static String PROVIDER_AUTHORITY =
                    "com.imie.edycem.provider";
    private final static Class<? extends ContentProvider> PROVIDER_CLASS =
                    EdycemProvider.class;

    private static android.content.Context context = null;
    private AndroidTestCase androidTestCase;
    private android.content.Context baseContext;

    public TestContextMock(AndroidTestCase androidTestCase) {
        this.androidTestCase = androidTestCase;
    }

    /**
     * Get the original context
     * @return unmocked android.content.Context
     */
    protected android.content.Context getBaseContext() {
        return this.baseContext;
    }

    /**
     * Get the mock for ContentResolver
     * @return MockContentResolver
     */
    protected MockContentResolver getMockContentResolver() {
        return new MockContentResolver();
    }

    /**
     * Get the mock for android.content.Context
     * @return MockContext
     */
    protected android.content.Context getMockContext() {
            return this.androidTestCase.getContext();
    }

    /**
     * Initialize the mock android.content.Context
     * @throws Exception
     */
    protected void setMockContext() throws Exception {
        if (this.baseContext == null) {
            this.baseContext = this.androidTestCase.getContext();
        }

        if (context == null) {
            ContentProvider provider = PROVIDER_CLASS.newInstance();
            MockContentResolver resolver = this.getMockContentResolver();

            RenamingDelegatingContext targetContextWrapper
                = new RenamingDelegatingContext(
                    // The context that most methods are delegated to:
                    this.getMockContext(),
                    // The context that file methods are delegated to:
                    this.baseContext,
                    // Prefix database
                    CONTEXT_PREFIX);

            context = new TestContextIsolatedBase(
                    resolver,
                    targetContextWrapper);

            PackageManager packageManager = this.baseContext.getPackageManager();
            ProviderInfo providerInfo = packageManager.resolveContentProvider(
                    EdycemProvider.class.getPackage().getName(), 0);

            provider.attachInfo(context, providerInfo);

            resolver.addProvider(PROVIDER_AUTHORITY, provider);

            // Call INITIALIZE_DATABASE to create adapters and database (if needed)
            provider.call("INITIALIZE_DATABASE", null, null);
        }

        this.androidTestCase.setContext(context);
    }

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        EdycemSQLiteOpenHelper.isJUnit = true;
        this.setMockContext();

        String dbPath =
                this.androidTestCase.getContext()
                        .getDatabasePath(SQLiteAdapterBase.DB_NAME)
                        .getAbsolutePath() + ".test";

        File cacheDbFile = new File(dbPath);

        if (!cacheDbFile.exists() || !DataLoader.hasFixturesBeenLoaded) {
            if (EdycemApplication.DEBUG) {
                android.util.Log.d("TEST", "Create new Database cache");
            }

            // Create initial database
            EdycemSQLiteOpenHelper helper =
                    new EdycemSQLiteOpenHelper(
                        this.getMockContext(),
                        SQLiteAdapterBase.DB_NAME,
                        null,
                        EdycemApplication.getVersionCode(
                                this.getMockContext()));

            SQLiteDatabase db = helper.getWritableDatabase();
            EdycemSQLiteOpenHelper.clearDatabase(db);

            db.beginTransaction();
            DataLoader dataLoader = new DataLoader(this.getMockContext());
            dataLoader.clean();
            dataLoader.loadData(db,
                        DataLoader.MODE_APP |
                        DataLoader.MODE_DEBUG |
                        DataLoader.MODE_TEST);
            db.setTransactionSuccessful();
            db.endTransaction();
            db.close();

            DatabaseUtil.exportDB(this.getMockContext(),
                    cacheDbFile,
                    SQLiteAdapterBase.DB_NAME);
        } else {
            if (EdycemApplication.DEBUG) {
                android.util.Log.d("TEST", "Re use old Database cache");
            }
            DatabaseUtil.importDB(this.getMockContext(),
                    cacheDbFile,
                    SQLiteAdapterBase.DB_NAME,
                    false);
        }
    }
}

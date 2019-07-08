/*
 * TestWSBase.java, Edycem Android
 *
 * Copyright 2019
 * Description : 
 * Author(s)   : Harmony
 * Licence     : 
 * Last update : Jul 5, 2019
 *
 */
package com.imie.edycem.test.base;


import com.google.mockwebserver.MockWebServer;
import android.content.Context;

/**
 * Web Service Test Base.
 */
public abstract class TestWSBase extends TestDBBase {
    /** Android {@link Context}. */
    protected Context ctx;
    /** {@link MockWebServer}. */
    protected MockWebServer server;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.ctx = this.getContext();

        this.server = new MockWebServer();
        this.server.play();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        this.server.shutdown();
    }
}

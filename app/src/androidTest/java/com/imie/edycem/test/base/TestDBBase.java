/*
 * TestDBBase.java, Edycem Android
 *
 * Copyright 2019
 * Description : 
 * Author(s)   : Harmony
 * Licence     : 
 * Last update : Jul 3, 2019
 *
 */
package com.imie.edycem.test.base;


import android.test.AndroidTestCase;


/** Base test abstract class for DB tests.<br/>
 * <b><i>This class will be overwrited whenever
 * you regenerate the project with Harmony.</i></b>
 */
public abstract class TestDBBase extends AndroidTestCase {

    /** The Mocked android {@link android.content.Context}. */
    protected TestContextMock testContextMock;

    protected TestDBBase() {
        this.testContextMock = new TestContextMock(this);
    }

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        this.testContextMock.setUp();
    }
}

/*
 * ProjectTestWSBase.java, Edycem Android
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

import android.database.Cursor;

import com.imie.edycem.data.ProjectWebServiceClientAdapter;
import com.imie.edycem.data.RestClient.RequestConstants;
import com.imie.edycem.entity.Project;
import com.imie.edycem.fixture.ProjectDataLoader;
import com.imie.edycem.test.utils.ProjectUtils;
import com.imie.edycem.test.utils.TestUtils;

import com.google.mockwebserver.MockResponse;

import junit.framework.Assert;

/** Project Web Service Test.
 * 
 * @see android.app.Fragment
 */
public abstract class ProjectTestWSBase extends TestWSBase {
    /** model {@link Project}. */
    protected Project model;
    /** web {@link ProjectWebServiceClientAdapter}. */
    protected ProjectWebServiceClientAdapter web;
    /** entities ArrayList<Project>. */
    protected ArrayList<Project> entities;
    /** nbEntities Number of entities. */
    protected int nbEntities = 0;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        String host = this.server.getHostName();
        int port = this.server.getPort();

        this.web = new ProjectWebServiceClientAdapter(
                this.ctx, host, port, RequestConstants.HTTP);
        
        this.entities = new ArrayList<Project>();        
        this.entities.addAll(ProjectDataLoader.getInstance(this.ctx).getMap().values());
        
        if (entities.size() > 0) {
            this.model = this.entities.get(TestUtils.generateRandomInt(0,entities.size()-1));
        }

        this.nbEntities += ProjectDataLoader.getInstance(this.ctx).getMap().size();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    /** Test case Create Entity */
    public void testInsert() {
        this.server.enqueue(new MockResponse().setBody("{'result'='0'}"));

        int result = this.web.insert(this.model);

        Assert.assertTrue(result >= 0);
    }
    
    /** Test case Get Entity. */
    public void testGet() {
        this.server.enqueue(new MockResponse().setBody(
                this.web.itemToJson(this.model).toString()));

        int result = this.web.get(this.model);

        Assert.assertTrue(result >= 0);
    }

    /** Test case get all Entity. */
    public void testGetAll() {
        this.server.enqueue(new MockResponse().setBody("{Projects :"
            + this.web.itemsToJson(this.entities).toString() + "}"));

        ArrayList<Project> projectList = 
                new ArrayList<Project>();

        int result = this.web.getAll(projectList);

        Assert.assertEquals(projectList.size(), this.entities.size());
    }
    
    /** Test case Update Entity. */
    public void testUpdate() {
        this.server.enqueue(new MockResponse().setBody("{'result'='1'}"));

        int result = this.web.update(this.model);

        Assert.assertTrue(result >= 0);
        
        this.server.enqueue(new MockResponse().setBody(
                this.web.itemToJson(this.model).toString()));

        Project item = new Project();
        item.setId(this.model.getId());

        result = this.web.get(item);
        
        ProjectUtils.equals(this.model, item);
    }
    
    /** Test case Delete Entity. */
    public void testDelete() {
        this.server.enqueue(new MockResponse().setBody("{'result'='1'}"));

        int result = this.web.delete(this.model);

        Assert.assertTrue(result == 0);

        this.server.enqueue(new MockResponse().setBody("{}"));

        result = this.web.get(this.model);

        Assert.assertTrue(result < 0);
    }
}

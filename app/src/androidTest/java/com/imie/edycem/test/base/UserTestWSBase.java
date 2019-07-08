/*
 * UserTestWSBase.java, Edycem Android
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

import com.imie.edycem.data.UserWebServiceClientAdapter;
import com.imie.edycem.data.RestClient.RequestConstants;
import com.imie.edycem.entity.User;
import com.imie.edycem.fixture.UserDataLoader;
import com.imie.edycem.test.utils.UserUtils;
import com.imie.edycem.test.utils.TestUtils;

import com.google.mockwebserver.MockResponse;

import junit.framework.Assert;

/** User Web Service Test.
 * 
 * @see android.app.Fragment
 */
public abstract class UserTestWSBase extends TestWSBase {
    /** model {@link User}. */
    protected User model;
    /** web {@link UserWebServiceClientAdapter}. */
    protected UserWebServiceClientAdapter web;
    /** entities ArrayList<User>. */
    protected ArrayList<User> entities;
    /** nbEntities Number of entities. */
    protected int nbEntities = 0;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        String host = this.server.getHostName();
        int port = this.server.getPort();

        this.web = new UserWebServiceClientAdapter(
                this.ctx, host, port, RequestConstants.HTTP);
        
        this.entities = new ArrayList<User>();        
        this.entities.addAll(UserDataLoader.getInstance(this.ctx).getMap().values());
        
        if (entities.size() > 0) {
            this.model = this.entities.get(TestUtils.generateRandomInt(0,entities.size()-1));
        }

        this.nbEntities += UserDataLoader.getInstance(this.ctx).getMap().size();
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
        this.server.enqueue(new MockResponse().setBody("{Users :"
            + this.web.itemsToJson(this.entities).toString() + "}"));

        ArrayList<User> userList = 
                new ArrayList<User>();

        int result = this.web.getAll(userList);

        Assert.assertEquals(userList.size(), this.entities.size());
    }
    
    /** Test case Update Entity. */
    public void testUpdate() {
        this.server.enqueue(new MockResponse().setBody("{'result'='1'}"));

        int result = this.web.update(this.model);

        Assert.assertTrue(result >= 0);
        
        this.server.enqueue(new MockResponse().setBody(
                this.web.itemToJson(this.model).toString()));

        User item = new User();
        item.setId(this.model.getId());

        result = this.web.get(item);
        
        UserUtils.equals(this.model, item);
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

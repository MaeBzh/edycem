/*
 * UserUtilsBase.java, Edycem Android
 *
 * Copyright 2019
 * Description : 
 * Author(s)   : Harmony
 * Licence     : 
 * Last update : Jul 5, 2019
 *
 */
package com.imie.edycem.test.utils.base;


import junit.framework.Assert;
import com.imie.edycem.entity.User;



import com.imie.edycem.test.utils.TestUtils;
import com.imie.edycem.entity.Job;
import com.imie.edycem.fixture.JobDataLoader;

import com.imie.edycem.entity.WorkingTime;
import com.imie.edycem.fixture.WorkingTimeDataLoader;

import com.imie.edycem.entity.Project;
import com.imie.edycem.fixture.ProjectDataLoader;


import java.util.ArrayList;

/** User utils test class base. */
public abstract class UserUtilsBase {

    // If you have enums, you may have to override this method to generate the random enums values
    /**
     * Generate a random entity
     *
     * @return The randomly generated entity
     */
    public static User generateRandom(android.content.Context ctx){
        User user = new User();

        user.setId(TestUtils.generateRandomInt(0,100) + 1);
        user.setIdServer(TestUtils.generateRandomInt(0,100));
        user.setFirstname("firstname_"+TestUtils.generateRandomString(10));
        user.setLastname("lastname_"+TestUtils.generateRandomString(10));
        user.setEmail("email_"+TestUtils.generateRandomString(10));
        user.setIsEligible(TestUtils.generateRandomBool());
        user.setIdSmartphone("idSmartphone_"+TestUtils.generateRandomString(10));
        user.setDateRgpd(TestUtils.generateRandomDateTime());
        ArrayList<Job> jobs =
            new ArrayList<Job>();
        jobs.addAll(JobDataLoader.getInstance(ctx).getMap().values());
        if (!jobs.isEmpty()) {
            user.setJob(jobs.get(TestUtils.generateRandomInt(0, jobs.size())));
        }
        ArrayList<WorkingTime> userWorkingTimess =
            new ArrayList<WorkingTime>();
        userWorkingTimess.addAll(WorkingTimeDataLoader.getInstance(ctx).getMap().values());
        ArrayList<WorkingTime> relatedUserWorkingTimess = new ArrayList<WorkingTime>();
        if (!userWorkingTimess.isEmpty()) {
            relatedUserWorkingTimess.add(userWorkingTimess.get(TestUtils.generateRandomInt(0, userWorkingTimess.size())));
            user.setUserWorkingTimes(relatedUserWorkingTimess);
        }
        ArrayList<Project> createdProjectss =
            new ArrayList<Project>();
        createdProjectss.addAll(ProjectDataLoader.getInstance(ctx).getMap().values());
        ArrayList<Project> relatedCreatedProjectss = new ArrayList<Project>();
        if (!createdProjectss.isEmpty()) {
            relatedCreatedProjectss.add(createdProjectss.get(TestUtils.generateRandomInt(0, createdProjectss.size())));
            user.setCreatedProjects(relatedCreatedProjectss);
        }

        return user;
    }

    public static boolean equals(User user1,
            User user2){
        return equals(user1, user2, true);
    }
    
    public static boolean equals(User user1,
            User user2,
            boolean checkRecursiveId){
        boolean ret = true;
        Assert.assertNotNull(user1);
        Assert.assertNotNull(user2);
        if (user1!=null && user2 !=null){
            Assert.assertEquals(user1.getId(), user2.getId());
            Assert.assertEquals(user1.getIdServer(), user2.getIdServer());
            Assert.assertEquals(user1.getFirstname(), user2.getFirstname());
            Assert.assertEquals(user1.getLastname(), user2.getLastname());
            Assert.assertEquals(user1.getEmail(), user2.getEmail());
            Assert.assertEquals(user1.isIsEligible(), user2.isIsEligible());
            Assert.assertEquals(user1.getIdSmartphone(), user2.getIdSmartphone());
            Assert.assertTrue(user1.getDateRgpd().isEqual(user2.getDateRgpd()));
            if (user1.getJob() != null
                    && user2.getJob() != null) {
                if (checkRecursiveId) {
                    Assert.assertEquals(user1.getJob().getId(),
                            user2.getJob().getId());
                }
            }
            if (user1.getUserWorkingTimes() != null
                    && user2.getUserWorkingTimes() != null) {
                Assert.assertEquals(user1.getUserWorkingTimes().size(),
                    user2.getUserWorkingTimes().size());
                if (checkRecursiveId) {
                    for (WorkingTime userWorkingTimes1 : user1.getUserWorkingTimes()) {
                        boolean found = false;
                        for (WorkingTime userWorkingTimes2 : user2.getUserWorkingTimes()) {
                            if (userWorkingTimes1.getId() == userWorkingTimes2.getId()) {
                                found = true;
                            }
                        }
                        Assert.assertTrue(
                                String.format(
                                        "Couldn't find associated userWorkingTimes (id = %s) in User (id = %s)",
                                        userWorkingTimes1.getId(),
                                        user1.getId()),
                                found);
                    }
                }
            }
            if (user1.getCreatedProjects() != null
                    && user2.getCreatedProjects() != null) {
                Assert.assertEquals(user1.getCreatedProjects().size(),
                    user2.getCreatedProjects().size());
                if (checkRecursiveId) {
                    for (Project createdProjects1 : user1.getCreatedProjects()) {
                        boolean found = false;
                        for (Project createdProjects2 : user2.getCreatedProjects()) {
                            if (createdProjects1.getId() == createdProjects2.getId()) {
                                found = true;
                            }
                        }
                        Assert.assertTrue(
                                String.format(
                                        "Couldn't find associated createdProjects (id = %s) in User (id = %s)",
                                        createdProjects1.getId(),
                                        user1.getId()),
                                found);
                    }
                }
            }
        }

        return ret;
    }
}


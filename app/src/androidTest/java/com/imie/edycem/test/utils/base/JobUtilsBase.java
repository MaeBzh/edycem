/*
 * JobUtilsBase.java, Edycem Android
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
import com.imie.edycem.entity.Job;



import com.imie.edycem.test.utils.TestUtils;
import com.imie.edycem.entity.User;
import com.imie.edycem.fixture.UserDataLoader;

import com.imie.edycem.entity.Project;
import com.imie.edycem.fixture.ProjectDataLoader;


import java.util.ArrayList;

/** Job utils test class base. */
public abstract class JobUtilsBase {

    // If you have enums, you may have to override this method to generate the random enums values
    /**
     * Generate a random entity
     *
     * @return The randomly generated entity
     */
    public static Job generateRandom(android.content.Context ctx){
        Job job = new Job();

        job.setId(TestUtils.generateRandomInt(0,100) + 1);
        job.setName("name_"+TestUtils.generateRandomString(10));
        ArrayList<User> userss =
            new ArrayList<User>();
        userss.addAll(UserDataLoader.getInstance(ctx).getMap().values());
        ArrayList<User> relatedUserss = new ArrayList<User>();
        if (!userss.isEmpty()) {
            relatedUserss.add(userss.get(TestUtils.generateRandomInt(0, userss.size())));
            job.setUsers(relatedUserss);
        }
        ArrayList<Project> projectss =
            new ArrayList<Project>();
        projectss.addAll(ProjectDataLoader.getInstance(ctx).getMap().values());
        ArrayList<Project> relatedProjectss = new ArrayList<Project>();
        if (!projectss.isEmpty()) {
            relatedProjectss.add(projectss.get(TestUtils.generateRandomInt(0, projectss.size())));
            job.setProjects(relatedProjectss);
        }

        return job;
    }

    public static boolean equals(Job job1,
            Job job2){
        return equals(job1, job2, true);
    }
    
    public static boolean equals(Job job1,
            Job job2,
            boolean checkRecursiveId){
        boolean ret = true;
        Assert.assertNotNull(job1);
        Assert.assertNotNull(job2);
        if (job1!=null && job2 !=null){
            Assert.assertEquals(job1.getId(), job2.getId());
            Assert.assertEquals(job1.getName(), job2.getName());
            if (job1.getUsers() != null
                    && job2.getUsers() != null) {
                Assert.assertEquals(job1.getUsers().size(),
                    job2.getUsers().size());
                if (checkRecursiveId) {
                    for (User users1 : job1.getUsers()) {
                        boolean found = false;
                        for (User users2 : job2.getUsers()) {
                            if (users1.getId() == users2.getId()) {
                                found = true;
                            }
                        }
                        Assert.assertTrue(
                                String.format(
                                        "Couldn't find associated users (id = %s) in Job (id = %s)",
                                        users1.getId(),
                                        job1.getId()),
                                found);
                    }
                }
            }
            if (job1.getProjects() != null
                    && job2.getProjects() != null) {
                Assert.assertEquals(job1.getProjects().size(),
                    job2.getProjects().size());
                if (checkRecursiveId) {
                    for (Project projects1 : job1.getProjects()) {
                        boolean found = false;
                        for (Project projects2 : job2.getProjects()) {
                            if (projects1.getId() == projects2.getId()) {
                                found = true;
                            }
                        }
                        Assert.assertTrue(
                                String.format(
                                        "Couldn't find associated projects (id = %s) in Job (id = %s)",
                                        projects1.getId(),
                                        job1.getId()),
                                found);
                    }
                }
            }
        }

        return ret;
    }
}


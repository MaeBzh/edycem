/*
 * JobUtilsBase.java, Edycem Android
 *
 * Copyright 2019
 * Description : 
 * Author(s)   : Harmony
 * Licence     : 
 * Last update : Jul 3, 2019
 *
 */
package com.imie.edycem.test.utils.base;


import junit.framework.Assert;
import com.imie.edycem.entity.Job;



import com.imie.edycem.test.utils.TestUtils;
import com.imie.edycem.entity.User;
import com.imie.edycem.test.utils.UserUtils;

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
        ArrayList<User> relatedUserss = new ArrayList<User>();
        relatedUserss.add(UserUtils.generateRandom(ctx));
        job.setUsers(relatedUserss);

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
        }

        return ret;
    }
}


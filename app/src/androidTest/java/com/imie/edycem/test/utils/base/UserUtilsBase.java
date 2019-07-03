/*
 * UserUtilsBase.java, Edycem Android
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
import com.imie.edycem.entity.User;



import com.imie.edycem.test.utils.TestUtils;

import com.imie.edycem.test.utils.JobUtils;
import com.imie.edycem.entity.WorkingTime;
import com.imie.edycem.test.utils.WorkingTimeUtils;

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
        user.setFirstname("firstname_"+TestUtils.generateRandomString(10));
        user.setLastname("lastname_"+TestUtils.generateRandomString(10));
        user.setEmail("email_"+TestUtils.generateRandomString(10));
        user.setIsEligible(TestUtils.generateRandomBool());
        user.setIdSmartphone("idSmartphone_"+TestUtils.generateRandomString(10));
        user.setDateRgpd(TestUtils.generateRandomDateTime());
        user.setJob(JobUtils.generateRandom(ctx));
        ArrayList<WorkingTime> relatedUserWorkingTimess = new ArrayList<WorkingTime>();
        relatedUserWorkingTimess.add(WorkingTimeUtils.generateRandom(ctx));
        user.setUserWorkingTimes(relatedUserWorkingTimess);

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
        }

        return ret;
    }
}


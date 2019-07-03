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
        user.setIdSmartphone("idSmartphone_"+TestUtils.generateRandomString(10));
        user.setPassword("password_"+TestUtils.generateRandomString(10));
        user.setDateRgpd(TestUtils.generateRandomDateTime());

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
            Assert.assertEquals(user1.getIdSmartphone(), user2.getIdSmartphone());
            Assert.assertEquals(user1.getPassword(), user2.getPassword());
            Assert.assertTrue(user1.getDateRgpd().isEqual(user2.getDateRgpd()));
        }

        return ret;
    }
}


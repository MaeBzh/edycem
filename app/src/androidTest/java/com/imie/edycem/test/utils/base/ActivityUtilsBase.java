/*
 * ActivityUtilsBase.java, Edycem Android
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
import com.imie.edycem.entity.Activity;



import com.imie.edycem.test.utils.TestUtils;


/** Activity utils test class base. */
public abstract class ActivityUtilsBase {

    // If you have enums, you may have to override this method to generate the random enums values
    /**
     * Generate a random entity
     *
     * @return The randomly generated entity
     */
    public static Activity generateRandom(android.content.Context ctx){
        Activity activity = new Activity();

        activity.setId(TestUtils.generateRandomInt(0,100) + 1);
        activity.setName("name_"+TestUtils.generateRandomString(10));

        return activity;
    }

    public static boolean equals(Activity activity1,
            Activity activity2){
        return equals(activity1, activity2, true);
    }
    
    public static boolean equals(Activity activity1,
            Activity activity2,
            boolean checkRecursiveId){
        boolean ret = true;
        Assert.assertNotNull(activity1);
        Assert.assertNotNull(activity2);
        if (activity1!=null && activity2 !=null){
            Assert.assertEquals(activity1.getId(), activity2.getId());
            Assert.assertEquals(activity1.getName(), activity2.getName());
        }

        return ret;
    }
}


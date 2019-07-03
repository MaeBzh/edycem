/*
 * WorkingTimeUtilsBase.java, Edycem Android
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
import com.imie.edycem.entity.WorkingTime;



import com.imie.edycem.test.utils.TestUtils;


/** WorkingTime utils test class base. */
public abstract class WorkingTimeUtilsBase {

    // If you have enums, you may have to override this method to generate the random enums values
    /**
     * Generate a random entity
     *
     * @return The randomly generated entity
     */
    public static WorkingTime generateRandom(android.content.Context ctx){
        WorkingTime workingTime = new WorkingTime();

        workingTime.setId(TestUtils.generateRandomInt(0,100) + 1);
        workingTime.setDate(TestUtils.generateRandomDate());
        workingTime.setSpentTime("spentTime_"+TestUtils.generateRandomString(10));
        workingTime.setDescription("description_"+TestUtils.generateRandomString(10));

        return workingTime;
    }

    public static boolean equals(WorkingTime workingTime1,
            WorkingTime workingTime2){
        return equals(workingTime1, workingTime2, true);
    }
    
    public static boolean equals(WorkingTime workingTime1,
            WorkingTime workingTime2,
            boolean checkRecursiveId){
        boolean ret = true;
        Assert.assertNotNull(workingTime1);
        Assert.assertNotNull(workingTime2);
        if (workingTime1!=null && workingTime2 !=null){
            Assert.assertEquals(workingTime1.getId(), workingTime2.getId());
            Assert.assertTrue(workingTime1.getDate().isEqual(workingTime2.getDate()));
            Assert.assertEquals(workingTime1.getSpentTime(), workingTime2.getSpentTime());
            Assert.assertEquals(workingTime1.getDescription(), workingTime2.getDescription());
        }

        return ret;
    }
}


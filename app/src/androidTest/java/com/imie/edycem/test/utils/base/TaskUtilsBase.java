/*
 * TaskUtilsBase.java, Edycem Android
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
import com.imie.edycem.entity.Task;



import com.imie.edycem.test.utils.TestUtils;


/** Task utils test class base. */
public abstract class TaskUtilsBase {

    // If you have enums, you may have to override this method to generate the random enums values
    /**
     * Generate a random entity
     *
     * @return The randomly generated entity
     */
    public static Task generateRandom(android.content.Context ctx){
        Task task = new Task();

        task.setId(TestUtils.generateRandomInt(0,100) + 1);
        task.setName("name_"+TestUtils.generateRandomString(10));

        return task;
    }

    public static boolean equals(Task task1,
            Task task2){
        return equals(task1, task2, true);
    }
    
    public static boolean equals(Task task1,
            Task task2,
            boolean checkRecursiveId){
        boolean ret = true;
        Assert.assertNotNull(task1);
        Assert.assertNotNull(task2);
        if (task1!=null && task2 !=null){
            Assert.assertEquals(task1.getId(), task2.getId());
            Assert.assertEquals(task1.getName(), task2.getName());
        }

        return ret;
    }
}


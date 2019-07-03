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

import com.imie.edycem.test.utils.ActivityUtils;
import com.imie.edycem.entity.WorkingTime;
import com.imie.edycem.test.utils.WorkingTimeUtils;

import java.util.ArrayList;

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
        task.setActivity(ActivityUtils.generateRandom(ctx));
        ArrayList<WorkingTime> relatedTaskWorkingTimess = new ArrayList<WorkingTime>();
        relatedTaskWorkingTimess.add(WorkingTimeUtils.generateRandom(ctx));
        task.setTaskWorkingTimes(relatedTaskWorkingTimess);

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
            if (task1.getActivity() != null
                    && task2.getActivity() != null) {
                if (checkRecursiveId) {
                    Assert.assertEquals(task1.getActivity().getId(),
                            task2.getActivity().getId());
                }
            }
            if (task1.getTaskWorkingTimes() != null
                    && task2.getTaskWorkingTimes() != null) {
                Assert.assertEquals(task1.getTaskWorkingTimes().size(),
                    task2.getTaskWorkingTimes().size());
                if (checkRecursiveId) {
                    for (WorkingTime taskWorkingTimes1 : task1.getTaskWorkingTimes()) {
                        boolean found = false;
                        for (WorkingTime taskWorkingTimes2 : task2.getTaskWorkingTimes()) {
                            if (taskWorkingTimes1.getId() == taskWorkingTimes2.getId()) {
                                found = true;
                            }
                        }
                        Assert.assertTrue(
                                String.format(
                                        "Couldn't find associated taskWorkingTimes (id = %s) in Task (id = %s)",
                                        taskWorkingTimes1.getId(),
                                        task1.getId()),
                                found);
                    }
                }
            }
        }

        return ret;
    }
}


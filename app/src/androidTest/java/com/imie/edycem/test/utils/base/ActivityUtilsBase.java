/*
 * ActivityUtilsBase.java, Edycem Android
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
import com.imie.edycem.entity.Activity;



import com.imie.edycem.test.utils.TestUtils;
import com.imie.edycem.entity.Task;
import com.imie.edycem.fixture.TaskDataLoader;


import java.util.ArrayList;

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
        ArrayList<Task> taskss =
            new ArrayList<Task>();
        taskss.addAll(TaskDataLoader.getInstance(ctx).getMap().values());
        ArrayList<Task> relatedTaskss = new ArrayList<Task>();
        if (!taskss.isEmpty()) {
            relatedTaskss.add(taskss.get(TestUtils.generateRandomInt(0, taskss.size())));
            activity.setTasks(relatedTaskss);
        }

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
            if (activity1.getTasks() != null
                    && activity2.getTasks() != null) {
                Assert.assertEquals(activity1.getTasks().size(),
                    activity2.getTasks().size());
                if (checkRecursiveId) {
                    for (Task tasks1 : activity1.getTasks()) {
                        boolean found = false;
                        for (Task tasks2 : activity2.getTasks()) {
                            if (tasks1.getId() == tasks2.getId()) {
                                found = true;
                            }
                        }
                        Assert.assertTrue(
                                String.format(
                                        "Couldn't find associated tasks (id = %s) in Activity (id = %s)",
                                        tasks1.getId(),
                                        activity1.getId()),
                                found);
                    }
                }
            }
        }

        return ret;
    }
}


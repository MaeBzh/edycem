/*
 * ProjectUtilsBase.java, Edycem Android
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
import com.imie.edycem.entity.Project;



import com.imie.edycem.test.utils.TestUtils;
import com.imie.edycem.entity.WorkingTime;
import com.imie.edycem.test.utils.WorkingTimeUtils;

import com.imie.edycem.test.utils.JobUtils;

import java.util.ArrayList;

/** Project utils test class base. */
public abstract class ProjectUtilsBase {

    // If you have enums, you may have to override this method to generate the random enums values
    /**
     * Generate a random entity
     *
     * @return The randomly generated entity
     */
    public static Project generateRandom(android.content.Context ctx){
        Project project = new Project();

        project.setId(TestUtils.generateRandomInt(0,100) + 1);
        project.setName("name_"+TestUtils.generateRandomString(10));
        project.setDescription("description_"+TestUtils.generateRandomString(10));
        project.setCompany("company_"+TestUtils.generateRandomString(10));
        project.setClaimantName("claimantName_"+TestUtils.generateRandomString(10));
        project.setRelevantSite("relevantSite_"+TestUtils.generateRandomString(10));
        project.setEligibleCir(TestUtils.generateRandomInt(0,100));
        project.setAsPartOfPulpit(TestUtils.generateRandomBool());
        project.setDeadline(TestUtils.generateRandomDateTime());
        project.setDocuments("documents_"+TestUtils.generateRandomString(10));
        project.setActivityType("activityType_"+TestUtils.generateRandomString(10));
        ArrayList<WorkingTime> relatedProjectWorkingTimess = new ArrayList<WorkingTime>();
        relatedProjectWorkingTimess.add(WorkingTimeUtils.generateRandom(ctx));
        project.setProjectWorkingTimes(relatedProjectWorkingTimess);
        project.setJob(JobUtils.generateRandom(ctx));

        return project;
    }

    public static boolean equals(Project project1,
            Project project2){
        return equals(project1, project2, true);
    }
    
    public static boolean equals(Project project1,
            Project project2,
            boolean checkRecursiveId){
        boolean ret = true;
        Assert.assertNotNull(project1);
        Assert.assertNotNull(project2);
        if (project1!=null && project2 !=null){
            Assert.assertEquals(project1.getId(), project2.getId());
            Assert.assertEquals(project1.getName(), project2.getName());
            Assert.assertEquals(project1.getDescription(), project2.getDescription());
            Assert.assertEquals(project1.getCompany(), project2.getCompany());
            Assert.assertEquals(project1.getClaimantName(), project2.getClaimantName());
            Assert.assertEquals(project1.getRelevantSite(), project2.getRelevantSite());
            Assert.assertEquals(project1.getEligibleCir(), project2.getEligibleCir());
            Assert.assertEquals(project1.isAsPartOfPulpit(), project2.isAsPartOfPulpit());
            Assert.assertTrue(project1.getDeadline().isEqual(project2.getDeadline()));
            Assert.assertEquals(project1.getDocuments(), project2.getDocuments());
            Assert.assertEquals(project1.getActivityType(), project2.getActivityType());
            if (project1.getProjectWorkingTimes() != null
                    && project2.getProjectWorkingTimes() != null) {
                Assert.assertEquals(project1.getProjectWorkingTimes().size(),
                    project2.getProjectWorkingTimes().size());
                if (checkRecursiveId) {
                    for (WorkingTime projectWorkingTimes1 : project1.getProjectWorkingTimes()) {
                        boolean found = false;
                        for (WorkingTime projectWorkingTimes2 : project2.getProjectWorkingTimes()) {
                            if (projectWorkingTimes1.getId() == projectWorkingTimes2.getId()) {
                                found = true;
                            }
                        }
                        Assert.assertTrue(
                                String.format(
                                        "Couldn't find associated projectWorkingTimes (id = %s) in Project (id = %s)",
                                        projectWorkingTimes1.getId(),
                                        project1.getId()),
                                found);
                    }
                }
            }
            if (project1.getJob() != null
                    && project2.getJob() != null) {
                if (checkRecursiveId) {
                    Assert.assertEquals(project1.getJob().getId(),
                            project2.getJob().getId());
                }
            }
        }

        return ret;
    }
}


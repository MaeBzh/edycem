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
        project.setIsEligibleCir(TestUtils.generateRandomBool());
        project.setAsPartOfPulpit(TestUtils.generateRandomBool());
        project.setDeadline(TestUtils.generateRandomDateTime());
        project.setDocuments("documents_"+TestUtils.generateRandomString(10));
        project.setActivityType("activityType_"+TestUtils.generateRandomString(10));

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
            Assert.assertEquals(project1.isIsEligibleCir(), project2.isIsEligibleCir());
            Assert.assertEquals(project1.isAsPartOfPulpit(), project2.isAsPartOfPulpit());
            Assert.assertTrue(project1.getDeadline().isEqual(project2.getDeadline()));
            Assert.assertEquals(project1.getDocuments(), project2.getDocuments());
            Assert.assertEquals(project1.getActivityType(), project2.getActivityType());
        }

        return ret;
    }
}


/*
 * ProjectContractBase.java, Edycem Android
 *
 * Copyright 2019
 * Description : 
 * Author(s)   : Harmony
 * Licence     : 
 * Last update : Jul 3, 2019
 *
 */
package com.imie.edycem.provider.contract.base;

import android.content.ContentValues;


import java.util.ArrayList;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import com.imie.edycem.entity.Project;
import com.imie.edycem.entity.WorkingTime;
import com.imie.edycem.entity.Job;



import com.imie.edycem.provider.contract.ProjectContract;
import com.imie.edycem.harmony.util.DateUtils;

/** Edycem contract base.
 *
 * This class is regenerated. DO NOT MODIFY.
 */
public abstract class ProjectContractBase {


        /** id. */
    public static final String COL_ID =
            "id";
    /** Alias. */
    public static final String ALIASED_COL_ID =
            ProjectContract.TABLE_NAME + "." + COL_ID;

    /** name. */
    public static final String COL_NAME =
            "name";
    /** Alias. */
    public static final String ALIASED_COL_NAME =
            ProjectContract.TABLE_NAME + "." + COL_NAME;

    /** description. */
    public static final String COL_DESCRIPTION =
            "description";
    /** Alias. */
    public static final String ALIASED_COL_DESCRIPTION =
            ProjectContract.TABLE_NAME + "." + COL_DESCRIPTION;

    /** company. */
    public static final String COL_COMPANY =
            "company";
    /** Alias. */
    public static final String ALIASED_COL_COMPANY =
            ProjectContract.TABLE_NAME + "." + COL_COMPANY;

    /** claimantName. */
    public static final String COL_CLAIMANTNAME =
            "claimantName";
    /** Alias. */
    public static final String ALIASED_COL_CLAIMANTNAME =
            ProjectContract.TABLE_NAME + "." + COL_CLAIMANTNAME;

    /** relevantSite. */
    public static final String COL_RELEVANTSITE =
            "relevantSite";
    /** Alias. */
    public static final String ALIASED_COL_RELEVANTSITE =
            ProjectContract.TABLE_NAME + "." + COL_RELEVANTSITE;

    /** eligibleCir. */
    public static final String COL_ELIGIBLECIR =
            "eligibleCir";
    /** Alias. */
    public static final String ALIASED_COL_ELIGIBLECIR =
            ProjectContract.TABLE_NAME + "." + COL_ELIGIBLECIR;

    /** asPartOfPulpit. */
    public static final String COL_ASPARTOFPULPIT =
            "asPartOfPulpit";
    /** Alias. */
    public static final String ALIASED_COL_ASPARTOFPULPIT =
            ProjectContract.TABLE_NAME + "." + COL_ASPARTOFPULPIT;

    /** deadline. */
    public static final String COL_DEADLINE =
            "deadline";
    /** Alias. */
    public static final String ALIASED_COL_DEADLINE =
            ProjectContract.TABLE_NAME + "." + COL_DEADLINE;

    /** documents. */
    public static final String COL_DOCUMENTS =
            "documents";
    /** Alias. */
    public static final String ALIASED_COL_DOCUMENTS =
            ProjectContract.TABLE_NAME + "." + COL_DOCUMENTS;

    /** activityType. */
    public static final String COL_ACTIVITYTYPE =
            "activityType";
    /** Alias. */
    public static final String ALIASED_COL_ACTIVITYTYPE =
            ProjectContract.TABLE_NAME + "." + COL_ACTIVITYTYPE;

    /** job_id. */
    public static final String COL_JOB_ID =
            "job_id";
    /** Alias. */
    public static final String ALIASED_COL_JOB_ID =
            ProjectContract.TABLE_NAME + "." + COL_JOB_ID;




    /** Constant for parcelisation/serialization. */
    public static final String PARCEL = "Project";
    /** Table name of SQLite database. */
    public static final String TABLE_NAME = "Project";
    /** Global Fields. */
    public static final String[] COLS = new String[] {

        
        ProjectContract.COL_ID,
        
        ProjectContract.COL_NAME,
        
        ProjectContract.COL_DESCRIPTION,
        
        ProjectContract.COL_COMPANY,
        
        ProjectContract.COL_CLAIMANTNAME,
        
        ProjectContract.COL_RELEVANTSITE,
        
        ProjectContract.COL_ELIGIBLECIR,
        
        ProjectContract.COL_ASPARTOFPULPIT,
        
        ProjectContract.COL_DEADLINE,
        
        ProjectContract.COL_DOCUMENTS,
        
        ProjectContract.COL_ACTIVITYTYPE,
        
        ProjectContract.COL_JOB_ID
    };

    /** Global Fields. */
    public static final String[] ALIASED_COLS = new String[] {
        
        ProjectContract.ALIASED_COL_ID,
        
        ProjectContract.ALIASED_COL_NAME,
        
        ProjectContract.ALIASED_COL_DESCRIPTION,
        
        ProjectContract.ALIASED_COL_COMPANY,
        
        ProjectContract.ALIASED_COL_CLAIMANTNAME,
        
        ProjectContract.ALIASED_COL_RELEVANTSITE,
        
        ProjectContract.ALIASED_COL_ELIGIBLECIR,
        
        ProjectContract.ALIASED_COL_ASPARTOFPULPIT,
        
        ProjectContract.ALIASED_COL_DEADLINE,
        
        ProjectContract.ALIASED_COL_DOCUMENTS,
        
        ProjectContract.ALIASED_COL_ACTIVITYTYPE,
        
        
        ProjectContract.ALIASED_COL_JOB_ID
    };


    /**
     * Converts a Project into a content values.
     *
     * @param item The Project to convert
     *
     * @return The content values
     */
    public static ContentValues itemToContentValues(final Project item) {
        final ContentValues result = new ContentValues();

             result.put(ProjectContract.COL_ID,
                String.valueOf(item.getId()));

             if (item.getName() != null) {
                result.put(ProjectContract.COL_NAME,
                    item.getName());
            }

             if (item.getDescription() != null) {
                result.put(ProjectContract.COL_DESCRIPTION,
                    item.getDescription());
            }

             if (item.getCompany() != null) {
                result.put(ProjectContract.COL_COMPANY,
                    item.getCompany());
            }

             if (item.getClaimantName() != null) {
                result.put(ProjectContract.COL_CLAIMANTNAME,
                    item.getClaimantName());
            }

             if (item.getRelevantSite() != null) {
                result.put(ProjectContract.COL_RELEVANTSITE,
                    item.getRelevantSite());
            } else {
                result.put(ProjectContract.COL_RELEVANTSITE, (String) null);
            }

             result.put(ProjectContract.COL_ELIGIBLECIR,
                String.valueOf(item.getEligibleCir()));

             result.put(ProjectContract.COL_ASPARTOFPULPIT,
                item.isAsPartOfPulpit() ? 1 : 0);

             if (item.getDeadline() != null) {
                result.put(ProjectContract.COL_DEADLINE,
                    item.getDeadline().toString(ISODateTimeFormat.dateTime()));
            } else {
                result.put(ProjectContract.COL_DEADLINE, (String) null);
            }

             if (item.getDocuments() != null) {
                result.put(ProjectContract.COL_DOCUMENTS,
                    item.getDocuments());
            } else {
                result.put(ProjectContract.COL_DOCUMENTS, (String) null);
            }

             if (item.getActivityType() != null) {
                result.put(ProjectContract.COL_ACTIVITYTYPE,
                    item.getActivityType());
            } else {
                result.put(ProjectContract.COL_ACTIVITYTYPE, (String) null);
            }

              if (item.getJob() != null) {
                result.put(ProjectContract.COL_JOB_ID,
                    item.getJob().getId());
            }


        return result;
    }

    /**
     * Converts a Cursor into a Project.
     *
     * @param cursor The cursor to convert
     *
     * @return The extracted Project
     */
    public static Project cursorToItem(final android.database.Cursor cursor) {
        Project result = new Project();
        ProjectContract.cursorToItem(cursor, result);
        return result;
    }

    /**
     * Convert Cursor of database to Project entity.
     * @param cursor Cursor object
     * @param result Project entity
     */
    public static void cursorToItem(final android.database.Cursor cursor, final Project result) {
        if (cursor.getCount() != 0) {
            int index;

            index = cursor.getColumnIndex(ProjectContract.COL_ID);

            if (index > -1) {
                result.setId(cursor.getInt(index));
            }
            index = cursor.getColumnIndex(ProjectContract.COL_NAME);

            if (index > -1) {
                result.setName(cursor.getString(index));
            }
            index = cursor.getColumnIndex(ProjectContract.COL_DESCRIPTION);

            if (index > -1) {
                result.setDescription(cursor.getString(index));
            }
            index = cursor.getColumnIndex(ProjectContract.COL_COMPANY);

            if (index > -1) {
                result.setCompany(cursor.getString(index));
            }
            index = cursor.getColumnIndex(ProjectContract.COL_CLAIMANTNAME);

            if (index > -1) {
                result.setClaimantName(cursor.getString(index));
            }
            index = cursor.getColumnIndex(ProjectContract.COL_RELEVANTSITE);

            if (index > -1) {
            if (!cursor.isNull(index)) {
                    result.setRelevantSite(cursor.getString(index));
            }
            }
            index = cursor.getColumnIndex(ProjectContract.COL_ELIGIBLECIR);

            if (index > -1) {
            if (!cursor.isNull(index)) {
                    result.setEligibleCir(cursor.getInt(index));
            }
            }
            index = cursor.getColumnIndex(ProjectContract.COL_ASPARTOFPULPIT);

            if (index > -1) {
            if (!cursor.isNull(index)) {
                    result.setAsPartOfPulpit(cursor.getInt(index) == 1);
            }
            }
            index = cursor.getColumnIndex(ProjectContract.COL_DEADLINE);

            if (index > -1) {
            if (!cursor.isNull(index)) {
                    final DateTime dtDeadline =
                        DateUtils.formatISOStringToDateTime(cursor.getString(index));
                    if (dtDeadline != null) {
                            result.setDeadline(dtDeadline);
                    } else {
                        result.setDeadline(new DateTime());
                    }
            }
            }
            index = cursor.getColumnIndex(ProjectContract.COL_DOCUMENTS);

            if (index > -1) {
            if (!cursor.isNull(index)) {
                    result.setDocuments(cursor.getString(index));
            }
            }
            index = cursor.getColumnIndex(ProjectContract.COL_ACTIVITYTYPE);

            if (index > -1) {
            if (!cursor.isNull(index)) {
                    result.setActivityType(cursor.getString(index));
            }
            }
            if (result.getJob() == null) {
                final Job job = new Job();
                index = cursor.getColumnIndex(ProjectContract.COL_JOB_ID);

                if (index > -1) {
                    job.setId(cursor.getInt(index));
                    result.setJob(job);
                }

            }

        }
    }

    /**
     * Convert Cursor of database to Array of Project entity.
     * @param cursor Cursor object
     * @return Array of Project entity
     */
    public static ArrayList<Project> cursorToItems(final android.database.Cursor cursor) {
        final ArrayList<Project> result = new ArrayList<Project>(cursor.getCount());

        if (cursor.getCount() != 0) {
            cursor.moveToFirst();

            Project item;
            do {
                item = ProjectContract.cursorToItem(cursor);
                result.add(item);
            } while (cursor.moveToNext());
        }

        return result;
    }
}

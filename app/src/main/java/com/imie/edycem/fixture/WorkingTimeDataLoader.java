/*
 * WorkingTimeDataLoader.java, Edycem Android
 *
 * Copyright 2019
 * Description : 
 * Author(s)   : Harmony
 * Licence     : 
 * Last update : Jul 5, 2019
 *
 */
package com.imie.edycem.fixture;

import java.util.Map;
import java.util.ArrayList;



import com.imie.edycem.entity.WorkingTime;


/**
 * WorkingTimeDataLoader.
 *
 * This dataloader implements the parsing method needed while reading
 * the fixtures files.
 */
public final class WorkingTimeDataLoader
                        extends FixtureBase<WorkingTime> {
    /** WorkingTimeDataLoader name. */
    private static final String FILE_NAME = "WorkingTime";

    /** Constant field for id. */
    private static final String ID = "id";
    /** Constant field for date. */
    private static final String DATE = "date";
    /** Constant field for spentTime. */
    private static final String SPENTTIME = "spentTime";
    /** Constant field for description. */
    private static final String DESCRIPTION = "description";
    /** Constant field for user. */
    private static final String USER = "user";
    /** Constant field for project. */
    private static final String PROJECT = "project";
    /** Constant field for task. */
    private static final String TASK = "task";


    /** WorkingTimeDataLoader instance (Singleton). */
    private static WorkingTimeDataLoader instance;

    /**
     * Get the WorkingTimeDataLoader singleton.
     * @param ctx The context
     * @return The dataloader instance
     */
    public static WorkingTimeDataLoader getInstance(
                                            final android.content.Context ctx) {
        if (instance == null) {
            instance = new WorkingTimeDataLoader(ctx);
        }
        return instance;
    }

    /**
     * Constructor.
     * @param ctx The context
     */
    private WorkingTimeDataLoader(final android.content.Context ctx) {
        super(ctx);
    }


    @Override
    protected WorkingTime extractItem(final Map<?, ?> columns) {
        final WorkingTime workingTime =
                new WorkingTime();

        return this.extractItem(columns, workingTime);
    }
    /**
     * Extract an entity from a fixture element (YML).
     * @param columns Columns to extract
     * @param workingTime Entity to extract
     * @return A WorkingTime entity
     */
    protected WorkingTime extractItem(final Map<?, ?> columns,
                WorkingTime workingTime) {
        workingTime.setId(this.parseIntField(columns, ID));
        workingTime.setDate(this.parseDateTimeField(columns, DATE));
        workingTime.setSpentTime(this.parseIntField(columns, SPENTTIME));
        workingTime.setDescription(this.parseField(columns, DESCRIPTION, String.class));
        workingTime.setUser(this.parseSimpleRelationField(columns, USER, UserDataLoader.getInstance(this.ctx)));
        if (workingTime.getUser() != null) {
            if (workingTime.getUser().getUserWorkingTimes() == null) {
                workingTime.getUser().setUserWorkingTimes(
                        new ArrayList<WorkingTime>());
            }

            workingTime.getUser().getUserWorkingTimes().add(workingTime);
        }
        workingTime.setProject(this.parseSimpleRelationField(columns, PROJECT, ProjectDataLoader.getInstance(this.ctx)));
        if (workingTime.getProject() != null) {
            if (workingTime.getProject().getProjectWorkingTimes() == null) {
                workingTime.getProject().setProjectWorkingTimes(
                        new ArrayList<WorkingTime>());
            }

            workingTime.getProject().getProjectWorkingTimes().add(workingTime);
        }
        workingTime.setTask(this.parseSimpleRelationField(columns, TASK, TaskDataLoader.getInstance(this.ctx)));
        if (workingTime.getTask() != null) {
            if (workingTime.getTask().getTaskWorkingTimes() == null) {
                workingTime.getTask().setTaskWorkingTimes(
                        new ArrayList<WorkingTime>());
            }

            workingTime.getTask().getTaskWorkingTimes().add(workingTime);
        }

        return workingTime;
    }
    /**
     * Loads WorkingTimes into the DataManager.
     * @param dataManager The DataManager
     */
    @Override
    public void load(final DataManager dataManager) {
        for (final WorkingTime workingTime : this.items.values()) {
            int id = dataManager.persist(workingTime);
            workingTime.setId(id);

        }
        dataManager.flush();
    }

    /**
     * Give priority for fixtures insertion in database.
     * 0 is the first.
     * @return The order
     */
    @Override
    public int getOrder() {
        return 0;
    }

    /**
     * Get the fixture file name.
     * @return A String representing the file name
     */
    @Override
    public String getFixtureFileName() {
        return FILE_NAME;
    }

    @Override
    protected WorkingTime get(final String key) {
        final WorkingTime result;
        
        if (this.items.containsKey(key)) {
            result = this.items.get(key);
        } else {
            result = null;
        }
        
        return result;
    }
}

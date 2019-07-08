/*
 * TaskDataLoader.java, Edycem Android
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



import com.imie.edycem.entity.Task;
import com.imie.edycem.entity.WorkingTime;


/**
 * TaskDataLoader.
 *
 * This dataloader implements the parsing method needed while reading
 * the fixtures files.
 */
public final class TaskDataLoader
                        extends FixtureBase<Task> {
    /** TaskDataLoader name. */
    private static final String FILE_NAME = "Task";

    /** Constant field for id. */
    private static final String ID = "id";
    /** Constant field for name. */
    private static final String NAME = "name";
    /** Constant field for activity. */
    private static final String ACTIVITY = "activity";
    /** Constant field for taskWorkingTimes. */
    private static final String TASKWORKINGTIMES = "taskWorkingTimes";


    /** TaskDataLoader instance (Singleton). */
    private static TaskDataLoader instance;

    /**
     * Get the TaskDataLoader singleton.
     * @param ctx The context
     * @return The dataloader instance
     */
    public static TaskDataLoader getInstance(
                                            final android.content.Context ctx) {
        if (instance == null) {
            instance = new TaskDataLoader(ctx);
        }
        return instance;
    }

    /**
     * Constructor.
     * @param ctx The context
     */
    private TaskDataLoader(final android.content.Context ctx) {
        super(ctx);
    }


    @Override
    protected Task extractItem(final Map<?, ?> columns) {
        final Task task =
                new Task();

        return this.extractItem(columns, task);
    }
    /**
     * Extract an entity from a fixture element (YML).
     * @param columns Columns to extract
     * @param task Entity to extract
     * @return A Task entity
     */
    protected Task extractItem(final Map<?, ?> columns,
                Task task) {
        task.setId(this.parseIntField(columns, ID));
        task.setName(this.parseField(columns, NAME, String.class));
        task.setActivity(this.parseSimpleRelationField(columns, ACTIVITY, ActivityDataLoader.getInstance(this.ctx)));
        if (task.getActivity() != null) {
            if (task.getActivity().getTasks() == null) {
                task.getActivity().setTasks(
                        new ArrayList<Task>());
            }

            task.getActivity().getTasks().add(task);
        }
        task.setTaskWorkingTimes(this.parseMultiRelationField(columns, TASKWORKINGTIMES, WorkingTimeDataLoader.getInstance(this.ctx)));
        if (task.getTaskWorkingTimes() != null) {
            for (WorkingTime related : task.getTaskWorkingTimes()) {
                related.setTask(task);
            }
        }

        return task;
    }
    /**
     * Loads Tasks into the DataManager.
     * @param dataManager The DataManager
     */
    @Override
    public void load(final DataManager dataManager) {
        for (final Task task : this.items.values()) {
            int id = dataManager.persist(task);
            task.setId(id);

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
    protected Task get(final String key) {
        final Task result;
        
        if (this.items.containsKey(key)) {
            result = this.items.get(key);
        } else {
            result = null;
        }
        
        return result;
    }
}

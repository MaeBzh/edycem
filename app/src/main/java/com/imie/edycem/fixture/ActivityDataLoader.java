/*
 * ActivityDataLoader.java, Edycem Android
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




import com.imie.edycem.entity.Activity;
import com.imie.edycem.entity.Task;


/**
 * ActivityDataLoader.
 *
 * This dataloader implements the parsing method needed while reading
 * the fixtures files.
 */
public final class ActivityDataLoader
                        extends FixtureBase<Activity> {
    /** ActivityDataLoader name. */
    private static final String FILE_NAME = "Activity";

    /** Constant field for id. */
    private static final String ID = "id";
    /** Constant field for name. */
    private static final String NAME = "name";
    /** Constant field for tasks. */
    private static final String TASKS = "tasks";


    /** ActivityDataLoader instance (Singleton). */
    private static ActivityDataLoader instance;

    /**
     * Get the ActivityDataLoader singleton.
     * @param ctx The context
     * @return The dataloader instance
     */
    public static ActivityDataLoader getInstance(
                                            final android.content.Context ctx) {
        if (instance == null) {
            instance = new ActivityDataLoader(ctx);
        }
        return instance;
    }

    /**
     * Constructor.
     * @param ctx The context
     */
    private ActivityDataLoader(final android.content.Context ctx) {
        super(ctx);
    }


    @Override
    protected Activity extractItem(final Map<?, ?> columns) {
        final Activity activity =
                new Activity();

        return this.extractItem(columns, activity);
    }
    /**
     * Extract an entity from a fixture element (YML).
     * @param columns Columns to extract
     * @param activity Entity to extract
     * @return A Activity entity
     */
    protected Activity extractItem(final Map<?, ?> columns,
                Activity activity) {
        activity.setId(this.parseIntField(columns, ID));
        activity.setName(this.parseField(columns, NAME, String.class));
        activity.setTasks(this.parseMultiRelationField(columns, TASKS, TaskDataLoader.getInstance(this.ctx)));
        if (activity.getTasks() != null) {
            for (Task related : activity.getTasks()) {
                related.setActivity(activity);
            }
        }

        return activity;
    }
    /**
     * Loads Activitys into the DataManager.
     * @param dataManager The DataManager
     */
    @Override
    public void load(final DataManager dataManager) {
        for (final Activity activity : this.items.values()) {
            int id = dataManager.persist(activity);
            activity.setId(id);

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
    protected Activity get(final String key) {
        final Activity result;
        
        if (this.items.containsKey(key)) {
            result = this.items.get(key);
        } else {
            result = null;
        }
        
        return result;
    }
}

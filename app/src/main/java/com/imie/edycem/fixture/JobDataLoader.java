/*
 * JobDataLoader.java, Edycem Android
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




import com.imie.edycem.entity.Job;
import com.imie.edycem.entity.User;
import com.imie.edycem.entity.Project;


/**
 * JobDataLoader.
 *
 * This dataloader implements the parsing method needed while reading
 * the fixtures files.
 */
public final class JobDataLoader
                        extends FixtureBase<Job> {
    /** JobDataLoader name. */
    private static final String FILE_NAME = "Job";

    /** Constant field for id. */
    private static final String ID = "id";
    /** Constant field for name. */
    private static final String NAME = "name";
    /** Constant field for users. */
    private static final String USERS = "users";
    /** Constant field for projects. */
    private static final String PROJECTS = "projects";


    /** JobDataLoader instance (Singleton). */
    private static JobDataLoader instance;

    /**
     * Get the JobDataLoader singleton.
     * @param ctx The context
     * @return The dataloader instance
     */
    public static JobDataLoader getInstance(
                                            final android.content.Context ctx) {
        if (instance == null) {
            instance = new JobDataLoader(ctx);
        }
        return instance;
    }

    /**
     * Constructor.
     * @param ctx The context
     */
    private JobDataLoader(final android.content.Context ctx) {
        super(ctx);
    }


    @Override
    protected Job extractItem(final Map<?, ?> columns) {
        final Job job =
                new Job();

        return this.extractItem(columns, job);
    }
    /**
     * Extract an entity from a fixture element (YML).
     * @param columns Columns to extract
     * @param job Entity to extract
     * @return A Job entity
     */
    protected Job extractItem(final Map<?, ?> columns,
                Job job) {
        job.setId(this.parseIntField(columns, ID));
        job.setName(this.parseField(columns, NAME, String.class));
        job.setUsers(this.parseMultiRelationField(columns, USERS, UserDataLoader.getInstance(this.ctx)));
        if (job.getUsers() != null) {
            for (User related : job.getUsers()) {
                related.setJob(job);
            }
        }
        job.setProjects(this.parseMultiRelationField(columns, PROJECTS, ProjectDataLoader.getInstance(this.ctx)));
        if (job.getProjects() != null) {
            for (Project related : job.getProjects()) {
                related.setJob(job);
            }
        }

        return job;
    }
    /**
     * Loads Jobs into the DataManager.
     * @param dataManager The DataManager
     */
    @Override
    public void load(final DataManager dataManager) {
        for (final Job job : this.items.values()) {
            int id = dataManager.persist(job);
            job.setId(id);

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
    protected Job get(final String key) {
        final Job result;
        
        if (this.items.containsKey(key)) {
            result = this.items.get(key);
        } else {
            result = null;
        }
        
        return result;
    }
}

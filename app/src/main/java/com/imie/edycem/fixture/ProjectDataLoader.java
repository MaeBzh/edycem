/*
 * ProjectDataLoader.java, Edycem Android
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



import com.imie.edycem.entity.Project;
import com.imie.edycem.entity.WorkingTime;


/**
 * ProjectDataLoader.
 *
 * This dataloader implements the parsing method needed while reading
 * the fixtures files.
 */
public final class ProjectDataLoader
                        extends FixtureBase<Project> {
    /** ProjectDataLoader name. */
    private static final String FILE_NAME = "Project";

    /** Constant field for id. */
    private static final String ID = "id";
    /** Constant field for name. */
    private static final String NAME = "name";
    /** Constant field for description. */
    private static final String DESCRIPTION = "description";
    /** Constant field for createdAt. */
    private static final String CREATEDAT = "createdAt";
    /** Constant field for company. */
    private static final String COMPANY = "company";
    /** Constant field for claimantName. */
    private static final String CLAIMANTNAME = "claimantName";
    /** Constant field for relevantSite. */
    private static final String RELEVANTSITE = "relevantSite";
    /** Constant field for eligibleCir. */
    private static final String ELIGIBLECIR = "eligibleCir";
    /** Constant field for asPartOfPulpit. */
    private static final String ASPARTOFPULPIT = "asPartOfPulpit";
    /** Constant field for deadline. */
    private static final String DEADLINE = "deadline";
    /** Constant field for documents. */
    private static final String DOCUMENTS = "documents";
    /** Constant field for activityType. */
    private static final String ACTIVITYTYPE = "activityType";
    /** Constant field for isValidate. */
    private static final String ISVALIDATE = "isValidate";
    /** Constant field for projectWorkingTimes. */
    private static final String PROJECTWORKINGTIMES = "projectWorkingTimes";
    /** Constant field for job. */
    private static final String JOB = "job";
    /** Constant field for creator. */
    private static final String CREATOR = "creator";


    /** ProjectDataLoader instance (Singleton). */
    private static ProjectDataLoader instance;

    /**
     * Get the ProjectDataLoader singleton.
     * @param ctx The context
     * @return The dataloader instance
     */
    public static ProjectDataLoader getInstance(
                                            final android.content.Context ctx) {
        if (instance == null) {
            instance = new ProjectDataLoader(ctx);
        }
        return instance;
    }

    /**
     * Constructor.
     * @param ctx The context
     */
    private ProjectDataLoader(final android.content.Context ctx) {
        super(ctx);
    }


    @Override
    protected Project extractItem(final Map<?, ?> columns) {
        final Project project =
                new Project();

        return this.extractItem(columns, project);
    }
    /**
     * Extract an entity from a fixture element (YML).
     * @param columns Columns to extract
     * @param project Entity to extract
     * @return A Project entity
     */
    protected Project extractItem(final Map<?, ?> columns,
                Project project) {
        project.setId(this.parseIntField(columns, ID));
        project.setName(this.parseField(columns, NAME, String.class));
        project.setDescription(this.parseField(columns, DESCRIPTION, String.class));
        project.setCreatedAt(this.parseDateTimeField(columns, CREATEDAT));
        project.setCompany(this.parseField(columns, COMPANY, String.class));
        project.setClaimantName(this.parseField(columns, CLAIMANTNAME, String.class));
        project.setRelevantSite(this.parseField(columns, RELEVANTSITE, String.class));
        project.setEligibleCir(this.parseIntField(columns, ELIGIBLECIR));
        project.setAsPartOfPulpit(this.parseBooleanField(columns, ASPARTOFPULPIT));
        project.setDeadline(this.parseDateTimeField(columns, DEADLINE));
        project.setDocuments(this.parseField(columns, DOCUMENTS, String.class));
        project.setActivityType(this.parseField(columns, ACTIVITYTYPE, String.class));
        project.setIsValidate(this.parseBooleanField(columns, ISVALIDATE));
        project.setProjectWorkingTimes(this.parseMultiRelationField(columns, PROJECTWORKINGTIMES, WorkingTimeDataLoader.getInstance(this.ctx)));
        if (project.getProjectWorkingTimes() != null) {
            for (WorkingTime related : project.getProjectWorkingTimes()) {
                related.setProject(project);
            }
        }
        project.setJob(this.parseSimpleRelationField(columns, JOB, JobDataLoader.getInstance(this.ctx)));
        if (project.getJob() != null) {
            if (project.getJob().getProjects() == null) {
                project.getJob().setProjects(
                        new ArrayList<Project>());
            }

            project.getJob().getProjects().add(project);
        }
        project.setCreator(this.parseSimpleRelationField(columns, CREATOR, UserDataLoader.getInstance(this.ctx)));
        if (project.getCreator() != null) {
            if (project.getCreator().getCreatedProjects() == null) {
                project.getCreator().setCreatedProjects(
                        new ArrayList<Project>());
            }

            project.getCreator().getCreatedProjects().add(project);
        }

        return project;
    }
    /**
     * Loads Projects into the DataManager.
     * @param dataManager The DataManager
     */
    @Override
    public void load(final DataManager dataManager) {
        for (final Project project : this.items.values()) {
            int id = dataManager.persist(project);
            project.setId(id);

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
    protected Project get(final String key) {
        final Project result;
        
        if (this.items.containsKey(key)) {
            result = this.items.get(key);
        } else {
            result = null;
        }
        
        return result;
    }
}

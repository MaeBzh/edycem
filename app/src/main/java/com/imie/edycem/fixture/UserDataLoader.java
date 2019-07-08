/*
 * UserDataLoader.java, Edycem Android
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



import com.imie.edycem.entity.User;
import com.imie.edycem.entity.WorkingTime;


/**
 * UserDataLoader.
 *
 * This dataloader implements the parsing method needed while reading
 * the fixtures files.
 */
public final class UserDataLoader
                        extends FixtureBase<User> {
    /** UserDataLoader name. */
    private static final String FILE_NAME = "User";

    /** Constant field for id. */
    private static final String ID = "id";
    /** Constant field for firstname. */
    private static final String FIRSTNAME = "firstname";
    /** Constant field for lastname. */
    private static final String LASTNAME = "lastname";
    /** Constant field for email. */
    private static final String EMAIL = "email";
    /** Constant field for isEligible. */
    private static final String ISELIGIBLE = "isEligible";
    /** Constant field for idSmartphone. */
    private static final String IDSMARTPHONE = "idSmartphone";
    /** Constant field for dateRgpd. */
    private static final String DATERGPD = "dateRgpd";
    /** Constant field for job. */
    private static final String JOB = "job";
    /** Constant field for userWorkingTimes. */
    private static final String USERWORKINGTIMES = "userWorkingTimes";


    /** UserDataLoader instance (Singleton). */
    private static UserDataLoader instance;

    /**
     * Get the UserDataLoader singleton.
     * @param ctx The context
     * @return The dataloader instance
     */
    public static UserDataLoader getInstance(
                                            final android.content.Context ctx) {
        if (instance == null) {
            instance = new UserDataLoader(ctx);
        }
        return instance;
    }

    /**
     * Constructor.
     * @param ctx The context
     */
    private UserDataLoader(final android.content.Context ctx) {
        super(ctx);
    }


    @Override
    protected User extractItem(final Map<?, ?> columns) {
        final User user =
                new User();

        return this.extractItem(columns, user);
    }
    /**
     * Extract an entity from a fixture element (YML).
     * @param columns Columns to extract
     * @param user Entity to extract
     * @return A User entity
     */
    protected User extractItem(final Map<?, ?> columns,
                User user) {
        user.setId(this.parseIntField(columns, ID));
        user.setFirstname(this.parseField(columns, FIRSTNAME, String.class));
        user.setLastname(this.parseField(columns, LASTNAME, String.class));
        user.setEmail(this.parseField(columns, EMAIL, String.class));
        user.setIsEligible(this.parseBooleanField(columns, ISELIGIBLE));
        user.setIdSmartphone(this.parseField(columns, IDSMARTPHONE, String.class));
        user.setDateRgpd(this.parseDateTimeField(columns, DATERGPD));
        user.setJob(this.parseSimpleRelationField(columns, JOB, JobDataLoader.getInstance(this.ctx)));
        if (user.getJob() != null) {
            if (user.getJob().getUsers() == null) {
                user.getJob().setUsers(
                        new ArrayList<User>());
            }

            user.getJob().getUsers().add(user);
        }
        user.setUserWorkingTimes(this.parseMultiRelationField(columns, USERWORKINGTIMES, WorkingTimeDataLoader.getInstance(this.ctx)));
        if (user.getUserWorkingTimes() != null) {
            for (WorkingTime related : user.getUserWorkingTimes()) {
                related.setUser(user);
            }
        }

        return user;
    }
    /**
     * Loads Users into the DataManager.
     * @param dataManager The DataManager
     */
    @Override
    public void load(final DataManager dataManager) {
        for (final User user : this.items.values()) {
            int id = dataManager.persist(user);
            user.setId(id);

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
    protected User get(final String key) {
        final User result;
        
        if (this.items.containsKey(key)) {
            result = this.items.get(key);
        } else {
            result = null;
        }
        
        return result;
    }
}

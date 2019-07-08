/*
 * SettingsDataLoader.java, Edycem Android
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




import com.imie.edycem.entity.Settings;


/**
 * SettingsDataLoader.
 *
 * This dataloader implements the parsing method needed while reading
 * the fixtures files.
 */
public final class SettingsDataLoader
                        extends FixtureBase<Settings> {
    /** SettingsDataLoader name. */
    private static final String FILE_NAME = "Settings";

    /** Constant field for id. */
    private static final String ID = "id";
    /** Constant field for rgpd. */
    private static final String RGPD = "rgpd";


    /** SettingsDataLoader instance (Singleton). */
    private static SettingsDataLoader instance;

    /**
     * Get the SettingsDataLoader singleton.
     * @param ctx The context
     * @return The dataloader instance
     */
    public static SettingsDataLoader getInstance(
                                            final android.content.Context ctx) {
        if (instance == null) {
            instance = new SettingsDataLoader(ctx);
        }
        return instance;
    }

    /**
     * Constructor.
     * @param ctx The context
     */
    private SettingsDataLoader(final android.content.Context ctx) {
        super(ctx);
    }


    @Override
    protected Settings extractItem(final Map<?, ?> columns) {
        final Settings settings =
                new Settings();

        return this.extractItem(columns, settings);
    }
    /**
     * Extract an entity from a fixture element (YML).
     * @param columns Columns to extract
     * @param settings Entity to extract
     * @return A Settings entity
     */
    protected Settings extractItem(final Map<?, ?> columns,
                Settings settings) {
        settings.setId(this.parseIntField(columns, ID));
        settings.setRgpd(this.parseField(columns, RGPD, String.class));

        return settings;
    }
    /**
     * Loads Settingss into the DataManager.
     * @param dataManager The DataManager
     */
    @Override
    public void load(final DataManager dataManager) {
        for (final Settings settings : this.items.values()) {
            int id = dataManager.persist(settings);
            settings.setId(id);

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
    protected Settings get(final String key) {
        final Settings result;
        
        if (this.items.containsKey(key)) {
            result = this.items.get(key);
        } else {
            result = null;
        }
        
        return result;
    }
}

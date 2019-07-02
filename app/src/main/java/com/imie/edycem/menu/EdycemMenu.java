/*
 * EdycemMenu.java, Edycem Android
 *
 * Copyright 2019
 * Description : 
 * Author(s)   : Harmony
 * Licence     : 
 * Last update : Jul 2, 2019
 *
 */
package com.imie.edycem.menu;


import android.support.v4.app.Fragment;

import com.imie.edycem.menu.base.EdycemMenuBase;

/**
 * EdycemMenu.
 * 
 * This class is an engine used to manage the different menus of your application.
 * Its use is quite simple :
 * Create a class called [YourMenuName]MenuWrapper in this package and
 * make it implement the interface MenuWrapperBase.
 * (For examples, please see CrudCreateMenuWrapper and CrudEditDeleteMenuWrapper in
 * this package.)
 * When this is done, just call this harmony command :
 * script/console.sh orm:menu:update.
 * This will auto-generate a group id for your menu.
 */
public class EdycemMenu
                extends EdycemMenuBase {

    /** Singleton unique instance. */
    private static volatile EdycemMenu singleton;

    /**
     * Constructor.
     * @param ctx The android.content.Context
     * @throws Exception If something bad happened
     */
    public EdycemMenu(final android.content.Context ctx) throws Exception {
        super(ctx);
    }

    /**
     * Constructor.
     * @param ctx The context
     * @param fragment The parent fragment
     * @throws Exception If something bad happened
     */
    public EdycemMenu(final android.content.Context ctx,
                        final Fragment fragment) throws Exception {
        super(ctx, fragment);
    }

    /** Get unique instance.
     * @param ctx The context
     * @return EdycemMenu instance
     * @throws Exception If something bad happened
     */
    public static final synchronized EdycemMenu getInstance(
                        final android.content.Context ctx) throws Exception {
        return getInstance(ctx, null);
    }

    /** Get unique instance.
     * @param ctx The context
     * @param fragment The parent fragment
     * @return EdycemMenu instance
     * @throws Exception If something bad happened
     */
    public static final synchronized EdycemMenu getInstance(
            final android.content.Context ctx, final Fragment fragment) throws Exception {
        if (singleton == null) {
            singleton = new EdycemMenu(ctx, fragment);
        }  else {
            singleton.ctx = ctx;
            singleton.fragment = fragment;
        }

        return singleton;
    }
}

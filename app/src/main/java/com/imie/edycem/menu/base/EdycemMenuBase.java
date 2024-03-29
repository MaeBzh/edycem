/*
 * EdycemMenuBase.java, Edycem Android
 *
 * Copyright 2019
 * Description : 
 * Author(s)   : Harmony
 * Licence     : 
 * Last update : Jul 2, 2019
 *
 */
package com.imie.edycem.menu.base;

import android.support.v4.app.FragmentActivity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;


/**
 * EdycemMenuBase.
 * This class is regenerated with harmony.
 * To modify or learn more about this class,
 * please see EdycemMenu.
 */
public abstract class EdycemMenuBase {

    /** Array of MenuWrapperBase. */
    protected SparseArray<MenuWrapperBase> menus =
                    new SparseArray<MenuWrapperBase>();
    /** android.content.Context. */
    protected android.content.Context ctx;
    /** parent fragment. */
    protected Fragment fragment;
    /** Menu. */
    protected Menu menu;

    /**
     * Constructor.
     * @param ctx context
     * @throws Exception if context is null
     */
    protected EdycemMenuBase(final android.content.Context ctx)
                                                        throws Exception {
        this(ctx, null);
    }

    /**
     * Constructor.
     * @param ctx context
     * @param fragment parent fragment
     * @throws Exception If context is null
     */
    protected EdycemMenuBase(final android.content.Context ctx,
                                final Fragment fragment) throws Exception {
        if (ctx == null) {
            throw new Exception(
                    "Unable to Initialise Menu Helper with no context");
        }

        this.fragment    = fragment;
        this.ctx     = ctx;

    }

    /** Initialize Menu component.
     * @param menu menu
     */
    private void initializeMenu(final Menu menu) {
        this.menu = menu;

        for (int i = 0; i < this.menus.size(); i++) {
            this.menus.valueAt(i).initializeMenu(menu,
                    (FragmentActivity) this.ctx,
                    this.fragment,
                    this.ctx);
        }

    }

    /** Update Menu component.
     * @param menu menu
     * @param ctx context
     */
    public void updateMenu(final Menu menu, final android.content.Context ctx) {
        if (ctx != null) {
            this.ctx = ctx;
        }

        this.initializeMenu(menu);
        this.updateMenu(menu);
    }

    /** Update Menu component.
     * @param menu menu
     */
    public void updateMenu(final Menu menu) {
        for (int i = 0; i < this.menus.size(); i++) {
            this.menus.valueAt(i).updateMenu(menu,
                    (FragmentActivity) this.ctx,
                    this.fragment,
                    this.ctx);
        }
    }

    /** Clear the menu components.
     * @param menu The menu to clear
     */
    public void clear(final Menu menu) {
        for (int i = 0; i < this.menus.size(); i++) {
            this.menus.valueAt(i).clear(menu,
                    (FragmentActivity) this.ctx,
                    this.fragment,
                    this.ctx);
        }
    }

    /** Call intent associate to menu item selected.
     * @param item item
     * @param ctx context
     * @return true if event has been treated
     */
    public boolean dispatch(final MenuItem item, final android.content.Context ctx) {
        if (ctx != null) {
            this.ctx = ctx;
        }

        return this.dispatch(item);
    }

    /** Call intent associate to menu item selected.
     * @param item item
     * @return true if event has been treated
     */
    private boolean dispatch(final MenuItem item) {
        return this.menus.get(item.getGroupId()).dispatch(item, this.ctx,
                this.fragment);
    }

    /**
     * Called when an activity you launched exits.
     * @see android.app.Activity#onActivityResult
     * @param requestCode The request code
     * @param resultCode The result code
     * @param data The intent
     * @param ctx The context
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data,
                                                             android.content.Context ctx) {
        this.onActivityResult(requestCode, resultCode, data, ctx, null);
    }

    /**
     * Called when an activity you launched exits.
     * @see android.app.Activity#onActivityResult
     * @param requestCode The request code
     * @param resultCode The result code
     * @param data The intent
     * @param ctx The context
     * @param fragment The fragment
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data,
                                          android.content.Context ctx, Fragment fragment) {
        if (ctx != null) {
            this.ctx = ctx;
        }

        if (fragment != null) {
            this.fragment = fragment;
        }

        this.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Called when an activity you launched exits.
     * @see android.app.Activity#onActivityResult
     * @param requestCode The request code
     * @param resultCode The result code
     * @param data The intent
     */
    private void onActivityResult(int requestCode, int resultCode,
                                                                 Intent data) {
        for (int i = 0; i < this.menus.size(); i++) {
            this.menus.valueAt(i).onActivityResult(requestCode,
                    resultCode,
                    data,
                    this.ctx,
                    this.fragment);
        }
    }

    /**
     * Hide the menus.
     */
    public void hideMenus() {
        for (int i = 0; i < this.menus.size(); i++) {
            this.menus.valueAt(i).hide(menu,
                    (FragmentActivity) this.ctx,
                    this.fragment,
                    this.ctx);
        }
        
        ((FragmentActivity) this.ctx).supportInvalidateOptionsMenu();
    }
    
    /**
     * Show the menus.
     */
    public void showMenus() {
        for (int i = 0; i < this.menus.size(); i++) {
            this.menus.valueAt(i).show(menu,
                    (FragmentActivity) this.ctx,
                    this.fragment,
                    this.ctx);
        }
        
        ((FragmentActivity) this.ctx).supportInvalidateOptionsMenu();
    }
}

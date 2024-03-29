/*
 * HarmonyGridFragment.java, Edycem Android
 *
 * Copyright 2019
 * Description : 
 * Author(s)   : Harmony
 * Licence     : 
 * Last update : Jul 2, 2019
 *
 */
package com.imie.edycem.harmony.view;

import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.imie.edycem.menu.EdycemMenu;

/**
 * Static library support version of the framework's 
 * {@link android.app.ListFragment}.
 * Used to write apps that run on platforms prior to Android 3.0.  When running
 * on Android 3.0 or above, this implementation is still used; it does not try
 * to switch to the framework's implementation.  See the framework SDK
 * documentation for a class overview.
 * @param <T> The type to hold
 */
public abstract class HarmonyGridFragment<T> extends Fragment implements LoaderManager.LoaderCallbacks<List<T>> {
    /**
     * Empty ID.
     */
    protected static final int INTERNAL_EMPTY_ID = 0x00ff0001;
    /**
     * Progress container ID.
     */
    protected static final int INTERNAL_PROGRESS_CONTAINER_ID = 0x00ff0002;
    /**
     * List container ID.
     */
    protected static final int INTERNAL_LIST_CONTAINER_ID = 0x00ff0003;
    
    /**
     * Handler.
     */
    private final Handler mHandler = new Handler();

    /**
     * RequestFocus runnable.
     */
    private final Runnable mRequestFocus = new Runnable() {
        public void run() {
            mGrid.focusableViewAvailable(mGrid);
        }
    };
    
    /**
     * On click listener.
     */
    private final AdapterView.OnItemClickListener mOnClickListener
            = new AdapterView.OnItemClickListener() {
        public void onItemClick(final AdapterView<?> parent, final View v, final int position, final long id) {
            onListItemClick((GridView) parent, v, position, id);
        }
    };

    /**
     * Array adapter.
     */
    private ArrayAdapter<T> mAdapter;
    /**
     * Grid view to show.
     */
    private GridView mGrid;
    /**
     * Empty view.
     */
    private View mEmptyView;
    /**
     * Empty view text view.
     */
    private TextView mStandardEmptyView;
    /**
     * Progress container.
     */
    private View mProgressContainer;
    /**
     * List container.
     */
    private View mListContainer;
    /**
     * emptyText.
     */
    private CharSequence mEmptyText;
    /**
     * is list shown.
     */
    private boolean mListShown;

    /*
     * Provide default implementation to return a simple list view.  Subclasses
     * can override to replace with their own layout.  If doing so, the
     * returned view hierarchy <em>must</em> have a GridView whose id
     * is {@link android.R.id#list android.R.id.list} and can optionally
     * have a sibling view id {@link android.R.id#empty android.R.id.empty}
     * that is to be shown when the list is empty.
     * 
     * <p>If you are overriding this method with your own custom content,
     * consider including the standard layout 
     * {@link android.R.layout#list_content}
     * in your layout file, so that you continue to retain all of the standard
     * behavior of ListFragment.  In particular, this is currently the only
     * way to have the built-in indeterminant progress state be shown.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final android.content.Context context = getActivity();

        FrameLayout root = new FrameLayout(context);

        // ------------------------------------------------------------------

        LinearLayout pframe = new LinearLayout(context);
        pframe.setId(INTERNAL_PROGRESS_CONTAINER_ID);
        pframe.setOrientation(LinearLayout.VERTICAL);
        pframe.setVisibility(View.GONE);
        pframe.setGravity(Gravity.CENTER);

        ProgressBar progress = new ProgressBar(context, null,
                android.R.attr.progressBarStyleLarge);
        pframe.addView(progress, new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        root.addView(pframe, new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        // ------------------------------------------------------------------

        FrameLayout lframe = new FrameLayout(context);
        lframe.setId(INTERNAL_LIST_CONTAINER_ID);
        
        TextView tv = new TextView(this.getActivity());
        tv.setId(INTERNAL_EMPTY_ID);
        tv.setGravity(Gravity.CENTER);
        lframe.addView(tv, new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        
        GridView lv = new GridView(this.getActivity());
        lv.setId(android.R.id.list);
        lv.setDrawSelectorOnTop(false);
        lframe.addView(lv, new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        root.addView(lframe, new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        
        // ------------------------------------------------------------------

        root.setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        return root;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        try {
            EdycemMenu.getInstance(this.getActivity(), this).clear(menu);
            EdycemMenu.getInstance(this.getActivity(), this).updateMenu(menu, this.getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean result;

        try {
            result = EdycemMenu.getInstance(this.getActivity(), this).dispatch(item, this.getActivity());
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        }

        return result;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            EdycemMenu.getInstance(this.getActivity(), this)
                    .onActivityResult(requestCode, resultCode, data, this.getActivity(), this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    /*
     * Attach to list view once the view hierarchy has been created.
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.ensureList();
    }

    /**
     * Detach from list view.
     */
    @Override
    public void onDestroyView() {
        this.mHandler.removeCallbacks(mRequestFocus);
        this.mGrid = null;
        this.mListShown = false;
        this.mEmptyView = null;
        this.mProgressContainer = null;
        this.mListContainer = null;
        this.mStandardEmptyView = null;
        super.onDestroyView();
    }

    /**
     * This method will be called when an item in the list is selected.
     * Subclasses should override. Subclasses can call
     * getGridView().getItemAtPosition(position) if they need to access the
     * data associated with the selected item.
     *
     * @param l The GridView where the click happened
     * @param v The view that was clicked within the GridView
     * @param position The position of the view in the list
     * @param id The row id of the item that was clicked
     */
    public void onListItemClick(final GridView l, final View v, final int position, final long id) {
    }

    /**
     * Provide the cursor for the list view.
     * @param adapter adapter to set
     */
    public void setListAdapter(final ArrayAdapter<T> adapter) {
        boolean hadAdapter = this.mAdapter != null;
        this.mAdapter = adapter;

        if (this.mGrid != null) {
            this.mGrid.setAdapter(adapter);

            if (!this.mListShown && !hadAdapter) {
                // The list was hidden, and previously didn't have an
                // adapter.  It is now time to show it.
                this.setListShown(true, this.getView().getWindowToken() != null);
            }
        }
    }

    /**
     * Set the currently selected list item to the specified.
     * position with the adapter's data.
     *
     * @param position position
     */
    public void setSelection(final int position) {
        this.ensureList();
        this.mGrid.setSelection(position);
    }

    /**
     * Get the position of the currently selected list item.
     * @return selected item position 
     */
    public int getSelectedItemPosition() {
        this.ensureList();
        return this.mGrid.getSelectedItemPosition();
    }

    /**
     * Get the cursor row ID of the currently selected list item.
     * @return selected item id
     */
    public long getSelectedItemId() {
        this.ensureList();
        return this.mGrid.getSelectedItemId();
    }

    /**
     * Get the activity's list view widget.
     * @return grid view
     */
    public GridView getGridView() {
        this.ensureList();
        return this.mGrid;
    }

    /**
     * The default content for a ListFragment has a TextView that can
     * be shown when the list is empty.  If you would like to have it
     * shown, call this method to supply the text it should use.
     * @param text text
     */
    public void setEmptyText(final CharSequence text) {
        this.ensureList();

        if (this.mStandardEmptyView == null) {
            throw new IllegalStateException("Can't be used with a custom content view");
        }

        this.mStandardEmptyView.setText(text);

        if (this.mEmptyText == null) {
            this.mGrid.setEmptyView(this.mStandardEmptyView);
        }

        this.mEmptyText = text;
    }
    
    /**
     * Control whether the list is being displayed.  You can make it not
     * displayed if you are waiting for the initial data to show in it.  During
     * this time an indeterminant progress indicator will be shown instead.
     * 
     * <p>Applications do not normally need to use this themselves.  The default
     * behavior of ListFragment is to start with the list not being shown, only
     * showing it once an adapter is given with 
     * {@link #setListAdapter(ListAdapter)}.
     * If the list at that point had not been shown, when it does get shown
     * it will be do without the user ever seeing the hidden state.
     * 
     * @param shown If true, the list view is shown; if false, the progress
     * indicator.  The initial value is true.
     */
    public void setListShown(final boolean shown) {
        this.setListShown(shown, true);
    }
    
    /**
     * Like {@link #setListShown(boolean)}, but no animation is used when
     * transitioning from the previous state.
     * @param shown shown 
     */
    public void setListShownNoAnimation(final boolean shown) {
        this.setListShown(shown, false);
    }
    
    /**
     * Control whether the list is being displayed.  You can make it not
     * displayed if you are waiting for the initial data to show in it.  During
     * this time an indeterminant progress indicator will be shown instead.
     * 
     * @param shown If true, the list view is shown; if false, the progress
     * indicator.  The initial value is true.
     * @param animate If true, an animation will be used to transition to the
     * new state.
     */
    private void setListShown(final boolean shown, final boolean animate) {
        this.ensureList();

        if (this.mProgressContainer == null) {
            throw new IllegalStateException("Can't be used with a custom content view");
        }

        if (this.mListShown == shown) {
            return;
        }

        this.mListShown = shown;

        if (shown) {
            if (animate) {
                this.mProgressContainer.startAnimation(AnimationUtils.loadAnimation(
                        getActivity(), android.R.anim.fade_out));
                this.mListContainer.startAnimation(AnimationUtils.loadAnimation(
                        getActivity(), android.R.anim.fade_in));
            } else {
                this.mProgressContainer.clearAnimation();
                this.mListContainer.clearAnimation();
            }

            this.mProgressContainer.setVisibility(View.GONE);
            this.mListContainer.setVisibility(View.VISIBLE);
        } else {
            if (animate) {
                this.mProgressContainer.startAnimation(AnimationUtils.loadAnimation(
                        getActivity(), android.R.anim.fade_in));
                this.mListContainer.startAnimation(AnimationUtils.loadAnimation(
                        getActivity(), android.R.anim.fade_out));
            } else {
                this.mProgressContainer.clearAnimation();
                this.mListContainer.clearAnimation();
            }

            this.mProgressContainer.setVisibility(View.VISIBLE);
            this.mListContainer.setVisibility(View.GONE);
        }
    }
    
    /**
     * Get the ListAdapter associated with this activity's GridView.
     * @return list adapter
     */
    public ListAdapter getListAdapter() {
        return this.mAdapter;
    }

    /**
     * Ensures list.
     */
    private void ensureList() {
        if (this.mGrid != null) {
            return;
        }

        View root = getView();

        if (root == null) {
            throw new IllegalStateException("Content view not yet created");
        }

        if (root instanceof GridView) {
            this.mGrid = (GridView) root;
        } else {
            this.mStandardEmptyView = (TextView) root.findViewById(INTERNAL_EMPTY_ID);

            if (this.mStandardEmptyView == null) {
                this.mEmptyView = root.findViewById(android.R.id.empty);
            } else {
                this.mStandardEmptyView.setVisibility(View.GONE);
            }

            this.mProgressContainer = root.findViewById(INTERNAL_PROGRESS_CONTAINER_ID);
            this.mListContainer = root.findViewById(INTERNAL_LIST_CONTAINER_ID);
            View rawGridView = root.findViewById(android.R.id.list);

            if (!(rawGridView instanceof GridView)) {
                if (rawGridView == null) {
                    throw new RuntimeException("Your content must have a GridView whose id "
                            + "attribute is 'android.R.id.list'");
                }

                throw new RuntimeException("Content has view with id attribute 'android.R.id.list'"
                        + "that is not a GridView class");
            }

            this.mGrid = (GridView) rawGridView;

            if (this.mEmptyView != null) {
                this.mGrid.setEmptyView(this.mEmptyView);
            } else if (this.mEmptyText != null) {
                this.mStandardEmptyView.setText(this.mEmptyText);
                this.mGrid.setEmptyView(this.mStandardEmptyView);
            }
        }

        this.mListShown = true;
        this.mGrid.setOnItemClickListener(this.mOnClickListener);

        if (this.mAdapter != null) {
            ArrayAdapter<T> adapter = this.mAdapter;
            this.mAdapter = null;
            this.setListAdapter(adapter);
        } else {
            // We are starting without an adapter, so assume we won't
            // have our data right away and start with the progress indicator.
            if (this.mProgressContainer != null) {
                this.setListShown(false, false);
            }
        }

        this.mHandler.post(mRequestFocus);
    }
}

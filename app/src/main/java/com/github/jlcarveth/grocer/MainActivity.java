package com.github.jlcarveth.grocer;

import android.net.Uri;
import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.github.jlcarveth.grocer.layout.AddDialogFragment;
import com.github.jlcarveth.grocer.layout.GroceryFragment;
import com.github.jlcarveth.grocer.layout.SettingsFragment;
import com.github.jlcarveth.grocer.model.GroceryItem;
import com.github.jlcarveth.grocer.util.DataHandler;
import com.github.jlcarveth.grocer.util.StorageHandler;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        GroceryFragment.OnListFragmentInteractionListener,
        SettingsFragment.OnFragmentInteractionListener {

    /**
     * Fragment Manager
     */
    FragmentManager fm = getFragmentManager();

    /**
     * StorageHandler - Needed to initialize the DataHandler
     */
    private StorageHandler storageHandler;

    /**
     * DataHandler - for handling data processing with DB
     */
    private DataHandler dataHandler;


    /**
     * A way to represent the types of fragments that can be displayed in this Activity.
     */
    public enum FragmentType {
        GROCERY("GROCERY"),
        RECIPE("RECIPE"),
        ADD_DIALOG("ADD_DIAG"),
        EDIT_DIALOG("EDIT_DIAG"),
        SETTINGS("SETTINGS");

        private final String tag;

        FragmentType(String tag) {
            this.tag = tag;
        }

        private String getTag() {
            return tag;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initializes the FAB, assigns the listener.
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddDialogFragment addDialogFragment = new AddDialogFragment();
                addDialogFragment.show(fm, "ADD_DIAG");
            }
        });

        // Drawer Layout for the slide out drawer.
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        // Initializes the Nav. View and assigns this class as the listener.
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            setFragment(FragmentType.GROCERY);
        }

        storageHandler = new StorageHandler(getApplicationContext());
        dataHandler = new DataHandler(storageHandler);
    }

    /**
     * Called when the activity has detected the user's press of the back key.
     * If the drawer is open, the back button will close it.
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Initialize the contents of the Activity's standard options menu.
     * @param menu The options menu in which you place your items
     * @return You must return true for the menu to be displayed.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * This hook is called whenever an item in your options menu is selected.
     * @param item The menu item that was selected.
     * @return Return false to allow normal menu processing to proceed, true to consume it here.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            setFragment(FragmentType.SETTINGS);
        } else if (id == R.id.action_sort) {
            List list = dataHandler.getGroceryList();

            list = dataHandler.sortGroceryList(list);
            System.out.println("Sorted: " + list.toString());

            dataHandler.clearGroceryList();
            dataHandler.addGroceryList(list);

            GroceryFragment fragment = (GroceryFragment) fm.findFragmentByTag(FragmentType.GROCERY.getTag());

            if (fragment != null && fragment.isVisible()) {
                System.out.println("UpdateData called");
                fragment.updateData();
            }

        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Called when an item in the navigation menu is selected.
     * @param item The menu item that was selected.
     * @return true to display the item as the selected item.
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_import) {
            // Handle the camera action
        } else if (id == R.id.nav_share_list) {

        } else if (id == R.id.nav_upgrade) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_grocery_list) {
            setFragment(FragmentType.GROCERY);
        } else if (id == R.id.nav_recipes) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setFragment(FragmentType ft) {
        Object fragment = null;
        String tag = "";
        switch (ft) {
            case GROCERY:
                fragment = new GroceryFragment();
                tag = FragmentType.GROCERY.getTag();
                break;
            case RECIPE:
                // Do nothing right now, no recipe fragment created yet
                tag = FragmentType.RECIPE.getTag();
                break;
            case ADD_DIALOG:
                // Inflate the DialogFragment
                tag = FragmentType.ADD_DIALOG.getTag();
                fragment = new AddDialogFragment();
                break;
            case SETTINGS:
                tag = FragmentType.SETTINGS.getTag();
                fragment = new SettingsFragment();
                break;
            default:
                fragment = null;
                return;
        }

        // This should work for the different Fragments being implemented
        if (fragment instanceof android.support.v4.app.Fragment) {
            fm.beginTransaction()
                    .replace(R.id.content, (Fragment) fragment, tag)
                    .addToBackStack(null)
                    .commit();
        } else if (fragment instanceof  android.app.Fragment) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.content, (android.app.Fragment) fragment, tag)
                    .addToBackStack(null)
                    .commit();
        }

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    /**
     * IDEK I think this was all auto-generated by Android Studio
     * Google searching reveals nothing about this interface.
     * @param item
     */
    public void onListFragmentInteraction(GroceryItem item) {
        System.out.println("Mystery Method has been called.");
        System.out.println(item);
    }
}

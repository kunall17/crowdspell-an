package com.lambda.crowdspell;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.*;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lambda.crowdspell.HTTPRequest.GameManager;
import com.lambda.crowdspell.HTTPRequest.UserManager;
import com.igl.crowdword.R;
import com.lambda.crowdspell.core.UserFunctions;
import com.lambda.crowdspell.fxns.User;
import com.lambda.crowdspell.fxns.WordSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class DashboardActivity extends ActionBarActivity {


    public static final String ARG_BUNDLE = "arg_bundle";
    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    Context context;


    pagerAdapter MyAdapter;
    TabLayout tablayout;
    ViewPager viewpager;

    public android.support.v4.app.Fragment frag1;
    public android.support.v4.app.Fragment frag2;
    public android.support.v4.app.Fragment frag3;
    User currentUser;

    ProgressDialog progressDialog;
    View root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        root = getLayoutInflater().inflate(R.layout.activity_dashboard, null);
        setContentView(root);

        ProgressDialog progressDialog;
        progressDialog = ProgressDialog.show(DashboardActivity.this, "Please wait ...", "Task in progress ...", true);
        progressDialog.setCancelable(true);

        context = this;

//        fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent in1 = new Intent(DashboardActivity.this, CreateSetActivity.class);
//                startActivity(in1);
//
//            }
//        });

        frag1 = new SetsFragment();
        frag2 = new TopFragment();
        frag3 = new FavouritesFragment();

        viewpager = (ViewPager) findViewById(R.id.pager);
        tablayout = (TabLayout) findViewById(R.id.tab_layout);


        MyAdapter = new pagerAdapter(getSupportFragmentManager());


        MyAdapter.addFragment(frag1, "All Sets");
        MyAdapter.addFragment(frag2, "Top");
        MyAdapter.addFragment(frag3, "Favourites");

        viewpager.setAdapter(MyAdapter);
        tablayout.setTabsFromPagerAdapter(MyAdapter);
        viewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tablayout));

        currentUser = new UserFunctions().getCurrentUser(getBaseContext());
        Toast.makeText(DashboardActivity.this, "Logged in as" + currentUser.getUsername(), Toast.LENGTH_LONG).show();

        toolbar = (Toolbar) findViewById(R.id.toolbar_dashboard);
        toolbar.setTitle("Home");

        setSupportActionBar(toolbar);
        //Initializing NavigationView
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        TextView username_drawer = (TextView) findViewById(R.id.username_drawer_txt);
        username_drawer.setText(currentUser.getUsername());

        if (new UserFunctions().checkIfGuestModeIsOn(getBaseContext())) {
            navigationView.removeView(findViewById((R.id.drawer_create)));
        } else {
            navigationView.removeView(findViewById((R.id.drawer_create_user)));
        }
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

                                                             // This method will trigger on item Click of navigation menu
                                                             @Override
                                                             public boolean onNavigationItemSelected(MenuItem menuItem) {


             //Checking if the item is in checked state or not, if not make it in checked state
             if (menuItem.isChecked())
                 menuItem.setChecked(false);
             else menuItem.setChecked(true);

             //Closing drawer on item click
             drawerLayout.closeDrawers();

             //Check to see which item was being clicked and perform appropriate action
             switch (menuItem.getItemId()) {

                 case R.id.drawer_home:
                     viewpager.setCurrentItem(0);
                     return true;
    /*

    //Replacing the main content with ContentFragment Which is our Inbox View;
    case R.id.drawer_profile:

    Intent in = new Intent(DashboardActivity.this, ProfileActivity.class);
    startActivity(in);
    return true;
    */

                 // For rest of the options we just show a toast on click
                 case R.id.drawer_help:
                     Snackbar.make(root, "Click on any set to play!", Snackbar.LENGTH_LONG).setAction("DISMISS", null).show();
                     return true;
                 case R.id.drawer_create:
                     if (!new UserFunctions().checkIfGuestModeIsOn(getBaseContext())) {
                         Intent in1 = new Intent(DashboardActivity.this, CreateSetActivity.class);
                         startActivity(in1);
                     } else {
                         Toast.makeText(DashboardActivity.this, "You are a guest and cannot create sets!", Toast.LENGTH_LONG).show();
                     }
                     return true;
                 case R.id.drawer_exit:
                     AlertDialog.Builder builder = new AlertDialog.Builder(DashboardActivity.this);
                     builder.setTitle("Exit?");
                     builder.setMessage("Are you Sure you want to Exit?");
                     builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                         @Override
                         public void onClick(DialogInterface dialog, int id) {
                             System.exit(0);
                         }
                     });
                     builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                         @Override
                         public void onClick(DialogInterface dialog, int id) {
                             dialog.dismiss();
                         }
                     });
                     AlertDialog dialog = builder.create();
                     dialog.show();
                     return true;
    /*
    case R.id.drawer_popular:
    //                        spn_adapter.add("mostpopular");
    return true;
    case R.id.drawer_rated:
    //                        spn_adapter.add("mostrated");
    return true;
    */
                 case R.id.drawer_create_user:
                     Intent in4 = new Intent(DashboardActivity.this, newUserActivity.class);
                     startActivity(in4);
                     return true;
                 case R.id.drawer_suprise:
                     Random rnd = new Random();
    //
    //                        final int x = rnd.nextInt(wordset_list.size());
    //                        WordSet wordset = wordset_list.get(x);

                     new AlertDialog.Builder(DashboardActivity.this)
                             .setTitle("Suprise Me")
    //                                .setMessage("Do you want to play " + wordset.getName() + "?")
                             .setIcon(android.R.drawable.ic_dialog_alert)
                             .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                 public void onClick(DialogInterface dialog, int whichButton) {
    //                                        startGame(x);
                                 }
                             })
                             .setNegativeButton(android.R.string.no, null).show();
                     return true;
                 case R.id.drawer_leaderboard:
                     viewpager.setCurrentItem(1);
                     return true;
             }

             return true;
         }
     }

        );

        // Initializing Drawer Layout and ActionBarToggle
        drawerLayout = (DrawerLayout)

                findViewById(R.id.drawer);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank

                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
        progressDialog.dismiss();
        Log.d("It came here", "asd");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
//        SearchManager searchManager =
//                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        //SearchView searchView =
        //       (SearchView) menu.findItem(R.id.search).getActionView();


//        MenuItem mSearchMenuItem = menu.findItem(R.id.search);
//        SearchView searchView =
//                (SearchView) MenuItemCompat.getActionView(mSearchMenuItem);
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(
//                new ComponentName(getApplicationContext(),
//                        DashboardActivity.class)));
//
//
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
//
//
//        searchView.setIconifiedByDefault(false);
//
//        SearchView.OnQueryTextListener textChangeListener = new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                // this is your adapter that will be filtered
////                myAdapter.getFilter().filter(newText);
//                System.out.println("on text chnge text: " + newText);
//                return true;
//            }
//
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                // this is your adapter that will be filtered
////                myAdapter.getFilter().filter(query);
//                System.out.println("on query submit: " + query);
//                return true;
//            }
//        };
//        searchView.setOnQueryTextListener(textChangeListener);

        return super.onCreateOptionsMenu(menu);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.menu_dashboard_search) {
            UserManager usm = new UserManager();
            String search = ""; //TODO search get as astring;
            List<WordSet> wordsetss;
            GameManager.searchAsync asd = new GameManager.searchAsync();

            //TODO   wordsetss = asd.execute(search);

        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */

    Fragment sf;

    class pagerAdapter extends FragmentPagerAdapter {


        private final List<android.support.v4.app.Fragment> FragmentList = new ArrayList();
        private final List<String> FragmentTitles = new ArrayList();

        public pagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(android.support.v4.app.Fragment fragment, String title) {
            FragmentList.add(fragment);
            FragmentTitles.add(title);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            return FragmentList.get(position);
        }

        @Override
        public int getCount() {
            return FragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return FragmentTitles.get(position);
        }
    }
}

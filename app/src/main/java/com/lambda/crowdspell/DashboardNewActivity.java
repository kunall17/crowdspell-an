package com.lambda.crowdspell;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.igl.crowdword.R;
import com.lambda.crowdspell.core.UserFunctions;
import com.lambda.crowdspell.fxns.WordSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class DashboardNewActivity extends AppCompatActivity implements YourRecyclerAdapter.AdapterCallback {
    // Need this to link with the Snackbar
    private CoordinatorLayout mCoordinator;
    //Need this to set the title of the app bar
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private FloatingActionButton mFab;
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ViewPager mPager;
    private YourPagerAdapter mAdapter;
    private TabLayout mTabLayout;
    List<WordSet> fav_list;
    String[] top_array;
    String[] fav_list_string;
    List<WordSet> wordset_list;
    Boolean top_empty = false;
    String[] sets_list_string;
    private NavigationView navigationView;
    View root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_new);


        String sets = getIntent().getStringExtra("json_sets");
        String fav = getIntent().getStringExtra("json_fav");
        String top[] = getIntent().getStringArrayExtra("json_top");
        System.out.println(fav.length() + "-1");
        System.out.println(fav + "-2");
        System.out.println("fav-" + fav);
        System.out.println("top-" + getIntent().getStringExtra("json_top"));
        Gson gson = new Gson();
        wordset_list = Arrays.asList(gson.fromJson(sets, WordSet[].class));
        try {

            if (fav != "" || fav.isEmpty() == false || fav.equals("") == false) {
                fav_list = Arrays.asList(gson.fromJson(fav, WordSet[].class));
                fav_list_string = new String[fav_list.size()];
                for (int i = 0; i < fav_list.size(); i++) {
                    fav_list_string[i] = fav_list.get(i).getName();
                }
            } else { //null
                fav_list_string = null;
            }

        } catch (Exception e) {
            System.out.println(e.toString());
        }
        mCoordinator = (CoordinatorLayout) findViewById(R.id.root_coordinator);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle("Home");

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView = (NavigationView) findViewById(R.id.navigation_drawer);
        if (new UserFunctions().checkIfGuestModeIsOn(getBaseContext())) {
            navigationView.removeView(findViewById((R.id.drawer_create)));
        } else {
            navigationView.removeView(findViewById((R.id.drawer_create_user)));
        }

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        sets_list_string = new String[wordset_list.size()];

        for (int i = 0; i < wordset_list.size(); i++) {
            sets_list_string[i] = wordset_list.get(i).getName();
        }

        mAdapter = new YourPagerAdapter(getSupportFragmentManager(), sets_list_string, top, fav_list_string);

        mPager = (ViewPager) findViewById(R.id.view_pager);
        mPager.setAdapter(mAdapter);

        //Notice how the Tab Layout links with the Pager Adapter
        mTabLayout.setTabsFromPagerAdapter(mAdapter);

        //Notice how The Tab Layout adn View Pager object are linked
        mTabLayout.setupWithViewPager(mPager);
        mPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));


        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Notice how the Coordinator Layout object is used here
//                Snackbar.make(mCoordinator, "FAB Clicked", Snackbar.LENGTH_SHORT).setAction("DISMISS", null).show();
                if (!new UserFunctions().checkIfGuestModeIsOn(getBaseContext())) {
                    Intent in1 = new Intent(DashboardNewActivity.this, CreateSetActivity.class);
                    startActivity(in1);
                } else {
                    Toast.makeText(DashboardNewActivity.this, "You are a guest and cannot create sets!", Toast.LENGTH_LONG).show();
                }
            }
        });

        //Notice how the title is set on the Collapsing Toolbar Layout instead of the Toolbar
        mCollapsingToolbarLayout.setTitle("Home");


        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

                                                             // This method will trigger on item Click of navigation menu
                                                             @Override
                                                             public boolean onNavigationItemSelected(MenuItem menuItem) {


                                                                 //Checking if the item is in checked state or not, if not make it in checked state
                                                                 if (menuItem.isChecked())
                                                                     menuItem.setChecked(false);
                                                                 else menuItem.setChecked(false);

                                                                 //Closing drawer on item click
                                                                 mDrawerLayout.closeDrawers();

                                                                 //Check to see which item was being clicked and perform appropriate action
                                                                 switch (menuItem.getItemId()) {

                                                                     case R.id.drawer_home:
                                                                         mPager.setCurrentItem(0);
                                                                         menuItem.setChecked(false);

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
                                                                         Snackbar.make(mCoordinator, "Click on any set to play!", Snackbar.LENGTH_LONG).setAction("DISMISS", null).show();
                                                                         menuItem.setChecked(false);
                                                                         return true;
                                                                     case R.id.drawer_create:
                                                                         if (!new UserFunctions().checkIfGuestModeIsOn(getBaseContext())) {
                                                                             Intent in1 = new Intent(DashboardNewActivity.this, CreateSetActivity.class);
                                                                             startActivity(in1);
                                                                         } else {
                                                                             Toast.makeText(DashboardNewActivity.this, "You are a guest and cannot create sets!", Toast.LENGTH_LONG).show();
                                                                         }
                                                                         menuItem.setChecked(false);
                                                                         return true;
                                                                     case R.id.drawer_exit:
                                                                         AlertDialog.Builder builder = new AlertDialog.Builder(DashboardNewActivity.this);
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
                                                                         menuItem.setChecked(false);
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
                                                                         Intent in4 = new Intent(DashboardNewActivity.this, newUserActivity.class);
                                                                         startActivity(in4);
                                                                         menuItem.setChecked(false);
                                                                         return true;
                                                                     case R.id.drawer_suprise:
                                                                         Random rnd = new Random();
                                                                         //
                                                                         //                        final int x = rnd.nextInt(wordset_list.size());
                                                                         //                        WordSet wordset = wordset_list.get(x);

                                                                         new AlertDialog.Builder(DashboardNewActivity.this)
                                                                                 .setTitle("Suprise Me")
                                                                                         //                                .setMessage("Do you want to play " + wordset.getName() + "?")
                                                                                 .setIcon(android.R.drawable.ic_dialog_alert)
                                                                                 .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                                                                     public void onClick(DialogInterface dialog, int whichButton) {
                                                                                         //                                        startGame(x);
                                                                                     }
                                                                                 })
                                                                                 .setNegativeButton(android.R.string.no, null).show();
                                                                         menuItem.setChecked(false);
                                                                         return true;
                                                                     case R.id.drawer_leaderboard:
                                                                         mPager.setCurrentItem(1);
                                                                         menuItem.setChecked(false);
                                                                         return true;
                                                                 }

                                                                 return true;
                                                             }
                                                         }

        );
    }


    public void startGame(int position) {
        Intent in1 = new Intent(DashboardNewActivity.this, GameNewActivity.class);

        Gson gson = new Gson();
        String json_new = gson.toJson(wordset_list.get(position));

        in1.putExtra("json_new", json_new);
        //in1.putExtra ("wordName", game_word[position].toUpperCase());
        startActivity(in1);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return true;
    }

    public void runFromSets(int position) {

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
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMethodCallback() {

    }

    @Override
    public void onMethodCallback(int whichPage, int position) {
        switch (whichPage - 1) {
            case (paths.FAV_FRAGMENTS):
                System.out.println("Called fav-" + position);
                break;
            case (paths.TOP_FRAGMENTS):
                System.out.println("Called top-" + position);
                break;
            case (paths.SETS_FRAGMENTS):
                System.out.println("Called sets-" + position);
                startGame(position);
                break;
        }
    }

    public static class MyFragment extends Fragment {
        public static final java.lang.String ARG_PAGE = "arg_page";
        public static final java.lang.String ARG_LIST = "arg_list";

        public MyFragment() {

        }

        public static MyFragment newInstance(int pageNumber, String[] list) {
            MyFragment myFragment = new MyFragment();
            Bundle arguments = new Bundle();
            arguments.putInt(ARG_PAGE, pageNumber + 1);
            arguments.putStringArray(ARG_LIST, list);
            myFragment.setArguments(arguments);
            return myFragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            Bundle arguments = getArguments();
            int pageNumber = arguments.getInt(ARG_PAGE);
            String[] list = arguments.getStringArray(ARG_LIST);
            RecyclerView recyclerView = new RecyclerView(getActivity());
            recyclerView.setAdapter(new YourRecyclerAdapter(getActivity(), pageNumber, list));
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            return recyclerView;
        }

        @Override
        public void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);
        }
    }


}

class paths {
    public static final int SETS_FRAGMENTS = 0;
    public static final int TOP_FRAGMENTS = 1;
    public static final int FAV_FRAGMENTS = 2;
}

class YourPagerAdapter extends FragmentStatePagerAdapter {

    public YourPagerAdapter(FragmentManager fm) {
        super(fm);

    }

    String[] sets_list, top_list, fav_list;

    public YourPagerAdapter(FragmentManager supportFragmentManager, String[] sets_list, String[] top_list, String[] fav_list) {
        super(supportFragmentManager);
        this.sets_list = sets_list;
        this.fav_list = fav_list;
        this.top_list = top_list;
    }

    @Override
    public Fragment getItem(int position) {
        DashboardNewActivity.MyFragment myFragment = null;
        switch (position) {
            case paths.FAV_FRAGMENTS:

                if (fav_list == null || fav_list.length == 0) {
                    BlankFragment fragment = new BlankFragment();
                    Bundle args = new Bundle();
                    args.putString("text", "No Favourites");
                    fragment.setArguments(args);
                    return fragment;

                } else {
                    myFragment = DashboardNewActivity.MyFragment.newInstance(position, fav_list);
                    break;
                }
            case paths.SETS_FRAGMENTS:
                myFragment = DashboardNewActivity.MyFragment.newInstance(position, sets_list);

                break;
            case paths.TOP_FRAGMENTS:
                if (top_list == null || top_list.length == 0) {
                    BlankFragment fragment = new BlankFragment();
                    Bundle args = new Bundle();
                    args.putString("text", "No Top Scores");
                    fragment.setArguments(args);
                    return fragment;
                }
                myFragment = DashboardNewActivity.MyFragment.newInstance(position, top_list);
                break;
        }
        return myFragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case paths.SETS_FRAGMENTS:
                return "Sets";
            case paths.TOP_FRAGMENTS:
                return "Top";
            case paths.FAV_FRAGMENTS:
                return "Fav";

        }
        return "";
    }

}

class YourRecyclerAdapter extends RecyclerView.Adapter<YourRecyclerAdapter.YourRecyclerViewHolder> {
    private ArrayList<String> list = new ArrayList<>();
    private LayoutInflater inflater;


    private AdapterCallback mAdapterCallback;

    public static interface AdapterCallback {
        void onMethodCallback();

        void onMethodCallback(int whichList, int position);
    }

    public YourRecyclerAdapter(Context context, int whichList, String[] listOfStrings) {
        try {
            this.mAdapterCallback = ((AdapterCallback) context);
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement AdapterCallback.");
        }

        inflater = LayoutInflater.from(context);
        for (String s : listOfStrings) {
            list.add(s);
        }
        this.whichList = whichList;
    }

    int whichList;

    @Override
    public YourRecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View root = inflater.inflate(R.layout.custom_row, viewGroup, false);
        YourRecyclerViewHolder holder = new YourRecyclerViewHolder(root);
        return holder;
    }

    @Override
    public void onBindViewHolder(YourRecyclerViewHolder yourRecyclerViewHolder, int i) {
        yourRecyclerViewHolder.textView.setText(list.get(i));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class YourRecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView textView;

        public YourRecyclerViewHolder(final View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.text_superhero);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        mAdapterCallback.onMethodCallback(whichList, getAdapterPosition());
                    } catch (ClassCastException exception) {
                        // do something
                    }
                }
            });
        }
    }
}
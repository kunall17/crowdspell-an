package com.lambda.crowdspell;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.igl.crowdword.R;
import com.lambda.crowdspell.HTTPRequest.ApiPaths;
import com.lambda.crowdspell.core.UserFunctions;
import com.lambda.crowdspell.fxns.Tag;
import com.lambda.crowdspell.fxns.User;
import com.lambda.crowdspell.fxns.WordSet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class DashboardNewActivity extends AppCompatActivity implements YourRecyclerAdapterForSets.AdapterCallback {
    private static final String WORDSETLIST_LIST_CURRENT = "wordlist_list_current";
    private static final String WORDSETLIST_LIST = "wordset_list";
    private static final String PARCEL_FAV_LIST = "fav_list";
    private static final String PARCEL_TOP_LIST = "top_list";
    // Need this to link with the Snackbar
    private CoordinatorLayout mCoordinator;
    //Need this to set the title of the app bar
    private FloatingActionButton mFab;
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ViewPager mPager;
    private YourPagerAdapter mAdapter;
    private TabLayout mTabLayout;

    List<WordSet> fav_list;
    String[] top_array;
    String[] fav_list_string, top;
    List<WordSet> wordset_list;
    List<WordSet> wordset_list_current;
    Boolean top_empty = false;
    String[] sets_list_string;
    private NavigationView navigationView;
    View root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_new);

        navigationView = (NavigationView) findViewById(R.id.navigation_drawer);

        if (savedInstanceState == null) {
            String sets = getIntent().getStringExtra("json_sets");
            String fav = getIntent().getStringExtra("json_fav");
            top = getIntent().getStringArrayExtra("json_top");
//        System.out.println(fav.length() + "-1");
//        System.out.println(fav + "-2");
//        System.out.println("fav-" + fav);
//        System.out.println("top-" + getIntent().getStringExtra("json_top"));
//            Gson gson = new Gson();
            Gson gson = getJsonWriterWithCustomDate();
            if (getResources().getString(R.string.SERVER_ADDRESS) != getResources().getString(R.string.SERVER_ADDRESS1)) {
            }
            wordset_list = Arrays.asList(gson.fromJson(sets, WordSet[].class));
            wordset_list_current = wordset_list;
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


        } else { // PARCEL AVAILABE
            wordset_list_current = savedInstanceState.getParcelableArrayList(WORDSETLIST_LIST_CURRENT);
            wordset_list = savedInstanceState.getParcelableArrayList(WORDSETLIST_LIST);

            if (savedInstanceState.getParcelableArrayList(PARCEL_FAV_LIST) != null) {
                fav_list = savedInstanceState.getParcelableArrayList(PARCEL_FAV_LIST);
                for (int i = 0; i < fav_list.size(); i++) {
                    fav_list_string[i] = fav_list.get(i).getName();
                }
            } else {
                fav_list_string = null;
            }
            if (savedInstanceState.getStringArray(PARCEL_TOP_LIST) != null) {
                top = savedInstanceState.getStringArray(PARCEL_TOP_LIST);

            } else top = null;
        }

        mCoordinator = (CoordinatorLayout) findViewById(R.id.root_coordinator);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle("Home");

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        sets_list_string = new String[wordset_list.size()];

        for (int i = 0; i < wordset_list.size(); i++) {
            sets_list_string[i] = wordset_list.get(i).getName();
        }

        mAdapter = new YourPagerAdapter(getSupportFragmentManager(), top, fav_list_string);

        mPager = (ViewPager) findViewById(R.id.view_pager);
        mPager.setAdapter(mAdapter);

        //Notice how the Tab Layout links with the Pager Adapter
        mTabLayout.setTabsFromPagerAdapter(mAdapter);

        //Notice how The Tab Layout adn View Pager object are linked
        mTabLayout.setupWithViewPager(mPager);
        mPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));


        //Notice how the title is set on the Collapsing Toolbar Layout instead of the Toolbar
        String username = new UserFunctions().getCurrentUser(DashboardNewActivity.this).getUsername();
        navigationView.getMenu().findItem(R.id.drawer_username).setTitle("Hi " + username);

        if (new UserFunctions().checkIfGuestModeIsOn(DashboardNewActivity.this)) {
            navigationView.getMenu().findItem(R.id.drawer_create).setVisible(false);

            mFab = (FloatingActionButton) findViewById(R.id.fab);
            mFab.setVisibility(View.GONE);
            Log.d("guestmode!", "123");
        } else { //User Mode
            navigationView.getMenu().findItem(R.id.drawer_create_user).setVisible(false);
            mFab = (FloatingActionButton) findViewById(R.id.fab);
            Log.d("guestmode!", "456");
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
        }

        navigationView.getMenu().findItem(R.id.drawer_suprise).setVisible(false);
        User user = new UserFunctions().getCurrentUser(this);
        Log.d("user-info", "token-" + user.getToken());
        Log.d("user-info", "salt-" + user.getSalt());
        Log.d("user-info", "ID-" + user.getId());
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
                                                                         if (!new UserFunctions().checkIfGuestModeIsOn(DashboardNewActivity.this)) {
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
                                                                                         //                                        star tGame(x);
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


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
//         Save the user's current game state
        super.onSaveInstanceState(savedInstanceState);
        try {
            ArrayList<WordSet> listOfStrings = new ArrayList<>(wordset_list.size());
            listOfStrings.addAll(wordset_list);
            savedInstanceState.putParcelableArrayList(WORDSETLIST_LIST_CURRENT, listOfStrings);
            listOfStrings.clear();
            listOfStrings.addAll(wordset_list_current);
            savedInstanceState.putParcelableArrayList(WORDSETLIST_LIST, listOfStrings);

            if (fav_list != null) {
                listOfStrings = new ArrayList<>(fav_list.size());
                listOfStrings.addAll(fav_list);
                savedInstanceState.putParcelableArrayList(PARCEL_FAV_LIST, listOfStrings);
            }

            if (top != null) savedInstanceState.putStringArray(PARCEL_TOP_LIST, top);
        } catch (Exception e) {
            Log.d("Exception AA GAYA", e.toString());
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DashboardNewActivity.this);
        builder.setTitle("Exit?");
        builder.setMessage("Do you want to exit?");
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                System.exit(0);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);

        final SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        SearchView searchView =(SearchView) menu.findItem(R.id.search).getActionView();


        final MenuItem mSearchMenuItem = menu.findItem(R.id.search);
        final SearchView searchView =
                (SearchView) MenuItemCompat.getActionView(mSearchMenuItem);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(
                new ComponentName(getApplicationContext(),
                        DashboardNewActivity.class)));


        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));


        searchView.setIconifiedByDefault(false);
        SearchView.OnQueryTextListener textChangeListener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                // this is your adapter that will be filtered
//                myAdapter.getFilter().filter(newText);
                return true;
            }


            @Override
            public boolean onQueryTextSubmit(String query) {
                // this is your adapter that will be filtered
//                myAdapter.getFilter().filter(query);
                Boolean internet = false;
                UserFunctions.checkInternetConnectionAsync checkInternet = new UserFunctions.checkInternetConnectionAsync();
                try {
                    internet = checkInternet.execute(getBaseContext()).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                Log.d("query", query);
                if (internet) {
                    searchWordSets sws = new searchWordSets(DashboardNewActivity.this);
                    sws.execute(query);
                } else {
                    Toast.makeText(DashboardNewActivity.this, "Internet Not Working!", Toast.LENGTH_SHORT).show();
                    List<WordSet> wordsets = new ArrayList<>();
                    for (WordSet set : wordset_list_current) {
                        Log.d("sets", set.getName());

                        if (set.getName().toLowerCase().contains(query.toLowerCase())) {
                            wordsets.add(set);
                            Log.d("contains", set.getName());
                        }
                    }
                    if (wordsets.size() != 0) {
                        wordset_list_current = wordsets;
                        resetList();

                    } else
                        Toast.makeText(DashboardNewActivity.this, "Nothing Found!", Toast.LENGTH_SHORT).show();
                }


                return true;
            }


        };

        ImageView closeButton = (ImageView) searchView.findViewById(R.id.search_close_btn);

        // Set on click listener
        closeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d("clicked", "");
                wordset_list_current = wordset_list;
                resetList();
                searchView.setQuery("", false);
                searchView.clearFocus();
                mSearchMenuItem.collapseActionView();
            }
        });
        final String TAG = "12";
        searchView.setOnQueryTextListener(textChangeListener);

        MenuItemCompat.setOnActionExpandListener(mSearchMenuItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // Write your code here
                wordset_list_current = wordset_list;
                resetList();
                Log.d(TAG, "123");
                return true;
            }
        });

        return true;
    }

    public void resetList() {

        mAdapter = new YourPagerAdapter(getSupportFragmentManager(), top, fav_list_string);
        mPager.setAdapter(mAdapter);

        //Notice how the Tab Layout links with the Pager Adapter
        mTabLayout.setTabsFromPagerAdapter(mAdapter);

        //Notice how The Tab Layout adn View Pager object are linked
        mTabLayout.setupWithViewPager(mPager);

    }

    public class searchWordSets extends AsyncTask<String, String, Integer> {
        ProgressDialog progress;
        Context context;

        public searchWordSets(Context context) {
            this.context = context;
            progress = new ProgressDialog(context);
        }

        @Override
        protected void onPreExecute() {
//            updateDisplay("Starting task");
            progress.setMessage("Searching WordSets :) ");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.show();
        }

        List<WordSet> wordset;

        @Override
        protected Integer doInBackground(String... params) {
            String output = "";
            try {

                URL url = new URL("http://46.101.37.183:8080/crowdspell-web/api/v1/search/" + params[0]);

                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty(ApiPaths.APP_AUTH_KEY,
                        ApiPaths.ANDROID_APP_KEY);
                con.connect();

                int code = con.getResponseCode();
                Log.d("Code", code + "");
                output = readFromConnection(con);

                Gson gson = getJsonWriterWithCustomDate();
                wordset = Arrays.asList(gson.fromJson(output, WordSet[].class));

                return code;
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return 0;
        }

        protected void onProgressUpdate(String... progUpdate) {
            progress.setMessage(progUpdate[0]);
        }

        protected void onPostExecute(Integer userPoints) {
            wordset_list_current = wordset;
            resetList();
            progress.dismiss();
        }

    }

    protected static Gson getJsonWriterWithCustomDate() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
        return gson;
    }

    protected static String readFromConnection(HttpURLConnection connection)
            throws IOException {
        InputStream iStream = connection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                iStream));
        String line;
        StringBuffer response = new StringBuffer();
        while ((line = reader.readLine()) != null) {
            response.append(line);
            response.append('\n');
        }
        reader.close();
        return response.toString();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_help) {
            AlertDialog.Builder builder = new AlertDialog.Builder(DashboardNewActivity.this);
            builder.setTitle("Help!");
            builder.setMessage(getString(R.string.help_string));
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {

                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
            return true;
        }

        if (id == R.id.action_reset) {
            wordset_list_current = wordset_list;
            resetList();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public List<WordSet> onMethodCallback() {
        Log.d("WORDSET SENT", "234");
        List<WordSet> asd = wordset_list_current;
        return asd;

    }

    @Override
    public void onMethodCallback(int adapterPosition) {
        startGame(adapterPosition);
    }

    private void startGame(int adapterPosition) {
        Intent in1 = new Intent(DashboardNewActivity.this, GameNewActivity.class);

        Gson gson = new Gson();
        String json_new = gson.toJson(wordset_list.get(adapterPosition));

        in1.putExtra("json_new", json_new);
        //in1.putExtra ("wordName", game_word[position].toUpperCase());
        startActivity(in1);
    }


    public static class MyFragment extends Fragment {
        public static final java.lang.String ARG_PAGE = "arg_page";
        public static final java.lang.String ARG_LIST = "arg_list";

        public MyFragment() {

        }

        public static MyFragment newInstance(int pageNumber, String[] list) {
            MyFragment myFragment = new MyFragment();
            Bundle arguments = new Bundle();
            arguments.putInt(ARG_PAGE, pageNumber);
            if (pageNumber != paths.SETS_FRAGMENTS) {
                arguments.putStringArray(ARG_LIST, list);
            }
            myFragment.setArguments(arguments);
            return myFragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            Bundle arguments = getArguments();
            int pageNumber = arguments.getInt(ARG_PAGE);
            String[] list = arguments.getStringArray(ARG_LIST);

            RecyclerView recyclerView = new RecyclerView(getActivity());
            if (pageNumber == paths.SETS_FRAGMENTS) {
                recyclerView.setAdapter(new YourRecyclerAdapterForSets(getActivity()));
            } else {
                Log.d("pageNumber", pageNumber + "");
                recyclerView.setAdapter(new YourRecyclerAdapter(getActivity(), list));
            }
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

    String[] top_list, fav_list;


    public YourPagerAdapter(FragmentManager supportFragmentManager, String[] top, String[] fav_list_string) {
        super(supportFragmentManager);
        this.fav_list = top;
        this.top_list = fav_list_string;

    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        Log.d("getItem", position + "");
        switch (position) {
            case paths.FAV_FRAGMENTS:

                if (fav_list == null || fav_list.length == 0) {
                    fragment = new BlankFragment();
                    Bundle args = new Bundle();
                    args.putString("text", "No Favourites");
                    fragment.setArguments(args);

                } else {
                    fragment = DashboardNewActivity.MyFragment.newInstance(position, fav_list);
                }
                break;

            case paths.SETS_FRAGMENTS:
                fragment = DashboardNewActivity.MyFragment.newInstance(position, null);
                break;
            case paths.TOP_FRAGMENTS:
                if (top_list == null || top_list.length == 0) {
                    fragment = new BlankFragment();
                    Bundle args = new Bundle();
                    args.putString("text", "No Favourites");
                    Log.d("top-fragments", "tops");
                    fragment.setArguments(args);
                } else {
                    Log.d("top-fragments", "tops not empty");
                    fragment = DashboardNewActivity.MyFragment.newInstance(position, top_list);
                }
                break;
        }
        Log.d("position", position + "");
        return fragment;
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
                return "FAV";
            case paths.FAV_FRAGMENTS:
                return "TOP";
//TODO CHECK WHATS WRONG WITH LISTS
        }
        return "";
    }

}


class YourRecyclerAdapterForSets extends RecyclerView.Adapter<YourRecyclerAdapterForSets.YourRecyclerViewHolder> {
    private List<WordSet> wordset_list = new ArrayList<>();
    private ArrayList<String> tags_list = new ArrayList<>();
    private LayoutInflater inflater;


    private AdapterCallback mAdapterCallback;

    public static interface AdapterCallback {
        List<WordSet> onMethodCallback();

        void onMethodCallback(int adapterPosition);


    }

    public YourRecyclerAdapterForSets(Context context) {
        try {
            this.mAdapterCallback = ((AdapterCallback) context);
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement AdapterCallback.");
        }

        inflater = LayoutInflater.from(context);
        this.wordset_list = mAdapterCallback.onMethodCallback();
        for (int i = 0; i < wordset_list.size(); i++) {
            String s = "Tags: ";
            for (Tag tag : wordset_list.get(i).getTags()) {
                s = s + tag.getName() + ",";
            }
            tags_list.add(s.substring(0, s.length() - 1));
        }
    }

    @Override
    public YourRecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View root = inflater.inflate(R.layout.custom_row2, viewGroup, false);
        YourRecyclerViewHolder holder = new YourRecyclerViewHolder(root);
        return holder;
    }


    @Override
    public void onBindViewHolder(YourRecyclerViewHolder yourRecyclerViewHolder, int i) {
//        Log.d("viewHolder", wordset_list.get(i).getName() + "  :  " + tags_list.get(i));
        yourRecyclerViewHolder.textView.setText(wordset_list.get(i).getName());
        yourRecyclerViewHolder.textView2.setText(tags_list.get(i));

    }

    @Override
    public int getItemCount() {
        return wordset_list.size();
    }

    class YourRecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        TextView textView2;

        public YourRecyclerViewHolder(final View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.itemTextView);
            textView2 = (TextView) itemView.findViewById(R.id.itemTextView2);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        mAdapterCallback.onMethodCallback(getAdapterPosition());
                    } catch (ClassCastException exception) {
                        // do something
                    }
                }
            });
        }
    }
}


class YourRecyclerAdapter extends RecyclerView.Adapter<YourRecyclerAdapter.YourRecyclerViewHolder> {
    private ArrayList<String> list = new ArrayList<>();
    private LayoutInflater inflater;


    public YourRecyclerAdapter(Context context, String[] listOfStrings) {
        inflater = LayoutInflater.from(context);
        for (String s : listOfStrings) {
            list.add(s);
        }
    }


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
            textView = (TextView) itemView.findViewById(R.id.itemTextView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                    } catch (ClassCastException exception) {
                        // do something
                    }
                }
            });
        }
    }


}


package com.lambda.crowdspell;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lambda.crowdspell.HTTPRequest.UserManager;
import com.igl.crowdword.R;
import com.lambda.crowdspell.core.UserFunctions;
import com.lambda.crowdspell.fxns.WordSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FavouritesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FavouritesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavouritesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ArrayList<String> set_lists;
    ArrayAdapter<String> sets_adapter;
    List<WordSet> sets_fav_list = null;

    private OnFragmentInteractionListener mListener;
    View rootView;
    ListView listview;
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment FavouritesFragment.
//     */
    // TODO: Rename and change types and number of parameters
    public void noListFound(String text) {
        LinearLayout ll = (LinearLayout) rootView.findViewById(R.id.favourties_linear);
        ll.removeAllViews();
        TextView et = new TextView(getActivity());
        et.setText(text);
        et.setTextSize(30);
        et.setPadding(10, 0, 10, 0);
        ll.setGravity(Gravity.CENTER);
        ll.addView(et);
    }


    public void setUpList() {
        UserFunctions uf = new UserFunctions();
        String token = uf.getCurrentToken(getActivity());

        UserManager.getUserFavouritesAsync asd = new UserManager.getUserFavouritesAsync();

        try {
            if (getResources().getString(R.string.SERVER_ADDRESS) == getResources().getString(R.string.SERVER_ADDRESS1)) { //Remove this
                UserFunctions.checkInternetConnectionAsync checkInternet = new UserFunctions.checkInternetConnectionAsync();
                Boolean internet = checkInternet.execute(getActivity()).get();
                if (internet == false) {
                    Toast.makeText(getActivity(), "Please check your Internet Connection", Toast.LENGTH_LONG).show();
                    return;
                }
                sets_fav_list = asd.execute(token).get();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if (sets_fav_list == null) {
            noListFound("Sorry no Favourites found!");
        } else {
            setListDisplay();
        }
    }

    public void setListDisplay() {
        for (WordSet wordset : sets_fav_list) {
            set_lists.add(wordset.getName());
        }
        sets_adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, set_lists);
        listview = (ListView) rootView.findViewById(R.id.game_list_fav);
        listview.setAdapter(sets_adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    public FavouritesFragment newInstance(String param1, String param2) {
        FavouritesFragment fragment = new FavouritesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    String list_json;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Gson gson = new Gson();
        list_json = gson.toJson(sets_fav_list);
        outState.putString("list_gson", list_json);
    }

    public FavouritesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_favourites2, container, false);

        if (savedInstanceState == null) {
            setUpList();

        } else {
            Gson gson = new Gson();
            String json = savedInstanceState.getString("list_gson");
            sets_fav_list = Arrays.asList(gson.fromJson(json, WordSet[].class));

            setListDisplay();
        }

//        srl = (SwipeRefreshLayout) rootView.findViewById(R.id.refreshSwipe_list);
//        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                filterWithTags();
//            }
//        });

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}

package com.igl.crowdword;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.igl.crowdword.HTTPRequest.GameManager;
import com.igl.crowdword.HTTPRequest.UserManager;
import com.igl.crowdword.core.UserFunctions;
import com.igl.crowdword.fxns.WordSet;
import com.igl.crowdword.fxns.analysis.UserPoints;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListFragment extends android.support.v4.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    SwipeRefreshLayout srl;

    private OnFragmentInteractionListener mListener;
    View rootView;
    ListView listview;

    int BUNDLE;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListFragment newInstance(String param1, String param2) {
        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        Bundle args = getArguments();
        BUNDLE = args.getInt(DashboardActivity.ARG_BUNDLE);

    }

    public void noListFound(String text){
        LinearLayout ll = (LinearLayout) rootView.findViewById(R.id.linearLayout_lists);
        ll.removeAllViews();
        TextView et = new TextView(getActivity());
        et.setText(text);
        et.setTextSize(30);
        et.setPadding(10, 0, 10, 0);
        ll.setGravity(Gravity.CENTER);
        ll.addView(et);
    }
    public void setUpList(int bundle) {
        if (bundle == 1) { //LEADERBOARD
            List<UserPoints> userPoints = null;

            try {
                userPoints = GameManager.getAllTopScorers();

            } catch (IOException e) {
                e.printStackTrace();
            }
            if(userPoints== null){
                noListFound("Sorry No Top Scorers found!");

            } else {
                for (UserPoints up : userPoints) {
                    set_lists.add(up.getUser().getUsername() + " : " + up.getPoints());
                }
                sets_adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, set_lists);
                listview = (ListView) rootView.findViewById(R.id.listView_frag);
                listview.setAdapter(sets_adapter);
                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    }
                });
            }
        } else { //FAVOURITES
            List<WordSet> wordsets = null;
            UserFunctions uf = new UserFunctions();

            wordsets = UserManager.getUserFavourites(uf.getCurrentToken(getActivity()));

            if(wordsets== null){
            noListFound("Sorry no Favourites found!");

            } else {

            for (WordSet wordset : wordsets) {
                set_lists.add(wordset.getName());
            }
            sets_adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, set_lists);
            listview = (ListView) rootView.findViewById(R.id.listView_frag);
            listview.setAdapter(sets_adapter);
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                }
            });
        }
    }
    }

    public void filterWithTags() {

    }

    ArrayList<String> set_lists;
    ArrayAdapter<String> sets_adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_list, container, false);

        srl = (SwipeRefreshLayout) rootView.findViewById(R.id.refreshSwipe_list);
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                filterWithTags();
            }
        });
        setUpList(BUNDLE);

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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}

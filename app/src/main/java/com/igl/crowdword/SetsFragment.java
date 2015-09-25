package com.igl.crowdword;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.igl.crowdword.HTTPRequest.GameManager;
import com.igl.crowdword.core.UserFunctions;
import com.igl.crowdword.fxns.Tag;
import com.igl.crowdword.fxns.WordSet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SetsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SetsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SetsFragment extends android.support.v4.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    SwipeRefreshLayout srl;

    WordSet wordset = null;
    List<WordSet> Wordset = null;
    List<WordSet> wordset_list = null;
    List<WordSet> backup_wordset_list = null;
    ArrayAdapter<String> spn_adapter;
    ArrayAdapter<String> mainListViewAdapter;

    AutoCompleteTextView actv;

    ListView listview_games;
    Map tags_map = new HashMap();
    List<String> allTags;

    String getJson() {
        InputStream is = getResources().openRawResource(R.raw.getwords);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return writer.toString();

    }

    public List<String> extractTags() {

        List<String> tags_f = new ArrayList<String>();
        for (int i = 0; i < wordset_list.size(); i++) {
            for (Tag tag : wordset_list.get(i).getTags()) {
                if (tags_f.contains(tag.getName().toString()) == false) {
                    tags_f.add(tag.getName().toString());

                    if (tags_map.containsKey(tag.getName())) {
                        tags_map.put(tag.getName().toString(), tags_map.get(tag.getName()).toString() + "," + i);

                    } else {
                        tags_map.put(tag.getName().toString(), i);

                    }
                }
            }
        }
        return tags_f;
    }


    public void filterWithTags() {

        if (spn_adapter.getCount() >= 1) {
            System.out.println("We Are Here:");
            List<WordSet> wordsets_new = new ArrayList<WordSet>();

            for (int i = 0; i < spn_adapter.getCount(); i++) {
                String tag = spn_adapter.getItem(i).toString();
                if (tags_map.containsKey(tag) == true) {
                    String key = tags_map.get(tag).toString();
                    String asd[] = key.split(",");
                    for (String s : asd) {
                        if (wordsets_new.contains(wordset_list.get(Integer.parseInt(s))) == false)
                            wordsets_new.add(wordset_list.get(Integer.parseInt(s)));
                    }
                }
            }
            if (wordsets_new.size() == 0) {
                Toast.makeText(getActivity(), "Sorry Nothing Found.!", Toast.LENGTH_LONG).show();
            } else {

                mainListViewAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, getNameArray(wordsets_new));
                listview_games.setAdapter(mainListViewAdapter);
            }

        }

        if (srl.isRefreshing()) srl.setRefreshing(false);
    }

    List<WordSet> wordsets_new;

    public void startGame(int position) {
        Intent in1 = new Intent(getActivity(), GameNewActivity.class);

        Gson gson = new Gson();
        String json_new = gson.toJson(wordsets_new.get(position)).toString();

        in1.putExtra("json_new", json_new);
        in1.putExtra("wordTitle", "TITLE");

        WordSet asd = new WordSet();
        String json_wordset = null;
        ObjectMapper mapper = new ObjectMapper();

        try {
            json_wordset = mapper.writeValueAsString(asd);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        in1.putExtra("json_wordset", json_wordset);
        //in1.putExtra ("wordName", game_word[position].toUpperCase());
        startActivity(in1);
    }


    public List<String> getNameArray(List<WordSet> wordsets_list) {
        List<String> nameArray = new ArrayList<String>();

        for (WordSet wordset : wordsets_list) {
            nameArray.add(wordset.getName());
        }
        return nameArray;
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SetsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SetsFragment newInstance(String param1, String param2) {
        SetsFragment fragment = new SetsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public SetsFragment() {
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

    public void surpirse() {

    }

    View rootView;

    public void initializeList() {

        String json1 = "";
        wordset_list = new ArrayList<WordSet>();
        String[] nameArray = null;

        if (UserFunctions.checkForServer(getActivity()) == true) {
            if (new UserFunctions().checkInternetConnection(getActivity()) == false) {
                Toast.makeText(getActivity(), "Please check your Internet Connection", Toast.LENGTH_LONG).show();
                return;
            }

            GameManager.getAllSetsAsync asd = new GameManager.getAllSetsAsync();
            try {
                wordset_list = asd.execute(getActivity()).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            nameArray = new String[wordset_list.size()];
            for (int i =0 ; i<wordset_list.size() ; i++){
                nameArray[i] = wordset_list.get(i).getName();
            }
            //nameArray = getNameArray(wordset_list).toArray(new String[wordset_list.size()]);
        } else {
            json1 = getJson();
            JsonElement json = new JsonParser().parse(json1);
            JsonArray array = json.getAsJsonArray();
            Iterator iterator = array.iterator();
            int i = 0;
            nameArray = new String[array.size()];

            while (iterator.hasNext()) {
                JsonElement json2 = (JsonElement) iterator.next();
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
                WordSet wordset = gson.fromJson(json2, WordSet.class);
                nameArray[i] = wordset.getName();
                i++;
                wordset_list.add(wordset);
            }
        }

        wordsets_new = wordset_list;
        mainListViewAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, nameArray);
        //ArrayAdapter<String> adap = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,game_title);
        listview_games.setAdapter(mainListViewAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_sets, container, false);


        srl = (SwipeRefreshLayout) rootView.findViewById(R.id.refreshSwipe_list);
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                filterWithTags();
                Toast.makeText(getActivity(), "ASD", Toast.LENGTH_LONG).show();
            }
        });

        actv = (AutoCompleteTextView) rootView.findViewById(R.id.tags_actv);


        listview_games = (ListView) rootView.findViewById(R.id.game_list);

        initializeList();
        listview_games.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Implement code for navigation to play game
                startGame(position);
            }
        });


//Added Newwly

        allTags = extractTags();
        final ArrayAdapter<String> adapter_actv = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, allTags);

        //Getting the instance of AutoCompleteTextView
        actv.setThreshold(3);//will start working from first character
        actv.setAdapter(adapter_actv);//setting the adapter data into the AutoCompleteTextView


        String[] tags_array = new String[100];
        tags_array[0] = "";


        final ArrayList<String> spn_array = new ArrayList<String>();
        spn_adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, spn_array) {

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = null;
                if (position == 0) {
                    TextView tv = new TextView(getContext());
                    tv.setVisibility(View.GONE);
                    v = tv;
                } else {
                    v = super.getDropDownView(position, null, parent);
                }
                return v;
            }
        };
        Spinner spinner = (Spinner) rootView.findViewById(R.id.tags_spnr);

        spn_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spn_adapter);


        actv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spn_adapter.add(adapter_actv.getItem(position));
                Toast.makeText(getActivity(), "Tag Added", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Toast.makeText(getActivity(), "SUPER", Toast.LENGTH_LONG).show();
//                spn_adapter.remove( spn_adapter.getItem(position) );
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });

        spn_adapter.add("Add Tags Here");


        Button addTag = (Button) rootView.findViewById(R.id.addTag_btn);
        addTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spn_adapter.add(actv.getText().toString());
                Toast.makeText(getActivity(), "Tag Added", Toast.LENGTH_LONG).show();
                actv.setText("");
            }
        });
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

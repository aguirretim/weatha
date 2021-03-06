package com.timapps.weatha;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InitialLocationAuthorization#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InitialLocationAuthorization extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView welcomeHeaderText;
    private TextView locationInfoText;
    private TextView PrivacyPolicyText;
    private ImageView imageView;
    private Button enableLocationButton;
    private Button disableLocationButton;
    MainActivity activity;


    public InitialLocationAuthorization() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InitialLocationAuthorization.
     */
    // TODO: Rename and change types and number of parameters
    public static InitialLocationAuthorization newInstance(String param1, String param2) {
        InitialLocationAuthorization fragment = new InitialLocationAuthorization();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        View view = inflater.inflate(R.layout.fragment_initial_location_authorization, container, false);



        welcomeHeaderText = (TextView) view.findViewById(R.id.welcomeHeaderText);
        locationInfoText = (TextView) view.findViewById(R.id.locationInfoText);
        PrivacyPolicyText = (TextView) view.findViewById(R.id.PrivacyPolicyText);
        imageView = (ImageView) view.findViewById(R.id.imageView);
        enableLocationButton = (Button) view.findViewById(R.id.enableLocationButton);
        disableLocationButton = (Button) view.findViewById(R.id.disableLocationButton);

        activity = (MainActivity) getActivity();

        enableLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.checkWeatherPerm();

            }
        });

        disableLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.createAddLocationFragment();

            }
        });



        return view;
    }


}


package com.example.fashion_store;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Objects;


public class ProfilePageFragment extends Fragment {

    String[] userData = new String[4];
    TextView userName;
    TextView userEmail;
    Button trackOrderBT;
    Button orderHistoryBT;
    Button signOutBT;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TransitionInflater inflater = TransitionInflater.from(requireContext());
        setExitTransition(inflater.inflateTransition(R.transition.slide_right));
        setEnterTransition(inflater.inflateTransition(R.transition.slide_right));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_page, container, false);
        //get user data from login page
        Intent userDataIntent = requireActivity().getIntent();
        userData = userDataIntent.getStringArrayExtra("user_data");
        //vars
        userName = view.findViewById(R.id.profile_page_user_name);
        userEmail = view.findViewById(R.id.profile_page_user_email);
        trackOrderBT = view.findViewById(R.id.profile_page_track_bt);
        orderHistoryBT = view.findViewById(R.id.profile_page_history_bt);
        signOutBT = view.findViewById(R.id.profile_page_sign_out_bt);
        //set profile name
        String name = userName.getText().toString().replace("{USER}", userData[2].concat("!"));
        userName.setText(name);
        //set profile email
        String email = userEmail.getText().toString().replace("{EMAIL}", userData[1]);
        userEmail.setText(email);
        //track
        trackOrderBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO
            }
        });
        //history
        orderHistoryBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO
            }
        });
        //sign out
        signOutBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO
            }
        });

        return view;
    }
}
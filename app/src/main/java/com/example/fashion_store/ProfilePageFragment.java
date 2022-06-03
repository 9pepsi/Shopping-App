package com.example.fashion_store;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.sql.SQLException;
import java.util.Objects;


public class ProfilePageFragment extends Fragment {

    TextView userName;
    TextView userEmail;
    Button trackOrderBT;
    Button orderHistoryBT;
    Button setAddress;
    Button signOutBT;

    ProgressDialog progressDialog;

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
        //vars
        userName = view.findViewById(R.id.profile_page_user_name);
        userEmail = view.findViewById(R.id.profile_page_user_email);
        trackOrderBT = view.findViewById(R.id.profile_page_track_bt);
        orderHistoryBT = view.findViewById(R.id.profile_page_history_bt);
        setAddress = view.findViewById(R.id.set_address_bt);
        signOutBT = view.findViewById(R.id.profile_page_sign_out_bt);
        //get user info
        class getUserInfoTask extends AsyncTask<Void, Void, String[]>{
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(view.getContext(), "", "Getting User Info...");
            }

            @Override
            protected String[] doInBackground(Void... voids) {
                String[] userData = new String[2];
                try {
                    userData[0] = DBHelper.getInstance().getUserEmail();
                    userData[1] = DBHelper.getInstance().getUserName();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return userData;
            }

            @Override
            protected void onPostExecute(String[] userData) {
                if(userData[0] != null){
                //set profile name
                String name = Objects.requireNonNull(userName).getText().toString().replace("{USER}", userData[1].concat("!"));
                userName.setText(name);
                //set profile email
                String email = Objects.requireNonNull(userEmail).getText().toString().replace("{EMAIL}", userData[0]);
                userEmail.setText(email);
                }else {
                    String empty = Objects.requireNonNull(userName).getText().toString().replace("Hi, {USER}", "");
                    userName.setText(empty);
                    userEmail.setText("");
                    Toast.makeText(getContext(), "Failed to fetch user details", Toast.LENGTH_LONG).show();
                }

                progressDialog.dismiss();
            }
        }
        new getUserInfoTask().execute();
        //track
        trackOrderBT.setOnClickListener(view1 -> startActivity(new Intent(view1.getContext(), TrackOrderActivity.class)));
        //history
        orderHistoryBT.setOnClickListener(view12 -> startActivity(new Intent(view12.getContext(), OrderHistoryActivity.class)));
        //set Address
        setAddress.setOnClickListener(view13 -> startActivity(new Intent(view13.getContext(), UpdateUserAddressActivity.class)));
        //sign out
        signOutBT.setOnClickListener(view14 -> new StoreDialogs().signOutDialog(view14.getContext(), "Are you sure you want to sign out?").show());

        return view;
    }
}
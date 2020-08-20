package com.logicrack.MaityPoultry.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.logicrack.MaityPoultry.R;
import com.logicrack.MaityPoultry.activity.LoginRegisterActivity;
import com.logicrack.MaityPoultry.activity.MainActivity;
import com.logicrack.MaityPoultry.model.User;
import com.logicrack.MaityPoultry.util.localstorage.LocalStorage;
import com.google.gson.Gson;

public class ProfileFragment extends Fragment {

TextView Email,FullName,Mobile;
    User user;
    LocalStorage localStorage;
    String userJson;
    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile,
               container, false);
        Email = view.findViewById(R.id.Email);

        ((MainActivity)getActivity()).activeFragment ="Profile";

        FullName = view.findViewById(R.id.FullName);

        Mobile = view.findViewById(R.id.Mobile);

        localStorage = new LocalStorage(getActivity());
        String userString = localStorage.getUserLogin();
        Gson gson = new Gson();
        userString = localStorage.getUserLogin();
        user = gson.fromJson(userString, User.class);

        if (user != null) {
            if(user.getName().length()!= 0 )
            {
                Email.setText(user.getEmail());
                FullName.setText(user.getName());
                Mobile.setText(user.getMobile());
            }
            else {
                Intent intent = new Intent(getContext(), LoginRegisterActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("PageType","PreCheckout");
                startActivity(intent);
                getActivity().finish();
            }

        }
        else{
            Intent intent = new Intent(getContext(), LoginRegisterActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("PageType","PreCheckout");
            startActivity(intent);
            getActivity().finish();
        }

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Profile");
    }
}

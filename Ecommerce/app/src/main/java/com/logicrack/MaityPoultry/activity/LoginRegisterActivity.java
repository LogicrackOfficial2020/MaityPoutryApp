package com.logicrack.MaityPoultry.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.logicrack.MaityPoultry.R;

import com.logicrack.MaityPoultry.fragment.Login_Fragment;
import com.logicrack.MaityPoultry.util.Utils;


public class LoginRegisterActivity extends AppCompatActivity {
    private static FragmentManager fragmentManager;
    public String PageType;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);
        Intent intent = getIntent();
        PageType=intent.getStringExtra("PageType");
        getSupportActionBar().hide();
        fragmentManager = getSupportFragmentManager();

        // If savedinstnacestate is null then replace login fragment
        if (savedInstanceState == null) {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.frameContainer, new Login_Fragment(),
                            Utils.Login_Fragment).commit();
        }

        // On close icon click finish activity
        findViewById(R.id.close_activity).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        finish();

                    }
                });

    }

    // Replace Login Fragment with animation
    public void replaceLoginFragment() {
        fragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.left_enter, R.anim.right_out)
                .replace(R.id.frameContainer, new Login_Fragment(),
                        Utils.Login_Fragment).commit();
    }

    @Override
    public void onBackPressed() {

        // Find the tag of signup and forgot password fragment
        Fragment SignUp_Fragment = fragmentManager
                .findFragmentByTag(Utils.SignUp_Fragment);
        Fragment ForgotPassword_Fragment = fragmentManager
                .findFragmentByTag(Utils.ForgotPassword_Fragment);

        // Check if both are null or not
        // If both are not null then replace login fragment else do backpressed
        // task

        if (SignUp_Fragment != null)
            replaceLoginFragment();
        else if (ForgotPassword_Fragment != null)
            replaceLoginFragment();
        else
            super.onBackPressed();
    }
}

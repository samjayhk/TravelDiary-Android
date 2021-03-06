package com.samjayspot.traveldiary;

/**
 * PLEASE BE MIND THAT YOU COULD NOT COPY, MODIFY OR SHARE THIS PROJECT IF YOU ARE NOT GET PERMISSION.
 * @samjayhk
 */

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import androidx.annotation.RequiresApi;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;
    Menu menu;
    SharedPreferences sharedPreferences;
    RelativeLayout rootView;

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        overridePendingTransition(R.anim.inlefttoright, R.anim.nulltranslation);

        rootView = (RelativeLayout) findViewById(R.id.rootView);
        bottomNavigationView = findViewById(R.id.bnvControl);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        bottomNavigationView.setItemIconTintList(null);
        menu = bottomNavigationView.getMenu();
        menu.findItem(R.id.action_home).setIcon(R.drawable.main);
        menu.findItem(R.id.action_write).setIcon(R.drawable.write);
        menu.findItem(R.id.action_user).setIcon(R.drawable.user_non);

        bottomNavigationView.setItemIconSize(120);

        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.INTERNET,
                Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.WAKE_LOCK,
                Manifest.permission.WAKE_LOCK, android.Manifest.permission.BIND_NOTIFICATION_LISTENER_SERVICE,
                Manifest.permission.ACCESS_NOTIFICATION_POLICY,
                Manifest.permission.WRITE_SETTINGS, Manifest.permission.CHANGE_CONFIGURATION,
                Manifest.permission.MODIFY_AUDIO_SETTINGS}, 10);

        changeFragment(0);
    }

    public ArrayList getSession() {
        sharedPreferences = getSharedPreferences("traveldiary", Context.MODE_PRIVATE);
        ArrayList session = new ArrayList();

        if (!sharedPreferences.getString("token", "").equals("")) {
            session.add(0, sharedPreferences.getString("token", ""));
            session.add(1, sharedPreferences.getString("username", ""));
            session.add(2, sharedPreferences.getString("reg", ""));
            session.add(3, sharedPreferences.getString("last", ""));
            session.add(4, sharedPreferences.getString("email", ""));
            Log.d("response", String.valueOf(session));
            return session;
        } else {
            Log.d("response", "No Session Record.");
            return null;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_home:
                menuItem.setIcon(R.drawable.main);
                menu.findItem(R.id.action_user).setIcon(R.drawable.user_non);
                changeFragment(0);
                break;
            case R.id.action_write:
                changeFragment(1);
                break;
            case R.id.action_user:
                menu.findItem(R.id.action_home).setIcon(R.drawable.main_non);
                menuItem.setIcon(R.drawable.user);
                changeFragment(2);
                break;
            default:
                break;
        }
        return false;
    }

    public void changeFragment(int id) {

        Fragment fragment = null;

        switch (id) {
            case 0:
                fragment = new HomeFragment();
                getSupportFragmentManager().beginTransaction().replace(
                        R.id.fragmentContainer, fragment)
                        .commit();
                break;
            case 1:
                if (getSession() != null) {
                    Intent userIntent = new Intent(MainActivity.this, WriteActivity.class);
                    startActivity(userIntent);
                } else {
                    Snackbar snackbar = Snackbar.make(rootView, "Please login first!", Snackbar.LENGTH_SHORT);
                    View rootSnackbar = snackbar.getView();
                    rootSnackbar.setBackgroundColor(Color.RED);
                    snackbar.show();
                }
                break;
            case 2:
                if (getSession() != null) {
                    fragment = new UserFragment();
                    getSupportFragmentManager().beginTransaction().replace(
                            R.id.fragmentContainer, fragment)
                            .commit();
                } else {
                    Intent userIntent = new Intent(MainActivity.this, WellcomeActivity.class);
                    startActivity(userIntent);
                }
                break;
            default:
                break;
        }


    }
}

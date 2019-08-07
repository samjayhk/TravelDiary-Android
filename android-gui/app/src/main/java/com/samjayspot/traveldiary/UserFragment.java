package com.samjayspot.traveldiary;

/**
 * PLEASE BE MIND THAT YOU COULD NOT COPY, MODIFY OR SHARE THIS PROJECT IF YOU ARE NOT GET PERMISSION.
 * @samjayhk
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class UserFragment extends Fragment implements View.OnClickListener {

    View rootView;

    TextView txtUserUsername, txtUserEmail, txtRegisterDate,
            txtLastLoginDate;
    Button btnChangePassword, btnLogout;

    SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(
                R.layout.fragment_user,
                container,
                false);

        ArrayList<String> info = getSession();

        txtUserUsername = (TextView) rootView.findViewById(R.id.txtUserUsername);
        txtUserEmail = (TextView) rootView.findViewById(R.id.txtUserEmail);
        txtRegisterDate = (TextView) rootView.findViewById(R.id.txtRegisterDate);
        txtLastLoginDate = (TextView) rootView.findViewById(R.id.txtLastLoginDate);

        btnChangePassword = (Button) rootView.findViewById(R.id.btnChangePassword);
        btnLogout = (Button) rootView.findViewById(R.id.btnLogout);

        btnChangePassword.setOnClickListener(this);
        btnLogout.setOnClickListener(this);

        txtUserUsername.setText(info.get(1));
        txtUserEmail.setText(info.get(4));
        txtRegisterDate.setText(timestampToDateTime(info.get(2)));
        txtLastLoginDate.setText(timestampToDateTime(info.get(3)));

        return rootView;

    }

    public String timestampToDateTime(String timestamp) {

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:MM:SS");
        String dateString = formatter.format(new Date(Long.parseLong(timestamp)));

        return dateString;
    }

    public void clearSession() {
        sharedPreferences = getActivity().getSharedPreferences("traveldiary", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }


    public ArrayList getSession() {
        sharedPreferences = getActivity().getSharedPreferences("traveldiary", Context.MODE_PRIVATE);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnChangePassword:
                Intent intent = new Intent(getActivity(), UpdatePasswordActivity.class);
                getActivity().startActivity(intent);
                break;
            case R.id.btnLogout:
                clearSession();
                Snackbar snackbar = Snackbar.make(rootView, "Good Bye!", Snackbar.LENGTH_SHORT);
                View rootSnackbar = snackbar.getView();
                rootSnackbar.setBackgroundColor(rootSnackbar.getResources().getColor(R.color.main_blue));
                snackbar.show();
                break;
            default:
                break;
        }
    }
}

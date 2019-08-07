package com.samjayspot.traveldiary;

/**
 * PLEASE BE MIND THAT YOU COULD NOT COPY, MODIFY OR SHARE THIS PROJECT IF YOU ARE NOT GET PERMISSION.
 * @samjayhk
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UpdatePasswordActivity extends SwipeBackActivity implements View.OnClickListener {

    ImageView btnBack;
    EditText edtRegisterPassword, edtEdtRegisterConfirmPassword;
    Button btnUpdate;

    RelativeLayout relativeLayoutRegister;

    private void putRequest(String url, String oldPassword, String newPassword) {
        MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();

        JSONObject postObject = new JSONObject();
        try {
            postObject.put("oldPassword", oldPassword);
            postObject.put("newPassword", newPassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        RequestBody body = RequestBody.create(JSON, postObject.toString());
        Request request = new Request.Builder()
                .addHeader("x-token", getSession().get(0).toString())
                .url(url)
                .put(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("bugs", e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject json = new JSONObject(response.body().string());

                    if (Boolean.parseBoolean(json.getString("result"))) {
                        clearSession();
                        Intent intent = new Intent(UpdatePasswordActivity.this, LoginActivity.class);
                        startActivity(intent);
                    } else {
                        Snackbar snackbar = Snackbar.make(relativeLayoutRegister, json.getString("message"), Snackbar.LENGTH_SHORT);
                        View rootSnackbar = snackbar.getView();
                        rootSnackbar.setBackgroundColor(Color.RED);
                        snackbar.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public ArrayList getSession() {
        SharedPreferences sharedPreferences = getSharedPreferences("traveldiary", Context.MODE_PRIVATE);
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

    public void clearSession() {
        SharedPreferences sharedPreferences = getSharedPreferences("traveldiary", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updatepassword);
        overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom);

        relativeLayoutRegister = (RelativeLayout) findViewById(R.id.relativeLayoutRegister);

        btnBack = (ImageView) findViewById(R.id.btnBack);
        edtRegisterPassword = (EditText) findViewById(R.id.edtRegisterPassword);
        edtEdtRegisterConfirmPassword = (EditText) findViewById(R.id.edtRegisterConfirmPassword);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);

        btnBack.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                onBackPressed();
                overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_top);
                break;
            case R.id.btnUpdate:
                if (!edtRegisterPassword.getText().toString().equals("") && !edtEdtRegisterConfirmPassword.getText().toString().equals("")) {
                    putRequest(APIsManagement.getUpdatePassword(), edtRegisterPassword.getText().toString(), edtEdtRegisterConfirmPassword.getText().toString());
                } else {
                    Snackbar snackbar = Snackbar.make(relativeLayoutRegister, "Please fill in password!", Snackbar.LENGTH_SHORT);
                    View rootSnackbar = snackbar.getView();
                    rootSnackbar.setBackgroundColor(Color.RED);
                    snackbar.show();
                }
                break;
            default:
                break;
        }

    }
}

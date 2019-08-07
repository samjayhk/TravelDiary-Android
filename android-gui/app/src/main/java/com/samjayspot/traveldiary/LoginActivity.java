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

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends SwipeBackActivity implements View.OnClickListener {

    ImageView btnBack;
    EditText edtLoginUsername, edtLoginPassword;
    Button btnLogin;

    RelativeLayout relativeLayoutLogin;
    SharedPreferences sharedpreferences;

    private void postRequest(String url, String username, String password) {
        MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();

        JSONObject postObject = new JSONObject();
        try {
            postObject.put("username", username);
            postObject.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        RequestBody body = RequestBody.create(JSON, postObject.toString());
        Request request = new Request.Builder()
                .url(url)
                .post(body)
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
                        putSession(json.getString("token"), json.getString("username"), json.getString("reg"),
                                json.getString("last"), json.getString("email"));
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        Log.d("bugs", json.getString("message"));
                        Snackbar snackbar = Snackbar.make(relativeLayoutLogin, json.getString("message"), Snackbar.LENGTH_SHORT);
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

    public void putSession(String token, String username, String reg, String last, String email) {
        sharedpreferences = getSharedPreferences("traveldiary", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("token", token);
        editor.putString("username", username);
        editor.putString("reg", reg);
        editor.putString("last", last);
        editor.putString("email", email);
        editor.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom);

        relativeLayoutLogin = (RelativeLayout) findViewById(R.id.relativeLayoutLogin);

        btnBack = (ImageView) findViewById(R.id.btnBack);
        edtLoginUsername = (EditText) findViewById(R.id.edtLoginUsername);
        edtLoginPassword = (EditText) findViewById(R.id.edtLoginPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        btnBack.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                onBackPressed();
                overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_top);
                break;
            case R.id.btnLogin:
                if (!edtLoginPassword.getText().toString().equals("")) {
                    postRequest(APIsManagement.getLogin(), edtLoginUsername.getText().toString(), edtLoginPassword.getText().toString());
                } else {
                    Log.d("bugs", "Password does not match!");
                    Snackbar snackbar = Snackbar.make(relativeLayoutLogin, "Please enter password!", Snackbar.LENGTH_SHORT);
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

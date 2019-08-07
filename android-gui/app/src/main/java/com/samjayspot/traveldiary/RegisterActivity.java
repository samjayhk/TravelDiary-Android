package com.samjayspot.traveldiary;

/**
 * PLEASE BE MIND THAT YOU COULD NOT COPY, MODIFY OR SHARE THIS PROJECT IF YOU ARE NOT GET PERMISSION.
 * @samjayhk
 */

import android.content.Intent;
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

public class RegisterActivity extends SwipeBackActivity implements View.OnClickListener {

    ImageView btnBack;
    EditText edtRegisterUsername, edtRegisterPassword, edtEdtRegisterConfirmPassword, edtRegisterEmail;
    Button btnRegister;

    RelativeLayout relativeLayoutRegister;

    private void postRequest(String url, String username, String password, String email) {
        MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();

        JSONObject postObject = new JSONObject();
        try {
            postObject.put("username", username);
            postObject.put("password", password);
            postObject.put("email", email);
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
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                    } else {
                        Log.d("bugs", json.getString("message"));
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom);

        relativeLayoutRegister = (RelativeLayout) findViewById(R.id.relativeLayoutRegister);

        btnBack = (ImageView) findViewById(R.id.btnBack);
        edtRegisterUsername = (EditText) findViewById(R.id.edtRegisterUsername);
        edtRegisterPassword = (EditText) findViewById(R.id.edtRegisterPassword);
        edtEdtRegisterConfirmPassword = (EditText) findViewById(R.id.edtRegisterConfirmPassword);
        edtRegisterEmail = (EditText) findViewById(R.id.edtRegisterEmail);
        btnRegister = (Button) findViewById(R.id.btnRegister);

        btnBack.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                onBackPressed();
                overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_top);
                break;
            case R.id.btnRegister:
                if (edtRegisterPassword.getText().toString().equals(edtEdtRegisterConfirmPassword.getText().toString())) {
                    postRequest(APIsManagement.getRegister(), edtRegisterUsername.getText().toString(), edtRegisterPassword.getText().toString(),
                            edtRegisterEmail.getText().toString());
                } else {
                    Log.d("bugs", "Password does not match!");
                    Snackbar snackbar = Snackbar.make(relativeLayoutRegister, "Password does not match!", Snackbar.LENGTH_SHORT);
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

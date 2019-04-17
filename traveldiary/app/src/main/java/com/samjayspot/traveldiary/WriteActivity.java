package com.samjayspot.traveldiary;

import android.app.AppComponentFactory;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.RequiresPermission;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jaredrummler.materialspinner.MaterialSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.text.Html.FROM_HTML_MODE_COMPACT;

public class WriteActivity extends SwipeBackActivity implements View.OnClickListener {

    ImageView btnClose;
    TextView btnSubmit, btnUpload;
    EditText edtTitle, edtContent;

    RelativeLayout writeLayout;
    MaterialSpinner spinner;
    SharedPreferences sharedPreferences;

    int currentTid = 0;
    ArrayList<Integer> tids = new ArrayList<Integer>();
    ArrayList<String> tags = new ArrayList<String>();

    private void postRequest(String url, String tid, String subject, String content) {

        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("tid", tid)
                .add("subject", subject)
                .add("content", content)
                .build();
        Request request =  new Request.Builder()
            .addHeader("x-token", getSession().get(0).toString())
            .url(url)
            .post(formBody)
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
                        Intent intent = new Intent(WriteActivity.this, MainActivity.class);
                        startActivity(intent);

                    } else {
                        Log.d("bugs", json.getString("message"));
                        Snackbar snackbar = Snackbar.make(writeLayout, json.getString("message"), Snackbar.LENGTH_SHORT);
                        View rootSnackbar = snackbar.getView();
                        rootSnackbar.setBackgroundColor(Color.RED);
                        snackbar.show();
                    }
                } catch (JSONException e) {
                    Snackbar snackbar = Snackbar.make(writeLayout, "Something went wrong", Snackbar.LENGTH_SHORT);
                    View rootSnackbar = snackbar.getView();
                    rootSnackbar.setBackgroundColor(Color.RED);
                    snackbar.show();
                }
            }
        });
    }

    public void getRequest(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();

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

                        JSONArray tagsList = json.getJSONArray("tags");

                        for (int i = 0; i < tagsList.length(); i++) {
                            JSONObject tag = tagsList.getJSONObject(i);
                            tids.add(tag.getInt("tid"));
                            tags.add(tag.getString("name"));
                        }
                        spinner.setItems(tags);
                    } else {
                        Log.d("bugs", json.getString("message"));
                    }

                } catch (JSONException e) {
                    Log.d("bugs", e.toString());
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom);

        writeLayout = (RelativeLayout) findViewById(R.id.writeLayout);
        spinner = (MaterialSpinner) findViewById(R.id.writeSpinner);
        btnClose = (ImageView) findViewById(R.id.btnClose);
        btnSubmit = (TextView) findViewById(R.id.btnSubmit);
        edtTitle = (EditText) findViewById(R.id.edtTitle);
        edtContent = (EditText) findViewById(R.id.edtContent);
        btnUpload = (TextView) findViewById(R.id.btnUpload);

        btnClose.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        btnUpload.setOnClickListener(this);

        try {
            this.getRequest(APIsManagement.getTagsList());
        } catch (IOException e) {
            e.printStackTrace();
        }

        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                currentTid = position;
                //Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
            }
        });

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

    public String nativeToUnicode(String content) {
        String characters = content;
        String ascii = "";
        for (int i = 0; i < characters.length(); i++) {
            int code = Character.codePointAt(String.valueOf(characters.charAt(i)), 0);
            if (code > 127) {
                String charAscii = Integer.toString(code, 16);
                charAscii = new String("0000").substring(charAscii.length(), 4) + charAscii;
                ascii += "\\u" + charAscii;
            } else {
                ascii += characters.charAt(i);
            }
        }
        return ascii;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnClose:
                onBackPressed();
                overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_top);
                break;
            case R.id.btnSubmit:
                String title = this.nativeToUnicode(edtTitle.getText().toString());
                String content = Html.toHtml(edtContent.getText(), Html.FROM_HTML_MODE_LEGACY);
                content = this.nativeToUnicode(content);

                postRequest(APIsManagement.getWriteThread(), String.valueOf(currentTid), title, content);

                break;
            case R.id.btnUpload:
                break;
            default:
                break;
        }
    }
}

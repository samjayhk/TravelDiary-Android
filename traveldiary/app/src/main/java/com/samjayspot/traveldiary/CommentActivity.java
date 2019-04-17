package com.samjayspot.traveldiary;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CommentActivity extends SwipeBackActivity implements View.OnClickListener {

    int pid;

    ImageView btnClose;
    TextView btnSubmit, btnUpload, txtTitle;
    EditText edtContent;

    RelativeLayout commentLayout;
    SharedPreferences sharedPreferences;

    private void postRequest(String url, String comment) {

        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("comment", comment)
                .build();
        Request request = new Request.Builder()
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
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                onBackPressed();
                                overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_top);
                            }
                        });
                    } else {
                        Log.d("bugs", json.getString("message"));
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
        setContentView(R.layout.activity_comment);
        overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom);

        commentLayout = (RelativeLayout) findViewById(R.id.commentLayout);
        btnClose = (ImageView) findViewById(R.id.btnCommentClose);
        btnSubmit = (TextView) findViewById(R.id.btnCommentComment);
        txtTitle = (TextView) findViewById(R.id.txtCommentTitle);
        edtContent = (EditText) findViewById(R.id.edtContent);
        btnUpload = (TextView) findViewById(R.id.btnCommentUpload);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            pid = extras.getInt("pid");
            txtTitle.setText(extras.getString("title"));
        }

        btnClose.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        btnUpload.setOnClickListener(this);

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
            case R.id.btnCommentClose:
                onBackPressed();
                overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_top);
                break;
            case R.id.btnCommentComment:
                String content = Html.toHtml(edtContent.getText(), Html.FROM_HTML_MODE_LEGACY);
                content = this.nativeToUnicode(content);

                postRequest(APIsManagement.getWriteComment(pid), content);
                break;
            case R.id.btnCommentUpload:
                break;
            default:
                break;
        }
    }
}

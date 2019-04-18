package com.samjayspot.traveldiary;

/**
 * THIS PROJECT IS A UNIVERSITY PROJECT AND MADE BY SCHOOL FOR HIGHER AND PROFESSIONAL EDUCATION WITH COVENTRY UNIVERSITY (UK).
 * PLEASE BE MIND THAT YOU COULD NOT COPY, MODIFY OR SHARE THIS PROJECT IF YOU ARE NOT GET PERMISSION.
 * AUTHOR INFORMATION:
 * NAME: TSANG Long Fung (187107130)
 * CONTACT NUMBER: (+852) 6679 2339
 * CONTACT EMAIL: 187107130@stu.vtc.edu.hk
 */

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SearchActivity extends SwipeBackActivity implements View.OnClickListener {

    RelativeLayout searchLayout;
    RecyclerView listRecyclerView;
    EditText etSearch;
    Button btnSearchCancel;

    PopularTabAdapter adapter;

    ArrayList<Integer> pid = new ArrayList<Integer>();
    ArrayList<Integer> uid = new ArrayList<Integer>();
    ArrayList<String> username = new ArrayList<String>();
    ArrayList<String> tags = new ArrayList<String>();
    ArrayList<String> subject = new ArrayList<String>();
    ArrayList<String> ntime = new ArrayList<String>();
    ArrayList<Integer> count = new ArrayList<Integer>();
    ArrayList<Integer> pages = new ArrayList<Integer>();
    ArrayList<String> cover = new ArrayList<String>();

    public void getRequest(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("bugs", e.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listRecyclerView.setAnimation(null);
                        adapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {

                    JSONObject json = new JSONObject(response.body().string());

                    if (Boolean.parseBoolean(json.getString("result"))) {

                        JSONArray threadList = json.getJSONArray("search");

                        for (int i = 0; i < threadList.length(); i++) {
                            JSONObject thread = threadList.getJSONObject(i);
                            pid.add(thread.getInt("pid"));
                        }

                        for (int i = 0; i < threadList.length(); i++) {
                            JSONObject thread = threadList.getJSONObject(i);
                            uid.add(thread.getInt("uid"));
                        }

                        for (int i = 0; i < threadList.length(); i++) {
                            JSONObject thread = threadList.getJSONObject(i);
                            username.add(thread.getString("username"));
                        }

                        for (int i = 0; i < threadList.length(); i++) {
                            JSONObject thread = threadList.getJSONObject(i);
                            tags.add(thread.getString("tag"));
                        }

                        for (int i = 0; i < threadList.length(); i++) {
                            JSONObject thread = threadList.getJSONObject(i);
                            subject.add(thread.getString("subject"));
                        }

                        for (int i = 0; i < threadList.length(); i++) {
                            JSONObject thread = threadList.getJSONObject(i);
                            ntime.add(thread.getString("ntime"));
                        }

                        for (int i = 0; i < threadList.length(); i++) {
                            JSONObject thread = threadList.getJSONObject(i);
                            count.add(thread.getInt("count"));
                        }

                        for (int i = 0; i < threadList.length(); i++) {
                            JSONObject thread = threadList.getJSONObject(i);
                            pages.add(thread.getInt("pages"));
                        }

                        for (int i = 0; i < threadList.length(); i++) {
                            JSONObject thread = threadList.getJSONObject(i);
                            cover.add(thread.getString("cover"));
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                listRecyclerView.setAnimation(null);
                                adapter.notifyDataSetChanged();
                            }
                        });
                    } else {
                        Log.d("bugs", json.getString("message"));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                listRecyclerView.setAnimation(null);
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }

                } catch (JSONException e) {
                    Log.d("bugs", e.toString());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            listRecyclerView.setAnimation(null);
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom);

        listRecyclerView = (RecyclerView) findViewById(R.id.listRecyclerView);
        searchLayout = (RelativeLayout) findViewById(R.id.searchLayout);
        etSearch = (EditText) findViewById(R.id.etSearch);
        btnSearchCancel = (Button) findViewById(R.id.btnSearchCancel);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        listRecyclerView.setLayoutManager(layoutManager);
        adapter = new PopularTabAdapter(pid, uid, username, tags, subject, ntime, count, pages, cover);
        listRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    try {
                        Snackbar snackbar = Snackbar.make(searchLayout, "Searching " + etSearch.getText().toString() + "...", Snackbar.LENGTH_SHORT);
                        View rootSnackbar = snackbar.getView();
                        rootSnackbar.setBackgroundColor(getColor(R.color.main_blue));
                        snackbar.show();
                        getRequest(APIsManagement.getSearch(etSearch.getText().toString(), 1));

                        InputMethodManager inputManager =
                                (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputManager.hideSoftInputFromWindow(
                                getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    return true;
                }
                return false;
            }
        });
        btnSearchCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSearchCancel:
                onBackPressed();
                overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_top);
                break;
            default:
                break;
        }
    }
}

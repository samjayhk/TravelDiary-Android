package com.samjayspot.traveldiary;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.just.agentweb.AgentWebView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ThreadViewActivity extends SwipeBackActivity implements View.OnClickListener {

    int pid = 0, page = 1, sum = 1;

    ImageView btnThreadBack, btnThreadReply, btnThreadMenu;
    TextView txtThreadTitle;
    SharedPreferences sharedPreferences;

    RecyclerView recyclerThread;
    ThreadViewAdapter threadViewAdapter;

    SwipeRefreshLayout mSwipeRefreshLayout;

    ArrayList<JSONObject> threads = new ArrayList<JSONObject>();
    ArrayList<Integer> cids = new ArrayList<Integer>();
    ArrayList<Integer> uids = new ArrayList<Integer>();
    ArrayList<String> username = new ArrayList<String>();
    ArrayList<String> comment = new ArrayList<String>();
    ArrayList<String> ctime = new ArrayList<String>();
    ArrayList<String> uptime = new ArrayList<String>();

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

                JSONObject json = null;
                try {
                    json = new JSONObject(response.body().string());
                    page = json.getInt("page");
                    sum = json.getInt("sum");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {

                    if (Boolean.parseBoolean(json.getString("result"))) {

                        threads.add(json.getJSONArray("thread").getJSONObject(0));
                        txtThreadTitle.setText(ascii2native(threads.get(0).getString("subject")));

                    } else {
                        Log.d("bugs", json.getString("message"));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                recyclerThread.setAnimation(null);
                                threadViewAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                } catch (JSONException e) {
                    Log.d("bugs", e.toString());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recyclerThread.setAnimation(null);
                            threadViewAdapter.notifyDataSetChanged();
                        }
                    });
                }

                try {

                    if (Boolean.parseBoolean(json.getString("result"))) {
                        JSONArray comments = json.getJSONArray("comment");

                        for (int i = 0; i < comments.length(); i++) {
                            JSONObject cmt = comments.getJSONObject(i);
                            cids.add(cmt.getInt("pid"));
                            uids.add(cmt.getInt("uid"));
                            username.add(cmt.getString("username"));
                            comment.add(cmt.getString("comment"));
                            ctime.add(cmt.getString("ctime"));
                            uptime.add(cmt.getString("uptime"));
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                recyclerThread.setAnimation(null);
                                threadViewAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                } catch (JSONException e) {
                    Log.d("bugs", e.toString());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recyclerThread.setAnimation(null);
                            threadViewAdapter.notifyDataSetChanged();
                        }
                    });
                }

            }
        });
    }

    public static String fromCharCode(int... codePoints) {
        StringBuilder builder = new StringBuilder(codePoints.length);
        for (int codePoint : codePoints) {
            builder.append(Character.toChars(codePoint));
        }
        return builder.toString();
    }

    public static String ascii2native(String str) {
        String[] character = str.split("\\\\u");
        String natives = character[0];
        for (int i = 1; i < character.length; i++) {
            String code = character[i];
            natives += fromCharCode(Integer.decode("0x" + code.substring(0, 4)));
            if (code.length() > 4) {
                natives += code.substring(4, code.length());
            }
        };
        return natives;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread);
        overridePendingTransition(R.anim.inlefttoright, R.anim.nulltranslation);

        recyclerThread = (RecyclerView) findViewById(R.id.recyclerThread);
        btnThreadBack = (ImageView) findViewById(R.id.btnThreadBack);
        btnThreadReply = (ImageView) findViewById(R.id.btnThreadReply);
        btnThreadMenu = (ImageView) findViewById(R.id.btnThreadMenu);
        txtThreadTitle = (TextView) findViewById(R.id.txtTreadTitle);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            pid = extras.getInt("pid");
            page = extras.getInt("page");
            try {
                getRequest(APIsManagement.getThread(pid, page));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerThread.setLayoutManager(layoutManager);
        threadViewAdapter = new ThreadViewAdapter(threads, cids, uids, username, comment, ctime, uptime);
        recyclerThread.setAdapter(threadViewAdapter);
        //threadViewAdapter.notifyDataSetChanged();

        btnThreadReply.setOnClickListener(this);
        btnThreadMenu.setOnClickListener(this);
        btnThreadBack.setOnClickListener(this);

        recyclerThread.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1)) {
                    if (page == sum) {
                        Snackbar snackbar = Snackbar.make(recyclerThread, "This is the end!", Snackbar.LENGTH_SHORT);
                        View rootSnackbar = snackbar.getView();
                        rootSnackbar.setBackgroundColor(getResources().getColor(R.color.main_blue));
                        snackbar.show();
                    } else {
                        Snackbar snackbar = Snackbar.make(recyclerThread, "loading page " + (page + 1), Snackbar.LENGTH_SHORT);
                        View rootSnackbar = snackbar.getView();
                        rootSnackbar.setBackgroundColor(getResources().getColor(R.color.main_blue));
                        snackbar.show();
                        try {
                            getRequest(APIsManagement.getThread(pid, page + 1));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swifeRefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                threads.clear();
                cids.clear();
                uids.clear();
                username.clear();
                comment.clear();
                ctime.clear();
                uptime.clear();
                try {
                    getRequest(APIsManagement.getThread(pid, page));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mSwipeRefreshLayout.setRefreshing(false);

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnThreadBack:
                onBackPressed();
                overridePendingTransition(R.anim.nulltranslation, R.anim.outlefttoright);
                break;
            case R.id.btnThreadReply:
                Intent replyIntent = new Intent(ThreadViewActivity.this, CommentActivity.class);
                replyIntent.putExtra("pid", pid);
                replyIntent.putExtra("page", page);
                startActivity(replyIntent);
                break;
            case R.id.btnThreadMenu:
                PopupMenu popup = new PopupMenu(this, v);

                popup.getMenuInflater().inflate(R.menu.thread, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_edit:

                                return true;
                            case R.id.action_delete:

                                return true;
                            default:
                                return false;
                        }
                    }
                });

                popup.show();

                break;
            case R.id.btnThreadComment:
                Intent commentIntent = new Intent(ThreadViewActivity.this, CommentActivity.class);
                commentIntent.putExtra("pid", pid);
                commentIntent.putExtra("page", page);
                startActivity(commentIntent);
                break;
        }
    }


}

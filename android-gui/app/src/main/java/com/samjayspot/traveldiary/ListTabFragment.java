package com.samjayspot.traveldiary;

/**
 * THIS PROJECT IS A UNIVERSITY PROJECT AND MADE BY SCHOOL FOR HIGHER AND PROFESSIONAL EDUCATION WITH COVENTRY UNIVERSITY (UK).
 * PLEASE BE MIND THAT YOU COULD NOT COPY, MODIFY OR SHARE THIS PROJECT IF YOU ARE NOT GET PERMISSION.
 * AUTHOR INFORMATION:
 * NAME: TSANG Long Fung (187107130)
 * CONTACT NUMBER: (+852) 6679 2339
 * CONTACT EMAIL: 187107130@stu.vtc.edu.hk
 */

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.jaredrummler.materialspinner.MaterialSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ListTabFragment extends Fragment {

    View rootView;
    RecyclerView recyclerView;
    MaterialSpinner materialSpinner;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ListTabAdapter listTabAdapter;

    int currentTid = 0;
    ArrayList<Integer> tidSpinner = new ArrayList<Integer>();
    ArrayList<String> tagSpinner = new ArrayList<String>();

    int currentPage = 1;
    int currentSum = 1;

    boolean refresh = false;

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
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setAnimation(null);
                        listTabAdapter.notifyDataSetChanged();
                        refresh = false;
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {

                    JSONObject json = new JSONObject(response.body().string());

                    if (Boolean.parseBoolean(json.getString("result"))) {

                        JSONArray threadList = json.getJSONArray("thread");
                        currentPage = json.getInt("page");
                        currentSum = json.getInt("sum");

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
                            tags.add(thread.getString("tags"));
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

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                recyclerView.setAnimation(null);
                                listTabAdapter.notifyDataSetChanged();
                                refresh = false;
                            }
                        });
                    } else {
                        Log.d("bugs", json.getString("message"));
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                recyclerView.setAnimation(null);
                                listTabAdapter.notifyDataSetChanged();
                                refresh = false;
                            }
                        });
                        Snackbar snackbar = Snackbar.make(rootView, json.getString("message"), Snackbar.LENGTH_SHORT);
                        View rootSnackbar = snackbar.getView();
                        rootSnackbar.setBackgroundColor(getResources().getColor(R.color.main_blue));
                        snackbar.show();
                    }

                } catch (JSONException e) {
                    Log.d("bugs", e.toString());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.setAnimation(null);
                            listTabAdapter.notifyDataSetChanged();
                            refresh = false;
                        }
                    });
                }
            }
        });
    }

    public void getTags(String url) throws IOException {
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
                            tidSpinner.add(tag.getInt("tid"));
                            tagSpinner.add(tag.getString("name"));
                        }
                        materialSpinner.setItems(tagSpinner);
                        getRequest(APIsManagement.getThreadList(0, 1));
                    } else {
                        Log.d("bugs", json.getString("message"));
                    }

                } catch (JSONException e) {
                    Log.d("bugs", e.toString());
                }
            }
        });
    }

    public ArrayList<Integer> convInt(int n) {
        ArrayList<Integer> re = new ArrayList<Integer>();
        for (int i = 0; i < n; i++) {
            re.add(i + 1);
        }
        return re;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(
                R.layout.fragment_home_list,
                container,
                false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.tagsRecyclerView);
        materialSpinner = (MaterialSpinner) rootView.findViewById(R.id.tagsSpinner);

        try {
            getTags(APIsManagement.getTagsList());
        } catch (IOException e) {
            e.printStackTrace();
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        listTabAdapter = new ListTabAdapter(pid, uid, username, tags, subject, ntime, count, pages, cover);
        recyclerView.setAdapter(listTabAdapter);
        listTabAdapter.notifyDataSetChanged();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1)) {
                    if (currentPage != currentSum) {
                        Snackbar snackbar = Snackbar.make(rootView, "loading page " + (currentPage + 1), Snackbar.LENGTH_SHORT);
                        View rootSnackbar = snackbar.getView();
                        rootSnackbar.setBackgroundColor(getResources().getColor(R.color.main_blue));
                        snackbar.show();
                        try {
                            getRequest(APIsManagement.getThreadList(currentTid, currentPage + 1));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (refresh) {
                    return true;
                } else {
                    return false;
                }
            }
        });

        materialSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                try {
                    refresh = true;
                    currentTid = position;
                    pid.clear();
                    uid.clear();
                    username.clear();
                    tags.clear();
                    subject.clear();
                    ntime.clear();
                    count.clear();
                    pages.clear();
                    cover.clear();
                    getRequest(APIsManagement.getThreadList(position, 1));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swifeRefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pid.clear();
                uid.clear();
                username.clear();
                tags.clear();
                subject.clear();
                ntime.clear();
                count.clear();
                pages.clear();
                cover.clear();
                try {
                    getRequest(APIsManagement.getThreadList(currentTid, 1));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mSwipeRefreshLayout.setRefreshing(false);

            }
        });


        return rootView;

    }
}
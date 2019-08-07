package com.samjayspot.traveldiary;

/**
 * PLEASE BE MIND THAT YOU COULD NOT COPY, MODIFY OR SHARE THIS PROJECT IF YOU ARE NOT GET PERMISSION.
 * @samjayhk
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.http.SslError;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ThreadViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static ArrayList<JSONObject> threads;
    private static ArrayList<Integer> cids;
    private static ArrayList<Integer> uids;
    private static ArrayList<String> username;
    private static ArrayList<String> comment;
    private static ArrayList<String> ctime;
    private static ArrayList<String> uptime;
    private int VIEW_TYPE_MAIN = 0;
    private int VIEW_TYPE_COMMENT = 1;

    ThreadViewAdapter(ArrayList<JSONObject> threads, ArrayList<Integer> cids, ArrayList<Integer> uids,
                      ArrayList<String> username, ArrayList<String> comment,
                      ArrayList<String> ctime, ArrayList<String> uptime) {
        ThreadViewAdapter.threads = threads;
        ThreadViewAdapter.cids = cids;
        ThreadViewAdapter.uids = uids;
        ThreadViewAdapter.username = username;
        ThreadViewAdapter.comment = comment;
        ThreadViewAdapter.ctime = ctime;
        ThreadViewAdapter.uptime = uptime;
    }

    private static String fromCharCode(int... codePoints) {
        StringBuilder builder = new StringBuilder(codePoints.length);
        for (int codePoint : codePoints) {
            builder.append(Character.toChars(codePoint));
        }
        return builder.toString();
    }

    private static String ascii2native(String str) {
        String[] character = str.split("\\\\u");
        String natives = character[0];
        for (int i = 1; i < character.length; i++) {
            String code = character[i];
            natives += fromCharCode(Integer.decode("0x" + code.substring(0, 4)));
            if (code.length() > 4) {
                natives += code.substring(4);
            }
        }
        return natives;
    }

    @Override
    public int getItemViewType(int position) {
        if (threads.isEmpty()) {
            return VIEW_TYPE_COMMENT;
        } else {
            if (position == 0) {
                return VIEW_TYPE_MAIN;
            } else {
                return VIEW_TYPE_COMMENT;
            }
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_MAIN) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_thread_main, parent, false);
            return new ThreadViewAdapter.ThreadViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_thread, parent, false);
            return new ThreadViewAdapter.CommentViewHolder(view);
        }
    }

    public ArrayList getSession(Context c) {
        SharedPreferences sharedPreferences = c.getSharedPreferences("traveldiary", Context.MODE_PRIVATE);
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

    public void deleteRequest(String url, final Context c, final View v, final int deletePos) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .addHeader("x-token", getSession(c).get(0).toString())
                .url(url)
                .delete()
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

                        cids.remove(deletePos);
                        uids.remove(deletePos);
                        username.remove(deletePos);
                        comment.remove(deletePos);
                        ctime.remove(deletePos);
                        uptime.remove(deletePos);
                        Snackbar snackbar = Snackbar.make(v, json.getString("message"), Snackbar.LENGTH_SHORT);
                        View rootSnackbar = snackbar.getView();
                        rootSnackbar.setBackgroundColor(c.getColor(R.color.main_blue));
                        snackbar.show();
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            public void run() {
                                notifyItemRemoved(deletePos);
                                notifyDataSetChanged();
                            }
                        });
                    } else {
                        Snackbar snackbar = Snackbar.make(v, json.getString("message"), Snackbar.LENGTH_SHORT);
                        View rootSnackbar = snackbar.getView();
                        rootSnackbar.setBackgroundColor(Color.RED);
                        snackbar.show();
                    }

                } catch (JSONException e) {
                    Snackbar snackbar = Snackbar.make(v, e.toString(), Snackbar.LENGTH_SHORT);
                    View rootSnackbar = snackbar.getView();
                    rootSnackbar.setBackgroundColor(Color.RED);
                    snackbar.show();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof ThreadViewHolder) {
            try {
                ((ThreadViewHolder) viewHolder).threadTags.setText("t/" + threads.get(0).getString("tags"));
                ((ThreadViewHolder) viewHolder).threadUsername.setText("u/" + threads.get(0).getString("username"));

                Date date = new Date();
                double cal = Math.abs(date.getTime() - Double.parseDouble(threads.get(0).getString("ptime")));
                double minsDiff = Math.ceil(cal / (1000 * 60));
                double hoursDiff = Math.ceil(cal / (1000 * 3600));
                double diffDays = Math.ceil(cal / (1000 * 3600 * 24));

                if (minsDiff - 1 < 60) {
                    ((ThreadViewHolder) viewHolder).threadTime.setText(((int) minsDiff - 1) + "m");
                } else if (hoursDiff - 1 < 24) {
                    ((ThreadViewHolder) viewHolder).threadTime.setText(((int) hoursDiff - 1) + "h");
                } else if (diffDays - 1 < 30) {
                    ((ThreadViewHolder) viewHolder).threadTime.setText(((int) diffDays - 1) + "d");
                } else {
                    ((ThreadViewHolder) viewHolder).threadTime.setText(((int) (diffDays - 1 / 30)) + "m");
                }

                ((ThreadViewHolder) viewHolder).threadCommentCount.setText(threads.get(0).getString("count"));
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (IndexOutOfBoundsException e) {
                // a comment view
            }

            String contentHTML = "<style>" +
                    "body, html {" +
                    "   height: 100% !important;" +
                    "   width: 100% !important;" +
                    "   color: white;" +
                    "   background: transparent !important;" +
                    "   margin: 0%;" +
                    "   font-size: 20px !important;" +
                    "   font-family: Helvetica Neue,Helvetica,Arial,Microsoft JhengHei,sans-serif !important;" +
                    "}" +
                    "a, a:link, a:visited, a:active {" +
                    "   color: #555555;" +
                    "   text-decoration: none;" +
                    "   word-wrap: break-word;" +
                    "}" +
                    "a:hover {" +
                    "   color: white !important;" +
                    "}" +
                    "p, h3{" +
                    "   color:white !important;" +
                    "   font-color:white !important;" +
                    "   margin-left:10dp !important;" +
                    "   margin-right:10dp !important;" +
                    "}" +
                    "img, video{" +
                    "   display: inline;" +
                    "   height: auto;" +
                    "   max-width: 100%;" +
                    "}" +
                    "figure {" +
                    "   max-width: 100%;" +
                    "   margin: 0%;" +
                    "}" +
                    "div.groove {" +
                    "   border-style: solid;" +
                    "   border-color: #E2E2E2;" +
                    "   border-width: 1px;" +
                    "   color: #E2E2E2;" +
                    "}" +
                    "div.groove > p {" +
                    "   color: #C7C7C7;" +
                    "   display: inline;" +
                    "   margin-left: 10px;" +
                    "}" +
                    "div.newline {" +
                    "   display: block;" +
                    "   color: #C2C2C2;" +
                    "   margin-top: 5px;" +
                    "   font-weight: bold;" +
                    "   margin-left: 10px;" +
                    "}" +
                    "div.groove > br {" +
                    "   display: inline;" +
                    "   content: ' ';" +
                    "   clear:none;" +
                    "}" +
                    "</style>";

            try {
                contentHTML += ascii2native(threads.get(0).getString("content").replace("http://localhost:3001/", APIsManagement.defaultServer));
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (IndexOutOfBoundsException e) {
                // a comment view
            }
            final String finalContentHTML = contentHTML;
            ((ThreadViewHolder) viewHolder).threadAgentWebView.getSettings().setJavaScriptEnabled(false);
            ((ThreadViewHolder) viewHolder).threadAgentWebView.getSettings().setDomStorageEnabled(true);
            ((ThreadViewHolder) viewHolder).threadAgentWebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
            ((ThreadViewHolder) viewHolder).threadAgentWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            ((ThreadViewHolder) viewHolder).threadAgentWebView.setBackgroundColor(Color.TRANSPARENT);
            ((ThreadViewHolder) viewHolder).threadAgentWebView.setOverScrollMode(WebView.SCROLL_AXIS_NONE);
            ((ThreadViewHolder) viewHolder).threadAgentWebView.getSettings().setUserAgentString(Locale.getDefault().getLanguage());
            ((ThreadViewHolder) viewHolder).threadAgentWebView.setWebViewClient(new WebViewClient() {
                @Override
                public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                    handler.proceed();
                }
            });
            ((ThreadViewHolder) viewHolder).threadAgentWebView.loadUrl("about:blank");
            ((ThreadViewHolder) viewHolder).threadAgentWebView.loadDataWithBaseURL(null, finalContentHTML, "text/html", "utf-8", null);

            ((ThreadViewHolder) viewHolder).btnThreadComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent replyIntent = new Intent(v.getContext(), CommentActivity.class);
                    try {
                        replyIntent.putExtra("pid", threads.get(0).getInt("pid"));
                        replyIntent.putExtra("title", "Re: " + threads.get(0).getString("subject"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    } catch (IndexOutOfBoundsException e) {
                        // a comment view
                    }
                    v.getContext().startActivity(replyIntent);
                }
            });

            ((ThreadViewHolder) viewHolder).btnThreadShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, threads.get(0).getString("subject"));
                        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, threads.get(0).getString("content"));
                        v.getContext().startActivity(Intent.createChooser(sharingIntent, "Share with friends"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            int newPos = i;
            if (threads.isEmpty()) {
                newPos = i;
            } else {
                newPos = i - 1;
            }

            ((CommentViewHolder) viewHolder).commentUsername.setText(username.get(newPos));

            Date date = new Date();
            double cal = Math.abs(date.getTime() - Double.parseDouble(ctime.get(newPos)));
            double minsDiff = Math.ceil(cal / (1000 * 60));
            double hoursDiff = Math.ceil(cal / (1000 * 3600));
            double diffDays = Math.ceil(cal / (1000 * 3600 * 24));

            if (minsDiff - 1 < 60) {
                ((CommentViewHolder) viewHolder).commentTime.setText("•" + ((int) minsDiff - 1) + "m");
            } else if (hoursDiff - 1 < 24) {
                ((CommentViewHolder) viewHolder).commentTime.setText("•" + ((int) hoursDiff - 1) + "h");
            } else if (diffDays - 1 < 30) {
                ((CommentViewHolder) viewHolder).commentTime.setText("•" + ((int) diffDays - 1) + "d");
            } else {
                ((CommentViewHolder) viewHolder).commentTime.setText("•" + ((int) (diffDays - 1 / 30)) + "m");
            }

            String contentHTML = "<style>" +
                    "body, html {" +
                    "   height: 100% !important;" +
                    "   width: 100% !important;" +
                    "   color: white;" +
                    "   background: transparent !important;" +
                    "   margin: 0%;" +
                    "   font-size: 20px !important;" +
                    "   font-family: Helvetica Neue,Helvetica,Arial,Microsoft JhengHei,sans-serif !important;" +
                    "}" +
                    "a, a:link, a:visited, a:active {" +
                    "   color: #555555;" +
                    "   text-decoration: none;" +
                    "   word-wrap: break-word;" +
                    "}" +
                    "a:hover {" +
                    "   color: white !important;" +
                    "}" +
                    "p, h3{" +
                    "   color:white !important;" +
                    "   font-color:white !important;" +
                    "}" +
                    "img, video{" +
                    "   display: inline;" +
                    "   height: auto;" +
                    "   max-width: 100%;" +
                    "}" +
                    "figure {" +
                    "   max-width: 100%;" +
                    "   margin: 0%;" +
                    "}" +
                    "div.groove {" +
                    "   border-style: solid;" +
                    "   border-color: #E2E2E2;" +
                    "   border-width: 1px;" +
                    "   color: #E2E2E2;" +
                    "}" +
                    "div.groove > p {" +
                    "   color: #C7C7C7;" +
                    "   display: inline;" +
                    "   margin-left: 10px;" +
                    "}" +
                    "div.newline {" +
                    "   display: block;" +
                    "   color: #C2C2C2;" +
                    "   margin-top: 5px;" +
                    "   font-weight: bold;" +
                    "   margin-left: 10px;" +
                    "}" +
                    "div.groove > br {" +
                    "   display: inline;" +
                    "   content: ' ';" +
                    "   clear:none;" +
                    "}" +
                    "</style>";

            contentHTML += ascii2native(comment.get(newPos).replace("http://localhost:3001/", APIsManagement.defaultServer));

            ((CommentViewHolder) viewHolder).commentAgentWebView.getSettings().setJavaScriptEnabled(false);
            ((CommentViewHolder) viewHolder).commentAgentWebView.getSettings().setDomStorageEnabled(true);
            ((CommentViewHolder) viewHolder).commentAgentWebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
            ((CommentViewHolder) viewHolder).commentAgentWebView.setBackgroundColor(Color.TRANSPARENT);
            ((CommentViewHolder) viewHolder).commentAgentWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            ((CommentViewHolder) viewHolder).commentAgentWebView.setOverScrollMode(WebView.SCROLL_AXIS_NONE);
            ((CommentViewHolder) viewHolder).commentAgentWebView.getSettings().setUserAgentString(Locale.getDefault().getLanguage());
            ((CommentViewHolder) viewHolder).commentAgentWebView.setWebViewClient(new WebViewClient() {
                @Override
                public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                    handler.proceed();
                }
            });
            ((CommentViewHolder) viewHolder).commentAgentWebView.loadUrl("about:blank");

            ((CommentViewHolder) viewHolder).commentAgentWebView.loadDataWithBaseURL(null, contentHTML, "text/html", "utf-8", null);

            final int finalNewPos = newPos;
            ((CommentViewHolder) viewHolder).btnMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    PopupMenu popup = new PopupMenu(v.getContext(), v);

                    popup.getMenuInflater().inflate(R.menu.comment, popup.getMenu());

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.action_edit_comment:
                                    try {
                                        if (username.get(finalNewPos).equals(getSession(v.getContext()).get(1))) {
                                            Intent editIntent = new Intent(v.getContext(), EditCommentActivity.class);


                                            editIntent.putExtra("cid", cids.get(finalNewPos));
                                            editIntent.putExtra("title", threads.get(0).getString("subject"));
                                            editIntent.putExtra("comment", comment.get(finalNewPos));

                                            v.getContext().startActivity(editIntent);
                                        } else {
                                            Snackbar snackbar = Snackbar.make(((CommentViewHolder) viewHolder).itemView, "Only comment owner can edit!", Snackbar.LENGTH_SHORT);
                                            View rootSnackbar = snackbar.getView();
                                            rootSnackbar.setBackgroundColor(Color.RED);
                                            snackbar.show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    return true;
                                case R.id.action_delete_comment:
                                    try {
                                        if (username.get(finalNewPos).equals(getSession(v.getContext()).get(1))) {
                                            try {
                                                deleteRequest(APIsManagement.getDeleteComment(cids.get(finalNewPos)), ((CommentViewHolder) viewHolder).itemView.getContext(), ((CommentViewHolder) viewHolder).itemView.getRootView(), finalNewPos);
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        } else {
                                            Snackbar snackbar = Snackbar.make(((CommentViewHolder) viewHolder).itemView, "Only comment owner can delete!", Snackbar.LENGTH_SHORT);
                                            View rootSnackbar = snackbar.getView();
                                            rootSnackbar.setBackgroundColor(Color.RED);
                                            snackbar.show();
                                        }
                                    } catch (IndexOutOfBoundsException e) {
                                        Log.d("bugs", e.toString());
                                    }
                                    return true;
                                default:
                                    return false;
                            }
                        }
                    });

                    popup.show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (threads.isEmpty()) {
            return cids.size();
        } else {
            return cids.size() + 1;
        }
    }

    public class ThreadViewHolder extends RecyclerView.ViewHolder {
        TextView threadTags, threadUsername, threadTime,
                threadCommentCount;
        RelativeLayout btnThreadComment, btnThreadShare;
        WebView threadAgentWebView;

        @SuppressLint("WrongViewCast")
        public ThreadViewHolder(View view) {
            super(view);
            this.threadTags = (TextView) view.findViewById(R.id.threadTags);
            this.threadUsername = (TextView) view.findViewById(R.id.threadUsername);
            this.threadTime = (TextView) view.findViewById(R.id.threadTime);
            this.threadCommentCount = (TextView) view.findViewById(R.id.threadCommentCount);
            this.btnThreadComment = (RelativeLayout) view.findViewById(R.id.btnThreadComment);
            this.btnThreadShare = (RelativeLayout) view.findViewById(R.id.btnThreadShare);
            this.threadAgentWebView = (WebView) view.findViewById(R.id.threadAgentWebView);
        }
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView commentUsername, commentTime;
        ImageView btnMenu;
        WebView commentAgentWebView;

        @SuppressLint("WrongViewCast")
        public CommentViewHolder(View view) {
            super(view);
            this.btnMenu = (ImageView) view.findViewById(R.id.btnMenu);
            this.commentUsername = (TextView) view.findViewById(R.id.commentUsername);
            this.commentTime = (TextView) view.findViewById(R.id.commentTime);
            this.commentAgentWebView = (WebView) view.findViewById(R.id.commentAgentWebView);
        }
    }
}

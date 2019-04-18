package com.samjayspot.traveldiary;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.samjayspot.traveldiary.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

public class PopularTabAdapter extends RecyclerView.Adapter<PopularTabAdapter.ViewHolder> {

    public static ArrayList<Integer> pid;
    public static ArrayList<Integer> uid;
    public static ArrayList<String> username;
    public static ArrayList<String> tags;
    public static ArrayList<String> subject;
    public static ArrayList<String> ntime;
    public static ArrayList<Integer> count;
    public static ArrayList<Integer> pages;
    public static ArrayList<String> cover;

    public PopularTabAdapter(ArrayList<Integer> pid, ArrayList<Integer> uid,
                             ArrayList<String> username, ArrayList<String> tags,
                             ArrayList<String> subject, ArrayList<String> ntime,
                             ArrayList<Integer> count, ArrayList<Integer> pages,
                             ArrayList<String> cover) {
        this.pid = pid;
        this.uid = uid;
        this.username = username;
        this.tags = tags;
        this.subject = subject;
        this.ntime = ntime;
        this.count = count;
        this.pages = pages;
        this.cover = cover;
    }

    @Override
    public PopularTabAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PopularTabAdapter.ViewHolder holder, final int position) {
        holder.txtSubject.setText(subject.get(position));
        holder.txtTags.setText("t/" + tags.get(position));
        holder.txtUsername.setText("u/" + username.get(position));
        holder.txtCommentCount.setText("" + count.get(position));


        if (pages.get(position) == 0) {
            holder.spinner.setItems(1);
        } else {
            ArrayList<Integer> sum_pages = new ArrayList<Integer>();
            for (int i = 0; i < pages.get(position); i++) {
                sum_pages.add(i+1);
            }
            holder.spinner.setItems(sum_pages);
        }

        try {
            if (cover.get(position).contains("localhost")) {
                Picasso.with(holder.itemView.getContext()).load(cover.get(position).replace("http://localhost:3001/", APIsManagement.defaultServer)).into(holder.imgCover);
            } else if (cover.get(position).contains("ir")) {
                Picasso.with(holder.itemView.getContext()).load(R.drawable.defaults).into(holder.imgCover);
            } else {
                Picasso.with(holder.itemView.getContext()).load(cover.get(position)).into(holder.imgCover);
            }
        } catch (IllegalArgumentException e) {
            Picasso.with(holder.itemView.getContext()).load(R.drawable.defaults).into(holder.imgCover);
        }

        Date date= new Date();
        double cal = Math.abs(date.getTime() - Double.parseDouble(ntime.get(position)));
        double minsDiff = Math.ceil(cal / (1000 * 60));
        double hoursDiff = Math.ceil(cal / (1000 * 3600));
        double diffDays = Math.ceil(cal / (1000 * 3600 * 24));

        if (minsDiff-1 < 60) {
            holder.txtTime.setText(((int)minsDiff-1) + "m");
        } else if (hoursDiff-1 < 24) {
            holder.txtTime.setText(((int)hoursDiff-1) + "h");
        } else if (diffDays-1 < 30) {
            holder.txtTime.setText(((int)diffDays-1) + "d");
        } else {
            holder.txtTime.setText(((int)(diffDays-1/30)) + "m");
        }

        holder.spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int p, long id, Object item) {
                Intent intent = new Intent(view.getContext(), ThreadViewActivity.class);
                intent.putExtra("pid", pid.get(position));
                intent.putExtra("page", p+1);
                view.getContext().startActivity(intent);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            int p = holder.getAdapterPosition();
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ThreadViewActivity.class);
                intent.putExtra("pid", pid.get(position));
                intent.putExtra("page", 1);
                v.getContext().startActivity(intent);
            }
        });

        holder.btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent replyIntent = new Intent(v.getContext(), CommentActivity.class);
                replyIntent.putExtra("pid", pid.get(position));
                replyIntent.putExtra("title", subject.get(position));
                v.getContext().startActivity(replyIntent);
            }
        });

        holder.btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, subject.get(position));
                v.getContext().startActivity(Intent.createChooser(sharingIntent, "Share with friends"));
            }
        });

    }

    @Override
    public int getItemCount() {
        return pid.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTags, txtUsername, txtSubject, txtCommentCount, txtTime;
        ImageView imgCover;
        RelativeLayout btnComment, btnShare;
        MaterialSpinner spinner;

        @SuppressLint("WrongViewCast")
        public ViewHolder(View view) {
            super(view);
            spinner = (MaterialSpinner) view.findViewById(R.id.pagesSpinner);
            txtTags = (TextView) view.findViewById(R.id.txtHomeListTags);
            txtUsername = (TextView) view.findViewById(R.id.txtHomeListUsername);
            txtSubject = (TextView) view.findViewById(R.id.txtHomeListTitle);
            txtCommentCount = (TextView) view.findViewById(R.id.txtHomeCommentCount);
            txtTime = (TextView) view.findViewById(R.id.txtHomeListTime);
            imgCover = (ImageView) view.findViewById(R.id.imgHomeListCover);
            btnComment = (RelativeLayout) view.findViewById(R.id.btnHomeComment);
            btnShare = (RelativeLayout) view.findViewById(R.id.btnHomeShare);
        }
    }

}

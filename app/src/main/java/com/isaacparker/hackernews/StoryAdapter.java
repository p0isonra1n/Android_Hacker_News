package com.isaacparker.hackernews;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.StoryViewHolder> {

    private Context context;
    private ArrayList<Story> storyList;

    public StoryAdapter(Context context, ArrayList<Story> storyList) {
        this.context = context;
        this.storyList = storyList;
    }


    @Override
    public int getItemCount() {
        return storyList.size();
    }

    @Override
    public void onBindViewHolder(StoryViewHolder storyViewHolder, int i) {
        Story story = storyList.get(i);
        storyViewHolder.tvTitle.setText(story.title);
        storyViewHolder.tvComment.setText(story.kids.size() + " comments");
        storyViewHolder.tvScore.setText(String.valueOf(story.score));
    }

    @Override
    public StoryViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card, viewGroup, false);

        return new StoryViewHolder(itemView);
    }

    public static class StoryViewHolder extends RecyclerView.ViewHolder{

        protected TextView tvTitle;
        protected TextView tvComment;
        protected TextView tvScore;

        public StoryViewHolder(View v){
            super(v);
            tvTitle = (TextView) v.findViewById(R.id.tvTitle);
            tvComment = (TextView) v.findViewById(R.id.tvComments);
            tvScore = (TextView) v.findViewById(R.id.tvScores);
        }
    }
}

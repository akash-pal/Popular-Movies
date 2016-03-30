package com.example.akashpal.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by akash pal on 3/28/2016.
 */
public class ReviewAdapter extends ArrayAdapter<Review> {


    private LayoutInflater mInflater;
    List<Review> reviewList;
    Context context;

    // Constructors
    public ReviewAdapter(Context context, List<Review> objects) {
        super(context, 0, objects);
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        reviewList = objects;
    }

    @Override
    public Review getItem(int position) {
        return reviewList.get(position);
    }

    private static class ViewHolder {
        public final LinearLayout rootView;
        public final TextView reviewContent;

        private ViewHolder(LinearLayout rootView, TextView reviewContent) {
            this.rootView = rootView;
            this.reviewContent = reviewContent;
        }

        public static ViewHolder create(LinearLayout rootView) {
            TextView reviewContent = (TextView) rootView.findViewById(R.id.reviewContent);
            return new ViewHolder(rootView, reviewContent);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;
        if (convertView == null) {
            View view = mInflater.inflate(R.layout.layout_row_review, parent, false);
            vh = ViewHolder.create((LinearLayout) view);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        Review item = getItem(position);
        vh.reviewContent.setText(item.getContent());

        return vh.rootView;
    }

}


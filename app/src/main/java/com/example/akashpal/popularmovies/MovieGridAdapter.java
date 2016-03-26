package com.example.akashpal.popularmovies;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import java.util.List;

/**
 * Created by akash pal on 3/14/2016.
 */
public class MovieGridAdapter extends BaseAdapter {

    public static final String TAG = MovieGridAdapter.class.getSimpleName();

    private final Context mContext;
    private final LayoutInflater mLayoutInflater;
    private final Movie mLock = new Movie();
    private List<Movie> mObjects;

    MovieGridAdapter(Context context, List<Movie> objects) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mObjects = objects;
    }

    public Context getContext() {
        return mContext;
    }

    public void add(Movie object) {
        synchronized (mLock) {
            mObjects.add(object);
        }
        notifyDataSetChanged();
    }

    public void clear() {
        synchronized (mLock) {
            mObjects.clear();
        }
        notifyDataSetChanged();
    }

    public void setData(List<Movie> data) {
        clear();
        for (Movie movie : data) {
            add(movie);
        }
        Log.v(TAG, mObjects.toString());
    }

    @Override
    public int getCount() {
        return mObjects.size();
    }

    @Override
    public Movie getItem(int position) {
        return mObjects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        ViewHolder viewHolder;

        if (view == null) {
            view = mLayoutInflater.inflate(R.layout.grid_item, parent, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }

        final Movie movie = getItem(position);

        viewHolder = (ViewHolder) view.getTag();

        Uri imageUri = Uri.parse(movie.getPoster_path());
        Picasso.with(getContext()).load(Constants.BASE_URL + imageUri.toString()).into(viewHolder.posterImageView);

        return view;
    }

    private static class ViewHolder {
        public final ImageView posterImageView;

        public ViewHolder(View view) {
            posterImageView = (ImageView) view.findViewById(R.id.posterImageView);
        }
    }

}

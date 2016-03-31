package com.example.akashpal.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.akashpal.popularmovies.model.Movie;

public class MainActivity extends AppCompatActivity implements MainActivityFragment.Callback{

    private boolean mTwoPane;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);


        if (findViewById(R.id.movie_detail_container) != null) {
            mTwoPane = true;
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, new DetailActivityFragment(),
                                DetailActivityFragment.TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
        }

    }

    @Override
    public void onItemSelected(Movie movie) {
        if (mTwoPane) {
            //tablet view
            Bundle arguments = new Bundle();
            arguments.putParcelable(Constants.DETAIL_MOVIE, movie);


            DetailActivityFragment fragment = new DetailActivityFragment();
            fragment.setArguments(arguments);


            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment, DetailActivityFragment.TAG)
                    .commit();
        } else {
            //mobile view
            Intent intent = new Intent(this, DetailActivity.class)
                    .putExtra(Constants.DETAIL_MOVIE, movie);
            startActivity(intent);
        }
    }

    @Override
    public void onFragmentInteraction(String title) {
        getSupportActionBar().setTitle(title);
    }

}

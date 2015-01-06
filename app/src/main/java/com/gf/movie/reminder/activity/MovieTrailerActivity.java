package com.gf.movie.reminder.activity;

import android.os.Bundle;

import com.gf.movie.reminder.R;
import com.gf.movie.reminder.activity.base.BaseTrailerActivity;


public class MovieTrailerActivity extends BaseTrailerActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_trailer);
    }
}

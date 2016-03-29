package com.nanodegree.udacity.movieapp;

/*
* Getting the data (ArrayList) from the previous Activity and displaying the information  about the clicked movie
* */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by nanirajesh on 29-03-2016.
 */
public class Details extends Activity {
    RatingBar ratingBar;
    TextView title;
    TextView description;
    TextView releasedate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);
        Intent i=getIntent();
        Bundle b=i.getBundleExtra("bundle");

        ArrayList<Information> arrayList=(ArrayList<Information>)b.getSerializable("list");
        int position=i.getIntExtra("position", 0);

        Information information=arrayList.get(position);
        ImageView view= (ImageView) findViewById(R.id.imageView);
        ratingBar= (RatingBar) findViewById(R.id.ratingBar);
        title= (TextView) findViewById(R.id.title1);
        description= (TextView) findViewById(R.id.descriptiontext);
        releasedate= (TextView) findViewById(R.id.releasedatetext);
        Picasso.with(this).load(information.getPosterpath()).into(view);
        ratingBar.setNumStars(5);
        float value=(Float.parseFloat(information.getVotes())*5)/10;
        ratingBar.setRating(value);
        Log.e("votes",value+"");
        //Log.d("Votes",information.getVotes()+"");
        ratingBar.setIsIndicator(true);
        releasedate.setText(information.getReleasedate());
        title.setText(information.getTitle());
        description.setText(information.getDescription());
       // Toast.makeText(this,position+"",Toast.LENGTH_LONG).show();
    }
}

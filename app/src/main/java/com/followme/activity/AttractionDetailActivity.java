package com.followme.activity;


import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.followme.bean.Attraction;
import com.followme.lusir.followmeandroid.R;
import com.followme.util.JsonTransform;

import com.squareup.picasso.Picasso;


public class AttractionDetailActivity extends AppCompatActivity {
    private String attractionJson;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attraction_detail);

        ImageView imageView = findViewById(R.id.img_attraction_detail);
        attractionJson = this.getIntent().getExtras().getString("attractionJson");

        Attraction attraction = JsonTransform.attractionJsonToattraction(attractionJson);


        if (attractionJson.length() > 0) {
            String imgURL = attraction.getImageurl();
            Picasso.with(this).load(imgURL).placeholder(R.drawable.ic_localtion).into(imageView);
        }

    }
}

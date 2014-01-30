package com.jmbg.loteriasgmv.view;

import com.jmbg.loteriasgmv.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
 
public class BetFullImageActivity extends Activity {
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bet_full_image_activity);
 
        // get intent data
        Intent i = getIntent();
        // Selected image id
        byte[] image_bytes = i.getExtras().getByteArray("bet_image");
 
        ImageView imageView = (ImageView) findViewById(R.id.full_image_view);
        imageView.setImageBitmap(BitmapFactory.decodeByteArray(
        		image_bytes, 0, image_bytes.length));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }
 
}
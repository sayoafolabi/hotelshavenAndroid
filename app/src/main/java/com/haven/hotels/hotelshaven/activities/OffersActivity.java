package com.haven.hotels.hotelshaven.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.haven.hotels.hotelshaven.R;
import com.haven.hotels.hotelshaven.other.StoredValues;

public class OffersActivity extends AppCompatActivity {


    final Context context = this;
    ImageView hImageView, lImageView, bImageView;
    String url, provider;
    StoredValues storedValues = new StoredValues();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers);




        hImageView = (ImageView) findViewById(R.id.imageViewhcl);
        lImageView = (ImageView) findViewById(R.id.imageViewlrl);
        bImageView = (ImageView) findViewById(R.id.imageViewdeal);



        hImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                url = "www.laterooms.com/en/deals";
                provider = "Laterooms";
                storedValues.store("DealUrl", url);
                storedValues.store("DealProvider", provider);

                Intent intent = new Intent(context, WebViewActivity.class);
                startActivity(intent);

            }
        });
    }

}

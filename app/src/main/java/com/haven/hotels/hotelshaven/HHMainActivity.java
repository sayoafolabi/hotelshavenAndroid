package com.haven.hotels.hotelshaven;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

public class HHMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hhmain);

//        ImageView image=(ImageView)findViewById(R.id.imageView1);
//
//        // Step1 : create the  RotateAnimation object
//        RotateAnimation anim = new RotateAnimation(0f, 350f, 15f, 15f);
//        // Step 2:  Set the Animation properties
//        anim.setInterpolator(new LinearInterpolator());
//        anim.setRepeatCount(Animation.INFINITE);
//        anim.setDuration(1000);
//
//        // Step 3: Start animating the image
//        image.startAnimation(anim);
//
//        // Later. if you want to  stop the animation
//        image.setAnimation(null);

        ((ViewSwitcher) findViewById(R.id.switcher)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ViewSwitcher switcher = (ViewSwitcher) v;

                if (switcher.getDisplayedChild() == 0) {
                    switcher.showNext();
                } else {
                    switcher.showPrevious();
                }
            }
        });

    }
}

package com.haven.hotels.hotelshaven.activities;

/**
 * Created by AfolabSa on 27/03/2017.
 */

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.haven.hotels.hotelshaven.R;
import com.haven.hotels.hotelshaven.other.StoredValues;

public class OfferWebViewActivity extends Activity {

    private WebView webView;
    ImageView imageView;
    TextView splashView;
    StoredValues storedValues = new StoredValues();
    String url, providerChosen, pText;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);

        url = storedValues.get("DealUrl");
        providerChosen = storedValues.get("DealProvider");

        imageView = (ImageView)findViewById(R.id.imgloader);
        webView = (WebView) findViewById(R.id.webView1);
        splashView = (TextView) findViewById(R.id.splashTextView);
        splashView.setText("You are now being redirected to" + providerChosen + "for great savings");



        webView.setWebViewClient(new MyBrowser());
        webView.clearCache(true);
        webView.clearHistory();

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setScrollbarFadingEnabled(false);
        webView.getSettings().setBuiltInZoomControls(true);


        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {

                // imageView.setVisibility(View.GONE);


                splashView.setVisibility(View.GONE);

                webView.setVisibility(View.VISIBLE);
            }

        });



        webView.loadUrl(url);

    }

    @Override
    public void onBackPressed() {
        if(webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

}

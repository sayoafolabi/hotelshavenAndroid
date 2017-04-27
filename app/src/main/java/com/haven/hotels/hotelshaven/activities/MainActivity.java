package com.haven.hotels.hotelshaven.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.haven.hotels.hotelshaven.R;
import com.haven.hotels.hotelshaven.other.DataValidation;
import com.haven.hotels.hotelshaven.other.HhData;
import com.haven.hotels.hotelshaven.other.ProvidersUrl;
import com.haven.hotels.hotelshaven.other.StoredValues;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    final Context context = this;
    private WebView webView;
    private EditText destination, checkinDate, checkoutDate;
    private DatePickerDialog checkinDatePickerDialog, checkoutDatePickerDialog;
    private Spinner adultSpinner, childrenSpinner, roomSpinner;
    private String destinationText, checkinText, checkoutText, adultText,
            childrenText, roomText, url, nights, urlCheckin, cinText, coutText, providerChosen;
    private Date userCheckinDate, userCheckOutDate;
    ImageView bookingImage, hotelscombinedImage, lateroomsImage;


    private SimpleDateFormat dateFormatter, dateFormatterUrl, dateFormat;
    private Calendar newDate, checkin, checkout, cin;

    DataValidation dataValidation = new DataValidation();
    StoredValues storedValues = new StoredValues();
    HhData hhData = new HhData();
    ProvidersUrl providersUrl = new ProvidersUrl();
    TimeZone timeZone = TimeZone.getTimeZone("UTC");


    private Button findHotels;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Configuration config = getResources().getConfiguration();

        if(config.smallestScreenWidthDp <= 320)
        {
            setContentView(R.layout.activity_main_small);
        }
        else if((config.smallestScreenWidthDp > 320) && (config.smallestScreenWidthDp <= 410))
        {
            setContentView(R.layout.activity_main_360);
        }
        else if((config.smallestScreenWidthDp > 410) && (config.smallestScreenWidthDp <= 598))
        {
            setContentView(R.layout.activity_main);
        }
        else if((config.smallestScreenWidthDp > 598) && (config.smallestScreenWidthDp <= 758))
        {
            setContentView(R.layout.activity_main_seveninch);
        }
        else if((config.smallestScreenWidthDp > 758))
        {
            setContentView(R.layout.activity_main_teninch);
        }



        dateFormatter = new SimpleDateFormat("dd MMM yy", Locale.ENGLISH);
        dateFormatterUrl = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

        findViewsById();

        setDateTimeField();

        findHotels.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {

                setDataFromApp();
                getDataFromApp();


                userCheckinDate = dataValidation.convertStringToDate(checkinText);
                userCheckOutDate = dataValidation.convertStringToDate(checkoutText);
                nights = dataValidation.numberOfNights(userCheckinDate, userCheckOutDate);
                urlCheckin = dateFormatterUrl.format(userCheckinDate);
                cinText = dateFormat.format(userCheckinDate);
                coutText = dateFormat.format(userCheckOutDate);

                if(dataValidation.isEmpty(destinationText))
                {
                    showPositiveAlert("Destination", "        Oops! You forgot your destination");
                    return;
                }
                else if(!dataValidation.isTodayOrAfter(userCheckinDate))
                {
                    showPositiveAlert("Checkin Date", "       Checkin date cannot be in the past");
                    return;
                }
                else if(!dataValidation.checkOutDateGreaterThanCheckInDate(userCheckinDate, userCheckOutDate))
                {
                    showPositiveAlert("Checkout Date", "  Checkout date must be after checkin date");
                    return;
                }

                else if(dataValidation.isUkPostcode(destinationText) || dataValidation.isUsPostcode(destinationText))
                {
                    //use laterooms
                    url = providersUrl.getLaterooms(adultText, childrenText, nights, urlCheckin, destinationText);
                    providerChosen = "Laterooms.com";
                    storedValues.store("url", url);
                    storedValues.store("provider", providerChosen);
                    Intent intent = new Intent(context, WebViewActivity.class);
                    startActivity(intent);

                }
                else
                {
                    //use hotelscombined
                    url = providersUrl.getHotelsCombined(destinationText, cinText, coutText, adultText, roomText);
                    providerChosen = "HotelsCombined.com";
                    storedValues.store("url", url);
                    storedValues.store("provider", providerChosen);
                    Intent intent = new Intent(context, WebViewActivity.class);
                    startActivity(intent);
                }



            }
        });

        if((config.smallestScreenWidthDp > 598) && (config.smallestScreenWidthDp <= 758))
        {
            bookingImage = (ImageView) findViewById(R.id.bookingPartner);
            lateroomsImage = (ImageView) findViewById(R.id.lateroomsPartner);
            hotelscombinedImage = (ImageView) findViewById(R.id.hotelscombinedPartner);

            bookingImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    url = "https://www.booking.com/dealspage.en-gb.html";
                    storedValues.store("url", url);
                    providerChosen = "Booking.com";
                    storedValues.store("provider", providerChosen);
                    Intent intent = new Intent(context, WebViewActivity.class);
                    startActivity(intent);
                }
            });

            lateroomsImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    url = "https://www.laterooms.com/en/deals";
                    storedValues.store("url", url);
                    providerChosen = "Laterooms.com";
                    storedValues.store("provider", providerChosen);
                    Intent intent = new Intent(context, WebViewActivity.class);
                    startActivity(intent);
                }
            });

            hotelscombinedImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    url = "https://www.hotelscombined.com/HottestDeals?a_aid=159705";
                    storedValues.store("url", url);
                    providerChosen = "Hotelscombined.com";
                    storedValues.store("provider", providerChosen);
                    Intent intent = new Intent(context, WebViewActivity.class);
                    startActivity(intent);
                }
            });
        }




    }


    private void findViewsById()
    {
        destination = (EditText) findViewById(R.id.destinationedittext);
        webView = (WebView) findViewById(R.id.webView1);

        checkinDate = (EditText) findViewById(R.id.checkinDate);
        checkinDate.setInputType(InputType.TYPE_NULL);
        cin = Calendar.getInstance(timeZone);
        //cin.add(Calendar.DATE, 1);
        checkinDate.setText(dateFormatter.format(cin.getTime()));

        checkoutDate = (EditText) findViewById(R.id.checkoutDate);
        checkoutDate.setInputType(InputType.TYPE_NULL);
        newDate = Calendar.getInstance(timeZone);
        newDate.add(Calendar.DATE, 1);
        checkoutDate.setText(dateFormatter.format(newDate.getTime()));

        adultSpinner = (Spinner) findViewById(R.id.adultSpinner);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.adultGuestArray, R.layout.spinnertext);
        adultSpinner.setAdapter(adapter);

        childrenSpinner = (Spinner) findViewById(R.id.childrenSpinner);
        ArrayAdapter adapter1 = ArrayAdapter.createFromResource(this, R.array.childrenGuestArray, R.layout.spinnertext);
        childrenSpinner.setAdapter(adapter1);

        roomSpinner = (Spinner) findViewById(R.id.roomSpinner);
        ArrayAdapter adapter2 = ArrayAdapter.createFromResource(this, R.array.roomArray, R.layout.spinnertext);
        roomSpinner.setAdapter(adapter2);

        findHotels = (Button) findViewById(R.id.findhotels);
    }

    private void setDateTimeField() {
        checkinDate.setOnClickListener(this);
        checkoutDate.setOnClickListener(this);


        Calendar newCalendar = Calendar.getInstance(timeZone);
        checkinDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                checkin = Calendar.getInstance(timeZone);
                checkin.set(year, monthOfYear, dayOfMonth);
                checkinDate.setText(dateFormatter.format(checkin.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        checkoutDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                checkout = Calendar.getInstance(timeZone);
                checkout.set(year, monthOfYear, dayOfMonth);
                checkoutDate.setText(dateFormatter.format(checkout.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.hh_menu, menu);
        return true;
    }

    @Override
    public void onClick(View view) {
        if(view == checkinDate) {
            checkinDatePickerDialog.show();
        } else if(view == checkoutDate) {
            checkoutDatePickerDialog.show();
        }
    }


    private void setDataFromApp()
    {
        hhData.setDestinationText(destination.getText().toString());
        hhData.setCheckinText(checkinDate.getText().toString());
        hhData.setCheckoutText(checkoutDate.getText().toString());
        hhData.setAdultText(adultSpinner.getSelectedItem().toString());
        hhData.setChildrenText(childrenSpinner.getSelectedItem().toString());
        hhData.setRoomText(roomSpinner.getSelectedItem().toString());
    }

    private void getDataFromApp()
    {
        destinationText =  hhData.getDestinationText().trim();
        checkinText = hhData.getCheckinText().trim();
        checkoutText = hhData.getCheckoutText().trim();
        adultText = hhData.getAdultText().trim();
        childrenText = hhData.getChildrenText().trim();
        roomText = hhData.getRoomText().trim();
    }

    private void showPositiveAlert(String title, String message)
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(title);

        // set dialog message
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setIcon(R.drawable.ic_error_black_18dp)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, close
                        // current activity
                        //MainActivity.this.finish();
                        dialog.cancel();

                    }
                }) ;

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }


    private void showAlert(String title, String message)
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(title);

        // set dialog message
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setIcon(R.drawable.ic_error_black_18dp)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, close
                        // current activity
                        //MainActivity.this.finish();
                        dialog.cancel();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        switch (item.getItemId())
        {
            case R.id.aboutus:
                Intent intentAbout = new Intent(context, AboutusActivity.class);
                startActivity(intentAbout);
                return true;

            case R.id.cookie:
                Intent intentCookie = new Intent(context, CookieActivity.class);
                startActivity(intentCookie);
                return true;

            case R.id.privacy:
                Intent intentPrivacy = new Intent(context, PrivacyPolicyActivity.class);
                startActivity(intentPrivacy);
                return true;

            case R.id.term:
                Intent intentTerm = new Intent(context, TermsActivity.class);
                startActivity(intentTerm);
                return true;

//            case R.id.blog:
//                return true;
//            case R.id.offers:
//                Intent intentOffers = new Intent(context, OffersActivity.class);
//                startActivity(intentOffers);
//                return true;

            case R.id.home:
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

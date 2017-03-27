package com.haven.hotels.hotelshaven;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.View;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    final Context context = this;
    private EditText destination, checkinDate, checkoutDate;
    private DatePickerDialog checkinDatePickerDialog, checkoutDatePickerDialog;
    private Spinner adultSpinner, childrenSpinner, roomSpinner;
    private String destinationText, checkinText, checkoutText, adultText,
            childrenText, roomText, url, nights, urlCheckin, cinText, coutText, providerChosen;
    private Date userCheckinDate, userCheckOutDate;


    private SimpleDateFormat dateFormatter, dateFormatterUrl, dateFormat;
    private Calendar newDate, checkin, checkout, cin;

    DataValidation dataValidation = new DataValidation();
    StoredValues storedValues = new StoredValues();
    HhData hhData = new HhData();
    ProvidersUrl providersUrl = new ProvidersUrl();

    private Button findHotels;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dateFormatter = new SimpleDateFormat("dd MMM yy");
        dateFormatterUrl = new SimpleDateFormat("yyyyMMdd");
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");



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
                    showPositiveAlert("Destination", "Please enter a destination");
                }
                else if(!dataValidation.isTodayOrAfter(userCheckinDate))
                {
                    showPositiveAlert("Checkin Date", "Please have a second look at checkin date");
                }
                else if(!dataValidation.checkOutDateGreaterThanCheckInDate(userCheckinDate, userCheckOutDate))
                {
                    showPositiveAlert("Checkout Date", "Checkout date must be after checkin date");
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




    }


    private void findViewsById()
    {
        destination = (EditText) findViewById(R.id.destinationedittext);


        checkinDate = (EditText) findViewById(R.id.checkinDate);
        checkinDate.setInputType(InputType.TYPE_NULL);
        cin = Calendar.getInstance();
        //cin.add(Calendar.DATE, 1);
        checkinDate.setText(dateFormatter.format(cin.getTime()));

        checkoutDate = (EditText) findViewById(R.id.checkoutDate);
        checkoutDate.setInputType(InputType.TYPE_NULL);
        newDate = Calendar.getInstance();
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

        Calendar newCalendar = Calendar.getInstance();
        checkinDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                checkin = Calendar.getInstance();
                checkin.set(year, monthOfYear, dayOfMonth);
                checkinDate.setText(dateFormatter.format(checkin.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        checkoutDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                checkout = Calendar.getInstance();
                checkout.set(year, monthOfYear, dayOfMonth);
                checkoutDate.setText(dateFormatter.format(checkout.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
        destinationText =  hhData.getDestinationText();
        checkinText = hhData.getCheckinText();
        checkoutText = hhData.getCheckoutText();
        adultText = hhData.getAdultText();
        childrenText = hhData.getChildrenText();
        roomText = hhData.getRoomText();
    }

    private void showPositiveAlert(String title, String message)
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(title);

        // set dialog message
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
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


}

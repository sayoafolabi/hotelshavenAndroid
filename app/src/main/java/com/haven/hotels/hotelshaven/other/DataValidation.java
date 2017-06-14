package com.haven.hotels.hotelshaven.other;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by AfolabSa on 25/03/2017.
 */

public class DataValidation
{

    TimeZone timeZone = TimeZone.getTimeZone("UTC");

    final Pattern UK_POSTCODE = Pattern.compile("^(([gG][iI][rR] {0,}0[aA]{2})|((([a-pr-uwyzA-PR-UWYZ][a-hk-yA-HK-Y]?[0-9][0-9]?)" +
            "|(([a-pr-uwyzA-PR-UWYZ][0-9][a-hjkstuwA-HJKSTUW])" +
            "|([a-pr-uwyzA-PR-UWYZ][a-hk-yA-HK-Y][0-9][abehmnprv-yABEHMNPRV-Y]))) " +
            "{0,}[0-9][abd-hjlnp-uw-zABD-HJLNP-UW-Z]{2}))$");

    final Pattern US_POSTCODE = Pattern.compile("^[0-9]{5}(?:-[0-9]{4})?$");

    Date today, parseDate;
    SimpleDateFormat dateFormat;


    public boolean isEmpty(String data)
    {
        return data.trim().isEmpty();
    }

    public boolean isUkPostcode(String data)
    {
        Matcher m = UK_POSTCODE.matcher(data);
        if(!m.matches())
        {
            return false;
        }

        return true;
    }

    public boolean isUsPostcode(String data)
    {
        Matcher m = US_POSTCODE.matcher(data);
        if(!m.matches())
        {
            return false;
        }

        return true;
    }

    public Date getToday()
    {
        dateFormat = new SimpleDateFormat("dd MMM yy", Locale.ENGLISH);

        try
        {
            today = dateFormat.parse(dateFormat.format(new Date()));

        } catch (ParseException e)
        {
            e.printStackTrace();
        }
        return today;
    }

    public boolean isTodayOrAfter(Date date)
    {
        return (date.equals(getToday())) || (date.after(getToday()));

    }

    public boolean checkOutDateGreaterThanCheckInDate(Date checkin, Date checkOut)
    {
        return checkOut.after(checkin);
    }

    public String numberOfNights(Date checkin, Date checkOut)
    {

        Calendar cin = Calendar.getInstance(timeZone);
        Calendar cout = Calendar.getInstance(timeZone);

        cin.setTime(checkin);
        cout.setTime(checkOut);

        int formate = cout.get(Calendar.DAY_OF_MONTH) - cin.get(Calendar.DAY_OF_MONTH);

        return Integer.toString(formate);
    }

    public Date convertStringToDate(String dateString)
    {
        DateFormat dateFormat = new SimpleDateFormat("yyyy");
        String year = dateFormat.format(new Date());

        dateString = dateString + " " + year;

        dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);

        try
        {
            parseDate = dateFormat.parse(dateString);
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        return parseDate;
    }
}

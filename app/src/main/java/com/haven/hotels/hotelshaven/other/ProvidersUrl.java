package com.haven.hotels.hotelshaven.other;

/**
 * Created by AfolabSa on 26/03/2017.
 */

public class ProvidersUrl
{
    private String hotelsCombined;
    private String booking;
    private String laterooms;


    public String getHotelsCombined(String destination, String checkin, String checkout, String adult, String room)
    {
        if(destination.contains(" "))
        {
            destination = destination.replace(" ", "_");
        }

        String first, second, third, fourth, five, sixth, seventh;

        first = "https://www.hotelscombined.com/Hotels/Search?a_aid=159705&brandid=481068&languageCode=EN&destination=place%3A";

        second = destination + "&radius=30mi&";

        third = "checkin=" + checkin + "&";

        fourth = "checkout=" + checkout +"&";

        five = "Rooms=" + room + "&";

        sixth = "adults_1=" + adult;

        seventh = "sort=Popularity-desc&showSoldOut=false";

        hotelsCombined = first + second + third + fourth + five + sixth + seventh;

        return hotelsCombined;
    }

    public String getHotelsCombinedWithChildren(String destination, String checkin, String checkout, String adult, String childAge1, String childAge2, String room)
    {
        if(destination.contains(" "))
        {
            destination = destination.replace(" ", "_");
        }

        hotelsCombined = "https://www.hotelscombined.com/?a_aid=159705&brandid=481068&languageCode=EN/Hotels/Search?a_aid=159705&" +
                "destination=place%3A%s&radius=30mi&" +
                "checkin=%s&" +
                "checkout=%s&" +
                "Rooms=%s&" +
                "adults_1=%s" +
                "&childAges_1=%s%2C%s" +
                "&pageSize=15&pageIndex=0&sort=Popularity-desc&showSoldOut=false";

        hotelsCombined = String.format(hotelsCombined, destination, checkin, checkout, room, adult, childAge1, childAge2);

        return hotelsCombined;
    }

    public String getLaterooms(String adult, String children, String nights, String checkin, String destination)
    {
        if(destination.contains(" "))
        {
            destination = destination.replace(" ", "+");
        }

        laterooms = "https://www.laterooms.com/en/p16107/Hotels.aspx?" +
                "rt=%s-%s&" +
                "rt-adult=%s&" +
                "rt-child=%s&" +
                "n=%s&" +
                "d=%s&" +
                "k=%s";

        laterooms = String.format(laterooms, adult, children, adult, children, nights, checkin, destination);

        return laterooms;
    }
}

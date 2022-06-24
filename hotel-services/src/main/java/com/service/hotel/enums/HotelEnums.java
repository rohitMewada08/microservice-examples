package com.service.hotel.enums;

public class HotelEnums {

    public enum Services{
        FREE_WIFI("Wifi"), DRINKING_WATER("Drinking Water"),
        BREAKFAST("Breakfast"), DINNER("Dinner"), CAB("Cab"),
        LIFT("Lift"), GEYSER("Geyser"), PARKING("parking"),
        ROOM_SERVICE("Room service"), PARKING_PAID("Parking paid"),
        GARDEN("Garden"), SWIM_POOL("Swimming pool");

        private String label;
        private Services(String label){
            this.label = label;
        }
    }

    public enum RoomType {
        QUEEN("Queen"),
        KING("King"),
        TWIN("Twin"),
        HOLLYWOOD_TWIN("Hollywood Twin"),
        DOUBLE_DOUBLE("Double Double"),
        STUDIO("Studio");

        String label;
        RoomType(String label){
            this.label = label;
        }
    }


}

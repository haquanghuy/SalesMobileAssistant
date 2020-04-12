package com.doannganh.salesmobileassistant.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class DirectionFinder {

    public static class Distance {
        public String text;
        public int value;

        public Distance(String text, int value) {
            this.text = text;
            this.value = value;
        }
    }

    public static class Duration {
        public String text;
        public int value;

        public Duration(String text, int value) {
            this.text = text;
            this.value = value;
        }
    }

    public static class Route {
        public Distance distance;
        public Duration duration;
        public String endAddress;
        public LatLng endLocation;
        public String startAddress;
        public LatLng startLocation;

        public List<LatLng> points;
    }
}

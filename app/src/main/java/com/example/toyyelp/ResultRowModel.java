package com.example.toyyelp;

public class ResultRowModel {
    String serialNumber;
    String businessImageUrl;
    String businessName;
    String rating;
    String distance;
    String id;

    public ResultRowModel(String serialNumber, String businessImage, String businessName, String rating, String distance, String id) {
        this.serialNumber = serialNumber;
        this.businessImageUrl = businessImage;
        this.businessName = businessName;
        this.rating = rating;
        this.distance = distance;
        this.id = id;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public String getBusinessImageUrl() {
        return businessImageUrl;
    }

    public String getBusinessName() {
        return businessName;
    }

    public String getRating() {
        return rating;
    }

    public String getDistance() {
        return distance;
    }

    public String getId() {
        return id;
    }
}

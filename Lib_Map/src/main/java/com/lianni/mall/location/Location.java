package com.lianni.mall.location;


/**
 * Created by LZ on 2016/8/22.
 * 城市
 */
public class Location implements Cloneable {

    private double latitude;
    private double longitude;
    private String address;
    private String name;
    private String city;
    private String cityCode;

    public Location() {
    }
    
    public String getCityCode() {
        return cityCode;
    }
    
    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }
    
    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void clone(Location currentLocation) {
        setAddress(currentLocation.getAddress());
        setName(currentLocation.getName());
        setLongitude(currentLocation.getLongitude());
        setLatitude(currentLocation.getLatitude());
        setCity(currentLocation.getCity());
        setCityCode(currentLocation.getCityCode());
    }
}

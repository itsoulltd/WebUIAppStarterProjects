package com.infoworks.domain.models;

import ch.hsr.geohash.GeoHash;
import ch.hsr.geohash.WGS84Point;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.infoworks.domain.entities.Persistable;

import java.util.List;

public class OStreetGeocode extends Persistable<String, Long> {

    private String place_id;
    private String licence;
    private String osm_type;
    private String lat;
    private String lon;
    private String display_name;
    private String type;
    private double importance;
    private List<String> boundingbox;

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public String getLicence() {
        return licence;
    }

    public void setLicence(String licence) {
        this.licence = licence;
    }

    public String getOsm_type() {
        return osm_type;
    }

    public void setOsm_type(String osm_type) {
        this.osm_type = osm_type;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getImportance() {
        return importance;
    }

    public void setImportance(double importance) {
        this.importance = importance;
    }

    public List<String> getBoundingbox() {
        return boundingbox;
    }

    public void setBoundingbox(List<String> boundingbox) {
        this.boundingbox = boundingbox;
    }

    @JsonIgnore
    public WGS84Point getPoint(){
        return new WGS84Point(Double.valueOf(getLat()), Double.valueOf(getLon()));
    }

    @JsonIgnore
    public String getGeoHash() {
        WGS84Point point = getPoint();
        String geoHash = GeoHash.geoHashStringWithCharacterPrecision(point.getLatitude()
                , point.getLongitude()
                , 12);
        return geoHash;
    }
}

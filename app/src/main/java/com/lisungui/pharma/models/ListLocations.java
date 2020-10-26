package com.lisungui.pharma.models;

import java.util.ArrayList;

/**
 * Created by khrishi on 16/12/16.
 */
public class ListLocations {

    int status;
    ArrayList<LatLongPojo> pharmacy = new ArrayList<>();

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ArrayList<LatLongPojo> getPharmacy() {
        return pharmacy;
    }

    public void setPharmacy(ArrayList<LatLongPojo> pharmacy) {
        this.pharmacy = pharmacy;
    }
}

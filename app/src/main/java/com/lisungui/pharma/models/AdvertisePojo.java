package com.lisungui.pharma.models;

import java.util.ArrayList;

/**
 * Created by prometteur-1 on 20/1/18.
 */

public class AdvertisePojo {

    int status;

    public ArrayList<Advertise> getAdvertise() {
        return advertise;
    }

    public void setAdvertise(ArrayList<Advertise> advertise) {
        this.advertise = advertise;
    }

    ArrayList<Advertise> advertise = new ArrayList<>();

}

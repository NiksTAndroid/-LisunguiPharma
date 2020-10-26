package com.lisungui.pharma.models;

import java.util.ArrayList;

/**
 * Created by khrishi on 1/1/17.
 */
public class ListMedicine {

    private int status;// medCount;
    private ArrayList<MedicinePojo> find_medicine = new ArrayList<>();

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    /*public int getMedCount() {
        return medCount;
    }
*/
    /*public void setMedCount(int medCount) {
        this.medCount = medCount;
    }*/

    public ArrayList<MedicinePojo> getMedData() {
        return find_medicine;
    }

    public void setMedData(ArrayList<MedicinePojo> medData) {
        this.find_medicine = medData;
    }
}

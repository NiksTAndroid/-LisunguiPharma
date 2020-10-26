package com.lisungui.pharma.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by khrishi on 24/12/16.
 */
public class TreatmentModel implements Serializable {

    private int id;
    private String med_name;
    private String img_source;
    private String toggle;
    private String description;
    private ArrayList<Alarm> arrayList = new ArrayList<>();

    public ArrayList<Alarm> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<Alarm> arrayList) {
        this.arrayList = arrayList;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getToggle() {
        return toggle;
    }

    public void setToggle(String toggle) {
        this.toggle = toggle;
    }

    public String getMed_name() {
        return med_name;
    }

    public void setMed_name(String med_name) {
        this.med_name = med_name;
    }

    public String getImg_source() {
        return img_source;
    }

    public void setImg_source(String img_source) {
        this.img_source = img_source;
    }


    /*public TreatmentModel () {}

    protected TreatmentModel(Parcel in) {

        id = in.readInt();
        med_name = in.readString();
        img_source = in.readString();
        toggle = in.readString();
        description = in.readString();
        arrayList = in.readArrayList(null);
    }

    public static final Creator<TreatmentModel> CREATOR = new Creator<TreatmentModel>() {
        @Override
        public TreatmentModel createFromParcel(Parcel in) {
            return new TreatmentModel(in);
        }

        @Override
        public TreatmentModel[] newArray(int size) {
            return new TreatmentModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(id);
        dest.writeString(med_name);
        dest.writeString(img_source);
        dest.writeString(toggle);
        dest.writeString(description);
        dest.writeList(arrayList);
    }*/
}
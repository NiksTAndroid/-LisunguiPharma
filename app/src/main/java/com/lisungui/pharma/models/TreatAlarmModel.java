package com.lisungui.pharma.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by khrishi on 24/12/16.
 */
public class TreatAlarmModel implements Parcelable {

    private int id;
    private String med_name;
    private String from_day;
    private String to_day;
    private String time;
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

    public String getFrom_day() {
        return from_day;
    }

    public void setFrom_day(String from_day) {
        this.from_day = from_day;
    }

    public String getTo_day() {
        return to_day;
    }

    public void setTo_day(String to_day) {
        this.to_day = to_day;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImg_source() {
        return img_source;
    }

    public void setImg_source(String img_source) {
        this.img_source = img_source;
    }


    public TreatAlarmModel () {}

    protected TreatAlarmModel(Parcel in) {

        id = in.readInt();
        med_name = in.readString();
        from_day = in.readString();
        to_day = in.readString();
        time = in.readString();
        img_source = in.readString();
        toggle = in.readString();
        description = in.readString();
        arrayList = in.readArrayList(null);

    }

    public static final Creator<TreatAlarmModel> CREATOR = new Creator<TreatAlarmModel>() {
        @Override
        public TreatAlarmModel createFromParcel(Parcel in) {
            return new TreatAlarmModel(in);
        }

        @Override
        public TreatAlarmModel[] newArray(int size) {
            return new TreatAlarmModel[size];
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
        dest.writeString(from_day);
        dest.writeString(to_day);
        dest.writeString(time);
        dest.writeString(img_source);
        dest.writeString(toggle);
        dest.writeString(description);
        dest.writeList(arrayList);
    }
}

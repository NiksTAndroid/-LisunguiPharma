package com.lisungui.pharma.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CountryMedicinesPojo {

    @SerializedName("medicine_country")
    @Expose
    private List<CountryMedicineList> medicineCountry = null;
    @SerializedName("status")
    @Expose
    private Integer status;

    public List<CountryMedicineList> getMedicineCountry() {
        return medicineCountry;
    }

    public void setMedicineCountry(List<CountryMedicineList> medicineCountry) {
        this.medicineCountry = medicineCountry;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}

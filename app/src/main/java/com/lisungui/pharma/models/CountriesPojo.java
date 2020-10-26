package com.lisungui.pharma.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CountriesPojo {

        @SerializedName("country")
        @Expose
        private List<CountryPojoArray> country = null;
        @SerializedName("status")
        @Expose
        private Integer status;

        public List<CountryPojoArray> getCountry() {
            return country;
        }

        public void setCountry(List<CountryPojoArray> country) {
            this.country = country;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

    }




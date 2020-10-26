package com.lisungui.pharma.models;

import java.util.ArrayList;

/**
 * Created by administrator on 16/1/17.
 */

public class DownloadOrderDetailsModel {

    private int order_server_id;
    private int order_qnty;
    private double order_total_price;
    private String order_date;
    private String order_track_status;
    private String order_update_date;

    ArrayList<OrderDataModel> order_data = new ArrayList<>();

    public int getOrder_server_id() {
        return order_server_id;
    }

    public void setOrder_server_id(int order_server_id) {
        this.order_server_id = order_server_id;
    }

    public int getOrder_qnty() {
        return order_qnty;
    }

    public void setOrder_qnty(int order_qnty) {
        this.order_qnty = order_qnty;
    }

    public double getOrder_total_price() {
        return order_total_price;
    }

    public void setOrder_total_price(double order_total_price) {
        this.order_total_price = order_total_price;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getOrder_track_status() {
        return order_track_status;
    }

    public void setOrder_track_status(String order_track_status) {
        this.order_track_status = order_track_status;
    }

    public String getOrder_update_date() {
        return order_update_date;
    }

    public void setOrder_update_date(String order_update_date) {
        this.order_update_date = order_update_date;
    }

    public ArrayList<OrderDataModel> getOrder_data() {
        return order_data;
    }

    public void setOrder_data(ArrayList<OrderDataModel> order_data) {
        this.order_data = order_data;
    }

    public class OrderDataModel {

        private int detail_id;
        private int detail_order_id;
        private int detail_med_id;
        private double detail_med_price;
        private int detail_med_qnty;
        private String med_name;

        public int getDetail_id() {
            return detail_id;
        }

        public void setDetail_id(int detail_id) {
            this.detail_id = detail_id;
        }

        public int getDetail_order_id() {
            return detail_order_id;
        }

        public void setDetail_order_id(int detail_order_id) {
            this.detail_order_id = detail_order_id;
        }

        public int getDetail_med_id() {
            return detail_med_id;
        }

        public void setDetail_med_id(int detail_med_id) {
            this.detail_med_id = detail_med_id;
        }

        public double getDetail_med_price() {
            return detail_med_price;
        }

        public void setDetail_med_price(double detail_med_price) {
            this.detail_med_price = detail_med_price;
        }

        public int getDetail_med_qnty() {
            return detail_med_qnty;
        }

        public void setDetail_med_qnty(int detail_med_qnty) {
            this.detail_med_qnty = detail_med_qnty;
        }

        public String getMed_name() {
            return med_name;
        }

        public void setMed_name(String med_name) {
            this.med_name = med_name;
        }
    }
}

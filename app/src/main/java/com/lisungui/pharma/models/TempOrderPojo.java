package com.lisungui.pharma.models;

/**
 * Created by siddeshwar on 12/1/17.
 */
public class TempOrderPojo {

    private int temp_order_id;
    private int temp_order_med_id;
    private String temp_order_med_name;
    private double temp_order_med_price;
    private double temp_order_total_price;
    private int temp_order_qty;

    public int getTemp_qty() {
        return temp_qty;
    }

    public void setTemp_qty(int temp_qty) {
        this.temp_qty = temp_qty;
    }

    private int temp_qty;


    public String getTemp_pharma_id() {
        return temp_pharma_id;
    }

    public void setTemp_pharma_id(String temp_pharma_id) {
        this.temp_pharma_id = temp_pharma_id;
    }

    private String temp_pharma_id;

    public int getTemp_order_id() {
        return temp_order_id;
    }

    public void setTemp_order_id(int temp_order_id) {
        this.temp_order_id = temp_order_id;
    }

    public int getTemp_order_med_id() {
        return temp_order_med_id;
    }

    public void setTemp_order_med_id(int temp_order_med_id) {
        this.temp_order_med_id = temp_order_med_id;
    }

    public String getTemp_order_med_name() {
        return temp_order_med_name;
    }

    public void setTemp_order_med_name(String temp_order_med_name) {
        this.temp_order_med_name = temp_order_med_name;
    }

    public double getTemp_order_med_price() {
        return temp_order_med_price;
    }

    public void setTemp_order_med_price(double temp_order_med_price) {
        this.temp_order_med_price = temp_order_med_price;
    }

    public double getTemp_order_total_price() {
        return temp_order_total_price;
    }

    public void setTemp_order_total_price(double temp_order_total_price) {
        this.temp_order_total_price = temp_order_total_price;
    }

    public int getTemp_order_qty() {
        return temp_order_qty;
    }

    public void setTemp_order_qty(int temp_order_qty) {
        this.temp_order_qty = temp_order_qty;
    }
}

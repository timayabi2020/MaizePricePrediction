/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neuralnets.research;

/**
 *
 * @author tmwamalwa
 */
public class Predictions {
    private String predicted;
    private String actual;
    private String error;
    private String mape;
    private String date;

    public String getPredicted() {
        return predicted;
    }

    public void setPredicted(String predicted) {
        this.predicted = predicted;
    }

    public String getActual() {
        return actual;
    }

    public void setActual(String actual) {
        this.actual = actual;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMape() {
        return mape;
    }

    public void setMape(String mape) {
        this.mape = mape;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    
    
}

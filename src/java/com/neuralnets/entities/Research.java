/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neuralnets.entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author tmwamalwa
 */
@Entity
public class Research implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String bananasprice;
    private String riceprice;
    private String wheatprice;
    private String rainfall;
    private String predictedvalue;
    private String inflation;
    private String maizeproduction;
    private String year;
    private String maizeprice;
    private String beanprice;
    private String politics;
    private String maizeimport;
    private String priceinkg;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Research)) {
            return false;
        }
        Research other = (Research) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.neuralnets.entities.Research[ id=" + id + " ]";
    }

    public String getBananasprice() {
        return bananasprice;
    }

    public void setBananasprice(String bananasprice) {
        this.bananasprice = bananasprice;
    }

  

    public String getRiceprice() {
        return riceprice;
    }

    public void setRiceprice(String riceprice) {
        this.riceprice = riceprice;
    }

    public String getWheatprice() {
        return wheatprice;
    }

    public void setWheatprice(String wheatprice) {
        this.wheatprice = wheatprice;
    }

    public String getRainfall() {
        return rainfall;
    }

    public void setRainfall(String rainfall) {
        this.rainfall = rainfall;
    }

    public String getPredictedvalue() {
        return predictedvalue;
    }

    public void setPredictedvalue(String predictedvalue) {
        this.predictedvalue = predictedvalue;
    }

    public String getInflation() {
        return inflation;
    }

    public void setInflation(String inflation) {
        this.inflation = inflation;
    }

    public String getMaizeproduction() {
        return maizeproduction;
    }

    public void setMaizeproduction(String maizeproduction) {
        this.maizeproduction = maizeproduction;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMaizeprice() {
        return maizeprice;
    }

    public void setMaizeprice(String maizeprice) {
        this.maizeprice = maizeprice;
    }

    public String getBeanprice() {
        return beanprice;
    }

    public void setBeanprice(String beanprice) {
        this.beanprice = beanprice;
    }

    public String getPolitics() {
        return politics;
    }

    public void setPolitics(String politics) {
        this.politics = politics;
    }

    public String getMaizeimport() {
        return maizeimport;
    }

    public void setMaizeimport(String maizeimport) {
        this.maizeimport = maizeimport;
    }

    public String getPriceinkg() {
        return priceinkg;
    }

    public void setPriceinkg(String priceinkg) {
        this.priceinkg = priceinkg;
    }
    
}

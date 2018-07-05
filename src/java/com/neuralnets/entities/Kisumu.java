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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 *
 * @author tmwamalwa
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "Kisumu.findAll", query = "SELECT n FROM Kisumu n"),
    @NamedQuery(name = "Kisumu.findById", query = "SELECT n FROM Kisumu n WHERE n.id = :id"),
    @NamedQuery(name = "Kisumu.findByData", query = "SELECT n FROM Kisumu n WHERE n.data = :data"),
    @NamedQuery(name = "Kisumu.findByPredictedvalue", query = "SELECT n FROM Kisumu n WHERE n.predictedvalue = :predictedvalue"),
    @NamedQuery(name = "Kisumu.findByDate", query = "SELECT n FROM Kisumu n WHERE n.date = :date")})
public class Kisumu implements Serializable {
    private String data;
    private String predictedvalue;
    private String date;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

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
        if (!(object instanceof Kisumu)) {
            return false;
        }
        Kisumu other = (Kisumu) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.neuralnets.entities.Kisumu[ id=" + id + " ]";
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getPredictedvalue() {
        return predictedvalue;
    }

    public void setPredictedvalue(String predictedvalue) {
        this.predictedvalue = predictedvalue;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neuralnets.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author tmwamalwa
 */
@Entity
@Table(name = "nairobi")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Nairobi.findAll", query = "SELECT n FROM Nairobi n"),
    @NamedQuery(name = "Nairobi.findById", query = "SELECT n FROM Nairobi n WHERE n.id = :id"),
    @NamedQuery(name = "Nairobi.findByData", query = "SELECT n FROM Nairobi n WHERE n.data = :data"),
    @NamedQuery(name = "Nairobi.findByPredictedvalue", query = "SELECT n FROM Nairobi n WHERE n.predictedvalue = :predictedvalue"),
    @NamedQuery(name = "Nairobi.findByDate", query = "SELECT n FROM Nairobi n WHERE n.date = :date")})
public class Nairobi implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 200)
    @Column(name = "data")
    private String data;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "predictedvalue")
    private String predictedvalue;
    @Size(max = 200)
    @Column(name = "date")
    private String date;

    public Nairobi() {
    }

    public Nairobi(Integer id) {
        this.id = id;
    }

    public Nairobi(Integer id, String predictedvalue) {
        this.id = id;
        this.predictedvalue = predictedvalue;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Nairobi)) {
            return false;
        }
        Nairobi other = (Nairobi) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.neuralnets.entities.Nairobi[ id=" + id + " ]";
    }
    
}

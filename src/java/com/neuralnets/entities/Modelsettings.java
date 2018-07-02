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
public class Modelsettings implements Serializable {
    private int trainingdata;
    private int testingdata;
    private double learningrate;
    private int maxiterations;
    private double maxerror;
    private double normalizer;
    private int neurons;
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
        if (!(object instanceof Modelsettings)) {
            return false;
        }
        Modelsettings other = (Modelsettings) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.neuralnets.entities.modelsettings[ id=" + id + " ]";
    }

    public int getTrainingdata() {
        return trainingdata;
    }

    public void setTrainingdata(int trainingdata) {
        this.trainingdata = trainingdata;
    }

    public int getTestingdata() {
        return testingdata;
    }

    public void setTestingdata(int testingdata) {
        this.testingdata = testingdata;
    }

    public double getLearningrate() {
        return learningrate;
    }

    public void setLearningrate(double learningrate) {
        this.learningrate = learningrate;
    }

    public int getMaxiterations() {
        return maxiterations;
    }

    public void setMaxiterations(int maxiterations) {
        this.maxiterations = maxiterations;
    }

    public double getMaxerror() {
        return maxerror;
    }

    public void setMaxerror(double maxerror) {
        this.maxerror = maxerror;
    }

    public double getNormalizer() {
        return normalizer;
    }

    public void setNormalizer(double normalizer) {
        this.normalizer = normalizer;
    }

    public int getNeurons() {
        return neurons;
    }

    public void setNeurons(int neurons) {
        this.neurons = neurons;
    }
    
}

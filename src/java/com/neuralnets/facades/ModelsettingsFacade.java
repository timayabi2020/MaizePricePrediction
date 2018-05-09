/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neuralnets.facades;

import com.neuralnets.entities.Modelsettings;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author tmwamalwa
 */
@Stateless
public class ModelsettingsFacade extends AbstractFacade<Modelsettings> {

    @PersistenceContext(unitName = "MaizePricePredictionPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ModelsettingsFacade() {
        super(Modelsettings.class);
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neuralnets.facades;

import com.neuralnets.entities.Eldoret;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author tmwamalwa
 */
@Stateless
public class EldoretFacade extends AbstractFacade<Eldoret> {

    @PersistenceContext(unitName = "MaizePricePredictionPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EldoretFacade() {
        super(Eldoret.class);
    }
    
}

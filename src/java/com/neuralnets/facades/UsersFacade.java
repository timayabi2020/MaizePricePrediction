/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neuralnets.facades;

import com.neuralnets.entities.Users;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author tmwamalwa
 */
@Stateless
public class UsersFacade extends AbstractFacade<Users> {

    @PersistenceContext(unitName = "MaizePricePredictionPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UsersFacade() {
        super(Users.class);
    }
     public Users fetchUserByUsername(String username) {
       Users user = null;
        
        Query query = getEntityManager().createNamedQuery("fetchUser2");
        query.setParameter("uname2", username);
        
        if(!query.getResultList().isEmpty()){
            user = (Users)query.getSingleResult();
        }
        
        return user;
    }
}

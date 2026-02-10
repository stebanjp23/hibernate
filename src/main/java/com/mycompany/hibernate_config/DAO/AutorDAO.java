/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.hibernate_config.DAO;


import com.mycompany.hibernate_config.Autor;
import jakarta.persistence.EntityManager;

/**
 *
 * @author juane
 */
public class AutorDAO {

    private EntityManager em;

    public AutorDAO(EntityManager em) {
        this.em = em;
    }

    public void crear(Autor autor) {
        em.getTransaction().begin();
        em.persist(autor);
        em.getTransaction().commit();
    }
}

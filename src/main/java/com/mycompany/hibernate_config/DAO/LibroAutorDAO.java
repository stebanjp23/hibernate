/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.hibernate_config.DAO;

import com.mycompany.hibernate_config.Autor;
import com.mycompany.hibernate_config.Libro;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import java.util.List;

/**
 *
 * @author juane
 */
public class LibroAutorDAO {

    private EntityManager em;

    public LibroAutorDAO(EntityManager em) {
        this.em = em;
    }

    public void crear(LibroAutorDAO libro) {
        em.getTransaction().begin();
        em.persist(libro);
        em.getTransaction().commit();
    }

    public Autor buscarAutor(int id) {
        return em.find(Autor.class, id);
    }

    public Autor buscarAutor_nom(String nombre) {
        try {
            return em.createQuery(
                    "SELECT a FROM Autor a WHERE a.nombre = :nombre",
                    Autor.class
            )
                    .setParameter("nombre", nombre)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    public List<Libro> listar_libro_autor(int id) {
        return em.createQuery(
                "SELECT l FROM Libro l JOIN l.autorList a WHERE a.id = :autorId",
                Libro.class)
                .setParameter("autorId", id)
                .getResultList();
    }

}

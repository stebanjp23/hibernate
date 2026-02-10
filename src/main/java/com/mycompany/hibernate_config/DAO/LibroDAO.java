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
public class LibroDAO {

    private EntityManager em;

    public LibroDAO(EntityManager em) {
        this.em = em;
    }

    public void crear(Libro libro) {
        em.getTransaction().begin();
        em.persist(libro);
        em.getTransaction().commit();
    }

    public Libro buscarPorId(int id) {
        return em.find(Libro.class, id);
    }

    public Libro buscarPornom(String nombre) {
        try {
            return em.createQuery(
                    "SELECT l FROM Libro l WHERE l.titulo = :titulo",
                    Libro.class
            )
                    .setParameter("titulo", nombre)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<Libro> listarTodos() {
        return em.createQuery("SELECT l FROM Libro l", Libro.class).getResultList();
    }

    public void actualizar(Libro libro) {
        em.getTransaction().begin();
        em.merge(libro);
        em.getTransaction().commit();
    }

    public void borrar(Libro libro) {
        em.getTransaction().begin();

        Libro managed = em.merge(libro);

        // romper relación ManyToMany
        managed.removeAutores();

        em.remove(managed);

        em.getTransaction().commit();
        em.close();
    }
}

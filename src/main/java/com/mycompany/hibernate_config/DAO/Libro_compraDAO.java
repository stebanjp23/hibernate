package com.mycompany.hibernate_config.DAO;

import com.mycompany.hibernate_config.Cliente;
import com.mycompany.hibernate_config.Libro;
import com.mycompany.hibernate_config.Librocompra;
import jakarta.persistence.EntityManager;
import java.util.Date;

public class Libro_compraDAO {
    private EntityManager em;

    public Libro_compraDAO(EntityManager em) {
        this.em = em;
    }

    /**
     * Registra una venta completa: resta stock y crea el recibo.
     * Todo ocurre dentro de una única transacción (Punto 3.10).
     */
    public void registrarVenta(int idCliente, int idLibro) {
        try {
            // Iniciamos la transacción: "Todo o nada"
            em.getTransaction().begin();

            // 1. Buscamos los objetos necesarios
            Cliente cliente = em.find(Cliente.class, idCliente);
            Libro libro = em.find(Libro.class, idLibro);

            // 2. Validaciones de negocio
            if (cliente == null) {
                System.out.println("Error: El cliente no existe.");
                em.getTransaction().rollback();
                return;
            }
            if (libro == null) {
                System.out.println("Error: El libro no existe.");
                em.getTransaction().rollback();
                return;
            }
            if (libro.getStock() <= 0) {
                System.out.println("Error: No queda stock de '" + libro.getTitulo() + "'.");
                em.getTransaction().rollback();
                return;
            }

            // 3. Modificamos el stock del libro (Hibernate detecta el cambio)
            libro.setStock(libro.getStock() - 1);
            em.merge(libro);

            // 4. Creamos el registro en la tabla LIBROCOMPRA
            Librocompra compra = new Librocompra();
            compra.setClienteId(cliente);
            compra.setLibroId(libro);
            compra.setFechaCompra(new Date()); // Fecha actual
            
            em.persist(compra);

            // 5. Finalizamos la transacción
            em.getTransaction().commit();
            System.out.println("VENTA ÉXITOSA: " + cliente.getNombre() + " compró " + libro.getTitulo());

        } catch (Exception e) {
            // Si algo falla (ej. error de red, BD caída), deshacemos todo
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.out.println("ERROR CRÍTICO: La venta no se pudo realizar. " + e.getMessage());
        }
    }
}
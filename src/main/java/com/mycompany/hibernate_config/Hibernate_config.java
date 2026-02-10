package com.mycompany.hibernate_config;

// Importamos las clases DAO para manejar las entidades de la base de datos
import com.mycompany.hibernate_config.DAO.AutorDAO;
import com.mycompany.hibernate_config.DAO.LibroAutorDAO;
import com.mycompany.hibernate_config.DAO.LibroDAO;
import com.mycompany.hibernate_config.DAO.Libro_compraDAO;

// Importamos clases de JPA para manejar la persistencia
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.Scanner;
import java.util.List;
import javax.crypto.AEADBadTagException;

public class Hibernate_config {

    public static void main(String[] args) {
        Scanner lector = new Scanner(System.in);

        String persistencia = "LibreriaBD"; // Nombre de la unidad de persistencia definida en persistence.xml

        // Creación de EntityManagerFactory y EntityManager para gestionar la conexión con la base de datos
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistencia);
        EntityManager em = emf.createEntityManager();

        // Creamos instancias de los DAO pasando el EntityManager
        LibroDAO libroDAO = new LibroDAO(em);
        LibroAutorDAO autorDAO = new LibroAutorDAO(em);
        AutorDAO nuevo_autordao = new AutorDAO(em);

        int op = 0;

        // Bucle principal del programa
        do {
            // Menú de opciones
            System.out.println("\n--- LIBRERÍA ATE ---");
            System.out.println("1. Añadir Libro ");
            System.out.println("2. Buscar Libro por ID ");
            System.out.println("3. Editar Stock ");
            System.out.println("4. Listar Libros ");
            System.out.println("5. Nueva venta ");
            System.out.println("6. Listar libros por autor");
            System.out.println("7. Eliminar libro");
            System.out.println("8. Salir");
            System.out.print("Selecciona una opción: ");

            try {
                op = Integer.parseInt(lector.nextLine()); // Leemos la opción del usuario y la convertimos a entero

                switch (op) {
                    case 1:
                        // Caso 1: Añadir un nuevo libro
                        System.out.print("Título del libro: ");
                        String titulo = lector.nextLine();
                        System.out.print("ISBN: ");
                        String isbn = lector.nextLine();

                        System.out.print("Nombre Autor: ");
                        String nom_at = lector.nextLine();

                        System.out.print("Apellido Autor: ");
                        String ap_at = lector.nextLine();

                        System.out.print("Stock: ");
                        int stock = Integer.parseInt(lector.nextLine());

                        System.out.print("ID Editorial: ");
                        int editorial_Id = Integer.parseInt(lector.nextLine());

                        // Buscamos la editorial en la base de datos usando el EntityManager
                        Editorial editorial = em.find(Editorial.class, editorial_Id);

                        if (editorial != null) {

                            //Creación nuevo autor
                            Autor nuevo_aut = new Autor();
                            nuevo_aut.setNombre(nom_at);
                            nuevo_aut.setApellido(ap_at);
                            nuevo_autordao.crear(nuevo_aut);

                            // Si la editorial existe, creamos un nuevo libro
                            Libro nuevo = new Libro();
                            nuevo.setTitulo(titulo);
                            nuevo.setIsbn(isbn);
                            nuevo.setStock(stock);
                            nuevo.setEditorialId(editorial); // Asociamos el objeto Editorial al libro

                            //Se asocia el autor al libro en la tabla Libro_autor
                            nuevo.addAutor(nuevo_aut);

                            libroDAO.crear(nuevo); // Guardamos el libro en la base de datos     
                            System.out.println("---------------------------------------------------");
                            System.out.println("Libro " + nuevo.getTitulo() + " añadido con exito");
                            System.out.println("Autor " + nuevo_aut.getNombre() + "" + nuevo_aut.getApellido() + " ID: " + nuevo_aut.getId());
                            System.out.println("---------------------------------------------------");

                        } else {
                            System.out.println("Error: La Editorial con ID " + editorial_Id + " no existe.");
                        }
                        break;

                    case 2:
                        // Caso 2: Buscar un libro por su ID
                        System.out.print("ID del libro a buscar: ");
                        int idBuscar = lector.nextInt();
                        lector.nextLine();
                        Libro encontrado = libroDAO.buscarPorId(idBuscar); // Buscamos el libro en la base de datos
                        if (encontrado != null) {
                            System.out.println("Encontrado: " + encontrado.getTitulo() + " | Stock: " + encontrado.getStock());
                        } else {
                            System.out.println("No se encontró ningún libro con ese ID.");
                        }
                        break;

                    case 3:
                        // Caso 3: Editar un libro existente
                        System.out.print("ID del libro a editar STOCK: ");
                        int idEdit = lector.nextInt();

                        Libro libroEdit = libroDAO.buscarPorId(idEdit); // Buscamos el libro por ID

                        if (libroEdit != null) {
                            System.out.println("---- " + libroEdit.getTitulo() + " ----");
                            System.out.print("Nuevo stock (actual (" + libroEdit.getStock() + "): ");
                            int nuevo_stock = lector.nextInt();
                            lector.nextLine();
                            libroEdit.setStock(nuevo_stock); // Actualizamos el stock

                            libroDAO.actualizar(libroEdit); // Guardamos los cambios en la base de datos
                            System.out.println("Libro actualizado.");
                        }
                        break;

                    case 4:
                        // Caso 4: Listar todos los libros
                        List<Libro> lista = libroDAO.listarTodos();
                        System.out.println("\n--- LISTADO DE LIBROS ---");
                        for (Libro l : lista) {
                            System.out.println("ID: " + l.getId() + " | " + l.getTitulo() + " | Stock: " + l.getStock());
                        }
                        break;

                    case 5:
                        // Caso 5: Registrar una nueva venta
                        System.out.print("Introduzca ID cliente: ");
                        int id_cli = lector.nextInt();

                        System.out.print("Introduzca ID libro: ");
                        int id_libro = lector.nextInt();

                        System.out.print("Introduzca cantidad: ");
                        int cant = lector.nextInt();
                        lector.nextLine(); // Limpiamos buffer del scanner

                        Libro_compraDAO compraDAO = new Libro_compraDAO(em);
                        for (int i = 0; i < cant; i++) {
                            // Registramos la venta tantas veces como la cantidad indicada
                            compraDAO.registrarVenta(id_cli, id_libro);
                        }
                        break;

                    case 6:
                        // Caso 6: Listar libros de un autor
                        System.out.print("Introduzca ID autor: ");
                        int id_autor = Integer.parseInt(lector.nextLine());

                        try {
                            Autor autor = autorDAO.buscarAutor(id_autor);
                            if (autor != null) {
                                List<Libro> libros_autor = autor.getLibroList();

                                if (!libros_autor.isEmpty()) {
                                    System.out.println("-- " + autor.getNombre() + " " + autor.getApellido() + " --");
                                    for (Libro libro : libros_autor) {
                                        System.out.println("- " + libro.getTitulo());
                                    }
                                } else {
                                    System.out.println("Autor no tiene libros asociados");
                                }
                            } else {
                                System.out.println("Autor no encontrado o no existe");
                            }
                        } catch (Exception e) {
                            System.out.println(e);
                        }

                        break;
                    case 7:
                        // Caso 7: eliminar libro
                        System.out.print("Introduzca el nombre del libro a eliminar: ");
                        String nom_libro = lector.nextLine();

                        Libro eliminar = libroDAO.buscarPornom(nom_libro);

                        if (eliminar != null) {
                            libroDAO.borrar(eliminar);
                            System.out.println("Libro eliminado con exito!");
                        } else {
                            System.out.println("No se ha podido eliminar el libro");
                        }

                        break;

                    case 8:
                        System.out.println("Cerrando...");
                        break;

                    default:
                        System.out.println("Opción no válida.");
                }
            } catch (NumberFormatException e) {
                // Captura errores si el usuario introduce un valor no numérico
                System.out.println("Error: Introduce un valor válido. " + e.getMessage());
            }
        } while (op != 8); // Repetimos hasta que el usuario elija salir

        // Cerramos los recursos de JPA
        em.close();
        emf.close();
    }
}

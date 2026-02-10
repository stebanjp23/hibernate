/*
 * Licencia y plantilla generada por NetBeans
 */
package com.mycompany.hibernate_config;

// Importaciones de JPA (Hibernate usa estas anotaciones)
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.util.List;

/**
 * Clase Cliente
 * Representa la tabla "cliente" de la base de datos "libreria"
 * 
 * @author juane
 */
@Entity // Indica que esta clase es una entidad JPA
@Table(name = "cliente", catalog = "libreria", schema = "")
@NamedQueries({
    // Consulta para obtener todos los clientes
    @NamedQuery(name = "Cliente.findAll", query = "SELECT c FROM Cliente c"),
    
    // Consulta para buscar un cliente por id
    @NamedQuery(name = "Cliente.findById", query = "SELECT c FROM Cliente c WHERE c.id = :id"),
    
    // Consulta para buscar un cliente por nombre
    @NamedQuery(name = "Cliente.findByNombre", query = "SELECT c FROM Cliente c WHERE c.nombre = :nombre"),
    
    // Consulta para buscar un cliente por email
    @NamedQuery(name = "Cliente.findByEmail", query = "SELECT c FROM Cliente c WHERE c.email = :email")
})
public class Cliente implements Serializable {

    // Necesario para que la entidad sea serializable
    private static final long serialVersionUID = 1L;

    // Clave primaria
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Autoincrement en la BD
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    // Campo nombre (obligatorio)
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;

    // Campo email (opcional)
    @Column(name = "email")
    private String email;

    // Relación 1 a muchos:
    // Un cliente puede tener muchas compras de libros
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "clienteId")
    private List<Librocompra> librocompraList;

    // Constructor vacío obligatorio para JPA
    public Cliente() {
    }

    // Constructor con id
    public Cliente(Integer id) {
        this.id = id;
    }

    // Constructor con id y nombre
    public Cliente(Integer id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    // Getter y Setter del id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    // Getter y Setter del nombre
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // Getter y Setter del email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Getter y Setter de la lista de compras
    public List<Librocompra> getLibrocompraList() {
        return librocompraList;
    }

    public void setLibrocompraList(List<Librocompra> librocompraList) {
        this.librocompraList = librocompraList;
    }

    // Método hashCode basado en el id
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    // Método equals para comparar entidades por id
    @Override
    public boolean equals(Object object) {
        // Ojo: no funciona bien si el id no está asignado
        if (!(object instanceof Cliente)) {
            return false;
        }
        Cliente other = (Cliente) object;
        if ((this.id == null && other.id != null) || 
            (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    // Representación en texto del objeto
    @Override
    public String toString() {
        return "com.mycompany.hibernate_config.Cliente[ id=" + id + " ]";
    }
}

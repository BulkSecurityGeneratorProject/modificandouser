package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Trabajador.
 */
@Entity
@Table(name = "trabajador")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "trabajador")
public class Trabajador implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "nombre", nullable = false)
    private String nombre;
    
    @NotNull
    @Column(name = "email", nullable = false)
    private String email;
    
    @NotNull
    @Column(name = "dni", nullable = false)
    private String dni;
    
    @NotNull
    @Column(name = "numero_telf", nullable = false)
    private Integer numeroTelf;
    
    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "trabajador_proceso",
               joinColumns = @JoinColumn(name="trabajadors_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="procesos_id", referencedColumnName="ID"))
    private Set<Proceso> procesos = new HashSet<>();

    @ManyToMany(mappedBy = "trabajadors")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Pedido> pedidos = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }

    public String getDni() {
        return dni;
    }
    
    public void setDni(String dni) {
        this.dni = dni;
    }

    public Integer getNumeroTelf() {
        return numeroTelf;
    }
    
    public void setNumeroTelf(Integer numeroTelf) {
        this.numeroTelf = numeroTelf;
    }

    public Set<Proceso> getProcesos() {
        return procesos;
    }

    public void setProcesos(Set<Proceso> procesos) {
        this.procesos = procesos;
    }

    public Set<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(Set<Pedido> pedidos) {
        this.pedidos = pedidos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Trabajador trabajador = (Trabajador) o;
        if(trabajador.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, trabajador.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Trabajador{" +
            "id=" + id +
            ", nombre='" + nombre + "'" +
            ", email='" + email + "'" +
            ", dni='" + dni + "'" +
            ", numeroTelf='" + numeroTelf + "'" +
            '}';
    }
}

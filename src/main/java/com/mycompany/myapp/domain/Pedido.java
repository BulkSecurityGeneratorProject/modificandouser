package com.mycompany.myapp.domain;

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
 * A Pedido.
 */
@Entity
@Table(name = "pedido")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "pedido")
public class Pedido implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Lob
    @Column(name = "descripcion", nullable = false)
    private String descripcion;
    
    @NotNull
    @Column(name = "coste_sin_iva", nullable = false)
    private Integer costeSinIva;
    
    @NotNull
    @Column(name = "coste_total", nullable = false)
    private Integer costeTotal;
    
    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "pedido_proceso",
               joinColumns = @JoinColumn(name="pedidos_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="procesos_id", referencedColumnName="ID"))
    private Set<Proceso> procesos = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "pedido_trabajador",
               joinColumns = @JoinColumn(name="pedidos_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="trabajadors_id", referencedColumnName="ID"))
    private Set<Trabajador> trabajadors = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getCosteSinIva() {
        return costeSinIva;
    }
    
    public void setCosteSinIva(Integer costeSinIva) {
        this.costeSinIva = costeSinIva;
    }

    public Integer getCosteTotal() {
        return costeTotal;
    }
    
    public void setCosteTotal(Integer costeTotal) {
        this.costeTotal = costeTotal;
    }

    public Set<Proceso> getProcesos() {
        return procesos;
    }

    public void setProcesos(Set<Proceso> procesos) {
        this.procesos = procesos;
    }

    public Set<Trabajador> getTrabajadors() {
        return trabajadors;
    }

    public void setTrabajadors(Set<Trabajador> trabajadors) {
        this.trabajadors = trabajadors;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Pedido pedido = (Pedido) o;
        if(pedido.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, pedido.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Pedido{" +
            "id=" + id +
            ", descripcion='" + descripcion + "'" +
            ", costeSinIva='" + costeSinIva + "'" +
            ", costeTotal='" + costeTotal + "'" +
            '}';
    }
}

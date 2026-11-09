/*
 * Entidad Producto - Actividad del laboratorio.
 */
package com.example.models;

import com.sun.istack.NotNull;
import java.io.Serializable;
import java.util.Calendar;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import org.eclipse.persistence.nosql.annotations.DataFormatType;
import org.eclipse.persistence.nosql.annotations.NoSql;

/**
 * Producto asociado a cada Competitor.
 * Atributos: nombre, marca y fecha de compra.
 */
@NoSql(dataFormat = DataFormatType.MAPPED)
@Embeddable
@XmlRootElement
public class Producto implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    private String nombre;

    @NotNull
    private String marca;

    @NotNull
    @Column(name = "fecha_compra")
    @Temporal(TemporalType.DATE)
    private Calendar fechaCompra;

    public Producto() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public Calendar getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(Calendar fechaCompra) {
        this.fechaCompra = fechaCompra;
    }
}

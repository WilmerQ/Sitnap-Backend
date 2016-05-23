/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.ucc.sipnat.modelo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;

/**
 *
 * @author Windows 8.1
 */
@Entity
public class DatosSensor extends CamposComunesdeEntidad implements Serializable {
    @ManyToOne
    private Sensor sensor;
    private String dato;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaRecoleccion;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaSincronizacion;
    private String horaDeRecoleccion;
    private Boolean revisado;

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    public String getDato() {
        return dato;
    }

    public void setDato(String dato) {
        this.dato = dato;
    }

    public Date getFechaRecoleccion() {
        return fechaRecoleccion;
    }

    public void setFechaRecoleccion(Date fechaRecoleccion) {
        this.fechaRecoleccion = fechaRecoleccion;
    }

    public Date getFechaSincronizacion() {
        return fechaSincronizacion;
    }

    public void setFechaSincronizacion(Date fechaSincronizacion) {
        this.fechaSincronizacion = fechaSincronizacion;
    }

    public String getHoraDeRecoleccion() {
        return horaDeRecoleccion;
    }

    public void setHoraDeRecoleccion(String horaDeRecoleccion) {
        this.horaDeRecoleccion = horaDeRecoleccion;
    }

    public Boolean getRevisado() {
        return revisado;
    }

    public void setRevisado(Boolean revisado) {
        this.revisado = revisado;
    }
}

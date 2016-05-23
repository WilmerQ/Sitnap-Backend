/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.ucc.sipnat.modelo;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;

/**
 *
 * @author Windows 8.1
 */
@Entity
public class Alerta extends CamposComunesdeEntidad implements Serializable {

    @ManyToOne
    private Proyecto proyecto;
    @Column(columnDefinition = "TEXT")
    private String Descripcion;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaDelDisparo;
    @Temporal(javax.persistence.TemporalType.TIME)
    private Date horaDelDisparo;

    public Proyecto getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String Descripcion) {
        this.Descripcion = Descripcion;
    }

    public Date getFechaDelDisparo() {
        return fechaDelDisparo;
    }

    public void setFechaDelDisparo(Date fechaDelDisparo) {
        this.fechaDelDisparo = fechaDelDisparo;
    }

    public Date getHoraDelDisparo() {
        return horaDelDisparo;
    }

    public void setHoraDelDisparo(Date horaDelDisparo) {
        this.horaDelDisparo = horaDelDisparo;
    }
    
    

}

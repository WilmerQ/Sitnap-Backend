/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.ucc.sipnat.modelo;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 *
 * @author Windows 8.1
 */
@Entity
public class ZonaXProyecto extends CamposComunesdeEntidad implements Serializable {
    
    @ManyToOne
    private Proyecto proyecto;
    @ManyToOne
    private ZonaAfectada zonaAfectada;

    public Proyecto getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }

    public ZonaAfectada getZonaAfectada() {
        return zonaAfectada;
    }

    public void setZonaAfectada(ZonaAfectada zonaAfectada) {
        this.zonaAfectada = zonaAfectada;
    }
}

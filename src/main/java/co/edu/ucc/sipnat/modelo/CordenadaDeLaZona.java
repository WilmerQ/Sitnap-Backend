/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.ucc.sipnat.modelo;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import org.primefaces.model.map.Marker;

/**
 *
 * @author Windows 8.1
 */
@Entity
public class CordenadaDeLaZona extends CamposComunesdeEntidad implements Serializable {
    
    @ManyToOne
    private ZonaAfectada zonaAfectada;
    private String latitud;
    private String longitud;
    private Integer orden;
    @Transient
    private Marker marker;

    public ZonaAfectada getZonaAfectada() {
        return zonaAfectada;
    }

    public void setZonaAfectada(ZonaAfectada zonaAfectada) {
        this.zonaAfectada = zonaAfectada;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }  
}

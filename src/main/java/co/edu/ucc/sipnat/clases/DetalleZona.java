/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.ucc.sipnat.clases;

import co.edu.ucc.sipnat.modelo.CordenadaDeLaZona;

/**
 *
 * @author Windows 8.1
 */
public class DetalleZona {

    private String latitud;
    private String longitud;
    private Integer orden;

    public DetalleZona() {
    }

    public DetalleZona(CordenadaDeLaZona cdlz) {
        this.latitud = cdlz.getLatitud();
        this.longitud = cdlz.getLongitud();
        this.orden = cdlz.getOrden();
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

}

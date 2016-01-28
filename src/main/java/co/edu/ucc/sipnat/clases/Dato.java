/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.ucc.sipnat.clases;

import co.edu.ucc.sipnat.modelo.DatosSensor;
import java.util.Date;

/**
 *
 * @author Windows 8.1
 */
public class Dato {
    
    private Date fechaRecolecion;
    private String horaRecolecion;
    private String dato;
    private Sensor sensor;
    
    public Dato(DatosSensor ds) {
        this.fechaRecolecion = ds.getFechaRecoleccion();
        this.horaRecolecion = null;
        this.dato = ds.getDato();
        this.sensor = new Sensor(ds.getSensor());
    }
    
    public Date getFechaRecolecion() {
        return fechaRecolecion;
    }
    
    public void setFechaRecolecion(Date fechaRecolecion) {
        this.fechaRecolecion = fechaRecolecion;
    }
    
    public String getHoraRecolecion() {
        return horaRecolecion;
    }
    
    public void setHoraRecolecion(String horaRecolecion) {
        this.horaRecolecion = horaRecolecion;
    }
    
    public String getDato() {
        return dato;
    }
    
    public void setDato(String dato) {
        this.dato = dato;
    }
    
    public Sensor getSensor() {
        return sensor;
    }
    
    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }
    
}

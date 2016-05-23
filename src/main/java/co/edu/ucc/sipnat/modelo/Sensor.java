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
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;

/**
 *
 * @author Windows 8.1
 */
@Entity
public class Sensor extends CamposComunesdeEntidad implements Serializable {
    private String codigoHexadecimal;
    private String latitud;
    private String longitud;
    private String estadoDelSensor;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaDeDesconexion;
    @ManyToOne
    private TipoSensor tipoSensor;
    @ManyToOne
    private Proyecto proyectoPadre;
    private BigDecimal promedio;
     
    public String getCodigoHexadecimal() {
        return codigoHexadecimal;
    }

    public void setCodigoHexadecimal(String codigoHexadecimal) {
        this.codigoHexadecimal = codigoHexadecimal;
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

    public TipoSensor getTipoSensor() {
        return tipoSensor;
    }

    public void setTipoSensor(TipoSensor tipoSensor) {
        this.tipoSensor = tipoSensor;
    }

    public String getEstadoDelSensor() {
        return estadoDelSensor;
    }

    public void setEstadoDelSensor(String estadoDelSensor) {
        this.estadoDelSensor = estadoDelSensor;
    }

    public Proyecto getProyectoPadre() {
        return proyectoPadre;
    }

    public void setProyectoPadre(Proyecto proyectoPadre) {
        this.proyectoPadre = proyectoPadre;
    }   

    public Date getFechaDeDesconexion() {
        return fechaDeDesconexion;
    }

    public void setFechaDeDesconexion(Date fechaDeDesconexion) {
        this.fechaDeDesconexion = fechaDeDesconexion;
    }

    public BigDecimal getPromedio() {
        return promedio;
    }

    public void setPromedio(BigDecimal promedio) {
        this.promedio = promedio;
    }  
}

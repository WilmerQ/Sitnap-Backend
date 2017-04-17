/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.ucc.sipnat.modelo;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Transient;

/**
 *
 * @author Windows 8.1
 */
@Entity
public class TipoSensor extends CamposComunesdeEntidad implements Serializable {

    private String nombre;
    private String unidadDeMedida;
    @Lob
    private byte[] logoDelTipo; 
    @Transient
    private String uuid;
    @Transient
    private Boolean selecionado;

    public TipoSensor(TipoSensor ts) {
        this.nombre = ts.getNombre();
        this.unidadDeMedida = ts.getUnidadDeMedida();
        this.setId(ts.getId());
    }

    public TipoSensor() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre.toUpperCase();
    }

    public byte[] getLogoDelTipo() {
        return logoDelTipo;
    }

    public void setLogoDelTipo(byte[] logoDelTipo) {
        this.logoDelTipo = logoDelTipo;
    }

    public String getUnidadDeMedida() {
        return unidadDeMedida;
    }

    public void setUnidadDeMedida(String unidadDeMedida) {
        this.unidadDeMedida = unidadDeMedida;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Boolean getSelecionado() {
        return selecionado;
    }

    public void setSelecionado(Boolean selecionado) {
        this.selecionado = selecionado;
    }
    
    
}

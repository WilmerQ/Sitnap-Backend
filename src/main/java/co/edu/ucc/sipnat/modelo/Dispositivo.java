/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.ucc.sipnat.modelo;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 *
 * @author Windows 8.1
 */
@Entity
public class Dispositivo extends CamposComunesdeEntidad implements Serializable {

    @Column(columnDefinition = "TEXT")
    private String token;
    @Column(columnDefinition = "TEXT")
    private String imei;
    @ManyToOne
    private Proyecto proyecto;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public Proyecto getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }
}

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
import javax.persistence.Transient;

/**
 *
 * @author Windows 8.1
 */
@Entity
public class Proyecto extends CamposComunesdeEntidad implements Serializable {

    private String nombre;
    @Column(columnDefinition = "TEXT")
    private String descripcion;
    private String alertaNivel1;
    private String alertaNivel2;
    private String alertaNivel3;
    @ManyToOne
    private Usuario usuario;
    @Transient
    private Boolean selecionado;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre.toUpperCase();
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion.toUpperCase();
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Boolean getSelecionado() {
        return selecionado;
    }

    public void setSelecionado(Boolean selecionado) {
        this.selecionado = selecionado;
    }   

    public String getAlertaNivel1() {
        return alertaNivel1;
    }

    public void setAlertaNivel1(String alertaNivel1) {
        this.alertaNivel1 = alertaNivel1;
    }

    public String getAlertaNivel2() {
        return alertaNivel2;
    }

    public void setAlertaNivel2(String alertaNivel2) {
        this.alertaNivel2 = alertaNivel2;
    }

    public String getAlertaNivel3() {
        return alertaNivel3;
    }

    public void setAlertaNivel3(String alertaNivel3) {
        this.alertaNivel3 = alertaNivel3;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ibcaribe.procc.services;

import java.io.Serializable;

/**
 *
 * @author juvinao
 */
public class FieldtoQuery implements Serializable {

    private String nombreCampo;
    private Object valorCampo;
    private Boolean usarLike;

    public FieldtoQuery() {
    }

    public FieldtoQuery(String nombreCampo, Object valorCampo) {
        this.nombreCampo = nombreCampo;
        this.valorCampo = valorCampo;
    }

    public FieldtoQuery(String nombreCampo, Object valorCampo, Boolean usarLike) {
        this.nombreCampo = nombreCampo;
        this.valorCampo = valorCampo;
        this.usarLike = usarLike;
    }

    public String getNombreCampo() {
        return nombreCampo;
    }

    public void setNombreCampo(String nombreCampo) {
        this.nombreCampo = nombreCampo;
    }

    public Object getValorCampo() {
        return valorCampo;
    }

    public void setValorCampo(Object valorCampo) {
        this.valorCampo = valorCampo;
    }

    public Boolean getUsarLike() {
        if (usarLike == null) {
            usarLike = false;
        }
        return usarLike;
    }

    public void setUsarLike(Boolean usarLike) {
        this.usarLike = usarLike;
    }
}

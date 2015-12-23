/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.ucc.sipnat.modelo;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;

/**
 *
 * @author Windows 8.1
 */
@Entity
public class Auditoria extends CamposComunesdeEntidad implements Serializable {
    private String metodo;
    private String causaDelError;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaYHoraDelaCausa;

    public String getMetodo() {
        return metodo;
    }

    public void setMetodo(String metodo) {
        this.metodo = metodo;
    }

    public String getCausaDelError() {
        return causaDelError;
    }

    public void setCausaDelError(String causaDelError) {
        this.causaDelError = causaDelError;
    }

    public Date getFechaYHoraDelaCausa() {
        return fechaYHoraDelaCausa;
    }

    public void setFechaYHoraDelaCausa(Date fechaYHoraDelaCausa) {
        this.fechaYHoraDelaCausa = fechaYHoraDelaCausa;
    }
    
    
}

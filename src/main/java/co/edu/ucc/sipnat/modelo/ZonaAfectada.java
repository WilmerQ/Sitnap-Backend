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

/**
 *
 * @author Windows 8.1
 */
@Entity
public class ZonaAfectada extends CamposComunesdeEntidad implements Serializable {
    private String nombreDelaZona;

    public String getNombreDelaZona() {
        return nombreDelaZona;
    }

    public void setNombreDelaZona(String nombreDelaZona) {
        this.nombreDelaZona = nombreDelaZona;
    }
}

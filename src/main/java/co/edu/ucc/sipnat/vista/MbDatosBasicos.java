/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.ucc.sipnat.vista;

import co.edu.ucc.sipnat.clases.DatosBasicos;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Windows 8.1
 */
@ViewScoped
@ManagedBean(name = "MbDatosBasicos")
public class MbDatosBasicos implements Serializable {

    private String ip;

    /**
     * Creates a new instance of MbDatosBasicos
     */
    public MbDatosBasicos() {
    }

    @PostConstruct
    public void init() {
        
        ip = DatosBasicos.ip;
        
        //ip = "localhost";
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}

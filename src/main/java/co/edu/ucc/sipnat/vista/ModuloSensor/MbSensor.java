/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.ucc.sipnat.vista.ModuloSensor;

import co.edu.ucc.sipnat.logica.CommonsBean;
import co.edu.ucc.sipnat.modelo.Sensor;
import co.edu.ucc.sipnat.modelo.Usuario;
import com.ibcaribe.i4w.base.SessionOperations;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Windows 8.1
 */
@ViewScoped
@ManagedBean(name = "MbTipoDeSensor")
public class MbSensor implements Serializable {

        
    
    private List<Sensor> sensores; 
    private Sensor sensor;
    private Usuario usuario;
    
    @EJB
    private CommonsBean cb;
    
    public MbSensor() {
        usuario = (Usuario) SessionOperations.getSessionValue("USUARIO");
        sensores = cb.getByOneField(Sensor.class, "usuariocreacion", usuario.getNombreUsuario());
    }
    
    @PostConstruct
    public void init(){
          
    }
    
}

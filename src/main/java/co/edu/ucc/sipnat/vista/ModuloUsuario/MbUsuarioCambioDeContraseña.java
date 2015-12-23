/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.ucc.sipnat.vista.ModuloUsuario;

import co.edu.ucc.sipnat.logica.CommonsBean;
import co.edu.ucc.sipnat.modelo.Usuario;
import com.ibcaribe.i4w.base.SessionOperations;
import com.ibcaribe.procc.services.Md5;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.resource.spi.CommException;

/**
 *
 * @author Windows 8.1
 */
@ViewScoped
@ManagedBean(name = "MbUsuarioCambioDeContraseña")
public class MbUsuarioCambioDeContraseña implements Serializable {

    private Usuario usuario;
    private String claveAntigua;
    private String claveNueva;
    private String claveNuevaConfirmacion;

    @EJB
    private CommonsBean cb;

    public MbUsuarioCambioDeContraseña() {
    }
    
    @PostConstruct
    public void init() {
        usuario = (Usuario) SessionOperations.getSessionValue("USUARIO");
        claveAntigua = "";
        claveNueva = "";
        claveNuevaConfirmacion = "";
    }

    public Boolean verificarFormulario() {
        Boolean res = Boolean.TRUE;
        if (claveAntigua.trim().length() == 0) {
            mostrarMensaje(FacesMessage.SEVERITY_ERROR, "ERROR", "Agregue clave antigua");
            res = Boolean.FALSE;
        }
        if (claveNueva.trim().length() == 0) {
            mostrarMensaje(FacesMessage.SEVERITY_ERROR, "ERROR", "Agregue clave nueva");
            res = Boolean.FALSE;
        }
        if (claveNuevaConfirmacion.trim().length() == 0) {
            mostrarMensaje(FacesMessage.SEVERITY_ERROR, "ERROR", "Agregue clave nueva de confirmacion");
            res = Boolean.FALSE;
        }
        if (res) {
            System.out.println(usuario.getClave());
            if (Md5.getEncoddedString(claveAntigua).equals(usuario.getClave())) {
                if (!claveNueva.equals(claveNuevaConfirmacion)) {
                    mostrarMensaje(FacesMessage.SEVERITY_ERROR, "ERROR", "Clave no coinciden");
                    res = Boolean.FALSE;
                }
            } else {
                mostrarMensaje(FacesMessage.SEVERITY_ERROR, "ERROR", "Clave antigua incorrecta");
                res = Boolean.FALSE;
            }
        }
        return res;
    }

    public void accionCambiarContraseña() {
        if (verificarFormulario()) {
            usuario.setClave(Md5.getEncoddedString(claveNueva));
            if (cb.guardar(usuario)) {
                mostrarMensaje(FacesMessage.SEVERITY_INFO, "Exitoso", "Se ha cambiado la contraseña");
                init();
            } else {
                mostrarMensaje(FacesMessage.SEVERITY_ERROR, "ERROR", "Ha ocurrido un error no se ha podido cambiar la contraseña");
            }
        }
    }

    public void mostrarMensaje(FacesMessage.Severity icono, String titulo, String mensaje) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(icono, titulo, mensaje));
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getClaveAntigua() {
        return claveAntigua;
    }

    public void setClaveAntigua(String claveAntigua) {
        this.claveAntigua = claveAntigua;
    }

    public String getClaveNueva() {
        return claveNueva;
    }

    public void setClaveNueva(String claveNueva) {
        this.claveNueva = claveNueva;
    }

    public String getClaveNuevaConfirmacion() {
        return claveNuevaConfirmacion;
    }

    public void setClaveNuevaConfirmacion(String claveNuevaConfirmacion) {
        this.claveNuevaConfirmacion = claveNuevaConfirmacion;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.ucc.sipnat.vista.ModuloUsuario;

import co.edu.ucc.sipnat.base.SessionOperations;
import co.edu.ucc.sipnat.logica.CommonsBean;
import co.edu.ucc.sipnat.modelo.Usuario;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Windows 8.1
 */
@ViewScoped
@ManagedBean(name = "MbUsuarioEditarDatos")
public class MbUsuarioEditarDatos implements Serializable {

    private Usuario usuario;

    @EJB
    private CommonsBean cb;

    /**
     * Creates a new instance of MbUsuarioEditarDatos
     */
    public MbUsuarioEditarDatos() {
    }

    @PostConstruct
    public void init() {
        usuario = (Usuario) SessionOperations.getSessionValue("USUARIO");
    }

    public Boolean verificarFormulario() {
        Boolean res = Boolean.TRUE;
        if (usuario.getEmail().trim().length() == 0) {
            res = Boolean.FALSE;
            mostrarMensaje(FacesMessage.SEVERITY_ERROR, "ERROR", "Agregue email");
        }
        if (usuario.getTelefono().trim().length() == 0) {
            res = Boolean.FALSE;
            mostrarMensaje(FacesMessage.SEVERITY_ERROR, "ERROR", "Agregue telefono");
        }
        return res;
    }

    public void accionActualizarDatos() {
        if (verificarFormulario()) {
            if (cb.guardar(usuario)) {
                Usuario u = (Usuario) cb.getById(Usuario.class, usuario.getId());
                SessionOperations.setSessionValue("USUARIO", u);
                mostrarMensaje(FacesMessage.SEVERITY_INFO, "Exitoso", "Se han actualizado los datos");
                init();
            } else {
                mostrarMensaje(FacesMessage.SEVERITY_ERROR, "ERROR", "no se ha actualizado los datos");
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
}

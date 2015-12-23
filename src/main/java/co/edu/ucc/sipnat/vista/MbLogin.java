/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.ucc.sipnat.vista;

import co.edu.ucc.sipnat.logica.CommonsBean;
import co.edu.ucc.sipnat.logica.LogicaLogin;
import co.edu.ucc.sipnat.modelo.Usuario;
import com.ibcaribe.i4w.base.SessionOperations;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Windows 8.1
 */
@ViewScoped
@ManagedBean(name = "MbLogin")
public class MbLogin implements Serializable {

    private Usuario usuario;
    private String nombreDeUsuaio;
    private String password;
    private Boolean autenticado;
    private Boolean isusuario;

    @EJB
    private CommonsBean cb;
    @EJB
    private LogicaLogin ll;

    public MbLogin() {

    }

    @PostConstruct
    public void init() {
        usuario = (Usuario) SessionOperations.getSessionValue("USUARIO");
        if (usuario == null) {
            usuario = new Usuario();
            autenticado = Boolean.FALSE;
            isusuario = Boolean.FALSE;
            SessionOperations.setSessionValue("USER", Boolean.FALSE);
        }else{
           autenticado = Boolean.TRUE;
           isusuario = Boolean.TRUE;
        }    
    }

    public String accionLogin() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getFlash().setKeepMessages(true);
        autenticado = false;
        isusuario = false;
        Usuario u = ll.login(nombreDeUsuaio, password);
        SessionOperations.setSessionValue("USER", Boolean.FALSE);
        if (u != null) {
            usuario = u;
            autenticado = true;
            isusuario = true;
            SessionOperations.setSessionValue("USER", Boolean.TRUE);
            SessionOperations.setSessionValue("USUARIO", usuario);
            
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, u.getNombreUsuario(), "Bienvenido"));
            redirect("usuario/index.xhtml");
        } else {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Usuario y/o contraseñas no validos"));

        }
        return null;
    }

    public String accionLogout() {
        init();
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getFlash().setKeepMessages(true);
        try {
            SessionOperations.setSessionValue("USER", Boolean.FALSE);
            context.getExternalContext().invalidateSession();
        } catch (Exception e) {

        }
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Salida", "Se ha cerrado la sesion correctamente"));
        String contextPath = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getContextPath();
        redirect(contextPath);
        return null;
    }

    private void redirect(String url) {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            context.getExternalContext().redirect(url);
        } catch (IOException ex) {
            Logger.getLogger(MbLogin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getNombreDeUsuaio() {
        return nombreDeUsuaio;
    }

    public void setNombreDeUsuaio(String nombreDeUsuaio) {
        this.nombreDeUsuaio = nombreDeUsuaio;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getAutenticado() {
        return autenticado;
    }

    public void setAutenticado(Boolean autenticado) {
        this.autenticado = autenticado;
    }

    public LogicaLogin getLl() {
        return ll;
    }

    public void setLl(LogicaLogin ll) {
        this.ll = ll;
    }

    public Boolean getIsusuario() {
        return isusuario;
    }

    public void setIsusuario(Boolean isusuario) {
        this.isusuario = isusuario;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}

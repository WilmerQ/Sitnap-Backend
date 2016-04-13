/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.ucc.sipnat.vista.ModuloSensor;

import co.edu.ucc.sipnat.base.SessionOperations;
import co.edu.ucc.sipnat.logica.CommonsBean;
import co.edu.ucc.sipnat.modelo.TipoSensor;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import org.primefaces.event.FileUploadEvent;

/**
 *
 * @author Windows 8.1
 */
@ViewScoped
@ManagedBean(name = "MbTipoDeSensor")
public class MbTipoDeSensor implements Serializable {

    private TipoSensor tipoSensor;
    private List<TipoSensor> tipoDeSensores;

    @EJB
    private CommonsBean cb;

    public MbTipoDeSensor() {
    }

    @PostConstruct
    public void init() {
        tipoSensor = new TipoSensor();
        tipoDeSensores = new ArrayList<>();
        tipoDeSensores = cb.getAll(TipoSensor.class);
    }

    public void handleFileUpload(FileUploadEvent Imagen) throws IOException {
        InputStream fi;
        InputStream fi2;
        fi = Imagen.getFile().getInputstream();
        fi2 = Imagen.getFile().getInputstream();
        byte[] buffer = new byte[(int) Imagen.getFile().getSize()];
        if (verificarIcono(fi2)) {
            fi.read(buffer);
            UUID id = UUID.randomUUID();
            tipoSensor.setLogoDelTipo(buffer);
            tipoSensor.setUuid(id.toString().toUpperCase());
            SessionOperations.setSessionValue(id.toString().toUpperCase(), buffer);
        }
    }

    public Boolean verificarIcono(InputStream fi) {
        Boolean resultado = Boolean.TRUE;
        try {
            BufferedImage buf = ImageIO.read(fi);
            int height = buf.getHeight();
            int width = buf.getWidth();
            System.out.println(width + " - " + height);
            if (width != 32 && height != 32) {
                resultado = Boolean.FALSE;
                mostrarMensaje(FacesMessage.SEVERITY_ERROR, "ERROR", "La dimension de la imagen debe ser de 32 x 32");
            }
        } catch (Exception e) {
            resultado = Boolean.FALSE;
            //e.printStackTrace();
        }
        return resultado;
    }

    public Boolean verificarFormulario() throws Exception {
        Boolean res = Boolean.TRUE;
        if (tipoSensor.getNombre().trim().length() == 0) {
            res = Boolean.FALSE;
            mostrarMensaje(FacesMessage.SEVERITY_ERROR, "ERROR", "Agregue nombre al tipo del sensor");
        } else {
            if (tipoSensor.getId() == null) {
                TipoSensor ts = (TipoSensor) cb.getByOneFieldWithOneResult(TipoSensor.class, "nombre", tipoSensor.getNombre());
                if (ts != null) {
                    res = Boolean.FALSE;
                    mostrarMensaje(FacesMessage.SEVERITY_ERROR, "ERROR", "Nombre del tipo del sensor ya existe");
                }
            } else {
                TipoSensor ts = (TipoSensor) cb.getByOneFieldWithOneResult(TipoSensor.class, "nombre", tipoSensor.getNombre());
                if (ts != null) {
                    if (!ts.getId().equals(tipoSensor.getId())) {
                        res = Boolean.FALSE;
                        mostrarMensaje(FacesMessage.SEVERITY_ERROR, "ERROR", "Nombre del tipo del sensor ya existe");
                    }
                }
            }
        }
        if (tipoSensor.getUnidadDeMedida().trim().length() == 0) {
            res = Boolean.FALSE;
            mostrarMensaje(FacesMessage.SEVERITY_ERROR, "ERROR", "Agregue unidad de medida al tipo del sensor");
        }
        if (tipoSensor.getLogoDelTipo() == null) {
            res = Boolean.FALSE;
            mostrarMensaje(FacesMessage.SEVERITY_ERROR, "ERROR", "Añade logo del tipo del sensor");
        }
        return res;
    }

    public void cargaTipoDeSensor(TipoSensor row) {
        this.tipoSensor = row;
        UUID id = UUID.randomUUID();
        tipoSensor.setUuid(id.toString().toUpperCase());
        SessionOperations.setSessionValue(id.toString().toUpperCase(), row.getLogoDelTipo());
    }

    public void accionGuarda() throws Exception {
        if (verificarFormulario()) {
            if (cb.guardar(tipoSensor)) {
                mostrarMensaje(FacesMessage.SEVERITY_INFO, "Exitoso", "Se ha guardado el tipo de sensor");
                init();
            } else {
                mostrarMensaje(FacesMessage.SEVERITY_INFO, "Exitoso", "No se ha guardado el tipo de sensor");
            }
        }
    }

    public void bajarIcono() {
        tipoSensor.setLogoDelTipo(null);
    }

    public void mostrarMensaje(FacesMessage.Severity icono, String titulo, String mensaje) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(icono, titulo, mensaje));
    }

    public TipoSensor getTipoSensor() {
        return tipoSensor;
    }

    public void setTipoSensor(TipoSensor tipoSensor) {
        this.tipoSensor = tipoSensor;
    }

    public List<TipoSensor> getTipoDeSensores() {
        return tipoDeSensores;
    }

    public void setTipoDeSensores(List<TipoSensor> tipoDeSensores) {
        this.tipoDeSensores = tipoDeSensores;
    }
}

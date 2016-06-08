/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.ucc.sipnat.vista.ModuloSensor;

import co.edu.ucc.sipnat.base.SessionOperations;
import co.edu.ucc.sipnat.clases.DatosBasicos;
import co.edu.ucc.sipnat.logica.CommonsBean;
import co.edu.ucc.sipnat.modelo.Sensor;
import co.edu.ucc.sipnat.modelo.TipoSensor;
import co.edu.ucc.sipnat.modelo.Usuario;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import org.primefaces.event.map.MarkerDragEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

/**
 *
 * @author Windows 8.1
 */
@ViewScoped
@ManagedBean(name = "MbSensor")
public class MbSensor implements Serializable {

    private List<Sensor> sensores;
    private List<SelectItem> tipoSensorItem;
    private Sensor sensor;
    private Usuario usuario;
    private String latitud;
    private String longitud;
    private MapModel draggableModel;
    private Marker marker;
    private Long idTipoSensor;
    private Long idSensor;
    private Boolean sensorCargado;

    @EJB
    private CommonsBean cb;

    public MbSensor() {

    }

    @PostConstruct
    public void init() {
        usuario = (Usuario) SessionOperations.getSessionValue("USUARIO");
        sensores = cb.getByOneField(Sensor.class, "usuarioCreacion", usuario.getNombreUsuario(), "ORDER BY o.id");
        tipoSensorItem = new LinkedList<>();
        sensor = new Sensor();
        List<TipoSensor> tipoSensores = cb.getAll(TipoSensor.class, "ORDER BY o.nombre");
        for (TipoSensor ts : tipoSensores) {
            tipoSensorItem.add(new SelectItem(ts.getId(), ts.getNombre()));
        }
        draggableModel = new DefaultMapModel();
        latitud = "";
        longitud = "";
        idTipoSensor = null;
        sensorCargado = Boolean.FALSE;
        idSensor = null;
    }

    public void cargaSensor(Sensor row) {
        sensorCargado = Boolean.TRUE;
        this.sensor = row;
        idSensor = row.getId();
        latitud = row.getLatitud();
        longitud = row.getLongitud();
        idTipoSensor = row.getTipoSensor().getId();
        draggableModel = new DefaultMapModel();
        LatLng coord1 = new LatLng(new Double(latitud), new Double(longitud));
        //Draggable
        draggableModel.addOverlay(new Marker(coord1, "Sensor", this, "http://" + DatosBasicos.ip + ":8080/sipnat/imagenServlet?id=" + row.getTipoSensor().getId()));
        for (Marker premarker : draggableModel.getMarkers()) {
            premarker.setDraggable(true);
        }
    }

    public void accionBuscarSensor() {
        if (idSensor == null) {
            mostrarMensaje(FacesMessage.SEVERITY_ERROR, "ERROR", "Ingrese el codigo del sensor");
        } else {
            Sensor s = (Sensor) cb.getById(Sensor.class, idSensor);
            if (s != null) {
                if (s.getUsuarioCreacion().equals(usuario.getNombreUsuario())) {
                    cargaSensor(s);
                } else {
                    mostrarMensaje(FacesMessage.SEVERITY_ERROR, "ERROR", "Error no se puede editar este sensor, tiene que ser el usuario que lo creo");
                }
            } else {
                mostrarMensaje(FacesMessage.SEVERITY_ERROR, "ERROR", "Codigo de sensor no existe");
            }
        }
    }

    public void updateIcon() {
        if (sensor.getId() != null) {
            draggableModel = new DefaultMapModel();
            LatLng coord1 = new LatLng(new Double(latitud), new Double(longitud));
            draggableModel.addOverlay(new Marker(coord1, "Sensor", this, "http://" + DatosBasicos.ip + ":8080/sipnat/imagenServlet?id=" + idTipoSensor));
            for (Marker premarker : draggableModel.getMarkers()) {
                premarker.setDraggable(true);
            }
        } else {
            mostrarMensaje(FacesMessage.SEVERITY_ERROR, "ERROR", "Carga el sensor");
        }
    }

    public void onMarkerDrag(MarkerDragEvent event) {
        marker = event.getMarker();
        latitud = marker.getLatlng().getLat() + "";
        longitud = marker.getLatlng().getLng() + "";
    }

    public Boolean verificarFormulario() {
        Boolean resultado = Boolean.TRUE;
        if (latitud.trim().length() == 0 || longitud.trim().length() == 0) {
            resultado = Boolean.FALSE;
            mostrarMensaje(FacesMessage.SEVERITY_ERROR, "ERROR", "Ubique el sensor");
        } else {
            try {
                Double l = new Double(latitud);
                Double lo = new Double(longitud);
            } catch (Exception e) {
                resultado = Boolean.FALSE;
                mostrarMensaje(FacesMessage.SEVERITY_ERROR, "ERROR", "Latitud o longitud deben ser datos numericos");
            }
        }
        if (sensor.getDescripcion().trim().length() == 0) {
            resultado = Boolean.FALSE;
            mostrarMensaje(FacesMessage.SEVERITY_ERROR, "ERROR", "Agregue descripcion");
        }
        return resultado;
    }

    public void accionActualizar() {
        if (verificarFormulario()) {
            sensor.setLatitud(latitud);
            sensor.setLongitud(longitud);
            sensor.setTipoSensor((TipoSensor) cb.getById(TipoSensor.class, idTipoSensor));
            if (cb.guardar(sensor)) {
                mostrarMensaje(FacesMessage.SEVERITY_INFO, "Exitoso", "Se ha actualizado en sensor");
                init();
            } else {
                mostrarMensaje(FacesMessage.SEVERITY_INFO, "ERROR", "No se ha actualizado en sensor");
            }
        }
    }

    public void mostrarMensaje(FacesMessage.Severity icono, String titulo, String mensaje) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(icono, titulo, mensaje));
    }

    public List<Sensor> getSensores() {
        return sensores;
    }

    public void setSensores(List<Sensor> sensores) {
        this.sensores = sensores;
    }

    public List<SelectItem> getTipoSensorItem() {
        return tipoSensorItem;
    }

    public void setTipoSensorItem(List<SelectItem> tipoSensorItem) {
        this.tipoSensorItem = tipoSensorItem;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public MapModel getDraggableModel() {
        return draggableModel;
    }

    public void setDraggableModel(MapModel draggableModel) {
        this.draggableModel = draggableModel;
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public Long getIdTipoSensor() {
        return idTipoSensor;
    }

    public void setIdTipoSensor(Long idTipoSensor) {
        this.idTipoSensor = idTipoSensor;
    }

    public Long getIdSensor() {
        return idSensor;
    }

    public void setIdSensor(Long idSensor) {
        this.idSensor = idSensor;
    }

    public Boolean getSensorCargado() {
        return sensorCargado;
    }

    public void setSensorCargado(Boolean sensorCargado) {
        this.sensorCargado = sensorCargado;
    }
}

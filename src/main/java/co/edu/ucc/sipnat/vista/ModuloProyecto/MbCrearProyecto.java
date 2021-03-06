/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.ucc.sipnat.vista.ModuloProyecto;

import co.edu.ucc.sipnat.base.SessionOperations;
import co.edu.ucc.sipnat.clases.DatosBasicos;
import co.edu.ucc.sipnat.logica.CommonsBean;
import co.edu.ucc.sipnat.logica.LogicaProyecto;
import co.edu.ucc.sipnat.modelo.Proyecto;
import co.edu.ucc.sipnat.modelo.ProyectoXSensor;
import co.edu.ucc.sipnat.modelo.Sensor;
import co.edu.ucc.sipnat.modelo.TipoSensor;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.map.MarkerDragEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

/**
 * @author Alvaro padilla
 * @see java.io.Serializable
 */
@ViewScoped
@ManagedBean(name = "MbCrearProyecto")
public class MbCrearProyecto implements Serializable {

    private String centro;
    private Proyecto proyecto;
    private Sensor sensor;
    private List<Sensor> sensores;
    private List<Sensor> sensoresYaCreado;
    private List<TipoSensor> tipoSensores;
    private Long idTipoSensor;
    private String latitud;
    private String longitud;
    private MapModel draggableModel;
    private Marker marker;
    @EJB
    private CommonsBean cb;
    @EJB
    private LogicaProyecto lp;

    /**
     * Creates a new instance of MbCrearProyecto
     */
    public MbCrearProyecto() {
    }

    @PostConstruct
    public void init() {
        centro = "11.247141, -74.205504";
        proyecto = new Proyecto();
        sensor = new Sensor();
        sensores = new ArrayList<>();
        sensoresYaCreado = new ArrayList<>();
        tipoSensores = cb.getAll(TipoSensor.class);
        latitud = "";
        longitud = "";
        draggableModel = new DefaultMapModel();
        if (SessionOperations.getSessionValue("PROYECTO") != null) {
            proyecto = (Proyecto) SessionOperations.getSessionValue("PROYECTO");
            List<ProyectoXSensor> pxses = cb.getByOneField(ProyectoXSensor.class, "proyecto", proyecto);
            for (ProyectoXSensor pxs : pxses) {
                sensores.add(pxs.getSensor());
            }
            mostrarSensoresAsignado();

            SessionOperations.setSessionValue("PROYECTO", null);
        }
    }

    public void selecionarTipo(TipoSensor row) {
        for (TipoSensor ts : tipoSensores) {
            if (ts.equals(row)) {
                ts.setSelecionado(Boolean.TRUE);
                idTipoSensor = ts.getId();
                cargaSensores();
                draggableModel = new DefaultMapModel();
                if (!sensores.isEmpty()) {
                    mostrarSensoresAsignado();
                }
                String[] parts = centro.split(",");
                LatLng coord1 = new LatLng(new Double(parts[0]), new Double(parts[1]));

                //Draggable
                draggableModel.addOverlay(new Marker(coord1, "Sensor", this, "http://" + DatosBasicos.ip + ":" + DatosBasicos.port + "/" + DatosBasicos.path + "/imagenServlet?id=" + idTipoSensor));
                for (Marker premarker : draggableModel.getMarkers()) {
                    premarker.setDraggable(true);
                }
            } else {
                ts.setSelecionado(Boolean.FALSE);
            }
        }
    }

    public void cargaSensores() {
        if (idTipoSensor != null) {
            sensoresYaCreado = cb.getByOneField(Sensor.class, "tipoSensor.id", idTipoSensor);
        }
    }

    public void onMarkerDrag(MarkerDragEvent event) {
        marker = event.getMarker();
        latitud = marker.getLatlng().getLat() + "";
        longitud = marker.getLatlng().getLng() + "";
        centro = marker.getLatlng().getLat() + "," + marker.getLatlng().getLng();
    }

    public Boolean verificarFormularioDelSensor() {
        Boolean res = Boolean.TRUE;
        if (idTipoSensor == null) {
            res = Boolean.FALSE;
            mostrarMensaje(FacesMessage.SEVERITY_ERROR, "ERROR", "Escoja un tipo de sensor");
        }
        if (latitud.trim().length() == 0 || longitud.trim().length() == 0) {
            res = Boolean.FALSE;
            mostrarMensaje(FacesMessage.SEVERITY_ERROR, "ERROR", "Ubique el sensor");
        } else {
            try {
                Double l = new Double(latitud);
                Double lo = new Double(longitud);
            } catch (Exception e) {
                res = Boolean.FALSE;
                mostrarMensaje(FacesMessage.SEVERITY_ERROR, "ERROR", "Latitud o longitud deben ser datos numericos");
            }
        }
        if (sensor.getDescripcion().trim().length() == 0) {
            res = Boolean.FALSE;
            mostrarMensaje(FacesMessage.SEVERITY_ERROR, "ERROR", "Agregue Descripcion del sensor");
        }
        return res;
    }

    public void accionAsignarSensor() {
        if (verificarFormularioDelSensor()) {
            sensor.setLatitud(latitud);
            sensor.setLongitud(longitud);
            sensor.setTipoSensor((TipoSensor) cb.getById(TipoSensor.class, idTipoSensor));
            sensores.add(sensor);
            centroZona();
            mostrarSensoresAsignado();

            String[] parts = centro.split(",");
            LatLng coord1 = null;
            if (sensores.size() == 1) {
                System.out.println("2da Cordenada");
                coord1 = new LatLng(new Double(parts[0]) + 0.010000, new Double(parts[1]) + 0.010000);
                System.out.println("lat " + coord1.getLat());
                System.out.println("lon " + coord1.getLng());
            } else {
                coord1 = new LatLng(new Double(parts[0]), new Double(parts[1]));
            }
            longitud = "";
            latitud = "";
            //Draggable

            draggableModel.addOverlay(new Marker(coord1, "Sensor", this, "http://" + DatosBasicos.ip + ":" + DatosBasicos.port + "/" + DatosBasicos.path + "/imagenServlet?id=" + idTipoSensor));

            int mak = draggableModel.getMarkers().size() - 1;
            Marker perMarker = draggableModel.getMarkers().get(mak);
            perMarker.setDraggable(true);

            sensor = new Sensor();
        }
    }

    public void accionAgregarSensor(Sensor row) {
        if (sensores.isEmpty()) {
            sensores.add(row);
            sensoresYaCreado.remove(row);
        } else {
            Boolean verificacion = Boolean.TRUE;
            for (Sensor sensore : sensores) {
                if (sensore.getId() != null) {
                    if (sensore.getId().equals(row.getId())) {
                        verificacion = Boolean.FALSE;
                        mostrarMensaje(FacesMessage.SEVERITY_ERROR, "ERROR", "Sensor ya asignado");
                    }
                }
            }
            if (verificacion) {
                sensores.add(row);
                sensoresYaCreado.remove(row);
                mostrarSensoresAsignado();
            }
        }
    }

    public void accionRemoverSensor(Sensor row) {
        if (row.getId() == null) {
            sensores.remove(row);
        } else if (proyecto.getId() == null) {
            sensores.remove(row);
            sensoresYaCreado.add(row);
        } else {
            ProyectoXSensor pxs = lp.buscarRelacion(proyecto, row);
            if (cb.remove(pxs)) {
                mostrarMensaje(FacesMessage.SEVERITY_INFO, "Alvertencia", "Sensor retirado");
                sensores.remove(row);
                sensoresYaCreado.add(row);
            } else {
                mostrarMensaje(FacesMessage.SEVERITY_ERROR, "ERROR", "No se pudo guarda");
            }
        }
        mostrarSensoresAsignado();
    }

    public void accionVerSensor(Sensor row) {
        for (TipoSensor ts : tipoSensores) {
            ts.setSelecionado(Boolean.FALSE);
        }
        idTipoSensor = null;
        draggableModel = new DefaultMapModel();
        centro = row.getLatitud() + "," + row.getLongitud();
        LatLng coord1 = new LatLng(new Double(row.getLatitud()), new Double(row.getLongitud()));
        //Draggable
        draggableModel.addOverlay(new Marker(coord1, "Sensor", this, "http://" + DatosBasicos.ip + ":" + DatosBasicos.port + "/" + DatosBasicos.path + "/imagenServlet?id=" + row.getTipoSensor().getId()));
    }

    public void accionVerTodoLosSensores() {
        for (TipoSensor ts : tipoSensores) {
            ts.setSelecionado(Boolean.FALSE);
        }
        idTipoSensor = null;
        draggableModel = new DefaultMapModel();
        centroZona();
        for (Sensor row : sensores) {

            LatLng coord1 = new LatLng(new Double(row.getLatitud()), new Double(row.getLongitud()));
            //Draggable
            draggableModel.addOverlay(new Marker(coord1, "Sensor", this, "http://" + DatosBasicos.ip + ":8080/sipnat/imagenServlet?id=" + row.getTipoSensor().getId()));
        }

    }

    public void mostrarSensoresAsignado() {
        for (TipoSensor ts : tipoSensores) {
            ts.setSelecionado(Boolean.FALSE);
        }
        draggableModel = new DefaultMapModel();
        centroZona();
        for (Sensor row : sensores) {
            LatLng coord1 = new LatLng(new Double(row.getLatitud()), new Double(row.getLongitud()));
            //Draggable
            draggableModel.addOverlay(new Marker(coord1, "Sensor", this, "http://" + DatosBasicos.ip + ":" + DatosBasicos.port + "/" + DatosBasicos.path + "/imagenServlet?id=" + row.getTipoSensor().getId()));
        }
    }

    public void accionGuarda() {
        try {
            if (verificarFormulario()) {
                if (lp.guardaProyecto(sensores, proyecto)) {
                    mostrarMensaje(FacesMessage.SEVERITY_INFO, "Exitoso", "Se ha guardado");
                    SessionOperations.setSessionValue("PROYECTO", proyecto);
                    redirect("verproyecto.xhtml");
                } else {
                    mostrarMensaje(FacesMessage.SEVERITY_ERROR, "ERROR", "No se ha guarado");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void redirect(String url) {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            context.getExternalContext().redirect(url);
        } catch (IOException ex) {
            Logger.getLogger(MbVerProyecto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Boolean verificarFormulario() throws Exception {
        Boolean resultado = Boolean.TRUE;
        if (sensores.isEmpty()) {
            resultado = Boolean.FALSE;
            mostrarMensaje(FacesMessage.SEVERITY_ERROR, "ERROR", "Asigne sensores");
        }
        if (proyecto.getNombre().trim().length() == 0) {
            resultado = Boolean.FALSE;
            mostrarMensaje(FacesMessage.SEVERITY_ERROR, "ERROR", "Agregue nombre");
        } else {
            Proyecto p = (Proyecto) cb.getByOneFieldWithOneResult(Proyecto.class, "nombre", proyecto.getNombre());
            if (p != null) {
                if (!p.getId().equals(proyecto.getId())) {
                    resultado = Boolean.FALSE;
                    mostrarMensaje(FacesMessage.SEVERITY_ERROR, "ERROR", "Nombre de proyecto ya exite");
                }
            }
        }

        if (proyecto.getDescripcion().trim().length() == 0) {
            resultado = Boolean.FALSE;
            mostrarMensaje(FacesMessage.SEVERITY_ERROR, "ERROR", "Agregue Descricion");
        }

        if (proyecto.getAlertaNivel1().trim().length() == 0) {
            resultado = Boolean.FALSE;
            mostrarMensaje(FacesMessage.SEVERITY_ERROR, "ERROR", "Agregue nivel de alerta 1");
        }
        if (proyecto.getAlertaNivel2().trim().length() == 0) {
            resultado = Boolean.FALSE;
            mostrarMensaje(FacesMessage.SEVERITY_ERROR, "ERROR", "Agregue nivel de alerta 2");
        }
        if (proyecto.getAlertaNivel3().trim().length() == 0) {
            resultado = Boolean.FALSE;
            mostrarMensaje(FacesMessage.SEVERITY_ERROR, "ERROR", "Agregue nivel de alerta 3");
        }
        return resultado;
    }

    public LatLng centroZona() {
        List<LatLng> latLngs = new ArrayList<>();

        for (Sensor dz : sensores) {
            latLngs.add(new LatLng(Double.parseDouble(dz.getLatitud()), Double.parseDouble(dz.getLongitud())));
        }
        //Polygon p = advancedModel.getPolygons().get(0);

        //latitud menor
        LatLng latmenor = latLngs.get(0);
        LatLng latMayor = latLngs.get(0);
        for (int i = 0; i < latLngs.size(); i++) {
            if (latLngs.get(i).getLat() < latmenor.getLat()) {
                latmenor = latLngs.get(i);
            }
            if (latLngs.get(i).getLat() > latMayor.getLat()) {
                latMayor = latLngs.get(i);
            }
        }

        //longitud menor
        LatLng lonmenor = latLngs.get(0);
        LatLng lonMayor = latLngs.get(0);
        for (int i = 0; i < latLngs.size(); i++) {
            if (latLngs.get(i).getLng() < lonmenor.getLng()) {
                lonmenor = latLngs.get(i);
            }
            if (latLngs.get(i).getLng() > lonMayor.getLng()) {
                lonMayor = latLngs.get(i);
            }
        }

        double dLat = latMayor.getLat() - latmenor.getLat();
        double dLng = lonMayor.getLng() - lonmenor.getLng();
        double sindLat = dLat / 2;
        double sindLng = dLng / 2;

        LatLng coord1 = new LatLng(sindLat + latmenor.getLat(), sindLng + lonmenor.getLng());
        //advancedModel.addOverlay(new Marker(coord1, zonaAfectada.getNombreDelaZona()));
        centro = coord1.getLat() + "," + coord1.getLng();
        return coord1;
    }

    public void mostrarMensaje(FacesMessage.Severity icono, String titulo, String mensaje) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(icono, titulo, mensaje));
    }

    public List<TipoSensor> getTipoSensores() {
        return tipoSensores;
    }

    public void setTipoSensores(List<TipoSensor> tipoSensores) {
        this.tipoSensores = tipoSensores;
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

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    public Long getIdTipoSensor() {
        return idTipoSensor;
    }

    public void setIdTipoSensor(Long idTipoSensor) {
        this.idTipoSensor = idTipoSensor;
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

    public Proyecto getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }

    public List<Sensor> getSensores() {
        return sensores;
    }

    public void setSensores(List<Sensor> sensores) {
        this.sensores = sensores;
    }

    public List<Sensor> getSensoresYaCreado() {
        return sensoresYaCreado;
    }

    public void setSensoresYaCreado(List<Sensor> sensoresYaCreado) {
        this.sensoresYaCreado = sensoresYaCreado;
    }

    public String getCentro() {
        return centro;
    }

    public void setCentro(String centro) {
        this.centro = centro;
    }

}

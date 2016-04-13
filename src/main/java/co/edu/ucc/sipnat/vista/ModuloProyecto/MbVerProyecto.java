/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.ucc.sipnat.vista.ModuloProyecto;

import co.edu.ucc.sipnat.base.SessionOperations;
import co.edu.ucc.sipnat.clases.DatosBasicos;
import co.edu.ucc.sipnat.logica.CommonsBean;
import co.edu.ucc.sipnat.logica.LogicaSensor;
import co.edu.ucc.sipnat.modelo.DatosSensor;
import co.edu.ucc.sipnat.modelo.Proyecto;
import co.edu.ucc.sipnat.modelo.ProyectoXSensor;
import co.edu.ucc.sipnat.modelo.Sensor;
import co.edu.ucc.sipnat.modelo.Usuario;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.DateAxis;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

/**
 *
 * @author Windows 8.1
 */
@ViewScoped
@ManagedBean(name = "MbVerProyecto")
public class MbVerProyecto implements Serializable {

    private List<Proyecto> proyectos;
    private List<ProyectoXSensor> sensores;
    private List<Sensor> listSensores;
    private Proyecto proyecto;
    private Usuario usuario;
    private MapModel draggableModel;
    private Marker marker;
    private Boolean mostarPopupDeGrafica;
    private Boolean mostrarGraficaGeneral;
    private LineChartModel dataModelSensor;
    private LineChartModel dataModelSensores;
    private Long idProyecto;

    @EJB
    private CommonsBean cb;

    @EJB
    private LogicaSensor ls;

    public MbVerProyecto() {
    }

    @PostConstruct
    public void init() {
        usuario = (Usuario) SessionOperations.getSessionValue("USUARIO");
        proyectos = cb.getAll(Proyecto.class);
        proyecto = new Proyecto();
        mostarPopupDeGrafica = Boolean.FALSE;
        mostrarGraficaGeneral = Boolean.FALSE;
        idProyecto = null;
        draggableModel = new DefaultMapModel();
        if (SessionOperations.getSessionValue("PROYECTO") != null) {
            proyecto = (Proyecto) SessionOperations.getSessionValue("PROYECTO");
            cargaProyecto(proyecto);
            SessionOperations.setSessionValue("PROYECTO", null);
        }
    }

    public void buscarProyecto() {
        if (idProyecto != null) {
            Proyecto p = (Proyecto) cb.getById(Proyecto.class, idProyecto);
            if (p != null) {
                cargaProyecto(p);
            } else {
                mostrarMensaje(FacesMessage.SEVERITY_ERROR, "ERROR", "Codigo no existe");
            }
        } else {
            mostrarMensaje(FacesMessage.SEVERITY_ERROR, "ERROR", "Agregar codigo");
        }
    }

    public void cargaProyecto(Proyecto row) {
        this.proyecto = row;
        idProyecto = row.getId();
        sensores = cb.getByOneField(ProyectoXSensor.class, "proyecto", row);
        draggableModel = new DefaultMapModel();
        listSensores = new ArrayList<>();
        for (ProyectoXSensor pxs : sensores) {
            listSensores.add(pxs.getSensor());
            LatLng coord1 = new LatLng(new Double(pxs.getSensor().getLatitud()), new Double(pxs.getSensor().getLongitud()));
            draggableModel.addOverlay(new Marker(coord1, pxs.getSensor().getId() + "", this, "http://"+DatosBasicos.ip+":8080/sipnat/imagenServlet?id=" + pxs.getSensor().getTipoSensor().getId()));
        }
        cargaGrafica();
    }

    public void cargaGrafica() {
        LineChartModel model = new LineChartModel();
        for (Sensor s : listSensores) {
            List<DatosSensor> dses = ls.obtenerDatos(s.getId());
            if (!dses.isEmpty()) {
                mostrarGraficaGeneral = Boolean.TRUE;
                model.addSeries(armaSerie(dses, s.getId().toString()));
            }
        }
        if (mostrarGraficaGeneral) {
            dataModelSensores = model;
            dataModelSensores.setAnimate(true);
            dataModelSensores.setTitle("Datos Recolectado");
            dataModelSensores.setLegendPosition("e");
            dataModelSensores.getAxis(AxisType.Y).setLabel("Datos");
            DateAxis axis = new DateAxis("Tiempo");
            axis.setTickAngle(-50);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            axis.setMax(formatter.format(new Date()));
            axis.setTickFormat("%b %#d, %y");
            dataModelSensores.getAxes().put(AxisType.X, axis);
        }
    }

    public LineChartSeries armaSerie(List<DatosSensor> dses, String id) {
        LineChartSeries series1 = new LineChartSeries();
        series1.setLabel("Codigo: " + id);
        for (DatosSensor dse : dses) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            series1.set(formatter.format(dse.getFechaRecoleccion()), Integer.parseInt(dse.getDato()));
        }
        return series1;
    }

    public void onMarkerSelect(OverlaySelectEvent event) {
        marker = (Marker) event.getOverlay();
        List<DatosSensor> dses = ls.obtenerDatos(new Long(marker.getTitle()));
        if (!dses.isEmpty()) {
            mostarPopupDeGrafica = Boolean.TRUE;
            dataModelSensor = initLinearModel(dses, marker.getTitle());
            dataModelSensor.setAnimate(true);
            dataModelSensor.setTitle("Datos Recolectado");
            dataModelSensor.setLegendPosition("e");
            dataModelSensor.getAxis(AxisType.Y).setLabel("Datos");
            DateAxis axis = new DateAxis("Tiempo");
            axis.setTickAngle(-50);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            axis.setMax(formatter.format(new Date()));
            axis.setTickFormat("%b %#d, %y");
            dataModelSensor.getAxes().put(AxisType.X, axis);
        } else {
            mostrarMensaje(FacesMessage.SEVERITY_INFO, "Alvertencia", "Sin datos");
        }
    }

    public LineChartModel initLinearModel(List<DatosSensor> dses, String id) {
        LineChartModel model = new LineChartModel();
        LineChartSeries series1 = new LineChartSeries();
        series1.setLabel("Codigo: " + id);
        for (DatosSensor dse : dses) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            series1.set(formatter.format(dse.getFechaRecoleccion()), Integer.parseInt(dse.getDato()));
        }
        model.addSeries(series1);
        return model;
    }

    public void ocultarPopup() {
        mostarPopupDeGrafica = Boolean.FALSE;
    }

    public void editarProyecto() {
        if (proyecto != null) {
            if (proyecto.getUsuarioCreacion().equals(usuario.getNombreUsuario())) {
                SessionOperations.setSessionValue("PROYECTO", proyecto);
                redirect("proyecto.xhtml");
            } else {
                mostrarMensaje(FacesMessage.SEVERITY_ERROR, "ERROR", "No puede editar proyecto, no le pertenece");
            }
        } else {
            mostrarMensaje(FacesMessage.SEVERITY_ERROR, "ERROR", "Cargue proyecto");
        }
    }

    public void mostrarMensaje(FacesMessage.Severity icono, String titulo, String mensaje) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(icono, titulo, mensaje));
    }

    private void redirect(String url) {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            context.getExternalContext().redirect(url);
        } catch (IOException ex) {
            Logger.getLogger(MbVerProyecto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<Proyecto> getProyectos() {
        return proyectos;
    }

    public void setProyectos(List<Proyecto> proyectos) {
        this.proyectos = proyectos;
    }

    public Proyecto getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
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

    public Boolean getMostarPopupDeGrafica() {
        return mostarPopupDeGrafica;
    }

    public void setMostarPopupDeGrafica(Boolean mostarPopupDeGrafica) {
        this.mostarPopupDeGrafica = mostarPopupDeGrafica;
    }

    public LineChartModel getDataModelSensor() {
        return dataModelSensor;
    }

    public void setDataModelSensor(LineChartModel dataModelSensor) {
        this.dataModelSensor = dataModelSensor;
    }

    public Boolean getMostrarGraficaGeneral() {
        return mostrarGraficaGeneral;
    }

    public void setMostrarGraficaGeneral(Boolean mostrarGraficaGeneral) {
        this.mostrarGraficaGeneral = mostrarGraficaGeneral;
    }

    public LineChartModel getDataModelSensores() {
        return dataModelSensores;
    }

    public void setDataModelSensores(LineChartModel dataModelSensores) {
        this.dataModelSensores = dataModelSensores;
    }

    public Long getIdProyecto() {
        return idProyecto;
    }

    public void setIdProyecto(Long idProyecto) {
        this.idProyecto = idProyecto;
    }

}

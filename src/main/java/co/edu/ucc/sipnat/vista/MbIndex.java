/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.ucc.sipnat.vista;

import co.edu.ucc.sipnat.clases.DatosBasicos;
import co.edu.ucc.sipnat.logica.CommonsBean;
import co.edu.ucc.sipnat.logica.LogicaSensor;
import co.edu.ucc.sipnat.modelo.DatosSensor;
import co.edu.ucc.sipnat.modelo.Proyecto;
import co.edu.ucc.sipnat.modelo.ProyectoXSensor;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
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
@ManagedBean(name = "MbIndex")
public class MbIndex implements Serializable {

    private List<SelectItem> proyectoItems;
    private List<ProyectoXSensor> sensores;
    private Long idProyecto;
    private MapModel draggableModel;
    private Marker marker;
    private LineChartModel dataModelSensor;
    private Boolean mostarPopupDeGrafica;

    @EJB
    private CommonsBean cb;

    @EJB
    private LogicaSensor ls;

    public MbIndex() {
    }

    @PostConstruct
    public void init() {
        mostarPopupDeGrafica = Boolean.FALSE;
        proyectoItems = new LinkedList<>();
        List<Proyecto> proyectos = cb.getAll(Proyecto.class, "ORDER BY o.id");
        for (Proyecto proyecto : proyectos) {
            proyectoItems.add(new SelectItem(proyecto.getId(), proyecto.getNombre()));
        }
        idProyecto = proyectos.get(1).getId();
        cargaProyecto();
    }

    public void cargaProyecto() {
        if (idProyecto != null) {
            Proyecto p = (Proyecto) cb.getById(Proyecto.class, idProyecto);
            sensores = cb.getByOneField(ProyectoXSensor.class, "proyecto", p);
            draggableModel = new DefaultMapModel();
            for (ProyectoXSensor pxs : sensores) {
                LatLng coord1 = new LatLng(new Double(pxs.getSensor().getLatitud()), new Double(pxs.getSensor().getLongitud()));               
                draggableModel.addOverlay(new Marker(coord1, pxs.getSensor().getId() + "", this, "http://"+DatosBasicos.ip+":8080/sipnat/imagenServlet?id=" + pxs.getSensor().getTipoSensor().getId()));
            }
        }
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

    public void mostrarMensaje(FacesMessage.Severity icono, String titulo, String mensaje) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(icono, titulo, mensaje));
    }

    public List<SelectItem> getProyectoItems() {
        return proyectoItems;
    }

    public void setProyectoItems(List<SelectItem> proyectoItems) {
        this.proyectoItems = proyectoItems;
    }

    public List<ProyectoXSensor> getSensores() {
        return sensores;
    }

    public void setSensores(List<ProyectoXSensor> sensores) {
        this.sensores = sensores;
    }

    public Long getIdProyecto() {
        return idProyecto;
    }

    public void setIdProyecto(Long idProyecto) {
        this.idProyecto = idProyecto;
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

    public LineChartModel getDataModelSensor() {
        return dataModelSensor;
    }

    public void setDataModelSensor(LineChartModel dataModelSensor) {
        this.dataModelSensor = dataModelSensor;
    }

    public Boolean getMostarPopupDeGrafica() {
        return mostarPopupDeGrafica;
    }

    public void setMostarPopupDeGrafica(Boolean mostarPopupDeGrafica) {
        this.mostarPopupDeGrafica = mostarPopupDeGrafica;
    }
}

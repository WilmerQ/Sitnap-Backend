/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.ucc.sipnat.vista.ModuloProyecto;

import co.edu.ucc.sipnat.base.SessionOperations;
import co.edu.ucc.sipnat.logica.CommonsBean;
import co.edu.ucc.sipnat.modelo.CordenadaDeLaZona;
import co.edu.ucc.sipnat.modelo.Proyecto;
import co.edu.ucc.sipnat.modelo.Usuario;
import co.edu.ucc.sipnat.modelo.ZonaAfectada;
import co.edu.ucc.sipnat.modelo.ZonaXProyecto;
import java.io.Serializable;
import java.util.ArrayList;
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
import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import org.primefaces.model.map.Polygon;

/**
 *
 * @author Windows 8.1
 */
@ViewScoped
@ManagedBean(name = "MbZonaAfecta")
public class MbZonaAfecta implements Serializable {

    private Usuario usuario;
    private List<Proyecto> proyectos;
    private List<CordenadaDeLaZona> listaCordenadaDeLaZona;
    private ZonaAfectada zonaAfectada;

    private MapModel advancedModel;
    private Marker marker;

    private List<SelectItem> colorItems;
    private List<Marker> listaMarker;

    public MbZonaAfecta() {
    }

    @EJB
    private CommonsBean cb;

    @PostConstruct
    public void init() {
        usuario = (Usuario) SessionOperations.getSessionValue("USUARIO");
        zonaAfectada = new ZonaAfectada();
        proyectos = cb.getByOneField(Proyecto.class, "usuarioCreacion", usuario.getNombreUsuario());
        listaCordenadaDeLaZona = new ArrayList<>();

        advancedModel = new DefaultMapModel();
        colorItems = new LinkedList<>();
        listaMarker = new ArrayList<>();

        colorItems.add(new SelectItem("#FF9900", "Zapote"));
        colorItems.add(new SelectItem("#3200ff", "Azul"));
        colorItems.add(new SelectItem("#65FF00", "Verde"));
        colorItems.add(new SelectItem("#FF0000", "Rojo"));
        colorItems.add(new SelectItem("#e5FF00", "Amarillo"));
    }

    public void agragarMarker() {
        if (!listaCordenadaDeLaZona.isEmpty()) {
            advancedModel = new DefaultMapModel();
            for (CordenadaDeLaZona cdlz : listaCordenadaDeLaZona) {
                advancedModel.addOverlay(cdlz.getMarker());
            }
        }
        LatLng coord1 = new LatLng(11.247141, -74.205504);
        int numero = listaCordenadaDeLaZona.size() + 1;
        advancedModel.addOverlay(new Marker(coord1, numero + ""));
        for (Marker premarker : getAdvancedModel().getMarkers()) {
            premarker.setDraggable(true);
        }
    }

    public void dibujar() {
        if (zonaAfectada.getNombreDelaZona().trim().length() > 0) {
            if (listaCordenadaDeLaZona.size() > 1) {
                advancedModel = new DefaultMapModel();
                Polygon polygon = new Polygon();

                //Polygon
                for (CordenadaDeLaZona cdlz : listaCordenadaDeLaZona) {
                    System.out.println(cdlz.getOrden());
                    polygon.getPaths().add(cdlz.getMarker().getLatlng());
                }

                polygon.setStrokeColor("#FF0000");
                polygon.setFillColor("#FF0000");
                polygon.setStrokeOpacity(0.7);
                polygon.setFillOpacity(0.5);
                advancedModel.addOverlay(polygon);

                numeroPoligono();
            } else {
                mostrarMensaje(FacesMessage.SEVERITY_ERROR, "ERROR", "Tiene que agregar mas de un punto");
            }
        } else {
            mostrarMensaje(FacesMessage.SEVERITY_ERROR, "ERROR", "Agregue nombre de la zona afectada");
        }
    }

    public void numeroPoligono() {
        Polygon p = advancedModel.getPolygons().get(0);
        List<LatLng> latLngs = p.getPaths();

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
        advancedModel.addOverlay(new Marker(coord1, zonaAfectada.getNombreDelaZona()));
    }

    public void onMarkerDrag(MarkerDragEvent event) {
        marker = event.getMarker();
        if (listaCordenadaDeLaZona.isEmpty()) {
            CordenadaDeLaZona cdlz = new CordenadaDeLaZona();
            cdlz.setMarker(marker);
            cdlz.setLatitud(marker.getLatlng().getLat() + "");
            cdlz.setLongitud(marker.getLatlng().getLng() + "");
            cdlz.setOrden(1);
            listaCordenadaDeLaZona.add(cdlz);
        } else {
            Boolean entro = Boolean.FALSE;
            for (CordenadaDeLaZona cordenadaDeLaZona : listaCordenadaDeLaZona) {
                if (cordenadaDeLaZona.getMarker().getId().equals(marker.getId())) {
                    entro = Boolean.TRUE;
                    cordenadaDeLaZona.setMarker(marker);
                    cordenadaDeLaZona.setLatitud(marker.getLatlng().getLat() + "");
                    cordenadaDeLaZona.setLongitud(marker.getLatlng().getLng() + "");
                    break;
                }
            }
            if (!entro) {
                CordenadaDeLaZona cdlz = new CordenadaDeLaZona();
                cdlz.setMarker(marker);
                cdlz.setLatitud(marker.getLatlng().getLat() + "");
                cdlz.setLongitud(marker.getLatlng().getLng() + "");
                cdlz.setOrden(listaCordenadaDeLaZona.size() + 1);
                listaCordenadaDeLaZona.add(cdlz);
            }
        }
    }

    public void accionGuarda() {
        if (verificarFormulario()) {
            try {
                if (cb.guardar(zonaAfectada)) {
                    for (CordenadaDeLaZona cdlz : listaCordenadaDeLaZona) {
                        cdlz.setZonaAfectada(zonaAfectada);
                        cb.guardar(cdlz);
                    }
                    for (Proyecto p : proyectos) {
                        if (p.getSelecionado()) {
                            ZonaXProyecto zonaXProyecto = new ZonaXProyecto();
                            zonaXProyecto.setProyecto(p);
                            zonaXProyecto.setZonaAfectada(zonaAfectada);
                            cb.guardar(zonaXProyecto);
                        }
                    }
                    mostrarMensaje(FacesMessage.SEVERITY_INFO, "Exitoso", "Se ha guardado");
                    init();
                }
            } catch (Exception e) {
                mostrarMensaje(FacesMessage.SEVERITY_ERROR, "ERROR", "No se ha guardado");
            }

        }
    }

    private Boolean verificarFormulario() {
        Boolean resultado = Boolean.TRUE;

        if (listaCordenadaDeLaZona.isEmpty()) {
            resultado = Boolean.FALSE;
            mostrarMensaje(FacesMessage.SEVERITY_ERROR, "ERROR", "Pinte la zona afectada");
        } else if (listaCordenadaDeLaZona.size() == 1) {
            resultado = Boolean.FALSE;
            mostrarMensaje(FacesMessage.SEVERITY_ERROR, "ERROR", "Tiene que agregar mas de un punto");
        }

        Boolean tenemosSelecionado = Boolean.FALSE;
        for (Proyecto p : proyectos) {
            if (p.getSelecionado()) {
                tenemosSelecionado = Boolean.TRUE;
                break;
            }
        }

        if (!tenemosSelecionado) {
            resultado = Boolean.FALSE;
            mostrarMensaje(FacesMessage.SEVERITY_ERROR, "ERROR", "Selecione al menos un proyecto que le pertenesca la zona");
        }

        return resultado;
    }

    public void onMarkerSelect(OverlaySelectEvent event) {
        marker = (Marker) event.getOverlay();
    }

    public void mostrarMensaje(FacesMessage.Severity icono, String titulo, String mensaje) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(icono, titulo, mensaje));
    }

    public MapModel getAdvancedModel() {
        return advancedModel;
    }

    public void setAdvancedModel(MapModel advancedModel) {
        this.advancedModel = advancedModel;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<Proyecto> getProyectos() {
        return proyectos;
    }

    public void setProyectos(List<Proyecto> proyectos) {
        this.proyectos = proyectos;
    }

    public List<CordenadaDeLaZona> getListaCordenadaDeLaZona() {
        return listaCordenadaDeLaZona;
    }

    public void setListaCordenadaDeLaZona(List<CordenadaDeLaZona> listaCordenadaDeLaZona) {
        this.listaCordenadaDeLaZona = listaCordenadaDeLaZona;
    }

    public ZonaAfectada getZonaAfectada() {
        return zonaAfectada;
    }

    public void setZonaAfectada(ZonaAfectada zonaAfectada) {
        this.zonaAfectada = zonaAfectada;
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public List<SelectItem> getColorItems() {
        return colorItems;
    }

    public void setColorItems(List<SelectItem> colorItems) {
        this.colorItems = colorItems;
    }

    public List<Marker> getListaMarker() {
        return listaMarker;
    }

    public void setListaMarker(List<Marker> listaMarker) {
        this.listaMarker = listaMarker;
    }

}

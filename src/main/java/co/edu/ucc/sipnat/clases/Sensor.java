/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.ucc.sipnat.clases;

/**
 *
 * @author Windows 8.1
 */
public class Sensor {

    private Long id;
    private String latitud;
    private String longitud;
    private String nombreTipoSensor;
    private String descripcion;
    private String estado;
    private Long idTipoSensor;

    public Sensor(co.edu.ucc.sipnat.modelo.Sensor s) {
        this.id = s.getId();
        this.latitud = s.getLatitud();
        this.longitud = s.getLongitud();
        this.estado = s.getEstadoDelSensor();
        this.idTipoSensor = s.getTipoSensor().getId();
        this.nombreTipoSensor = s.getTipoSensor().getNombre();
        this.descripcion = s.getDescripcion();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getIdTipoSensor() {
        return idTipoSensor;
    }

    public void setIdTipoSensor(Long idTipoSensor) {
        this.idTipoSensor = idTipoSensor;
    }

    public String getNombreTipoSensor() {
        return nombreTipoSensor;
    }

    public void setNombreTipoSensor(String nombreTipoSensor) {
        this.nombreTipoSensor = nombreTipoSensor;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}

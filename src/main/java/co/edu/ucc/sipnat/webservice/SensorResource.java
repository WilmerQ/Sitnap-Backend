/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.ucc.sipnat.webservice;

import co.edu.ucc.sipnat.logica.CommonsBean;
import co.edu.ucc.sipnat.modelo.Auditoria;
import co.edu.ucc.sipnat.modelo.DatosSensor;
import co.edu.ucc.sipnat.modelo.Sensor;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;

/**
 * REST Web Service
 *
 * @author Windows 8.1
 */
@Path("Sensor")
@Stateless
public class SensorResource {

    @Context
    private UriInfo context;

    @EJB
    private CommonsBean cb;

    /**
     * Creates a new instance of SensorResource
     */
    public SensorResource() {
    }

    /**
     * Retrieves representation of an instance of
     * co.edu.ucc.sipnat.base.SensorResource
     *
     * @param codigoSensor
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    @Path("/{codigoSensor}")
    public String darDeAlta(@PathParam("codigoSensor") String codigoSensor) throws Exception {
        Auditoria auditoria;
        if (codigoSensor.trim().length() == 0) {
            auditoria = new Auditoria();
            auditoria.setMetodo("Dar de alta");
            auditoria.setCausaDelError("Dato esta vacio");
            auditoria.setFechaYHoraDelaCausa(new Date());
            cb.guardar(auditoria);
            return "1"; //dato null
        } else {
            Sensor sensor = (Sensor) cb.getById(Sensor.class, new Long(codigoSensor));
            if (sensor == null) {
                auditoria = new Auditoria();
                auditoria.setMetodo("Dar de alta");
                auditoria.setCausaDelError("Codigo erroneo: " + codigoSensor);
                auditoria.setFechaYHoraDelaCausa(new Date());
                cb.guardar(auditoria);
                return "1"; //codigo de sensor erroneo
            } else {
                if (!sensor.getEstadoDelSensor().equals("Conectado")) {
                    sensor.setEstadoDelSensor("Conectado");
                    if (cb.guardar(sensor)) {
                        auditoria = new Auditoria();
                        auditoria.setMetodo("Dar de alta");
                        auditoria.setCausaDelError("Correcto " + sensor.getId());
                        auditoria.setFechaYHoraDelaCausa(new Date());
                        cb.guardar(auditoria);
                        return "2";//correto
                    } else {
                        auditoria = new Auditoria();
                        auditoria.setMetodo("Dar de alta");
                        auditoria.setCausaDelError("Error interno en el ejb, codigo de sensor " + codigoSensor);
                        auditoria.setFechaYHoraDelaCausa(new Date());
                        cb.guardar(auditoria);
                        return "1";//Error en el ejb
                    }
                } else {
                    auditoria = new Auditoria();
                    auditoria.setMetodo("Dar de alta");
                    auditoria.setCausaDelError("ya esta conectado verifique codigo enviado, Codigo sensor " + codigoSensor);
                    auditoria.setFechaYHoraDelaCausa(new Date());
                    cb.guardar(auditoria);
                    return "1";//ya esta conectado verifique codigo enviado
                }
            }
        }
    }
    
    @GET
    @Produces("application/json")
    @Path("/{codigoSensor}/{fecha}")
    public String desconectar(@PathParam("codigoSensor") String codigoSensor, @PathParam("fecha") String fecha) throws Exception {
        Auditoria auditoria;
        if (codigoSensor.trim().length() == 0) {
            auditoria = new Auditoria();
            auditoria.setMetodo("desconectar");
            auditoria.setCausaDelError("Dato esta vacio");
            auditoria.setFechaYHoraDelaCausa(new Date());
            cb.guardar(auditoria);
            return "1"; //dato null
        } else {
            Sensor sensor = (Sensor) cb.getById(Sensor.class, new Long(codigoSensor));
            if (sensor == null) {
                auditoria = new Auditoria();
                auditoria.setMetodo("desconectar");
                auditoria.setCausaDelError("Codigo erroneo: " + codigoSensor);
                auditoria.setFechaYHoraDelaCausa(new Date());
                cb.guardar(auditoria);
                return "1"; //codigo de sensor erroneo
            } else {
                if (sensor.getEstadoDelSensor().equals("Conectado")) {
                    sensor.setEstadoDelSensor("Desconectado");
                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                    sensor.setFechaDeDesconexion(formatter.parse(fecha));
                    if (cb.guardar(sensor)) {
                        auditoria = new Auditoria();
                        auditoria.setMetodo("Dar de alta");
                        auditoria.setCausaDelError("Correcto " + sensor.getId());
                        auditoria.setFechaYHoraDelaCausa(new Date());
                        cb.guardar(auditoria);
                        return "2";//correto
                    } else {
                        auditoria = new Auditoria();
                        auditoria.setMetodo("desconectar");
                        auditoria.setCausaDelError("Error interno en el ejb, codigo de sensor " + codigoSensor);
                        auditoria.setFechaYHoraDelaCausa(new Date());
                        cb.guardar(auditoria);
                        return "1";//Error en el ejb
                    }
                } else {
                    auditoria = new Auditoria();
                    auditoria.setMetodo("desconectar");
                    auditoria.setCausaDelError("no esta conectado verifique codigo enviado, Codigo sensor " + codigoSensor);
                    auditoria.setFechaYHoraDelaCausa(new Date());
                    cb.guardar(auditoria);
                    return "1";//ya esta conectado verifique codigo enviado
                }
            }
        }
    }

    /**
     * Retrieves representation of an instance of
     * co.edu.ucc.sipnat.base.SensorResource
     *
     * @param codigo
     * @param dato
     * @param fechaRecoleccion
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    @Path("/{codigo}/{dato}/{fechaRecoleccion}/{horaRecoleccion}")
    public String obtenerDato(@PathParam("codigo") String codigo, @PathParam("dato") String dato, @PathParam("fechaRecoleccion") String fechaRecoleccion, @PathParam("horaRecoleccion") String horaRecoleccion) throws Exception {
        Auditoria auditoria;
        if (codigo.trim().length() == 0) {
            auditoria = new Auditoria();
            auditoria.setMetodo("Obteniendo dato");
            auditoria.setCausaDelError("Codigo de sensor vacio");
            auditoria.setFechaYHoraDelaCausa(new Date());
            cb.guardar(auditoria);
            return "1"; //dato null
        } else {
            Sensor sensor = (Sensor) cb.getById(Sensor.class, new Long(codigo));
            if (sensor == null) {
                auditoria = new Auditoria();
                auditoria.setMetodo("Obteniendo dato");
                auditoria.setCausaDelError("codigo de sensor erroneo: " + codigo);
                auditoria.setFechaYHoraDelaCausa(new Date());
                cb.guardar(auditoria);
                return "1"; //codigo de sensor erroneo
            } else {
                if (sensor.getEstadoDelSensor().equals("Conectado")) {
                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                    DatosSensor ds = new DatosSensor();
                    try {
                        Double d = new Double(dato);
                        ds.setDato(dato);
                        ds.setFechaRecoleccion(formatter.parse(fechaRecoleccion + " " + horaRecoleccion));
                        ds.setFechaSincronizacion(new Date());
                        ds.setHoraDeRecoleccion(horaRecoleccion);
                        ds.setSensor(sensor);
                        if (cb.guardar(ds)) {
                            return "2";//correto
                        } else {
                            auditoria = new Auditoria();
                            auditoria.setMetodo("Obteniendo dato");
                            auditoria.setCausaDelError("Error en el ejb, codigo de sensor " + codigo);
                            auditoria.setFechaYHoraDelaCausa(new Date());
                            cb.guardar(auditoria);
                            return "1";//Error en el ejb
                        }
                    } catch (Exception e) {
                        auditoria = new Auditoria();
                        auditoria.setMetodo("Obteniendo dato");
                        auditoria.setCausaDelError("Error en el dato, el dato debe ser numerico Codigo de sensor" + codigo);
                        auditoria.setFechaYHoraDelaCausa(new Date());
                        cb.guardar(auditoria);
                        return "1";//Error en el dato
                    }
                } else {
                    auditoria = new Auditoria();
                    auditoria.setMetodo("Obteniendo dato");
                    auditoria.setCausaDelError("Codigo erroneo: " + codigo);
                    auditoria.setFechaYHoraDelaCausa(new Date());
                    cb.guardar(auditoria);
                    return "1";//Sensor no conectado
                }
            }
        }
    }

    /**
     * PUT method for updating or creating an instance of SensorResource
     *
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/json")
    public void putJson(String content) {
    }
}

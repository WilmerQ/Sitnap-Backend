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
            Sensor sensor = (Sensor) cb.getByOneFieldWithOneResult(Sensor.class, "codigoHexadecimal", codigoSensor);
            if (sensor == null) {
                auditoria = new Auditoria();
                auditoria.setMetodo("Dar de alta");
                auditoria.setCausaDelError("Codigo erroneo");
                auditoria.setFechaYHoraDelaCausa(new Date());
                cb.guardar(auditoria);
                return "1"; //codigo de sensor erroneo
            } else {
                if (!sensor.getEstadoDelSensor().equals("Conectado")) {
                    sensor.setEstadoDelSensor("Conectado");
                    if (cb.guardar(sensor)) {
                        return "2";//correto
                    } else {
                        auditoria = new Auditoria();
                        auditoria.setMetodo("Dar de alta");
                        auditoria.setCausaDelError("Error interno en el ejb");
                        auditoria.setFechaYHoraDelaCausa(new Date());
                        return "1";//Error en el ejb
                    }
                } else {
                    auditoria = new Auditoria();
                    auditoria.setMetodo("Dar de alta");
                    auditoria.setCausaDelError("ya esta conectado verifique codigo enviado");
                    auditoria.setFechaYHoraDelaCausa(new Date());
                    return "1";//ya esta conectado verifique codigo enviado
                }
            }

        }
    }

    /**
     * Retrieves representation of an instance of
     * co.edu.ucc.sipnat.base.SensorResource
     *
     * @param codigoSensor
     * @param dato
     * @param fechaRecoleccion
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    @Path("/{codigoSensor}/{dato}/{fechaRecoleccion}")
    public String obtenerDato(@PathParam("codigoSensor") String codigoSensor, @PathParam("dato") String dato, @PathParam("fechaRecoleccion") Date fechaRecoleccion) throws Exception {
        Auditoria auditoria;
        if (codigoSensor.trim().length() == 0) {
            auditoria = new Auditoria();
            auditoria.setMetodo("Obteniendo dato");
            auditoria.setCausaDelError("Codigo de sensor vacio");
            auditoria.setFechaYHoraDelaCausa(new Date());
            cb.guardar(auditoria);
            return "1"; //dato null
        } else {
            Sensor sensor = (Sensor) cb.getByOneFieldWithOneResult(Sensor.class, "codigoHexadecimal", codigoSensor);
            if (sensor == null) {
                auditoria = new Auditoria();
                auditoria.setMetodo("Obteniendo dato");
                auditoria.setCausaDelError("codigo de sensor erroneo");
                auditoria.setFechaYHoraDelaCausa(new Date());
                cb.guardar(auditoria);
                return "1"; //codigo de sensor erroneo
            } else {
                if (sensor.getEstadoDelSensor().equals("Conectado")) {
                    DatosSensor ds = new DatosSensor();
                    ds.setDato(dato);
                    ds.setFechaRecoleccion(fechaRecoleccion);
                    ds.setFechaSincronizacion(new Date());
                    ds.setSensor(sensor);
                    if (cb.guardar(ds)) {
                        return "2";//correto
                    } else {
                        auditoria = new Auditoria();
                        auditoria.setMetodo("Obteniendo dato");
                        auditoria.setCausaDelError("Error en el ejb");
                        auditoria.setFechaYHoraDelaCausa(new Date());
                        cb.guardar(auditoria);
                        return "1";//Error en el ejb
                    }
                } else {
                    auditoria = new Auditoria();
                    auditoria.setMetodo("Obteniendo dato");
                    auditoria.setCausaDelError("Codigo erroneo");
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

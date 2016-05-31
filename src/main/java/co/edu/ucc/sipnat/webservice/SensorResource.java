/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.ucc.sipnat.webservice;

import co.edu.ucc.sipnat.base.GsonExcludeListStrategy;
import co.edu.ucc.sipnat.logica.CommonsBean;
import co.edu.ucc.sipnat.logica.LogicaAlerta;
import co.edu.ucc.sipnat.modelo.Auditoria;
import co.edu.ucc.sipnat.modelo.DatosSensor;
import co.edu.ucc.sipnat.modelo.Sensor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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

    @EJB
    private LogicaAlerta la;

    /**
     * Creates a new instance of SensorResource
     */
    public SensorResource() {
    }

    @GET
    @Produces("application/json")
    public String getSensores() {
        List<Sensor> sensores = cb.getAll(Sensor.class);
        List<co.edu.ucc.sipnat.clases.Sensor> listSensores = new ArrayList<>();
        for (Sensor sensore : sensores) {
            co.edu.ucc.sipnat.clases.Sensor s = new co.edu.ucc.sipnat.clases.Sensor(sensore);
            listSensores.add(s);
        }
        Gson g = new GsonBuilder().setExclusionStrategies(new GsonExcludeListStrategy()).setPrettyPrinting().create();
        return g.toJson(listSensores);
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
            Long id = 0L;
            try {
                id = new Long(codigoSensor);
            } catch (Exception e) {
                auditoria = new Auditoria();
                auditoria.setMetodo("Dar de alta");
                auditoria.setCausaDelError("Codigo debe ser numerico: " + codigoSensor);
                auditoria.setFechaYHoraDelaCausa(new Date());
                cb.guardar(auditoria);
                return "1";
            }
            Sensor sensor = (Sensor) cb.getById(Sensor.class, id);
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
                        auditoria.setCausaDelError("Correcto, se ha conectado sensor: " + sensor.getId());
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
            Long id = 0L;
            try {
                id = new Long(codigoSensor);
            } catch (Exception e) {
                auditoria = new Auditoria();
                auditoria.setMetodo("desconectar");
                auditoria.setCausaDelError("Codigo debe ser numerico: " + codigoSensor);
                auditoria.setFechaYHoraDelaCausa(new Date());
                cb.guardar(auditoria);
                return "1";
            }
            Sensor sensor = (Sensor) cb.getById(Sensor.class, id);
            if (sensor == null) {
                auditoria = new Auditoria();
                auditoria.setMetodo("desconectar");
                auditoria.setCausaDelError("Codigo erroneo: " + codigoSensor);
                auditoria.setFechaYHoraDelaCausa(new Date());
                cb.guardar(auditoria);
                return "1"; //codigo de sensor erroneo
            } else {
                if (sensor.getEstadoDelSensor().equals("Conectado")) {
                    if (verificarFecha(fecha)) {
                        sensor.setEstadoDelSensor("Desconectado");
                        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                        sensor.setFechaDeDesconexion(formatter.parse(fecha));
                        if (cb.guardar(sensor)) {
                            auditoria = new Auditoria();
                            auditoria.setMetodo("Desconectar");
                            auditoria.setCausaDelError("Correcto, se ha desconectado sensor: " + sensor.getId());
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
                        auditoria.setCausaDelError("Error en formato de fecha: " + fecha + " Codigo sensor " + codigoSensor);
                        auditoria.setFechaYHoraDelaCausa(new Date());
                        cb.guardar(auditoria);
                        return "1";
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
            Long id = 0L;
            try {
                id = new Long(codigo);
            } catch (Exception e) {
                auditoria = new Auditoria();
                auditoria.setMetodo("Obteniendo dato");
                auditoria.setCausaDelError("Codigo debe ser numerico: " + codigo);
                auditoria.setFechaYHoraDelaCausa(new Date());
                cb.guardar(auditoria);
                return "1";
            }
            final Sensor sensor = (Sensor) cb.getById(Sensor.class, id);
            if (sensor == null) {
                auditoria = new Auditoria();
                auditoria.setMetodo("Obteniendo dato");
                auditoria.setCausaDelError("codigo de sensor erroneo: " + codigo);
                auditoria.setFechaYHoraDelaCausa(new Date());
                cb.guardar(auditoria);
                return "1"; //codigo de sensor erroneo
            } else {
                if (sensor.getEstadoDelSensor().equals("Conectado")) {
                    if (verificarFecha(fechaRecoleccion)) {
                        if (verificarHora(horaRecoleccion)) {
                            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                            DatosSensor ds = new DatosSensor();
                            try {
                                Double d = new Double(dato);
                                ds.setDato(dato);
                                ds.setFechaRecoleccion(formatter.parse(fechaRecoleccion + " " + horaRecoleccion));
                                ds.setFechaSincronizacion(new Date());
                                ds.setHoraDeRecoleccion(horaRecoleccion);
                                ds.setSensor(sensor);
                                ds.setRevisado(Boolean.FALSE);
                                if (cb.guardar(ds)) {
                                    System.out.println("Dato ingresado: " + dato + " Fecha: "+ ds.getFechaRecoleccion() + " Sensor: " + sensor.getId());
                                    final Sensor s = sensor;
                                    final DatosSensor ds1 = ds;
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            System.out.println("Ejecutando alarta");
                                            la.accionMandarAlarta(s, ds1);
                                        }
                                    }).start();
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
                                auditoria.setCausaDelError("Error en el dato, el dato debe ser numerico, Codigo de sensor: " + codigo);
                                auditoria.setFechaYHoraDelaCausa(new Date());
                                cb.guardar(auditoria);
                                return "1";//Error en el dato
                            }
                        } else {
                            auditoria = new Auditoria();
                            auditoria.setMetodo("Obteniendo dato");
                            auditoria.setCausaDelError("Error en formato de hora: " + horaRecoleccion + " Codigo sensor " + codigo);
                            auditoria.setFechaYHoraDelaCausa(new Date());
                            cb.guardar(auditoria);
                            return "1";//Fecha mala
                        }
                    } else {
                        auditoria = new Auditoria();
                        auditoria.setMetodo("Obteniendo dato");
                        auditoria.setCausaDelError("Error en formato de fecha: " + fechaRecoleccion + " Codigo sensor " + codigo);
                        auditoria.setFechaYHoraDelaCausa(new Date());
                        cb.guardar(auditoria);
                        return "1";//Fecha mala
                    }
                } else {
                    auditoria = new Auditoria();
                    auditoria.setMetodo("Obteniendo dato");
                    auditoria.setCausaDelError("Sensor desconectado codigo: " + codigo);
                    auditoria.setFechaYHoraDelaCausa(new Date());
                    cb.guardar(auditoria);
                    return "1";//Sensor no conectado
                }
            }
        }
    }

    private Boolean verificarFecha(String cadena) {
        Boolean resultado = Boolean.TRUE;
        String[] campos = cadena.split("-");
        
        if (campos.length != 3) {
            resultado = Boolean.FALSE;
        } else {
        
            if (campos[0].length() > 2 || campos[0].length() < 1) {
                resultado = Boolean.FALSE;
            } else {
                try {
                    Long nu = new Long(campos[0]);
                } catch (Exception e) {
                    resultado = Boolean.FALSE;
                }
            }
        
            if (campos[1].length() > 2 && campos[1].length() < 1) {
                resultado = Boolean.FALSE;
            } else {
                try {
                    Long nu = new Long(campos[1]);
                } catch (Exception e) {
                    resultado = Boolean.FALSE;
                }
            }
        
            if (campos[2].length() != 4) {
                resultado = Boolean.FALSE;
            } else {
                try {
                    Long nu = new Long(campos[2]);
                } catch (Exception e) {
                    resultado = Boolean.FALSE;
                }
            }
        }
        return resultado;
    }

    private Boolean verificarHora(String cadena) {
        Boolean resultado = Boolean.TRUE;
        String[] campos = cadena.split(":");
        
        if (campos.length != 3) {
            resultado = Boolean.FALSE;
        } else {
        
            if (campos[0].length() > 2 || campos[0].length() < 1) {
                resultado = Boolean.FALSE;
            } else {
                try {
                    Long nu = new Long(campos[0]);
                } catch (Exception e) {
                    resultado = Boolean.FALSE;
                }
            }
        
            if (campos[1].length() > 2 && campos[1].length() < 1) {
                resultado = Boolean.FALSE;
            } else {
                try {
                    Long nu = new Long(campos[1]);
                } catch (Exception e) {
                    resultado = Boolean.FALSE;
                }
            }
        
            if (campos[2].length() > 2 && campos[2].length() < 1) {
                resultado = Boolean.FALSE;
            } else {
                try {
                    Long nu = new Long(campos[2]);
                } catch (Exception e) {
                    resultado = Boolean.FALSE;
                }
            }
        }
        return resultado;
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.ucc.sipnat.webservice;

import co.edu.ucc.sipnat.base.GsonExcludeListStrategy;
import co.edu.ucc.sipnat.logica.CommonsBean;
import co.edu.ucc.sipnat.modelo.Proyecto;
import co.edu.ucc.sipnat.modelo.ProyectoXSensor;
import co.edu.ucc.sipnat.modelo.Sensor;
import co.edu.ucc.sipnat.modelo.TipoSensor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ibcaribe.procc.services.FieldtoQuery;
import java.util.ArrayList;
import java.util.List;
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
@Path("SensorMovil")
@Stateless
public class SensorMovilResource {

    @Context
    private UriInfo context;

    @EJB
    private CommonsBean cb;

    /**
     * Creates a new instance of SensorMovilResource
     */
    public SensorMovilResource() {
    }

    /**
     * Retrieves representation of an instance of
     * co.edu.ucc.sipnat.webservice.SensorMovilResource
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    public String getJson() {
        List<Sensor> sensores = cb.getByOneField(Sensor.class, "estadoDelSensor", "Conectado");
        List<co.edu.ucc.sipnat.clases.Sensor> listSensores = new ArrayList<>();
        for (Sensor sensore : sensores) {
            co.edu.ucc.sipnat.clases.Sensor s = new co.edu.ucc.sipnat.clases.Sensor(sensore);
            listSensores.add(s);
        }
        Gson g = new GsonBuilder().setExclusionStrategies(new GsonExcludeListStrategy()).setPrettyPrinting().create();
        return g.toJson(listSensores);
    }

    @GET
    @Produces("application/json")
    @Path("/{id}")
    public String getSensorXProyecto(@PathParam("id") String id) {
        Proyecto p = (Proyecto) cb.getById(Proyecto.class, new Long(id));
        List<FieldtoQuery> fqs = new ArrayList<>();
        fqs.add(new FieldtoQuery("proyecto", p));
        fqs.add(new FieldtoQuery("sensor.estadoDelSensor", "Conectado"));
        List<ProyectoXSensor> list = cb.getByManyFields(ProyectoXSensor.class, fqs);
        List<co.edu.ucc.sipnat.clases.Sensor> listSensores = new ArrayList<>();
        List<Sensor> sensores = new ArrayList<>();
        for (ProyectoXSensor list1 : list) {
            Sensor s1 = list1.getSensor();
            co.edu.ucc.sipnat.clases.Sensor s = new co.edu.ucc.sipnat.clases.Sensor(s1);
            listSensores.add(s);
            
        }
        Gson g = new GsonBuilder().setExclusionStrategies(new GsonExcludeListStrategy()).setPrettyPrinting().create();
        return g.toJson(listSensores);
    }

    /**
     * PUT method for updating or creating an instance of SensorMovilResource
     *
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/json")
    public void putJson(String content) {
    }
}

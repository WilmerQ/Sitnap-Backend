/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.ucc.sipnat.webservice;

import co.edu.ucc.sipnat.base.GsonExcludeListStrategy;
import co.edu.ucc.sipnat.clases.Dato;
import co.edu.ucc.sipnat.logica.CommonsBean;
import co.edu.ucc.sipnat.modelo.DatosSensor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
@Path("dato")
@Stateless
public class DatoResource {

    @Context
    private UriInfo context;

    @EJB
    private CommonsBean cb;

    /**
     * Creates a new instance of DatoResource
     */
    public DatoResource() {
    }

    /**
     * Retrieves representation of an instance of
     * co.edu.ucc.sipnat.webservice.DatoResource
     *
     * @param id
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    @Path("/{id}")
    public String getJson(@PathParam("id") String id) {
        List<DatosSensor> dses = cb.getByOneField(DatosSensor.class, "sensor.id", new Long(id),"ORDER BY o.fechaRecoleccion ASC");
        List<Dato> datos = new ArrayList<>();
        for (DatosSensor ds : dses) {
            datos.add(new Dato(ds));
        }
        Gson g = new GsonBuilder().setExclusionStrategies(new GsonExcludeListStrategy()).setPrettyPrinting().create();
        return g.toJson(datos);
    }

    /**
     * PUT method for updating or creating an instance of DatoResource
     *
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/json")
    public void putJson(String content) {
    }
}

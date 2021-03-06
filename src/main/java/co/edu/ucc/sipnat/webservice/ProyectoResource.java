/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.ucc.sipnat.webservice;

import co.edu.ucc.sipnat.base.GsonExcludeListStrategy;
import co.edu.ucc.sipnat.logica.CommonsBean;
import co.edu.ucc.sipnat.modelo.Proyecto;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
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
@Path("Proyecto")
@Stateless
public class ProyectoResource {

    @Context
    private UriInfo context;

    @EJB
    private CommonsBean cb;

    /**
     * Creates a new instance of ProyectoResource
     */
    public ProyectoResource() {
    }

    /**
     * Retrieves representation of an instance of
     * co.edu.ucc.sipnat.webservice.ProyectoResource
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    public String getJson() {
        List<Proyecto> proyectos = cb.getAll(Proyecto.class);
        List<co.edu.ucc.sipnat.clases.Proyecto> list = new ArrayList<>();
        for (Proyecto proyecto : proyectos) {
            list.add(new co.edu.ucc.sipnat.clases.Proyecto(proyecto));
        }
        Gson g = new GsonBuilder().setExclusionStrategies(new GsonExcludeListStrategy()).setPrettyPrinting().create();
        return g.toJson(list);
    }

    @GET
    @Produces("application/json")
    @Path("/{id}")
    public String getProyectoXUsuario(@PathParam("id") String id) {
        List<Proyecto> p = cb.getByOneField(Proyecto.class, "usuarioCreacion", id);
        List<Long> idP = new ArrayList<>();
        List<co.edu.ucc.sipnat.clases.Proyecto> listp = new ArrayList<>();
        for (Proyecto pr : p) {
            listp.add(new co.edu.ucc.sipnat.clases.Proyecto(pr));
            idP.add(pr.getId());
        }
        if (!idP.isEmpty()) {
            List<Proyecto> list = cb.getNotIn(Proyecto.class, "id", idP);
            for (Proyecto list1 : list) {
                listp.add(new co.edu.ucc.sipnat.clases.Proyecto(list1));
                p.add(list1);
            }
        } else {
            List<Proyecto> proyectos = cb.getAll(Proyecto.class);
            listp = new ArrayList<>();
            for (Proyecto proyecto : proyectos) {
                listp.add(new co.edu.ucc.sipnat.clases.Proyecto(proyecto));
            }
        }
        Gson g = new GsonBuilder().setExclusionStrategies(new GsonExcludeListStrategy()).setPrettyPrinting().create();
        return g.toJson(listp);
    }

    @GET
    @Produces("application/json")
    @Path("/{id}/{dos}")
    public String getProyectos(@PathParam("id") String id, @PathParam("dos") String dos) {
        Type listType = new TypeToken<LinkedList<Long>>() {
        }.getType();
        List<Long> ids = new Gson().fromJson(id, listType);
        List<Proyecto> p = cb.getIn(Proyecto.class, "id", ids);
        List<co.edu.ucc.sipnat.clases.Proyecto> listp = new ArrayList<>();
        for (Proyecto pr : p) {
            listp.add(new co.edu.ucc.sipnat.clases.Proyecto(pr));

        }
        Gson g = new GsonBuilder().setExclusionStrategies(new GsonExcludeListStrategy()).setPrettyPrinting().create();
        return g.toJson(listp);
    }

    /**
     * PUT method for updating or creating an instance of ProyectoResource
     *
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/json")
    public void putJson(String content) {
    }
}

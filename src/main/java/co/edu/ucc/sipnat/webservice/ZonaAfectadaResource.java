/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.ucc.sipnat.webservice;

import co.edu.ucc.sipnat.base.GsonExcludeListStrategy;
import co.edu.ucc.sipnat.clases.DetalleZona;
import co.edu.ucc.sipnat.clases.Zona;
import co.edu.ucc.sipnat.logica.CommonsBean;
import co.edu.ucc.sipnat.modelo.CordenadaDeLaZona;
import co.edu.ucc.sipnat.modelo.ZonaXProyecto;
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
@Path("ZonaAfectada")
@Stateless
public class ZonaAfectadaResource {

    @Context
    private UriInfo context;

    @EJB
    private CommonsBean cb;

    /**
     * Creates a new instance of ZonaAfectadaResource
     */
    public ZonaAfectadaResource() {
    }

    /**
     * Retrieves representation of an instance of
     * co.edu.ucc.sipnat.base.ZonaAfectadaResource
     *
     * @param id
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    @Path("/{id}")
    public String getZonaAfectada(@PathParam("id") String id) {
        List<ZonaXProyecto> zxps = cb.getByOneField(ZonaXProyecto.class, "proyecto.id", new Long(id));
        List<Zona> zonas = new ArrayList<>();
        for (ZonaXProyecto zxp : zxps) {
            Zona z = new Zona();
            z.setNombre(zxp.getZonaAfectada().getNombreDelaZona());
            List<CordenadaDeLaZona> cdlzs = cb.getByOneField(CordenadaDeLaZona.class, "zonaAfectada", zxp.getZonaAfectada());
            List<DetalleZona> dzs = new ArrayList<>();
            for (CordenadaDeLaZona cdlz : cdlzs) {
                System.out.println("Asinado cordenada");
                DetalleZona dz = new DetalleZona();
                dz.setLatitud(cdlz.getLatitud());
                dz.setLongitud(cdlz.getLongitud());
                dz.setOrden(cdlz.getOrden());
                System.out.println(dz.getLatitud()+" "+dz.getLongitud());
                dzs.add(dz);
                
            }
            z.setDetalleZonas(dzs);
            zonas.add(z);
        }
        Gson g = new GsonBuilder().setPrettyPrinting().create();
        return g.toJson(zonas);
    }

    /**
     * PUT method for updating or creating an instance of ZonaAfectadaResource
     *
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/json")
    public void putJson(String content) {
    }
}

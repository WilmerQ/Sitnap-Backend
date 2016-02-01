/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.ucc.sipnat.webservice;

import co.edu.ucc.sipnat.logica.CommonsBean;
import co.edu.ucc.sipnat.modelo.Usuario;
import static com.google.common.hash.Hashing.md5;
import com.ibcaribe.procc.services.Md5;
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
@Path("CambioContra")
@Stateless
public class CambioContraResource {

    @Context
    private UriInfo context;

    @EJB
    private CommonsBean cb;

    /**
     * Creates a new instance of CambioContraResource
     */
    public CambioContraResource() {
    }

    /**
     * Retrieves representation of an instance of
     * co.edu.ucc.sipnat.webservice.CambioContraResource
     *
     * @param id
     * @param clave
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    @Path("/{id}/{clave}")
    public String getJson(@PathParam("id") String id, @PathParam("clave") String clave) throws Exception {
        Usuario u = (Usuario) cb.getByOneFieldWithOneResult(Usuario.class, "nombreUsuario", id);
        if (u == null) {
            return "fail";
        } else {
            u.setClave(Md5.getEncoddedString(clave));
            if (cb.guardar(u)) {
                return "ok";
            } else {
                return "fail";
            }
        }
    }

    /**
     * PUT method for updating or creating an instance of CambioContraResource
     *
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/json")
    public void putJson(String content) {
    }
}

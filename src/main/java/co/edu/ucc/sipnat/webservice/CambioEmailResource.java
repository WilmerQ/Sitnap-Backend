/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.ucc.sipnat.webservice;

import co.edu.ucc.sipnat.logica.CommonsBean;
import co.edu.ucc.sipnat.modelo.Usuario;
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
@Path("CambioEmail")
@Stateless
public class CambioEmailResource {

    @Context
    private UriInfo context;

    @EJB
    private CommonsBean cb;

    /**
     * Creates a new instance of CambioEmailResource
     */
    public CambioEmailResource() {
    }

    /**
     * Retrieves representation of an instance of
     * co.edu.ucc.sipnat.webservice.CambioEmailResource
     *
     * @param id
     * @param clave
     * @return an instance of java.lang.String
     * @throws java.lang.Exception
     */
    @GET
    @Produces("application/json")
    @Path("/{id}/{email}/{telefono}")
    public String getJson(@PathParam("id") String id, @PathParam("email") String email, @PathParam("telefono") String telefono) throws Exception {
        Usuario u = (Usuario) cb.getByOneFieldWithOneResult(Usuario.class, "nombreUsuario", id);
        if (u == null) {
            return "fail";
        } else {
            u.setEmail(email);
            u.setTelefono(telefono);
            if (cb.guardar(u)) {
                return "ok";
            } else {
                return "fail";
            }
        }
    }

    /**
     * PUT method for updating or creating an instance of CambioEmailResource
     *
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/xml")
    public void putXml(String content) {
    }
}

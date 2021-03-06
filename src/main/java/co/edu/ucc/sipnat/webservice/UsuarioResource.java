/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.ucc.sipnat.webservice;

import co.edu.ucc.sipnat.base.GsonExcludeListStrategy;
import co.edu.ucc.sipnat.base.Md5;
import co.edu.ucc.sipnat.logica.CommonsBean;
import co.edu.ucc.sipnat.logica.LogicaLogin;
import co.edu.ucc.sipnat.modelo.Usuario;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
@Path("Usuario")
@Stateless
public class UsuarioResource {

    @Context
    private UriInfo context;

    @EJB
    private LogicaLogin ll;

    @EJB
    private CommonsBean cb;

    /**
     * Creates a new instance of UsuarioResource
     */
    public UsuarioResource() {
    }

    /**
     * Retrieves representation of an instance of
     * co.edu.ucc.sipnat.webservice.UsuarioResource
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    public String getJson() {
        List<Usuario> usuarios = cb.getAll(Usuario.class);
        Gson g = new GsonBuilder().setExclusionStrategies(new GsonExcludeListStrategy()).setPrettyPrinting().create();
        return g.toJson(usuarios);
    }

    @GET
    @Produces("application/json")
    @Path("/{id}/{clave}")
    public String getUsuarioLogin(@PathParam("id") String id, @PathParam("clave") String clave) {
        Gson g = new GsonBuilder().setExclusionStrategies(new GsonExcludeListStrategy()).setPrettyPrinting().create();
        System.out.println(id + " " + clave);
        Usuario usuario = ll.login(id, clave);
        if (usuario == null) {
            usuario = new Usuario();
            usuario.setInformeDeError(1);
            return g.toJson(usuario);
        } else {
            usuario.setInformeDeError(2);
            return g.toJson(usuario);
        }
    }
    
    @GET
    @Produces("application/json")
    @Path("/{id}")
    public String getUsuario(@PathParam("id") String id) {
        Gson g = new GsonBuilder().setExclusionStrategies(new GsonExcludeListStrategy()).setPrettyPrinting().create();
        Usuario usuario = (Usuario) cb.getById(Usuario.class, new Long(id));
        if (usuario == null) {
            usuario = new Usuario();
            usuario.setInformeDeError(1);
            return g.toJson(usuario);
        } else {
            usuario.setInformeDeError(2);
            return g.toJson(usuario);
        }
    }

    /**
     *
     * @param id
     * @param clave
     * @param email
     * @param telefono
     * @return
     * @throws java.lang.Exception
     */
    @GET
    @Produces("application/json")
    @Path("/{id}/{clave}/{email}/{telefono}")
    public String getNewUsuario(@PathParam("id") String id, @PathParam("clave") String clave, @PathParam("email") String email, @PathParam("telefono") String telefono) throws Exception {
        Usuario u = (Usuario) cb.getByOneFieldWithOneResult(Usuario.class, "nombreUsuario", id);
        if (u != null) {
            return "exite";
        } else {
            u = new Usuario();
            u.setNombreUsuario(id);
            u.setEmail(email);
            u.setTelefono(telefono);
            u.setClave(Md5.getEncoddedString(clave));
            if (cb.guardar(u)) {
                return "ok";
            } else {
                return "fail";
            }
        }
    }

    /**
     * PUT method for updating or creating an instance of UsuarioResource
     *
     * @param content representation for the resourcle
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/json")
    public void putJson(String content) {
    }
}

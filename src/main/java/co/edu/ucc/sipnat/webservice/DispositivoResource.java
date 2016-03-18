/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.ucc.sipnat.webservice;

import co.edu.ucc.sipnat.logica.CommonsBean;
import co.edu.ucc.sipnat.modelo.Dispositivo;
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
@Path("dispositivos")
@Stateless
public class DispositivoResource {

    @Context
    private UriInfo context;
    
     @EJB
    private CommonsBean cb;

    /**
     * Creates a new instance of DispositivoResource
     */
    public DispositivoResource() {
    }

    /**
     * Retrieves representation of an instance of co.edu.ucc.sipnat.webservice.DispositivoResource
     * @param token
     * @param imei
     * @return an instance of java.lang.String
     */
    @GET
    @Path("/{token}/{imei}")
    @Produces("application/json")
    public String guardaDato(@PathParam("token") String token, @PathParam("imei") String imei) {
        if(token.trim().length() > 0){
            if(imei.trim().length() > 0){
                Dispositivo d = new Dispositivo();
                d.setImei(imei);
                d.setToken(token);
                if(cb.guardar(d)){
                    return "Ok";
                }else{
                    return "Error";
                }
            }else{
                return "Error";
            }
        }else{
            return "Error";
        }
    }

    /**
     * PUT method for updating or creating an instance of DispositivoResource
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/json")
    public void putJson(String content) {
    }
}

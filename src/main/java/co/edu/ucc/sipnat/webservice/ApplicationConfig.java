/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.ucc.sipnat.webservice;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author Windows 8.1
 */
@javax.ws.rs.ApplicationPath("webresources")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(co.edu.ucc.sipnat.webservice.CambioContraResource.class);
        resources.add(co.edu.ucc.sipnat.webservice.DatoResource.class);
        resources.add(co.edu.ucc.sipnat.webservice.ProyectoResource.class);
        resources.add(co.edu.ucc.sipnat.webservice.SensorMovilResource.class);
        resources.add(co.edu.ucc.sipnat.webservice.SensorResource.class);
        resources.add(co.edu.ucc.sipnat.webservice.UsuarioResource.class);
    }
    
}

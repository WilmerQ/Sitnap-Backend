/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.ucc.sipnat.logica;

import co.edu.ucc.sipnat.modelo.Proyecto;
import co.edu.ucc.sipnat.modelo.ProyectoXSensor;
import co.edu.ucc.sipnat.modelo.Sensor;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Windows 8.1
 */
@Stateless
@LocalBean
public class LogicaProyecto {

    @PersistenceContext(unitName = "SIPNATPU")
    private EntityManager em;

    @EJB
    private CommonsBean cb;

    public Boolean guardaProyecto(List<Sensor> sensores, Proyecto proyecto) {
        try {
            if (cb.guardar(proyecto)) {
                for (Sensor s : sensores) {
                    if (s.getId() == null) {
                        s.setProyectoPadre(proyecto);
                        if (cb.guardar(s)) {
                            ProyectoXSensor pxs = buscarRelacion(proyecto, s);
                            if (pxs == null) {
                                pxs = new ProyectoXSensor();
                                pxs.setProyecto(proyecto);
                                pxs.setSensor(s);
                                if (!cb.guardar(pxs)) {
                                }
                            }
                        }
                    } else {
                        ProyectoXSensor pxs = buscarRelacion(proyecto, s);
                        if (pxs == null) {
                            pxs = new ProyectoXSensor();
                            pxs.setProyecto(proyecto);
                            pxs.setSensor(s);
                            if (!cb.guardar(pxs)) {
                            }
                        }
                    }
                }
            } else {
                return Boolean.FALSE;
            }
            return Boolean.TRUE;
        } catch (Exception e) {
            return Boolean.FALSE;
        }
    }

    public ProyectoXSensor buscarRelacion(Proyecto p, Sensor s) {
        try {
            return (ProyectoXSensor) em.createQuery("SELECT p FROM ProyectoXSensor p WHERE p.proyecto=:pr AND p.sensor =:s").setParameter("pr", p).setParameter("s", s).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }
}

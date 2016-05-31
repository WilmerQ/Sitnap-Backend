/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.ucc.sipnat.logica;

import co.edu.ucc.sipnat.modelo.DatosSensor;
import co.edu.ucc.sipnat.modelo.Sensor;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Windows 8.1
 */
@Stateless
@LocalBean
public class LogicaSensor {

    @PersistenceContext(unitName = "SIPNATPU")
    private EntityManager em;

    @EJB
    private CommonsBean cb;

    public List<DatosSensor> obtenerDatos(Long id) {
        return em.createQuery("SELECT d FROM DatosSensor d WHERE d.sensor.id =:s ORDER BY d.fechaCreacion DESC").setParameter("s", id).getResultList();
    }

    public List<DatosSensor> obtenerDatos(Long id, Date fi, Date ff) {
        return em.createQuery("SELECT d FROM DatosSensor d WHERE d.sensor.id =:s AND (d.fechaRecoleccion BETWEEN :fi AND :ff ) ORDER BY d.fechaRecoleccion ASC").setParameter("s", id).setParameter("fi", fi).setParameter("ff", ff).getResultList();
    }

    public List<DatosSensor> obtenerDatos(List<Sensor> s, Date fi, Date ff) {
        return em.createQuery("SELECT d FROM DatosSensor d WHERE d.sensor in (:s) AND (d.fechaRecoleccion BETWEEN :fi AND :ff ) ORDER BY d.fechaRecoleccion ASC").setParameter("s", s).setParameter("fi", fi).setParameter("ff", ff).getResultList();
    }

}

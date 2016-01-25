/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.ucc.sipnat.logica;

import co.edu.ucc.sipnat.modelo.DatosSensor;
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

    public List<DatosSensor> obtenerDatos(Long id){
        return em.createQuery("SELECT d FROM DatosSensor d WHERE d.sensor.id =:s ORDER BY d.fechaCreacion DESC").setParameter("s", id).getResultList();
    }
}

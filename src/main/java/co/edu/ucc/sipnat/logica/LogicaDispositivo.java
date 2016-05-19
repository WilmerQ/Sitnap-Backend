/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.ucc.sipnat.logica;

import co.edu.ucc.sipnat.modelo.Dispositivo;
import co.edu.ucc.sipnat.modelo.Proyecto;
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
public class LogicaDispositivo {

    @PersistenceContext(unitName = "SIPNATPU")
    private EntityManager em;

    @EJB
    private CommonsBean cb;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    public Dispositivo getDispositivos(String token, Proyecto p) {
        try {
            return (Dispositivo) em.createQuery("SELECT d FROM Dispositivo d WHERE d.token = :t AND d.proyecto = :p").setParameter("p", p).setParameter("t", token).getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}

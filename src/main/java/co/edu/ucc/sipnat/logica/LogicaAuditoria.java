/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.ucc.sipnat.logica;

import co.edu.ucc.sipnat.modelo.Auditoria;
import java.util.Date;
import java.util.List;
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
public class LogicaAuditoria {

    @PersistenceContext(unitName = "SIPNATPU")
    private EntityManager em;

    public List<Auditoria> auditoriaPorFecha(Date fechai, Date fechaf) {
        System.out.println("Consultado auditoria");
        return em.createQuery("SELECT a FROM Auditoria a WHERE a.fechaCreacion BETWEEN :fi AND :ff").setParameter("fi", fechai).setParameter("ff", fechaf).getResultList();
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.ucc.sipnat.logica;



import co.edu.ucc.sipnat.modelo.Usuario;
import com.ibcaribe.procc.services.Md5;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Alvaro padilla
 */
@Stateless
@LocalBean
public class LogicaLogin {

    @PersistenceContext(unitName = "SIPNATPU")
    private EntityManager em;

    public Usuario login(String nombreUsuario, String passwordPlano) {
       try {
            String encPassword = Md5.getEncoddedString(passwordPlano);
            return (Usuario) em.createQuery("Select u from Usuario u where u.nombreUsuario = :n and u.clave = :p")
                    .setParameter("n", nombreUsuario)
                    .setParameter("p", encPassword).getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}

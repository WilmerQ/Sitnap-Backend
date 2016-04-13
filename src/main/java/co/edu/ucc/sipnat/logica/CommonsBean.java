/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.ucc.sipnat.logica;


import co.edu.ucc.sipnat.base.FieldtoQuery;
import co.edu.ucc.sipnat.modelo.CamposComunesdeEntidad;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author juvinao
 */
@Stateless
@LocalBean
public class CommonsBean {

    @PersistenceContext(unitName = "SIPNATPU")
    private EntityManager em;

    public boolean guardar(CamposComunesdeEntidad f) {
        try {
            if (f.getId() == null) {
                em.persist(f);
            } else {
                em.merge(f);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void remove(CamposComunesdeEntidad f) {
        f = (CamposComunesdeEntidad) getById(f.getClass(), f.getId());
        em.remove(f);
        //em.remove(em.contains(f) ? f : em.merge(f));
    }

    public List getAll(Class o) {
        String nombreClase = o.getSimpleName();
        return em.createQuery("Select o from " + nombreClase + " o ").getResultList();
    }

    public List getAll(Class o, String order) {
        String nombreClase = o.getSimpleName();
        return em.createQuery("Select o from " + nombreClase + " o " + order).getResultList();
    }

    public Object getById(Class o, Long id) {
        String nombreClase = o.getSimpleName();
        try {
            return (CamposComunesdeEntidad) em.createQuery("Select o from " + nombreClase + " o where o.id = :id").setParameter("id", id).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
        
    }

    public List getByOneField(Class o, String campo, Object value) {
        String nombreClase = o.getSimpleName();
        return em.createQuery("Select o from " + nombreClase + " o where o." + campo + " = :v").setParameter("v", value).getResultList();
    }

    public Object getByOneFieldWithOneResult(Class o, String campo, Object value) throws Exception {
        String nombreClase = o.getSimpleName();
        try {
            return em.createQuery("Select o from " + nombreClase + " o where o." + campo + " = :v").setParameter("v", value).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public List getByOneField(Class o, String campo, Object value, String order) {
        String nombreClase = o.getSimpleName();
        return em.createQuery("Select o from " + nombreClase + " o where o." + campo + " = :v " + order).setParameter("v", value).getResultList();
    }

    public List getByOneField(Class o, String campo, Object value, String order, int limit) {
        String nombreClase = o.getSimpleName();
        return em.createQuery("Select o from " + nombreClase + " o where o." + campo + " = :v " + order).setParameter("v", value).setMaxResults(limit).getResultList();
    }

    public List getByManyFields(Class o, List<FieldtoQuery> campos) {
        return getByManyFields(o, campos, "");
    }
    public List getByManyFields(Class o, List<FieldtoQuery> campos, String order) {
        return getByManyFields(o, campos, order, 0);
    }

    public List getByManyFields(Class o, List<FieldtoQuery> campos, String order, int limit) {
        String nombreClase = o.getSimpleName();
        String sql = "Select o from " + nombreClase + " o ";
        if (!campos.isEmpty()) {
            String where = "where ";
            for (int i = 0; i < campos.size(); i++) {
                FieldtoQuery f = campos.get(i);
                if (f.getUsarLike()) {
                    where = where + " o." + f.getNombreCampo() + " like :f" + i + " ";
                } else {
                    where = where + " o." + f.getNombreCampo() + " = :f" + i + " ";
                }
                if (i != campos.size() - 1) {
                    where = where + " and ";
                }
            }
            sql = sql + where + order;
            //System.out.println(sql);
            Query query = em.createQuery(sql);
            for (int i = 0; i < campos.size(); i++) {
                FieldtoQuery f = campos.get(i);
                if (f.getUsarLike()) {
                    query.setParameter("f" + i, "%" + f.getValorCampo() + "%");
                } else {
                    query.setParameter("f" + i, f.getValorCampo());
                }
            }
            if (limit>0){
                query.setMaxResults(limit);
            }
            return query.getResultList();
        } else {
            return null;
        }
    }

    public List getLikeOneField(Class o, String campo, Object value) {
        String nombreClase = o.getSimpleName();
        value = "%" + value.toString() + "%";
        return em.createQuery("Select o from " + nombreClase + " o where o." + campo + " like :v").setParameter("v", value).getResultList();
    }

    public List getLikeOneField(Class o, String campo, Object value, boolean forceUpperCase) {
        String nombreClase = o.getSimpleName();
        value = "%" + value.toString().toUpperCase() + "%";
        return em.createQuery("Select o from " + nombreClase + " o where upper(o." + campo + ") like :v").setParameter("v", value).getResultList();
    }

    public List getNotIn(Class o, String campo, List notIn) {
        String nombreClase = o.getSimpleName();
        return em.createQuery("Select o from " + nombreClase + " o where o." + campo + " not in (:notin)").setParameter("notin", notIn).getResultList();
    }
}

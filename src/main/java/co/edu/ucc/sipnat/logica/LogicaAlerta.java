/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.ucc.sipnat.logica;

import co.edu.ucc.sipnat.base.GsonExcludeListStrategy;
import co.edu.ucc.sipnat.modelo.Alerta;
import co.edu.ucc.sipnat.modelo.DatosSensor;
import co.edu.ucc.sipnat.modelo.Dispositivo;
import co.edu.ucc.sipnat.modelo.PromedioDatoSensor;
import co.edu.ucc.sipnat.modelo.Proyecto;
import co.edu.ucc.sipnat.modelo.ProyectoXSensor;
import co.edu.ucc.sipnat.modelo.Sensor;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Sender;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Alvaro Padilla
 *
 */
@Stateless
@LocalBean
public class LogicaAlerta {

    @EJB
    private CommonsBean cb;

    @PersistenceContext(unitName = "SIPNATPU")
    private EntityManager em;

    //Key de la app movil.
    public static final String GCM_API_KEY = "AIzaSyANLRpamMIbOZ_nOmU8JnroWtBH8_bdtBw";
    //mensaje a mandar.
    public static final String MESSAGE_VALUE = "Esto es una alerta!";
    public static final String MESSAGE_KEY = "message";

    //Simulacion de prueba para mandar una notificacion a la app movil.
    public void accionMandarAlarta(Sensor s, DatosSensor dato) {
        System.out.println("En el metodo");
        Boolean mandarAlerta = Boolean.FALSE;
        int nivel = 0;
        PromedioDatoSensor pds = getPromedioDatoSensor(s);
        if (pds == null) {
            Long cantidad = obtenerCantidad(s);
            System.out.println("Cantidad " + cantidad);
            if (cantidad >= 100) {
                List<DatosSensor> dses = obtenerDatosDeSensores(s);
                BigDecimal promedio = BigDecimal.ZERO;
                System.out.println("Tamaño de la lista " + dses.size());
                BigDecimal tamañoLista = new BigDecimal(dses.size());
                for (DatosSensor ds : dses) {
                    promedio = promedio.add(new BigDecimal(ds.getDato()));
                    ds.setRevisado(Boolean.TRUE);
                    cb.guardar(ds);
                }
                System.out.println("Suma sin dividir " + promedio);
                promedio = promedio.divide(tamañoLista, 2, RoundingMode.HALF_UP);
                PromedioDatoSensor datoSensor = new PromedioDatoSensor();
                datoSensor.setPromedio(promedio);
                datoSensor.setSensor(s);
                cb.guardar(datoSensor);
            }
        } else {
            BigDecimal resultado = BigDecimal.ZERO;
            resultado = new BigDecimal(dato.getDato()).divide(pds.getPromedio(), 2, RoundingMode.HALF_UP);
            resultado = resultado.multiply(new BigDecimal(100));
            System.out.println("Resultado es " + resultado + "%");
            dato.setRevisado(Boolean.TRUE);
            cb.guardar(dato);
            Double res = new Double(resultado.toString());
            if (res < 70 && res > 50) {
                System.out.println("Nivel 1");
                nivel = 1;
                mandarAlerta = Boolean.TRUE;
            } else if (res < 50 && res > 30) {
                System.out.println("Nivel 2");
                nivel = 2;
                mandarAlerta = Boolean.TRUE;
            } else if (res < 30) {
                System.out.println("Nivel 3");
                nivel = 3;
                mandarAlerta = Boolean.TRUE;
            } else if (res > 130 && res < 150) {
                System.out.println("Nivel 1");
                nivel = 1;
                mandarAlerta = Boolean.TRUE;
            } else if (res > 150 && res < 170) {
                System.out.println("Nivel 2");
                nivel = 2;
                mandarAlerta = Boolean.TRUE;
            } else if (res > 171) {
                System.out.println("Nivel 3");
                nivel = 3;
                mandarAlerta = Boolean.TRUE;
            }
        }
        if (mandarAlerta) {
            List<DatosSensor> dses = obtenerDatosDeSensores(s);
            BigDecimal promedio = BigDecimal.ZERO;
            System.out.println("Tamaño de la lista " + dses.size());
            BigDecimal tamañoLista = new BigDecimal(dses.size());
            for (DatosSensor ds : dses) {
                promedio = promedio.add(new BigDecimal(ds.getDato()));
                ds.setRevisado(Boolean.TRUE);
                cb.guardar(ds);
            }
            System.out.println("Suma sin dividir " + promedio);
            promedio = promedio.divide(tamañoLista, 2, RoundingMode.HALF_UP);
            PromedioDatoSensor datoSensor = new PromedioDatoSensor();
            datoSensor.setPromedio(promedio);
            datoSensor.setSensor(s);
            cb.guardar(datoSensor);
            try {
                List<ProyectoXSensor> pxses = cb.getByOneField(ProyectoXSensor.class, "sensor", s);
                List<Proyecto> list = new ArrayList<>();
                Alerta a = new Alerta();
                String des = "Los siguiente proyecto dispararon alertas \n";
                for (ProyectoXSensor pxs : pxses) {
                    Alerta alerta = new Alerta();
                    alerta.setProyecto(pxs.getProyecto());
                    alerta.setFechaDelDisparo(new Date());
                    alerta.setHoraDelDisparo(new Date());
                    alerta.setNivel("Alerta de nivel " + nivel);
                    alerta.setDato(dato.getDato());
                    list.add(pxs.getProyecto());
                    if (nivel == 1) {
                        des = des + pxs.getProyecto().getNombre() + " " + pxs.getProyecto().getAlertaNivel1() + "\n";
                        alerta.setCodigoColor("#fff176");
                    } else if (nivel == 2) {
                        des = des + pxs.getProyecto().getNombre() + " " + pxs.getProyecto().getAlertaNivel2() + "\n";
                        alerta.setCodigoColor("#ffa726");
                    } else if (nivel == 3) {
                        des = des + pxs.getProyecto().getNombre() + " " + pxs.getProyecto().getAlertaNivel3() + "\n";
                        alerta.setCodigoColor("#ef5350");
                    }
                    alerta.setDescripcion(des);
                    if (cb.guardar(alerta)) {
                        a = alerta;
                    }
                }

                Gson g = new GsonBuilder().setExclusionStrategies(new GsonExcludeListStrategy()).setPrettyPrinting().create();
                String mensaje = g.toJson(a);
                System.out.println("Json " + mensaje);
                List<String> ds1 = getDispositivos(list);
                if (!ds1.isEmpty()) {
                    ArrayList<String> devicesList = new ArrayList<>();
                    for (String s1 : ds1) {
                        System.out.println(s1);
                        devicesList.add(s1);
                    }
                    Sender sender = new Sender(GCM_API_KEY);
                    Message message = new Message.Builder().timeToLive(30).delayWhileIdle(true).addData(MESSAGE_KEY, mensaje).build();
                    MulticastResult result = sender.send(message, devicesList, 1);
                    sender.send(message, devicesList, 1);
                    System.out.println("Resultado: " + result.toString());
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Long obtenerCantidad(Sensor s) {
        try {
            return (Long) em.createQuery("SELECT COUNT(ds.sensor) FROM DatosSensor ds WHERE ds.sensor = :s AND ds.revisado = false").setParameter("s", s).getSingleResult();
        } catch (Exception e) {
            return 0L;
        }
    }

    private PromedioDatoSensor getPromedioDatoSensor(Sensor s) {
        try {
            Long id = (Long) em.createQuery("SELECT MAX(o.id)FROM PromedioDatoSensor o WHERE o.sensor = :s").setParameter("s", s).getSingleResult();
            return (PromedioDatoSensor) cb.getById(PromedioDatoSensor.class, id);
        } catch (Exception e) {
            return null;
        }
    }

    private List<String> getDispositivos(List<Proyecto> proyecto) {
        return em.createQuery("SELECT DISTINCT d.token FROM Dispositivo d WHERE d.proyecto IN (:p)").setParameter("p", proyecto).getResultList();
    }

    private List<DatosSensor> obtenerDatosDeSensores(Sensor s) {
        return em.createQuery("SELECT d FROM DatosSensor d WHERE d.sensor =:s").setParameter("s", s).getResultList();
    }

    public List<Alerta> alertas(Date fi, Date ff) {
        return em.createQuery("SELECT a FROM Alerta a WHERE a.fechaDelDisparo BETWEEN :fi AND :ff").setParameter("fi", fi).setParameter("ff", ff).getResultList();
    }
}

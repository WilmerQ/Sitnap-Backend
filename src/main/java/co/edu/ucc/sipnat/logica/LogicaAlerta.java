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
        if (s.getPromedio() == null) {
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
                s.setPromedio(promedio);
                cb.guardar(s);
            }
        } else {
            BigDecimal resultado = BigDecimal.ZERO;
            resultado = new BigDecimal(dato.getDato()).divide(s.getPromedio(), 2, RoundingMode.HALF_UP);
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
            try {
                List<ProyectoXSensor> pxses = cb.getByOneField(ProyectoXSensor.class, "sensor", s);
                for (ProyectoXSensor pxse : pxses) {
                    Alerta alerta = new Alerta();
                    alerta.setProyecto(pxse.getProyecto());
                    alerta.setFechaDelDisparo(new Date());
                    alerta.setHoraDelDisparo(new Date());
                    alerta.setNivel("Alerta de nivel " + nivel);
                    alerta.setDato(dato.getDato());

                    if (nivel == 1) {
                        alerta.setDescripcion(pxse.getProyecto().getAlertaNivel1() + " Sensor de tipo " + s.getTipoSensor().getNombre() + " Dato capturado: " + dato.getDato());
                        alerta.setCodigoColor("#fff176");
                    } else if (nivel == 2) {
                        alerta.setDescripcion(pxse.getProyecto().getAlertaNivel2() + " Sensor de tipo " + s.getTipoSensor().getNombre() + " Dato capturado: " + dato.getDato());
                        alerta.setCodigoColor("#ffa726");
                    } else if (nivel == 3) {
                        alerta.setDescripcion(pxse.getProyecto().getAlertaNivel3() + " Sensor de tipo " + s.getTipoSensor().getNombre() + " Dato capturado: " + dato.getDato());
                        alerta.setCodigoColor("#ef5350");
                    }

                    if (cb.guardar(alerta)) {
                        Gson g = new GsonBuilder().setExclusionStrategies(new GsonExcludeListStrategy()).setPrettyPrinting().create();
                        String mensaje = g.toJson(alerta);
                        System.out.println("Json " + mensaje);
                        List<Dispositivo> ds = cb.getByOneField(Dispositivo.class, "proyecto", pxse.getProyecto());
                        ArrayList<String> devicesList = new ArrayList<>();
                        for (Dispositivo d : ds) {
                            devicesList.add(d.getToken());
                        }
                        Sender sender = new Sender(GCM_API_KEY);
                        Message message = new Message.Builder().timeToLive(30).delayWhileIdle(true).addData(MESSAGE_KEY, mensaje).build();
                        MulticastResult result = sender.send(message, devicesList, 1);
                        sender.send(message, devicesList, 1);
                        System.out.println("Resultado: " + result.toString());
                    }
                    s.setPromedio(null);
                    cb.guardar(s);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Long obtenerCantidad(Sensor s) {
        try {
            return (Long) em.createQuery("SELECT COUNT(ds.sensor) FROM DatosSensor ds WHERE ds.sensor = :s").setParameter("s", s).getSingleResult();
        } catch (Exception e) {
            return 0L;
        }

    }

    private List<DatosSensor> obtenerDatosDeSensores(Sensor s) {
        return em.createQuery("SELECT d FROM DatosSensor d WHERE d.sensor =:s").setParameter("s", s).getResultList();
    }

    public List<Alerta> alertas(Date fi, Date ff) {
        return em.createQuery("SELECT a FROM Alerta a WHERE a.fechaDelDisparo BETWEEN :fi AND :ff").setParameter("fi", fi).setParameter("ff", ff).getResultList();
    }
}

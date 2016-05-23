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
    //id de los telefono registrado.
    public static final String REG_ID = "APA91bHLom3jahCWOw7N8ow9lJ3Jm9E9EskMkomSvK319HLga2Lxcy7RzCVREaPy2It-xFt0TC-QINH4Xl4vF54gg5xpEp57RncIo9okFQMxhvl0LBhXm8elS4FAteUkCE15AlQ_dMOby5q4oejDszhENgwSvH4Sjg";
    public static final String REG_ID2 = "APA91bEiQrfLNBdqvs04qvCJX8-mtCAK_jWkOlnzcP6WHZckc7e-7VKkasuY91GDZ453puiPSqbLHW-w2kET4eYqX3c1I11TuXM6Ts7-t-ZCFLkUvSIzeZ89LStOIgA_QdkmdLmNFCkEVb8iqG0KejFJZ2X2SaPDDQ";

    //Simulacion de prueba para mandar una notificacion a la app movil.
    public void accionMandarAlarta(Sensor s, DatosSensor dato) {
        Boolean mandarAlerta = Boolean.FALSE;
        int nivel = 0;
        if (s.getPromedio() == null) {
            Long cantidad = obtenerCantidadSinRevisar();
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
                    if (nivel == 1) {
                        alerta.setDescripcion("Alerta de nivel " + nivel + ": " + pxse.getProyecto().getAlertaNivel1() + " Sensor de tipo " + s.getTipoSensor().getNombre() + " Registro datos por debajo del promedio.");
                    } else if (nivel == 2) {
                        alerta.setDescripcion("Alerta de nivel " + nivel + ": " + pxse.getProyecto().getAlertaNivel2() + " Sensor de tipo " + s.getTipoSensor().getNombre() + " Registro datos por debajo del promedio.");
                    } else if (nivel == 3) {
                        alerta.setDescripcion("Alerta de nivel " + nivel + ": " + pxse.getProyecto().getAlertaNivel3() + " Sensor de tipo " + s.getTipoSensor().getNombre() + " Registro datos por debajo del promedio.");
                    }
                    if (cb.guardar(alerta)) {
                        Gson g = new GsonBuilder().setExclusionStrategies(new GsonExcludeListStrategy()).setPrettyPrinting().create();
                        String mensaje = g.toJson(alerta);
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
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Long obtenerCantidadSinRevisar() {
        return (Long) em.createQuery("SELECT COUNT(ds.revisado) FROM DatosSensor ds WHERE ds.revisado = false").getSingleResult();
    }

    private List<DatosSensor> obtenerDatosDeSensores(Sensor s) {
        return em.createQuery("SELECT d FROM DatosSensor d WHERE d.sensor =:s and d.revisado = false").setParameter("s", s).getResultList();
    }
}

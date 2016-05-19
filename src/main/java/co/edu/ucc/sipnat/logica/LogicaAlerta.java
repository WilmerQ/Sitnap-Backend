/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.ucc.sipnat.logica;

import co.edu.ucc.sipnat.modelo.Dispositivo;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Sender;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

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
    
    //Key de la app movil.
    public static final String GCM_API_KEY = "AIzaSyANLRpamMIbOZ_nOmU8JnroWtBH8_bdtBw";
    //mensaje a mandar.
    public static final String MESSAGE_VALUE = "Esto es una alerta!";
    public static final String MESSAGE_KEY = "message";
    //id de los telefono registrado.
    public static final String REG_ID = "APA91bHLom3jahCWOw7N8ow9lJ3Jm9E9EskMkomSvK319HLga2Lxcy7RzCVREaPy2It-xFt0TC-QINH4Xl4vF54gg5xpEp57RncIo9okFQMxhvl0LBhXm8elS4FAteUkCE15AlQ_dMOby5q4oejDszhENgwSvH4Sjg";
    public static final String REG_ID2 = "APA91bEiQrfLNBdqvs04qvCJX8-mtCAK_jWkOlnzcP6WHZckc7e-7VKkasuY91GDZ453puiPSqbLHW-w2kET4eYqX3c1I11TuXM6Ts7-t-ZCFLkUvSIzeZ89LStOIgA_QdkmdLmNFCkEVb8iqG0KejFJZ2X2SaPDDQ";

    
    //Simulacion de prueba para mandar una notificacion a la app movil.
    
    public void accionMandarAlarta() {
        Boolean mandarAlerta = Boolean.FALSE;
        for (int i = 0; i < 10; i++) {
            int d = (int) (Math.random() * 10 + 1);
            if (d > 8) {
                mandarAlerta = Boolean.TRUE;
                break;
            }
        }
        if (mandarAlerta) {
            try {
                List<Dispositivo> ds = cb.getAll(Dispositivo.class);
                ArrayList<String> devicesList = new ArrayList<>();
                for (Dispositivo d : ds) {
                    devicesList.add(d.getToken());
                }
                
                    Sender sender = new Sender(GCM_API_KEY);
                    Message message = new Message.Builder().timeToLive(30).delayWhileIdle(true).addData(MESSAGE_KEY, MESSAGE_VALUE).build();
                    MulticastResult result = sender.send(message, devicesList, 1);
                    sender.send(message, devicesList, 1);
                    System.out.println("Resultado: " + result.toString());
                
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

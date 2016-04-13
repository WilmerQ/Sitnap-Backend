/**
 * 
 */
package co.edu.ucc.sipnat.base;

import javax.faces.context.FacesContext;

/**
 * @author Alvaro Padilla
 *
 */
public class SessionOperations {

    public static void setSessionValue(String key, Object object) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(key, object);
    }

    public static Object getSessionValue(String key) {
        return FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(key);
    }
}

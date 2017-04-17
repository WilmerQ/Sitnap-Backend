/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.ucc.sipnat.clases;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Windows 8.1
 */
public class DatosBasicos {
    public static String ip = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getServerName();    
    public static String path = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getContextPath();    
    public static Integer port = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getLocalPort();    
    //public static String ip = "localhost";  
    //public static String ip = "54.165.89.80";  
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.ucc.sipnat.servlet;

import co.edu.ucc.sipnat.logica.CommonsBean;
import co.edu.ucc.sipnat.modelo.TipoSensor;
import java.io.IOException;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Windows 8.1
 */
@WebServlet(urlPatterns = {"/imagenServlet"})
public class ImagenServlet extends HttpServlet {

    @EJB
    private CommonsBean cb;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idDeImagen = request.getParameter("id");
        System.out.println("+++++++++"+idDeImagen);
        try {
            Long l = new Long(idDeImagen);
            TipoSensor ts = (TipoSensor) cb.getById(TipoSensor.class, l);
            if (ts == null) {
                byte[] imagen = (byte[]) request.getSession().getAttribute(idDeImagen);
                response.setContentType("image");
                response.setContentLength(imagen.length);
                response.getOutputStream().write(imagen);
            } else {
                byte[] imagen = ts.getLogoDelTipo();
                response.setContentType("image");
                response.setContentLength(imagen.length);
                response.getOutputStream().write(imagen);
            }
        } catch (Exception e) {
            byte[] imagen = (byte[]) request.getSession().getAttribute(idDeImagen);
            response.setContentType("image");
            response.setContentLength(imagen.length);
            response.getOutputStream().write(imagen);
        }

    }
}

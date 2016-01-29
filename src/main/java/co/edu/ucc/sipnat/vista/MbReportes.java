/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.ucc.sipnat.vista;

import co.edu.ucc.sipnat.logica.CommonsBean;
import co.edu.ucc.sipnat.logica.LogicaAuditoria;
import co.edu.ucc.sipnat.logica.LogicaSensor;
import co.edu.ucc.sipnat.modelo.Auditoria;
import co.edu.ucc.sipnat.modelo.DatosSensor;
import co.edu.ucc.sipnat.modelo.Sensor;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Windows 8.1
 */
@ViewScoped
@ManagedBean(name = "MbReportes")
public class MbReportes implements Serializable {

    private Long idSensor;
    private Long idProyecto;
    private Date fechainicio;
    private Date fechafinal;
    private Boolean proyecto;
    private Boolean sensor;
    private Boolean auditoria;

    @EJB
    private CommonsBean cb;

    @EJB
    private LogicaAuditoria la;

    @EJB
    private LogicaSensor ls;

    @Context
    private ServletContext servletContext;

    /**
     * Creates a new instance of MbReportes
     */
    public MbReportes() {
    }

    @PostConstruct
    public void init() {
        idProyecto = null;
        idSensor = null;
        fechafinal = new Date();
        fechainicio = new Date();
        proyecto = Boolean.FALSE;
        sensor = Boolean.FALSE;
        auditoria = Boolean.FALSE;
    }

    public void cargaFormulario(String opcion) {
        switch (opcion) {
            case "proyecto":
                proyecto = Boolean.TRUE;
                sensor = Boolean.FALSE;
                auditoria = Boolean.FALSE;
                break;
            case "sensor":
                proyecto = Boolean.FALSE;
                sensor = Boolean.TRUE;
                auditoria = Boolean.FALSE;
                break;
            case "auditoria":
                proyecto = Boolean.FALSE;
                sensor = Boolean.FALSE;
                auditoria = Boolean.TRUE;
                break;
        }
    }

    public Boolean verificarFormulario() {
        Boolean res = Boolean.TRUE;
        if (proyecto) {
            if (fechafinal == null) {
                mostrarMensaje(FacesMessage.SEVERITY_ERROR, "ERROR", "Agregue fecha final");
                res = Boolean.FALSE;
            }

            if (fechafinal == null) {
                mostrarMensaje(FacesMessage.SEVERITY_ERROR, "ERROR", "Agregue fecha inicio");
                res = Boolean.FALSE;

            }

            if (idProyecto == null) {
                mostrarMensaje(FacesMessage.SEVERITY_ERROR, "ERROR", "Agregue codigo del proyecto");
                res = Boolean.FALSE;
            }
        }

        if (sensor) {
            if (fechafinal == null) {
                mostrarMensaje(FacesMessage.SEVERITY_ERROR, "ERROR", "Agregue fecha final");
                res = Boolean.FALSE;
            }

            if (fechafinal == null) {
                mostrarMensaje(FacesMessage.SEVERITY_ERROR, "ERROR", "Agregue fecha inicio");
                res = Boolean.FALSE;

            }

            if (idSensor == null) {
                mostrarMensaje(FacesMessage.SEVERITY_ERROR, "ERROR", "Agregue codigo del sensor");
                res = Boolean.FALSE;
            }
        }

        if (auditoria) {
            if (fechafinal == null) {
                mostrarMensaje(FacesMessage.SEVERITY_ERROR, "ERROR", "Agregue fecha final");
                res = Boolean.FALSE;
            }

            if (fechafinal == null) {
                mostrarMensaje(FacesMessage.SEVERITY_ERROR, "ERROR", "Agregue fecha inicio");
                res = Boolean.FALSE;

            }
        }
        return res;
    }

    public void cargaAuditoria() throws FileNotFoundException, IOException {
        if (verificarFormulario()) {
            List<Auditoria> auditorias = la.auditoriaPorFecha(fechainicio, fechafinal);
            if (!auditorias.isEmpty()) {
                Workbook workbook = new XSSFWorkbook();
                Sheet auditoriaSheet = workbook.createSheet("Auditoria");
                int rowIndex = 0;
                Row rowinit = auditoriaSheet.createRow(rowIndex);
                int cellinit = 0;
                rowinit.createCell(cellinit++).setCellValue("Codigo");
                rowinit.createCell(cellinit++).setCellValue("Fecha");
                rowinit.createCell(cellinit++).setCellValue("Metodo");
                rowinit.createCell(cellinit++).setCellValue("Causa del error");
                for (Auditoria a : auditorias) {
                    Row row2 = auditoriaSheet.createRow(++rowIndex);
                    int cellIndex = 0;
                    row2.createCell(cellIndex++).setCellValue(a.getId());
                    row2.createCell(cellIndex++).setCellValue(a.getFechaCreacion().toString());
                    row2.createCell(cellIndex++).setCellValue(a.getMetodo());
                    row2.createCell(cellIndex++).setCellValue(a.getCausaDelError());
                }
                servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
                String basePath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/usuario/archivo/");
                String nameFile = "auditoria.xlsx";
                String pathFile = basePath + File.separatorChar + nameFile;
                System.out.println(pathFile);
                FileOutputStream archivo = new FileOutputStream(pathFile);
                workbook.write(archivo);
                archivo.close();
                String contextPath = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getContextPath();
                redirect(contextPath + "/usuario/archivo/" + nameFile);
            } else {
                mostrarMensaje(FacesMessage.SEVERITY_INFO, "Alvertencia", "No hay datos");
            }
        }
    }

    public void cargaDatoSensor() throws FileNotFoundException, IOException {
        if (verificarFormulario()) {
            Sensor s = (Sensor) cb.getById(Sensor.class, idSensor);
            if (s != null) {
                List<DatosSensor> dses = ls.obtenerDatos(s, fechainicio, fechafinal);
                if (!dses.isEmpty()) {
                    Workbook workbook = new XSSFWorkbook();
                    Sheet auditoriaSheet = workbook.createSheet("datoSensor" + idSensor);
                    int rowIndex = 0;
                    Row rowinit = auditoriaSheet.createRow(rowIndex);
                    int cellinit = 0;
                    rowinit.createCell(cellinit++).setCellValue("Codigo Sensor");
                    rowinit.createCell(cellinit++).setCellValue("Tipo de sensor");
                    rowinit.createCell(cellinit++).setCellValue("Fecha recolecion");
                    rowinit.createCell(cellinit++).setCellValue("Fecha sincronizacion");
                    rowinit.createCell(cellinit++).setCellValue("Datos");
                    for (DatosSensor ds : dses) {
                        Row row2 = auditoriaSheet.createRow(++rowIndex);
                        int cellIndex = 0;
                        row2.createCell(cellIndex++).setCellValue(ds.getSensor().getId());
                        row2.createCell(cellIndex++).setCellValue(ds.getSensor().getTipoSensor().getNombre());
                        row2.createCell(cellIndex++).setCellValue(ds.getFechaRecoleccion().toString());
                        row2.createCell(cellIndex++).setCellValue(ds.getFechaSincronizacion().toString());
                        row2.createCell(cellIndex++).setCellValue(Double.parseDouble(ds.getDato()));
                    }
                    servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
                    String basePath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/usuario/archivo/");
                    String nameFile = "datoSensor" + idSensor + ".xlsx";
                    String pathFile = basePath + File.separatorChar + nameFile;
                    System.out.println(pathFile);
                    FileOutputStream archivo = new FileOutputStream(pathFile);
                    workbook.write(archivo);
                    archivo.close();
                    String contextPath = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getContextPath();
                    redirect(contextPath + "/usuario/archivo/" + nameFile);
                } else {
                    mostrarMensaje(FacesMessage.SEVERITY_INFO, "Alvertencia", "No hay datos");
                }
            } else {
                mostrarMensaje(FacesMessage.SEVERITY_ERROR, "ERROR", "Codigo de sensor no existe");
            }
        }
    }

    public void mostrarMensaje(FacesMessage.Severity icono, String titulo, String mensaje) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(icono, titulo, mensaje));
    }

    private void redirect(String url) {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            context.getExternalContext().redirect(url);
        } catch (IOException ex) {
            Logger.getLogger(MbReportes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Long getIdSensor() {
        return idSensor;
    }

    public void setIdSensor(Long idSensor) {
        this.idSensor = idSensor;
    }

    public Long getIdProyecto() {
        return idProyecto;
    }

    public void setIdProyecto(Long idProyecto) {
        this.idProyecto = idProyecto;
    }

    public Date getFechainicio() {
        return fechainicio;
    }

    public void setFechainicio(Date fechainicio) {
        this.fechainicio = fechainicio;
    }

    public Date getFechafinal() {
        return fechafinal;
    }

    public void setFechafinal(Date fechafinal) {
        this.fechafinal = fechafinal;
    }

    public Boolean getProyecto() {
        return proyecto;
    }

    public void setProyecto(Boolean proyecto) {
        this.proyecto = proyecto;
    }

    public Boolean getSensor() {
        return sensor;
    }

    public void setSensor(Boolean sensor) {
        this.sensor = sensor;
    }

    public Boolean getAuditoria() {
        return auditoria;
    }

    public void setAuditoria(Boolean auditoria) {
        this.auditoria = auditoria;
    }
}

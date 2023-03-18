/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cedi.util.utilitarios;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.jdbc.Work;


import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

/**
 *
 * @author Colaborador
 */
public class ChamarRelatorio implements Work {

   
	private String caminhoRelatorio;
	private String nomeRelatorio;
	private HashMap param;
	
	public ChamarRelatorio(String caminho, HashMap parametros, String nome){
		this.caminhoRelatorio = caminho;
		this.nomeRelatorio = nome;
		this.param = parametros;
	}
	
	@Override
	public void execute(Connection connection) throws SQLException {
		try {
			HashMap parametros = param;

	            FacesContext facesContext = FacesContext.getCurrentInstance();

	            facesContext.responseComplete();

	            ServletContext scontext = (ServletContext) facesContext.getExternalContext().getContext();

	            JasperPrint jasperPrint = JasperFillManager.fillReport(scontext.getRealPath("/WEB-INF/relatorios/" + caminhoRelatorio), parametros, connection);

//	        JasperPrint jasperPrint = JasperFillManager.fillReport("C:\\Users\\Colaborador\\Documents\\workspaceJasperStudio\\MyReports\\Blank_A4_1.jasper", parametros, con);
	            ByteArrayOutputStream baos = new ByteArrayOutputStream();

	            JasperExportManager.exportReportToPdfStream(jasperPrint, baos);

	            byte[] bytes = baos.toByteArray();

	            if (bytes != null && bytes.length > 0) {

	                HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();

	                response.setContentType("application/pdf");

//	              response.setHeader("Content-disposition", "inline; filename=\"relatorioPorData.pdf\"");
	                response.setHeader("Content-disposition", "attachment; filename=\"" + nomeRelatorio + ".pdf\"");
//	                System.out.println("Aquiiiiii");
	                response.setContentLength(bytes.length);

	                ServletOutputStream outputStream = response.getOutputStream();

	                outputStream.write(bytes, 0, bytes.length);

	                outputStream.flush();

	                outputStream.close();

	                baos.close();
	            }
			
		} catch (Exception e) {
			throw new SQLException("Erro ao executar relatório " + this.caminhoRelatorio, e);
		}
	}
	
}

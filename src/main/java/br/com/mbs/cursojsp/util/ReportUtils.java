package br.com.mbs.cursojsp.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletContext;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRXlsExporter;

public class ReportUtils implements Serializable{
	private static final long serialVersionUID = 1L;
	
	public byte[] geraRelatorioPDF(List<?> listaDados, String nomeRelatorio, HashMap<String, Object> params ,ServletContext servletContext) throws JRException {
		
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(listaDados);
		
		String caminhoJasper = servletContext.getRealPath("relatorios") + File.separator + nomeRelatorio + ".jasper";
		JasperPrint print = JasperFillManager.fillReport(caminhoJasper, params, dataSource);
		
		return JasperExportManager.exportReportToPdf(print);
	}
	
	public byte[] geraRelatorioExcel(List<?> listaDados, String nomeRelatorio, HashMap<String, Object> params ,ServletContext servletContext) throws JRException {
		
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(listaDados);
		
		String caminhoJasper = servletContext.getRealPath("relatorios") + File.separator + nomeRelatorio + ".jasper";
		JasperPrint print = JasperFillManager.fillReport(caminhoJasper, params, dataSource);
		
		JRExporter exporter = new JRXlsExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, bytes);
		exporter.exportReport();
		
		return bytes.toByteArray();
	}

}

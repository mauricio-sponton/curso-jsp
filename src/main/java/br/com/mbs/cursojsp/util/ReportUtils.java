package br.com.mbs.cursojsp.util;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import jakarta.servlet.ServletContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class ReportUtils implements Serializable{
	private static final long serialVersionUID = 1L;
	
	public byte[] geraRelatorioPDF(List<?> listaDados, String nomeRelatorio, ServletContext servletContext) throws JRException {
		
		var dataSource = new JRBeanCollectionDataSource(listaDados);
		
		String caminhoJasper = servletContext.getRealPath("relatorios") + File.separator + nomeRelatorio + ".jasper";
		JasperPrint print = JasperFillManager.fillReport(caminhoJasper, new HashMap<>(), dataSource);
		
		return JasperExportManager.exportReportToPdf(print);
	}

}

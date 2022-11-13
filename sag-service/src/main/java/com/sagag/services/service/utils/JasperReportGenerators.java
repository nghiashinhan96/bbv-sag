package com.sagag.services.service.utils;

import com.google.common.base.MoreObjects;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;

/**
 * Class for implementation of jasper report.
 */
@UtilityClass
@Slf4j
public final class JasperReportGenerators {

  private static final String REPORT_DIRECTORY = "/templates/reports/";

  /**
   * Generates the JasperReports by templates.
   *
   * @param templates the list of templates
   * @return the array of {@link JasperReport}
   */
  public static JasperReport[] generateJasperReports(String[] templates) throws JRException {
    if (ArrayUtils.isEmpty(templates)) {
      log.error("Not found the list of templates");
      throw new IllegalArgumentException("The given template list must not be empty");
    }
    final List<JasperReport> jasperReports = new ArrayList<>();
    InputStream inputStream = null;
    for (String template : templates) {
      inputStream = JasperReportGenerators.class.getResourceAsStream(REPORT_DIRECTORY + template);
      if (inputStream == null) {
        continue;
      }
      jasperReports.add(JasperCompileManager.compileReport(inputStream));
    }
    IOUtils.closeQuietly(inputStream);
    return jasperReports.stream().toArray(JasperReport[]::new);
  }
  
  public static byte[] generateWord(JasperReport jasperReport,
      Map<String, Object> parameters, final JRDataSource dataSource) throws JRException {
      // Export to RTF stream
      final JRDocxExporter jrDocxExporter = new JRDocxExporter();
      
      final JasperPrint jasperPrint = getJasperPrint(jasperReport, parameters, dataSource);
      jrDocxExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
      
      final ByteArrayOutputStream baos = new ByteArrayOutputStream();
      jrDocxExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(baos));
      jrDocxExporter.exportReport();
      
      return baos.toByteArray();
  }

  public static byte[] generatePdf(JasperReport jasperReport,
      Map<String, Object> parameters, final JRDataSource dataSource) throws JRException {
    // Export to PDF stream
    final ByteArrayOutputStream baos = new ByteArrayOutputStream();
    final JasperPrint jasperPrint = getJasperPrint(jasperReport, parameters, dataSource);
    JasperExportManager.exportReportToPdfStream(jasperPrint, baos);
    return baos.toByteArray();
  }

  public static byte[] generateRtf(JasperReport jasperReport,
      Map<String, Object> parameters, final JRDataSource dataSource) throws JRException {
    // Export to RTF stream
    final JRRtfExporter rtfExporter = new JRRtfExporter();

    final JasperPrint jasperPrint = getJasperPrint(jasperReport, parameters, dataSource);
    rtfExporter.setExporterInput(new SimpleExporterInput(jasperPrint));

    final ByteArrayOutputStream baos = new ByteArrayOutputStream();
    rtfExporter.setExporterOutput(new SimpleWriterExporterOutput(baos));
    rtfExporter.exportReport();

    return baos.toByteArray();
  }

  private static JasperPrint getJasperPrint(final JasperReport jasperReport,
      final Map<String, Object> parameters, final JRDataSource dataSource) throws JRException {
    if (Objects.isNull(jasperReport)) {
      throw new IllegalArgumentException("The given input stream must not be null");
    }
    if (MapUtils.isEmpty(parameters)) {
      throw new IllegalArgumentException("The given parameters must not be empty");
    }
    return JasperFillManager.fillReport(jasperReport, parameters,
        MoreObjects.firstNonNull(dataSource, new JREmptyDataSource()));
  }

}

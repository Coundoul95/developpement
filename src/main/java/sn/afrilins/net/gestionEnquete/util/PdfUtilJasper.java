package sn.afrilins.net.gestionEnquete.util;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class PdfUtilJasper {

    @Autowired
    private DataSource dataSource;

    public byte[] createReport(String reportName, Map paramimprime) throws SQLException {
        try {
            InputStream reportStream = getClass().getResourceAsStream("/report/" + reportName + ".jasper");
            Map parameters = new HashMap();
            parameters.putAll(paramimprime);

            parameters.put("IMAGE_DIR", "/static/img/logo.svg");

            parameters.put("imprime", reportName);
            Connection connection = this.dataSource.getConnection();
            log.info("report ====================== {}", reportStream);
            byte[] reportByte = JasperRunManager.runReportToPdf(reportStream, parameters, connection);
            reportStream.close();
            connection.close();
            return reportByte;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (JRException e) {
            log.info(" ============= {}", e);
            throw new RuntimeException(e);
        }

    }

}

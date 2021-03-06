package ru.mmb.datacollector.transport.exporter.data;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Date;

import ru.mmb.datacollector.model.registry.Settings;
import ru.mmb.datacollector.transport.exporter.DataExtractorToJson;
import ru.mmb.datacollector.transport.exporter.ExportFormat;
import ru.mmb.datacollector.transport.exporter.ExportState;
import ru.mmb.datacollector.util.DateFormat;

public class DataExporter {
    private final Date exportDate;
    private final ExportState exportState;
    private final ExportFormat exportFormat;

    private BufferedWriter writer;

    public DataExporter(ExportState exportState, ExportFormat exportFormat) {
        this.exportDate = new Date();
        this.exportState = exportState;
        this.exportFormat = exportFormat;
    }

    public String exportData() throws Exception {
        String fileName = generateFileName(exportDate);
        writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), "UTF8"));
        try {
            createExportDataMethod().exportData();
        } finally {
            writer.close();
        }
        return fileName;
    }

    private ExportDataMethod createExportDataMethod() throws Exception {
        if (exportFormat == ExportFormat.JSON) {
            return new ExportDataMethodJson(exportState, new DataExtractorToJson(), writer);
        } else {
            return new ExportDataMethodTxtToSite(exportState, writer);
        }
    }

    private String generateFileName(Date exportDate) {
        String result = Settings.getInstance().getExportDir() + "/exp_";
        result +=
                Settings.getInstance().getUserId() + "_" + Settings.getInstance().getDeviceId() + "_"
                        + DateFormat.format(exportDate) + "." + exportFormat.getFileExtension();
        return result;
    }
}

package utils;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@Log4j2
public class ZipUtils {

    public static void extractGoldDataAndOutputData(File zipFile, String goldDataFolder, String outputDataFolder,
                                                    String goldDataRegex, String outputDataRegex) {

        try (ZipFile file = new ZipFile(zipFile)) {
            Enumeration<? extends ZipEntry> zipEntries = file.entries();

            while (zipEntries.hasMoreElements()) {
                ZipEntry zipEntry = zipEntries.nextElement();

                if (zipEntry.isDirectory()) continue;

                String fileName = zipEntry.getName().substring(zipEntry.getName().lastIndexOf("/") + 1);

                String destinationFolder;
                if (fileName.matches(goldDataRegex)) {
                    destinationFolder = goldDataFolder;
                } else if (fileName.matches(outputDataRegex)) {
                    destinationFolder = outputDataFolder;
                } else {
                    log.info("File {} does not match either gold_data ({}) nor output_data ({}). Archive: {}",
                            fileName, goldDataRegex, outputDataRegex, zipFile);
                    continue;
                }

                File unzippedFile = new File(destinationFolder, fileName);
                unzippedFile.getParentFile().mkdirs();

                try (InputStream in = file.getInputStream(zipEntry);
                     OutputStream out = new FileOutputStream(unzippedFile)) {
                    IOUtils.copy(in, out);
                } catch (IOException e) {
                    log.error("Error while extracting file: {} from archive {}", fileName, zipFile, e);
                }
            }
        } catch (IOException e) {
            log.error("Error while extracting zip archive: {}", zipFile, e);
        }

    }

}
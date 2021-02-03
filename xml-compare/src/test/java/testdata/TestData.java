package testdata;

import lombok.extern.log4j.Log4j2;
import org.testng.annotations.DataProvider;
import utils.AppProperties;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Log4j2
public class TestData {

    @DataProvider(name = "getXmlFiles")
    public static Object[][] getXmlFiles() {

        String goldData = AppProperties.getProperty("gold_data", null);
        String outputData = AppProperties.getProperty("output_data", null);
        String[] ignoreNodes = AppProperties.getProperty("ignore_nodes", "").split("([,;])");

        return new Object[][]{
                {goldData, outputData, ignoreNodes}
        };
    }

    @DataProvider(name = "getXmlFilesFromGoldDataAndOutputDataFolders", parallel = true)
    public static Object[][] getXmlFilesFromGoldDataAndOutputDataFolders() {

        String gold_data_folder = AppProperties.getProperty("gold_data_folder");
        String output_data_folder = AppProperties.getProperty("output_data_folder");
        String[] ignoreNodes = AppProperties.getProperty("ignore_nodes", "").split("([,;])");
        try {
            List<String> goldDataFilenames = Files.list(Paths.get(gold_data_folder))
                    .map(p -> p.getFileName().toString())
                    .collect(Collectors.toList());
            List<String> outputDataFilenames = Files.list(Paths.get(output_data_folder))
                    .map(p -> p.getFileName().toString())
                    .collect(Collectors.toList());

            Set<String> goldDataNumbers = goldDataFilenames.stream()
                    .map(s -> s.substring(1))
                    .collect(Collectors.toSet());
            Set<String> outputDataNumbers = outputDataFilenames.stream()
                    .map(s -> s.substring(1))
                    .collect(Collectors.toSet());

            Set<String> unionData = new HashSet<>(goldDataNumbers);
            unionData.addAll(outputDataNumbers);

            Object[][] result = new Object[unionData.size()][3];
            int counter = 0;
            for (String s : unionData) {
                String goldData = goldDataNumbers.contains(s)
                        ? gold_data_folder + "/" + goldDataFilenames.stream().filter(name -> name.substring(1).equals(s)).findFirst().get()
                        : null;
                String outputData = outputDataNumbers.contains(s)
                        ? output_data_folder + "/" + outputDataFilenames.stream().filter(name -> name.substring(1).equals(s)).findFirst().get()
                        : null;
                result[counter] = new Object[]{goldData, outputData, ignoreNodes};
                counter++;

                if (goldData != null && outputData == null) {
                    log.info("Gold data file '{}' has no output data pair", goldData);
                } else if (goldData == null && outputData != null) {
                    log.info("Output data file '{}' has no gold data pair", outputData);
                } else {
                    log.info("Gold data file '{}' has paired with output data file '{}'", goldData, outputData);
                }
            }

            return result;
        } catch (IOException e) {
            log.error("Error while getting filenames from folders: {}; {}", gold_data_folder,
                    output_data_folder, e);
        }

        return new Object[][]{};
    }

}

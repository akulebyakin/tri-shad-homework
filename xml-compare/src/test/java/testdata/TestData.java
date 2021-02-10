package testdata;

import exceptions.TestDataException;
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

    private static final String NO_GOLD_DATA_PAIR_ERR_MSG = "Gold data file '%s' has no output data pair";
    private static final String NO_OUTPUT_DATA_PAIR_ERR_MSG = "Output data file '%s' has no gold data pair";

    @DataProvider(name = "getXmlFiles")
    public static Object[][] getXmlFiles() {
        String goldData = AppProperties.getProperty("task_one_gold_data", null);
        String outputData = AppProperties.getProperty("task_one_output_data", null);
        String[] ignoreNodes = AppProperties.getProperty("task_one_ignore_nodes", "").split("([,;])");

        return new Object[][]{
                {goldData, outputData, ignoreNodes}
        };
    }

    @DataProvider(name = "getXmlFilesFromGoldDataAndOutputDataFolders", parallel = true)
    public static Object[][] getXmlFilesFromGoldDataAndOutputDataFolders() throws TestDataException {
        String goldDataFolder = AppProperties.getProperty("task_two_gold_data_folder");
        String outputDataFolder = AppProperties.getProperty("task_two_output_data_folder");
        String[] ignoreNodesDefinitions = AppProperties.getProperty("task_two_ignore_nodes_definitions", "").split("([,;])");

        try {
            List<String> goldDataFilenames = Files.list(Paths.get(goldDataFolder))
                    .map(p -> p.getFileName().toString())
                    .collect(Collectors.toList());
            List<String> outputDataFilenames = Files.list(Paths.get(outputDataFolder))
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
                        ? goldDataFolder + "/" + goldDataFilenames.stream().filter(name -> name.substring(1).equals(s)).findFirst().get()
                        : null;
                String outputData = outputDataNumbers.contains(s)
                        ? outputDataFolder + "/" + outputDataFilenames.stream().filter(name -> name.substring(1).equals(s)).findFirst().get()
                        : null;

                result[counter] = new Object[]{goldData, outputData, ignoreNodesDefinitions};
                counter++;

                if (goldData != null && outputData == null) {
                    String errorMessage = String.format(NO_GOLD_DATA_PAIR_ERR_MSG, goldData);
                    log.error(errorMessage);
                    throw new TestDataException(errorMessage);
                } else if (goldData == null && outputData != null) {
                    String errorMessage = String.format(NO_OUTPUT_DATA_PAIR_ERR_MSG, outputData);
                    log.error(errorMessage);
                    throw new TestDataException(errorMessage);
                } else {
                    log.info("Gold data file '{}' has paired with output data file '{}'",
                            goldData, outputData);
                }
            }

            return result;
        } catch (IOException e) {
            log.error("Error while getting filenames from folders: {}; {}", goldDataFolder,
                    outputDataFolder, e);
        }

        return new Object[][]{};
    }

}

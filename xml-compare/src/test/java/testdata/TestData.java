package testdata;

import lombok.extern.log4j.Log4j2;
import org.testng.annotations.DataProvider;
import utils.AppProperties;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
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

    @DataProvider
    public static Object[][] getXmlFilesFromGoldDataAndOutputDataFolders() {

        String gold_data_folder = AppProperties.getProperty("gold_data_folder");
        String output_data_folder = AppProperties.getProperty("output_data_folder");
        String goldDataRegex = AppProperties.getProperty("gold_data_regex");
        String outputDataRegex = AppProperties.getProperty("output_data_regex");
        try {
            List<String> goldDataFileNames = Files.list(Paths.get(gold_data_folder))
                    .map(p -> p.getFileName().toString())
                    .collect(Collectors.toList());
            List<String> outputDataFileNames = Files.list(Paths.get(output_data_folder))
                    .map(p -> p.getFileName().toString())
                    .collect(Collectors.toList());


            System.out.println("debug");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new Object[][]{};

    }

}

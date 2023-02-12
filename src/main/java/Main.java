import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";
        List<Employee> list = parceCSV(columnMapping, fileName);
        listToJSON(list);
    }

    public static List<Employee> parceCSV(String[] columnMapping, String fileName) {
        List<Employee> myEmployeeList = null;
        try (CSVReader reader = new CSVReader(new FileReader(fileName))) {
            ColumnPositionMappingStrategy<Employee> strategy =
                    new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);
            CsvToBean<Employee> myCSVParsing = new CsvToBeanBuilder<Employee>(reader)
                    .withMappingStrategy(strategy)
                    .build();
            myEmployeeList = myCSVParsing.parse();
            myEmployeeList.forEach(System.out::println);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return myEmployeeList;
    }

    public static void listToJSON(List<Employee> list) {

        Type listType = new TypeToken<List<Employee>>() {}.getType();
        GsonBuilder myJsonBuilder = new GsonBuilder();
        Gson myGson = myJsonBuilder.create();
        String json = myGson.toJson(list, listType);
        writeString(json);
    }

    public static void writeString(String jsonString) {
        try (FileWriter file = new
                FileWriter("new_data.json")) {
            file.write(jsonString);
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

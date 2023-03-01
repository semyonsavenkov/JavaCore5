import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvValidationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import java.lang.reflect.Field;

public class Main {

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {

//        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
//        String fileName = "data.csv";
//        List<Employee> list = parceCSV(columnMapping, fileName);
//        listToJSON(list);
        List<Employee> list = parceXML( "data.xml");


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

    public static List<Employee> parceXML(String fileName) throws ParserConfigurationException, IOException, SAXException {
        List<Employee> myEmployeeList = null;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse( new File(fileName));
        Node root = doc.getDocumentElement();
        NodeList myNodeList = root.getChildNodes();
        for (int i = 0; i < myNodeList.getLength(); i++) {
            Node currentNode = myNodeList.item(i);
            if (Node.ELEMENT_NODE == currentNode.getNodeType()) {
                Element employeeXML  = (Element) currentNode;
                if (employeeXML.getNodeName() == "employee") {
                    NodeList employeeFields = employeeXML.getChildNodes();

//                    Class<Employee> myEmployeeClass = (Class<Employee>) employee.getClass();
//                    Field[] fields = myEmployeeClass.getDeclaredFields();
//                    fields.

                    long currentID = 0;
                    String currentFirstName = "";
                    String currentLastName = "";
                    String currentCountry = "";
                    int currentAge = 0;
                    for (int j = 0; j < employeeFields.getLength(); j++) {
                        Node currentField = employeeFields.item(j);
                        String fieldName = currentField.getNodeName();
                        switch (fieldName) {
                            case "id":
                                currentID = Long.parseLong(currentField.getNodeValue());
                            case "firstName":
                                currentFirstName = currentField.getNodeValue();
                            case "lastName":
                                currentLastName = currentField.getNodeValue();
                            case "country":
                                currentCountry = currentField.getNodeValue();
                            case "age":
                                currentAge = Integer.parseInt(currentField.getNodeValue());
                        }
                    }
                    Employee employee = new Employee(currentID, currentFirstName, currentLastName, currentCountry, currentAge);
                    myEmployeeList.add(employee);
                }
            }
        }


        return myEmployeeList;
    }

}

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
import java.util.ArrayList;
import java.util.List;

import java.lang.reflect.Field;

public class Main<myEmployeeList> {

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {

//        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
//        String fileName = "data.csv";
//        List<Employee> list = parceCSV(columnMapping, fileName);
//        listToJSON(list);
        List<Employee> list = parceXML("data.xml");
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

        } catch (IOException e) {
            e.printStackTrace();
        }

        return myEmployeeList;
    }

    public static void listToJSON(List<Employee> list) {

        Type listType = new TypeToken<List<Employee>>() {
        }.getType();
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
        List<Employee> myEmployeeList = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new File(fileName));
        Node root = doc.getDocumentElement();
        NodeList myNodeList = root.getChildNodes();
        for (int i = 0; i < myNodeList.getLength(); i++) {
            Node currentNode = myNodeList.item(i);
            if (Node.ELEMENT_NODE == currentNode.getNodeType()) {
                Element employeeXML = (Element) currentNode;
                NodeList nlId = employeeXML.getElementsByTagName("id");
                for (int j = 0; j < nlId.getLength(); j++) {
                    Employee myEmployee = new Employee(Long.parseLong(nlId.item(j).getTextContent()),
                            employeeXML.getElementsByTagName("firstName").item(j).getTextContent(),
                            employeeXML.getElementsByTagName("lastName").item(j).getTextContent(),
                            employeeXML.getElementsByTagName("country").item(j).getTextContent(),
                            Integer.parseInt(employeeXML.getElementsByTagName("age").item(j).getTextContent()));
                    myEmployeeList.add(myEmployee);
//                    myEmployeeList.add(new Employee(Long.parseLong(nlId.item(j).getTextContent()),
//                            employeeXML.getElementsByTagName("firstName").item(j).getTextContent(),
//                            employeeXML.getElementsByTagName("lastName").item(j).getTextContent(),
//                            employeeXML.getElementsByTagName("country").item(j).getTextContent(),
//                            Integer.parseInt(employeeXML.getElementsByTagName("age").item(j).getTextContent())));
                }

            }
        }
        return myEmployeeList;
    }

}


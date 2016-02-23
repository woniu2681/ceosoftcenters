package boomi.sample;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.TreeMap;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class EmployeeList {
  public static void main(String[] args) throws Exception {
    TreeMap<String, Element> previousTreeMap = new TreeMap<String, Element>();
    SAXBuilder builder = new SAXBuilder();
    XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());

    for (int i = 0; i < 2; i++) {
      Document doc = builder.build(new FileInputStream("src/boomi/sample/EmployeeList" + i + ".xml"));
      Element root = doc.getRootElement();
      List<Element> employeeList = root.getChildren();

      if (employeeList != null && !employeeList.isEmpty()) {
        for (Element employee : employeeList) {
          String personIdExternal = employee.getChildText("personIdExternal");
          if (i == 0) {
            previousTreeMap.put(personIdExternal, (Element) employee.clone());
          } else if (i == 1) {
            Element previous = previousTreeMap.get(personIdExternal);
            if (previous != null) {
              Element current = (Element) employee.clone();
              String previousXML = xmlOutputter.outputString(previous);
              String currentXML = xmlOutputter.outputString(current);
              if (!previousXML.equals(currentXML)) {
                InputStream os = new ByteArrayInputStream(currentXML.getBytes("UTF-8"));
                System.out.println(currentXML);
              }
            }
          }
        }
      }
    }
  }
}

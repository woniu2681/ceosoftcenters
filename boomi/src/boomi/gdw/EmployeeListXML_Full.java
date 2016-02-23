package boomi.gdw;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;

import org.jaxen.jdom.JDOMXPath;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class EmployeeListXML_Full {
  public static void main(String[] args) throws Exception {
    String LAST_DAY = "9999-12-31";
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Calendar cal = Calendar.getInstance();
    SAXBuilder builder = new SAXBuilder();
    XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());

    for (int i = 0; i < 1; i++) {
      Document doc = builder.build(new FileInputStream("src/boomi/gdw/EmployeeList" + i + ".xml"));

      Element root = doc.getRootElement();
      List<Element> employeeList = root.getChildren("Employee");

      if (employeeList != null && !employeeList.isEmpty()) {
        for (Element employee : employeeList) {
          String personIdExternal = employee.getChildText("personIdExternal");
          Element currentEmployee = new Element("Employee");
          currentEmployee.addContent(employee.cloneContent());
          Document currentEmployeeDoc = new Document(currentEmployee);

          JDOMXPath noEffectiveXPath = new JDOMXPath("//NoEffective");
          Element currentNoEffectiveElement = (Element) noEffectiveXPath.selectSingleNode(currentEmployee);

          JDOMXPath effectiveXPath = new JDOMXPath("//Effective/*");
          List<Element> currentEffectiveElements = effectiveXPath.selectNodes(currentEmployee);

          TreeSet<String> currentStartDateTreeSet = new TreeSet<String>();

          for (Element currentEffectiveElement : currentEffectiveElements) {
            String currentStartDateStr = currentEffectiveElement.getChildText("startDate");
            currentStartDateTreeSet.add(currentStartDateStr);
          }

          for (String currentStartDateStr : currentStartDateTreeSet) {
            Element currentEmployeeElement = new Element("Employee");
            currentEmployeeElement.addContent(new Element("action").setText("F - FirstRun - " + currentStartDateStr));
            currentEmployeeElement.addContent(new Element("personIdExternal").setText(personIdExternal));
            currentEmployeeElement.addContent(new Element("startDate").setText(currentStartDateStr));
            String currentEndDateStr = currentStartDateTreeSet.higher(currentStartDateStr);
            String str = LAST_DAY;
            if (currentEndDateStr != null) {
              cal.setTime(sdf.parse(currentEndDateStr));
              cal.add(Calendar.DAY_OF_MONTH, -1);
              str = sdf.format(cal.getTime());
            }
            currentEmployeeElement.addContent(new Element("endDate").setText(str));
            for (Element currentEffectiveElement : currentEffectiveElements) {
              Date updatedStartDate = sdf.parse(currentStartDateStr);
              String startDateStr = currentEffectiveElement.getChildText("startDate");
              Date startDate = sdf.parse(startDateStr);
              String endDateStr = currentEffectiveElement.getChildText("endDate");
              Date endDate = sdf.parse(endDateStr);
              if (startDate.compareTo(updatedStartDate) <= 0 && endDate.compareTo(updatedStartDate) >= 0) {
                Element element = (Element) currentEffectiveElement.clone();
                element.removeChild("startDate");
                element.removeChild("endDate");
                element.removeChild("seqNumber");
                currentEmployeeElement.addContent(element);
              }
            }

            if (currentNoEffectiveElement != null) {
              currentEmployeeElement.addContent(currentNoEffectiveElement.cloneContent());
            }

            InputStream os = new ByteArrayInputStream(xmlOutputter.outputString(currentEmployeeElement).getBytes("UTF-8"));
            System.out.println(xmlOutputter.outputString(currentEmployeeElement));
          }
        }
      }
    }
  }
}
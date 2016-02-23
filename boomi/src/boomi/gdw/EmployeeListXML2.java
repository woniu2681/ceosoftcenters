package boomi.gdw;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

import org.jaxen.jdom.JDOMXPath;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class EmployeeListXML2 {
  public static void main(String[] args) throws Exception {
    String LAST_DAY = "9999-12-31";
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Calendar cal = Calendar.getInstance();
    SAXBuilder builder = new SAXBuilder();
    XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
    TreeMap<String, Element> previousRunMap = new TreeMap<String, Element>();

    for (int i = 0; i < 2; i++) {
      Document doc = builder.build(new FileInputStream("src/boomi/gdw/EmployeeList" + i + ".xml"));
      Element root = doc.getRootElement();
      List<Element> employeeList = root.getChildren("Employee");

      if (employeeList != null && !employeeList.isEmpty()) {
        for (Element employee : employeeList) {
          String personIdExternal = employee.getChildText("personIdExternal");
          
          if (i == 0) {
            previousRunMap.put(personIdExternal, employee);
          } else if (i == 1) {
            Element currentEmployee = new Element("Employee");
            currentEmployee.addContent(employee.cloneContent());
            Document currentEmployeeDoc = new Document(currentEmployee);

            Element previousElement = previousRunMap.get(personIdExternal);
            Element previousEmployee = new Element("Employee");
            previousEmployee.addContent(previousElement != null ? previousElement.cloneContent() : null);
            Document previousEmployeeDoc = new Document(previousEmployee);

            JDOMXPath noEffectiveXPath = new JDOMXPath("//NoEffective");
            Element currentNoEffectiveElement = (Element) noEffectiveXPath.selectSingleNode(currentEmployee);
            Element previousNoEffectiveElement = (Element) noEffectiveXPath.selectSingleNode(previousEmployee);

            JDOMXPath effectiveXPath = new JDOMXPath("//Effective/*");
            List<Element> currentEffectiveElements = effectiveXPath.selectNodes(currentEmployee);
            List<Element> previousEffectiveElements = effectiveXPath.selectNodes(previousEmployee);

            TreeSet<String> currentStartDateTreeSet = new TreeSet<String>();
            TreeSet<String> previousStartDateTreeSet = new TreeSet<String>();

            for (Element previousEffectiveElement : previousEffectiveElements) {
              String previousStartDateStr = previousEffectiveElement.getChildText("startDate");
              previousStartDateTreeSet.add(previousStartDateStr);
            }

            for (Element currentEffectiveElement : currentEffectiveElements) {
              String currentStartDateStr = currentEffectiveElement.getChildText("startDate");
              currentStartDateTreeSet.add(currentStartDateStr);
            }

            for (String previousStartDateStr : previousStartDateTreeSet) {
              if (!currentStartDateTreeSet.contains(previousStartDateStr)) {
                Element deletedEmployee = new Element("Employee");
                deletedEmployee.addContent(new Element("action").setText("D - Delete"));
                deletedEmployee.addContent(new Element("personIdExternal").setText(personIdExternal));
                deletedEmployee.addContent(new Element("startDate").setText(previousStartDateStr));
                String previousEndDateStr = previousStartDateTreeSet.higher(previousStartDateStr);
                String str = LAST_DAY;
                if (previousEndDateStr != null) {
                  cal.setTime(sdf.parse(previousEndDateStr));
                  cal.add(Calendar.DAY_OF_MONTH, -1);
                  str = sdf.format(cal.getTime());
                }
                deletedEmployee.addContent(new Element("endDate").setText(str));
                for (Element previousEffectiveElement : previousEffectiveElements) {
                  Date deletedStartDate = sdf.parse(previousStartDateStr);
                  String startDateStr = previousEffectiveElement.getChildText("startDate");
                  Date startDate = sdf.parse(startDateStr);
                  String endDateStr = previousEffectiveElement.getChildText("endDate");
                  Date endDate = sdf.parse(endDateStr);
                  if (startDate.compareTo(deletedStartDate) <= 0 && endDate.compareTo(deletedStartDate) >= 0) {
                    Element element = (Element) previousEffectiveElement.clone();
                    element.removeChild("startDate");
                    element.removeChild("endDate");
                    element.removeChild("seqNumber");
                    deletedEmployee.addContent(element);
                  }
                }

                if (previousNoEffectiveElement != null) {
                  deletedEmployee.addContent(previousNoEffectiveElement.cloneContent());
                }

                InputStream os = new ByteArrayInputStream(xmlOutputter.outputString(deletedEmployee).getBytes("UTF-8"));
                System.out.println(xmlOutputter.outputString(deletedEmployee));
              }
            }

            for (String currentStartDateStr : currentStartDateTreeSet) {
              if (!previousStartDateTreeSet.contains(currentStartDateStr)) {
                Element insertedEmployee = new Element("Employee");
                insertedEmployee.addContent(new Element("action").setText("I - Insert"));
                insertedEmployee.addContent(new Element("personIdExternal").setText(personIdExternal));
                insertedEmployee.addContent(new Element("startDate").setText(currentStartDateStr));
                String currentEndDateStr = currentStartDateTreeSet.higher(currentStartDateStr);
                String str = LAST_DAY;
                if (currentEndDateStr != null) {
                  cal.setTime(sdf.parse(currentEndDateStr));
                  cal.add(Calendar.DAY_OF_MONTH, -1);
                  str = sdf.format(cal.getTime());
                }
                insertedEmployee.addContent(new Element("endDate").setText(str));
                for (Element currentElement : currentEffectiveElements) {
                  Date insertedStartDate = sdf.parse(currentStartDateStr);
                  String startDateStr = currentElement.getChildText("startDate");
                  Date startDate = sdf.parse(startDateStr);
                  String endDateStr = currentElement.getChildText("endDate");
                  Date endDate = sdf.parse(endDateStr);
                  if (startDate.compareTo(insertedStartDate) <= 0 && endDate.compareTo(insertedStartDate) >= 0) {
                    Element element = (Element) currentElement.clone();
                    element.removeChild("startDate");
                    element.removeChild("endDate");
                    element.removeChild("seqNumber");
                    insertedEmployee.addContent(element);
                  }
                }

                if (currentNoEffectiveElement != null) {
                  insertedEmployee.addContent(currentNoEffectiveElement.cloneContent());
                }

                InputStream os = new ByteArrayInputStream(xmlOutputter.outputString(insertedEmployee).getBytes("UTF-8"));
                System.out.println(xmlOutputter.outputString(insertedEmployee));
              } else {
                Element currentEmployeeElement = new Element("Employee");
                currentEmployeeElement.addContent(new Element("action").setText("U - Update"));
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

                Element previousEmployeeElement = new Element("Employee");
                previousEmployeeElement.addContent(new Element("action").setText("U - Update"));
                previousEmployeeElement.addContent(new Element("personIdExternal").setText(personIdExternal));
                previousEmployeeElement.addContent(new Element("startDate").setText(currentStartDateStr));
                String previousEndDateStr = previousStartDateTreeSet.higher(currentStartDateStr);
                String str1 = LAST_DAY;
                if (previousEndDateStr != null) {
                  cal.setTime(sdf.parse(previousEndDateStr));
                  cal.add(Calendar.DAY_OF_MONTH, -1);
                  str1 = sdf.format(cal.getTime());
                }
                previousEmployeeElement.addContent(new Element("endDate").setText(str1));
                for (Element previousEffectiveElement : previousEffectiveElements) {
                  Date updatedStartDate = sdf.parse(currentStartDateStr);
                  String startDateStr = previousEffectiveElement.getChildText("startDate");
                  Date startDate = sdf.parse(startDateStr);
                  String endDateStr = previousEffectiveElement.getChildText("endDate");
                  Date endDate = sdf.parse(endDateStr);
                  if (startDate.compareTo(updatedStartDate) <= 0 && endDate.compareTo(updatedStartDate) >= 0) {
                    Element element = (Element) previousEffectiveElement.clone();
                    element.removeChild("startDate");
                    element.removeChild("endDate");
                    element.removeChild("seqNumber");
                    previousEmployeeElement.addContent(element);
                  }
                }

                String currentXML = xmlOutputter.outputString(currentEmployeeElement);
                String previousXML = xmlOutputter.outputString(previousEmployeeElement);

                if (!currentXML.equals(previousXML)) {
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
    }
  }
}
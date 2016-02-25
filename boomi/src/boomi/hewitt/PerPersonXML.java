package boomi.hewitt;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

public class PerPersonXML {
  public static void main(String[] args) throws Exception {
    SAXBuilder builder = new SAXBuilder();
    XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
    SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
    Date tempLastRunDate = sdf2.parse("2014-01-01");
    Date tempCurrentRunDate = sdf2.parse("2016-01-01");
    // Date tempLastRunDate = sdf2.parse(ExecutionUtil.getDynamicProcessProperty("TempLastRunDate"));
    // Date tempCurrentRunDate = sdf2.parse(ExecutionUtil.getDynamicProcessProperty("TempCurrentRunDate"));

    for (int i = 0; i < 1; i++) {
      Document doc = builder.build(new FileInputStream("src/boomi/hewitt/PerPerson.xml"));
      Element root = doc.getRootElement();

      Element personIdExternal = root.getChild("personIdExternal");
      Element dateOfBirth = root.getChild("dateOfBirth");
      Element noEffective = root.getChild("NoEffective");

      JDOMXPath effectiveXPath = new JDOMXPath("/PerPerson/Effective/*");
      List<Element> effectiveElements = effectiveXPath.selectNodes(root);

      TreeSet<Date> effectiveDateTreeSet = new TreeSet<Date>();
      TreeMap<Date, Element> employeeTreeMap = new TreeMap<Date, Element>();

      for (Element effectiveElement : effectiveElements) {
        String effectiveDateStr = effectiveElement.getChildText("startDate");
        Date effectiveDate = sdf1.parse(effectiveDateStr);
        effectiveDateTreeSet.add(effectiveDate);
      }

      for (Date effectiveDate : effectiveDateTreeSet) {
        Element employeeElement = new Element("Employee");
        employeeElement.addContent((Element) personIdExternal.clone());
        employeeElement.addContent((Element) dateOfBirth.clone());
        employeeElement.addContent(noEffective != null ? noEffective.cloneContent() : null);

        for (Element effectiveElement : effectiveElements) {
          String startDateStr = effectiveElement.getChildText("startDate");
          Date startDate = sdf1.parse(startDateStr);
          String endDateStr = effectiveElement.getChildText("endDate");
          Date endDate = sdf1.parse(endDateStr);
          if (startDate.compareTo(effectiveDate) <= 0 && endDate.compareTo(effectiveDate) >= 0) {
            employeeElement.addContent((Element) effectiveElement.clone());
          }
        }

        employeeTreeMap.put(effectiveDate, employeeElement);
      }

      List<Element> outputList = new ArrayList<Element>();
      boolean noneEMC = false;

      for (Date effectiveDate : employeeTreeMap.keySet()) {
        Element currentEmployeeElement = employeeTreeMap.get(effectiveDate);

        if (effectiveDate.compareTo(tempLastRunDate) >= 0 && effectiveDate.compareTo(tempCurrentRunDate) < 0) {
          Element currentEmpJobElement = currentEmployeeElement.getChild("EmpJob");
          Element currentPerPersonalElement = currentEmployeeElement.getChild("PerPersonal");
          Element currentPaymentInformationDetailV3Element = currentEmployeeElement.getChild("PaymentInformationDetailV3");

          if (currentEmpJobElement != null) {
            String currentEmpJobEvent = currentEmpJobElement.getChildText("event");
            String currentEmpJobStartDateStr = currentEmpJobElement.getChildText("startDate");
            String currentEmpJobEmployeeClass = currentEmpJobElement.getChildText("employeeClassCode");
            String currentEmpJobCustomString4 = currentEmpJobElement.getChildText("customString4");

            if (currentEmpJobEvent.equals("H") && effectiveDate.compareTo(sdf1.parse(currentEmpJobStartDateStr)) == 0) {
              Element newEmployeeElement = employeeTreeMap.lastEntry().getValue();
              newEmployeeElement.addContent(new Element("category").setText("NEW"));
              newEmployeeElement.addContent(new Element("effectiveDate").setText(sdf1.format(effectiveDate)));
              outputList.add(newEmployeeElement);
              noneEMC = true;
            } else if (currentEmpJobEvent.equals("R") && effectiveDate.compareTo(sdf1.parse(currentEmpJobStartDateStr)) == 0) {
              Element newEmployeeElement = employeeTreeMap.lastEntry().getValue();
              newEmployeeElement.addContent(new Element("category").setText("REH"));
              newEmployeeElement.addContent(new Element("effectiveDate").setText(sdf1.format(effectiveDate)));
              outputList.add(newEmployeeElement);
              noneEMC = true;
            } else if (currentEmpJobEvent.equals("26") && effectiveDate.compareTo(sdf1.parse(currentEmpJobStartDateStr)) == 0) {
              Element newEmployeeElement = new Element("Employee");
              newEmployeeElement.addContent((Element) personIdExternal.clone());
              newEmployeeElement.addContent(new Element("category").setText("R"));
              newEmployeeElement.addContent(new Element("effectiveDate").setText(sdf1.format(effectiveDate)));
              newEmployeeElement.addContent(new Element("EmpJob").addContent(new Element("customString4").setText(currentEmpJobCustomString4)));
              outputList.add(newEmployeeElement);
            } else {
              if (employeeTreeMap.lowerEntry(effectiveDate) != null) {
                Element previousEmployeeElement = employeeTreeMap.lowerEntry(effectiveDate).getValue();
                Element previousEmpJobElement = previousEmployeeElement.getChild("EmpJob");
                Element previousPerPersonalElement = previousEmployeeElement.getChild("PerPersonal");
                Element previousPaymentInformationDetailV3Element = previousEmployeeElement.getChild("PaymentInformationDetailV3");
                String previousEmpJobEmployeeClass = null;
                if (previousEmpJobElement != null) {
                  previousEmpJobEmployeeClass = previousEmpJobElement.getChildText("employeeClassCode");
                }

                if (previousEmpJobEmployeeClass != null && previousEmpJobEmployeeClass.equals("PS") && currentEmpJobEmployeeClass != null && !currentEmpJobEmployeeClass.equals("PS") && !currentEmpJobEmployeeClass.equals("LE")) {
                  currentEmployeeElement.addContent(new Element("category").setText("NEW"));
                  currentEmployeeElement.addContent(new Element("effectiveDate").setText(sdf1.format(effectiveDate)));
                  outputList.add(currentEmployeeElement);
                } else {
                  Element newEmployeeElement = new Element("Employee");
                  TreeSet<Date> dateTreeSet = new TreeSet<Date>();

                  Element newEmpJobElement = new Element("EmpJob");
                  if (currentEmpJobElement != null) {
                    newEmpJobElement = (Element) currentEmpJobElement.clone();
                    List<Element> currentEmpJobChildren = currentEmpJobElement.getChildren();
                    boolean isChanged = false;
                    for (Element currentChildElement : currentEmpJobChildren) {
                      String currentChildElementName = currentChildElement.getName();
                      String currentChildElementText = currentChildElement.getText();
                      String previousChildElementText = previousEmpJobElement != null ? previousEmpJobElement.getChildText(currentChildElementName) : "";
                      if (!currentChildElementName.equals("startDate") && !currentChildElementName.equals("endDate") && !currentChildElementName.equals("seqNumber") && !currentChildElementName.equals("customString4") && !currentChildElementName.equals("employeeClassCode") && !currentChildElementName.equals("emplStatusCode") && !currentChildElementName.equals("event") && !currentChildElementName.equals("locationCode")) {
                        if (previousChildElementText.equals(currentChildElementText)) {
                          newEmpJobElement.removeChild(currentChildElementName);
                        } else {
                          isChanged = true;
                        }
                      }
                    }

                    if (isChanged) {
                      dateTreeSet.add(sdf1.parse(currentEmpJobElement.getChildText("startDate")));
                      newEmployeeElement.addContent(newEmpJobElement);
                    }
                  }

                  if (currentPerPersonalElement != null) {
                    Element newPerPersonalElement = (Element) currentPerPersonalElement.clone();
                    List<Element> currentPerPersonalChildren = currentPerPersonalElement.getChildren();
                    boolean isChanged = false;
                    for (Element currentChildElement : currentPerPersonalChildren) {
                      String currentChildElementName = currentChildElement.getName();
                      String currentChildElementText = currentChildElement.getText();
                      String previousChildElementText = previousPerPersonalElement != null ? previousPerPersonalElement.getChildText(currentChildElementName) : "";
                      if (!currentChildElementName.equals("startDate") && !currentChildElementName.equals("endDate")) {
                        if (previousChildElementText.equals(currentChildElementText)) {
                          newPerPersonalElement.removeChild(currentChildElementName);
                        } else {
                          isChanged = true;
                        }
                      }
                    }

                    if (isChanged) {
                      dateTreeSet.add(sdf1.parse(currentPerPersonalElement.getChildText("startDate")));
                      newEmployeeElement.addContent(newPerPersonalElement);
                    }
                  }

                  if (currentPaymentInformationDetailV3Element != null) {
                    Element newPaymentInformationDetailV3Element = (Element) currentPaymentInformationDetailV3Element.clone();
                    List<Element> currentPaymentInformationDetailV3Children = currentPaymentInformationDetailV3Element.getChildren();
                    boolean isChanged = false;
                    for (Element currentChildElement : currentPaymentInformationDetailV3Children) {
                      String currentChildElementName = currentChildElement.getName();
                      String currentChildElementText = currentChildElement.getText();
                      String previousChildElementText = previousPaymentInformationDetailV3Element != null ? previousPaymentInformationDetailV3Element.getChildText(currentChildElementName) : "";
                      if (!currentChildElementName.equals("startDate") && !currentChildElementName.equals("endDate")) {
                        if (previousChildElementText.equals(currentChildElementText)) {
                          newPaymentInformationDetailV3Element.removeChild(currentChildElementName);
                        } else {
                          isChanged = true;
                        }
                      }
                    }

                    if (isChanged) {
                      dateTreeSet.add(sdf1.parse(currentPaymentInformationDetailV3Element.getChildText("startDate")));
                      newEmployeeElement.addContent(newPaymentInformationDetailV3Element);
                    }
                  }

                  if (newEmployeeElement != null && newEmployeeElement.getContentSize() != 0) {
                    newEmployeeElement.addContent((Element) personIdExternal.clone());
                    newEmployeeElement.addContent(new Element("category").setText("EMC"));
                    newEmployeeElement.addContent(new Element("effectiveDate").setText(sdf1.format(dateTreeSet.last())));
                    if (newEmployeeElement.getChild("EmpJob") == null) {
                      newEmployeeElement.addContent(newEmpJobElement);
                    }
                    outputList.add(newEmployeeElement);
                  }
                }
              }
            }
          }
        }
      }

      if (outputList != null && !outputList.isEmpty()) {
        for (int j = 0; j < outputList.size(); j++) {
          Element e = outputList.get(j);
          if (e.getChildText("category").equals("EMC") && noneEMC) {
            outputList.remove(j);
          }
        }

        for (Element output : outputList) {
          InputStream os = new ByteArrayInputStream(xmlOutputter.outputString(output).getBytes("UTF-8"));
          System.out.println(xmlOutputter.outputString(output));
        }

      }
    }
  }
}

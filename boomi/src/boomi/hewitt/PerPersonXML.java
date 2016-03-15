package boomi.hewitt;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
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
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    Date tempLastRunDate = sdf.parse("2010-01-01");
    Date tempCurrentRunDate = sdf.parse("2016-03-01");
    // Date tempLastRunDate = sdf.parse(ExecutionUtil.getDynamicProcessProperty("TempLastRunDate"));
    // Date tempCurrentRunDate = sdf.parse(ExecutionUtil.getDynamicProcessProperty("TempCurrentRunDate"));

    final String HIRE_DATE = "HIRE_DATE";
    final String REHIRE_DATE = "REHIRE_DATE";

    JDOMXPath termDateXPath = new JDOMXPath("/PerPerson/Current/EmpEmployment/endDate");
    JDOMXPath badgeNumberXPath = new JDOMXPath("/PerPerson/Current/EmpJob/customString4");

    JDOMXPath effectiveDateXPath = new JDOMXPath("/PerPerson/History/*/startDate");
    JDOMXPath jobXPath = new JDOMXPath("/PerPerson/History/EmpJob");
    JDOMXPath paymentXPath = new JDOMXPath("/PerPerson/History/PaymentInformationDetailV3");
    JDOMXPath personalXPath = new JDOMXPath("/PerPerson/History/PerPersonal");

    InputStream os = null;

    for (int i = 0; i < 1; i++) {
      Document doc = builder.build(new FileInputStream("src/boomi/hewitt/PerPerson_New.xml"));
      Element root = doc.getRootElement();

      Element termDateElement = (Element) termDateXPath.selectSingleNode(doc);
      String termDateStr = termDateElement != null ? termDateElement.getText() : null;
      Date termDate = termDateStr != null ? sdf.parse(termDateStr) : null;

      Element personIdExternal = root.getChild("personIdExternal");
      Element dateOfBirth = root.getChild("dateOfBirth");
      Element current = root.getChild("Current");

      Element badgeNumberElement = (Element) badgeNumberXPath.selectSingleNode(doc);
      String badgeNumber = badgeNumberElement != null ? badgeNumberElement.getText() : null;

      List<Element> effectiveDateList = effectiveDateXPath.selectNodes(doc);
      List<Element> jobList = jobXPath.selectNodes(doc);
      List<Element> paymentList = paymentXPath.selectNodes(doc);
      List<Element> personalList = personalXPath.selectNodes(doc);
      TreeMap<Date, Element> jobTreeMap = new TreeMap<Date, Element>();

      if (effectiveDateList != null && !effectiveDateList.isEmpty()) {
        TreeMap<String, Date> eventMap = new TreeMap<String, Date>();

        if (jobList != null && !jobList.isEmpty()) {
          for (Element job : jobList) {
            String startDateStr = job.getChildText("startDate");
            String eventCode = job.getChildText("eventCode");
            Date startDate = sdf.parse(startDateStr);

            if (startDate.compareTo(tempLastRunDate) >= 0 && startDate.compareTo(tempCurrentRunDate) < 0) {
              if (eventCode != null && eventCode.equals("H")) {
                Date hireDate = eventMap.get(HIRE_DATE);
                if (hireDate == null || startDate.compareTo(hireDate) < 0) {
                  eventMap.put(HIRE_DATE, startDate);
                }
              } else if (eventCode != null && eventCode.equals("R")) {
                Date rehireDate = eventMap.get(REHIRE_DATE);
                if (rehireDate != null || startDate.compareTo(rehireDate) < 0) {
                  eventMap.put(REHIRE_DATE, startDate);
                }
              }

              Element preJob = jobTreeMap.get(startDate);
              if (preJob != null) {
                Integer preSeqNumber = Integer.parseInt(preJob.getChildText("seqNumber"));
                Integer curSeqNumber = Integer.parseInt(job.getChildText("seqNumber"));
                if (curSeqNumber > preSeqNumber) {
                  jobTreeMap.put(startDate, job);
                }
              } else {
                jobTreeMap.put(startDate, job);
              }

            }
          }
        }

        Date hireDate = eventMap.get(HIRE_DATE);
        Date rehireDate = eventMap.get(REHIRE_DATE);
        if (hireDate != null) {
          Element newEmployee = new Element("Employee");
          newEmployee.addContent((Element) personIdExternal.clone());
          newEmployee.addContent((Element) dateOfBirth.clone());
          newEmployee.addContent(new Element("category").setText("NEW"));
          newEmployee.addContent(new Element("effectiveDate").setText(sdf.format(hireDate)));
          newEmployee.addContent(current.cloneContent());
          os = new ByteArrayInputStream(xmlOutputter.outputString(newEmployee).getBytes("UTF-8"));
          System.out.println(xmlOutputter.outputString(newEmployee));

          if (termDate != null && termDate.compareTo(tempLastRunDate) >= 0 && termDate.compareTo(tempCurrentRunDate) < 0) {
            Element termEmployee = new Element("Employee");
            termEmployee.addContent((Element) personIdExternal.clone());
            termEmployee.addContent(new Element("category").setText("R"));
            termEmployee.addContent(new Element("effectiveDate").setText(termDateStr));
            termEmployee.addContent(new Element("EmpJob").addContent(new Element("customString4").setText(badgeNumber)));
            os = new ByteArrayInputStream(xmlOutputter.outputString(termEmployee).getBytes("UTF-8"));
            System.out.println(xmlOutputter.outputString(termEmployee));
          }
        } else if (rehireDate != null) {
          Element newEmployee = new Element("Employee");
          newEmployee.addContent((Element) personIdExternal.clone());
          newEmployee.addContent((Element) dateOfBirth.clone());
          newEmployee.addContent(new Element("category").setText("REH"));
          newEmployee.addContent(new Element("effectiveDate").setText(sdf.format(rehireDate)));
          newEmployee.addContent(current.cloneContent());
          os = new ByteArrayInputStream(xmlOutputter.outputString(newEmployee).getBytes("UTF-8"));
          System.out.println(xmlOutputter.outputString(newEmployee));

          if (termDate != null && termDate.compareTo(tempLastRunDate) >= 0 && termDate.compareTo(tempCurrentRunDate) < 0) {
            Element termEmployee = new Element("Employee");
            termEmployee.addContent((Element) personIdExternal.clone());
            termEmployee.addContent(new Element("category").setText("R"));
            termEmployee.addContent(new Element("effectiveDate").setText(termDateStr));
            termEmployee.addContent(new Element("EmpJob").addContent(new Element("customString4").setText(badgeNumber)));
            os = new ByteArrayInputStream(xmlOutputter.outputString(termEmployee).getBytes("UTF-8"));
            System.out.println(xmlOutputter.outputString(termEmployee));
          }
        } else {
          TreeSet<Date> effectiveDateTreeSet = new TreeSet<Date>();

          for (Element effectiveDateElement : effectiveDateList) {
            String effectiveDateStr = effectiveDateElement.getText();
            Date effectiveDate = sdf.parse(effectiveDateStr);

            effectiveDateTreeSet.add(effectiveDate);
          }

          TreeMap<Date, Element> employeeTreeMap = new TreeMap<Date, Element>();

          if (effectiveDateTreeSet != null && !effectiveDateTreeSet.isEmpty()) {
            for (Date effectiveDate : effectiveDateTreeSet) {
              Element employee = new Element("Employee");

              if (jobTreeMap != null && !jobTreeMap.isEmpty()) {
                for (Element effectiveElement : jobTreeMap.values()) {
                  String startDateStr = effectiveElement.getChildText("startDate");
                  Date startDate = sdf.parse(startDateStr);
                  String endDateStr = effectiveElement.getChildText("endDate");
                  Date endDate = sdf.parse(endDateStr);
                  if (startDate.compareTo(effectiveDate) <= 0 && endDate.compareTo(effectiveDate) >= 0) {
                    employee.addContent((Element) effectiveElement.clone());
                  }
                }
              }

              if (paymentList != null && !paymentList.isEmpty()) {
                for (Element payment : paymentList) {
                  String startDateStr = payment.getChildText("startDate");
                  Date startDate = sdf.parse(startDateStr);
                  String endDateStr = payment.getChildText("endDate");
                  Date endDate = sdf.parse(endDateStr);
                  if (startDate.compareTo(effectiveDate) <= 0 && endDate.compareTo(effectiveDate) >= 0) {
                    employee.addContent((Element) payment.clone());
                  }
                }
              }

              if (personalList != null && !personalList.isEmpty()) {
                for (Element personal : personalList) {
                  String startDateStr = personal.getChildText("startDate");
                  Date startDate = sdf.parse(startDateStr);
                  String endDateStr = personal.getChildText("endDate");
                  Date endDate = sdf.parse(endDateStr);
                  if (startDate.compareTo(effectiveDate) <= 0 && endDate.compareTo(effectiveDate) >= 0) {
                    employee.addContent((Element) personal.clone());
                  }
                }
              }

              employeeTreeMap.put(effectiveDate, employee);
            }
          }

          for (Date effectiveDate : employeeTreeMap.keySet()) {
            Element currentEmployeeElement = employeeTreeMap.get(effectiveDate);

            if (effectiveDate.compareTo(tempLastRunDate) >= 0 && effectiveDate.compareTo(tempCurrentRunDate) < 0) {
              Element currentEmpJobElement = currentEmployeeElement.getChild("EmpJob");
              Element currentPerPersonalElement = currentEmployeeElement.getChild("PerPersonal");
              Element currentPaymentInformationDetailV3Element = currentEmployeeElement.getChild("PaymentInformationDetailV3");

              if (currentEmpJobElement != null) {
                String currentEmpJobStartDateStr = currentEmpJobElement.getChildText("startDate");
                String currentEmpJobEvent = currentEmpJobElement.getChildText("eventCode");
                String currentEmpJobCustomString4 = currentEmpJobElement.getChildText("customString4");

                if (currentEmpJobEvent.equals("26") && effectiveDate.compareTo(sdf.parse(currentEmpJobStartDateStr)) == 0) {
                  Element termEmployee = new Element("Employee");
                  termEmployee.addContent((Element) personIdExternal.clone());
                  termEmployee.addContent(new Element("category").setText("R"));
                  termEmployee.addContent(new Element("effectiveDate").setText(sdf.format(effectiveDate)));
                  termEmployee.addContent(new Element("EmpJob").addContent(new Element("customString4").setText(currentEmpJobCustomString4)));
                  os = new ByteArrayInputStream(xmlOutputter.outputString(termEmployee).getBytes("UTF-8"));
                  System.out.println(xmlOutputter.outputString(termEmployee));
                } else {
                  if (employeeTreeMap.lowerEntry(effectiveDate) != null) {
                    Element previousEmployeeElement = employeeTreeMap.lowerEntry(effectiveDate).getValue();
                    Element previousEmpJobElement = previousEmployeeElement.getChild("EmpJob");
                    Element previousPerPersonalElement = previousEmployeeElement.getChild("PerPersonal");
                    Element previousPaymentInformationDetailV3Element = previousEmployeeElement.getChild("PaymentInformationDetailV3");

                    Element newEmployee = new Element("Employee");
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
                        if (!currentChildElementName.equals("startDate") && !currentChildElementName.equals("endDate") && !currentChildElementName.equals("seqNumber") && !currentChildElementName.equals("customString4") && !currentChildElementName.equals("employeeClassCode") && !currentChildElementName.equals("eventCode")) {
                          if (previousChildElementText.equals(currentChildElementText)) {
                            newEmpJobElement.removeChild(currentChildElementName);
                          } else {
                            isChanged = true;
                          }
                        }
                      }

                      if (isChanged) {
                        dateTreeSet.add(sdf.parse(currentEmpJobElement.getChildText("startDate")));
                        newEmployee.addContent(newEmpJobElement);
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
                        dateTreeSet.add(sdf.parse(currentPerPersonalElement.getChildText("startDate")));
                        newEmployee.addContent(newPerPersonalElement);
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
                        dateTreeSet.add(sdf.parse(currentPaymentInformationDetailV3Element.getChildText("startDate")));
                        newEmployee.addContent(newPaymentInformationDetailV3Element);
                      }
                    }

                    if (newEmployee != null && newEmployee.getContentSize() != 0) {
                      newEmployee.addContent((Element) personIdExternal.clone());
                      newEmployee.addContent(new Element("category").setText("EMC"));
                      newEmployee.addContent(new Element("effectiveDate").setText(sdf.format(dateTreeSet.last())));
                      if (newEmployee.getChild("EmpJob") == null) {
                        newEmployee.addContent(newEmpJobElement);
                      }
                      os = new ByteArrayInputStream(xmlOutputter.outputString(newEmployee).getBytes("UTF-8"));
                      System.out.println(xmlOutputter.outputString(newEmployee));
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}

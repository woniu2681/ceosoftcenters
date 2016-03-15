package boomi.hewitt;

import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import org.jaxen.jdom.JDOMXPath;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class PayComponentXML {
  public static void main(String[] args) throws Exception {
    SAXBuilder builder = new SAXBuilder();
    XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Date tempLastRunDate = sdf.parse("2010-01-01");
    Date tempCurrentRunDate = sdf.parse("2016-03-15");
    // Date tempLastRunDate = sdf.parse(ExecutionUtil.getDynamicProcessProperty("TempLastRunDate"));
    // Date tempCurrentRunDate = sdf.parse(ExecutionUtil.getDynamicProcessProperty("TempCurrentRunDate"));
    String[] payComponentCodeList = "STAT BON,BSP,COACH,CON,IN-CSTMR,ESIC,IN-FUNALLOW,GRAT,HRA,LTA,MGRALLOW,MEDDED,IN-MED,Perform,PETRO,PROV,IN-SKILL ALLOW".split(",");

    for (int i = 0; i < 1; i++) {
      Document doc = builder.build(new FileInputStream("src/boomi/hewitt/PerPerson_New.xml"));
      Element root = doc.getRootElement();

      if (payComponentCodeList != null && payComponentCodeList.length > 0) {
        Element empCompensation = new Element("EmpCompensation");

        for (String payComponentCode : payComponentCodeList) {
          JDOMXPath empPayCompRecurringXPath = new JDOMXPath("/PerPerson/History/EmpCompensation/EmpPayCompRecurring[payComponentCode='" + payComponentCode + "']");
          List<Element> empPayCompRecurringList = empPayCompRecurringXPath.selectNodes(doc);

          if (empPayCompRecurringList != null && !empPayCompRecurringList.isEmpty()) {
            TreeMap<Date, Element> empPayCompRecurringTreeMap = new TreeMap<Date, Element>();

            for (Element element : empPayCompRecurringList) {
              String startDateStr = element.getChildText("startDate");
              Date startDate = sdf.parse(startDateStr);
              empPayCompRecurringTreeMap.put(startDate, element);
            }

            if (empPayCompRecurringTreeMap != null && !empPayCompRecurringTreeMap.isEmpty()) {
              Element empPayCompRecurring = new Element("EmpPayCompRecurring");

              for (Date date : empPayCompRecurringTreeMap.keySet()) {
                Element current = empPayCompRecurringTreeMap.get(date);
                Element previous = empPayCompRecurringTreeMap.lowerEntry(date) != null ? empPayCompRecurringTreeMap.lowerEntry(date).getValue() : null;
                Element next = empPayCompRecurringTreeMap.higherEntry(date) != null ? empPayCompRecurringTreeMap.higherEntry(date).getValue() : null;

                if (date.compareTo(tempLastRunDate) >= 0 && date.compareTo(tempCurrentRunDate) < 0) {
                  if (previous == null && next == null) {
                    empPayCompRecurring = (Element) current.clone();
                  } else {
                    if (previous != null) {
                      String paycompvalueCurrent = current.getChildText("paycompvalue");
                      String paycompvaluePrevious = previous.getChildText("paycompvalue");

                      if (!paycompvalueCurrent.equals(paycompvaluePrevious)) {
                        empPayCompRecurring = (Element) current.clone();
                      }
                    }
                  }
                }
              }

              if (empPayCompRecurring != null && empPayCompRecurring.getContentSize() > 0) {
                empCompensation.addContent(empPayCompRecurring);
              }
            }
          }
        }

        if (empCompensation != null && empCompensation.getContentSize() > 0) {
          root.getChild("History").removeChildren("EmpCompensation");
          root.getChild("History").addContent(empCompensation);
          System.out.println(xmlOutputter.outputString(root));
        }
      }
    }
  }
}

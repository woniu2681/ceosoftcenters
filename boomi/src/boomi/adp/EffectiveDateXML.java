package boomi.adp;

import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;

import org.jaxen.jdom.JDOMXPath;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class EffectiveDateXML {
  public static void main(String[] args) throws Exception {
    SAXBuilder builder = new SAXBuilder();
    XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
    JDOMXPath effectiveDateXPath = new JDOMXPath("//EFFECTIVE_DATE");
    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMM");
    String inPeriodNumber = sdf2.format(new Date());
    
    Document doc = builder.build(new FileInputStream("src/boomi/adp/Employee0.xml"));
    Element employee = doc.getRootElement();
    employee.getChild("PIV_KY_REG").addContent(new Element("IN_PERIOD_NUMBER").setText(inPeriodNumber));
    
    List<Element> effectiveDateList = effectiveDateXPath.selectNodes(employee);
    if (effectiveDateList != null && !effectiveDateList.isEmpty()) {
      TreeSet<Date> effectiveDateTreeSet = new TreeSet<Date>();
      for (Element element : effectiveDateList) {
        if (element.getText() != null) {
          effectiveDateTreeSet.add(sdf1.parse(element.getText()));
        }
      }

      if (effectiveDateTreeSet != null && !effectiveDateTreeSet.isEmpty()) {
        Date forPeriod = effectiveDateTreeSet.last();
        String forPeriodNumber = sdf2.format(forPeriod);
        employee.getChild("PIV_KY_REG").addContent(new Element("FOR_PERIOD_NUMBER").setText(forPeriodNumber));
      }
    }
   
    System.out.println(xmlOutputter.outputString(employee));
  }
}

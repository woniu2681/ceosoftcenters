package boomi.hewitt;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import org.jaxen.jdom.JDOMXPath;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

public class PayComponentXML {
  public static void main(String[] args) throws Exception {
    SAXBuilder builder = new SAXBuilder();
    JDOMXPath badgeNumberXPath = new JDOMXPath("/PerPerson/Current/EmpJob/customString4");
    JDOMXPath compensationXPath = new JDOMXPath("/PerPerson/History/EmpCompensation/*");

    for (int i = 0; i < 1; i++) {
      Document doc = builder.build(new FileInputStream("src/boomi/hewitt/PerPerson_New.xml"));
      Element root = doc.getRootElement();
      String personIdExternal = root.getChildText("personIdExternal");

      Element badgeNumberElement = (Element) badgeNumberXPath.selectSingleNode(doc);
      String badgeNumber = badgeNumberElement != null ? badgeNumberElement.getText() : null;

      List<Element> list = compensationXPath.selectNodes(root);
      for (Element e : list) {
        if (!e.getName().equals("EmpPayCompRecurring")) {
          continue;
        }

        String payCode = e.getChildText("payComponentCode");
        String payName = e.getChildText("payComponentName");
        String payValue = e.getChildText("paycompvalue");
        String payDate = e.getChildText("startDate");

        StringBuffer payRecord = new StringBuffer();
        payRecord.append("EARN");
        payRecord.append("|");
        payRecord.append(("".equals(badgeNumber) || badgeNumber == null) ? personIdExternal : badgeNumber);
        payRecord.append("|");
        payRecord.append(payCode != null ? payCode : "");
        payRecord.append("|");
        payRecord.append(payName != null ? payName : "");
        payRecord.append("|");
        payRecord.append(payDate != null ? payDate : "");
        payRecord.append("|");
        payRecord.append(payValue != null ? payValue : "");

        InputStream os = new ByteArrayInputStream(payRecord.toString().getBytes("UTF-8"));
        System.out.println(payRecord.toString());
      }
    }
  }
}

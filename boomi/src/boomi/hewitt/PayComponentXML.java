package boomi.hewitt;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

public class PayComponentXML {
  public static void main(String[] args) throws Exception {
    SAXBuilder builder = new SAXBuilder();
    String PAY_CODE = "payComponentCode";
    String PAY_NAME = "payComponentName";
    String PAY_VALUE = "paycompvalue";

    for (int i = 0; i < 1; i++) {
      Document doc = builder.build(new FileInputStream("src/boomi/hewitt/EmpCompensation.xml"));
      Element record = doc.getRootElement();
      String personIdExternal = record.getChildText("PerPerson_personIdExternal");
      String empID = record.getChildText("EmpJob_customString4");
      String startDate = record.getChildText("EmpCompensation_startDate");

      Element payComponentElement = record.getChild("PayComponent");

      if (payComponentElement == null) {
        continue;
      }

      List<Element> list = payComponentElement.getChildren();

      for (Element e : list) {
        StringBuffer payRecord = new StringBuffer();
        Element payCodeEle = e.getChild(PAY_CODE);
        Element payNameEle = e.getChild(PAY_NAME);
        Element payValueEle = e.getChild(PAY_VALUE);

        String payCode = payCodeEle != null ? payCodeEle.getText() : "";
        String payName = payNameEle != null ? payNameEle.getText() : "";
        String payValue = payValueEle != null ? payValueEle.getText() : "";

        payRecord.append("EARN");
        payRecord.append("|");
        payRecord.append(("".equals(empID) || empID == null) ? personIdExternal : empID);
        payRecord.append("|");
        payRecord.append(payCode);
        payRecord.append("|");
        payRecord.append(payName);
        payRecord.append("|");
        payRecord.append(startDate);
        payRecord.append("|");
        payRecord.append(payValue);

        InputStream os = new ByteArrayInputStream(payRecord.toString().getBytes("UTF-8"));
        System.out.println(payRecord.toString());
      }
    }
  }
}

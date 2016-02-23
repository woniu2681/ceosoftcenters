package boomi.oanda;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import org.jaxen.jdom.JDOMXPath;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

public class BydResponse {
  public static void main(String[] args) throws Exception {
    SAXBuilder builder = new SAXBuilder();
    JDOMXPath MaximumLogItemSeverityCodeXPath = new JDOMXPath("//MaximumLogItemSeverityCode");
    JDOMXPath ItemXPath = new JDOMXPath("//Item");

    Document doc = builder.build(new FileInputStream("src/boomi/oanda/Response.xml"));
    if (doc != null) {
      Element maximumLogItemSeverityCodeElement = (Element) MaximumLogItemSeverityCodeXPath.selectSingleNode(doc);
      List<Element> itemElementList = ItemXPath.selectNodes(doc);

      String response = "";
      if (maximumLogItemSeverityCodeElement != null) {
        response += "MaximumLogItemSeverityCode: " + maximumLogItemSeverityCodeElement.getText();
      }

      if (itemElementList != null && !itemElementList.isEmpty()) {
        for (Element itemElement : itemElementList) {
          String severityCode = itemElement.getChildText("SeverityCode");
          String note = itemElement.getChildText("Note");

          response += System.lineSeparator();
          response += "SeverityCode: " + severityCode;
          response += System.lineSeparator();
          response += "Note: " + note;
        }
      }

      if (response != null && !response.isEmpty()) {
        InputStream os = new ByteArrayInputStream(response.getBytes("UTF-8"));
        System.out.println(response);
      }
    }
  }
}

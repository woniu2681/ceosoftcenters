package boomi.esl;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.TreeMap;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class RemoveExtraPerPerson {
  public static void main(String[] args) throws Exception {
    TreeMap<String, Element> map = new TreeMap<String, Element>();
    SAXBuilder builder = new SAXBuilder();
    XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());

    for (int i = 0; i <= 1; i++) {
      InputStream is = new FileInputStream("src/boomi/esl/test1.xml");
      Document doc = builder.build(is);
      Element root = doc.getRootElement();
      String key = root.getChildText("personIdExternal");
      if (!map.containsKey(key)) {
        map.put(key, root);
        InputStream os = new ByteArrayInputStream(xmlOutputter.outputString(root).getBytes("UTF-8"));
        System.out.println(xmlOutputter.outputString(root));
      }
    }
  }
}

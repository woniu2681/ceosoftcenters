package boomi.gdw;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.TreeMap;

import org.jaxen.jdom.JDOMXPath;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class EmployeeListXML1 {
	public static void main(String[] args) throws Exception {
		XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
		SAXBuilder builder = new SAXBuilder();
		Document doc = builder.build(new FileInputStream("src/boomi/gdw/EmployeeList.xml"));

		Element employee = doc.getRootElement();
		JDOMXPath jobSeqNumberXPath = new JDOMXPath("//EmpJob[seqNumber>1]");
		JDOMXPath compSeqNumberXPath = new JDOMXPath("//EmpPayCompRecurring[seqNumber>1]");

		List<Element> jobInfoList = jobSeqNumberXPath.selectNodes(employee);
		List<Element> compInfoList = compSeqNumberXPath.selectNodes(employee);

		TreeMap<String, Integer> jobInfoMap = new TreeMap<String, Integer>();
		TreeMap<String, Integer> compInfoMap = new TreeMap<String, Integer>();

		for (Element element : jobInfoList) {
			String startDate = element.getChildText("startDate");
			String seqNumberStr = element.getChildText("seqNumber");
			int seqNumber = Integer.parseInt(seqNumberStr);
			if (jobInfoMap.containsKey(startDate)) {
				if (seqNumber > jobInfoMap.get(startDate)) {
					jobInfoMap.put(startDate, seqNumber);
				}
			} else {
				jobInfoMap.put(startDate, seqNumber);
			}
		}

		for (Element element : compInfoList) {
			String startDate = element.getChildText("startDate");
			String seqNumberStr = element.getChildText("seqNumber");
			int seqNumber = Integer.parseInt(seqNumberStr);
			if (compInfoMap.containsKey(startDate)) {
				if (seqNumber > compInfoMap.get(startDate)) {
					compInfoMap.put(startDate, seqNumber);
				}
			} else {
				compInfoMap.put(startDate, seqNumber);
			}
		}

		for (String startDate : jobInfoMap.keySet()) {
			JDOMXPath jobInfoXPath = new JDOMXPath("//EmpJob[startDate='" + startDate + "' and seqNumber<" + jobInfoMap.get(startDate) + "]");
			List<Element> list = jobInfoXPath.selectNodes(employee);
			for (Element element : list) {
				element.getParent().removeContent(element);
			}
		}

		for (String startDate : compInfoMap.keySet()) {
			JDOMXPath compInfoXPath = new JDOMXPath("//EmpPayCompRecurring[startDate='" + startDate + "' and seqNumber<" + compInfoMap.get(startDate) + "]");
			List<Element> list = compInfoXPath.selectNodes(employee);
			for (Element element : list) {
				element.getParent().removeContent(element);
			}
		}

		InputStream os = new ByteArrayInputStream(xmlOutputter.outputString(employee).getBytes("UTF-8"));
		System.out.println(xmlOutputter.outputString(employee));
	}
}

package organstufftree;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.description.ParameterDesc;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class TestErpWebservice {

		public static void main(String[] args) {
//			System.out.println(readOrganWebServiceXml());
//			System.out.println(readUserWebServiceXml());
//			String result = readUserWebServiceXml();
			String result = readPa0001WebServiceXml();
			if(result==null){
				result = "";
			}
			try {
				analyDeptsXmlDept(result);
			} catch (ParserConfigurationException | SAXException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			FileWriter fw = null;
//			try {
//				fw = new FileWriter("d:/abc.txt");
//				fw.write(result);
//				System.out.println("ok");
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
		}
		
		//解析xml文件
		public static void analyDeptsXmlDept(String xml)
				throws ParserConfigurationException, SAXException, IOException {
			DocumentBuilderFactory factoy = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factoy.newDocumentBuilder();
			Document doc = builder.parse(new InputSource(new StringReader(xml)));
			Element root = doc.getDocumentElement();
			NodeList nodeList = root.getChildNodes();
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node node = nodeList.item(i);
				NodeList ChildNodeList = node.getChildNodes();
				for (int j = 0; j < ChildNodeList.getLength(); j++) {
					Node childNode = ChildNodeList.item(j);
					String nodeName = childNode.getNodeName();
					String nodeContent = childNode.getTextContent();
					System.out.print(nodeContent+"/");
//					if ("SNAME".equals(nodeName)) {
//						sysout
//					} else if ("Z00HRSSDW".equals(nodeName)) {
//						dept.setDeptType(nodeContent);
//					} else if ("Z00HRSSBM".equals(nodeName)) {
//						dept.setDeptName(nodeContent);
//					} else if ("Z00HRSSDWID".equals(nodeName)) {
//						dept.setParentId(nodeContent);
//					}
				}
				System.out.println("");
			}
		}
		
		public static String readUserWebServiceXml() {

			String uri = "http://10.1.82.15:8002/sap/bc/srt/rfc/sap/z42fm_typt_90/800/z42fm_typt_90/z42fm_typt_90";
//			String uri = "http://10.85.4.205:8000/sap/bc/srt/rfc/sap/z42fm_typt_90/300/z42fm_typt_90/z42fm_typt_90";
			String nameSpace = "urn:sap-com:document:sap:soap:functions:mc-style";
			String method = "Z42fmTypt90";
			String element = "Datatype";
			String type = "TYPT02";

			String resultXml = "";
			try {
				resultXml = ReadWebServiceXml(uri, nameSpace, method, element, type);
			} catch (AxisFault e) {
				e.printStackTrace();
			}

			return resultXml;
		}
		
		public static String readOrganWebServiceXml() {
			String uri = "http://10.1.82.15:8002/sap/bc/srt/rfc/sap/z42fm_typt_90/800/z42fm_typt_90/z42fm_typt_90";
//			String uri = "http://10.85.4.205:8000/sap/bc/srt/rfc/sap/z42fm_typt_90/300/z42fm_typt_90/z42fm_typt_90";
			String nameSpace = "urn:sap-com:document:sap:soap:functions:mc-style";
			String method = "Z42fmTypt90";
			String element = "Datatype";
			String type = "TYPT01";
			String resultXml = "";
			try {
				resultXml = ReadWebServiceXml(uri, nameSpace, method, element, type);
			} catch (AxisFault e) {
				e.getMessage();
			}

			return resultXml;
		}
		
		@SuppressWarnings({ "unchecked", "rawtypes" })
		public static String readPa0001WebServiceXml() {
			String wsdl = "http://10.1.82.14:8001/sap/bc/srt/rfc/sap/z42hr_fm_001/800/z42hr_fm_001/z42hr_fm_001";
			String namespace = "urn:sap-com:document:sap:soap:functions:mc-style";
			String userName = "z_webservice";
			String password = "web12345";
			String operationName = "Z42hrFm001";

			StringBuffer xml = new StringBuffer();
			xml.append("?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			xml.append("<Document>");
			xml.append("<ITEM>");
			xml.append("<YWTYPE>all</YWTYPE>");
			xml.append("<DATATYPE>0001</DATATYPE>");
			xml.append("</ITEM>");
			xml.append("</Document>");
			Map map = new HashMap();
			map.put("Input", xml.toString());
			String resultXml = null;
			try {
				resultXml = axisPortService(wsdl,userName,password, namespace,operationName, map);
			} catch (Exception e) {
				e.printStackTrace();
			}

			return resultXml;
		}
		
		private static String ReadWebServiceXml(String uri, String nameSpace,
				String method, String element, String value) throws AxisFault {
			ServiceClient client = null;
			EndpointReference targetEPR = new EndpointReference(uri);

			OMFactory fac = OMAbstractFactory.getOMFactory();
			OMElement payload = fac.createOMElement(method,
					fac.createOMNamespace(nameSpace, "urn"));
			OMElement ele = fac.createOMElement(element,
					fac.createOMNamespace("", ""));
			ele.setText(value);
			payload.addChild(ele);
			Options options = new Options();
			client = new ServiceClient();
			options.setTo(targetEPR);
			options.setTimeOutInMilliSeconds(5 * 60 * 1000);
			client.setOptions(options);
			OMElement result = client.sendReceive(payload);
			String readXML = result.getFirstElement().getText();
			client.cleanupTransport();
			return readXML;
		}
		
		@SuppressWarnings("rawtypes")
		private static String axisPortService(String wsdl, String userName,
				String password, String namespace, String operationName, Map paraMap) throws Exception {
			
			Service service = new Service();
			Call call = (Call)service.createCall();
			call.setTargetEndpointAddress(new URL(wsdl));
			call.setUsername(userName);
			call.setPassword(password);
			org.apache.axis.description.OperationDesc oper;

			oper = new org.apache.axis.description.OperationDesc();
			oper.setName(operationName);
			Iterator it = paraMap.keySet().iterator();
			int length = paraMap.size();
			String para[] = dynArray(length);
			int i = 0;
			while (it.hasNext()) {
				String key = it.next().toString();
				
				oper.addParameter(new ParameterDesc(new QName("", key),
						ParameterDesc.IN, new QName(
								"http://www.w3.org/2001/XMLSchema", "string"),
						java.lang.String.class, false, false));
				
				para[i] = paraMap.get(key).toString();
				i++;
			}
			oper.setReturnType(new QName("http://www.w3.org/2001/XMLSchema",
					"string"));
			oper.setReturnClass(java.lang.String.class);
			oper.setReturnQName(new QName("", "OUTPUT"));
			oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
			oper.setUse(org.apache.axis.constants.Use.LITERAL);

			call.setOperation(oper);
			call.setUseSOAPAction(true);
			call.setSOAPActionURI("");
			call.setEncodingStyle(null);
			call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR,
					Boolean.FALSE);
			call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS,
					Boolean.FALSE);
			call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
			call.setOperationName(new QName(namespace, operationName));
		
			String result = (String) call.invoke(para);
			return result;
		}
		
		private static String[] dynArray(int length) throws ClassNotFoundException {
			Class<?> classType = Class.forName("java.lang.String");
			Object array = Array.newInstance(classType, length);
			String para[] = (String[]) array;
			return para;
		}

}

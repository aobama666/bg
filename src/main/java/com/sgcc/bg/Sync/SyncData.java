package com.sgcc.bg.Sync;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Array;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axis.client.Call;
import org.apache.axis.description.ParameterDesc;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

/*import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;*/

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.sgcc.bg.common.ConfigUtils;
import com.sgcc.bg.common.DateUtil;
import com.sgcc.bg.common.Rtext;
import com.sgcc.bg.model.HRDept;
import com.sgcc.bg.model.HRUser;

@Service
public class SyncData {
	private final static Log logger = LogFactory.getLog(SyncData.class);
	
	public static List<HRDept> getWebServiceDeptList() {
		String result = RedServiceXml();
		List<HRDept> deptList = null;
		try {
			deptList = analyDeptsXmlDept(result);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return deptList;
	}
	
	//ConfigUtils.getConfig()根据key值，获取各个参数的值，调用ServiceClient服务
	public static String RedServiceXml(){
		
		String resultXml = "";
		String uri = ConfigUtils.getConfig("axis2_syncOrgan_serverUri");//uri地址
		String nameSpace = ConfigUtils.getConfig("axis2_syncOrgan_server_Namespace");//
		String method = ConfigUtils.getConfig("axis2_syncOrgan_server_method");//
		String element = ConfigUtils.getConfig("axis2_syncOrgan_server_element");
		String type = ConfigUtils.getConfig("axis2_syncOrgan_server_Type");
		try {
			resultXml = ReadWebServiceXml(uri,nameSpace,method,element,type);
		}catch(AxisFault e){
			e.printStackTrace();
		}
		return resultXml;	
	}
	//ServiceClient根据uri地址等参数读取xml文件，并返回
	public static String ReadWebServiceXml(String uri,String nameSpace,String method,String element,String value) throws AxisFault{
		ServiceClient client = null;
		EndpointReference targetEPR = new EndpointReference(uri);
		OMFactory fac = OMAbstractFactory.getOMFactory();
		OMElement payload = fac.createOMElement(method, fac.createOMNamespace(
				nameSpace, "urn"));
		OMElement ele = fac.createOMElement(element, fac.createOMNamespace("",
				""));
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
	//解析xml文件
	public static List<HRDept> analyDeptsXmlDept(String xml)
			throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory factoy = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factoy.newDocumentBuilder();
		Document doc = builder.parse(new InputSource(new StringReader(xml)));
		Element root = doc.getDocumentElement();
		NodeList nodeList = root.getChildNodes();
		List<HRDept> listDept = new ArrayList<HRDept>();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			NodeList ChildNodeList = node.getChildNodes();
			HRDept dept = new HRDept();
			for (int j = 0; j < ChildNodeList.getLength(); j++) {
				Node childNode = ChildNodeList.item(j);
				String nodeName = childNode.getNodeName();
				String nodeContent = childNode.getTextContent();
				if ("HR_ID".equals(nodeName)) {
					dept.setDeptId(nodeContent);
				} else if ("HR_TYPE".equals(nodeName)) {
					dept.setDeptType(nodeContent);
				} else if ("HR_TEXT".equals(nodeName)) {
					dept.setDeptName(nodeContent);
				} else if ("P_HR_ID".equals(nodeName)) {
					dept.setParentId(nodeContent);
				}
			}
			listDept.add(dept);
		}
		return listDept;
	}
	public static Object saveDept() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public static List<HRUser> getWebServiceUserList() {
		long start = System.currentTimeMillis();
		String result = readUserWebServiceXml();
		long end = System.currentTimeMillis();
		logger.info("请求Erp耗时"+(end-start)+"ms");
		List<HRUser> userList = null;
		try { 
			userList = analyUserXml(result);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return userList;
	}
	
	public static String readUserWebServiceXml() {

		String uri = ConfigUtils.getConfig("axis2_syncUser_serverUri");
		String nameSpace = ConfigUtils.getConfig("axis2_syncUser_server_Namespace");
		String method = ConfigUtils.getConfig("axis2_syncUser_server_method");
		String element = ConfigUtils.getConfig("axis2_syncUser_server_element");
		String type = ConfigUtils.getConfig("axis2_syncUser_server_Type");
		/*String uri = "http://10.1.82.15:8002/sap/bc/srt/rfc/sap/z42fm_typt_90/800/z42fm_typt_90/z42fm_typt_90";
		String nameSpace = "urn:sap-com:document:sap:soap:functions:mc-style";
		String method = "Z42fmTypt90";
		String element = "Datatype";
		String type = "TYPT02";*/

		String resultXml = "";
		try {
			resultXml = ReadWebServiceXml(uri, nameSpace, method, element, type);
		} catch (AxisFault e) {
			e.printStackTrace();
		}

		return resultXml;
	}
	
	//读取人员xml
	public static String RedUserServiceXml(){
		String resultXml = "";
		String uri = ConfigUtils.getConfig("axis2_syncUser_serverUri");//uri地址
		String nameSpace = ConfigUtils.getConfig("axis2_syncUser_server_Namespace");//
		String method = ConfigUtils.getConfig("axis2_syncUser_server_method");//
		String element = ConfigUtils.getConfig("axis2_syncUser_server_element");
		String type = ConfigUtils.getConfig("axis2_syncUser_server_Type");
		try {
			resultXml = ReadWebServiceXml(uri,nameSpace,method,element,type);
		}catch(AxisFault e){
			e.printStackTrace();
		}
		return resultXml;	
	}
	
	//解析人员xml文件
	public static List<HRUser> analyUserXml(String xml)
			throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory factoy = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factoy.newDocumentBuilder();
		Document doc = builder.parse(new InputSource(new StringReader(xml)));
		Element root = doc.getDocumentElement();
		NodeList nodeList = root.getChildNodes();
		List<HRUser> listUser = new ArrayList<HRUser>();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			NodeList ChildNodeList = node.getChildNodes();
			HRUser user = new HRUser();
			for (int j = 0; j < ChildNodeList.getLength(); j++) {
				Node childNode = ChildNodeList.item(j);
				String nodeName = childNode.getNodeName();
				String nodeContent = childNode.getTextContent();
				if ("USER_ID".equals(nodeName)) {
					user.setHrCode(nodeContent);
				} else if ("USER_CODE".equals(nodeName)) {
					user.setUserName(nodeContent);
				} else if ("USER_NAME".equals(nodeName)) {
					user.setUserAlias(nodeContent);
				} else if ("USER_STATUS".equals(nodeName)) {
					user.setUserStatus(nodeContent);
				} else if ("USER_SEX".equals(nodeName)) {
					user.setUserSex(nodeContent);
				} else if ("USER_BIRTHDATE".equals(nodeName)) {
					user.setBirthDate(nodeContent);
				} else if ("USER_JOBDATE".equals(nodeName)) {
					user.setJobdate(nodeContent);
				} else if ("USER_LAB_CODE".equals(nodeName)) {
					user.setUserDeptCode(nodeContent);
				} else if ("USER_LAB_NAME".equals(nodeName)) {
						user.setUserDeptName(nodeContent);
					} else if ("USER_POST_CODE".equals(nodeName)) {
						user.setUserPostCode(nodeContent);
					} else if ("USER_POST_NAME".equals(nodeName)) {
						user.setUserPostName(nodeContent);
					} else if ("USER_STATION".equals(nodeName)) {
						user.setUserStation(nodeContent);
					} else if ("USER_MAJOR".equals(nodeName)) {
						user.setUserMajor(nodeContent);
					} else if ("USER_EDU".equals(nodeName)) {
						user.setUserEdu(nodeContent);
					} else if ("USER_CARD".equals(nodeName)) {
						user.setUserCard(nodeContent);
					} else if ("USER_TEL".equals(nodeName)) {
						user.setUserTel(nodeContent);
					} else if ("USER_PHONE".equals(nodeName)) {
						user.setUserPhone(nodeContent);
					} else if ("USER_EMAIL".equals(nodeName)) {
						user.setUserEmail(nodeContent);
					} else if ("USER_POSTCODE".equals(nodeName)) {
						user.setUserPostalCode(nodeContent);
					} else if ("USER_ADDR".equals(nodeName)) {
						user.setUserAddr(nodeContent);
					} else if ("USER_TAX".equals(nodeName)) {
						user.setUserTax(nodeContent);
					} else if ("USER_NATION".equals(nodeName)) {
						user.setUserNation(nodeContent);
					} else if ("USER_COUNTRY".equals(nodeName)) {
						user.setUserCountry(nodeContent);
					}else if ("USER_YKTKH".equals(nodeName)) {
						user.setEmployeeNumber(nodeContent);
					}
				}
				listUser.add(user);
			}
			return listUser;
		}
		
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static String readPa0001WebServiceXml() {
		String wsdl = ConfigUtils.getConfig("axis2_syncUserDeptRelation_Wsdl");;
		String namespace = ConfigUtils.getConfig("axis2_syncUserDeptRelation_Namespace");;
		String userName = ConfigUtils.getConfig("axis2_syncUserDeptRelation_UserName");;
		String password = ConfigUtils.getConfig("axis2_syncUserDeptRelation_Password");;
		String operationName = ConfigUtils.getConfig("axis2_syncUserDeptRelation_OperationName");;

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
	
	@SuppressWarnings("rawtypes")
	private static  String axisPortService(String wsdl, String userName,
			String password, String namespace, String operationName, Map paraMap) throws Exception {
		
		org.apache.axis.client.Service service = new org.apache.axis.client.Service();
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
	
	//解析人员组织关系xml文件
	public static List<Map<String,Object>> analyUserDeptRelationXml(String xml)
			throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory factoy = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factoy.newDocumentBuilder();
		Document doc = builder.parse(new InputSource(new StringReader(xml)));
		Element root = doc.getDocumentElement();
		NodeList nodeList = root.getElementsByTagName("ITEM");
		List<Map<String,Object>> listRelation = new ArrayList<Map<String,Object>>();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			NodeList ChildNodeList = node.getChildNodes();
			Map<String,Object> relation = new HashMap<>();
			for (int j = 0; j < ChildNodeList.getLength(); j++) {
				Node childNode = ChildNodeList.item(j);
				String nodeName = childNode.getNodeName();
				Object nodeContent = childNode.getTextContent();
				if ("BEGDA".equals(nodeName) || "ENDDA".equals(nodeName) || "AEDTM".equals(nodeName) || "Z00HRYWRQ".equals(nodeName) || "SJC".equals(nodeName)) {
					SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
					try {
						nodeContent = dateFormat.parse((String)nodeContent);
					} catch (ParseException e) {
						nodeContent=null;
					}
				}else if ("PLANS".equals(nodeName)) {
					try {
						nodeContent=Integer.parseInt((String) nodeContent);
					} catch (Exception e) {
						nodeContent=null;
					}
				}
				relation.put(nodeName, nodeContent);
			}
			listRelation.add(relation);
		}
		return listRelation;
	}
	
	public static List<Map<String,Object>> getWebServiceUserDeptRelationList() {
		long start = System.currentTimeMillis();
		String result = readPa0001WebServiceXml();
		long end = System.currentTimeMillis();
		logger.info("请求Erp耗时"+(end-start)+"ms");
		List<Map<String,Object>> userDeptRelationList = null;
		try { 
			userDeptRelationList = analyUserDeptRelationXml(result);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return userDeptRelationList;
	}
	
}

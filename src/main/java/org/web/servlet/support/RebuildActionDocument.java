package org.web.servlet.support;

import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import tool.mastery.dom.ReadXmlUtils;
import tool.mastery.dom.RebuildDocument;

/**
 * 对action.xml进行重组，因为可能包含include标签
 * @author mastery
 * @Time 2015-3-11 下午6:59:31
 * 
 */
public class RebuildActionDocument implements RebuildDocument {

	@Override
	public Element rebuildDocument(Element rootElement)
			throws DocumentException {
		Element retElement = DF.createElement("actions");
		this.createActionElement(retElement , rootElement);
		return retElement;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void createActionElement(Element retElement , Element rootElement) throws DocumentException {
		List list = rootElement.elements();
		for (int i = 0; i < list.size(); i++) {
			Element element = (Element) list.get(i);
			
			if (element.getName().equalsIgnoreCase("action")) {
				Element newElement = createNode(retElement, element , "action");
				List<Element> results = element.elements();
				for(Element result : results) {
					createNode(newElement, result , result.getName());
				}
			}else if (element.getName().equalsIgnoreCase("include")){
				Element rootElement1 = ReadXmlUtils
						.getRootElementByPath(element.attributeValue("file"));
				this.createActionElement(retElement , rootElement1);
			}else {
				createNode(retElement, element , element.getName());
			}
		}
	}

	@SuppressWarnings("unchecked")
	private Element createNode(Element retElement, Element element , String nodeName) {
		Element currentElement = DF.createElement(nodeName);
		List<Attribute> list1 = element.attributes();
		for(Attribute attribute : list1) {
			String name =  attribute.getName();
			currentElement.add(DF.createAttribute(currentElement, name,
					element.attributeValue(name)));
		}
		currentElement.add(DF.createText(element.getText()));
		retElement.add(currentElement);
		return currentElement;
	}

}

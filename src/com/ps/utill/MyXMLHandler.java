package com.ps.utill;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class MyXMLHandler extends DefaultHandler {

	Boolean currentElement = false;
	String currentValue = null;
	public static SitesList sitesList = null;

	public static SitesList getSitesList() {
		return sitesList;
	}

	public static void setSitesList(SitesList sitesList) {
		MyXMLHandler.sitesList = sitesList;
	}
	
	String type="";

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Called when tag starts ( ex:- <name>AndroidPeople</name> -- <name> )
	 */
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		currentElement = true;
		if (localName.equals("presence")) {
			if (attributes.getValue("type") != null) {
				 type = attributes.getValue("type");
				System.out.println("Type.." + type);
			} else if (attributes.getValue("id") != null) {
				String id = attributes.getValue("id");
				System.out.println("ID.." + id);
			}

		}
	}

	/**
	 * Called when tag closing ( ex:- <name>AndroidPeople</name> -- </name> )
	 */
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		currentElement = false;
		/** set value */
		if (localName.equalsIgnoreCase("presence"))
			sitesList.setName(currentValue);
		else if (localName.equalsIgnoreCase("website"))
			sitesList.setWebsite(currentValue);
	}

	/**
	 * Called to get tag characters ( ex:- <name>AndroidPeople</name> -- to get
	 * AndroidPeople Character )
	 */
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {

		if (currentElement) {
			currentValue = new String(ch, start, length);
			currentElement = false;
		}

	}

}

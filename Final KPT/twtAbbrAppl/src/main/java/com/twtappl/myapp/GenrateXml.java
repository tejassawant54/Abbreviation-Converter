package com.twtappl.myapp;

import java.util.ArrayList;
import java.util.List;

public class GenrateXml {

	public static void main(String[] args) {
		GenrateXml gXml = new GenrateXml();
		List<AbbrPojo> list = new ArrayList<AbbrPojo>();
		
		AbbrPojo abbr1 = new AbbrPojo();
		abbr1.setSrNo(1);
		abbr1.setAbbrText("lol");
		abbr1.setNonAbbrText("louds of lafter");
		
		
		AbbrPojo abbr2 = new AbbrPojo();
		abbr2.setSrNo(345);
		abbr2.setAbbrText("hru?");
		abbr2.setNonAbbrText("How are you?");
		
		list.add(abbr1);
		list.add(abbr2);
		
		System.out.println(gXml.convertToXML(list));
		
	}
	
	
	/**
	 * Method convertToXML - convert data fetched from DB to XML string, this XML string is
	 * used to show data in tabular format.
	 * @param  reqObj ArrayList<MOTS>
	 * @return String
	 */
	public String convertToXML(List<AbbrPojo> reqObj){

		final String xmlHeader = "<?xml version='1.0' encoding='utf-8'?>";
		StringBuffer xmlToReturn = new StringBuffer(xmlHeader);
		
		int totpages = (reqObj.size()/10);
		if(totpages == 0){totpages = 1;}
		xmlToReturn.append("<requestList>");
		xmlToReturn.append("<currentpage>1</currentpage>");
		xmlToReturn.append("<totalpages>");xmlToReturn.append(totpages);xmlToReturn.append("</totalpages>");
		xmlToReturn.append("<totalrecords>"); xmlToReturn.append(reqObj.size());xmlToReturn.append("</totalrecords>");
		xmlToReturn.append("<elements>");
		
		for(AbbrPojo requestVal:reqObj){
			xmlToReturn.append("<element>");
			
			xmlToReturn.append("<SrNo>"); 	
			xmlToReturn.append(TwtUtil.formatContentForGrid(""+requestVal.getSrNo()));		 	
			xmlToReturn.append("</SrNo>");
			
			xmlToReturn.append("<AbbrText>"); 	
			xmlToReturn.append(TwtUtil.formatContentForGrid(""+requestVal.getAbbrText()));		 	
			xmlToReturn.append("</AbbrText>");
			
			xmlToReturn.append("<NonAbbrText>"); 	
			xmlToReturn.append(TwtUtil.formatContentForGrid(requestVal.getNonAbbrText()));		 	
			xmlToReturn.append("</NonAbbrText>");
			
			xmlToReturn.append("</element>");
		}
		
		xmlToReturn.append("</elements>");
		xmlToReturn.append("</requestList>");
		
		return xmlToReturn.toString();
	}


}

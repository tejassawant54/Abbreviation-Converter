package com.twtappl.myapp;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.twtappl.myapp.abbrCnvtr.AbbrCntvr;
import com.twtappl.myapp.abbrCnvtr.WekaTesting;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		
		return "home";
	}
	
	@RequestMapping(value = "/abbrCnvtr", method = RequestMethod.GET)
	public String abbrCnvtr(HttpServletRequest request) {
		logger.info("Inside method abbrCnvtr");
		
		List<AbbrPojo> showDataList = null;
		try {
				String searchText = request.getParameter("searchText");
				if(searchText != null && !searchText.isEmpty()){
		
					showDataList = new AbbrCntvr().showResults(searchText);
					
					//String GridXML = "<?xml version='1.0' encoding='utf-8'?> <requestList><currentpage>1</currentpage><totalpages>1</totalpages><totalrecords>2</totalrecords><elements><element><SrNo>10</SrNo><AbbrText>123</AbbrText><NonAbbrText>UUID123</NonAbbrText></element><element><SrNo>10</SrNo><AbbrText>123</AbbrText><NonAbbrText>UUID123</NonAbbrText></element></elements></requestList>";
					
					String GridXML = new GenrateXml().convertToXML(showDataList);
					logger.info("Print XML String {}.", GridXML);
					request.setAttribute("GridXML", GridXML);
				}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "abbrCnvtr";
	}
	
	@RequestMapping(value = "/wekaStats", method = RequestMethod.GET)
	public String wekaStats(HttpServletRequest request) {
		logger.info("Inside method wekaStats");
		System.out.println(""+WekaTesting.hm);
		request.setAttribute("statMap", WekaTesting.hm);
		return "wekaStats";
	}
}

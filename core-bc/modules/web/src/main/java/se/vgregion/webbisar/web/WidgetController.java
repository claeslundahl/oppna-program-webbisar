/**
 * Copyright 2010 Västra Götalandsregionen
 *
 *   This library is free software; you can redistribute it and/or modify
 *   it under the terms of version 2.1 of the GNU Lesser General Public
 *   License as published by the Free Software Foundation.
 *
 *   This library is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public
 *   License along with this library; if not, write to the
 *   Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 *   Boston, MA 02111-1307  USA
 *
 */

package se.vgregion.webbisar.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import se.vgregion.webbisar.svc.Configuration;
import se.vgregion.webbisar.svc.WebbisService;
import se.vgregion.webbisar.types.Hospital;
import se.vgregion.webbisar.types.Webbis;

public class WidgetController extends AbstractController {

	private WebbisService webbisService;
	private Configuration cfg;
	
	@Autowired
	public void setWebbisService(WebbisService webbisService) {
		this.webbisService = webbisService;
	}

	@Autowired
	public void setConfiguration(Configuration configuration) {
		this.cfg = configuration;
	}

	public ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String callback = request.getParameter("callback");
		String h = request.getParameter("loc");
		String num = request.getParameter("num");
		String id = request.getParameter("id");
		
		List<WebbisBean> webbisList = new ArrayList<WebbisBean>();
		if(id != null) {
			Webbis w = webbisService.getById(parseId(id));
			webbisList.add(new WebbisBean(cfg.getImageBaseUrl(), w, webbisList));
		} else {
			int maxResult = parseNum(num);
			Hospital hospital = parseHospital(h);
			
			List<Webbis> webbisar;
			if(hospital != null) {
				webbisar = webbisService.getLatestWebbisar(hospital, maxResult);
			} else {
				webbisar = webbisService.getLatestWebbisar(maxResult);
			}
	
			for(Webbis w : webbisar){
				webbisList.add(new WebbisBean(cfg.getImageBaseUrl(), w, webbisList));
			}
		}
		
		
		ModelAndView mav = new ModelAndView("badge");
		mav.addObject("webbisar", webbisList);
		mav.addObject("callback", callback);
		return mav;
	}

	private Long parseId(String id) {
		try {
			return Long.parseLong(id);
		} catch (NumberFormatException e) {
			return 0L;
		}
	}

	private Hospital parseHospital(String h) {
		try {
			return Hospital.valueOf(h);
		} catch (Exception e) {
			// Ignore just return null.
		}
		return null;
	}

	private int parseNum(String num) {
		if(num == null) return 1;
		try {
			int res = Integer.parseInt(num);
			return res < 100 ? res : 100;
		} catch (NumberFormatException e){
			return 1;
		}
	}
	
	
}
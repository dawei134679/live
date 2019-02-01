package com.mpig.api.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@Scope("prototype")
@RequestMapping("/")
public class RootController extends BaseController {
	private final static String response = "<cross-domain-policy><site-control permitted-cross-domain-policies=\"all\"/><allow-access-from domain=\"*\"/><allow-http-request-headers-from domain=\"*\" headers=\"*\"/></cross-domain-policy>";

	@RequestMapping(value = "/crossdomain.xml", method = RequestMethod.GET)
	public void crossDomain(HttpServletRequest req, HttpServletResponse resp) {
		try {
			resp.setHeader("Content-type", "text/xml;charset=UTF-8");
			resp.getOutputStream().write(response.getBytes("UTF-8"));
			return;
		} catch (Exception e) {
		}
	}
}

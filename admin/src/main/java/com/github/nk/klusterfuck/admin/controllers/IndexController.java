package com.github.nk.klusterfuck.admin.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.view.RedirectView;

/**
 * Created by nk on 3/6/17.
 */
@Controller
@RequestMapping({"", "/"})
public class IndexController {

	@RequestMapping(method = RequestMethod.GET)
	public RedirectView welcomePage() {
		RedirectView rv = new RedirectView("index.html", true);
		return rv;
	}
}
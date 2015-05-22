package com.ironside.weixin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ironside.weixin.request.DefaultPostProcess;

@Controller
@RequestMapping("/weixin")
public class WeixinController {
	
	private DefaultPostProcess postProcess;
	
	public WeixinController() {
		MyPostProcessor processor = new MyPostProcessor();
		this.postProcess = new DefaultPostProcess();
		this.postProcess.setProcessor(processor);
	}

	@RequestMapping(method = RequestMethod.POST, produces = "text/html;charset=utf-8")
	@ResponseBody
	public String message(@RequestBody String postData) {
		String responseData =  postProcess.process(postData);
		return responseData;
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public String listMess(@RequestParam String name) {
		return name; 
	}

}

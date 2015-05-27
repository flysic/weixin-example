package com.ironside.weixin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ironside.weixin.request.DefaultGetRequest;
import com.ironside.weixin.request.DefaultPostProcess;

@Controller
@RequestMapping("/weixin")
public class WeixinController {
	
	private DefaultPostProcess postProcess;
	private DefaultGetRequest getRequest;
	private final String token = "ironside";
	
	
	public WeixinController() {
		MyPostProcessor processor = new MyPostProcessor();
		this.postProcess = new DefaultPostProcess();
		this.postProcess.setProcessor(processor);
		this.getRequest = new DefaultGetRequest();
	}

	/**
	 * 处理POST方式推送给微信公众账号的消息处理
	 * @param postData 具体实现消息解析
	 * @return 响应信息
	 */
	@RequestMapping(method = RequestMethod.POST, produces = "text/html;charset=utf-8")
	@ResponseBody
	public String post(@RequestBody String postData) {
		String responseData =  postProcess.process(postData);
		return responseData;
	}
	
	/**
	 * 在开发者首次提交验证申请时，微信服务器将发送GET请求到填写的URL上， 并且带上四个参数
	 * (signature、timestamp、nonce、echostr)，开发者通过对签名(即signature)的效验，来判断此条消息的真实性
	 * @param signatur 微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数 
	 * @param timestamp 时间戳 
	 * @param nonce 随机数 
	 * @param echostr 随机字符串 
	 * @return  若确认此次GET请求来自微信服务器，请原样返回echostr参数内容，则接入生效，成为开发者成功，否则接入失败 
	 */
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public String validate(@RequestParam String signature, @RequestParam String timestamp, 
	@RequestParam String nonce, @RequestParam String echostr) {
		return getRequest.process(token, signature, timestamp, nonce, echostr);
	}

}

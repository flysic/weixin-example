package org.flysic.example.weixin.controller;

import org.apache.commons.lang.StringUtils;
import org.flysic.commons.weixin.passive.PostProcessorAdapter;
import org.flysic.commons.weixin.passive.request.entity.EventMenuClickEntity;
import org.flysic.commons.weixin.passive.request.entity.TextEntity;
import org.flysic.commons.weixin.passive.request.entity.VideoEntity;
import org.flysic.commons.weixin.passive.response.entity.AbstractBaseResponse;
import org.flysic.commons.weixin.passive.response.entity.ImageResponse;
import org.flysic.commons.weixin.passive.response.entity.TextResponse;
import org.flysic.commons.weixin.passive.response.entity.TransferCustomerResponse;
import org.flysic.example.weixin.service.MyMessage;
import org.flysic.example.weixin.service.MyServiceImpl;

/**
 * 测试用的POST处理器
 * @author 雪庭(flysic) QQ: 119238122 微信: flysic github: https://github.com/flysic
 * @sine 1.0 at 2015年5月8日
 */
public class MyPostProcessor extends PostProcessorAdapter {
	
	@Override
	public String postProcessText(TextEntity entity) {
		// 取得文本内容
		String flower = entity.getContent();
		// 如果是数字，从数据库得到花的介绍
		if (StringUtils.isNumeric(flower)) {
			return doFlower(entity);
		}
		// 如果是文本，发送到客服
		return doTransferCustomer(entity);
	}
	
	// 发送到客服
	private String doTransferCustomer(TextEntity entity) {
		TransferCustomerResponse transferCustomerResponse = this.responseManager.getTransferCustomerResponse(entity);
		String result = this.responseManager.responseToXml(transferCustomerResponse);
		return result;
	}

	// 从数据库得到花的介绍
	private String doFlower(TextEntity entity) {
		String flowerInfo;
		String flower = entity.getContent();
		// 数据库查找
		flowerInfo = MyServiceImpl.findFlower(flower);
		// 以文本效应方式返回花的介绍
		TextResponse textResponse = this.responseManager.getTextResponse(entity);
		textResponse.setContent(flowerInfo);
		String result = this.responseManager.responseToXml(textResponse);
		return result;
	}

	@Override
	public String postProcessEventMenuClick(EventMenuClickEntity entity) {
		// 取得点击菜单的事件KEY值
		String eventKey = entity.getEventKey();
		AbstractBaseResponse response = null;
		String result;
		switch(eventKey) {
		case MyMessage.CLICK_TEXT:
			response = this.responseManager.getTextResponse(entity);
			// 可在这里进行处理
			break;
		case MyMessage.CLICK_IMAGE:
			response = this.responseManager.getImageResponse(entity);
			((ImageResponse)response).getImage().setMediaId("f_79hUmXIreErst9BLJDe2i0HZUKPQF9kPs9VOaRm_JCbM4gsc04VuIdRRdb82dg");
			// 可在这里进行处理
			break;
		case MyMessage.CLICK_TRASFER_CUSTOMER:
			response = this.responseManager.getTransferCustomerResponse(entity);
			((TransferCustomerResponse)response).setTransInfo(null);
			break;
		default:
			
		}
		result = this.responseManager.responseToXml(response);
		return result;
	}
	
	@Override
	public String postProcessVideo(VideoEntity entity) {
		TextResponse response = this.responseManager.getTextResponse(entity);
		return this.responseManager.responseToXml(response);
	}

}

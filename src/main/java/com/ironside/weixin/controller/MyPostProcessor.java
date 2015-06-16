package com.ironside.weixin.controller;

import com.ironside.weixin.passive.PostProcessorAdapter;
import com.ironside.weixin.passive.request.entity.EventMenuClickEntity;
import com.ironside.weixin.passive.request.entity.TextEntity;
import com.ironside.weixin.passive.request.entity.VideoEntity;
import com.ironside.weixin.passive.response.entity.AbstractBaseResponse;
import com.ironside.weixin.passive.response.entity.ImageResponse;
import com.ironside.weixin.passive.response.entity.TextResponse;
import com.ironside.weixin.passive.response.entity.VideoResponse;
import com.ironside.weixin.service.MyMessage;
import com.ironside.weixin.service.MyServiceImpl;

/**
 * 测试用的POST处理器
 * @author 雪庭
 * @sine 1.0 at 2015年5月8日
 */
public class MyPostProcessor extends PostProcessorAdapter {
	
	@Override
	public String postProcessText(TextEntity entity) {
		// 取得文本内容(花名)
		String flower = entity.getContent();
		// 花的介绍
		String flowerInfo;
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
		case MyMessage.CLICK_VIDEO:
			response = this.responseManager.getVideoResponse(entity);
			((VideoResponse)response).getVideo().setDescription("视频");
			((VideoResponse)response).getVideo().setMediaId("FapYmcWHAzTMS8Pk6k-kW8DY8LU-O7di8p5jtPP5xBuCEzJ13pzRfjkFrUHKBaP-");
			((VideoResponse)response).getVideo().setTitle("开发者");
			// 可在这里进行处理
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

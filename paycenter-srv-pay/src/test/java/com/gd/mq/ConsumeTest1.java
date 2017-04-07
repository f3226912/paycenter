package com.gd.mq;

import java.util.Properties;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Consumer;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.PropertyKeyConst;

public class ConsumeTest1 {

	public static void main(String[] args) {
		Properties properties = new Properties();
		properties.put(PropertyKeyConst.ConsumerId, "CID_dev_nsy_order_pay_created_a");// 您在MQ控制台创建的Consumer
															// ID
		properties.put(PropertyKeyConst.AccessKey, "LTAIPLNUr0D3XxQk");// 鉴权用AccessKey，在阿里云服务器管理控制台创建
		properties.put(PropertyKeyConst.SecretKey, "E3d0QQoIUkInV9vXGJkE47qUvBJTnC");// 鉴权用SecretKey，在阿里云服务器管理控制台创建
		Consumer consumer = ONSFactory.createConsumer(properties);
		consumer.subscribe("dev_nsy_order_pay_created_a", "*", new MessageListener() {
			public Action consume(Message message, ConsumeContext context) {
				System.out.println("Receive: " + message);
				return Action.CommitMessage;
			}
		});
	
		consumer.start();
		System.out.println("Consumer Started");
	}
}

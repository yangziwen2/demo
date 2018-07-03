package com.example.demo.controller;

import javax.annotation.PostConstruct;
import javax.jms.Topic;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dal.dao.RecordDao;
import com.example.demo.dal.model.CodeCommit;

@RestController
@RequestMapping("/test")
public class TestController {

	@Autowired
	private RecordDao recordDao;

	@Autowired
	@Qualifier("testTopic")
	private Topic testTopic;

	@Autowired
	private JmsTemplate jmsTemplate;

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private CuratorFramework curator;

	@PostConstruct
	public void init() {
		try {
			curator.create().withMode(CreateMode.EPHEMERAL).forPath("/test", "data".getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@GetMapping("/mybatis")
	public Object testMybatis() {
		return recordDao.getList();
	}

	@GetMapping("/mongo")
	public Object testMongo() {
		Criteria criteria = Criteria.where("author").is("yangziwen@baidu.com")
				.and("repo_name").is("baidu/icode/code-office");
		return mongoTemplate.find(Query.query(criteria).limit(10), CodeCommit.class);
	}

	@GetMapping("/activemq/send")
	public Object sendToActivemq() {
		jmsTemplate.convertAndSend(testTopic, "test activemq");
		return "success";
	}

	@JmsListener(destination = "test.topic", containerFactory = "jmsListenerContainerTopic")
	public void receiveFromActivemq(String message) {
		System.out.println("ActiveMQ Topic Received: " + message);
	}

	@GetMapping("/kafka/send")
	public Object sendToKafka() {
		kafkaTemplate.send("test", "test kafka");
		return "success";
	}

	@KafkaListener(topics = {"test"})
	public void receiveFromKafka(String message) {
		System.out.println("Kafka Topic Received: " + message);
	}

	@GetMapping("/zookeeper")
	public Object testZookeeper() throws Exception {
		return curator.getData().forPath("/test");
	}

}

package com.kevin.sm.test;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kevin.sm.model.User;
import com.kevin.sm.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
// = extends SpringJUnit4ClassRunner
@ContextConfiguration(locations = { "classpath:spring.xml", "classpath:spring-mybatis.xml" })
public class TestMybatis {

	private static final Logger	logger	= LogManager.getLogger(TestMybatis.class);

	@Autowired
	private UserService		muserService;

	@Test
	public void test1() {

		List<User> list = muserService.getAll();
		logger.info("test");
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
		logger.info(gson.toJson(list));
	}

	@Test
	public void test2() {

		User muser = new User();
		muser.setId("0000");
		muser.setName("aaaa");
		muser.setAge(28);
		muser.setAddress("ABCD");
		int i = muserService.insert(muser);
		logger.info("add {}", i);
	}

	@Test
	public void test3() {

		User muser = new User();
		muser.setId("0000");
		muser.setName("bbbb");
		muser.setAge(32);
		muser.setAddress("ABCD");
		int i = muserService.update(muser);
		logger.info("update {}", i);
	}

	@Test
	public void test4() {

		User muser = new User();
		muser.setId("0000");
		muser.setName("bbbb");
		muser.setAge(32);
		muser.setAddress("ABCD");
		int i = muserService.delete("0000");
		logger.info("delete {}", i);
	}

}

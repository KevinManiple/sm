package com.kevin.sm.controller;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.kevin.sm.model.User;
import com.kevin.sm.service.UserService;

@Controller
@RequestMapping(value = "user", produces = MediaType.ALL_VALUE)
public class UserController {

	@Autowired
	private UserService	service;

	@RequestMapping(value = "listUser", method = RequestMethod.GET)
	public String listUser(HttpServletRequest request) {

		List<User> list = service.getAll();
		request.setAttribute("userlist", list);
		return "listUser";
	}

	@RequestMapping(value = "addUser", method = RequestMethod.POST)
	public String addUser(User muser) {

		String id = UUID.randomUUID().toString();
		muser.setId(id);
		service.insert(muser);
		return "redirect:/user/listUser.do";
	}

	@RequestMapping(value = "deleteUser", method = RequestMethod.GET)
	public String deleteUser(String id) {

		service.delete(id);
		return "redirect:/user/listUser.do";
	}

	@RequestMapping(value = "updateUserUI", method = RequestMethod.GET)
	public String updateUserUI(String id, HttpServletRequest request) {

		User muser = service.selectByPrimaryKey(id);
		request.setAttribute("user", muser);
		return "updateUser";
	}

	@RequestMapping(value = "updateUser", method = RequestMethod.POST)
	public String updateUser(User muser) {

		service.update(muser);
		return "redirect:/user/listUser.do";
	}
}

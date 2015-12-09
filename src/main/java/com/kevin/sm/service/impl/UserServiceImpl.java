package com.kevin.sm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kevin.sm.dao.UserDao;
import com.kevin.sm.model.User;
import com.kevin.sm.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao	dao;

	@Override
	public List<User> getAll() {
		
		return dao.getAll();
	}

	@Override
	public int insert(User muser) {

		return dao.insert(muser);
	}

	@Override
	public int update(User muser) {

		return dao.updateByPrimaryKey(muser);
	}

	@Override
	public int delete(String id) {

		return dao.deleteByPrimaryKey(id);
	}

	@Override
	public User selectByPrimaryKey(String id) {

		return dao.selectByPrimaryKey(id);
	}

}

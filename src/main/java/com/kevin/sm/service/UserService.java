package com.kevin.sm.service;

import java.util.List;

import com.kevin.sm.model.User;

public interface UserService {

	List<User> getAll();
	
	User selectByPrimaryKey(String id);
	
    int insert(User muser);
    
    int update(User muser);
    
    int delete(String id);
}

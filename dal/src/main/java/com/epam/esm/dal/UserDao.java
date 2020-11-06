package com.epam.esm.dal;

import java.util.List;

import com.epam.esm.entity.User;

public interface UserDao {
	
	User addUser(User user);
	
	List<User> findUsers();
	
	User findUser(long id);
	
	int deleteUser(long id);
	
	int updateUser(long id, User user);

}

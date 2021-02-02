package com.epam.esm.dal;

import com.epam.esm.entity.User;

public interface UserDao {
	
	User addUser(User user);
	
	User findUser(long id);

}

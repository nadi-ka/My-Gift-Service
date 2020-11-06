package com.epam.esm.dal;

import com.epam.esm.entity.User;

public interface OrderDao {
	
	User findOrdersByUserId(long userId);

}

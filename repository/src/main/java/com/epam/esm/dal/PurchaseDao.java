package com.epam.esm.dal;

import java.util.List;

import com.epam.esm.entity.Order;

public interface OrderDao {
	
	List<Order> findOrdersByUserId(long userId);

}

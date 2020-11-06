package com.epam.esm.service;

import java.util.List;

import com.epam.esm.dto.OrderDTO;

public interface OrderService {
	
	List<OrderDTO> getOrdersByUserId(long userId);

}

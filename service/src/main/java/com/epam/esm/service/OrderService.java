package com.epam.esm.service;

import com.epam.esm.dto.UserDTO;

public interface OrderService {
	
	UserDTO getUserOrdersById(long userId);

}

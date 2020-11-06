package com.epam.esm.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.epam.esm.dal.OrderDao;
import com.epam.esm.dto.UserDTO;
import com.epam.esm.entity.User;
import com.epam.esm.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService{
	
	@Autowired
	private OrderDao orderDao;
	
	@Autowired
	private ModelMapper modelMapper;
	
	public void setModelMapper(ModelMapper mapper) {
		this.modelMapper = mapper;
	}

	@Override
	public UserDTO getUserOrdersById(long userId) {
		
		User user = orderDao.findOrdersByUserId(userId);
		
		if (user == null) {
			return null;
		}
		
		return convertToDto(user);
	}
	
	private UserDTO convertToDto(User user) {

		UserDTO userDTO = modelMapper.map(user, UserDTO.class);

		return userDTO;
	}

	private User convertToEntity(UserDTO userDTO) {

		User user = modelMapper.map(userDTO, User.class);

		return user;
	}

}

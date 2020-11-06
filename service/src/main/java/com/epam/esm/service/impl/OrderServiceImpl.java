package com.epam.esm.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.epam.esm.dal.OrderDao;
import com.epam.esm.dto.OrderDTO;
import com.epam.esm.entity.Order;
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
	public List<OrderDTO> getOrdersByUserId(long userId) {
		
		List<Order> orders = orderDao.findOrdersByUserId(userId);
		
		if (orders == null) {
			return null;
		}
		
		return orders.stream().map(this::convertToDto).collect(Collectors.toList());
	}
	
	private OrderDTO convertToDto(Order order) {

		OrderDTO orderDTO = modelMapper.map(order, OrderDTO.class);

		return orderDTO;
	}

	private Order convertToEntity(OrderDTO orderDTO) {

		Order order = modelMapper.map(orderDTO, Order.class);

		return order;
	}

}

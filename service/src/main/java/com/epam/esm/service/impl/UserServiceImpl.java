package com.epam.esm.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.epam.esm.dal.UserDao;
import com.epam.esm.dto.UserDTO;
import com.epam.esm.entity.User;
import com.epam.esm.service.UserService;

@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private ModelMapper modelMapper;
	
	public void setModelMapper(ModelMapper mapper) {
		this.modelMapper = mapper;
	}

	@Override
	public UserDTO saveUser(UserDTO userDTO) {
		
		User user = userDao.addUser(convertToEntity(userDTO));
		
		return convertToDto(user);
	}
	
	@Override
	public UserDTO getUser(long id) {
		
		User user = userDao.findUser(id);
		
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

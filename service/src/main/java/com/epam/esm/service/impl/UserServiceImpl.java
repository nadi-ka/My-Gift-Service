package com.epam.esm.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.epam.esm.dal.UserDao;
import com.epam.esm.dto.UserDTO;
import com.epam.esm.entity.User;
import com.epam.esm.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	private UserDao userDao;
	private ModelMapper modelMapper;

	@Autowired
	public UserServiceImpl(UserDao userDao, ModelMapper modelMapper) {
		this.userDao = userDao;
		this.modelMapper = modelMapper;
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
			return new UserDTO();
		}
		return convertToDto(user);
	}

	private UserDTO convertToDto(User user) {
		return modelMapper.map(user, UserDTO.class);
	}

	private User convertToEntity(UserDTO userDTO) {
		return modelMapper.map(userDTO, User.class);
	}

}

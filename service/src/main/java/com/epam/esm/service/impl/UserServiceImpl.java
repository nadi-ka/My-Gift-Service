package com.epam.esm.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.epam.esm.dal.UserDao;
import com.epam.esm.dto.UserDTO;
import com.epam.esm.dto.UserRegisterDTO;
import com.epam.esm.entity.User;
import com.epam.esm.entity.role.Role;
import com.epam.esm.service.UserService;
import com.epam.esm.service.exception.NotUniqueEmailException;
import com.epam.esm.service.exception.NotUniqueLoginException;

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
	public UserDTO saveUser(UserRegisterDTO userDTO) {
		if (userDao.findUserByLogin(userDTO.getLogin()) != null) {
			throw new NotUniqueLoginException("The login is already exists");
		}
		if (userDao.findUserByEmail(userDTO.getEmail()) != null) {
			throw new NotUniqueEmailException("The email is already exists");
		}
		User userToSave = convertToEntity(userDTO);
		userToSave.setRole(Role.USER);
		User user = userDao.addUser(userToSave);
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
		return modelMapper.map(user, UserDTO.class);
	}
	
	private User convertToEntity(UserRegisterDTO userDTO) {
		return modelMapper.map(userDTO, User.class);
	}

}

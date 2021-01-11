package com.epam.esm.service;

import com.epam.esm.dto.UserDTO;
import com.epam.esm.dto.UserRegisterDTO;

public interface UserService {
	
	UserDTO saveUser(UserRegisterDTO userDTO);
	
	UserDTO getUser(long id);

}

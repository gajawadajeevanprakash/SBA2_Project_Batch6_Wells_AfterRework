package com.iiht.training.eloan.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iiht.training.eloan.dto.UserDto;
import com.iiht.training.eloan.entity.Users;
import com.iiht.training.eloan.repository.UsersRepository;
import com.iiht.training.eloan.service.AdminService;

@Service
public class AdminServiceImpl implements AdminService {

	@Autowired
	private UsersRepository usersRepository;
	
	// utility methods for User Dto
		private UserDto convertEntityToOutputDto(Users user) {
			UserDto userOutputDto = new UserDto();
			userOutputDto.setId(user.getId());
			userOutputDto.setFirstName(user.getFirstName());
			userOutputDto.setLastName(user.getLastName());
			userOutputDto.setEmail(user.getEmail());
			userOutputDto.setMobile(user.getMobile());
			return userOutputDto;
		}

		private Users covertInputDtoToEntity(UserDto userInputDto, String strInputRoleName) {
			Users user = new Users();
			//user.setId(userInputDto.getId());
			user.setFirstName(userInputDto.getFirstName());
			user.setLastName(userInputDto.getLastName());
			user.setEmail(userInputDto.getEmail());
			user.setMobile(userInputDto.getMobile());
			user.setRole(strInputRoleName);
			return user;
		}
			
	@Override
	public UserDto registerClerk(UserDto userDto) {
		// convert dto into entity
		Users registeruser = this.covertInputDtoToEntity(userDto, "Clerk");
		// save entity in DB : returns the copy of newly added record back
		
		Users newregisteruser = this.usersRepository.save(registeruser);
		// convert entity into output dto
		
		UserDto userOutputDto = this.convertEntityToOutputDto(newregisteruser);
		return userOutputDto;
	}

	@Override
	public UserDto registerManager(UserDto userDto) {
		// convert dto into entity
		Users registeruser = this.covertInputDtoToEntity(userDto, "Manager");
		// save entity in DB : returns the copy of newly added record back
		
		Users newregisteruser = this.usersRepository.save(registeruser);
		// convert entity into output dto
		
		UserDto userOutputDto = this.convertEntityToOutputDto(newregisteruser);
		return userOutputDto;
	}

	@Override
	public List<UserDto> getAllClerks() {
		
		List<Users> users = this.usersRepository.findByUserRole("Clerk");
		List<UserDto> userOutputDtos = 
				users.stream()
							 .map(this :: convertEntityToOutputDto)
							 .collect(Collectors.toList());
		return userOutputDtos;
	}

	@Override
	public List<UserDto> getAllManagers() {
		
		List<Users> users = this.usersRepository.findByUserRole("Manager");
		List<UserDto> userOutputDtos = 
				users.stream()
							 .map(this :: convertEntityToOutputDto)
							 .collect(Collectors.toList());
		return userOutputDtos;
	}

}

package com.iiht.training.eloan.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iiht.training.eloan.dto.LoanOutputDto;
import com.iiht.training.eloan.dto.UserDto;
import com.iiht.training.eloan.exception.ClerkNotFoundException;
import com.iiht.training.eloan.exception.CustomerNotFoundException;
import com.iiht.training.eloan.exception.ManagerNotFoundException;
import com.iiht.training.eloan.service.AdminService;

@RestController
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private AdminService adminService;
	
	@PostMapping("/register-clerk")
	public ResponseEntity<UserDto> registerClerk(@Valid @RequestBody UserDto userDto, BindingResult result)
	{
		if(result.hasErrors()) {
			throw new ClerkNotFoundException("Invalid data format! Cannot create Clerk");
		}
		
		UserDto outputDto = new UserDto();
		outputDto = this.adminService.registerClerk(userDto);
		ResponseEntity<UserDto> response = new ResponseEntity<UserDto> (outputDto, HttpStatus.OK);
		return response;
	}
	
	@PostMapping("/register-manager")
	public ResponseEntity<UserDto> registerManager(@Valid @RequestBody UserDto userDto, BindingResult result){
		
		if(result.hasErrors()) {
			throw new ManagerNotFoundException("Invalid data format! Cannot create Manager");
		}
		
		UserDto outputDto = new UserDto();
		outputDto = this.adminService.registerManager(userDto);
		ResponseEntity<UserDto> response = new ResponseEntity<UserDto> (outputDto, HttpStatus.OK);
		return response;
	}
	
	@GetMapping("/all-clerks")
	public ResponseEntity<List<UserDto>> getAllClerks()
	{
		List<UserDto> userOutputDtos = this.adminService.getAllClerks();
		
		ResponseEntity<List<UserDto>> response =
				new ResponseEntity<List<UserDto>>(userOutputDtos, HttpStatus.OK);
		
		return response;
	}
	
	@GetMapping("/all-managers")
	public ResponseEntity<List<UserDto>> getAllManagers(){
		List<UserDto> userOutputDtos = this.adminService.getAllManagers();
		
		ResponseEntity<List<UserDto>> response =
				new ResponseEntity<List<UserDto>>(userOutputDtos, HttpStatus.OK);
		
		return response;
	}
	
	
}

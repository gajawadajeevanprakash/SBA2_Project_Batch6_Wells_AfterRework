package com.iiht.training.eloan.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.iiht.training.eloan.dto.LoanDto;
import com.iiht.training.eloan.dto.LoanOutputDto;
import com.iiht.training.eloan.dto.ProcessingDto;
import com.iiht.training.eloan.dto.SanctionOutputDto;
import com.iiht.training.eloan.dto.UserDto;
import com.iiht.training.eloan.entity.Loan;
import com.iiht.training.eloan.entity.ProcessingInfo;
import com.iiht.training.eloan.entity.SanctionInfo;
import com.iiht.training.eloan.entity.Users;
import com.iiht.training.eloan.exception.CustomerNotFoundException;
import com.iiht.training.eloan.exception.LoanNotFoundException;
import com.iiht.training.eloan.repository.LoanRepository;
import com.iiht.training.eloan.repository.ProcessingInfoRepository;
import com.iiht.training.eloan.repository.SanctionInfoRepository;
import com.iiht.training.eloan.repository.UsersRepository;
import com.iiht.training.eloan.service.CustomerService;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private UsersRepository usersRepository;
	
	@Autowired
	private LoanRepository loanRepository;
	
	@Autowired
	private ProcessingInfoRepository pProcessingInfoRepository;
	
	@Autowired
	private SanctionInfoRepository sanctionInfoRepository;
	
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
		
		private LoanOutputDto convertLoanEntityToOutputDto(Loan loan, UserDto userDto, LoanDto loanDto, ProcessingDto processingDto,
				SanctionOutputDto sanctionOutputDto) {
			LoanOutputDto loanOutputDto = new LoanOutputDto();
			String status;
			
			loanOutputDto.setLoanAppId(loan.getId());
			loanOutputDto.setCustomerId(loan.getCustomerId());
			loanOutputDto.setUserDto(userDto);
			loanOutputDto.setLoanDto(loanDto);
			loanOutputDto.setProcessingDto(processingDto);
			loanOutputDto.setSanctionOutputDto(sanctionOutputDto);
			if (loan.getStatus()==0) {
				status = "Applied";
			} else if(loan.getStatus()==1) {
				status = "Processed";
			} else if(loan.getStatus()==2) {
				status = "Sanctioned";
			} else if(loan.getStatus()==-1) {
				status = "Rejected";
			} else {
				status = "";
			}
			loanOutputDto.setStatus(status);
			loanOutputDto.setRemark(loan.getRemark());
			
			return loanOutputDto;
		}
		
		private ProcessingDto convertProcessingInfoToDto(ProcessingInfo processingInfo) {
			if (processingInfo!=null) {
				ProcessingDto processingDto = new ProcessingDto();
				
				processingDto.setAcresOfLand(processingInfo.getAcresOfLand());
				processingDto.setLandValue(processingInfo.getLandValue());
				processingDto.setAppraisedBy(processingInfo.getAppraisedBy());
				processingDto.setValuationDate(processingInfo.getValuationDate());
				processingDto.setAddressOfProperty(processingInfo.getAddressOfProperty());
				processingDto.setSuggestedAmountOfLoan(processingInfo.getSuggestedAmountOfLoan());
				
				return processingDto;
			} else {
				return null;
			}
		}
		
		private SanctionOutputDto convertSanctionInfoToOutputDto(SanctionInfo sanctionInfo) {
			if (sanctionInfo!=null) {
				SanctionOutputDto sanctionOutputDto = new SanctionOutputDto();
				
				sanctionOutputDto.setLoanAmountSanctioned(sanctionInfo.getLoanAmountSanctioned());
				sanctionOutputDto.setTermOfLoan(sanctionInfo.getTermOfLoan());
				sanctionOutputDto.setPaymentStartDate(sanctionInfo.getPaymentStartDate());
				sanctionOutputDto.setLoanClosureDate(sanctionInfo.getLoanClosureDate());
				sanctionOutputDto.setMonthlyPayment(sanctionInfo.getMonthlyPayment());
				
				return sanctionOutputDto;
			} else {
				return null;
			}
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
	public UserDto register(UserDto userDto) {
		// convert dto into entity
		Users registeruser = this.covertInputDtoToEntity(userDto, "Customer");
		// save entity in DB : returns the copy of newly added record back
		
		System.out.println("register user" + registeruser);
		
		Users newregisteruser = this.usersRepository.save(registeruser);
		// convert entity into output dto
		
		System.out.println("register new user" + newregisteruser);
		
		UserDto userOutputDto = this.convertEntityToOutputDto(newregisteruser);
		return userOutputDto;
	}
	public Long getLoanAppId(Loan loan) {
		Long loanAppId = loan.getId();
		
		return loanAppId;
	}
	
	//********************************************************************************//
	
	/*
	 * @Override public UserDto register(UserDto userDto) { // TODO Auto-generated
	 * method stub return null; }
	 */
	
	private UserDto convertCustomerEntityToOutputDto(Users user) {
		UserDto userDto = new UserDto();
		
		userDto.setFirstName(user.getFirstName());
		userDto.setLastName(user.getLastName());
		userDto.setEmail(user.getEmail());
		userDto.setMobile(user.getMobile());
		userDto.setId(user.getId());
		
		return userDto;
	}
	
	// utility methods for Loan Dto
		private LoanOutputDto convertEntityToOutputDtoLoan(Loan loan) {
			LoanOutputDto loanOutputDto = new LoanOutputDto();
			loanOutputDto.setCustomerId(loan.getCustomerId());
			loanOutputDto.setLoanAppId(loan.getId());
			loanOutputDto.setStatus("Applied");
			loanOutputDto.setRemark(loan.getRemark());
			return loanOutputDto;
		}
		
		private LoanDto convertEntityToLoanDto(Loan loan) {
			LoanDto loanDto = new LoanDto();
			
			loanDto.setLoanName(loan.getLoanName());
			loanDto.setLoanAmount(loan.getLoanAmount());
			loanDto.setLoanApplicationDate(loan.getLoanApplicationDate());
			loanDto.setBusinessStructure(loan.getBusinessStructure());
			loanDto.setBillingIndicator(loan.getBillingIndicator());
			loanDto.setTaxIndicator(loan.getTaxIndicator());
			
			return loanDto;
		}

		private Loan covertInputDtoToEntityLoan(LoanDto loanInputDto, Long customerId) {
			Loan loan = new Loan();
			loan.setCustomerId(customerId);
			loan.setLoanName(loanInputDto.getLoanName());
			loan.setLoanAmount(loanInputDto.getLoanAmount());
			loan.setLoanApplicationDate(loanInputDto.getLoanApplicationDate());
			loan.setBusinessStructure(loanInputDto.getBusinessStructure());
			loan.setBillingIndicator(loanInputDto.getBillingIndicator());
			loan.setTaxIndicator(loanInputDto.getTaxIndicator());
			loan.setStatus(0);
			loan.setRemark("All Good"); // Giving comment as as 'All Good' as he is applying for loan newly
			return loan;
		}

	@Override
	public LoanOutputDto applyLoan(Long customerId, LoanDto loanDto) {
		
		Users user = this.usersRepository.findByIdAndRole(customerId,"Customer").orElseThrow(() -> new CustomerNotFoundException("Customer Not Found"));
		Loan registerloan = this.covertInputDtoToEntityLoan(loanDto, customerId);
		
		// save entity in DB : returns the copy of newly added record back
		Loan newregisterloan = this.loanRepository.save(registerloan);
		
		// convert entity into output dto
		LoanOutputDto loanOutputDto = this.convertEntityToOutputDtoLoan(newregisterloan);
		return loanOutputDto;
	}

	@Override
	public LoanOutputDto getStatus(Long loanAppId) {
		// fetch record from DB
		
		Loan appliedLoan = this.loanRepository.findById(loanAppId).orElseThrow(() -> new LoanNotFoundException("Loan Not Found"));
		Users user = this.usersRepository.findById(appliedLoan.getCustomerId()).orElseThrow(() -> new CustomerNotFoundException("Customer Not Found"));
		ProcessingInfo processingInfo = this.pProcessingInfoRepository.findByLoanAppId(appliedLoan.getId()).orElse(null);
		SanctionInfo sanctionInfo = this.sanctionInfoRepository.findByLoanAppId(appliedLoan.getId()).orElse(null);
		
		// convert entity into output DTO
		UserDto userDto = this.convertCustomerEntityToOutputDto(user);
		LoanDto loanDto = this.convertEntityToLoanDto(appliedLoan);
		ProcessingDto processingDto = this.convertProcessingInfoToDto(processingInfo);
		SanctionOutputDto sanctionOutputDto = this.convertSanctionInfoToOutputDto(sanctionInfo);
		LoanOutputDto loanOutputDto = this.convertLoanEntityToOutputDto(appliedLoan, userDto, loanDto, processingDto, sanctionOutputDto); 
		
		return loanOutputDto;
	}

	@Override
	public List<LoanOutputDto> getStatusAll(Long customerId) {
		
		this.usersRepository.findByIdAndRole(customerId,"Customer").orElseThrow(() -> new CustomerNotFoundException("Customer Not Found"));
		
		//Get List of loans using Customer Id
		List<Loan> allLoan = this.loanRepository.findAllByCustomerId(customerId).orElseThrow(() -> new LoanNotFoundException("No Applied Loans Found"));

		//Get List of application Id's using Loan object
		List<Long> loanAppIds = allLoan.stream().map(this::getLoanAppId).collect(Collectors.toList()); 
		
		//Get LoanOutputDto object from the application Id's 
		List<LoanOutputDto> loanOutputDto = loanAppIds.stream().map(this::getStatus).collect(Collectors.toList());
		
		return loanOutputDto;
		
		
	}

}

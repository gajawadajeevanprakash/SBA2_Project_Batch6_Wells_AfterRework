package com.iiht.training.eloan.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.iiht.training.eloan.entity.Users;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long>{
	
	/* Static Query
	 * @Query("Select u from Users u where u.role='Clerk'") public List<Users>
	 * findAllClerks();
	 * 
	 * @Query("Select u from Users u where u.role='Manager'")public List<Users>
	 * findAllManagers();
	 */
	
	//Dynamic query
	@Query("Select u from Users u where u.role=?1")
	public List<Users> findByUserRole(String name);
	
	Optional<List<Users>> findAllByRole(String role);
	
	Optional<Users> findByIdAndRole(Long id,String role);

}

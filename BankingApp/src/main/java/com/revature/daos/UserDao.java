package com.revature.daos;

import java.util.List;

import com.revature.models.BankAccount;
import com.revature.models.User;

public interface UserDao {
	public static final UserDao currentImplementation = UserDaoSQL.instance;
	
	List<User> findAllUsers(); //For use by admins only
	
	User findByUserId(int id); // For use by admins only
	
	User findByUsername(String username); // Admin use only
	
	User findByUsernameAndPassword(String username, String password); // Used for logging in
	
	int save(User u);
	
	int changePassword(User u, String pass);

}

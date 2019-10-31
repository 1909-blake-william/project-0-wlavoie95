package com.revature.util;

import com.revature.daos.BankAccountDao;
import com.revature.daos.UserDao;
import com.revature.models.BankAccount;
import com.revature.models.User;

public class AuthUtil {
	public static final AuthUtil instance = new AuthUtil();

	private UserDao userDao = UserDao.currentImplementation;
	private User currentUser = null;
	private BankAccountDao bankAccountDao = BankAccountDao.currentImplementation;
	private BankAccount currentBankAccount = null;

	private AuthUtil() {
		super();
	}

	public User login(String username, String password) {
		User u = userDao.findByUsernameAndPassword(username, password);
		currentUser = u;
		return u;
	}
	
	public User logout() {
		currentUser = null;
		return currentUser;
	}

	public User getCurrentUser() {
		return currentUser;
	}
	
	public BankAccount loginBankAccount(int bankAccountId) {
		System.out.println(bankAccountId);
		BankAccount b = bankAccountDao.getBankAccountById(bankAccountId);
		currentBankAccount = b;
		return currentBankAccount;
	}
	
	public BankAccount logoutBankAccount() {
		currentBankAccount = null;
		return currentBankAccount;
	}
	
	public BankAccount getCurrentBankAccount() {
		return currentBankAccount;
	}
}
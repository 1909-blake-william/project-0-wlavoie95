package com.revature.daos;

import java.util.List;

import com.revature.models.BankAccount;
import com.revature.models.User;

public interface BankAccountDao {
	public static final BankAccountDao currentImplementation = BankAccountDaoSQL.instance;
	
	
	List<BankAccount> listUserBankAccounts(User user);
	
	List<BankAccount> listAllBankAccounts(); // Can be used by an Admin only
	
	BankAccount getBankAccountById(int id);
	
	int addBankAccount(User u, BankAccount b);
	
	int deposit(BankAccount b, double amount);
	
	int withdrawal(BankAccount b, double amount);
	
	int closeAccount(User u, BankAccount b);
	
}

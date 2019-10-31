package com.revature.daos;

import java.util.List;

import com.revature.models.BankAccount;
import com.revature.models.Transaction;
import com.revature.models.User;

public interface TransactionDao {
	public static final TransactionDao currentImplementation = TransactionDaoSQL.instance;
	
	List<Transaction> listUserTransactions(User user);
	
	List<Transaction> listBankAccountTransactions(BankAccount bAccount);
	
	List<Transaction> listAllTransactions();
	
	int createLog(String action);
	
	int createLog(String action, double amount);
}

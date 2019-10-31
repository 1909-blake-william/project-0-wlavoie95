package com.revature.prompts;


import java.util.List;
import java.util.Scanner;

import com.revature.daos.BankAccountDao;
import com.revature.daos.TransactionDao;
import com.revature.daos.UserDao;
import com.revature.models.BankAccount;
import com.revature.models.Transaction;
import com.revature.models.User;
import com.revature.util.AuthUtil;

public class AdminMenuPrompt implements Prompt{
	private UserDao userDao = UserDao.currentImplementation;
	private BankAccountDao bankAccountDao = BankAccountDao.currentImplementation;
	private TransactionDao transactionDao = TransactionDao.currentImplementation;
	private AuthUtil authUtil = AuthUtil.instance;

	@Override
	public Prompt run() {
		User u = authUtil.getCurrentUser();
		Scanner scan = new Scanner(System.in);
		System.out.println("Welcome, " + u.getUsername() + ". Please choose an option.");
		System.out.println("Enter 1 to view all User accounts.");
		System.out.println("Enter 2 to view all Bank Accounts.");
		System.out.println("Enter 3 to view a specific User's Bank Accounts.");
		System.out.println("Enter 4 to view all Transaction logs."); //
		System.out.println("Enter 5 to view a specific User's Transaction logs."); //
		System.out.println("Enter 6 to view a specific Bank Account's Transaction logs."); //
		System.out.println("Enter 7 to create a new Admin User.");
		System.out.println("Enter 8 to change your password.");
		System.out.println("Enter 9 to log out.");
		
		int selection = scan.nextInt();
		scan.nextLine();
		
		switch (selection) {
		case 1:{
			List<User> users = userDao.findAllUsers();
			for(User user : users) {
				System.out.println(user.adminToString());
			}
			break;
		}
		
		case 2:{
			List<BankAccount> bankAccounts = bankAccountDao.listAllBankAccounts();
			for(BankAccount bAcc : bankAccounts) {
				System.out.println(bAcc);
			}
			break;
		}
		
		case 3:{
			System.out.println("Please provide the username of the User whose Bank Account you are searching for.");
			String username = scan.nextLine();
			User user = userDao.findByUsername(username);
			System.out.println(bankAccountDao.listUserBankAccounts(user));
			break;
		}
		
		case 4:{
			List<Transaction> transactions = transactionDao.listAllTransactions();
			for(Transaction t : transactions) {
				System.out.println(t);
			}
			break;
		}
		
		case 5:{
			System.out.println("Please provide the username of the User whose Transaction logs you are searching for.");
			String username = scan.nextLine();
			User user = userDao.findByUsername(username);
			List<Transaction> transactions = transactionDao.listUserTransactions(user);
			for(Transaction t : transactions) {
				System.out.println(t);
			}
			break;
		}
		
		case 6:{
			System.out.println("Please provide the ID of the Bank Action whose Transaction logs you are searching for.");
			int aId = scan.nextInt();
			scan.nextLine();
			BankAccount bAccount = bankAccountDao.getBankAccountById(aId);
			List<Transaction> transactions = transactionDao.listBankAccountTransactions(bAccount);
			for(Transaction t : transactions) {
				System.out.println(t);
			}
			break;
		}
		
		case 7:{
			System.out.println("Username?");
			String username = scan.nextLine();
			System.out.println("Password?");
			String password = scan.nextLine();
			userDao.save(new User(0, username, password, true));
			System.out.println("Successfully created new Admin User with username " + username);
			break;
		}
		
		case 8:{
			System.out.println("Please confirm your old password.");
			String oldPass = scan.nextLine();
			if(oldPass.equals(u.getPassword())) {
				System.out.println("Please enter your new password.");
				String newPass = scan.nextLine();
				userDao.changePassword(u, newPass);
				System.out.println("Password has been successfully changed.");
			} else {
				System.out.println("Password does not match.");
			}
			break;
		}
		
		case 9:{
			System.out.println("Logging out.");
			authUtil.logout();
			System.out.println("Have a nice day!");
			System.out.println();
			return PromptFactory.loginMenu;
		}
		
		default:
			System.out.println("Invalid selection. Please try again.");
			break;
		}
		
		return this;
	}

}

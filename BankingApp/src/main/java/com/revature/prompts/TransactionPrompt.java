package com.revature.prompts;

import java.util.Scanner;

import com.revature.daos.BankAccountDao;
import com.revature.daos.TransactionDao;
import com.revature.daos.UserDao;
import com.revature.models.BankAccount;
import com.revature.models.User;
import com.revature.util.AuthUtil;

public class TransactionPrompt implements Prompt{
	private UserDao userDao = UserDao.currentImplementation;
	private BankAccountDao bankAccountDao = BankAccountDao.currentImplementation;
	private TransactionDao transactionDao = TransactionDao.currentImplementation;
	private AuthUtil authUtil = AuthUtil.instance;
	
	@Override
	public Prompt run() {
		User u = authUtil.getCurrentUser();
		BankAccount b = authUtil.getCurrentBankAccount();
		Scanner scan = new Scanner(System.in);
		System.out.println(b);
		System.out.println("What would you like to do?");
		System.out.println();
		System.out.println("Enter 1 to make a withdrawal.");
		System.out.println("Enter 2 to make a deposit.");
		System.out.println("Enter 3 to return to the accounts overview.");
		int selection = scan.nextInt();
		scan.nextLine();
		
		switch (selection) {
		case 1:{
			System.out.println("How much money would you like to withdraw?");
			double amount = scan.nextDouble();
			scan.nextLine();
			bankAccountDao.withdrawal(b, amount);
			transactionDao.createLog("Withdrawal", amount);
			System.out.println("Your new balance is: $" + b.getBalance() + ".");
			break;
		}
		case 2:{
			System.out.println("How much money would you like to deposit?");
			double amount = scan.nextDouble();
			scan.nextLine();
			bankAccountDao.deposit(b, amount);
			transactionDao.createLog("Withdrawal", amount);
			System.out.println("Your new balance is: $" + b.getBalance() + ".");
			break;
		}
		case 3:{
			System.out.println("Returning to the accounts overview");
			authUtil.logoutBankAccount();
			return PromptFactory.userMenu;
		}
		default:
			System.out.println("Invalid selection.");
			break;
		}
		
		return this;
	}

}

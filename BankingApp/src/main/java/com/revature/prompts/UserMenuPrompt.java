package com.revature.prompts;

import java.util.List;
import java.util.Scanner;

import com.revature.daos.BankAccountDao;
import com.revature.daos.TransactionDao;
import com.revature.daos.UserDao;
import com.revature.models.BankAccount;
import com.revature.models.User;
import com.revature.util.AuthUtil;

public class UserMenuPrompt implements Prompt {
	private UserDao userDao = UserDao.currentImplementation;
	private BankAccountDao bankAccountDao = BankAccountDao.currentImplementation;
	private TransactionDao transactionDao = TransactionDao.currentImplementation;

	@Override
	public Prompt run() {
		AuthUtil authUtil = AuthUtil.instance;
		User u = authUtil.getCurrentUser();
		List<BankAccount> bankAccounts = bankAccountDao.listUserBankAccounts(u);
		Scanner scan = new Scanner(System.in);
		System.out.println("Welcome, " + u.getUsername() + ". Please choose an option.");
		System.out.println("Enter 1 to view your bank accounts.");
		System.out.println("Enter 2 to open a new bank account.");
		System.out.println("Enter 3 to close a bank account.");
		System.out.println("Enter 4 to select a bank account.");
		System.out.println("Enter 5 to change password.");
		System.out.println("Enter 6 to log out.");

		int selection = scan.nextInt();
		scan.nextLine();

		switch (selection) {
		case 1: {
			if (bankAccounts != null && bankAccounts.size() > 0) {
				int countInactives = 0;
				for (int i = 0; i < bankAccounts.size(); i++) {
					if (bankAccounts.get(i).isActive()) {
						System.out.println(bankAccounts.get(i));
					} else {
						countInactives++;
					}
				}
				if (countInactives == bankAccounts.size()) {
					System.out.println("You do not have any opened bank accounts with us!");
				}
			} else {
				System.out.println("You have not opened any bank accounts with us!");
			}
			break;
		}

		case 2: {
			System.out.println("Would you like to open a Checking account or a Savings account?");
			String accountType = scan.nextLine();
			BankAccount b;
			if (accountType.toLowerCase().equals("checking")) {
				b = new BankAccount("Checking");
				bankAccountDao.addBankAccount(u, b);
			} else if (accountType.toLowerCase().equals("savings")) {
				b = new BankAccount("Savings");
				bankAccountDao.addBankAccount(u, b);
			} else {
				System.out.println("Account type not recognized.");
			}
			break;
		}

		case 3: {
			System.out.println("Please select a Bank Account by ID from the following: ");
			System.out.println();
			if (bankAccounts != null && bankAccounts.size() > 0) {
				for (BankAccount b : bankAccounts) {
					if (b.isActive()) {
						System.out.println(b);
					}
				}
			} else {
				System.out.println("Please open a Bank Account before trying to access one.");
				break;
			}

			int aId = scan.nextInt();
			BankAccount b = authUtil.loginBankAccount(aId);
			int closed = bankAccountDao.closeAccount(u, b);
			if (closed != 0) {
				transactionDao.createLog("Account Closed");
			}
			authUtil.logoutBankAccount();
			break;
		}

		case 4: {
			System.out.println("Please select a Bank Account by ID from the following: ");
			System.out.println();
			if (bankAccounts != null && bankAccounts.size() > 0) {
				for (BankAccount b : bankAccounts) {
					if (b.isActive()) {
						System.out.println(b);
					}
				}
			} else {
				System.out.println("Please open a Bank Account before trying to access one.");
				break;
			}

			int aId = scan.nextInt();
			BankAccount b = authUtil.loginBankAccount(aId);

			if (b != null) {
				return PromptFactory.transactionMenu;
			} else {
				System.out.println("Account ID not recognized.");
				break;
			}
		}

		case 5: {
			System.out.println("Please confirm your old password.");
			String oldPass = scan.nextLine();
			if (oldPass.equals(u.getPassword())) {
				System.out.println("Please enter your new password.");
				String newPass = scan.nextLine();
				userDao.changePassword(u, newPass);
				System.out.println("Password has been successfully changed.");
			} else {
				System.out.println("Password does not match.");
			}
			break;
		}

		case 6: {
			System.out.println("Logging out.");
			authUtil.logout();
			System.out.println("Have a nice day!");
			return PromptFactory.loginMenu;
		}

		default:
			System.out.println("Invalid selection. Please try again.");
			break;
		}

		return this;
	}

}

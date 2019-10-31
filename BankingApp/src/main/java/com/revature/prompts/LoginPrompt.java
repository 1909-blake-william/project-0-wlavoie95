package com.revature.prompts;

import java.util.Scanner;

import org.apache.log4j.Logger;

import com.revature.daos.UserDao;
import com.revature.models.User;
import com.revature.util.AuthUtil;

public class LoginPrompt implements Prompt {
	private Logger log = Logger.getRootLogger();
	private UserDao userDao = UserDao.currentImplementation;

	@Override
	public Prompt run() {
		AuthUtil authUtil = AuthUtil.instance;
		Scanner scan = new Scanner(System.in);
		System.out.println("Welcome to Console Line Banking!");
		System.out.println("Press 1 to login.");
		System.out.println("Press 2 to register.");

		String selection = scan.nextLine();

		switch (selection) {
		/*
		 * For user login, user types in their username and password. If the user is an
		 * admin, they will be taken to the admin menu. Otherwise, they will be taken to
		 * the user menu. If the username and password do not both match, the user will
		 * fail to login.
		 */
		case "1": {
			System.out.println("User Login");
			System.out.println("Enter Username: ");
			String username = scan.nextLine();
			System.out.println("Enter Password: ");
			String password = scan.nextLine();

			User u = authUtil.login(username, password);
			if (u != null) {
				if (u.isAdmin()) {
					return PromptFactory.adminMenu; // Not yet implemented.
				} else {
					return PromptFactory.userMenu;
				}
			} else {
				System.out.println("Incorrect Username and/or Password.");
			}
			break;
		}
		/*
		 * 
		 */
		case "2": {
			System.out.println("User Registration");
			System.out.println("Enter Username: ");
			String username = scan.nextLine();
			System.out.println("Enter Password: ");
			String password = scan.nextLine();
			userDao.save(new User(0, username, password, false));
			authUtil.login(username, password);
			return PromptFactory.userMenu;
		}
		default: {
			System.out.println("Invalid Selection.");
			break;
		}

		}

		return this;
	}

}

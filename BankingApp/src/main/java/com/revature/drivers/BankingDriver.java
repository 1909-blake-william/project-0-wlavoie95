package com.revature.drivers;

import org.apache.log4j.Logger;

import com.revature.prompts.Prompt;
import com.revature.prompts.PromptFactory;

public class BankingDriver {

	private static Logger log = Logger.getRootLogger();

	public static void main(String[] args) {
		Prompt p = PromptFactory.loginMenu;

		while (true) {
			p = p.run();
		}
	}
}
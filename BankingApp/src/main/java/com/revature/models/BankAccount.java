package com.revature.models;

public class BankAccount {
	private int accountId;
	private String accountType;
	private double balance;
	private boolean isActive;
	
	public BankAccount() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BankAccount(String accountType) { // Bank account initially has no money in it
		super();
		this.accountId = 0; // accountId is determined later by a SQL sequence
		this.accountType = accountType;
		this.balance = 0;
		this.isActive = true;
	}
	
	public BankAccount(int accountId, String accountType, double balance, boolean isActive) {
		super();
		this.accountId = accountId;
		this.accountType = accountType;
		this.balance = balance;
		this.isActive = isActive;
	}
	
	public int getAccountId() {
		return accountId;
	}

	public void setAccountNum(int accountId) {
		this.accountId = accountId;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accountType == null) ? 0 : accountType.hashCode());
		long temp;
		temp = Double.doubleToLongBits(balance);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BankAccount other = (BankAccount) obj;
		if (accountType == null) {
			if (other.accountType != null)
				return false;
		} else if (!accountType.equals(other.accountType))
			return false;
		if (Double.doubleToLongBits(balance) != Double.doubleToLongBits(other.balance))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "BankAccount [accountId = " + accountId + ", accountType = " + accountType + ", balance = " + balance + "]";
	}

}

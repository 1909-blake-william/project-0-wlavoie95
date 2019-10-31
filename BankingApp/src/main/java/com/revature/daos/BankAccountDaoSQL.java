package com.revature.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.revature.models.BankAccount;
import com.revature.models.User;
import com.revature.util.AuthUtil;
import com.revature.util.ConnectionUtil;

public class BankAccountDaoSQL implements BankAccountDao {
	private Logger log = Logger.getRootLogger();
	public static final BankAccountDaoSQL instance = new BankAccountDaoSQL();
	private UserDao userDao = UserDao.currentImplementation;
	private TransactionDao transactionDao = TransactionDao.currentImplementation;

	public BankAccountDaoSQL() {
		super();
		// TODO Auto-generated constructor stub
	}

	private BankAccount extractBankAccount(ResultSet rs) throws SQLException { // for the actual user
		int accountNum = rs.getInt("a_id");
		String accountType = rs.getString("a_type");
		double balance = rs.getDouble("balance");
		int active = rs.getInt("is_active");
		boolean isActive = false;
		if (active == 1) {
			isActive = true;
		}
		return new BankAccount(accountNum, accountType, balance, isActive);

	}

	@Override
	public List<BankAccount> listUserBankAccounts(User user) {
//		log.debug("attempting to find the user's bank accounts from DB");

		try (Connection c = ConnectionUtil.getConnection()) {

			String sql = "SELECT * FROM bank_account " + "WHERE u_id = ?";

			PreparedStatement ps = c.prepareStatement(sql);
			ps.setInt(1, user.getUserId());

			ResultSet rs = ps.executeQuery();

			List<BankAccount> bankAccounts = new ArrayList<BankAccount>();
			while (rs.next()) {
				bankAccounts.add(extractBankAccount(rs));
			}
			return bankAccounts;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<BankAccount> listAllBankAccounts() {
//		log.debug("attempting to find all bank accounts from DB");
		try (Connection c = ConnectionUtil.getConnection()) {

			String sql = "SELECT * FROM bank_account";

			PreparedStatement ps = c.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			List<BankAccount> bankAccounts = new ArrayList<BankAccount>();
			while (rs.next()) {
				bankAccounts.add(extractBankAccount(rs));
			}
			return bankAccounts;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public BankAccount getBankAccountById(int id) {
//		log.debug("Attempted to find a Bank Account by ID");
		try (Connection c = ConnectionUtil.getConnection()) {

			String sql = "SELECT * FROM bank_account " + "WHERE a_id = ?";

			PreparedStatement ps = c.prepareStatement(sql);
			ps.setInt(1, id);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				return extractBankAccount(rs);
			} else {
				return null;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public int addBankAccount(User u, BankAccount b) {
//		log.debug("attempting to add a new Bank Account to the DB");
		try (Connection c = ConnectionUtil.getConnection()) {

			String sql = "INSERT INTO bank_account (a_id, a_type, u_id) " + "VALUES (ACCOUNT_ID_SEQ.nextval, ?, ?)";

			PreparedStatement ps = c.prepareStatement(sql);
			ps.setString(1, b.getAccountType());
			ps.setInt(2, u.getUserId());
			return ps.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public int deposit(BankAccount b, double amount) {
		if (amount < 0) {
			System.out.println("Cannot make a withdrawal with a deposit!");
		} else {
			b.setBalance(b.getBalance() + amount);
			try (Connection c = ConnectionUtil.getConnection()) {

				String sql = "UPDATE  bank_account " + "SET balance = ? " + "WHERE a_id = ?";

				PreparedStatement ps = c.prepareStatement(sql);
				ps.setDouble(1, b.getBalance());
				ps.setInt(2, b.getAccountId());
				return ps.executeUpdate();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return 0;
			}
		}
		return 0;
	}

	@Override
	public int withdrawal(BankAccount b, double amount) {
		if (amount < 0) {
			System.out.println("Cannot make a deposit with a withdrawal!");
		} else {
			if (b.getBalance() < amount) {
				System.out.println("Cannot make withdrawal, account would be overdrafted.");
				System.out.println("Attempted to withdraw $" + amount + ", but the account only has $" 
						+ b.getBalance() + ".");
			} else {
				b.setBalance(b.getBalance() - amount);
				try (Connection c = ConnectionUtil.getConnection()) {

					String sql = "UPDATE  bank_account " + "SET balance = ? " + "WHERE a_id = ?";

					PreparedStatement ps = c.prepareStatement(sql);
					ps.setDouble(1, b.getBalance());
					ps.setInt(2, b.getAccountId());
					return ps.executeUpdate();

				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return 0;
				}
			}
		}
		return 0;
	}

	@Override
	public int closeAccount(User u, BankAccount b) {
		if (b.getBalance() > 0) {
			System.out.println("Account must have a balance of $0 to be closed.");
		} else {
			b.setActive(false);
//			log.debug("attempting to close a Bank Account");
			try (Connection c = ConnectionUtil.getConnection()) {
				String sql = "UPDATE bank_account " + "SET  is_active = ? " + "WHERE a_id = ?";

				PreparedStatement ps = c.prepareStatement(sql);
				ps.setInt(1, 0); // true is 1 and false is 0 for the DB, will always be 0 in this method
				ps.setInt(2, b.getAccountId());

				return ps.executeUpdate();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return 0;
			}
			
			
		}
		return 0;
	}

}

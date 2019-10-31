package com.revature.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.revature.models.BankAccount;
import com.revature.models.Transaction;
import com.revature.models.User;
import com.revature.util.AuthUtil;
import com.revature.util.ConnectionUtil;

public class TransactionDaoSQL implements TransactionDao{
	public static final TransactionDaoSQL instance = new TransactionDaoSQL();
	private Logger log = Logger.getRootLogger();
	AuthUtil authUtil = AuthUtil.instance;

	private Transaction extractTransaction(ResultSet rs) throws SQLException {
		int tId = rs.getInt("t_id");
		int uId = rs.getInt("u_id");
		int aId = rs.getInt("a_id");
		String action = rs.getString("action");
		double amount = rs.getDouble("amount");
		Timestamp timestamp = rs.getTimestamp("t_time");
		return new Transaction(tId, uId, aId, action, amount, timestamp);
	}
	
	@Override
	public List<Transaction> listUserTransactions(User user) {
		try (Connection c = ConnectionUtil.getConnection()) {

			String sql = "SELECT * FROM transaction_log " + "WHERE u_id = ? " + "ORDER BY t_id DESC";

			PreparedStatement ps = c.prepareStatement(sql);
			ps.setInt(1, user.getUserId());

			ResultSet rs = ps.executeQuery();

			List<Transaction> transactions = new ArrayList<Transaction>();
			while (rs.next()) {
				transactions.add(extractTransaction(rs));
			}
			return transactions;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}


	@Override
	public List<Transaction> listBankAccountTransactions(BankAccount bAccount) {
		try (Connection c = ConnectionUtil.getConnection()) {

			String sql = "SELECT * FROM transaction_log " + "WHERE a_id = ? " + "ORDER BY t_id DESC";

			PreparedStatement ps = c.prepareStatement(sql);
			ps.setInt(1, bAccount.getAccountId());

			ResultSet rs = ps.executeQuery();

			List<Transaction> transactions = new ArrayList<Transaction>();
			while (rs.next()) {
				transactions.add(extractTransaction(rs));
			}
			return transactions;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}


	@Override
	public List<Transaction> listAllTransactions() {
		try (Connection c = ConnectionUtil.getConnection()) {

			String sql = "SELECT * FROM transaction_log " + "ORDER BY t_id DESC";

			PreparedStatement ps = c.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			List<Transaction> transactions = new ArrayList<Transaction>();
			while (rs.next()) {
				transactions.add(extractTransaction(rs));
			}
			return transactions;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	} 

	@Override
	public int createLog(String action) {
		User u = authUtil.getCurrentUser();
		BankAccount b = authUtil.getCurrentBankAccount();
//		log.debug("attempting to add a new Bank Account to the DB");
		try (Connection c = ConnectionUtil.getConnection()) {

			String sql = "INSERT INTO transaction_log (t_id, a_id, u_id, action, t_time) "
					+ "VALUES (TRANSACTION_ID_SEQ.nextval, ?, ?, ?, ?)";

			PreparedStatement ps = c.prepareStatement(sql);
			ps.setInt(1, b.getAccountId());
			ps.setInt(2, u.getUserId());
			ps.setString(3, action);
			ps.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
			
			return ps.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}		
	}
	
	@Override
	public int createLog(String action, double amount) {
		User u = authUtil.getCurrentUser();
		BankAccount b = authUtil.getCurrentBankAccount();
//		log.debug("attempting to add a new Bank Account Transaction to the DB");
		try (Connection c = ConnectionUtil.getConnection()) {

			String sql = "INSERT INTO transaction_log (t_id, a_id, u_id, action, amount, t_time) "
					+ "VALUES (TRANSACTION_ID_SEQ.nextval, ?, ?, ?, ?, ?)";
			Timestamp ts = new Timestamp(System.currentTimeMillis());
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setInt(1, b.getAccountId());
			ps.setInt(2, u.getUserId());;
			ps.setString(3, action);
			ps.setDouble(4, amount);
			ps.setTimestamp(5, ts);
			
			return ps.executeUpdate(); // <--- here

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}		
	}

}

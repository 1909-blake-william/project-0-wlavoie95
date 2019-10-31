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
import com.revature.util.ConnectionUtil;

public class UserDaoSQL implements UserDao {
	public static final UserDaoSQL instance = new UserDaoSQL();
	private Logger log = Logger.getRootLogger();

	private UserDaoSQL() {
		super();
		// TODO Auto-generated constructor stub
	}

	private User extractUser(ResultSet rs) throws SQLException{ // for the actual user
		int id = rs.getInt("u_id");
		String username = rs.getString("username");
		String password = rs.getString("password");
		int admin = rs.getInt("is_admin");
		boolean isAdmin = false;
		if (admin == 1) {
			isAdmin = true;
		}
		return new User(id, username, password, isAdmin);

	}

	@Override
	public List<User> findAllUsers() {
//		log.debug("attempting to find user by credentials from DB");
		try (Connection c = ConnectionUtil.getConnection()) {

			String sql = "SELECT * FROM user_account";

			PreparedStatement ps = c.prepareStatement(sql);

			ResultSet rs = ps.executeQuery();
			List<User> users = new ArrayList<User>();
			while (rs.next()) {
				users.add(extractUser(rs));
			}
			return users;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public int save(User u) {
//		log.debug("attempting to add a new user to the DB");
		try (Connection c = ConnectionUtil.getConnection()) {

			String sql = "INSERT INTO user_account (u_id, username, password, is_admin) "
					+ "VALUES (USER_ID_SEQ.nextval, ?, ?, ?)";

			PreparedStatement ps = c.prepareStatement(sql);
			ps.setString(1, u.getUsername());
			ps.setString(2, u.getPassword());
			if (u.isAdmin()) {
				ps.setInt(3, 1); // User is an admin
			} else {
				ps.setInt(3, 0); // User is not an admin
			}
			return ps.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public User findByUserId(int id) {
		log.debug("attempting to find user by user id from DB");
		User u = new User();
		try (Connection c = ConnectionUtil.getConnection()) {

			String sql = "SELECT * FROM user_account " + "WHERE user_id = ?";

			PreparedStatement ps = c.prepareStatement(sql);
			
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				u = extractUser(rs);
			}
			return u;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			return null;
		}
	}

	@Override
	public User findByUsername(String username) {
//		log.debug("attempting to find user by username from DB");
		User u = new User();
		try (Connection c = ConnectionUtil.getConnection()) {

			String sql = "SELECT * FROM user_account " + "WHERE username = ?";

			PreparedStatement ps = c.prepareStatement(sql);
			
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				u = extractUser(rs);
			}
			return u;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			return null;
		}
	}

	@Override
	public User findByUsernameAndPassword(String username, String password) {
		try (Connection c = ConnectionUtil.getConnection()) {

			String sql = "SELECT * FROM user_account " + "WHERE username = ? AND password = ?";

			PreparedStatement ps = c.prepareStatement(sql);
			ps.setString(1, username);
			ps.setString(2, password);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				return extractUser(rs);
			} else {
				return null;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			return null;
		}
	}

	@Override
	public int changePassword(User u, String pass) {
		try (Connection c = ConnectionUtil.getConnection()) {
			String sql = "UPDATE user_account " + "SET  password = ? " + "WHERE u_id = ?";

			PreparedStatement ps = c.prepareStatement(sql);
			ps.setString(1, pass);
			ps.setInt(2, u.getUserId());

			return ps.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}
}

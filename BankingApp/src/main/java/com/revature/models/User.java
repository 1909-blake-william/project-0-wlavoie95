package com.revature.models;

public class User {
	private int userId;
	private boolean isAdmin;
	private String username;
	private String password;

	public User() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public User(int userId, String username, String password, boolean isAdmin) {
		super();
		this.userId = userId;
		this.isAdmin = isAdmin;
		this.username = username;
		this.password = password;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (isAdmin ? 1231 : 1237);
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
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
		User other = (User) obj;
		if (isAdmin != other.isAdmin)
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "User [username = " + username + " ]";
	}

	public String adminToString() {
		return "User [userId = " + userId + " username = " + username + " isAdmin = " + isAdmin + "]";
	}
}

package com.rental.app.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.rental.app.db.PostgreSQLJDBC;
import com.rental.app.model.Customer;

public class CustomerStore {
	private static final CustomerStore INSTANCE = new CustomerStore();
	
	public static CustomerStore getInstance() {
		return INSTANCE;
	}
	
	public boolean checkValidCustomer(int customerId) throws SQLException {
		PreparedStatement pstmt = null;
		Connection connection = null;
		ResultSet rs = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append ("select * from customer where id = ?");
			connection = PostgreSQLJDBC.getInstance().connect();

			pstmt = connection.prepareStatement(sql.toString());
			pstmt.setInt(1, customerId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				return true;
			}
		} catch(Exception e){
			throw new SQLException(e);
		} finally{
			if (pstmt != null) pstmt.close();
			if (connection != null) connection.close();
		}
		return false;
	}
	
	public boolean checkUserCredentailsValid(String emailId, String password) throws SQLException {
		PreparedStatement pstmt = null;
		Connection connection = null;
		ResultSet rs = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append ("select * from customer where emailid = ? and password = ?");
			connection = PostgreSQLJDBC.getInstance().connect();

			pstmt = connection.prepareStatement(sql.toString());
			pstmt.setString(1, emailId);
			pstmt.setString(2, password);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				return true;
			}
		} catch(Exception e){
			throw new SQLException(e);
		} finally{
			if (pstmt != null) pstmt.close();
			if (connection != null) connection.close();
		}
		return false;
	}
	
	public Customer getCustomer(int customerId) throws SQLException {
		PreparedStatement pstmt = null;
		Connection connection = null;
		ResultSet rs = null;
		Customer customer = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append ("select * from customer where id = ?");
			connection = PostgreSQLJDBC.getInstance().connect();

			pstmt = connection.prepareStatement(sql.toString());
			pstmt.setInt(1, customerId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				customer = new Customer.CustomerBuilder().setName(rs.getString("name")).setAge(rs.getInt("age"))
					.setEmailId(rs.getString("emailid")).setPassword(rs.getString("password")).setCity(rs.getString("city"))
					.setState(rs.getString("state")).setCountry(rs.getString("country")).setContactNumber(rs.getInt("contactnumber"))
					.setRewardPoints(rs.getInt("rewardpoints")).build();
			}
		} catch (Exception e) {
			throw new SQLException(e);
		} finally {
			if (pstmt != null) pstmt.close();
			if (connection != null) connection.close();
		}
		return customer;
	}
	
	public void addCustomer(Customer customer) throws SQLException {
		PreparedStatement pstmt = null;
		Connection connection = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append ("INSERT INTO customer values (DEFAULT, ?, ?, ?, ?, ?, ?"
					+ ", ?, ?, ?, ?)");
			connection = PostgreSQLJDBC.getInstance().connect();

			pstmt = connection.prepareStatement(sql.toString());
			pstmt.setString(1, customer.getName());
			pstmt.setInt(2, customer.getAge());
			pstmt.setString(3, customer.getEmailId());
			pstmt.setString(4, customer.getPassword());
			pstmt.setString(5, customer.getCity());
			pstmt.setString(6, customer.getState());
			pstmt.setString(7, customer.getCountry());
			pstmt.setInt(8, customer.getContactNumber());
			pstmt.setString(9, customer.getBillingAddress());
			pstmt.setInt(10, customer.getRewardPoints());
			pstmt.executeUpdate();
		} catch(Exception e){
			throw new SQLException(e);
		} finally{
			if (pstmt != null) pstmt.close();
			if (connection != null) connection.close();
		}
	}
	
	public void updateCustomer(Customer customer) throws SQLException {
		PreparedStatement pstmt = null;
		Connection connection = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append ("update customer set name = ?, age = ?, emailid = ?, password = ?, city = ?"
					+ ", state = ?, country = ?, contactnumber = ?, billingaddress = ?, rewardpoints = ?"
					+ " where id = ?");
			connection = PostgreSQLJDBC.getInstance().connect();

			pstmt = connection.prepareStatement(sql.toString());
			pstmt.setString(1, customer.getName());
			pstmt.setInt(2, customer.getAge());
			pstmt.setString(3, customer.getEmailId());
			pstmt.setString(4, customer.getPassword());
			pstmt.setString(5, customer.getCity());
			pstmt.setString(6, customer.getState());
			pstmt.setString(7, customer.getCountry());
			pstmt.setInt(8, customer.getContactNumber());
			pstmt.setString(9, customer.getBillingAddress());
			pstmt.setInt(10, customer.getRewardPoints());
			pstmt.setInt(11, customer.getId());
			pstmt.executeUpdate();
		} catch(Exception e) {
			throw new SQLException(e);
		} finally {
			if (pstmt != null) pstmt.close();
			if (connection != null) connection.close();
		}
	}
	
	public void deleteCustomer(int customerId) throws SQLException {
		PreparedStatement pstmt = null;
		Connection connection = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append ("delete from customer where id = ?");
			connection = PostgreSQLJDBC.getInstance().connect();

			pstmt = connection.prepareStatement(sql.toString());
			pstmt.setInt(1, customerId);
			pstmt.executeUpdate();
		} catch(Exception e){
			throw new SQLException(e);
		} finally{
			if (pstmt != null) pstmt.close();
			if (connection != null) connection.close();
		}
	}
}

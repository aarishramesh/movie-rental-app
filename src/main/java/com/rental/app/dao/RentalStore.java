package com.rental.app.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.rental.app.db.PostgreSQLJDBC;
import com.rental.app.model.Rental;

public class RentalStore {
	private static final RentalStore INSTANCE = new RentalStore();
	
	public static RentalStore getInstance() {
		return INSTANCE;
	}
	
	public void rentMovie(Rental rental) throws SQLException {
		PreparedStatement pstmt = null;
		Connection connection = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append ("INSERT INTO rental VALUES (DEFAULT, ?, ?, ?, ?, NULL);");
			connection = PostgreSQLJDBC.getInstance().connect();

			pstmt = connection.prepareStatement(sql.toString());
			pstmt.setInt(1, rental.getCustomerId());
			pstmt.setInt(2, rental.getMovieId());
			pstmt.setDate(3, new Date(rental.getStartTime()));
			pstmt.setDate(4, new Date(rental.getEndTime()));
			pstmt.executeUpdate();
		} catch(Exception e){
			throw new SQLException(e);
		} finally{
			if (pstmt != null) pstmt.close();
			if (connection != null) connection.close();
		}
	}
	
	public List<Rental> getRentalsForCustomer(int customerId) throws SQLException {
		PreparedStatement pstmt = null;
		Connection connection = null;
		ResultSet rs = null;
		List<Rental> customerRentals = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append ("select * from rental where customerid = ?;");
			connection = PostgreSQLJDBC.getInstance().connect();

			pstmt = connection.prepareStatement(sql.toString());
			pstmt.setInt(1, customerId);
			
			rs = pstmt.executeQuery();
			customerRentals = constructRentalsFromResultSet(rs);
		} catch(Exception e){
			throw new SQLException(e);
		} finally{
			if (pstmt != null) pstmt.close();
			if (connection != null) connection.close();
		}
		return customerRentals;
	}
	
	private List<Rental> constructRentalsFromResultSet(ResultSet rs) throws SQLException {
		List<Rental> customerRentals = new ArrayList<Rental>();
		while(rs.next()) {
			Rental rental = new Rental(rs.getInt("customerid"), rs.getInt("movieid")
					, rs.getTimestamp("starttime").getTime(), rs.getTimestamp("endtime").getTime());
			customerRentals.add(rental);
		}
		return customerRentals;
	}
	
	public List<Rental> getAllRentalsOverDueDate() throws SQLException {
		PreparedStatement pstmt = null;
		Connection connection = null;
		ResultSet rs = null;
		List<Rental> customerRentals = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append ("select * from rental where endtime < now();");
			connection = PostgreSQLJDBC.getInstance().connect();

			pstmt = connection.prepareStatement(sql.toString());
			
			rs = pstmt.executeQuery();
			customerRentals = constructRentalsFromResultSet(rs);
		} catch(Exception e) {
			throw new SQLException(e);
		} finally {
			if (pstmt != null) pstmt.close();
			if (connection != null) connection.close();
		}
		return customerRentals;
	}
}

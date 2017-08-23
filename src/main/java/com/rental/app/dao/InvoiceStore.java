package com.rental.app.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.rental.app.db.PostgreSQLJDBC;
import com.rental.app.model.Invoice;

public class InvoiceStore {
	private static final InvoiceStore INSTANCE = new InvoiceStore();
	
	public static InvoiceStore getInstance() {
		return INSTANCE;
	}
	
	public Invoice getInvoiceForRentalId(int rentalId) throws SQLException {
		PreparedStatement pstmt = null;
		Connection connection = null;
		ResultSet rs = null;
		Invoice invoice = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append ("select * from invoice where rentalid = ?;");
			connection = PostgreSQLJDBC.getInstance().connect();

			pstmt = connection.prepareStatement(sql.toString());
			pstmt.setLong(1, rentalId);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				invoice = new Invoice();
				invoice.setRentalId(rentalId);
				invoice.setNoOfDaysRented(rs.getInt("noofdaysrented"));
				invoice.setRentalCost(rs.getInt("rentalcost"));
				invoice.setTax(rs.getInt("tax"));
				invoice.setTotalCost(rs.getInt("totalcost"));
				invoice.setHasPaid(rs.getBoolean("haspaid"));
			}
		} catch(Exception e){
			throw new SQLException(e);
		} finally{
			if (pstmt != null) pstmt.close();
			if (connection != null) connection.close();
		}
		return invoice;
	}
	
	public Invoice getInvoiceForInvoiceId(int invoiceId) throws SQLException {
		PreparedStatement pstmt = null;
		Connection connection = null;
		ResultSet rs = null;
		Invoice invoice = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append ("select * from invoice where id = ?;");
			connection = PostgreSQLJDBC.getInstance().connect();

			pstmt = connection.prepareStatement(sql.toString());
			pstmt.setLong(1, invoiceId);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				invoice = new Invoice();
				invoice.setId(invoiceId);
				invoice.setRentalId(rs.getInt("rentalid"));
				invoice.setNoOfDaysRented(rs.getInt("noofdaysrented"));
				invoice.setRentalCost(rs.getInt("rentalcost"));
				invoice.setTax(rs.getInt("tax"));
				invoice.setTotalCost(rs.getInt("totalcost"));
				invoice.setHasPaid(rs.getBoolean("haspaid"));
			}
		} catch(Exception e){
			throw new SQLException(e);
		} finally{
			if (pstmt != null) pstmt.close();
			if (connection != null) connection.close();
		}
		return invoice;
	}
	
	public void addInvoice(Invoice invoice) throws SQLException {
		PreparedStatement pstmt = null;
		Connection connection = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append ("insert into invoice values (DEFAULT, ?, ?, ?, ?, ?, DEFAULT);");
			connection = PostgreSQLJDBC.getInstance().connect();

			pstmt = connection.prepareStatement(sql.toString());
			pstmt.setInt(1, invoice.getRentalId());
			pstmt.setInt(2, invoice.getNoOfDaysRented());
			pstmt.setInt(3, invoice.getRentalCost());
			pstmt.setInt(4, invoice.getTax());
			pstmt.setInt(5, invoice.getTotalCost());
			pstmt.executeUpdate();
		} catch(Exception e) {
			throw new SQLException(e);
		} finally {
			if (pstmt != null) pstmt.close();
			if (connection != null) connection.close();
		}
	}
	
	public boolean checkInvoiceValid(int invoiceId) throws SQLException {
		PreparedStatement pstmt = null;
		Connection connection = null;
		ResultSet rs = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append ("select * from invoice where id = ?;");
			connection = PostgreSQLJDBC.getInstance().connect();

			pstmt = connection.prepareStatement(sql.toString());
			pstmt.setInt(1, invoiceId);
			
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
	
	public Invoice payCustomerInvoice(int invoiceId) throws SQLException {
		PreparedStatement pstmt = null;
		Connection connection = null;
		Invoice invoice = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append ("update invoice set haspaid = true where id = ?;");
			connection = PostgreSQLJDBC.getInstance().connect();

			pstmt = connection.prepareStatement(sql.toString());
			pstmt.setLong(1, invoiceId);
			pstmt.executeUpdate();
			invoice = getInvoiceForInvoiceId(invoiceId);
		} catch(Exception e){
			throw new SQLException(e);
		} finally{
			if (pstmt != null) pstmt.close();
			if (connection != null) connection.close();
		}
		return invoice;
	}
}

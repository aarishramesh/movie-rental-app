package com.rental.app.util;

import java.sql.SQLException;

import com.rental.app.dao.CustomerStore;
import com.rental.app.dao.InvoiceStore;

public class MovieRentalUtil {
	
	public static boolean userValid(int customerId) throws SQLException {
		return CustomerStore.getInstance().checkValidCustomer(customerId);
	}
	
	public static boolean userCredentialsValid(String emailId, String password) throws SQLException {
		return CustomerStore.getInstance().checkUserCredentailsValid(emailId, password);
	}
	
	public static boolean invoiceValid(int invoiceId) throws SQLException {
		return InvoiceStore.getInstance().checkInvoiceValid(invoiceId);
	}
}

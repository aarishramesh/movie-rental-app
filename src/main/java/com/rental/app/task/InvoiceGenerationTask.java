package com.rental.app.task;

import java.sql.SQLException;
import java.util.List;
import java.util.TimerTask;

import com.rental.app.dao.InvoiceStore;
import com.rental.app.dao.RentalStore;
import com.rental.app.model.Invoice;
import com.rental.app.model.Rental;

public class InvoiceGenerationTask extends TimerTask {

	public static final long TASK_INTERVAL = 5 * 60 * 1000;

	@Override
	public void run() {
		try {
			//Runs every 5 minutes
			// Fetches all Rentals which are expired and generates invoice
			List<Rental> overdueRentals = RentalStore.getInstance().getAllRentalsOverDueDate();
			for (Rental rental : overdueRentals) {
				long startTime = rental.getStartTime();
				long endTime =  rental.getEndTime();
				long totalRentalTime = endTime - startTime;
				int noOfDays = (int) totalRentalTime / (86400 * 1000) ;
				Invoice invoice = new Invoice(rental.getId(), rental.getMovieId(), noOfDays);
				InvoiceStore.getInstance().addInvoice(invoice);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
}

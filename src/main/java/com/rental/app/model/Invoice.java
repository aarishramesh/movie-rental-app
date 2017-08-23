package com.rental.app.model;

import java.sql.SQLException;

import com.rental.app.dao.MovieStore;

import lombok.Data;

/**
 *                              Table "public.invoice"
       Column       |  Type   |                      Modifiers                       
--------------------+---------+------------------------------------------------------
 id                 | integer | not null default nextval('invoice_id_seq'::regclass)
 rentalid           | integer  | not null
 noofdaysrented     | integer | not null
 rentalcost         | integer | not null
 tax                | integer | not null
 totalcost          | integer | not null
 haspaid            | boolean | default false
 rewardpointsearned | integer | default 0
Indexes:
    "invoice_pkey" PRIMARY KEY, btree (id)
Foreign-key constraints:
    "invoice_rentalid_fkey" FOREIGN KEY (rentalid) REFERENCES rental(id)
Referenced by:
    TABLE "rental" CONSTRAINT "rental_invoiceid_fkey" FOREIGN KEY (invoiceid) REFERENCES invoice(id)

 * 
 * @author aarishramesh
 *
 */
@Data
public class Invoice {
	private int id;
	private int rentalId;
	private int noOfDaysRented;
	private int rentalCost;
	private int tax;
	private int totalCost;
	private boolean hasPaid;

	public Invoice() {
		
	}
	
	public Invoice(int rentalId, int movieId, int noOfDaysRented) {
		this.rentalId = rentalId;
		this.noOfDaysRented = noOfDaysRented;

		try {
			Movie movie = MovieStore.getInstance().getMovie(movieId);
			int rentPerDay = movie.getMovieType().getRentalCost();
			this.rentalCost = rentPerDay * noOfDaysRented;
			int taxPercent = movie.getMovieType().getTaxPercent();
			this.tax = (this.rentalCost * taxPercent) / 100;
			this.totalCost = this.rentalCost + this.tax;
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
}
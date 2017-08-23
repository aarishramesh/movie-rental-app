package com.rental.app.model;

import lombok.Data;

/**
 *                                   Table "public.rental"
   Column   |            Type             |                      Modifiers                      
------------+-----------------------------+-----------------------------------------------------
 id         | integer                     | not null default nextval('rental_id_seq'::regclass)
 customerid | integer                      | not null
 movieid    | integer                      | not null
 starttime  | timestamp without time zone | not null
 endtime    | timestamp without time zone | not null
 invoiceid  | integer                      | 
Indexes:
    "rental_pkey" PRIMARY KEY, btree (id)
Foreign-key constraints:
    "rental_customerid_fkey" FOREIGN KEY (customerid) REFERENCES customer(id)
    "rental_invoiceid_fkey" FOREIGN KEY (invoiceid) REFERENCES invoice(id)
    "rental_movieid_fkey" FOREIGN KEY (movieid) REFERENCES movie(id)
Referenced by:
    TABLE "invoice" CONSTRAINT "invoice_rentalid_fkey" FOREIGN KEY (rentalid) REFERENCES rental(id)

 * 
 * @author aarishramesh
 *
 */
@Data
public class Rental {
	private int id;
	private int customerId;
	private int movieId;
	private long startTime;
	private long endTime;
	private Invoice invoice;
	
	public Rental(int customerId, int movieId, long startTime, long endTime) {
		this.customerId = customerId;
		this.movieId = movieId;
		this.startTime = startTime;
		this.endTime = endTime;
	}
}
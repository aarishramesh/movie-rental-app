package com.rental.app.model.request;

import lombok.Data;

@Data
public class RentalRequestBody {
	private String startDate;
	private String endDate;
	private int movieId;
	private int customerId;
}

package com.rental.app.model.request;

import lombok.Data;

@Data
public class CustomerRequestBody {
	private String name;
	private int age;
	private String emailId;
	private String password;
	private String city;
	private String state;
	private String country;
	private int contactNumber;
	private String billingAddress;
}

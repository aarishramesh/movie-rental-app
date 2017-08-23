package com.rental.app.model;

import java.util.List;

import lombok.Data;

/**
 *                          Table "public.customer"
     Column     |  Type   |                       Modifiers                       
----------------+---------+-------------------------------------------------------
 id             | integer | not null default nextval('customer_id_seq'::regclass)
 name           | text    | not null
 age            | integer | not null
 emailid        | text    | not null
 password       | text    | not null
 city           | text    | not null
 state          | text    | not null
 country        | text    | not null
 contactnumber  | integer | not null
 billingaddress | text    | not null
 rewardpoints   | integer  | default 0
Indexes:
    "customer_pkey" PRIMARY KEY, btree (id)
Referenced by:
    TABLE "rental" CONSTRAINT "rental_customerid_fkey" FOREIGN KEY (customerid) REFERENCES customer(id)

 * 
 * @author aarishramesh
 *
 */
@Data
public class Customer {
	private int id;
	private String name;
	private int age;
	private String emailId;
	private String password;
	private String city;
	private String state;
	private String country;
	private int contactNumber;
	private String billingAddress;
	private int rewardPoints;
	private List<Movie> wishList;
	private List<Rental> customerRentals;
	
	@Data
	public static class CustomerBuilder {
		private int id;
		private String name;
		private int age;
		private String emailId;
		private String password;
		private String city;
		private String state;
		private String country;
		private int contactNumber;
		private String billingAddress;
		private int rewardPoints;
		private List<Movie> wishList;
		private List<Rental> customerRentals;
		
		public CustomerBuilder setId(int id) {
			this.id = id;
			return this;
		}
		
		public CustomerBuilder setName(String name) {
			this.name = name;
			return this;
		}
		
		public CustomerBuilder setAge(int age) {
			this.age = age;
			return this;
		}
		
		public CustomerBuilder setEmailId(String emailId) {
			this.emailId = emailId;
			return this;
		}
		
		public CustomerBuilder setPassword(String password) {
			this.password = password;
			return this;
		}
		
		public CustomerBuilder setCity(String city) {
			this.city = city;
			return this;
		}
		
		public CustomerBuilder setState(String state) {
			this.state = state;
			return this;
		}
		
		public CustomerBuilder setCountry(String country) {
			this.country = country;
			return this;
		}
		
		public CustomerBuilder setContactNumber(int contactNumber) {
			this.contactNumber = contactNumber;
			return this;
		}
		
		public CustomerBuilder setBillingAddress(String billingAddress) {
			this.billingAddress = billingAddress;
			return this;
		}
		
		public CustomerBuilder setRewardPoints(int rewardPoints) {
			this.rewardPoints = rewardPoints;
			return this;
		}
		
		public CustomerBuilder setWishlist(List<Movie> wishList) {
			this.wishList = wishList;
			return this;
		}
			
		public CustomerBuilder setCustomerRentals(List<Rental> customerRentals) {
			this.customerRentals = customerRentals;
			return this;
		}
		
		public Customer build() {
			Customer customer = new Customer();
			customer.setAge(this.age);
			customer.setName(name);
			customer.setEmailId(emailId);
			customer.setPassword(password);
			customer.setCity(city);
			customer.setState(state);
			customer.setCountry(country);
			customer.setContactNumber(contactNumber);
			customer.setBillingAddress(billingAddress);
			customer.setRewardPoints(rewardPoints);
			customer.setWishList(wishList);
			customer.setCustomerRentals(customerRentals);
			return customer;
		}
	}
}

//create table customer (id serial primary key, name text not null, age int not null, emailid text not null, password text not null, city text not null, state text not null, country text not null,  contactnumber int not null, billingaddress text not null, rewardpoints bigint default 0);
package com.rental.app.service;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.put;

import java.util.Timer;
import java.util.TimerTask;

import com.google.gson.Gson;
import com.rental.app.handler.MovieRentalHandler;
import com.rental.app.model.request.CustomerRequestBody;
import com.rental.app.model.request.MovieRequestBody;
import com.rental.app.model.request.RentalRequestBody;
import com.rental.app.model.response.ApiResponse;
import com.rental.app.task.InvoiceGenerationTask;

public class MovieRentalService {
	public static void main(String[] args) {
		port(7777);
		
		TimerTask task = new InvoiceGenerationTask();
		Timer timer = new Timer(true);
		timer.scheduleAtFixedRate(task, 0, InvoiceGenerationTask.TASK_INTERVAL);
		
		get ("movie-rental/app/ping", (request, response) -> {
			return "pong";
		});
		
		get("movie-rental/app/movie/:id", (request, response) -> {
			response.type("application/json");
			String customerIdStr = request.queryParams("customerId");
			int customerId = Integer.parseInt(customerIdStr);
			String movieIdStr = request.params(":id");
			int movieId = Integer.parseInt(movieIdStr);
			ApiResponse apiResponse = MovieRentalHandler.getInstance().getMovieForId(customerId, movieId);
			Gson gson = new Gson ();
			return gson.toJson(apiResponse);
		});
		
		post ( "movie-rental/app/movie", (request, response) -> {
			response.type("application/json");
			MovieRequestBody requestBody = new Gson().fromJson (request.body (), MovieRequestBody.class);
			ApiResponse apiResponse = MovieRentalHandler.getInstance().storeMovie(requestBody);
			Gson gson = new Gson ();
			return gson.toJson(apiResponse);
		});
		
		put("movie-rental/app/movie/:id", (request, response) -> {
			response.type("application/json");
			String movieIdStr = request.params(":id");
			int movieId = Integer.parseInt(movieIdStr);
			MovieRequestBody requestBody = new Gson().fromJson (request.body (), MovieRequestBody.class);
			ApiResponse apiResponse = MovieRentalHandler.getInstance().updateMovie(movieId, requestBody);
			Gson gson = new Gson ();
			return gson.toJson(apiResponse);
		});
		
		delete("movie-rental/app/movie/:id", (request, response) -> {
			response.type("application/json");
			String movieIdStr = request.params(":id");
			int movieId = Integer.parseInt(movieIdStr);
			ApiResponse apiResponse = MovieRentalHandler.getInstance().deleteMovie(movieId);
			Gson gson = new Gson ();
			return gson.toJson(apiResponse);
		});
		
		get("movie-rental/app/movie/genre/:genre", (request, response) -> {
			response.type("application/json");
			String genreIdStr = request.params(":genre");
			int genreId = Integer.parseInt(genreIdStr);
			String customerIdStr = request.queryParams("customerId");
			int customerId = Integer.parseInt(customerIdStr);
			int offset = Integer.parseInt(request.queryParams("offset"));
			int limit = Integer.parseInt(request.queryParams("limit"));
			ApiResponse apiResponse = MovieRentalHandler.getInstance().getMoviesByGenre(customerId, genreId, offset, limit);
			Gson gson = new Gson ();
			return gson.toJson(apiResponse);
		});
		
		get("movie-rental/app/movie/language/:language", (request, response) -> {
			response.type("application/json");
			String languageIdStr = request.params(":language");
			int languageId = Integer.parseInt(languageIdStr);
			String customerIdStr = request.queryParams("customerId");
			int customerId = Integer.parseInt(customerIdStr);
			int offset = Integer.parseInt(request.queryParams("offset"));
			int limit = Integer.parseInt(request.queryParams("limit"));
			ApiResponse apiResponse = MovieRentalHandler.getInstance().getMoviesByLanguage(customerId, languageId, offset, limit);
			Gson gson = new Gson ();
			return gson.toJson(apiResponse);
		});
		
		// Endpoints relating to Rental action
		
		post("movie-rental/app/movie/:id/rent", (request, response) -> {
			response.type("application/json");
			String movieIdStr = request.params(":id");
			int movieId = Integer.parseInt(movieIdStr);
			String customerIdStr = request.queryParams("customerId");
			int customerId = Integer.parseInt(customerIdStr);
			RentalRequestBody requestBody = new Gson().fromJson (request.body (), RentalRequestBody.class);
			requestBody.setCustomerId(customerId);
			requestBody.setMovieId(movieId);
			ApiResponse apiResponse = MovieRentalHandler.getInstance().rentMovie(requestBody);
			Gson gson = new Gson ();
			return gson.toJson(apiResponse);
		});
				
		// Endpoints relating to Customer
		
		get("movie-rental/app/customer/signin", (request, response) -> {
			response.type("application/json");
			String emailId = request.queryParams("emailid");
			String password = request.queryParams("password");
			ApiResponse apiResponse = MovieRentalHandler.getInstance().checkUserCredentialsValid(emailId, password);
			Gson gson = new Gson ();
			return gson.toJson(apiResponse);
		});
		
		get("movie-rental/app/customer/:id", (request, response) -> {
			response.type("application/json");
			String customerIdStr = request.queryParams("customerId");
			int customerId = Integer.parseInt(customerIdStr);
			ApiResponse apiResponse = MovieRentalHandler.getInstance().getCustomer(customerId);
			Gson gson = new Gson ();
			return gson.toJson(apiResponse);
		});
		
		post("movie-rental/app/customer", (request, response) -> {
			response.type("application/json");
			CustomerRequestBody requestBody = new Gson().fromJson (request.body (), CustomerRequestBody.class);
			ApiResponse apiResponse = MovieRentalHandler.getInstance().addCustomer(requestBody);
			Gson gson = new Gson ();
			return gson.toJson(apiResponse);
		});
		
		put("movie-rental/app/customer/:id", (request, response) -> {
			CustomerRequestBody requestBody = new Gson().fromJson (request.body (), CustomerRequestBody.class);
			response.type("application/json");
			ApiResponse apiResponse = MovieRentalHandler.getInstance().updateCustomer(requestBody);
			Gson gson = new Gson ();
			return gson.toJson(apiResponse);
		});
		
		put("movie-rental/app/invoice/:id/pay", (request, response) -> {
			response.type("application/json");
			String customerIdStr = request.queryParams("customerId");
			int customerId = Integer.parseInt(customerIdStr);
			String invoiceIdStr = request.params(":id");
			int invoiceId = Integer.parseInt(invoiceIdStr);
			ApiResponse apiResponse = MovieRentalHandler.getInstance().payInvoiceForCustomer(customerId, invoiceId);
			Gson gson = new Gson ();
			return gson.toJson(apiResponse);
		});
		
		delete("movie-rental/app/customer/:id", (request, response) -> {
			response.type("application/json");
			String customerIdStr = request.params("customerId");
			int customerId = Integer.parseInt(customerIdStr);
			ApiResponse apiResponse = MovieRentalHandler.getInstance().deleteCustomer(customerId);
			Gson gson = new Gson ();
			return gson.toJson(apiResponse);
		});
		
	}
}

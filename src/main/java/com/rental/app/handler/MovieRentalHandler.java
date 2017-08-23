package com.rental.app.handler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.rental.app.dao.CustomerStore;
import com.rental.app.dao.InvoiceStore;
import com.rental.app.dao.MovieStore;
import com.rental.app.dao.RentalStore;
import com.rental.app.model.Customer;
import com.rental.app.model.Invoice;
import com.rental.app.model.Movie;
import com.rental.app.model.Movie.Genre;
import com.rental.app.model.Movie.Language;
import com.rental.app.model.Movie.MovieType;
import com.rental.app.model.Rental;
import com.rental.app.model.request.CustomerRequestBody;
import com.rental.app.model.request.MovieRequestBody;
import com.rental.app.model.request.RentalRequestBody;
import com.rental.app.model.response.ApiResponse;
import com.rental.app.model.response.Error;
import com.rental.app.model.response.Link;
import com.rental.app.util.MovieRentalUtil;

public class MovieRentalHandler {
	private static final MovieRentalHandler INSTANCE = new MovieRentalHandler();

	public static MovieRentalHandler getInstance() {
		return INSTANCE;
	}

	/**
	 * TODO - Password encryption
	 * Custom error codes have been used
	 * 
	 * @param emailId
	 * @param password
	 * @return
	 */
	public ApiResponse checkUserCredentialsValid(String emailId, String password) {
		ApiResponse response = new ApiResponse();
		try {
			boolean userValid = MovieRentalUtil.userCredentialsValid(emailId, password);
			if (userValid) {
				response.setData("signin success");
			} else {
				Error error = new Error("412", "User Invalid");
				response.setError(error);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			Error error = new Error("500", "Internal Server error");
			response.setError(error);
		}
		return response;
	}

	public ApiResponse getMovieForId(int customerId, int movieId) {
		ApiResponse response = new ApiResponse();
		try {
			boolean userValid = MovieRentalUtil.userValid(customerId);
			if (userValid) {
				Movie movie = MovieStore.getInstance().getMovie(movieId);
				if (movie != null) {
					response.setData(movie);
				} else {
					Error error = new Error("404", "Movie not found");
					response.setError(error);
				}
			} else {
				Error error = new Error("412", "User Invalid");
				response.setError(error);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			Error error = new Error("500", "Internal Server error");
			response.setError(error);
		}
		return response;
	}

	public ApiResponse getMoviesByGenre(int customerId, int genreId, int offset, int limit) {
		ApiResponse response = new ApiResponse();
		try {
			boolean userValid = MovieRentalUtil.userValid(customerId);
			if (userValid) {
				List<Movie> moviesByGenre = MovieStore.getInstance().getMoviesByGenre(genreId
						, offset, limit);
				response.setData(moviesByGenre);
				response.setLink(new Link(offset));
			} else {
				Error error = new Error("412", "User Invalid");
				response.setError(error);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			Error error = new Error("500", "Internal Server error");
			response.setError(error);
		}
		return response;
	}

	public ApiResponse getMoviesByLanguage(int customerId, int languageId, int offset, int limit) {
		ApiResponse response = new ApiResponse();
		try {
			boolean userValid = MovieRentalUtil.userValid(customerId);
			if (userValid) {
				List<Movie> moviesByLanguage = MovieStore.getInstance().getMoviesByLanguage(languageId
						, offset, limit);
				response.setData(moviesByLanguage);
				response.setLink(new Link(offset));
			} else {
				Error error = new Error("412", "User Invalid");
				response.setError(error);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			Error error = new Error("500", "Internal Server error");
			response.setError(error);
		}
		return response;
	}

	public ApiResponse storeMovie(MovieRequestBody requestBody) {
		ApiResponse response = new ApiResponse();
		try {
			Genre genre = Genre.getGenreById(requestBody.getGenre());
			Language language = Language.getLanguageById(requestBody.getLanguage());
			MovieType movieType = MovieType.getMovieTypeById(requestBody.getMovieType());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = sdf.parse(requestBody.getReleaseDate());
			Movie movie = new Movie.MovieBuilder().setName(requestBody.getName())
					.setReleaseDate(date.getTime()).setCast(requestBody.getCast())
					.setMovieType(movieType).setLanguage(language).setGenre(genre).build();
			MovieStore.getInstance().addMovie(movie);
			response.setData(movie);
		} catch (Exception ex) {
			ex.printStackTrace();
			Error error = new Error("500", "Internal Server error");
			response.setError(error);
		}
		return response;
	}

	public ApiResponse updateMovie(int movieId, MovieRequestBody requestBody) {
		ApiResponse response = new ApiResponse();
		try {
			Movie movie = MovieStore.getInstance().getMovie(movieId);
			if (movie != null) {
				Genre genre = Genre.getGenreById(requestBody.getGenre());
				Language language = Language.getLanguageById(requestBody.getLanguage());
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date date = sdf.parse(requestBody.getReleaseDate());
				movie.setCast(requestBody.getCast());
				movie.setGenre(genre);
				movie.setLanguage(language);
				movie.setReleaseDate(date.getTime());
				MovieStore.getInstance().updateMovie(movie);
				response.setData(movie);
			} else {
				Error error = new Error("404", "Movie not found");
				response.setError(error);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			Error error = new Error("500", "Internal Server error");
			response.setError(error);
		}
		return response;
	}

	public ApiResponse deleteMovie(int movieId) {
		ApiResponse response = new ApiResponse();
		try {
			boolean movieExists = MovieStore.getInstance().movieExists(movieId);
			if  (movieExists) {
				MovieStore.getInstance().deleteMovie(movieId);
				response.setData(movieId);
			} else {
				Error error = new Error("404", "Movie not found");
				response.setError(error);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			Error error = new Error("500", "Internal Server error");
			response.setError(error);
		}
		return response;
	}

	public ApiResponse rentMovie(RentalRequestBody rentalBody) {
		ApiResponse response = new ApiResponse();
		try {
			// Check if user has paid all the invoices
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date startDate = sdf.parse(rentalBody.getStartDate());
			Date endDate = sdf.parse(rentalBody.getEndDate());

			Rental rental = new Rental(rentalBody.getCustomerId(), rentalBody.getMovieId()
					, startDate.getTime(), endDate.getTime());
			RentalStore.getInstance().rentMovie(rental);
		} catch (Exception ex) {
			ex.printStackTrace();
			Error error = new Error("500", "Internal Server error");
			response.setError(error);
		}
		return response;
	}

	public ApiResponse addCustomer(CustomerRequestBody customerBody) {
		ApiResponse response = new ApiResponse();
		try {
			Customer customer = new Customer.CustomerBuilder().setAge(customerBody.getAge()).setName(customerBody.getName())
					.setEmailId(customerBody.getEmailId()).setPassword(customerBody.getPassword()).setCity(customerBody.getCity())
					.setState(customerBody.getState()).setCountry(customerBody.getCountry()).setContactNumber(customerBody.getContactNumber())
					.setBillingAddress(customerBody.getBillingAddress()).build();
			CustomerStore.getInstance().addCustomer(customer);
			response.setData(customer);
		} catch (Exception ex) {
			ex.printStackTrace();
			Error error = new Error("500", "Internal Server error");
			response.setError(error);
		}
		return response;
	}

	public ApiResponse updateCustomer(CustomerRequestBody customerBody) {
		ApiResponse response = new ApiResponse();
		try {
			Customer customer = new Customer.CustomerBuilder().setAge(customerBody.getAge()).setName(customerBody.getName())
					.setEmailId(customerBody.getEmailId()).setPassword(customerBody.getPassword()).setCity(customerBody.getCity())
					.setState(customerBody.getState()).setCountry(customerBody.getCountry()).setContactNumber(customerBody.getContactNumber())
					.setBillingAddress(customerBody.getBillingAddress()).build();
			CustomerStore.getInstance().updateCustomer(customer);
			response.setData(customer);
		} catch (Exception ex) {
			ex.printStackTrace();
			Error error = new Error("500", "Internal Server error");
			response.setError(error);
		}
		return response;
	}

	public ApiResponse deleteCustomer(int customerId) {
		ApiResponse response = new ApiResponse();
		try {
			boolean userValid = MovieRentalUtil.userValid(customerId);
			if (userValid) {
				CustomerStore.getInstance().deleteCustomer(customerId);
			} else {
				Error error = new Error("412", "User Invalid");
				response.setError(error);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			Error error = new Error("500", "Internal Server error");
			response.setError(error);
		}
		return response;
	}

	public ApiResponse getCustomer(int customerId) {
		ApiResponse response = new ApiResponse();
		try {
			boolean userValid = MovieRentalUtil.userValid(customerId);
			if (userValid) {
				Customer customer = CustomerStore.getInstance().getCustomer(customerId);
				List<Rental> customerRentals = RentalStore.getInstance().getRentalsForCustomer(customerId);
				customer.setCustomerRentals(customerRentals);
				for (Rental rental : customerRentals) {
					Invoice invoice = InvoiceStore.getInstance().getInvoiceForRentalId(rental.getId());
					rental.setInvoice(invoice);
				}
				response.setData(customer);
			} else {
				Error error = new Error("412", "User Invalid");
				response.setError(error);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			Error error = new Error("500", "Internal Server error");
			response.setError(error);
		}
		return response;
	}

	public ApiResponse payInvoiceForCustomer(int customerId, int invoiceId) {
		ApiResponse response = new ApiResponse();
		try {
			boolean userValid = MovieRentalUtil.userValid(customerId);
			if (userValid) {
				boolean invoiceValid = MovieRentalUtil.invoiceValid(invoiceId);
				if (invoiceValid) {
					Invoice invoice = InvoiceStore.getInstance().payCustomerInvoice(invoiceId);
					response.setData(invoice);
				} else {
					Error error = new Error("404", "Invalid invoice");
					response.setError(error);
				}
			} else {
				Error error = new Error("412", "User Invalid");
				response.setError(error);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			Error error = new Error("500", "Internal Server error");
			response.setError(error);
		}
		return response;
	}
}

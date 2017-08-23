package com.rental.app.model.request;

import com.rental.app.model.Movie.Cast;

import lombok.Data;

@Data
public class MovieRequestBody {
	private String name;
	private int language;
	private String releaseDate;
	private int genre;
	private int movieType;
	private Cast cast;
}
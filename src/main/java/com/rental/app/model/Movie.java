package com.rental.app.model;

import lombok.Data;

/**
 * Movie table schema
 *   Column    |            Type             |                     Modifiers                      
-------------+-----------------------------+----------------------------------------------------
 id          | integer                     | not null default nextval('movie_id_seq'::regclass)
 name        | text                        | not null
 language    | integer                     | not null
 releasedate | timestamp without time zone | 
 genre       | integer                     | not null
 movietype   | integer                     | not null
Indexes:
    "movie_pkey" PRIMARY KEY, btree (id)
Referenced by:
    TABLE "casts" CONSTRAINT "casts_movieid_fkey" FOREIGN KEY (movieid) REFERENCES movie(id)
    TABLE "rental" CONSTRAINT "rental_movieid_fkey" FOREIGN KEY (movieid) REFERENCES movie(id)

 * @author aarishramesh
 *
 */
@Data
public class Movie {
	private int id;
	private String name;
	private Language language;
	private long releaseDate;
	private Genre genre;
	private MovieType movieType;
	private Cast cast;

	@Data
	public static class MovieBuilder {
		private int id;
		private String name;
		private Language language;
		private long releaseDate;
		private Genre genre;
		private MovieType movieType;
		private Cast cast;
		
		public static MovieBuilder getInstance() {
			return new MovieBuilder();
		}
		
		public MovieBuilder setName(String name) {
			this.name = name;
			return this;
		}
		
		public MovieBuilder setLanguage(Language language) {
			this.language = language;
			return this;
		}
		
		public MovieBuilder setReleaseDate(long releaseDate) {
			this.releaseDate = releaseDate;
			return this;
		}
		
		public MovieBuilder setGenre(Genre genre) {
			this.genre = genre;
			return this;
		}
		
		public MovieBuilder setMovieType(MovieType movieType) {
			this.movieType = movieType;
			return this;
		}
		
		public MovieBuilder setCast(Cast cast) {
			this.cast = cast;
			return this;
		}
		
		public Movie build() {
			Movie movie = new Movie();
			movie.setId(id);
			movie.setLanguage(language);
			movie.setName(name);
			movie.setReleaseDate(releaseDate);
			movie.setGenre(genre);
			return movie;
		}
	}
	
	public static enum Genre {		
		TV(1), ActionAndAdventure(2), Comedy(3), Documentary(4), Drama(5)
		, Horror(6), Independent(7), SciFi(8), Fantasy(9), Romantic(10), 
		Thriller(11), Children(12), Others(13);
	
		private int type;

		
		private Genre(int type) {
			this.type = type;
		}
		
		public int getType() {
			return this.type;
		}
		
		public static Genre getGenreById(int type) {
			for (Genre genre : Genre.values()) {
				if (genre.getType() == type) {
					return genre;
				}
			}
			return null;
		}
 	}
	
	public static enum Language {
		English(1), Tamil(2), Hindi(3), Telugu(3), Kannada(4), French(6), German(7);
		
		private int language;
		
		private Language(int language) {
			this.language = language;
		}
		
		public int getType() {
			return this.language;
		}
		
		public static Language getLanguageById(int id) {
			for (Language language : Language.values()) {
				if (language.getType() == id) {
					return language;
				}
			}
			return null;
		}
	}
	
	public static enum MovieType {
		Latest(1, 100, 28), New(2, 80, 18), Old(3, 60, 12), VeryOld(4, 40, 5);
		
		private int type;
		private int rentalCostPerDay;
		private int taxPercent;
		
		private MovieType(int type, int rentalCostPerDay, int taxPercent) {
			this.type = type;
			this.rentalCostPerDay = rentalCostPerDay;
			this.taxPercent = taxPercent;
		}
		
		public int getRentalCost() {
			return this.rentalCostPerDay;
		}
		
		public int getType() {
			return this.type;
		}
		
		public int getTaxPercent() {
			return this.taxPercent;
		}
		
		public static MovieType getMovieTypeById(int id) {
			for (MovieType movieType : MovieType.values()) {
				if (movieType.getType() == id) {
					return movieType;
				}
			}
			return null;
		}
	}
	
	@Data
	public static class Cast {
		private String director;
		private String producer;
		private String hero;
		private String heroine;
		private String musicDirector;
		private String cinematographer;
		private String dop;
		private String editor;
	}

}
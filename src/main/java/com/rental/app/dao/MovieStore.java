package com.rental.app.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.rental.app.db.PostgreSQLJDBC;
import com.rental.app.model.Movie;
import com.rental.app.model.Movie.Cast;
import com.rental.app.model.Movie.Genre;
import com.rental.app.model.Movie.Language;
import com.rental.app.model.Movie.MovieType;

/**
 *
 * DAO layer class for CRUD operations on movie table
 * 
 * @author aarishramesh
 *
 */
public class MovieStore {
	private static final MovieStore INSTANCE = new MovieStore();

	public static MovieStore getInstance() {
		return INSTANCE;
	}

	public Movie addMovie(Movie movie) throws SQLException {
		PreparedStatement pstmt = null;
		Connection connection = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append ("INSERT INTO movie VALUES (DEFAULT, ?, ?, ?, ?, ?);");
			connection = PostgreSQLJDBC.getInstance().connect();

			pstmt = connection.prepareStatement(sql.toString());
			pstmt.setString(1, movie.getName());
			pstmt.setInt(2, movie.getLanguage().getType());
			pstmt.setDate(3, new Date(movie.getReleaseDate()));
			pstmt.setInt(4, movie.getGenre().getType());
			pstmt.setInt(5, movie.getMovieType().getType());
			pstmt.executeUpdate();
			Movie addedMovie = getMovie(movie.getName(), new Date(movie.getReleaseDate()));
			addCast(addedMovie.getId(), movie.getCast());
		} catch(Exception e){
			throw new SQLException(e);
		} finally{
			if (pstmt != null) pstmt.close();
			if (connection != null) connection.close();
		}
		return movie;
	}

	private void addCast(int movieId, Cast cast) throws SQLException {
		PreparedStatement pstmt = null;
		Connection connection = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append ("INSERT INTO casts VALUES (?, ?, ?, ?, ?, ?, ?, ?);");
			connection = PostgreSQLJDBC.getInstance().connect();

			pstmt = connection.prepareStatement(sql.toString());
			pstmt.setInt(1, movieId);
			pstmt.setString(2, cast.getDirector());
			pstmt.setString(3, cast.getProducer());
			pstmt.setString(4, cast.getHero());
			pstmt.setString(5, cast.getHeroine());
			pstmt.setString(6, cast.getMusicDirector());
			pstmt.setString(7, cast.getDop());
			pstmt.setString(8, cast.getEditor());
			pstmt.executeUpdate();
		} catch(Exception e){
			throw new SQLException(e);
		} finally{
			if (pstmt != null) pstmt.close();
			if (connection != null) connection.close();
		}
	}
	
	public Movie updateMovie(Movie movie) throws SQLException {
		PreparedStatement pstmt = null;
		Connection connection = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append ("update movie set name = ?, language = ?, releasedate = ?, genre = ?"
					+ " where id = ?");
			connection = PostgreSQLJDBC.getInstance().connect();

			pstmt = connection.prepareStatement(sql.toString());
			pstmt.setString(1, movie.getName());
			pstmt.setInt(2, movie.getLanguage().getType());
			pstmt.setDate(3, new Date(movie.getReleaseDate()));
			pstmt.setInt(4, movie.getGenre().getType());
			pstmt.setInt(5, movie.getId());
			pstmt.executeUpdate();
			updateCast(movie);
		} catch(Exception e){
			throw new SQLException(e);
		} finally{
			if (pstmt != null) pstmt.close();
			if (connection != null) connection.close();
		}
		movie = getMovie(movie.getId());
		return movie;
	}
	
	private Movie updateCast(Movie movie) throws SQLException {
		PreparedStatement pstmt = null;
		Connection connection = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append ("update casts set director = ?, producer = ?, hero = ?, heroine = ?"
					+ ", musicdirector = ?, dop = ?, editor = ? where movieid = ?");
			connection = PostgreSQLJDBC.getInstance().connect();

			Cast cast = movie.getCast();
			pstmt = connection.prepareStatement(sql.toString());
			pstmt.setString(1, cast.getDirector());
			pstmt.setString(2, cast.getProducer());
			pstmt.setString(3, cast.getHero());
			pstmt.setString(4, cast.getHeroine());
			pstmt.setString(5, cast.getMusicDirector());
			pstmt.setString(6, cast.getDop());
			pstmt.setString(7, cast.getEditor());
			pstmt.setInt(8, movie.getId());
			pstmt.executeUpdate();
		} catch(Exception e){
			throw new SQLException(e);
		} finally{
			if (pstmt != null) pstmt.close();
			if (connection != null) connection.close();
		}
		return movie;
	}
	
	public void deleteMovie(int movieId) throws SQLException {
		PreparedStatement pstmt = null;
		Connection connection = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append ("delete from movie where id = ?");
			connection = PostgreSQLJDBC.getInstance().connect();
			pstmt = connection.prepareStatement(sql.toString());
			pstmt.setInt(1, movieId);
			pstmt.executeUpdate();
		} catch(Exception e){
			throw new SQLException(e);
		} finally{
			if (pstmt != null) pstmt.close();
			if (connection != null) connection.close();
		}
	}
	
	public Movie getMovie(String name, Date releaseDate) throws SQLException {
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		Connection connection = null;
		Movie movie = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append ("select * from movie where name = ? and releasedate = ?");
			connection = PostgreSQLJDBC.getInstance().connect();

			pstmt = connection.prepareStatement(sql.toString());
			pstmt.setString(1, name);
			pstmt.setDate(2, releaseDate);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				movie = new Movie();
				movie.setId(rs.getInt("id"));
				movie.setName(rs.getString("name"));
				movie.setLanguage(Language.getLanguageById(rs.getInt("language")));
				Date date = rs.getDate("releasedate");
				movie.setReleaseDate(date.getTime());
				movie.setGenre(Genre.getGenreById(rs.getInt("genre")));
				movie.setMovieType(MovieType.getMovieTypeById(rs.getInt("movietype")));
			}
		} catch(Exception e) {
			throw new SQLException(e);
		} finally {
			if (pstmt != null) pstmt.close();
			if (connection != null) connection.close();
		}
		return movie;
	}
	
	public boolean movieExists(int id) throws SQLException {
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		Connection connection = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append ("select * from movie where id = ?");
			connection = PostgreSQLJDBC.getInstance().connect();

			pstmt = connection.prepareStatement(sql.toString());
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				return true;
			}
		} catch(Exception e) {
			throw new SQLException(e);
		} finally {
			if (pstmt != null) pstmt.close();
			if (connection != null) connection.close();
		}
		return false;
	}
	
	public Movie getMovie(int id) throws SQLException {
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		Connection connection = null;
		Movie movie = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append ("select * from movie where id = ?");
			connection = PostgreSQLJDBC.getInstance().connect();

			pstmt = connection.prepareStatement(sql.toString());
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				movie = new Movie();
				movie.setId(id);
				movie.setName(rs.getString("name"));
				movie.setLanguage(Language.getLanguageById(rs.getInt("language")));
				Date date = rs.getDate("releasedate");
				movie.setReleaseDate(date.getTime());
				movie.setGenre(Genre.getGenreById(rs.getInt("genre")));
				movie.setMovieType(MovieType.getMovieTypeById(rs.getInt("movietype")));
			}
			Cast cast = getCastForMovie(id);
			movie.setCast(cast);
		} catch(Exception e) {
			throw new SQLException(e);
		} finally {
			if (pstmt != null) pstmt.close();
			if (connection != null) connection.close();
		}
		return movie;
	}

	public List<Movie> getMoviesByGenre(int genreId, int offset, int limit) throws SQLException {
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		Connection connection = null;
		List<Movie> moviesList = new ArrayList<Movie>();
		try {
			StringBuilder sql = new StringBuilder();
			sql.append ("select * from movie where genre = ? limit ? offset ?");
			connection = PostgreSQLJDBC.getInstance().connect();

			pstmt = connection.prepareStatement(sql.toString());
			pstmt.setInt(1, genreId);
			pstmt.setInt(2, limit);
			pstmt.setInt(3, offset);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				Movie movie = new Movie();
				int movieId = rs.getInt("id");
				movie.setId(movieId);
				movie.setName(rs.getString("name"));
				movie.setLanguage(Language.getLanguageById(rs.getInt("language")));
				Date date = rs.getDate("releasedate");
				movie.setReleaseDate(date.getTime());
				movie.setGenre(Genre.getGenreById(rs.getInt("genre")));
				Cast cast = getCastForMovie(movieId);
				movie.setCast(cast);
				moviesList.add(movie);
			}
		} catch(Exception e) {
			throw new SQLException(e);
		} finally {
			if (pstmt != null) pstmt.close();
			if (connection != null) connection.close();
		}
		return moviesList;
	}
	
	public List<Movie> getMoviesByLanguage(int languageId, int offset, int limit) throws SQLException {
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		Connection connection = null;
		List<Movie> moviesList = new ArrayList<Movie>();
		try {
			StringBuilder sql = new StringBuilder();
			sql.append ("select * from movie where language = ? limit ? offset ?");
			connection = PostgreSQLJDBC.getInstance().connect();

			pstmt = connection.prepareStatement(sql.toString());
			pstmt.setInt(1, languageId);
			pstmt.setInt(2, limit);
			pstmt.setInt(3, offset);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				Movie movie = new Movie();
				int movieId = rs.getInt("id");
				movie.setId(movieId);
				movie.setName(rs.getString("name"));
				movie.setLanguage(Language.getLanguageById(rs.getInt("language")));
				Date date = rs.getDate("releasedate");
				movie.setReleaseDate(date.getTime());
				movie.setGenre(Genre.getGenreById(rs.getInt("genre")));
				Cast cast = getCastForMovie(movieId);
				movie.setCast(cast);
				moviesList.add(movie);
			}
		} catch(Exception e) {
			throw new SQLException(e);
		} finally {
			if (pstmt != null) pstmt.close();
			if (connection != null) connection.close();
		}
		return moviesList;
	}
	
	private Cast getCastForMovie(int id) throws SQLException {
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		Connection connection = null;
		Cast cast = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append ("select * from casts where movieid = ?");
			connection = PostgreSQLJDBC.getInstance().connect();

			pstmt = connection.prepareStatement(sql.toString());
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				cast = new Cast();
				cast.setDirector(rs.getString("director"));
				cast.setProducer(rs.getString("producer"));
				cast.setHero(rs.getString("hero"));
				cast.setHeroine(rs.getString("heroine"));
				cast.setCinematographer(rs.getString("cinematographer"));
				cast.setMusicDirector(rs.getString("musicdirector"));
				cast.setDop(rs.getString("dop"));
				cast.setEditor("editor");
			}
		} catch(Exception e){
			throw new SQLException(e);
		} finally{
			if (pstmt != null) pstmt.close();
			if (connection != null) connection.close();
		}
		return cast;
	}
}

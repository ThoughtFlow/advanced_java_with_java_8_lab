package lab06;

import java.util.List;
import java.util.Set;

public interface MovieDb {

	/**
	 * Adds a movie to the database with the given categories, name and year released.
	 * 
	 * @param categories The set of categories for the new movie.
	 * @param name The name of the movie.
	 * @param yearReleased The year of release
	 */
	void add(Set<Category> categories, String name, Integer yearReleased);

	/**
	 * Adds a movie to the database with the given category, name and year released.
	 * 
	 * @param category The category for the new movie
	 * @param name The name of the movie.
	 * @param yearReleased The year of release
	 */
	void add(Category category, String name, Integer yearReleased);

	/**
	 * Searches for the given movie title and returns as a Movie record.
	 * 
	 * @param name The name of the movie to search.
	 * @return The found movie or null if not found.
	 */
	Movie findByName(String name);

	/**
	 * Searches by category and returns the list of movies for the given category.
	 * 
	 * @param category The category name to search.
	 * @return The list of movies matching the category or an empty list.
	 */
	List<String> findByCategory(Category category);

	/**
	 * Deletes the movie with the given name. 
	 * 
	 * @param name The name of the movie to delete.
	 * @return True if found and deleted - false otherwise.
	 */
	boolean delete(String name);
}
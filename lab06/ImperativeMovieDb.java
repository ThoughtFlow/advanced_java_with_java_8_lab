package lab06;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ImperativeMovieDb implements MovieDb {

	private final Map<Category, List<Movie>> database = new HashMap<>();

	@Override
	public void add(Set<Category> categories, String name, Integer yearReleased) {

		Movie movieToAdd = new Movie(categories, name, yearReleased);

		for (Category nextCategory : categories) {
			List<Movie> movies = database.get(nextCategory);
			if (movies == null) {
				movies = new LinkedList<Movie>();
				database.put(nextCategory, movies);
			}
			movies.add(movieToAdd);
		}
	}

	@Override
	public void add(Category category, String name, Integer yearReleased) {

		Set<Category> categories = new HashSet<>();
		categories.add(category);
		add(categories, name, yearReleased);
	}

	@Override
	public Movie findByName(String name) {
		Movie foundMovie = null;
		Iterator<List<Movie>> iterator = database.values().iterator();
		while (iterator.hasNext() && foundMovie == null) {
			List<Movie> nextList = iterator.next();

			for (Movie nextMovie : nextList) {
				if (nextMovie.getName().equals(name)) {
					foundMovie = nextMovie;
					break;
				}
			}
		}

		return foundMovie;
	}
	
	@Override
	public List<String> findByCategory(Category category) {
		List<Movie> movies = database.containsKey(category) ? database.get(category) : Collections.emptyList();
		List<String> movieTitles = new ArrayList<>();
		
		for (Movie next : movies)
		{
			movieTitles.add(next.getName());
		}
		
		return movieTitles;
	}

	@Override
	public boolean delete(String name) {
		Movie foundMovie = null;
		Iterator<List<Movie>> iterator = database.values().iterator();
		while (iterator.hasNext()) {
			List<Movie> nextList = iterator.next();

			for (Movie nextMovie : nextList) {
				if (nextMovie.getName().equals(name)) {
					nextList.remove(nextMovie);
					foundMovie = nextMovie;
				}
			}
		}

		return foundMovie != null;
	}
}

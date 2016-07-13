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
import java.util.function.Predicate;

public class FunctionalMovieDb implements MovieDb {

	private final Map<Category, List<Movie>> database = new HashMap<>();

	@Override
	public void add(Set<Category> categories, String name, Integer yearReleased) {

		Movie movieToAdd = new Movie(categories, name, yearReleased);
		
		// Can use this method...
//		categories.forEach(category -> database.computeIfAbsent(category, k -> new LinkedList<Movie>()));
		
		// ...Or this method
//		categories.forEach(category -> database.compute(category, (k, v) -> {v.add(movieToAdd); return v;}));
		
		// ...Or this method
		List<Movie> newMovieList = new LinkedList<Movie>();
		newMovieList.add(movieToAdd);
		categories.forEach(category -> database.merge(category, newMovieList, (t1, t2) -> {t1.add(movieToAdd); return t1;}));
	}

	@Override
	public void add(Category category, String name, Integer yearReleased) {

		Set<Category> categories = new HashSet<>();
		categories.add(category);
		add(categories, name, yearReleased);
	}

	@Override
	public Movie find(String name) {
		// Can't refactor because foundMovie is immutable within a lambda
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
	public List<String> getByCategory(Category category) {
		// Refactored functionally
		List<Movie> movies = database.getOrDefault(category, Collections.emptyList());

		// Refactored functionally (loophole exploited)
		List<String> movieTitles = new ArrayList<>();
		movies.forEach(next -> movieTitles.add(next.getName()));
		
		return movieTitles;
	}

	@Override
	public boolean delete(String name) {

		Predicate<Movie> p = movie -> movie.getName().equals(name);
		// Refactored functionally
		database.values().forEach(list -> 
			list.removeIf(p)
		);

		// Can't support this with lists
		return false;
	}
}

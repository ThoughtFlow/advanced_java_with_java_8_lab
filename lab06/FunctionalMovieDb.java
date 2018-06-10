package lab06;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class FunctionalMovieDb implements MovieDb {

	private final Map<Category, List<Movie>> database = new HashMap<>();

	@Override
	public void add(Set<Category> categories, String name, Integer yearReleased) {

		Movie movieToAdd = new Movie(categories, name, yearReleased);
		
		// Can use this method...
//		categories.forEach(category -> database.computeIfAbsent(category, k -> new LinkedList<Movie>()));
//		categories.forEach(category -> database.compute(category, (k, v) -> {v.add(movieToAdd); return v;}));

		// ...Or this method
		categories.forEach(category -> {
			database.computeIfAbsent(category, k -> new LinkedList<Movie>());
			database.compute(category, (k, v) -> {v.add(movieToAdd); return v;});
		});
	}
	
	@Override
	public void add(Category category, String name, Integer yearReleased) {

		Set<Category> categories = new HashSet<>();
		categories.add(category);
		add(categories, name, yearReleased);
	}
	
	@Override
	public Movie findByName(String name) {
		// Can't use a simple Movie class because it will be immutable within lambda. 
		// An AtomicReference is used to store the found movie. Can also just create your own holder class.
		AtomicReference<Movie> foundMovie = new AtomicReference<>();

		// Iterate over all the movies in the Map and store it foundMovie if found.
		Consumer<Movie> consumer = nextTitle -> {if (nextTitle.getName().equals(name)) foundMovie.set(nextTitle);};
		database.values().forEach(nextList -> nextList.forEach(consumer));

		return foundMovie.get();
	}
	
	@Override
	public List<String> findByCategory(Category category) {
		// Refactored functionally
		List<Movie> movies = database.getOrDefault(category, Collections.emptyList());

		// Refactored functionally (loophole exploited)
		List<String> movieTitles = new ArrayList<>();
		movies.forEach(next -> movieTitles.add(next.getName()));
		
		return movieTitles;
	}

	@Override
	public boolean delete(String name) {

		final boolean existedBeforeDelete = findByName(name) != null;
		Predicate<Movie> p = movie -> movie.getName().equals(name);
		
		// Refactored functionally
		database.values().forEach(list -> list.removeIf(p));

		// Can't support this with lists so we need to save the flag to determine whether or not it existed before delete
		return existedBeforeDelete;
	}
}
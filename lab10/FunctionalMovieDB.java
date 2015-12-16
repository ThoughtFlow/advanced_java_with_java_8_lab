package lab10;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FunctionalMovieDB {

	private final Map<Category, List<Movie>> database = new HashMap<>();

	public void add(Set<Category> categories, String name, Integer yearReleased) {

		Movie movieToAdd = new Movie(categories, name, yearReleased);
		
//		categories.forEach(category -> database.computeIfAbsent(category, k -> new LinkedList<Movie>()));
//		categories.forEach(category -> database.compute(category, (k, v) -> {v.add(movieToAdd); return v;}));
		List<Movie> newMovieList = new LinkedList<Movie>();
		newMovieList.add(movieToAdd);
		categories.forEach(category -> database.merge(category, newMovieList, (t1, t2) -> {t1.add(movieToAdd); return t1;}));
	}

	public void add(Category category, String name, Integer yearReleased) {

		Set<Category> categories = new HashSet<>();
		categories.add(category);
		add(categories, name, yearReleased);
	}

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
	
	public List<String> getByCategory(Category category) {
		// Refactored functionally
		List<Movie> movies = database.getOrDefault(category, Collections.emptyList());

		// Refactored functionally (loophole exploited)
		List<String> movieTitles = new ArrayList<>();
		movies.forEach(next -> movieTitles.add(next.getName()));
		
		return movieTitles;
	}

	public boolean delete(String name) {

		// Refactored functionally
		database.values().forEach(list -> 
			list.removeIf(movie -> movie.getName().equals(name))
		);

		// Can't support this with lists
		return false;
	}

	public static void main(String... args) {

		FunctionalMovieDB movies = new FunctionalMovieDB();
		movies.add(Category.COMEDY, "The Simpsons", 2015);
		movies.add(Category.DRAMA, "Goodfellas", 1990);
		movies.add(Category.HORROR, "Jurrasic Parc", 1993);
		movies.add(Category.HORROR, "Friday the 13th", 1980);
		Set<Category> romanticComedy = new HashSet<>(Arrays.asList(Category.COMEDY, Category.ROMANTIC));
		movies.add(romanticComedy, "Sleepless in Seattle", 1994);		
		
		System.out.println("The Simpsons was released in " + movies.find("The Simpsons").getReleaseYear());

		System.out.println("Funny movies: ");
		for (String next : movies.getByCategory(Category.COMEDY))
		{
			System.out.println(" - " + next);
		}
		movies.delete("Sleepless in Seattle");
		
		System.out.println("Horror movies: ");
		for (String next : movies.getByCategory(Category.HORROR))
		{
			System.out.println("- " + next);
		}
	}
}

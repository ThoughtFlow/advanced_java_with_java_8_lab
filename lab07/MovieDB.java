package lab07;

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

public class MovieDB {

	private final Map<Category, List<Movie>> database = new HashMap<>();

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

	public void add(Category category, String name, Integer yearReleased) {

		Set<Category> categories = new HashSet<>();
		categories.add(category);
		add(categories, name, yearReleased);
	}

	public Movie find(String name) {
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
		List<Movie> movies = database.containsKey(category) ? database.get(category) : Collections.emptyList();
		List<String> movieTitles = new ArrayList<>();
		
		for (Movie next : movies)
		{
			movieTitles.add(next.getName());
		}
		
		return movieTitles;
	}

	public boolean delete(String name) {
		Movie foundMovie = null;
		Iterator<List<Movie>> iterator = database.values().iterator();
		while (iterator.hasNext() && foundMovie == null) {
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

	public static void main(String... args) {

		MovieDB movies = new MovieDB();
		movies.add(Category.COMEDY, "The Simpsons", 2007);
		movies.add(Category.DRAMA, "Goodfellas", 1990);
		movies.add(Category.HORROR, "Silence of the Lambs", 1991);
		Set<Category> romanticComedy = new HashSet<>(Arrays.asList(Category.COMEDY, Category.ROMANTIC));
		movies.add(romanticComedy, "When Harry Met Sally", 1989);		

		System.out.println(movies.find("The Simpsons").getReleaseYear());
		for (String next : movies.getByCategory(Category.COMEDY))
		{
			System.out.println(next);
		}
		movies.delete("When Harry Met Sally");
		
		for (String next : movies.getByCategory(Category.COMEDY))
		{
			System.out.println(next);
		}
	}
}

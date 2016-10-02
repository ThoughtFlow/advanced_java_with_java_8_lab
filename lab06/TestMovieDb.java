package lab06;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class TestMovieDb {

	public static void runTest(MovieDb movies) {
		System.out.println("==================");
		System.out.println("Testing: " + movies.getClass());
		movies.add(Category.COMEDY, "The Simpsons", 2007);
		movies.add(Category.DRAMA, "Goodfellas", 1990);
		movies.add(Category.HORROR, "Silence of the Lambs", 1991);
		Set<Category> romanticComedy = new HashSet<>(Arrays.asList(Category.COMEDY, Category.ROMANTIC));
		movies.add(romanticComedy, "When Harry Met Sally", 1989);		

		System.out.println("Year of release for The Simpsons: " + movies.find("The Simpsons").getReleaseYear());
		
		System.out.println("Fetching movies within Comedy category: ");
		for (String next : movies.getByCategory(Category.COMEDY))
		{
			System.out.println("  " + next);
		}
		
		System.out.println("Deleting \"When Harry Met Sally\". Succeeded: " + movies.delete("When Harry Met Sally"));
		
		System.out.println("Fetching movies within Comedy category again: ");
		for (String next : movies.getByCategory(Category.COMEDY))
		{
			System.out.println(" " + next);
		}	
	}
	
	public static void main(String... args) {
		runTest(new ImperativeMovieDb());
		runTest(new FunctionalMovieDb());
	}
}

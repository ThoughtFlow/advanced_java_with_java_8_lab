package lab06;

import java.util.Set;

public class Movie{

	private final Set<Category> categories;
	private final String name;
	private final Integer releaseYear;
	
	public Movie(Set<Category> categories, String name, Integer releaseYear) {
		this.categories = categories;
		this.name = name;
		this.releaseYear = releaseYear;
	}

	public Set<Category> getCategories() {
		return categories;
	}

	public String getName() {
		return name;
	}

	public Integer getReleaseYear() {
		return releaseYear;
	}
}

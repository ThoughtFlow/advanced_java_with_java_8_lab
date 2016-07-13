package lock_moviedb;

import java.util.Set;

public class Movie{

	private final Set<Category> categories;
	private final String name;
	private final Integer releaseYear;
	
	public Movie(Set<Category> categories, String name, Integer releaseYear) {
		super();
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((releaseYear == null) ? 0 : releaseYear.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Movie other = (Movie) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (releaseYear == null) {
			if (other.releaseYear != null)
				return false;
		} else if (!releaseYear.equals(other.releaseYear))
			return false;
		return true;
	}
}

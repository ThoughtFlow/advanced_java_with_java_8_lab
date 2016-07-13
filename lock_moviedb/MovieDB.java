package lock_moviedb;

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
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MovieDB {

	private final Map<Category, List<Movie>> database = new HashMap<>();
	private final List<AccessRecord> records = Collections.synchronizedList(new LinkedList<>());

	private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

	public void add(Set<Category> categories, String name, Integer yearReleased) {

		Lock lock = readWriteLock.writeLock();
		lock.lock();
		AccessRecord startRecord = recordAccessStart("Add");

		try {
			Movie movieToAdd = new Movie(categories, name, yearReleased);

			for (Category nextCategory : categories) {
				List<Movie> movies = database.get(nextCategory);
				if (movies == null) {
					movies = new LinkedList<Movie>();
					database.put(nextCategory, movies);
				}
				if (movies.contains(movieToAdd) == false)
				{
				   movies.add(movieToAdd);
				}
			}
		} finally {
			recordAccessEnd(startRecord);
			lock.unlock();
		}
	}

	public void add(Category category, String name, Integer yearReleased) {

		Set<Category> categories = new HashSet<>();
		categories.add(category);
		add(categories, name, yearReleased);
	}

	public Movie find(String name) {

		Lock lock = readWriteLock.readLock();
		lock.lock();
		AccessRecord startRecord = recordAccessStart("Find");

		try {
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
		} finally {
			recordAccessEnd(startRecord);
			lock.unlock();
		}
	}

	public List<String> getByCategory(Category category) {

		Lock lock = readWriteLock.readLock();
		lock.lock();
		AccessRecord startRecord = recordAccessStart("Find category");

		try {
			List<Movie> movies = database.containsKey(category) ? database.get(category) : Collections.emptyList();
			List<String> movieTitles = new ArrayList<>();

			for (Movie next : movies) {
				movieTitles.add(next.getName());
			}

			return movieTitles;
		} finally {
			recordAccessEnd(startRecord);
			lock.unlock();
		}
	}

	public boolean delete(String name) {

		Lock lock = readWriteLock.writeLock();
		lock.lock();
		AccessRecord startRecord = recordAccessStart("delete");

		try {
			Movie foundMovie = null;
			Iterator<List<Movie>> iterator = database.values().iterator();
			while (iterator.hasNext() && foundMovie == null) {
				List<Movie> nextList = iterator.next();

				Iterator<Movie> movieIterator = nextList.iterator();
				while (movieIterator.hasNext()) {
					Movie nextMovie = movieIterator.next();
					if (nextMovie.getName().equals(name)) {
						foundMovie = nextMovie;
						movieIterator.remove();
					}
				}
			}

			return foundMovie != null;
		} finally {
			recordAccessEnd(startRecord);
			lock.unlock();
		}
	}

	public List<AccessRecord> getRecords() {
		return records;
	}

	public static void main(String... args) {

		MovieDB movies = new MovieDB();
		movies.add(Category.COMEDY, "The Simpsons", 2015);
		movies.add(Category.COMEDY, "The Simpsons", 2015);
		movies.add(Category.DRAMA, "Goodfellas", 1990);
		movies.add(Category.HORROR, "Jurrasic Parc", 1993);
		Set<Category> romanticComedy = new HashSet<>(Arrays.asList(Category.COMEDY, Category.ROMANTIC));
		movies.add(romanticComedy, "Sleepless in Seattle", 1994);

		System.out.println(movies.find("The Simpsons").getReleaseYear());
		for (String next : movies.getByCategory(Category.COMEDY)) {
			System.out.println(next);
		}
		movies.delete("Sleepless in Seattle");

		for (String next : movies.getByCategory(Category.HORROR)) {
			System.out.println(next);
		}
	}
	
	private AccessRecord recordAccessStart(String method)
	{
		return AccessRecord.recordStart(Thread.currentThread().getName(), method);
	}
	
	private void recordAccessEnd(AccessRecord startRecord)
	{
	   records.add(AccessRecord.recordEnd(startRecord));
	}
}

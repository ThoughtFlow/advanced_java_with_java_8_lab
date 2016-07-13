package multithreaded_streamio;

public enum Category {

	SPORTS,
	POLITICS,
	HISTORY;
	
	public static Category toCategory(String string) throws IllegalArgumentException {
		Category foundCategory;
		
		switch (string.toUpperCase()) {
		   case "SPORTS" : foundCategory = SPORTS;
		   				   break;
		   	
		   case "POLITICS" : foundCategory = POLITICS;
		                     break;
		                     
		   case "HISTORY" : foundCategory = HISTORY;
		                    break;
		                    
		   default : throw new IllegalArgumentException("Unknown category: " + string);
		}
		   
		return foundCategory;
	}
}

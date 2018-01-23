package lab11;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class Util {

	public static BufferedReader getReader(String stringedUrl) throws IOException {
		
	       URL url = new URL(stringedUrl);
		   return new BufferedReader(new InputStreamReader(url.openConnection().getInputStream()));
	}
}

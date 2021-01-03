package question4;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;


public class Runner {

	public static void main(String[] args) throws Exception {
		GroupBy1.main(null);
		Files.copy(Paths.get("input-all/Lieux.csv"), Paths.get("input-output-4-1/Lieux.csv"), StandardCopyOption.REPLACE_EXISTING );
		Join2.main(null);
		GroupBy3.main(null);
		
		
	}

}

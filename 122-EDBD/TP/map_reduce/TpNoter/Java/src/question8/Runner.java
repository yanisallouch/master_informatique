package question8;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;


public class Runner {

	public static void main(String[] args) throws Exception {
		GroupByLieuEtDate1.main(null);
		Files.copy(Paths.get("input-all/Lieux.csv"), Paths.get("input-output-8-1/Lieux.csv"), StandardCopyOption.REPLACE_EXISTING);
		JoinLieu2.main(null);
		Files.copy(Paths.get("input-all/Dates.csv"), Paths.get("input-output-8-2/Dates.csv"), StandardCopyOption.REPLACE_EXISTING);
		JoinDate3.main(null);
		GroupBy4.main(null);
		GroupBy5.main(null);
		
		
	}

}

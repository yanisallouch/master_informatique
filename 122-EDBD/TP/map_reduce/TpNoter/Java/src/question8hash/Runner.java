package question8hash;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;


public class Runner {

	public static void main(String[] args) throws Exception {
		GroupByLieuEtDate1.main(null);
		Files.copy(Paths.get("input-all/Lieux.csv"), Paths.get("input-output-8hash-1/Lieux.csv"), StandardCopyOption.REPLACE_EXISTING);
		Files.copy(Paths.get("input-all/Dates.csv"), Paths.get("input-output-8hash-1/Dates.csv"), StandardCopyOption.REPLACE_EXISTING);
		HashJoinContinentSemaine2et3et4.main(null);
		GroupBy4.main(null);
		GroupBy5.main(null);
		
		
	}

}

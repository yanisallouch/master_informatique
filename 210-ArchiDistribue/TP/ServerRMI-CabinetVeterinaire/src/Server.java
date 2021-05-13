
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;

public class Server {
	
	public Server() {}

	@SuppressWarnings("unused")
	public static void main(String args[]) {

		try {
			CabinetVeterinaire objCabinet = new CabinetVeterinaireImplementation();
			Registry registry = LocateRegistry.createRegistry(1199);

			String unixPath = "/mnt/e/gitlab.com/master_informatique/210-ArchiDistribue/TP/ServerRMI-CabinetVeterinaire/src/Server.policy";
			String windowsPath = "E:\\gitlab.com\\master_informatique\\210-ArchiDistribue\\TP\\ServerRMI-CabinetVeterinaire\\src\\Server.policy";
			
			System.setProperty( "java.security.policy", windowsPath);
			SecurityManager securityManager = new SecurityManager();
        	System.setSecurityManager(securityManager);

			if (registry==null){
				System.err.println("RmiRegistry not found");
			}else{
				registry.bind("Cabinet", objCabinet);
				System.err.println("Server ready");
				// output (red) : Server ready
			}
		} catch (Exception e) {
			System.err.println("Server exception: " + e.toString());
			e.printStackTrace();
		}
	}
}
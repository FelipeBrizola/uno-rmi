import java.rmi.Naming;
import java.rmi.RemoteException;

public class Server {

	public static void main (String[] argv) {
		try {
			java.rmi.registry.LocateRegistry.createRegistry(1099);
			System.out.println("RMI registry ready.");
		} catch (RemoteException e) {
			System.out.println("RMI registry already running.");
		}
		
		try {
			Naming.rebind("Uno", new Uno ("Hello, game!"));
			System.out.println ("GameServer is ready.");
		} catch (Exception e) {
			System.out.println ("GameServer failed:");
			e.printStackTrace();
		}
	}
}

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

public class Client {

	private IUno game;
	private int playerId;
	
	public Client(int playerId, IUno game) {
		this.game = game;
		this.playerId = playerId;
	}
	
	public void runClient()  throws RemoteException, InterruptedException {
		
		int hasGame = game.hasGame(playerId);
		
		if (hasGame == 0)
			System.out.println("Aguardando partida...");

		// Aguarda ate partida iniciar
		while (hasGame == 0) {
			Thread.sleep(3000);
			hasGame = game.hasGame(playerId);
		}
		
		switch (hasGame) {
			case 1:
				System.out.println("Voce jogara contra: " + game.getOpponent(playerId));
				System.out.println("Voce comeca jogando e suas cartao sao: \n" + game.showCards(playerId));
				runGame();
				break;
			case 2:
				System.out.println("Voce jogara contra: " + game.getOpponent(playerId));
				System.out.println("Voce sera o segundo a jogar e suas cartao sao: \n" + game.showCards(playerId));
				break;
			case -2 :
				System.out.println("Tempo de espera esgotado.");
				break;
			case -1 :
				System.out.println("Ocorreu um erro ao consultar partida.");
				break;
			default:
				break;
		}
		
	}
	
	private void runGame() throws RemoteException, InterruptedException {
		
		while(true) {

			int isMyTurn = game.isMyTurn(this.playerId);
			
			switch (isMyTurn) {
				case -2:
					System.out.println("Erro: ainda nao ha 2 jogadores registrados na partida.");
					return;
				case -1 :
					System.out.println("Ocorreu um erro ao consultar partida.");
					return;
				case 0 :
					Thread.sleep(2000); // aguarda sua vez
					break;
				case 1 :
					int resultado = play();
					
					if (resultado == 2)
						System.out.println("Partida encerrada, voce demorou muito para jogar.");
					
					if (resultado == -3)
						System.out.println("Partida encerrada.");
					
					break;
				case 2 :
					System.out.println("VOCE VENCEU!!!");
					return;
				case 3 :
					System.out.println("VOCE PERDEU!");
					return;
				case 4 :
					System.out.println("Ocorreu um empate!");
					return;
				case 5 :
					System.out.println("VOCE VENCEU POR WO!!!");
					return;
				case 6 :
					System.out.println("VOCE PERDEU POR WO!");
					return;
				default:
					return;
			}
		
		}
		
	}	
	
	private int play() throws RemoteException {
		return game.playCard(this.playerId, 0, 0);
	}
	
	
	
	
	
	
}


import java.rmi.RemoteException;
import java.util.Scanner;

public class Client {

	private IUno unoGame;
	private int playerId;
	private Scanner scanner;

	public Client(int playerId, IUno unoGame) {
		this.unoGame = unoGame;
		this.playerId = playerId;
	}

	public void runClient(Scanner scanner) throws RemoteException, InterruptedException {

		this.scanner = scanner;

		try {

			int hasGame = unoGame.hasGame(playerId);

			if (hasGame == 0)
				System.out.println("Aguardando partida...");

			// Aguarda ate partida iniciar
			while (hasGame == 0) {
				Thread.sleep(3000);
				hasGame = unoGame.hasGame(playerId);
			}

			//System.out.println(hasGame);

			switch (hasGame) {
			case 1:
				System.out.println("Voce jogara contra: " + unoGame.getOpponent(playerId));
				System.out.println("Voce comeca jogando e suas cartao sao: \n" + unoGame.showCards(playerId));
				runGame();
				break;
			case 2:
				System.out.println("Voce jogara contra: " + unoGame.getOpponent(playerId));
				System.out.println("Voce sera o segundo a jogar e suas cartao sao: \n" + unoGame.showCards(playerId));
				runGame();
				break;
			case -2:
				System.out.println("Tempo de espera esgotado.");
				break;
			case -1:
				System.out.println("Ocorreu um erro ao consultar partida.");
				break;
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void runGame() throws RemoteException, InterruptedException {

		while (true) {

			int isMyTurn = unoGame.isMyTurn(this.playerId);

			System.out.println("IS MY TURN: " + isMyTurn);

			switch (isMyTurn) {
			case -2:
				System.out.println("Erro: ainda nao ha 2 jogadores registrados na partida.");
				return;
			case -1:
				System.out.println("Ocorreu um erro ao consultar partida.");
				return;
			case 0:
				Thread.sleep(2000); // aguarda sua vez
				break;
			case 1:
				int status = play();

				if(status == 0) {
					System.out.println("Jogada invalida. Tente novamente");
					status = play();
				}
				if (status == 1)
					System.out.println("Jogada ok");

				if (status == 2)
					System.out.println("Partida encerrada, voce demorou muito para jogar.");

				if (status == -3)
					System.out.println("Partida encerrada.");

				break;
			case 2:
				System.out.println("VOCE VENCEU!!!");
				return;
			case 3:
				System.out.println("VOCE PERDEU!");
				return;
			case 4:
				System.out.println("Ocorreu um empate!");
				return;
			case 5:
				System.out.println("VOCE VENCEU POR WO!!!");
				return;
			case 6:
				System.out.println("VOCE PERDEU POR WO!");
				return;
			default:
				return;
			}

		}

	}

	private int play() throws RemoteException {
		
		System.out.println("Carta da mesa: " +unoGame.getCardFromTable(playerId));
		
		System.out.println("Digite o id da carta que deseja usar: ");
		String selectedCardIndex = this.scanner.nextLine();

		unoGame.showCards(this.playerId);
		return unoGame.playCard(this.playerId, Integer.parseInt(selectedCardIndex), 0);
	}

}

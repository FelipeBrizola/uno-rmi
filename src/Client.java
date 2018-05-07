
import java.rmi.RemoteException;
import java.util.Scanner;

import javax.lang.model.util.ElementScanner6;

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

			// System.out.println(hasGame);

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

				if (status == 0) {
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

		System.out.println("Carta da mesa: " + unoGame.getCardFromTable(playerId));

		System.out.println("Para jogar uma carta digite o Id");
		System.out.println("Para comprar uma carta digite c");

		int index = -1;
		String option = this.scanner.nextLine();

		if (isInt(option))
			index = Integer.parseInt(option);

		unoGame.showCards(this.playerId);

		// jogada
		if (index == -1) {

			// erro ao comprar carta
			if (unoGame.getCardFromDeck(playerId) != 0)
				return 0;

			String[] cardsStr = unoGame.showCards(playerId).split("\n");
			System.out.println("Voce comprou a carta: " + cardsStr[cardsStr.length - 1]);
			System.out.println("Suas cartas agora sao: " + unoGame.showCards(playerId));

			System.out.println("Voce pode passar a vez ou jogar com a carta que acabou de comprar");
			System.out.println("Para passar a vez digite p. Para Jogar com a carta comprada digite j");
			option = this.scanner.nextLine();

			if (option.equals("j"))
				return unoGame.playCard(this.playerId, cardsStr.length - 1, 0);
			else if (option.equals("p"))
				return unoGame.playCard(this.playerId, -1, 0);
			else
				return 0;

		}

		else
			return unoGame.playCard(this.playerId, index, 0);
	}

	private boolean isInt(String number) {
		try {
			Integer.parseInt(number);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
}
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
				Thread.sleep(2000); // aguarda vez
				break;
			case 1:

				//System.out.println("Suas cartas sao: \n" + unoGame.showCards(playerId));

				int status = play();
				switch (status) {
				case -4:
					System.out.println("Nao é sua vez");
					break;
				case -3:
					System.out.println("Parametros invalidos");
					break;
				case -2:
					System.out.println("Partida nao iniciada");
					break;
				case -1:
					System.out.println("Jogador nao encontrado");
					break;
				case 0:
					System.out.println("XXX Jogada invalida. Tente novamente XXX");
					status = play();
					break;
				case 1:
					System.out.println("==> Jogada ok <==");
					break;

				default:
					break;
				}

				System.out.println("Suas cartas: \n " + unoGame.showCards(playerId));

				break;
			case 2:
				System.out.println("VOCE VENCEU!!!");
				System.out.println("Voce fez: " + unoGame.getOpponentScore(playerId));
				System.out.println("Seu adversario fez: " + unoGame.getScore(playerId));
				return;
			case 3:
				System.out.println("VOCE PERDEU!");
				System.out.println("Voce fez: " + unoGame.getOpponentScore(playerId) + " pontos");
				System.out.println("Seu adversario fez: " + unoGame.getScore(playerId) + " pontos");
				return;
			case 4:
				System.out.println("EMPATE!");
				System.out.println("Voce e seu adversario fizeram " + unoGame.getScore(playerId) + " pontos");
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

		int activeColor = unoGame.getActiveColor(playerId);

		if (activeColor == -1)
			System.out.println("Carta da mesa: " + unoGame.getCardFromTable(playerId));
		else
			System.out.println("Cor ativa na mesa: " + Helper.intColorToStrColor(activeColor));

		System.out.println("Para jogar uma carta digite o Id");
		System.out.println("Para comprar uma carta digite c");

		int index = -1;
		String option = this.scanner.nextLine();

		if (Helper.isInt(option))
			index = Integer.parseInt(option);

		unoGame.showCards(this.playerId);

		String[] cardsStr = unoGame.showCards(playerId).split("\n");

		if (index >= 0 && index <= unoGame.getNumberOfCards(playerId)) {

			// jogada com coringa
			if (cardsStr[index].contains("JOKER")) {
				option = Helper.playWithJoker(scanner);
				int colorCard = -1;
				if (Helper.isInt(option)) {
					colorCard = Integer.parseInt(option);
					return unoGame.playCard(this.playerId, index, colorCard);
				} else
					return 0;
				
			}
			// jogada normal
			return unoGame.playCard(this.playerId, index, -1);
		}

		// comprando carta
		else if (index == -1) {

			// erro ao comprar carta
			if (unoGame.getCardFromDeck(playerId) != 0)
				return 0;

			System.out.println("Voce comprou a carta: " + cardsStr[cardsStr.length - 1]);
			System.out.println("Para passar a vez digite p");
			System.out.println("Para Jogar com a carta comprada digite j");
			
			option = this.scanner.nextLine();

			// quando comprar e jogar. pode ter comprado coringa. entao selecionar cor ativa
			if (option.equals("j")) {

				// carta comprada é sempre a ultima do baralho
				if (cardsStr[cardsStr.length - 1].contains("JOKER")) {
					option = Helper.playWithJoker(scanner);
					int colorCard = -1;
					if (Helper.isInt(option)) {
						colorCard = Integer.parseInt(option);
						return unoGame.playCard(this.playerId, index, colorCard);
					} else
						return 0;
				}
				return unoGame.playCard(this.playerId, cardsStr.length - 1, -1);
			} else if (option.equals("p"))
				return unoGame.playCard(this.playerId, -1, -1);
			else
				return 0;

		}

		return 0;
	}
}
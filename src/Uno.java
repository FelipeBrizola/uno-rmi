import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Stack;

public class Uno extends UnicastRemoteObject implements IUno {

	private static int MAX_PLAYERS = 500;

	private static final long serialVersionUID = 1L;
	private String name;
	private ArrayList<Player> playersPool = new ArrayList<>();
	private ArrayList<Game> games = new ArrayList<>();

	private synchronized Game getGameByPlayerId(int playerId) {

		for (Game game : games)
			for (Player player : game.getPlayers())
				if (player.getId() == playerId)
					return game;

		return null;
	}

	public synchronized void removeClosedGames() {
		new Thread() {
			public void run() {
				try {
					while (true) {

						// 1 min p destruir partida
						Thread.sleep(60000);
						long now = System.currentTimeMillis();

						System.out.println("ROTINA PARA REMOVER JOGOS FINALIZADOS");

						for (int i = 0; i < games.size(); i += 1)
							if ((games.get(i).getStatus() == GameStatus.CLOSED)
									&& (now - games.get(i).getClosedTimer() > 20000)) {
								removePlayersFromPlayerPool();
								games.remove(i);
								System.out.println("JOGO REMOVIDO");
							}

					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	private void removePlayersFromPlayerPool() {
		for (int i = 0; i < playersPool.size(); i += 1) {
			for (Player player : games.get(i).getPlayers())
				if (player.getId() == playersPool.get(i).getId())
					playersPool.remove(i);
		}
	}

	private void removeGameByPlayerId(int playerId) {
		for (int i = 0; i < games.size(); i += 1) {
			if (games.get(i).getPlayerByPlayerId(playerId).getId() == playerId)
				games.remove(i);
		}

	}

	// Aloca jogador em alguma partida nao inicializada ou cria uma com ele
	// mesmo
	private synchronized Game allocatesPlayer(Player newPlayer) throws Exception {
		boolean wasAllocated = false;

		if (games.size() > 0) {
			for (Game game : games) {

				System.out.println("ROTINA DE ALOCACAO");
				System.out.println("PARAMETRO ENVIADO" + newPlayer.getId());

				// entra segundo jogador
				if (game.getStatus() == GameStatus.WAITING && game.getPlayers().size() == 1) {

					System.out.println("ADICIONANDO " + newPlayer.getId() + "A PARTIDA");
					game.addOpponent(newPlayer);
					wasAllocated = true;
					game.watchTurnTimer();
					break;
				}

			}

		}

		if (!wasAllocated) {
			System.out.println("NOVA PARTIDA: " + newPlayer.getId());
			Game game =  new Game(newPlayer);
			game.watchGameTimer();
			return game;
		}

		return null;
	}

	protected Uno(String name) throws RemoteException {
		this.name = name;
		removeClosedGames();
	}

	@Override
	public synchronized int registerPlayer(String playerName) throws RemoteException {

		if (playersPool.size() > MAX_PLAYERS)
			return -2;

		for (Player player : playersPool)
			if (player.getName().equals(playerName))
				return -1;

		// id do jogador sera o indice da lista
		Player newPlayer = new Player(playerName, playersPool.size());

		try {
			Game game = allocatesPlayer(newPlayer);
			if (game != null)
				games.add(game);

			System.out.println("QUANTIDADE DE JOGOS NO SERVIDOR " + games.size());

		} catch (Exception e) {
			e.printStackTrace();
		}

		playersPool.add(newPlayer);

		return newPlayer.getId();
	}

	@Override
	public int gameOver(int playerId) throws RemoteException {
		ArrayList<Player> playersFromGame = this.getGameByPlayerId(playerId).getPlayers();

		for (Player playerFromGame : playersFromGame)
			for (int i = 0; i < this.playersPool.size(); i += 1)
				if (this.playersPool.get(i).getId() == playerFromGame.getId())
					this.playersPool.remove(i);

		return 0;
	}

	@Override
	public synchronized int hasGame(int playerId) throws RemoteException {

		try {

			Game game = this.getGameByPlayerId(playerId);

			// acabou o tempo de espera para inicializar partida
			if (game != null && game.getStatus() == GameStatus.TIMEOUT) {
				removeGameByPlayerId(playerId);
				for (int i = 0; i < playersPool.size(); i += 1) {
					if (playersPool.get(i).getId() == playerId)
						playersPool.remove(i);
				}
				return -2;
			}

			// aguarda partida
			if (game != null && game.getPlayers().size() == 1)
				return 0;

			// decide qual jogador comeca jogando.
			if (game != null && game.getPlayers().size() == 2) {
				if (game.getPlayerByPlayerId(playerId).getId() < game.getOpponentByPlayerId(playerId).getId()) {
					game.getPlayerByPlayerId(playerId).setIsMyTurn(true);
					return 1;
				}
				game.getOpponentByPlayerId(playerId).setIsMyTurn(true);
				return 2;
			}

			return 0;

		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}

	}

	@Override
	public String getOpponent(int playerId) throws RemoteException {

		for (Player player : this.getGameByPlayerId(playerId).getPlayers()) {
			if (player.getId() != playerId)
				return player.getName();
		}

		return "";

	}

	@Override
	public int isMyTurn(int playerId) throws RemoteException {
		try {

			Game game = getGameByPlayerId(playerId);

			// playerId venceu por wo
			if (playerId == game.getWoPlayers()[0]) {
				game.setClosedTimer(System.currentTimeMillis());
				game.setStatus(GameStatus.CLOSED);
				return 6;
			}

			// playerId perdeu por wo
			if (playerId == game.getWoPlayers()[1]) {
				game.setClosedTimer(System.currentTimeMillis());
				game.setStatus(GameStatus.CLOSED);
				return 5;
			}

			// nao existem 2 jogadores na partida
			if (game.getPlayers().size() != 2)
				return -2;

			// vencedor
			if (this.getNumberOfCards(playerId) == 0) {
				game.setClosedTimer(System.currentTimeMillis());
				game.setStatus(GameStatus.CLOSED);
				return 2;
			}

			// perdedor
			else if (this.getNumberOfCardsFromOpponent(playerId) == 0) {
				game.setClosedTimer(System.currentTimeMillis());
				game.setStatus(GameStatus.CLOSED);
				return 3;
			}

			// jogadores ainda tem cartas, mas baralho de compra acabou
			else if (this.getNumberOfCards(playerId) > 0 && this.getNumberOfCardsFromOpponent(playerId) > 0
					&& this.getNumberOfCardsFromDeck(playerId) == 0) {
				game.setClosedTimer(System.currentTimeMillis());
				game.setStatus(GameStatus.CLOSED);
				return 4;
			}

			Player opponent = game.getOpponentByPlayerId(playerId);
			Player currentPlayer = game.getPlayerByPlayerId(playerId);

			// vez do oponente do playerId
			if (opponent.getIsMyTurn() && !currentPlayer.getIsMyTurn())
				return 0;

			// vez do playerId
			if (currentPlayer.getIsMyTurn() && !opponent.getIsMyTurn())
				return 1;

			return 0;

		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}

	}

	@Override // baralho de compra
	public int getNumberOfCardsFromDeck(int playerId) throws RemoteException {
		Game game = this.getGameByPlayerId(playerId);
		return game.getDeck().size();

	}

	@Override
	public int getNumberOfCards(int playerId) throws RemoteException {
		try {
			Player player = this.getGameByPlayerId(playerId).getPlayerByPlayerId(playerId);
			return player.getDeck().size();
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	@Override
	public int getNumberOfCardsFromOpponent(int playerId) throws RemoteException {
		try {
			Player player = this.getGameByPlayerId(playerId).getOpponentByPlayerId(playerId);
			return player.getDeck().size();
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	@Override
	public String showCards(int playerId) throws RemoteException {
		Game game = this.getGameByPlayerId(playerId);
		return game.getPlayerByPlayerId(playerId).showDeck();
	}

	@Override
	public int getActiveColor(int playerId) throws RemoteException {
		Game game = this.getGameByPlayerId(playerId);
		return game.getActiveColor();

	}

	@Override
	public int getScore(int playerId) throws RemoteException {

		Game game = getGameByPlayerId(playerId);

		if (game == null)
			return 2;

		Player player = game.getPlayerByPlayerId(playerId);

		if (player == null)
			return 1;

		if (game.getStatus() != GameStatus.CLOSED)
			return 3;

		return Helper.sumScore(player.getDeck());
	}

	@Override
	public String getCardFromTable(int playerId) throws RemoteException {
		Game game = this.getGameByPlayerId(playerId);
		Card card = game.getTableDeck().peek();

		return Helper.cardToString(card);

	}

	@Override
	public int getCardFromDeck(int playerId) throws RemoteException {
		// comprar carta
		// remove carta do baralho de compra
		// insere no baralho do playerId

		try {
			Game game = this.getGameByPlayerId(playerId);
			Player player = game.getPlayerByPlayerId(playerId);
			Card card = game.getDeck().pop();
			Stack<Card> playerDeck = player.getDeck();
			playerDeck.push(card);
			player.setDeck(playerDeck);
			return 0;
		} catch (Exception e) {
			return -1;
		}

	}

	@Override
	public int getOpponentScore(int playerId) throws RemoteException {
		Game game = getGameByPlayerId(playerId);

		if (game == null)
			return 2;

		Player player = game.getOpponentByPlayerId(playerId);

		if (player == null)
			return 1;

		if (game.getStatus() != GameStatus.CLOSED)
			return 3;

		return Helper.sumScore(player.getDeck());
	}

	@Override
	public int playCard(int playerId, int index, int cardColor) throws RemoteException {

		Game game = this.getGameByPlayerId(playerId);

		// set do time da jogada
		game.getPlayerByPlayerId(playerId).setTurnTime(System.currentTimeMillis());

		// troca a vez do jogador
		if (index == -1) {
			this.getGameByPlayerId(playerId).setTurnPlayer();
			return 1;
		}

		// algum jogador nao encontrado
		if (game.getPlayerByPlayerId(playerId) == null || game.getOpponentByPlayerId(playerId) == null)
			return -1;

		// parametros invalidos - indice do baralho
		if (index < -1 || index > this.getNumberOfCards(playerId))
			return -3;

		// parametros invalidos - cor inexistente
		if (cardColor < -2 || cardColor > 3)
			return -3;

		// nao é vez do jogador ainda
		if (!game.getPlayerByPlayerId(playerId).getIsMyTurn())
			return -4;

		Card tableCard = Helper.stringToCard(this.getCardFromTable(playerId));
		Card playedCard = this.getGameByPlayerId(playerId).getPlayerByPlayerId(playerId).getDeck().get(index);

		Player player = null;

		// seta cor ativa
		if (cardColor != -1)
			game.setActiveColor(cardColor);

		switch (this.match(playedCard, tableCard, playerId)) {
		// erro
		case -1:
			return 0;
		// jogada normal
		case 0:
			this.playCardFull(playerId, index, playedCard);
			this.getGameByPlayerId(playerId).setTurnPlayer();
			game.setActiveColor(-1);
			break;
		// pular e inverter
		case 1:
			this.playCardFull(playerId, index, playedCard);
			game.setActiveColor(-1);
			break;
		case 2:
			// +2
			// adversario compra 2 cartas e turno nao é trocado.
			this.playCardFull(playerId, index, playedCard);

			player = game.getOpponentByPlayerId(playerId);
			this.getCardFromDeck(player.getId());
			this.getCardFromDeck(player.getId());
			game.setActiveColor(-1);
			break;
		case 3:
			// coringa
			this.playCardFull(playerId, index, playedCard);
			this.getGameByPlayerId(playerId).setTurnPlayer();
			break;
		case 4:
			// coringa +4
			player = game.getOpponentByPlayerId(playerId);
			this.getCardFromDeck(player.getId());
			this.getCardFromDeck(player.getId());
			this.getCardFromDeck(player.getId());
			this.getCardFromDeck(player.getId());
			this.getGameByPlayerId(playerId).setTurnPlayer();
			break;

		default:
			break;
		}

		return 1;

	}

	// remove carta do deck do jogador e a coloca no topo da pilha de descarte
	private void playCardFull(int playerId, int cardIndex, Card playedCard) {
		Stack<Card> tableDeck = this.getGameByPlayerId(playerId).getTableDeck();
		tableDeck.push(playedCard);
		this.getGameByPlayerId(playerId).setTableDeck(tableDeck);
		this.getGameByPlayerId(playerId).getPlayerByPlayerId(playerId).getDeck().remove(cardIndex);
	}

	private int match(Card playedCard, Card tableCard, int playerId) {

		if ((playedCard.getColor() != null)
				&& this.getGameByPlayerId(playerId).getActiveColor() == playedCard.getColor().getValue())
			return 0;

		if (playedCard.getType() == TypeCard.JOKER)
			return 3;

		if (playedCard.getType() == TypeCard.JOKER_4)
			return 4;

		if (playedCard.getType() != null) {

			if (playedCard.getNumber() == -1 && ((tableCard.getColor() == playedCard.getColor())
					|| (tableCard.getType() == playedCard.getType()))) {
				switch (playedCard.getType()) {
				case MORE_2:
					return 2;

				case SKIP:
					return 1;
				case REVERSE:
					return 1;

				default:
					break;
				}
			}

			return -1;
		}

		if ((tableCard.getColor() == playedCard.getColor()) || (tableCard.getNumber() == playedCard.getNumber()))
			return 0;

		return -1;
	}

}

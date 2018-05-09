import java.awt.Window.Type;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Stack;

import javax.management.ValueExp;

public class Uno extends UnicastRemoteObject implements IUno {

	private static int MAX_PLAYERS = 2;

	private static final long serialVersionUID = 1L;
	private String name;
	private ArrayList<Player> playersPool = new ArrayList<>();
	private ArrayList<Game> games = new ArrayList<>();

	private Game getGameByPlayerId(int playerId) {

		for (Game game : games)
			for (Player player : game.getPlayers())
				if (player.getId() == playerId)
					return game;

		return null;
	}

	private Player getPlayerById(int playerId) {

		for (Player player : playersPool)
			if (player.getId() == playerId)
				return player;

		return null;
	}

	private int sumScore(Stack<Card> deck) {
		int sum = 0;

		for (Card card : deck) {

			if (card.getType() == TypeCard.JOKER || card.getType() == TypeCard.JOKER_4)
				sum += 50;
			else if (card.getType() == TypeCard.MORE_2 || card.getType() == TypeCard.SKIP
					|| card.getType() == TypeCard.REVERSE)
				sum += 20;
			else
				sum += card.getNumber();
		}

		return sum;
	}

	private void allocatesPlayer(Player newPlayer) throws Exception {
		// aloca jogador em alguma partida ou cria uma so com ele, por enquanto

		if (games.size() > 0) {
			for (Game game : games) {
				ArrayList<Player> playersOnGame = game.getPlayers();

				// entra segundo jogador
				if (game.getStatus() == GameStatus.WAITING && playersOnGame.size() == 1) {
					// todo: remover os 2 jogadores do playersPoll ?
					game.addOpponent(newPlayer);
					break;
				}

				// cria partida com 1 jogador
				games.add(new Game(newPlayer));

			}
		} else
			// cria partida com 1 jogador quando ainda nao existem partidas
			games.add(new Game(newPlayer));

	}

	private String cardToString(Card card) {

		if (card.getColor() != null && card.getNumber() != -1 && card.getType() == null)
			return card.getNumber() + "/" + card.getColor();

		if (card.getColor() != null && card.getNumber() == -1 && card.getType() != null)
			return card.getType() + "/" + card.getColor();

		if (card.getColor() == null && card.getNumber() == -1 && card.getType() != null)
			return card.getType() + "/*";

		return "";
	}

	private Card stringToCard(String cardStr) {

		String[] values = cardStr.split("/");

		// coringa
		if (cardStr.contains("*"))
			return new Card(null, TypeCard.valueOf(values[0]), -1);

		try {
			int number = Integer.parseInt(values[0]);
			return new Card(ColorCard.valueOf(values[1]), null, number);
		} catch (Exception e) {
			return new Card(ColorCard.valueOf(values[1]), TypeCard.valueOf(values[0]), -1);
		}

	}

	protected Uno(String name) throws RemoteException {
		this.name = name;
	}

	@Override
	public int registerPlayer(String playerName) throws RemoteException {

		if (playersPool.size() > MAX_PLAYERS)
			return -2;

		for (Player player : playersPool)
			if (player.getName().equals(playerName))
				return -1;

		Player newPlayer = new Player(playerName, playersPool.size());

		try {
			allocatesPlayer(newPlayer);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		playersPool.add(newPlayer);

		// id do jogador sera o indice da lista
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
	public int hasGame(int playerId) throws RemoteException {

		// validar tempo esgotado: -2
		// 1 playerId comeca jogando
		// id menor comeca

		try {

			Game game = this.getGameByPlayerId(playerId);

			if (game.getPlayers().size() == 2) {

				if (game.getPlayerByPlayerId(playerId).getId() < game.getOpponentByPlayerId(playerId).getId()) {
					game.getPlayerByPlayerId(playerId).setIsMyTurn(true);
					return 1;
				}
				game.getOpponentByPlayerId(playerId).setIsMyTurn(true);
				return 2;
			}

			return 0;

		} catch (Exception e) {
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

			if (game.getPlayers().size() != 2)
				return -2;

			// vencedor
			if (this.getNumberOfCards(playerId) == 0)
				return 2;

			// perdedor
			else if (this.getNumberOfCardsFromOpponent(playerId) == 0)
				return 3;

			// jogadores ainda tem cartas, mas baralho de compra acabou
			else if (this.getNumberOfCards(playerId) > 0 && this.getNumberOfCardsFromOpponent(playerId) > 0 && this.getNumberOfCardsFromDeck(playerId) == 0)
				return 4;

			Player opponent = game.getOpponentByPlayerId(playerId);
			Player currentPlayer = game.getPlayerByPlayerId(playerId);

			// vez do oponente
			if (opponent.getIsMyTurn() && !currentPlayer.getIsMyTurn())
				return 0;

			// minha vez
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

		Game game = this.getGameByPlayerId(playerId);

		if (game != null)
			for (Player player : game.getPlayers())
				if (player.getId() == playerId)
					player.getDeck().size();

		return -1;

	}

	@Override
	public int getNumberOfCardsFromOpponent(int playerId) throws RemoteException {

		Game game = this.getGameByPlayerId(playerId);

		if (game != null)
			for (Player player : game.getPlayers())
				if (player.getId() != playerId)
					player.getDeck().size();

		return -1;

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
		Game game = this.getGameByPlayerId(playerId);
		Player player = game.getPlayerByPlayerId(playerId);

		return this.sumScore(player.getDeck());
	}

	@Override
	public String getCardFromTable(int playerId) throws RemoteException {
		Game game = this.getGameByPlayerId(playerId);
		Card card = game.getTableDeck().peek();

		return this.cardToString(card);

	}

	@Override
	public int getCardFromDeck(int playerId) throws RemoteException {
		// comprar carta

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
		Game game = this.getGameByPlayerId(playerId);

		for (Player player : game.getPlayers())
			if (player.getId() != playerId)
				return this.sumScore(player.getDeck());

		return 0;
	}

	@Override
	public int playCard(int playerId, int index, int cardColor) throws RemoteException {
		// cor é quando jogador usou coring  e a cor a ser usada

		Game game = this.getGameByPlayerId(playerId);

		// troca a vez do jogador
		if (index == -1) {
			this.getGameByPlayerId(playerId).setTurnPlayer();
			return 1;
		}

		// algum jogador nao encontrado
		if(game.getPlayerByPlayerId(playerId) == null || game.getOpponentByPlayerId(playerId) == null)
			return -1;

		// parametros invalidos
		if (index < -1 && index > this.getNumberOfCards(playerId) - 1)
			return -3;

		// parametros invalidos
		if (cardColor < -2 && cardColor > 3)
			return -3;

		// so seta vez do jogador apos partida ter comecado
		//return -2

		// nao é vez do jogador ainda
		if (!game.getPlayerByPlayerId(playerId).getIsMyTurn())
			return -4;

		Card tableCard = this.stringToCard(this.getCardFromTable(playerId));
		Card playedCard = this.getGameByPlayerId(playerId).getPlayerByPlayerId(playerId).getDeck().get(index);

		Player player = null;

		switch (this.match(playedCard, tableCard, playerId)) {
		// erro
		case -1:
			game.setActiveColor(-1);
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
			game.setActiveColor(cardColor);
			break;
		case 4:
			// coringa +4
			player = game.getOpponentByPlayerId(playerId);
			this.getCardFromDeck(player.getId());
			this.getCardFromDeck(player.getId());
			this.getCardFromDeck(player.getId());
			this.getCardFromDeck(player.getId());
			this.getGameByPlayerId(playerId).setTurnPlayer();
			game.setActiveColor(cardColor);
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

		if (this.getGameByPlayerId(playerId).getActiveColor() == playedCard.getColor().getValue())
			return 0;

		if (playedCard.getType() == TypeCard.JOKER)
			return 3;

		if (playedCard.getType() == TypeCard.JOKER_4)
			return 4;

		if (playedCard.getType() != null) {

			if (playedCard.getNumber() == -1 && (tableCard.getColor() == playedCard.getColor() || tableCard.getType() == playedCard.getType())) {
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
	
			return 0;
		}
		
		if ((tableCard.getColor() == playedCard.getColor()) || (tableCard.getNumber() == playedCard.getNumber()))
			return 0;

		return -1;
	}

}

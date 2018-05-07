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

	private int colorFactory(ColorCard color) {

		switch (color) {
		case BLUE:
			return 0;
		case GREEN:
			return 2;
		case RED:
			return 3;
		case YELLOW:
			return 1;

		default:
			return -1;
		}
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
			//cria partida com 1 jogador quando ainda nao existem partidas
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

			if (this.getNumberOfCards(playerId) == 0)
				return 2;

			else if (this.getNumberOfCardsFromOpponent(playerId) == 0)
				return 3;

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
		Stack<Card> tableDeck = game.getTableDeck();

		return this.colorFactory(tableDeck.peek().getColor());

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
		// cor é quando jogador usou coring  isse a cor a ser usada

		// passa a vez
		if (index == -1) {
			this.getGameByPlayerId(playerId).setTurnPlayer();
			return 1;
		}


		Card tableCard = this.stringToCard(this.getCardFromTable(playerId));

		Card playedCard = this.getGameByPlayerId(playerId).getPlayerByPlayerId(playerId).getDeck().get(index);

		// valida jogada. remove carta do deck do jogador e a coloca no topo da pilha de
		// descarte
		if (tableCard.getColor() == playedCard.getColor() || tableCard.getNumber() == playedCard.getNumber()) {
			Stack<Card> tableDeck = this.getGameByPlayerId(playerId).getTableDeck();
			tableDeck.push(playedCard);
			this.getGameByPlayerId(playerId).setTableDeck(tableDeck);
			this.getGameByPlayerId(playerId).getPlayerByPlayerId(playerId).getDeck().remove(index);

			this.getGameByPlayerId(playerId).setTurnPlayer();

			return 1;
		}

		return 0;
	}

}

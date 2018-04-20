import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

public class Uno extends UnicastRemoteObject implements IUno {

	// baralho
	// mesa
	// 2 jogadores
	// lista global de jogadores
	
	private static final long serialVersionUID = 1L;
	private String name;
	private ArrayList<Player> players = new ArrayList<>();
	private ArrayList<Game> games = new ArrayList<>();
	
	private Game getGameByPlayerId(int playerId) {

		for (Game game : games) 
			for(Player player : game.getPlayers()) 
				if (player.getId() == playerId)
					return game;
		
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
		
		for(Card card : deck) {
			
			if (card.getType() == TypeCard.JOKER || card.getType() == TypeCard.JOKER_4)
				sum += 50;
			else if (card.getType() == TypeCard.MORE_2 || card.getType() == TypeCard.SKIP || card.getType() == TypeCard.REVERSE)
				sum += 20;
			else
				sum += card.getNumber();
		}
		
		return sum;
	}
	
	protected Uno(String name) throws RemoteException {
		this.name = name;
	}

	@Override
	public int registerPlayer(String playerName) throws RemoteException {
			
		for (Player player : players) {
			if (player.getName().equals(playerName)) 
				if (player.getIsPlaying())
					return -2;
			
				return -1;
		}
		
		Player newPlayer =  new Player(playerName, players.size());
		players.add(newPlayer);
		
		// id do jogador sera o indice da lista
		return newPlayer.getId();
	}

	@Override
	public int gameOver(int playerId) throws RemoteException {
		ArrayList<Player> playersFromGame = this.getGameByPlayerId(playerId).getPlayers();
		
		for(Player playerFromGame : playersFromGame) 
			for(int i = 0; i < this.players.size(); i+= 1)
				if (this.players.get(i).getId() == playerFromGame.getId())
					this.players.remove(i);
					
		return 0;
	}

	@Override
	public int hasGame(int playerId) throws RemoteException {
		// tempo esgotado = -2
		// erro -1
		// ainda nao ha partida = 0;
		
		// encontra jogador disponivel
		// valida quem comeca jogando
		for (Player player : players) {
			
			if (!player.getIsPlaying()) 
				if (players.get(playerId).getId() < player.getId())
					return 1;

				return 2;
		}
		
		// nao ha partida pq todos estao jogando
		return 0;
	}

	@Override
	public String getOpponent(int playerId) throws RemoteException {

		for(Player player: players) {
			if (!player.getIsPlaying() && player.getId() != playerId)
				return player.getName();
		}
		
		return "";
		
	}

	@Override
	public int isMyTurn(int playerId) throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
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
			for(Player player : game.getPlayers()) 
				if (player.getId() == playerId)
					player.getDeck().size();
			
		return -1;
		
	}

	@Override
	public int getNumberOfCardsFromOpponent(int playerId) throws RemoteException {
		
		Game game = this.getGameByPlayerId(playerId);
		
		if (game != null) 
			for(Player player : game.getPlayers()) 
				if (player.getId() != playerId)
					player.getDeck().size();
			
		return -1;
		
	}

	@Override
	public String showCards(int playerId) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
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
		return null;
		
	}

	@Override
	public int getCardFromDeck(int playerId) throws RemoteException {

		Game game = this.getGameByPlayerId(playerId);
		Player player = game.getPlayerByPlayerId(playerId);
		Card card = game.getDeck().pop();
		Stack<Card> playerDeck = player.getDeck();
		playerDeck.push(card);
		player.setDeck(playerDeck);
		return 0;
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
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public String hello() throws RemoteException {
		return "HELLO!!";
	}

}

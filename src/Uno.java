import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

public class Uno extends UnicastRemoteObject implements IUno {

	private static int MAX_PLAYERS = 2;
	
	private static final long serialVersionUID = 1L;
	private String name;
	private ArrayList<Player> playersPool = new ArrayList<>();
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
	
	private void allocatesPlayer(Player newPlayer) {
		// aloca jogador em alguma partida ou cria uma so com ele, por enquanto
		for (Game game : games) {
			ArrayList<Player> playersOnGame = game.getPlayers();
		
			if (game.getStatus() == GameStatus.WAITING && playersOnGame.size() == 1)
				game.addOpponent(newPlayer);
			else
				games.add(new Game(newPlayer));
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
		
		Player newPlayer =  new Player(playerName, playersPool.size());
		
		allocatesPlayer(newPlayer);
		
		
		
		playersPool.add(newPlayer);
		
		// id do jogador sera o indice da lista
		return newPlayer.getId();
	}

	@Override
	public int gameOver(int playerId) throws RemoteException {
		ArrayList<Player> playersFromGame = this.getGameByPlayerId(playerId).getPlayers();
		
		for(Player playerFromGame : playersFromGame) 
			for(int i = 0; i < this.playersPool.size(); i+= 1)
				if (this.playersPool.get(i).getId() == playerFromGame.getId())
					this.playersPool.remove(i);
					
		return 0;
	}

	@Override
	public int hasGame(int playerId) throws RemoteException {
		
		// validar tempo esgotado: -2
		
		try {				
			for (Player player : playersPool) {
				// econtra jogagor que nao seja eu mesmo.
				if (!player.getIsPlaying() && player.getId() != playerId) {
					
					// saber quem comeca jogando.
					if (playersPool.get(playerId).getId() < player.getId())
						return 1;
					return 2;
				} 
			}
		
			return 0;

		} catch (Exception e) {
			return -1;
		}
		
}

	@Override
	public String getOpponent(int playerId) throws RemoteException {

		for(Player player: playersPool) {
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
		return "SUAS CARTAS";
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

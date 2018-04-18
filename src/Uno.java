import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;

public class Uno extends UnicastRemoteObject implements IUno {

	// baralho
	// mesa
	// 2 jogadores
	// lista global de jogadores
	
	private static final long serialVersionUID = 1L;
	private String name;
	
	private ArrayList<Player> players = new ArrayList<>();
	
	
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
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int isMyTurn(int playerId) throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNumberOfCardsFromDeck(int playerId) throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNumberOfCards(int playerId) throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNumberOfCardsFromOpponent(int playerId) throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String showCards(int playerId) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getActiveColor(int playerId) throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getScore(int playerId) throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getCardFromTable(int playerId) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getCardFromDeck(int playerId) throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getOpponentScore(int playerId) throws RemoteException {
		// TODO Auto-generated method stub
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

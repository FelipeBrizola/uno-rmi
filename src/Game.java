import java.rmi.RemoteException;

public class Game implements IGame {

	@Override
	public int registerPlayer(String playerName) throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int gameOver(int playerId) throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int hasGame(int playerId) throws RemoteException {
		// TODO Auto-generated method stub
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

}

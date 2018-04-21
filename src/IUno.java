import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IUno extends Remote {
	
	/**
	 * registraJogador
	 * @param nome do usuário/jogador.
	 * @return  id (valor inteiro) do usuário (que corresponde a um número de identificação único para este usuário durante uma partida),
	 * ­1 se este usuário já está cadastrado ou
	 * 2 se o número máximo de jogadores tiver sido atingido.
	 */
	public int registerPlayer(String playerName) throws RemoteException;

	/**
	 * encerraPartida
	 * @param id do usuário (obtido através da chamada registraJogador).
	 * @return ­1 (erro), 0 (ok).
	 */
	public int gameOver(int playerId) throws RemoteException;

	/**
	 * temPartida
	 * @param id do usuário (obtido através da chamada registraJogador).
	 * @return ­2 (tempo de espera esgotado), ­1 (erro), 0 (ainda não há partida),
	 * 1 (sim, há partida e o jogador inicia jogando) ou
	 * 2 (sim, há partida e o jogador é o segundo a jogar).
	 */
	public int hasGame(int playerId) throws RemoteException;

	/**
	 * obtemOponente
	 * @param id do usuário (obtido através da chamada registraJogador).
	 * @return  string vazio para erro ou string com o nome do oponente.
	 */
	public String getOpponent(int playerId) throws RemoteException;

	/**
	 * ehMinhaVez
	 * @param id do usuário (obtido através da chamada registraJogador).
	 * @return ­2 (erro: ainda não há 2 jogadores registrados na partida), 
	 * 1 (erro),
	 * 0 (não),
	 * 1 (sim),
	 * 2 (é o vencedor),
	 * 3 (é o perdedor),
	 * 4 (houve empate),
	 * 5 (vencedor por WO),
	 * 6 (perdedor por WO).
	 */
	public int isMyTurn(int playerId) throws RemoteException;

	/**
	 * obtemNumCartasBaralho
	 * @param id do usuário (obtido através da chamada registraJogador).
	 * @return ­2 (erro: ainda não há 2 jogadores registrados na partida),
	 * ­1 (erro) ou um valor inteiro com o número de cartas do baralho de compra.
	 */
	public int getNumberOfCardsFromDeck(int playerId) throws RemoteException;

	/**
	 * obtemNumCartas
	 * @param id do usuário (obtido através da chamada registraJogador).
	 * @return ­2 (erro: ainda não há 2 jogadores registrados na partida),
	 * ­1 (erro) ou um valor inteiro com o número de cartas do próprio jogador.
	 */
	public int getNumberOfCards(int playerId) throws RemoteException;

	/**
	 * obtemNumCartasOponente
	 * @param id do usuário (obtido através da chamada registraJogador).
	 * @return  ­2 (erro: ainda não há 2 jogadores registrados na partida),
	 * 1 (erro) ou um valor inteiro com o número de cartas do oponente.
	 */
	public int getNumberOfCardsFromOpponent(int playerId) throws RemoteException;

	/**
	 * mostraMao
	 * @param id do usuário (obtido através da chamada registraJogador).
	 * @return string vazio em caso de erro ou string representando o conjunto de cartas que um jogador tem na sua mão.
	 * Uma forma para identificar e representar todas as cartas e principalmente as cartas que fazem parte da mão do jogador
	 * corresponde a atribuir a cada uma das 108 cartas do jogo um valor numérico de 0 a 107 e também um rótulo que permita
	 * identificar a carta e suas características (cor e valor, por exemplo) de uma forma mais amigável.
	 * O Quadro 1 mostra um conjunto de equivalências possível.
	 * Neste caso, a operação mostraMao, poderia, por exemplo, retornar a seguinte cadeia de caracteres
	 * “3/Az|1/Vd|0/Az|+2/Vm|In/Am|Cg/*|3/Am”,
	 * representando as 7 cartas iniciais para determinado jogador
	 * (3 azul, 1 verde, 0 azul, “+2” vermelho, “inverter” amarelo, curinga, 3 amarelo), separadas pelo caractere “|”.
	 */
	public String showCards(int playerId) throws RemoteException;

	/**
	 * obtemCorAtiva
	 * @param id do usuário (obtido através da chamada registraJogador).
	 * @return valor inteiro correspondendo à cor que está ativa no topo do baralho de descarte
	 * (0 = azul; 1 = amarelo; 2 = verde; 3 = vermelho) –
	 * esta informação é necessária nos casos onde um jogador descartou um curinga e escolheu determinada cor.
	 */
	public int getActiveColor(int playerId) throws RemoteException;

	/**
	 * obtemPontos
	 * @param id do usuário (obtido através da chamada registraJogador).
	 * @return valor inteiro com o número de pontos conquistado no jogo, ­1 (jogador não encontrado), ­2 (partida não iniciada: ainda não há dois jogadores registrados na partida), ­3 (a partida ainda não foi concluída).
	 */
	public int getScore(int playerId) throws RemoteException;

	/**
	 * obtemCartaMesa
	 * @param id do usuário (obtido através da chamada registraJogador).
	 * @return string vazio em caso de erro ou string representando a carta que está no topo da pilha de descarte (usando o mesmo padrão definido pela operação mostraMao).
	 */
	public String getCardFromTable(int playerId) throws RemoteException;

	/**
	 * compraCarta
	 * @param id do usuário (obtido através da chamada registraJogador).
	 * @return código de sucesso (0) ou código de erro (­1).
	 */
	public int getCardFromDeck(int playerId) throws RemoteException;

	/**
	 * obtemPontosOponente
	 * @param id do usuário (obtido através da chamada registraJogador).
	 * @return valor inteiro com o número de pontos conquistado no jogo pelo adversário,
	 * 1 (jogador não encontrado),
	 * ­2 (partida não iniciada: ainda não há dois jogadores registrados na partida),
	 * ­3 (a partida ainda não foi concluída).
	 */
	public int getOpponentScore(int playerId) throws RemoteException;

	/**
	 * jogaCarta
	 * @param id do usuário (obtido através da chamada registraJogador), índice da carta da mão que deve ser jogada (de 0 até o número máximo de cartas do jogador menos 1) e cor da carta, no caso da carta jogada ser um curinga (0 = azul; 1 = amarelo; 2 = verde; 3 = vermelho).
	 * @return 1 (tudo certo),
	 * 0 (jogada inválida: por exemplo, a carta não corresponde à cor que está na mesa),
	 * ­1 (jogador não encontrado), 
	 * 2 (partida não iniciada: ainda não há dois jogadores registrados na partida), 
	 * 3 (parâmetros inválidos),
	 * ­4 (não é a vez do jogador).
	 */
	public int playCard(int playerId, int index, int cardColor) throws RemoteException;
	
	public String hello() throws RemoteException;
}

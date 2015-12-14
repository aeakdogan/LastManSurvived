
public class GameController 
{
	//properties
	private Game game;
	private int packC;
	
	//constructor
	public GameController()
	{
		game = new Game();
		packC = 0;
	}

	//methods
	public void charactersSelected(int no, int level)
	{
		game.createBots(no, level);
	}
	
	public void heroSelected(String heroId)
	{
		Character player = new Character(heroId, Character.HARD, game);
		game.setPlayer(player);
	}
	
	public void mapSelected(String mapId)
	{
		game.setGameMap(new GameMap(mapId, game));
	}
	
	private boolean isValidTimer(int t)
	{
		if(t > 30 && t < 300)
			return true;
		return false;
	}
	
	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public boolean timerSelected(int t)
	{
		if (!isValidTimer(t))
			return false;
		game.setMaxTime(t);
		return true;
	}
	
	public boolean startGame()
	{
		if(game.isReady())
			return true;
		return false;
	}
	
	public void updateGame(int elapsed)
	{
		if(game.getCurrentTime() + 1 == game.getMaxTime() | game.getDeadCharacters().contains(game.getPlayer()) | game.getAliveCharacters().size() == 1)
		{
			endGame();
			return;
		}
		if(++packC == Pack.RATE)
		{
			int randomX = Location.CELL + (int)(Math.random()*(Location.X_LIMIT - 2*Location.CELL) - Location.CELL + 1);
			int randomY = Location.CELL + (int)(Math.random()*(Location.Y_LIMIT - 2*Location.CELL) - Location.CELL + 1);
			Location loc = new Location(randomX, randomY);
			int prob = 1 + (int)(Math.random()*3);
			Pack newPack;
			if(prob == 1)
			{
				newPack = new Pack("healthBoost");
			}
			else
			{
				newPack = new Pack("weaponBoost");
			}
			packC = 0;
			game.getGameMap().addToCurrentPacks(loc, newPack);
		}
		game.update(elapsed);
	}
	
	public void endGame()
	{
		String resultScreenInfo = "GAME TIME: " + game.getCurrentTime();
		for(int i = 0; i < game.getDeadCharacters().size(); i++)
		{
			if(game.getDeadCharacters().get(i) == game.getPlayer())
				resultScreenInfo = "PLAYER: " + game.getPlayer().getHero().getId() + 
				", HP: 0, Death Time: " + game.getPlayer().getDeathTime() + "\n" + resultScreenInfo;
			else
				resultScreenInfo = "BOT: " + game.getDeadCharacters().get(i).getHero().getId() + 
				", HP: 0, Death Time: " + game.getDeadCharacters().get(i).getDeathTime() + "\n" + resultScreenInfo;
		}
		for(int i = 0; i < game.getAliveCharacters().size(); i++)
		{
			if(game.getAliveCharacters().get(i) == game.getPlayer())
				resultScreenInfo = "PLAYER: " + game.getPlayer().getHero().getId() + 
				", HP:" + game.getPlayer().gethP() + ", ALIVE\n" + resultScreenInfo;
			else
				resultScreenInfo = "BOT: " + game.getAliveCharacters().get(i).getHero().getId() + 
				", HP: " + game.getAliveCharacters().get(i).gethP() + ", ALIVE\n" + resultScreenInfo;
		}
		
		game.endGame(resultScreenInfo);
	}
}

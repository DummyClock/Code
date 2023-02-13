/*	Elias Saied
	5/9/2022
	
	This is the class that'll be controlled by players. It encapsulates a backpack, a team, and a PC for
		personal usage.
*/
public class Player implements Trainer {
	private String playerName;
	private Backpack myBag;
	private Team myTeam;
	private PC_Storage myPC;
	
	public Player(String name)
	{
		playerName = name;
		myBag = new Backpack();
		myTeam = new Team();
		myPC = new PC_Storage();
	}
	
	/*
	 *  @return the Name of the Trainer
	 */
	public String getName()
	{
		return playerName;
	}
	
	/*
	 *  Add a Pokemon into the Team
	 *  @param PKMN an instance of a Pokemon
	 */
	@Override
	public void addToTeam(Pokemon PKMN) {
		myTeam.add(PKMN);
	}

	/*
	 * @return true/false If the CPU"s team has fainted
	 */
	@Override
	public boolean isTeamFainted() {
		return myTeam.isTeamFainted();
	}

	/*
	 *  @return An instance of the Trainer's PC_Storage. Ultimately allow's acces to it.
	 */
	public PC_Storage getPC_Storage() {
		return myPC;
	}

	/*
	 * @return true/false If the CPU"s team has fainted
	 */
	@Override
	public Team getTeam() {
		// TODO Auto-generated method stub
		return myTeam;
	}

	/*
	 * @return An instance of the Trainer's Backpack. Ultimately allow's access to it.
	 */
	@Override
	public Backpack getBackpack() {
		return myBag;
	}
	

}

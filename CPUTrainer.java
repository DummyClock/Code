/*	Elias Saied
	5/9/2022
	
	A Trainer that'll rely on algorithms to carry out funtions. These trainers are meant to go up against
	the Player. Each encapsulates a backpack and team.
*/


public class CPUTrainer implements Trainer {

	private String trainerName;
	private Backpack trainerBag;
	private Team trainerTeam;
	private String trainerClass;
	
	public CPUTrainer(String name, String trainerClass)
	{
		trainerName = name;
		this.trainerClass = trainerClass;
		trainerBag = new Backpack();
		trainerTeam = new Team();
	}
	
	/*
	 *  @return the Full Name of the Trainer (TrainerClass + Name)
	 */
	public String getFullName()
	{
		return trainerClass + " " + trainerName;
	}
	
	/*
	 * Set the name of this trainer
	 * @param	name	The name of this trainer 
	 */
	public void setName(String name)
	{
		trainerName = name;
	}
	
	/*
	 * @param trainerClass		The trainer class for this Trainer
	 */
	public void setTrainerClass(String trainerClass)
	{
		this.trainerClass = trainerClass;
	}

	/*
	 *  Add a Pokemon into the Team
	 *  @param PKMN an instance of a Pokemon
	 */
	@Override
	public void addToTeam(Pokemon PKMN) {
		trainerTeam.add(PKMN);
	}

	/*
	 * @return true/false If the CPU"s team has fainted
	 */
	@Override
	public boolean isTeamFainted() {
		return trainerTeam.isTeamFainted();
	}

	/*
	 * @return An instance of the Trainer's team. Ultimately allow's access to it.
	 */
	@Override
	public Team getTeam() {
		// TODO Auto-generated method stub
		return trainerTeam;
	}

	/*
	 * @return An instance of the Trainer's Backpack. Ultimately allow's access to it.
	 */
	@Override
	public Backpack getBackpack() {
		return trainerBag;
	}
	
	
	/*
	 *  @return the Name of the Trainer
	 */
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return trainerName;
	}

}

import java.util.List;

/*	Elias Saied
	4/12/2022
	
	An interface that'll be used to build the Pokemon class. It holds method headers for the various stats of a Pokemon
 */

public interface PokemonEntry {
	
	/*
	 * @param name	A String value assigned to the Pokemon
	 * Set's the name of the Pokemon
	 */
	public void setName(String name);
	
	/*
	 * @return name		The name of the Pokemon
	 */
	public String getName();
	
	/*
	 * Get the 1-2 types assigned to a Pokemon
	 * @return types	An array of Strings that specific the Pokemon's type(s)
	 */
	public String[] getTypes();
	
	/*
	 * Returns the type weakness of a Pokemon
	 * @return	typeWeakness	A List of Strings that indicate which types are weak for this Pokemon
	 */
	public List<String> getTypeWeakness();
	
	/*
	 * Set the Pokemon's HP. HP can't exceed the maxHP
	 * @param healthPoints		The amount of health that will set for this Pokemon 
	 */
	public void setHP(int healthPoints);
	
	/*
	 * @return The HP of this Pokemon
	 */
	public int getHP();
	
	/*
	 * Set the max HP for this Pokemon
	 * @param maximum_HP	The maximum HP for a Pokemon  
	 */
	public void setMaxHP(int maximum_healthPoints);
	
	/*
	 * @return	The max HP of the Pokemon
	 */
	public int getMaxHP();
	
	/*
	 * Changes the base attack stat for this Pokemon
	 * @param attackStat	The new base attack to be set
	 */
	public void setAttack(int attackStat);
	
	/*
	 * @return 	The current attack stat of this Pokemon
	 */
	public int getAttack();
	
	/*
	 * Changes the base defense stat for a Pokemon
	 * @defStat		The new defense stat to be set
	 */
	public void setDefense(int defStat);
	
	/*
	 * @return 		The current defense stat
	 */
	public int getDefense();
	
	/*
	 * Changes the current speed stat
	 * @param speedStat		The new speed stat to be set
	 */
	public void setSpeed(int speedStat);
	
	/*
	 * @return 	The current speed stat
	 */
	public int getSpeed();
	
	/*
	 * @return	The pokedex number
	 */
	public int getPokedexNum();

	/* Assigns a Pokedex number to this Pokemon
	 * @param PKMN_Num	A unique number key that'll be used to identify the pokemon
	 */
	public void setPokedexNum(int PKMN_Num);

	/*
	 * Use one of the moves from the Pokemon's MoveList
	 * @param moveNum	The move number that'll reference one of the Pokemon's moves (Must be 0, 1, 2, or 3)
	 * @param target	The pokemon that will be effected by this move
	 */
	public void useMove(int moveNum, Pokemon target);
	
	/*
	 * Adds a new move into the Move list
	 * @param newMove	A move that will be added
	 */
	public void addMove(Move newMove);
	
	//public Move deleteMove(Move moveToRemove);
	
	/*
	 * Displays all current moves of this Pokemon
	 */
	public void displayMoveList();

	/*
	 *  Set's a status condition for the Pokemon based on the integer passed in
	 *  	1 = Confuse
	 *  	2 = Paralyze
	 *  	3 = Poison
	 *  	4 = Burn
	 *  	5 = Sleep
	 *  	6 = Freeze
	 *  @param statusType	An int that will determine the status set
	 */
	public void setStatusCondition(int statusType);
	
	/*
	 * Set's this Pokemon as a CAPTURED pokemon
	 */
	public void caught();
	
	/*
	 *@param 	true/false If the Pokemon has been captured and is owned by a trainer 
	 */
	public boolean isCaught();
		
}

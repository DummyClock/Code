/*	Elias Saied
	4/12/2022
	
	An interface that'll be used to create Moves for Pokemon to use
*/

public interface MoveEntry {

	/*
	 *  The damage and possible effects from a Move will be inflicted onto the Opponent
	 *  @param foe	The opposing Pokemon who will recieve these effects
	 *  
	 *  check accuracy
	 *  	If accuracy is accepted
	 *  		perform move x times
	 *  			- Subtract damage
	 *  			- Chance of setting a status condition
	 */
	public void useMove(Pokemon foe);
	
	/*
	 * Returns the current power points of move
	 * @return the current power points of the move
	 */
	public int getPP();
	
	/*
	 * Set the power points for a move
	 * @param PP_Value	The amount of times a move can be used
	 */
	public void setMaxPP(int PP_Value);

	
	/*
	 *  @return the name of the Move
	 */
	public String getMoveName();
	
	/*
	 * @return the type for a move
	 */
	public String getType();
	
	/*
	 * Displays the Stat values of a Move
	 */
	public void getMoveStats();
}

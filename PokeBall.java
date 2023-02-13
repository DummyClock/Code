import java.util.Random;
import java.util.concurrent.TimeUnit;
/*	Elias Saied
	5/9/2022

	These items will be stored in bags and will be used by the player to capture a Pokemon
	and add them to their team.
*/
public class PokeBall implements ItemEntry {
	private String itemName;
	private String description;
	private int quantity;
	private double ballModifier;
	private Random generator = new Random();
	private String itemType;
	
	public PokeBall(double ballModifier, String itemName, String description, int quantity)
	{
		this.ballModifier = ballModifier;
		this.itemName = itemName;
		this.description = description;
		this.quantity = quantity;
		itemType = "Poke Ball";
	}
	
	/*
	 * @return The name of the Item
	 */
	public String getItemName() {
		return itemName;
	}

	/*
	 * @return The description of the Item
	 */
	public String getDescription() {
		return description;
	}
	
	/*
	 * @param info	The new description to bet assigned to the object
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}

	/*
	 *  @return The quantity of an Item
	 */
	public void setQuantity(int num_of_items) {
		quantity = num_of_items;
	}
	
	/*
	 *  Used to determine which itemType this item is
	 *  @return 	A string value indicating the type
	 */
	@Override
	public String getItemType()
	{
		return itemType;
	}
	
	/*
	 * @param anItem	The item you want to compare to
	 * @return true/false if both items have the same name
	 */
	public boolean compareTo(ItemEntry anItem)
	{
		if(itemName.equals(anItem.getItemName()) && itemType.equals(anItem.getItemType()))
			return true;
		else
			return false;
	}

	/*
	 * 	Uses the pokeball to try and capture a Pokemon.
	 * 
	 * @param target		The pokemon that will attempt to be captured.
	 * @param myTrainer	 	The trainer who will receive the pokemon if captured
	 */
	public <T extends ItemEntry> void useItem(Pokemon target, Trainer myTrainer) {
		Backpack trainerBag = myTrainer.getBackpack();	// Get the trainer's Bag
		boolean own;
		own = trainerBag.contains(this);		// check if the Trainer has this item
		
		if(own) {//If Trainer owns
			int HP = target.getHP();
			if(HP >= 0) 
			{
				double catchValue;			//Declare catchValue to determine if the Pokemon will be caught
				int maxHP = target.getMaxHP();
				double captureRate = target.getCaptureRate();		//Get the Pokemon's capture rate
				
				//Calculate catchValue
				catchValue = ((((3 * maxHP - 2 * HP) * (captureRate * ballModifier))/(3 * maxHP)));	
				System.out.println(myTrainer.getName() + " threw a " + itemName + ".");
				trainerBag.withdrawItem(this);		//Update the quantity 
				try {
					TimeUnit.MILLISECONDS.sleep(900);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				if(!target.isCaught())
				{
					if(catchValue == 255)				//if catchValue is equal to 255 - Success
					{
						myTrainer.addToTeam(target);	//add Pokemon to team or storage
						System.out.println("Gotcha!\n" + "target.getName() was caught!");
						target.caught();
					}
					else		// Do a series of luck tests
					{
						//Calculate the probability for the test to succeed
						double shakeProbability;
						shakeProbability = 65536 / Math.pow((255/catchValue),0.1875);
						int randomNum = generator.nextInt(65536);	//Generate random number
						//System.out.println("Shake Prob: " + shakeProbability);
						//System.out.println("Random num: " + randomNum);
						
						//Nested if else Loops
						//If lucky : You caught it
						if(randomNum < shakeProbability)		//Check 1 - if less than shake probability : Success
						{
							if(shakeCheck(shakeProbability))		//Check 2
							{
								if(shakeCheck(shakeProbability))		//Check 3
								{
									if(shakeCheck(shakeProbability))		//Check 4
									{
										// SUCCESSFULLY CAPTURED POKEMON 
										System.out.println("Gotcha!\n" + target.getName() + " was caught!");
										myTrainer.addToTeam(target);
										target.caught();
									}
									else									//Fail
										System.out.println("What!! The Pokemon appeared to've been caught!");
								}
								else									// You don't catch it
									System.out.println("Aghhhh! You almost had it!");
							}
							else									// You don't catch it
								System.out.println("Shoot. The Pokemon broke out.");
						}
						else									// You don't catch it
							System.out.println("Oh no! The Pokemon broke free!");
					} 
				}
				else
					System.out.println("The trainer blocked the Poke ball. Don't be a thief!");
			}
			else
				System.out.println("You can't catch a Pokemon that has fainted.");
		}
		else	// Don't own this item
			System.out.println("You don't have that item.");
	}

	/*
	 * A random number test
	 * 
	 * @return A result that will indicate success or failure
	 */
	private boolean shakeCheck(double shakeProb)
	{
		int randomNum;
		System.out.println("*shake*");
		randomNum = generator.nextInt(65536);		//generate a random number
		
		//Pauses the thread for 2 Seconds for the sake of building anticipation
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return randomNum < shakeProb;		// Return the result of this comparison
	}

	/*
	 * @return The quantity of an item object
	 */
	@Override
	public int getQuantity() {
		// TODO Auto-generated method stub
		return quantity;
	}
	
	/*
	 *  Lower the quantity of an object by 1
	 */
	@Override
	public void decrementQuantity()
	{
		quantity--;
	}


}

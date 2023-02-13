/*	Elias Saied
	5/9/2022
	
	These will be stored in backpacks and will be used by trainers to restore HP to their 
	Pokemon.
*/
public class HealItem implements ItemEntry {
	private String itemName;
	private String description;
	private int quantity;
	private int healAmount;
	private String itemType;
	
	public HealItem(int healPoints, String itemName, String description, int quantity)
	{
		this.healAmount = healPoints;
		this.itemName = itemName;
		this.description = description;
		this.quantity = quantity;
		itemType = "Heal Item";
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
	 * @return true/false if both items have the same name and are the same type
	 */
	public boolean compareTo(ItemEntry anItem)
	{
		if(itemName.equals(anItem.getItemName()) && itemType.equals(anItem.getItemType()))
			return true;
		else
			return false;
	}
	
	/*
	 * Uses the item to perform an action and will subtract 1 from the quantity
	 */
	public <T extends ItemEntry> void useItem(Pokemon target, Trainer myTrainer) {
		Backpack trainerBag = myTrainer.getBackpack();	// Get the trainer's Bag
		boolean own = trainerBag.contains(this);		// check if the Trainer has this item
		
		if(own) {									// if this item is owned, then use it
			int maxHP = target.getMaxHP();
			int currHP =  target.getHP();
			
			if(maxHP > currHP)		// if currHP is less than maxHP
			{
				target.setHP(currHP + healAmount);		// Heal
				//Prints the correct amount of HP to restore based on the current HP
				if((maxHP - currHP) >= healAmount)
					System.out.println("The " + getItemName() + " restored " + (healAmount) + " HP to " + target.getName() + ".");
				else
					System.out.println("The " + getItemName() + " restored " + (maxHP - currHP) + " HP to " + target.getName() + ".");
				
				trainerBag.withdrawItem(this); //Update the quantity after use
			}
			else
				System.out.println("HP is Full.");
		}
		else
			System.out.println("You don't have that item.");
	}
	
	/*
	 * @return The quantity of an item object
	 */
	@Override
	public int getQuantity() {
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

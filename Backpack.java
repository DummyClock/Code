import java.util.Arrays;
/*	Elias Saied
	5/9/2022
	
	This will store all the items of trainer that can be used in and outside of battle.
*/
public class Backpack implements Storage {
	private ItemEntry[] backpack;
	private int DEFAULT_CAPACITY = 20;
	private int MAX_CAPACITY = 200; 
	private int numOfEntries;
	
	public Backpack()
	{
		backpack = new ItemEntry[DEFAULT_CAPACITY];
		numOfEntries = 0;
	}
	
	//Add an Item into the bag
	//@param	anItem		An Item that will be added
	public void addItem(ItemEntry anItem)
	{
		if(backpack.length < MAX_CAPACITY)	//if full make a bigger bag
		{
			int newLength = backpack.length * 2;
			ItemEntry[] upgradedBag = new ItemEntry[newLength];
			upgradedBag = Arrays.copyOf(upgradedBag, newLength);	
		}
		
		backpack[numOfEntries] = anItem;
		//System.out.println(anItem.getItemName() + " was added to Bag");
		numOfEntries++;
	}
	
	/*
	 *  Lower the quanity of an item by 1
	 *  @anItem		The item that'll have it's quantity decremented
	 */
	public void withdrawItem(ItemEntry anItem)
	{
		int location;
		location = getIndex(anItem);		// Get the location og an item
		backpack[location].decrementQuantity();
		
		if(anItem.getQuantity() == 0)		//If the last item was used clear out it's slot
			clearItemSpot(location);
	}
	
	/*
	 *  Dump out all of a specific item / Clear's out the item slot from the Bag
	 *  @param location		The position of an item with the bag
	 */
	private void clearItemSpot(int location)
	{
		backpack[location] = null;	//remove
		numOfEntries--;				//Decrement number of entries
		backpack[location] = backpack[numOfEntries];		//move last entry to emptied spot
	}
	
	//Checks if the bag contains a specific item
	//@param	anItem 	The item to be checked for in the bag
	//@return	true/false if the item was found
	public boolean contains(ItemEntry anItem)
	{
		//System.out.println("Item: " + anItem.getItemName());
		int location = getIndex(anItem);
		if(location > -1)
		{
			//System.out.println("There's no " + anItem.getItemName() + " in the bag");
			return true;
		}
		else
			return false;
	}

	
	/*
	 * Checks if the item is in the bag, then returns it if so
	 * @param itemName		The name of the item
	 * @return 	An itemEntry if it exists, otherwise null
	 */
	public ItemEntry getItem(String itemName)
	{
		//Create dummy placeholders to compare to: 1 Heal Version & 1 Pokeball Version	(BAD CODE)
		ItemEntry placeholder = new PokeBall((float)1.25, itemName, null, 1);
		ItemEntry placeholder2 = new HealItem(20, itemName, null, 1);
	
		//Check if either types in the bag
		int location = getIndex(placeholder);
		int location2 = getIndex(placeholder2);
		
		if(location != -1)			//If there's an item with the same name and type(pokeball type)
			return backpack[location];
		else if(location2 != -1)		//If there's an item with the same name and type(heal type)
			return backpack[location2];
		else						//else neither version of the item exists, therefore the item doesn't exist in the bag
			return null;
	}
	
	/*
	 * Find the index of a requested item
	 * @param	anItem		The item to search for
	 * @return	The location of the item within the array OR -1 if not found
	 */
	private int getIndex(ItemEntry anItem)
	{
		boolean found = false;
		int entriesLooped = 0;
		int location = -1;		//If -1 is returned, that means the item is not in the bag

			do
			{
				//System.out.println("Item at index " + entriesLooped + " is " + backpack[entriesLooped]);
				if(backpack[entriesLooped] == null)
				{
					found = true;
					location = -1;
					

				}
				else if(backpack[entriesLooped].compareTo(anItem))
				{
					found = true;
					location = entriesLooped;
					//System.out.println(anItem.getItemName() + " found in the bag");
					return location;
				}
				entriesLooped++;	//Increment each element spot
			}while(!found && entriesLooped < backpack.length);	//Stop looking for item if it was found or if the entire bag was checked
			
		
		return location;
	}
	
	/*
	 * @param item	The request item
	 * @return the quantity of the item within the bag
	 */
	public int getQuantityOf(ItemEntry item)
	{
		int location = getIndex(item);
		if(location > -1)
		{
			System.out.println("There's no " + item.getItemName() + " in the bag");
			return backpack[location].getQuantity();
		}
		else
			return 0;
	}
	
	/*
	 * Display the contents of the bag
	 */
	@Override
	public void display() {
		for(int i = 0; i < numOfEntries; i++)
			System.out.println(" - " + backpack[i].getItemName() + "		" + backpack[i].getQuantity());

	}
	
}

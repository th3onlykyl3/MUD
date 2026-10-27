import java.util.ArrayList;
import java.util.HashMap;

public class Room {
	private String description;
	private ArrayList<Item> items = new ArrayList<Item>();
	private HashMap<String, Room> exits = new HashMap<>();
	
	
	
	public Room(String description) {
		this.description = description;
		
	}
	public void setExit(String direction, Room room) {
	    exits.put(direction.toLowerCase(), room);
	}
	public Room getExit(String direction) {
		return exits.get(direction.toLowerCase());
	}
	public void addItem(Item item) {
		items.add(item);
	}
	
	
	public Item removeItem(String name) {
		for(Item item : items) {
			if(item.getName().equals(name.toLowerCase())) {
				items.remove(item);
				return item;
			}
		}
		return null;
	
	}
	public String showDescription() {
		return "You are in " + description;
	}
	public String showRoom() {
		StringBuilder sb = new StringBuilder();
		if (items.isEmpty()) {
			sb.append("items here: ").append(items).append("/");
		}
		if (!exits.isEmpty()) {
			sb.append(" Directions: ").append(exits.keySet()).append("/");
		}
		return sb.toString();
		}
	
	public String getDescription() {
		   return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String[] getExitNames() {
		return exits.keySet().toArray(new String[0]);
		
	}
	public boolean hasExit(String direction)  {
		return exits.containsKey(direction.toLowerCase());
	}
	
	public String[] getItemNames() {
		String[] names = new String[items.size()];
		for (int i = 0; i < items.size(); i++){
			names[i] = items.get(i).getName();		
		}
		return names;
	}
}

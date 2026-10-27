
import java.util.ArrayList;
import java.util.Iterator;

public class Player implements WorldObserver {

    private String name;
    private Room currentRoom;
    private ArrayList<Item> inventory = new ArrayList<>();
    private World world;

    // Constructors

    public Player(String name, Room startingRoom, World world) {
        this.name = name;
        this.currentRoom = startingRoom;
        this.world = world;
        world.addObserver(this);
    }

    // Getters / Setters

    public String getName() {
        return name;
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public ArrayList<Item> getInventory() {
        return inventory;
    }

    public String[] getInventoryNames() {
        String[] names = new String[inventory.size()];
        for (int i = 0; i < inventory.size(); i++){
            names[i] = inventory.get(i).getName();
        }
        return names;
    }

    public void setCurrentRoom(Room room) {
        this.currentRoom = room;
    }

    // Movement

    public String move(String direction) {
        if (!currentRoom.hasExit(direction)) {
            return "You cannot go " + direction;
        }

        currentRoom = currentRoom.getExit(direction);

        if (world != null)
            world.notifyPlayerMoved(this, currentRoom);

        return "You move " + direction + ".";
    }

    // Inventory management

    public boolean hasItem(String name) {
        for (Item item : inventory) {
            if (item.getName().equalsIgnoreCase(name)) return true;
        }
        return false;
    }

    public String getItem(String name) {
        Item item = currentRoom.removeItem(name);
        if (item == null) return "No such item.";

        inventory.add(item);

        if (world != null)
            world.notifyRoomUpdated(currentRoom);

        return "You picked up " + name;
    }

    public String dropItem(String name) {
        Iterator<Item> iter = inventory.iterator();

        while (iter.hasNext()) {
            Item item = iter.next();
            if (item.getName().equalsIgnoreCase(name)) {
                iter.remove();
                currentRoom.addItem(item);

                if (world != null)
                    world.notifyRoomUpdated(currentRoom);

                return "You dropped " + item.getName();
            }
        }
        return "You don't have that item.";
    }
    //Give Item Feature 
    public String giveItem(String itemName, Player other) {
        if (other == null) {
            return "There is no player to give items to!";
        }

        if (other.getCurrentRoom() != this.currentRoom) {
            return other.getName() + " is not in the same room!";
        }

        Iterator<Item> iter = inventory.iterator();
        while (iter.hasNext()) {
            Item item = iter.next();
            if (item.getName().equalsIgnoreCase(itemName)) {
                iter.remove();
                other.getInventory().add(item);

                // Notify world so UIs update
                if (world != null) {
                    world.notifyRoomUpdated(this.currentRoom);
                    world.broadcastMessage(this.getName() + " gives " + item.getName() + " to " + other.getName());
                }

                return "You gave " + item.getName() + " to " + other.getName();
            }
        }

        return "You don't have that item.";
    }

    public String showInventory() {
        if (inventory.isEmpty()) {
            return "Inventory is empty";
        }

        StringBuilder s = new StringBuilder("You are carrying: ");
        for (Item i : inventory) {
            s.append(i.getName()).append(" ");
        }
        return s.toString().trim();
    }

    // Player actions

    public String dab(Player other) {
        if (other == null)
            return name + " tries to dab... but nobody is here!";

        if (other.getCurrentRoom() != this.currentRoom)
            return name + " tries to dab " + other.getName() + ", but they're not in the same room!";

        return name + " dabs up " + other.getName() + "!";
    }

    public String swim() {
        if (!currentRoom.getDescription().toLowerCase().contains("swimpool")) {
            return "You are not at the swimming pool.";
        }

        if (!hasItem("swimtrunks")) {
            return "You need swim trunks to swim!";
        }

        return "You dive into the pool. The water feels nice and warm.";
    }

    public String showStatus() {
        return currentRoom.showDescription() + "\n" +
               currentRoom.showRoom() + "\n" +
               showInventory();
    }

    // WorldObserver Callbacks

    @Override
    public void onMobMoved(Mob mob, Room newRoom) {
        if (newRoom == currentRoom) {
            System.out.println("A " + mob.getName() + " enters the room!");
        }
    }

    @Override
    public void onPlayerMoved(Player player, Room newRoom) {
        if (player == this) return;

        if (newRoom == currentRoom) {
            System.out.println(player.getName() + " enters the room!");
            world.broadcastMessage(player.getName() + " enters the room!");
        }
    }
    @Override
    public void onMessage(String msg) {
        System.out.println(" " + msg);
    }

    @Override
    public void onRoomUpdated(Room room) {
        // UI or player notifications could go here
    }
    
    public World getWorld() {
        return world;
    }
}
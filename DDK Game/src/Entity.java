
public abstract class Entity {
    protected String name;
    protected Room currentRoom;

    public Entity(String name, Room startRoom) {
        this.name = name;
        this.currentRoom = startRoom;
    }

    public String getName() {
        return name;
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public boolean move(String direction) {
        Room next = currentRoom.getExit(direction);
        if (next != null) {
            currentRoom = next;
            return true;
        }
        return false;
    }

    public boolean isInSameRoom(Entity other) {
        return this.currentRoom == other.currentRoom;
    }
}

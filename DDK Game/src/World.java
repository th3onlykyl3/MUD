import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

public class World {

    private List<WorldObserver> observers = new ArrayList<>();
    private List<Mob> mobs = new ArrayList<>();
    private List<Player> players = new ArrayList<>();


    //Player Management
    public void addPlayer(Player p) {
        players.add(p);
    }

    public List<Player> getPlayers() {
        return players;
    }

    // Get the "other" player
    public Player getOtherPlayer(Player me) {
        for (Player p : players) {
            if (p != me)
                return p;
        }
        return null;
    }


    // Observer Registration
    public void addObserver(WorldObserver obs) {
        observers.add(obs);
    }

    public void removeObserver(WorldObserver obs) {
        observers.remove(obs);
    }


    // Messaging
  
    public void broadcastMessage(String msg) {
        for (WorldObserver o : observers) {
            o.onMessage(msg);
        }
    }


    //Player Movement
    public void notifyPlayerMoved(Player movedPlayer, Room newRoom) {

        //Update every UI window
        for (WorldObserver o : observers) {
            o.onPlayerMoved(movedPlayer, newRoom);
        }

        // Check if another player is in the same room
        for (Player p : players) {
            if (p != movedPlayer && p.getCurrentRoom() == newRoom) {
                broadcastMessage(movedPlayer.getName() +
                    " has entered the room with " + p.getName() + "!");
            }
        }
    }


    //Mob Movement
    public void notifyMobMoved(Mob mob, Room newRoom) {
        for (WorldObserver o : observers) {
            o.onMobMoved(mob, newRoom);
        }
    }


    //Room Updated
    public void notifyRoomUpdated(Room room) {
        for (WorldObserver o : observers) {
            o.onRoomUpdated(room);
        }
    }


    // Mob Management
    public void addMob(Mob mob) {
        mobs.add(mob);
    }

    public List<Mob> getMobs() {
        return mobs;
    }
    
    public void dogStealAttempt(Mob dog) {
        for (Player p : players) {
            if (p.getCurrentRoom() == dog.getCurrentRoom()) {
                dog.stealFromPlayer(p);
            }
        }
        
    }
}



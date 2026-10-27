import java.util.Random;

public class Mob extends Entity implements Runnable, WorldObserver {

    private volatile boolean running = true;
    private int moveInterval;
    private Random rand = new Random();
    private World world;
    

    public Mob(String name, Room startRoom, int moveInterval, World world) {
        super(name, startRoom);
        this.moveInterval = moveInterval;
        this.world = world;

        // Register Mob as an observer of the world
        world.addObserver(this);
    }

    @Override
    public void run() {
        while (running) {
            try {
                Thread.sleep(moveInterval);
            } catch (InterruptedException e) {
                return;
            }
            roam();
        }
    }

    private void roam() {
        String[] exits = currentRoom.getExitNames();

        if (exits.length == 0)
            return;

        String dir = exits[rand.nextInt(exits.length)];

        boolean moved = move(dir);

        if (moved && world != null) {
            world.notifyMobMoved(this, currentRoom);
        }
    }

    public void stopMob() {
        running = false;
    }


    //WorldObserver Implementation

    @Override
    public void onPlayerMoved(Player player, Room newRoom) {
        if (newRoom == currentRoom) {
            System.out.println(getName() + " sees the player enter the room!");
        }
    }

    @Override
    public void onMobMoved(Mob mob, Room newRoom) {
        // Ignore notifications about itself
        if (mob == this) return;

        if (newRoom == currentRoom) {
            System.out.println(getName() + " sees another mob: " + mob.getName());
            
            if (mob.getName().equalsIgnoreCase("dog")) {
                world.dogStealAttempt(mob);
            }
        }
    }
    
    public void stealFromPlayer(Player player) {
        if (player.getInventory().isEmpty()) {
            return; // nothing to steal
        }

        // Steal the first item
        Item stolen = player.getInventory().remove(0);

        // Notify UI
        player.getWorld().broadcastMessage(
            getName() + " steals " + stolen.getName() + " from " + player.getName() + "!"
        );

        // Update player's UI
        player.getWorld().notifyRoomUpdated(player.getCurrentRoom());
    }

    @Override
    public void onRoomUpdated(Room room) {
    }
    @Override
    public void onMessage(String msg) {
        System.out.println(" " + msg);
    }

}

	
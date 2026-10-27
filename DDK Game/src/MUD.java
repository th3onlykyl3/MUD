import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MUD {

    private ExecutorService pool;
    private Mob mob1, mob2;

    public static void main(String[] args) {
        new MUD().start();
    }

    public void start() {

        //Rooms
        Room hall = new Room("hall.");
        Room kitchen = new Room("kitchen.");
        Room attic = new Room("attic.");
        Room basement = new Room("basement.");
        Room chamber = new Room("chamber.");
        Room swimpool = new Room("swimpool.");
        Room bedroom = new Room ("bedroom.");
        Room bathroom = new Room ("bathroom.");

        //Connections
        kitchen.setExit("south", hall);
        hall.setExit("north", kitchen);
        hall.setExit("east", attic);
        attic.setExit("west", hall);
        hall.setExit("west", bedroom);
        bedroom.setExit("east", hall);
        hall.setExit("south", basement);
        basement.setExit("east", hall);
        bedroom.setExit("north", bathroom);
        bathroom.setExit("south", bedroom);
        basement.setExit("north", chamber);
        chamber.setExit("south", basement);
        chamber.setExit("north", swimpool);
        swimpool.setExit("south", chamber);
        		

        //Items
        hall.addItem(new Item("key", "An old key", 1));
        kitchen.addItem(new Item("pan", "Cast iron pan", 1));
        swimpool.addItem(new Item("swimtrunks", "A pair of blue swim trunks", 1));

        // Shared World
        World world = new World();

        // TWO PLAYERS IN SAME WORLD
        Player player1 = new Player("P1", hall, world);
        Player player2 = new Player("P2", attic, world);

        world.addPlayer(player1);
        world.addPlayer(player2);
        

        //TWO UI WINDOWS
        UI ui1 = new UI(world, player1);
        UI ui2 = new UI(world, player2);

        //Register UI windows as observers
        world.addObserver(ui1);
        world.addObserver(ui2);

        //Launch both windows
        ui1.launchGUI();
        ui2.launchGUI();

        //Thread pool
        pool = Executors.newFixedThreadPool(4);

        //Create mobs
        mob1 = new Mob("Giant", kitchen, 3000, world);
        mob2 = new Mob("Dog", attic, 5000, world);

        world.addMob(mob1);
        world.addMob(mob2);

        //Execute mobs
        pool.execute(mob1);
        pool.execute(mob2);

        //Shutdown
        Runtime.getRuntime().addShutdownHook(
            new Thread(() -> {
                stopMobs();
                pool.shutdown();
                System.out.println("MOBS stopped and thread pool shutdown.");
            })
        );
    }


    public void stopMobs() {
        if (mob1 != null) mob1.stopMob();
        if (mob2 != null) mob2.stopMob();
    }
}


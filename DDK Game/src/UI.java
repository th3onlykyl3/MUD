import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;

public class UI extends JFrame implements WorldObserver {

    private Player player;
    private World world;

    private JLabel roomLabel;
    private JLabel roomImage;
    private JTextArea roomInfo;
    private JList<String> roomItemsList;
    private JList<String> inventoryList;
    private JTextField commandField;
    private JTextArea messageArea;
    private JTextArea mobArea;
    private JButton northButton, southButton, eastButton, westButton;

    private DefaultListModel<String> roomItemsModel;
    private DefaultListModel<String> inventoryModel;

    private static final long serialVersionUID = 1L;

    //OBSERVER CALLBACKS
    @Override
    public void onMobMoved(Mob mob, Room newRoom) {
        SwingUtilities.invokeLater(this::updateDisplay);
    }

    @Override
    public void onPlayerMoved(Player player, Room newRoom) {
        SwingUtilities.invokeLater(this::updateDisplay);
    }

    @Override
    public void onRoomUpdated(Room room) {
        SwingUtilities.invokeLater(this::updateDisplay);
    }
    @Override
    public void onMessage(String msg) {
        SwingUtilities.invokeLater(() -> {
            messageArea.append(msg + "\n");
        });
    }

    //CONSTRUCTOR
    public UI(World world, Player player) {
        this.player = player;
        this.world = world;

        world.addObserver(this);

        buildUI();
        updateDisplay();
    }

    //BUILD UI
    private void buildUI() {

        setTitle("MUD Adventure");
        setSize(900, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        //Top Panel
        JPanel topPanel = new JPanel(new BorderLayout());
        roomLabel = new JLabel("", SwingConstants.CENTER);
        roomLabel.setFont(new Font("Serif", Font.BOLD, 20));
        topPanel.add(roomLabel, BorderLayout.NORTH);

        roomImage = new JLabel("", SwingConstants.CENTER);
        roomImage.setPreferredSize(new Dimension(700, 200));
        topPanel.add(roomImage, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        //Center Panel Room description + Mobs
        JPanel centerPanel = new JPanel(new BorderLayout());

        //Room description
        roomInfo = new JTextArea(5, 30);
        roomInfo.setEditable(false);
        roomInfo.setLineWrap(true);
        roomInfo.setWrapStyleWord(true);
        centerPanel.add(new JScrollPane(roomInfo), BorderLayout.CENTER);

        //Mob panel
        mobArea = new JTextArea(4, 30);
        mobArea.setEditable(false);
        mobArea.setBorder(BorderFactory.createTitledBorder("Mobs in Room"));
        centerPanel.add(new JScrollPane(mobArea), BorderLayout.SOUTH);

        add(centerPanel, BorderLayout.CENTER);

        //Right Panel Room Items + Inventory
        JPanel sidePanel = new JPanel(new GridLayout(2, 1));
        sidePanel.setPreferredSize(new Dimension(200, 0));

        roomItemsModel = new DefaultListModel<>();
        roomItemsList = new JList<>(roomItemsModel);
        roomItemsList.setBorder(BorderFactory.createTitledBorder("Room Items"));
        sidePanel.add(new JScrollPane(roomItemsList));

        inventoryModel = new DefaultListModel<>();
        inventoryList = new JList<>(inventoryModel);
        inventoryList.setBorder(BorderFactory.createTitledBorder("Your Inventory"));
        sidePanel.add(new JScrollPane(inventoryList));

        add(sidePanel, BorderLayout.EAST);

        //Bottom Panel Commands, Messages, Movement Buttons
        JPanel bottomPanel = new JPanel(new BorderLayout());

        commandField = new JTextField();
        commandField.setBorder(BorderFactory.createTitledBorder("Enter Command"));
        bottomPanel.add(commandField, BorderLayout.NORTH);

        messageArea = new JTextArea(4, 40);
        messageArea.setEditable(false);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        messageArea.setBorder(BorderFactory.createTitledBorder("Messages"));
        bottomPanel.add(new JScrollPane(messageArea), BorderLayout.CENTER);

        // Movement Buttons
        JPanel directionPanel = new JPanel(new GridLayout(2, 3, 5, 5));
        directionPanel.setBorder(BorderFactory.createTitledBorder("Move"));

        northButton = new JButton("North");
        southButton = new JButton("South");
        eastButton = new JButton("East");
        westButton = new JButton("West");

        directionPanel.add(new JLabel());
        directionPanel.add(northButton);
        directionPanel.add(new JLabel());
        directionPanel.add(southButton);
        directionPanel.add(eastButton);
        directionPanel.add(westButton);

        bottomPanel.add(directionPanel, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);

        //Input Handler
        commandField.addActionListener(e -> {
            String command = commandField.getText().trim().toLowerCase();
            processCommand(command);
            commandField.setText("");
        });

        //Movement Button Handlers
        ActionListener moveListener = e ->
                processCommand(e.getActionCommand().toLowerCase());

        northButton.addActionListener(moveListener);
        southButton.addActionListener(moveListener);
        eastButton.addActionListener(moveListener);
        westButton.addActionListener(moveListener);
        
     // Label the window with the player's name
        setTitle("MUD Adventure - " + player.getName());
     
    }

    private void processCommand(String command) {
    	

        String message;

        if (command.equals("exit")) {
            dispose();
            System.exit(0);
            return;
        }

        //dab command
        if (command.equals("dab")) {
            Player other = world.getOtherPlayer(player);

            if (other != null) {
                String result = player.dab(other);

                //Show message on both players' windows
                world.broadcastMessage(result);

                messageArea.setText(result);
                return;
            }
           
        }
        // Give command: "give <item> to <player>"
        if (command.startsWith("give ")) {
            String[] parts = command.substring(5).split(" to ");
            if (parts.length == 2) {
                String itemName = parts[0].trim();
                String targetName = parts[1].trim();

                Player other = null;
                for (Player p : world.getPlayers()) {
                    if (p.getName().equalsIgnoreCase(targetName)) {
                        other = p;
                        break;
                    }
                }

                message = player.giveItem(itemName, other);
                messageArea.setText(message);
                return;
            } else {
                messageArea.setText("Invalid command. Use: give <item> to <player>");
                return;
            }
        }
        
        
        

        //Swim command
        if (command.equals("swim")) {
            message = player.swim();
            messageArea.setText(message);
            return;
        }

        if (command.startsWith("get "))
            message = player.getItem(command.substring(4));
        else if (command.startsWith("drop "))
            message = player.dropItem(command.substring(5));
        else if (Arrays.asList("north", "south", "east", "west").contains(command))
            message = player.move(command);
        else
            message = "Unknown command.";

        messageArea.setText(message);
        updateDisplay();
    }


    //Update All UI
    private void updateDisplay() {

        Room room = player.getCurrentRoom();

        roomLabel.setText(room.getDescription());
        roomInfo.setText(room.showRoom());

        //Room items
        roomItemsModel.clear();
        for (String s : room.getItemNames())
            roomItemsModel.addElement(s);

        //Inventory
        inventoryModel.clear();
        for (String s : player.getInventoryNames())
            inventoryModel.addElement(s);

        //Mobs
        updateMobPanel();

        //Room image
        String roomName = room.getDescription().toLowerCase();
        ImageIcon icon = null;

        try {
            if (roomName.contains("hall"))
                icon = new ImageIcon(getClass().getResource("/images/hall.jpg"));
            else if (roomName.contains("kitchen"))
                icon = new ImageIcon(getClass().getResource("/images/kitchen.jpg"));
            else if (roomName.contains("attic"))
                icon = new ImageIcon(getClass().getResource("/images/attic.jpg"));
            else if (roomName.contains("bedroom"))
                icon = new ImageIcon(getClass().getResource("/images/bedroom.jpg"));
            else if (roomName.contains("bathroom"))
                icon = new ImageIcon(getClass().getResource("/images/bathroom.jpg"));
            else if (roomName.contains("basement"))
                icon = new ImageIcon(getClass().getResource("/images/basement.jpg"));
            else if (roomName.contains("chamber"))
                icon = new ImageIcon(getClass().getResource("/images/chamber.jpg"));
            else if (roomName.contains("swimpool"))
                icon = new ImageIcon(getClass().getResource("/images/swimpool.jpg"));
        } catch (Exception e) {
            System.out.println("Couldn't load image for: " + roomName);
        }

        if (icon != null)
            roomImage.setIcon(new ImageIcon(icon.getImage().getScaledInstance(700, 200, Image.SCALE_SMOOTH)));
        else
            roomImage.setIcon(null);

        //Enable/disable movement buttons
        northButton.setEnabled(room.hasExit("north"));
        southButton.setEnabled(room.hasExit("south"));
        eastButton.setEnabled(room.hasExit("east"));
        westButton.setEnabled(room.hasExit("west"));
    }

    //Update Mob Panel
    private void updateMobPanel() {
        Room playerRoom = player.getCurrentRoom();
        StringBuilder sb = new StringBuilder();
        boolean mobFound = false;

        for (Mob mob : world.getMobs()) {
            if (mob.getCurrentRoom() == playerRoom) {
                sb.append(mob.getName()).append(" is here.\n");
                mobFound = true;
            }
        }

        if (!mobFound)
            sb.append("No mobs here.\n");

        mobArea.setText(sb.toString());
    }
    
    
    public void showMessage(String msg) {
        messageArea.setText(msg);
    }

    //Launch GUI
    public void launchGUI() {
        SwingUtilities.invokeLater(() -> setVisible(true));
    }
}


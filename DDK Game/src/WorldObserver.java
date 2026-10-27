public interface WorldObserver {
    void onMobMoved(Mob mob, Room newRoom);
    void onPlayerMoved(Player player, Room newRoom);
    void onRoomUpdated(Room room);
    void onMessage (String msg);
}
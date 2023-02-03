package beats.game;

public enum BeatType {
    up(0, "up"),
    down(1, "down"),
    left(2, "left"),
    right(3, "right"),
    click(4, "click"),
    set(5, "set");

    private final int id;
    private final String jsonval;

    private BeatType(int id, String jsonval) {
        this.id = id;
        this.jsonval = jsonval;

    }

    public String toString() {
        return jsonval;
    }

    public static int getID(String json) {
        for (BeatType t : BeatType.values()) {
            if (t.jsonval.equals(json)) return t.id;
        }
        return -1;
    }

    public static BeatType getType(int id) {
        for (BeatType t : BeatType.values()) {
            if (t.id == id) return t;
        }
        return null;
    }
}

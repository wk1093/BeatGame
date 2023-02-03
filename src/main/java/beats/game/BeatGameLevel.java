package beats.game;

import beats.util.FileUtils;
import org.joml.Vector2f;
import org.json.*;

import java.util.Vector;

public class BeatGameLevel {
    private String name;
    private String song;
    private String composer;
    private String author;
    private int difficulty;
    private double offsetms;
    private double length;
    private double bpm;
    private Vector<JsonBeat> beatmap;
    private boolean noBPM;

    public BeatGameLevel(String path) {
        System.out.println("Loading beatmap '"+path+"':\n");
        // open file
        String f = FileUtils.readFile(path);
        JSONObject obj = new JSONObject(f);

        name = obj.getString("name");
        song = obj.getString("song");
        try { composer = obj.getString("composer"); } catch (JSONException e) { composer = "Unknown"; }
        try { author = obj.getString("author"); } catch (JSONException e) { author = "Unknown"; }
        difficulty = obj.getInt("difficulty");
        try { offsetms = obj.getDouble("offsetms"); } catch (JSONException e) { offsetms = 0.0; }
        try { length = obj.getDouble("length"); } catch (JSONException e) { length = 0.0; }
        try { bpm = obj.getDouble("bpm"); } catch (JSONException e) { bpm = 0.0; noBPM = true; }
        JSONArray map = obj.getJSONArray("beatmap");
        beatmap = new Vector<JsonBeat>();
        for (int i = 0; i < map.length(); i++) {
            JSONObject o = map.getJSONObject(i);
            beatmap.add(new JsonBeat(o));
        }
        System.out.println("Level '"+name+"':");
        System.out.println("\tsong: '"+song+"'");
        System.out.println("\tcomposer: '"+composer+"'");
        System.out.println("\tauthor: '"+author+"'");
        System.out.println("\tdifficulty: "+difficulty);
        System.out.println("\tsong offset (ms): "+offsetms);
        System.out.println("\tlength (ms): "+length);
        System.out.println("\tBPM (ms): "+bpm);
        System.out.println("\t BEATS("+beatmap.size()+"):");

        for (JsonBeat beat : beatmap) {
            System.out.println("\t\t"+beat.getType());
            System.out.println("\t\t\ttime (ms): "+beat.getTimems());
            if (beat.getType() == BeatType.set) {
                System.out.println("\t\t\tbpm: "+beat.getBpm());
            } else {
                System.out.println("\t\t\tpos: (" + beat.getPos().x + ", " + beat.getPos().y + ")");
                System.out.println("\t\t\treaction (ms): " + beat.getReactionms());
            }
        }

    }

}

class JsonBeat {
    private BeatType type;
    // u/d/l/r/c
    private double timems;
    private Vector2f pos; // this position is not in pixel coords, it is from 0-500 on x and y, starting on the bottom-left
    private double reactionms;

    // set
    private double bpm;


    public BeatType getType() {
        return type;
    }

    public void setType(BeatType type) {
        this.type = type;
    }

    public double getTimems() { // TODO: Add math to calculate timems in beat mode
        return timems;
    }

    public void setTimems(double timems) {
        this.timems = timems;
    }

    public Vector2f getPos() {
        return pos;
    }

    public void setPos(Vector2f pos) {
        this.pos = pos;
    }

    public double getReactionms() {
        return reactionms;
    }

    public void setReactionms(double reactionms) {
        this.reactionms = reactionms;
    }

    public double getBpm() {
        return bpm;
    }

    public void setBpm(double bpm) {
        this.bpm = bpm;
    }

    public JsonBeat(JSONObject json) {
        switch(json.getString("type")) {
            case "up": type = BeatType.up; break;
            case "down": type = BeatType.down; break;
            case "left": type = BeatType.left; break;
            case "right": type = BeatType.right; break;
            case "click": type = BeatType.click; break;
            case "set": type = BeatType.set; break;
            default:
                throw new IllegalStateException("Invalid Beat Type");
        }

        timems = json.getDouble("time");
        if (type == BeatType.set) {
            bpm = json.getDouble("bpm");
        } else {
            double x = json.getDouble("x");
            double y = json.getDouble("y");
            pos = new Vector2f((float) x, (float)y);
            try { reactionms = json.getDouble("reaction"); } catch (JSONException e) { reactionms = 0.5; }
        }
    }
}

enum BeatType {
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

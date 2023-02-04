package beats.game;

import beats.Sound;
import beats.ecs.GameObject;
import beats.util.AssetPool;
import beats.util.FileUtils;
import org.joml.Vector2f;
import org.json.*;
import beats.game.BeatType;
import beats.game.JsonBeat;

import java.util.Vector;

public class BeatGameLevel {
    private String name;
    private String composer;
    private String author;

    public String getName() {
        return name;
    }

    public String getComposer() {
        return composer;
    }

    public String getAuthor() {
        return author;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public double getOffsetms() {
        return offsetms;
    }

    public double getLength() {
        return length;
    }

    public double getBpm() {
        return bpm;
    }

    public Vector<JsonBeat> getBeatmap() {
        return beatmap;
    }

    public boolean isNoBPM() {
        return noBPM;
    }

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
        try { composer = obj.getString("composer"); } catch (JSONException e) { composer = "Unknown"; }
        try { author = obj.getString("author"); } catch (JSONException e) { author = "Unknown"; }
        difficulty = obj.getInt("difficulty");
        try { offsetms = obj.getDouble("offsetms"); } catch (JSONException e) { offsetms = 0.0; }
        length = obj.getDouble("length");
        try { bpm = obj.getDouble("bpm"); } catch (JSONException e) { bpm = 0.0; noBPM = true; }
        JSONArray map = obj.getJSONArray("beatmap");
        beatmap = new Vector<JsonBeat>();
        for (int i = 0; i < map.length(); i++) {
            JSONObject o = map.getJSONObject(i);
            beatmap.add(new JsonBeat(o));
        }
        System.out.println("Level '"+name+"':");
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


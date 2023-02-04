package beats.game;

import beats.ecs.GameObject;
import org.joml.Vector2f;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonBeat {
    private BeatType type;
    // u/d/l/r/c
    private double timems;
    private Vector2f pos; // this position is not in pixel coords, it is from 0-500 on x and y, starting on the bottom-left
    private double reactionms;

    public GameObject obj;
    public GameObject circle;
    public GameObject outline;

    public boolean started = false;
    public boolean finished = false;
    public boolean played = false;


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

    public JsonBeat(JSONObject json) {
        switch (json.getString("type")) {
            case "up":
                type = BeatType.up;
                break;
            case "down":
                type = BeatType.down;
                break;
            case "left":
                type = BeatType.left;
                break;
            case "right":
                type = BeatType.right;
                break;
            case "click":
                type = BeatType.click;
                break;
            case "set":
                type = BeatType.set;
                break;
            default:
                throw new IllegalStateException("Invalid Beat Type");
        }

        timems = json.getDouble("time");
        if (type != BeatType.set) {
            double x = json.getDouble("x");
            double y = json.getDouble("y");
            pos = new Vector2f((float) x, (float) y);
            try {
                reactionms = json.getDouble("reaction");
            } catch (JSONException e) {
                reactionms = 500;
            }
        }
    }
}

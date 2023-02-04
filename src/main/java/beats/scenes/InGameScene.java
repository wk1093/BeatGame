package beats.scenes;

import beats.Scene;
import beats.Window;
import beats.ecs.GameObject;
import beats.ecs.Transform;
import beats.ecs.components.SpriteRenderer;
import beats.game.BeatGameLevel;
import beats.game.BeatType;
import beats.game.JsonBeat;
import beats.renderer.SpriteSheet;
import beats.util.AssetPool;
import beats.util.Time;
import org.joml.Vector2f;

public class InGameScene extends Scene {
    private BeatGameLevel level;
    private SpriteSheet sprites;
    private double startTime = 0.0;
    int countdown_num = 3;
    GameObject countdown;
    boolean started_game = false;


    public InGameScene() {
        super();
    }

    private void loadResources() {
        AssetPool.getShader("assets/shaders/default.glsl");
        AssetPool.addSpriteSheet("assets/images/spritesheet.png", new SpriteSheet(AssetPool.getTexture("assets/images/spritesheet.png"), 32, 32, 9, 0));
    }

    @Override
    public void init() {
        super.init();
        loadResources();

        sprites = AssetPool.getSpriteSheet("assets/images/spritesheet.png");
        assert sprites != null;

        level = new BeatGameLevel("assets/levels/" + Window.level + ".json");
        Vector2f pos = new Vector2f(Window.getWidth()/2.0f - 50, Window.getHeight()/2.0f - 50);
        countdown = new GameObject("Countdown", new Transform(pos, new Vector2f(100, 100)), 0);
        countdown.addComponent(new SpriteRenderer(sprites.getSprite(6)));
        addGameObjectToScene(countdown);
        startTime = Time.getTime();
    }

    private double elapsed() {
        return Time.getTime() - startTime;
    }

    private double time() {
        return Time.getTime() - startTime - 3 - level.getOffsetms()/1000.0;
    }

    public void update(float dt) {
        if (!started_game) {
            if (elapsed() >= 1.0f && countdown_num == 3) {
                countdown_num = 2;
                countdown.getComponent(SpriteRenderer.class).setSprite(sprites.getSprite(7));
            }
            if (elapsed() >= 2.0f && countdown_num == 2) {
                countdown_num = 1;
                countdown.getComponent(SpriteRenderer.class).setSprite(sprites.getSprite(8));
                countdown.getComponent(SpriteRenderer.class).dirty();
            }
            if (elapsed() >= 3.0f && countdown_num == 1) {
                countdown_num = 0;
                started_game = true;
                removeGameObjectFromScene(countdown);
                gameStart();
            }
        }

        if (started_game) {
            //startTime = Time.getTime();
            gameLoop(dt);
        }

        for (GameObject go : this.gameObjects) {
            go.update(dt);
        }
        this.renderer.render();
    }



    // ~~~~~~~~~~~~~~~~~~~~~~~ GAME LOGIC ~~~~~~~~~~~~~~~~~~~~~~~
    public Vector2f fromBeatCoord(float x, float y) {
        // x is a value between 0 and 500 starting from the left
        // y is a value between 0 and 500 starting from the bottom
        // returns a Vector2f with the position in pixels on the entire screen
        float x_pos = x / 500.0f * Window.getWidth();
        float y_pos = y / 500.0f * Window.getHeight();
        return new Vector2f(x_pos, y_pos);

    }

    public void gameStart() {

    }

    private final static float BEAT_SIZE = 40.0f;

    public void gameLoop(float dt) {
        //System.out.println("Elapsed: " + time() + "s");
        for (JsonBeat beat : level.getBeatmap()) {
            if (beat.getTimems() - beat.getReactionms() <= time()*1000 && !beat.started) { //
                System.out.println("Starting beat at " + beat.getTimems() + "ms");
                beat.started = true;
                if (beat.getType() != BeatType.set) {
                    Vector2f centerpos = fromBeatCoord(beat.getPos().x, beat.getPos().y);
                    beat.obj = new GameObject("Beat", new Transform(new Vector2f(centerpos.x - BEAT_SIZE/2.0f, centerpos.y - BEAT_SIZE/2.0f), new Vector2f(BEAT_SIZE, BEAT_SIZE)), 0);
                    beat.obj.addComponent(new SpriteRenderer(sprites.getSprite(beat.getType().ordinal())));
                    addGameObjectToScene(beat.obj);
                    beat.circle = new GameObject("Circle", new Transform(new Vector2f(centerpos.x - BEAT_SIZE, centerpos.y - BEAT_SIZE), new Vector2f(BEAT_SIZE*2, BEAT_SIZE*2)), 0);
                    beat.circle.addComponent(new SpriteRenderer(sprites.getSprite(5)));
                    addGameObjectToScene(beat.circle);
                }
            } else if (beat.started && beat.getTimems() <= time()*1000 && !beat.finished) {
                System.out.println("Finishing beat at " + beat.getTimems() + "ms");
                beat.finished = true;
                if (beat.getType() != BeatType.set) {
                    removeGameObjectFromScene(beat.obj);
                    removeGameObjectFromScene(beat.circle);
                }
            } else if (!beat.finished && beat.started) {
                // adjust size of circle
                // when it is first started it is 2X the size of the beat
                // when it is finished it is 1X the size of the beat
                // when it is in the middle it is 1.5X the size of the beat
                float circle_size = 0.0f;
                float time_since_start = (float) (time()*1000 - beat.getTimems() + beat.getReactionms());
                float size = time_since_start / (float) beat.getReactionms();
                if (size < 0.5f) {
                    circle_size = 2.0f - size*2.0f;
                } else if (size < 1.0f) {
                    circle_size = 1.0f;
                } else {
                    circle_size = 1.0f - (size - 1.0f);
                }
                Vector2f centerpos = fromBeatCoord(beat.getPos().x, beat.getPos().y);
                beat.circle.transform.scale = (new Vector2f(BEAT_SIZE*2*circle_size, BEAT_SIZE*2*circle_size));
                beat.circle.transform.position = new Vector2f(centerpos.x - BEAT_SIZE*circle_size, centerpos.y - BEAT_SIZE*circle_size);
            }

        }
    }
}

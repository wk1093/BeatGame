package beats.scenes;

import beats.ImGuiLayer;
import beats.Scene;
import beats.Window;
import beats.ecs.GameObject;
import beats.ecs.Transform;
import beats.ecs.components.SpriteRenderer;
import beats.game.BeatGameLevel;
import beats.game.BeatType;
import beats.game.JsonBeat;
import beats.input.KeyListener;
import beats.input.MouseListener;
import beats.renderer.SpriteSheet;
import beats.util.AssetPool;
import beats.util.Time;
import imgui.ImGui;
import org.joml.Vector2f;
import org.joml.Vector4f;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;

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
        for (String level : Window.levels) {
            AssetPool.addSound("assets/levels/" + level + ".ogg", false);
        }
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
                AssetPool.getSound("assets/levels/" + Window.level + ".ogg").play();
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

    private final static float BEAT_SIZE = 40.0f;
    private float health = 100.0f;

    public void gameLoop(float dt) {
        // half of reaction is before beat, half is after
        health -= dt * 10.0f;
        for (JsonBeat beat : level.getBeatmap()) {
            if (beat.getTimems() - beat.getReactionms()/2.0f <= time()*1000 && !beat.started) { // show beat
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
                    beat.outline = new GameObject("Circle", new Transform(new Vector2f(centerpos.x - BEAT_SIZE/2.0f, centerpos.y - BEAT_SIZE/2.0f), new Vector2f(BEAT_SIZE, BEAT_SIZE)), 0);
                    beat.outline.addComponent(new SpriteRenderer(sprites.getSprite(5)));
                    addGameObjectToScene(beat.outline);
                }
            } else if (beat.started && beat.getTimems() + beat.getReactionms()/2.0f <= time()*1000 && !beat.finished) { // hide beat
                beat.finished = true;
                if (beat.getType() != BeatType.set) {
                    removeGameObjectFromScene(beat.obj);
                    removeGameObjectFromScene(beat.circle);
                    removeGameObjectFromScene(beat.outline);
                }
                Vector2f centerpos = fromBeatCoord(beat.getPos().x, beat.getPos().y);
                if (beat.getType().pressed() && !beat.played) {
                    float mx = MouseListener.getX();
                    float my = MouseListener.getY();
                    float dist = (float) Math.sqrt((mx - centerpos.x) * (mx - centerpos.x) + (my - centerpos.y) * (my - centerpos.y));
                    if (dist <= BEAT_SIZE / 2.0f) {
                        beat.played = true;

                    }
                }

                if (!beat.played) {
                    System.out.println("Missed beat at " + beat.getTimems() + "ms");
                    health -= 50.0f;
                }
            } else if (!beat.finished && beat.started) {
                // adjust size of circle
                // when it is first started it is 2X the size of the beat
                // when it is finished it is 1X the size of the beat
                // when it is in the middle it is 1.5X the size of the beat
                float circle_size = 0.0f;
                float time_since_start = (float) (time()*1000 - beat.getTimems() + beat.getReactionms()/2.0f);
                float size = time_since_start / (float) (beat.getReactionms()/2.0f);
                // when size = 0.0f, circle_size = 2.0f
                // when size = 0.5f, circle_size = 1.5f
                // when size = 1.0f, circle_size = 1.0f
                // and all in between
                circle_size = 2.0f - size;
                circle_size /= 2.0f;
                Vector2f centerpos = fromBeatCoord(beat.getPos().x, beat.getPos().y);
                beat.circle.transform.scale = (new Vector2f(BEAT_SIZE*2*circle_size, BEAT_SIZE*2*circle_size));
                beat.circle.transform.position = new Vector2f(centerpos.x - BEAT_SIZE*circle_size, centerpos.y - BEAT_SIZE*circle_size);
                circle_size *= 2.0f;
                if (beat.getType().pressed() && !beat.played) {
                    float mx = MouseListener.getX();
                    float my = MouseListener.getY();
                    float dist = (float) Math.sqrt((mx - centerpos.x) * (mx - centerpos.x) + (my - centerpos.y) * (my - centerpos.y));
                    if (dist <= BEAT_SIZE / 2.0f) {
                        // the mouse is inside the circle
                        beat.played = true;
                        beat.obj.getComponent(SpriteRenderer.class).setColor(new Vector4f(0.2f, 1.0f, 0.2f, 1.0f));
                        // the heath is increased, if thew circle is smaller you get more health
                        // circle_size = 1.0f -> 10.0f
                        // circle_size = 2.0f -> 1.0f
                        // circle_size = 0.5f -> 1.0f
                        if (circle_size > 1.0f) {
                            health += 10.0f * (1.8f / circle_size) - 8.0f;
                            if (health > 100.0f) {
                                health = 100.0f;
                            }
                            System.out.println("EARLY");
                        } else {
                            System.out.println("LATE");
                            float diff = 1.0f - circle_size;
                            if (Math.abs(diff) < 0.2f) {
                                health += 10.0f;
                                if (health > 100.0f) {
                                    health = 100.0f;
                                }
                            } else {
                                health += 2.0f;
                                if (health > 100.0f) {
                                    health = 100.0f;
                                }
                            }
                        }
                    }
                }
            }

        }
        if (getProgress() >= 1.3f) {
            System.out.println("Game finished");
            started_game = false;
            Window.changeScene(1);
            AssetPool.getSound("assets/levels/" + Window.level + ".ogg").stop();
        }
    }

    private float getProgress() {
        return (float) (time()*1000 / level.getLength());
    }

    @Override
    public void imgui() {
        ImGuiLayer.beginFullscreen("Game");

        ImGui.setCursorPos(ImGui.getCursorPosX(), 15);
        ImGui.progressBar(health/100.0f, Window.getWidth()-30, 15, "");
        ImGui.setCursorPos(ImGui.getCursorPosX(), Window.getHeight()-30);
        ImGui.progressBar(getProgress(), Window.getWidth()-30, 15, "");


        ImGui.end();
    }
}

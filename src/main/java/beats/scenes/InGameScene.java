package beats.scenes;

import beats.Scene;
import beats.ecs.GameObject;
import beats.ecs.components.SpriteRenderer;
import beats.game.BeatGameLevel;
import beats.renderer.Camera;
import beats.renderer.Shader;
import beats.renderer.Texture;
import beats.util.ShaderParser;
import beats.util.Time;
import beats.util.TimeVal;
import beats.util.VertexAttribBuilder;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class InGameScene extends Scene {
    private float[] vertexArray = {
            //   x       y     z      r     g     b     a   u  v
            100.5f,   0.5f, 0.0f,  1.0f, 0.0f, 0.0f, 1.0f,  1, 1, //br
              0.5f, 100.5f, 0.0f,  0.0f, 1.0f, 0.0f, 1.0f,  0, 0, //tl
            100.5f, 100.5f, 0.0f,  1.0f, 0.0f, 1.0f, 1.0f,  1, 0, //tr
              0.5f,   0.5f, 0.0f,  1.0f, 1.0f, 0.0f, 1.0f,  0, 1, //bl

    };

    // counter-clockwise
    private int[] elementArray = {
            2, 1, 0,
            0, 1, 3,
    };

    private int vaoID, vboID, eboID;

    private Shader defaultShader;
    private Texture testTexture;
    private VertexAttribBuilder vertexAttribBuilder;
    GameObject testObj;

    public InGameScene() {
        super();
    }

    @Override
    public void init() {
        super.init();
        this.testObj = new GameObject("Test Object");
        //this.testObj.addComponent(new SpriteRenderer());
        this.addGameObjectToScene(this.testObj);

        defaultShader = new Shader("assets/shaders/default.glsl");
        defaultShader.compile();
        testTexture = new Texture("assets/images/test.jpg");

        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();
        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        vertexAttribBuilder = new VertexAttribBuilder(3, 4, 2); // vec3 position, vec3 color, vec2 texcoord
        vertexAttribBuilder.build();


        camera.position.x -= 50.0f;
        camera.position.y -= 50.0f;
    }



    public void update(float dt) {
        defaultShader.use();
        defaultShader.uploadTexture("uTexture", 0);
        glActiveTexture(GL_TEXTURE0);
        testTexture.bind();
        defaultShader.uploadMat4f("uProjection", camera.getProjectionMatrix());
        defaultShader.uploadMat4f("uView", camera.getViewMatrix());
        defaultShader.uploadFloat("uTime", Time.getTime());
        glBindVertexArray(vaoID);
        vertexAttribBuilder.enable();
        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);
        vertexAttribBuilder.disable();
        glBindVertexArray(0);
        defaultShader.detach();

        for (GameObject go : this.gameObjects) {
            go.update(dt);
        }

    }
}

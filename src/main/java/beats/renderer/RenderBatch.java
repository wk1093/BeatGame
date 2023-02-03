package beats.renderer;

import beats.Window;
import beats.ecs.components.SpriteRenderer;
import beats.util.VertexAttribBuilder;
import org.joml.Vector4f;

import static org.lwjgl.opengl.GL30.*;

public class RenderBatch {
    VertexAttribBuilder vertexAttribBuilder;

    private SpriteRenderer[] sprites;
    private int numSprites;
    private boolean hasRoom;
    private float[] vertices;

    private int vaoID, vboID;
    private int maxBatchSize;
    private Shader shader;

    public RenderBatch(int maxBatchSize) {
        shader = new Shader("assets/shaders/default.glsl");
        shader.compile();

        // POS: float float, COLOR: float float float float
        vertexAttribBuilder = new VertexAttribBuilder(2, 4);

        this.sprites = new SpriteRenderer[maxBatchSize];
        this.maxBatchSize = maxBatchSize;
        this.numSprites = 0;
        this.hasRoom = true;

        vertices = new float[maxBatchSize * 4 * vertexAttribBuilder.getSize()];
    }

    public void start() {

        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, (long) vertices.length * Float.BYTES, GL_DYNAMIC_DRAW);

        int eboID = glGenBuffers();
        int[] indices = generateIndices();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        vertexAttribBuilder.build();

    }

    public void addSprite(SpriteRenderer spr) {
        int index = numSprites;
        sprites[index] = spr;
        numSprites++;

        loadVertexProperties(index);

        if (numSprites >= maxBatchSize) {
            hasRoom = false;
        }
    }

    public void render() {
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);

        shader.use();
        shader.uploadMat4f("uProjection", Window.getScene().camera().getProjectionMatrix());
        shader.uploadMat4f("uView", Window.getScene().camera().getViewMatrix());

        glBindVertexArray(vaoID);
        vertexAttribBuilder.enable();
        glDrawElements(GL_TRIANGLES, numSprites * 6, GL_UNSIGNED_INT, 0);
        vertexAttribBuilder.disable();
        glBindVertexArray(0);

    }

    private void loadVertexProperties(int index) {
        SpriteRenderer sprite = sprites[index];

        int off = index * 4 * vertexAttribBuilder.getSize();

        Vector4f color = sprite.getColor();
        float xAdd = 1.0f;
        float yAdd = 1.0f;
        for (int i = 0;i < 4; i++) {
            if (i == 1) {
                yAdd = 0.0f;
            } else if (i == 2) {
                xAdd = 0.0f;
            } else if (i == 3) {
                yAdd = 1.0f;
            }

            vertices[off] = sprite.gameObject.transform.position.x + (xAdd * sprite.gameObject.transform.scale.x);
            vertices[off+1] = sprite.gameObject.transform.position.y + (yAdd * sprite.gameObject.transform.scale.y);

            vertices[off+2] = color.x;
            vertices[off+3] = color.y;
            vertices[off+4] = color.z;
            vertices[off+5] = color.w;

            off += vertexAttribBuilder.getSize();
        }
    }

    private int[] generateIndices() {
        // 6 indices per quad (3 per triangle)
        int[] elements = new int[maxBatchSize * 6];
        for (int i = 0; i < maxBatchSize; i++) {
            loadElementIndices(elements, i);
        }
        return elements;
    }

    private void loadElementIndices(int[] elements, int index) {
        int oai = index * 6;
        int off = index * 4;
        elements[oai] = off + 3;
        elements[oai + 1] = off + 2;
        elements[oai + 2] = off;

        elements[oai + 3] = off;
        elements[oai + 4] = off + 2;
        elements[oai + 5] = off + 1;
    }

    public boolean hasRoom() {
        return hasRoom;
    }
}

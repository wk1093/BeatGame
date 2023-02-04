package beats.renderer;

import beats.Loggable;
import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.*;

public class Texture implements Loggable {
    private String filepath;
    private int texID;
    private int width, height;

    public Texture(String filepath) {
        this.filepath = filepath;

        texID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texID);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);
        stbi_set_flip_vertically_on_load(true);
        ByteBuffer image = stbi_load(filepath, width, height, channels, 0);
        if (image != null) {
            this.width = width.get(0);
            this.height = height.get(0);
            if (channels.get(0) == 4) {
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(), height.get(), 0, GL_RGBA, GL_UNSIGNED_BYTE, image);
            } else if (channels.get(0) == 3) {
                LOGGER.info("Texture '" + filepath + "' has no alpha channel!");
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width.get(), height.get(), 0, GL_RGB, GL_UNSIGNED_BYTE, image);
            } else {
                LOGGER.severe("Unknown texture format in texture: '" + filepath + "'");
                throw new IllegalStateException("Failed to load texture!");
            }
        } else {
            LOGGER.severe("Failed to load texture: '" + filepath + "'");
            throw new IllegalStateException("Failed to load texture!");
        }
        stbi_image_free(image);
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, texID);
    }

    public void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}

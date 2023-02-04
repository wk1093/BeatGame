package beats.util;

import beats.Loggable;
import beats.renderer.Shader;
import beats.renderer.SpriteSheet;
import beats.renderer.Texture;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class AssetPool implements Loggable {
    private static Map<String, Shader> shaders = new HashMap<>();
    private static Map<String, Texture> textures = new HashMap<>();
    private static Map<String, SpriteSheet> spriteSheets = new HashMap<>();

    public static Shader getShader(String resourceName) {
        File file = new File(resourceName);
        if (shaders.containsKey(file.getAbsolutePath())) {
            return shaders.get(file.getAbsolutePath());
        }
        Shader shader = new Shader(resourceName);
        shader.compile();
        shaders.put(file.getAbsolutePath(), shader);
        return shader;
    }

    public static Texture getTexture(String resourceName) {
        File file = new File(resourceName);
        if (textures.containsKey(file.getAbsolutePath())) {
            return textures.get(file.getAbsolutePath());
        }
        Texture texture = new Texture(resourceName);
        textures.put(file.getAbsolutePath(), texture);
        return texture;
    }

    public static void addSpriteSheet(String resourceName, SpriteSheet spriteSheet) {
        File file = new File(resourceName);
        if (!spriteSheets.containsKey(file.getAbsolutePath())) {
            spriteSheets.put(file.getAbsolutePath(), spriteSheet);
        }
    }
    public static SpriteSheet getSpriteSheet(String resourceName) {
        File file = new File(resourceName);
        if (spriteSheets.containsKey(file.getAbsolutePath())) {
            return spriteSheets.get(file.getAbsolutePath());
        }
        LOGGER.warning("Sprite sheet not found: " + resourceName);
        return null;
    }
}

package beats.util;

import beats.Loggable;
import beats.Sound;
import beats.renderer.Shader;
import beats.renderer.SpriteSheet;
import beats.renderer.Texture;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class AssetPool implements Loggable {
    private static Map<String, Shader> shaders = new HashMap<>();
    private static Map<String, Texture> textures = new HashMap<>();
    private static Map<String, SpriteSheet> spriteSheets = new HashMap<>();
    private static Map<String, Sound> sounds = new HashMap<>();

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

    public static Collection<Sound> getAllSounds() {
        return sounds.values();
    }

    public static Sound getSound(String soundFile) {
        File file = new File(soundFile);
        if (sounds.containsKey(file.getAbsolutePath())) {
            return sounds.get(file.getAbsolutePath());
        } else {
            assert false : "Sound file not added '" + soundFile + "'";
        }

        return null;
    }

    public static Sound addSound(String soundFile, boolean loops) {
        File file = new File(soundFile);
        if (sounds.containsKey(file.getAbsolutePath())) {
            return sounds.get(file.getAbsolutePath());
        } else {
            Sound sound = new Sound(file.getAbsolutePath(), loops);
            AssetPool.sounds.put(file.getAbsolutePath(), sound);
            return sound;
        }
    }
}

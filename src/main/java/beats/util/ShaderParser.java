package beats.util;

import beats.Loggable;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;

public class ShaderParser implements Loggable {
    // finds //#//vertex and //#//fragment
    // everything after //#//vertex is vertex shader
    // everything after //#//fragment is fragment shader
    public static String[] parse(String src_file) {
        String src = FileUtils.readFile(src_file);
        String[] shaders = new String[2];
        shaders[0] = "";
        shaders[1] = "";
        String[] lines = src.split("\n");
        int vertexStart = -1;
        int fragmentStart = -1;
        for (int i = 0; i < lines.length; i++) {
            if (lines[i].contains("//#//vertex")) {
                vertexStart = i;
            }
            if (lines[i].contains("//#//fragment")) {
                fragmentStart = i;
            }
        }
        if (vertexStart == -1 || fragmentStart == -1) {
            LOGGER.severe("Invalid shader file");
            throw new IllegalArgumentException("Invalid shader file");
        }
        for (int i = vertexStart + 1; i < fragmentStart; i++) {
            if (lines[i] == null || lines[i].equals("")) {
                continue;
            }
            shaders[0] += lines[i] + "\n";

        }
        for (int i = fragmentStart + 1; i < lines.length; i++) {
            if (lines[i] == null || lines[i].equals("")) {
                continue;
            }
            shaders[1] += lines[i] + "\n";
        }
        return shaders;
    }

    public static int createShader(String src, int type, String filename) {
        int id = glCreateShader(type);
        glShaderSource(id, src);
        glCompileShader(id);
        int res = glGetShaderi(id, GL_COMPILE_STATUS);
        if (res == GL_FALSE) {
            int len = glGetShaderi(id, GL_INFO_LOG_LENGTH);
            LOGGER.severe("ERROR in '"+filename+"':\n\tShader Compilation Failed!");
            LOGGER.severe(glGetShaderInfoLog(id, len));
            throw new IllegalStateException("Failed to compile shader '"+filename+"'!");
        }

        return id;
    }

    public static int[] createProgram(String path, String filename) {
        // input path, filename ex: "assets/shaders/" and "default.glsl"
        // output vertexID, fragmentID, programID
        String[] shaders = ShaderParser.parse(path+filename);
        int vertexID = ShaderParser.createShader(shaders[0], GL_VERTEX_SHADER, filename+":vertex");
        int fragmentID = ShaderParser.createShader(shaders[1], GL_FRAGMENT_SHADER, filename+":fragment");
        int shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram, vertexID);
        glAttachShader(shaderProgram, fragmentID);
        glLinkProgram(shaderProgram);
        int res = glGetProgrami(shaderProgram, GL_LINK_STATUS);
        if (res == GL_FALSE) {
            int len = glGetProgrami(shaderProgram, GL_INFO_LOG_LENGTH);
            LOGGER.severe("ERROR in '"+filename+"':\n\tProgram Linkage Failed!");
            LOGGER.severe(glGetProgramInfoLog(shaderProgram, len));
            throw new IllegalStateException("Failed to link shaders '"+filename+"'!");
        }
        return new int[] {vertexID, fragmentID, shaderProgram};
    }
}

package beats.renderer;

import beats.util.ShaderParser;
import org.joml.*;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL20.*;

public class Shader {
    private int shaderProgramID;
    private boolean beingUsed = false;
    private String filepath;


    public Shader(String filepath) {
        this.filepath = filepath;
    }

    public String[] getPathFile() { // returns path,file, ex: "assets/shaders/file.glsl": "assets/shaders/","file.glsl"
        String[] split_str = filepath.split("/");
        StringBuilder path = new StringBuilder();
        for (int i = 0; i < split_str.length-1; i++) {
            path.append(split_str[i]);
            path.append("/");
        }
        return new String[] {path.toString(), split_str[split_str.length-1]};
    }

    public void compile() {
        String[] pf = getPathFile();
        int[] ids = ShaderParser.createProgram(pf[0], pf[1]);
        shaderProgramID = ids[2];
    }

    public void use() {
        if (!beingUsed) {
            beingUsed = true;
            glUseProgram(shaderProgramID);
        }
    }

    public void detach() {
        glUseProgram(0);
        beingUsed = false;
    }

    public void uploadMat4f(String varName, Matrix4f mat) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
        mat.get(matBuffer);
        glUniformMatrix4fv(varLocation, false, matBuffer);
    }

    public void uploadMat3f(String varName, Matrix3f mat) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(9);
        mat.get(matBuffer);
        glUniformMatrix3fv(varLocation, false, matBuffer);
    }

    public void uploadVec4f(String varName, Vector4f vec) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform4f(varLocation, vec.x, vec.y, vec.z, vec.w);
    }

    public void uploadVec3f(String varName, Vector3f vec) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform3f(varLocation, vec.x, vec.y, vec.z);
    }
    public void uploadVec2f(String varName, Vector2f vec) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform2f(varLocation, vec.x, vec.y);
    }

    public void uploadFloat(String varName, float value) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform1f(varLocation, value);
    }

    public void uploadInt(String varName, int value) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform1i(varLocation, value);
    }

    public void uploadTexture(String varName, int slot) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform1i(varLocation, slot);
    }

    public void uploadIntArray(String varName, int[] values) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform1iv(varLocation, values);
    }
}

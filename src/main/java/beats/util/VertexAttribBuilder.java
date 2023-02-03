package beats.util;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

public class VertexAttribBuilder { // a class to build vertex attribute pointers
    private int[] attribs;

    public VertexAttribBuilder(int... attribs) {
        this.attribs = attribs;
    }

    public void build() {
        int offset = 0;
        int stride = 0;
        for (int i = 0; i < attribs.length; i++) {
            stride += attribs[i];
        }
        for (int i = 0; i < attribs.length; i++) {
            glVertexAttribPointer(i, attribs[i], GL_FLOAT, false, stride * Float.BYTES, (long) offset * Float.BYTES);
            glEnableVertexAttribArray(i);
            offset += attribs[i];
        }
    }

    public void enable() {
        for (int i = 0; i < attribs.length; i++) {
            glEnableVertexAttribArray(i);
        }
    }

    public void disable() {
        for (int i = 0; i < attribs.length; i++) {
            glDisableVertexAttribArray(i);
        }
    }




}

package com.game.framework.gl;

import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.glDisableVertexAttribArray;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glDrawElements;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glVertexAttribPointer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;


public class Vertices {
   
    final boolean hasColor;
    final boolean hasTexCoords;
    final int vertexSize;
    final IntBuffer vertices;
    final int[] tmpBuffer;
    final ShortBuffer indices;
    
    final static int POSITION_COMPONENT_COUNT = 2;
    final static int COLOR_COMPONENT_COUNT = 4;
    final static int TEXTURE_COMPONENT_COUNT = 2;
    
    private int aPositionLocation,aColorLocation,aTextureLocation;
    
    public Vertices(int maxVertices, int maxIndices, boolean hasColor, boolean hasTexCoords) {
       
        this.hasColor = hasColor;
        this.hasTexCoords = hasTexCoords;
        this.vertexSize = (2 + (hasColor?4:0) + (hasTexCoords?2:0)) * 4;
        this.tmpBuffer = new int[maxVertices * vertexSize / 4];
        
        ByteBuffer buffer = ByteBuffer.allocateDirect(maxVertices * vertexSize);
        buffer.order(ByteOrder.nativeOrder());
        vertices = buffer.asIntBuffer();
        
        if(maxIndices > 0) {
            buffer = ByteBuffer.allocateDirect(maxIndices * Short.SIZE / 8);
            buffer.order(ByteOrder.nativeOrder());
            indices = buffer.asShortBuffer();
        } else {
            indices = null;
        }
    }
    
    public void setVertices(float[] vertices, int offset, int length) {
        this.vertices.clear();
        int len = offset + length;
        for(int i=offset, j=0; i < len; i++, j++) 
            tmpBuffer[j] = Float.floatToRawIntBits(vertices[i]);
        this.vertices.put(tmpBuffer, 0, length);
        this.vertices.flip();
    }
    
    public void setIndices(short[] indices, int offset, int length) {
        this.indices.clear();
        this.indices.put(indices, offset, length);
        this.indices.flip();
    }
    
public void setAttribLocations(int aPositionLocation, int aColorLocation, int aTextureLocation) {
   
    
    this.aPositionLocation = aPositionLocation;
    this.aColorLocation = aColorLocation;
    this.aTextureLocation = aTextureLocation;
	
    vertices.position(0);
    //gl.glVertexPointer(2, GL10.GL_FLOAT, vertexSize, vertices);
    glVertexAttribPointer(aPositionLocation, 2, GL_FLOAT, false, vertexSize, vertices);
    glEnableVertexAttribArray(aPositionLocation);
    
    if(hasColor) {
       // gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
        vertices.position(POSITION_COMPONENT_COUNT);
        glVertexAttribPointer(aColorLocation,COLOR_COMPONENT_COUNT ,GL_FLOAT,false,vertexSize,vertices);
       // gl.glColorPointer(4, GL10.GL_FLOAT, vertexSize, vertices);
        glEnableVertexAttribArray(aColorLocation);
    }
    
    if(hasTexCoords) {
        //gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        vertices.position(hasColor?POSITION_COMPONENT_COUNT+COLOR_COMPONENT_COUNT:POSITION_COMPONENT_COUNT);
       //gl.glTexCoordPointer(2, GL10.GL_FLOAT, vertexSize, vertices);
        glVertexAttribPointer(aTextureLocation,TEXTURE_COMPONENT_COUNT, GL_FLOAT,false,vertexSize,vertices);
        glEnableVertexAttribArray(aTextureLocation);
    }
}

public void draw(int primitiveType, int offset, int numVertices) {        
  
    
    if(indices!=null) {
        indices.position(offset);
        glDrawElements(primitiveType, numVertices, GL10.GL_UNSIGNED_SHORT, indices);
    } else {
        glDrawArrays(primitiveType, offset, numVertices);
    }        
}

public void disableAttribLocations() {
    
    if(hasTexCoords)
     glDisableVertexAttribArray(aTextureLocation);

    if(hasColor)
       // gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
	glDisableVertexAttribArray(aColorLocation);
}
}

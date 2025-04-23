package com.coconut.marshmallow.renderer;

import java.awt.Color;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL30;

import com.coconut.marshmallow.Display;
import com.coconut.marshmallow.camera.Camera;
import com.coconut.marshmallow.math.Vector;
import com.coconut.marshmallow.object.GameObject;
import com.coconut.marshmallow.shader.Shader;
import com.coconut.marshmallow.shader.ShaderManager;

public class OvalRenderer extends GameObject {

	protected Matrix4f modelMatrix;
	protected Vector3f glmAnchor;

	@Override
	public void MARSHMALLOW_RENDER() {

		modelMatrix = makeModelMatrix();

		float r = (float) color.getRed() / 255, g = (float) color.getGreen() / 255, b = (float) color.getBlue() / 255,
				a = (float) color.getAlpha() / 255;

		Shader shaderBackup = Display.getShader();
		Display.uploadShader(ShaderManager.shapeShader);

		Display.getShader().uploadMat4f("uProjection", Camera.getProjectionMatrix());
		Display.getShader().uploadMat4f("uView", Camera.getViewMatrix());
		Display.getShader().uploadMat4f("uModel", modelMatrix);
		Display.getShader().uploadVec4f("uColor", new Vector4f(r, g, b, a));

		GL30.glBindVertexArray(Shapes.oval.getVaoID());

		GL30.glDrawArrays(GL30.GL_TRIANGLE_FAN, 0, Shapes.oval.vertexArray.length / 3);

		Display.uploadShader(shaderBackup);
	}

	protected Color color;

	public OvalRenderer(Vector position, float width, float height, Color color) {
		super(position, width, height);
		this.color = color;
	}

}

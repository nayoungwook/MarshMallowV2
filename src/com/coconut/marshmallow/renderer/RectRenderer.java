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

public class RectRenderer extends GameObject {

	protected Matrix4f modelMatrix;
	protected Vector3f glmAnchor;
	protected Color color = null;

	@Override
	public void MARSHMALLOW_RENDER() {

		modelMatrix = new Matrix4f();
		glmAnchor = new Vector3f(anchor.getX(), anchor.getY(), 0f);

		modelMatrix = makeModelMatrix();

		float r = (float) color.getRed() / 255, g = (float) color.getGreen() / 255, b = (float) color.getBlue() / 255,
				a = (float) color.getAlpha() / 255;

		Shader shaderBackup = Display.getShader();
		Display.uploadShader(ShaderManager.shapeShader);
		Display.getShader().uploadMat4f("uProjection", Camera.getProjectionMatrix());
		Display.getShader().uploadMat4f("uView", Camera.getViewMatrix());
		Display.getShader().uploadMat4f("uModel", modelMatrix);
		Display.getShader().uploadVec4f("uColor", new Vector4f(r, g, b, a));

		GL30.glBindVertexArray(Shapes.rect.getVaoID());

		GL30.glDrawElements(GL30.GL_TRIANGLES, Shapes.rect.getElementArray().length, GL30.GL_UNSIGNED_INT, 0);

		Display.uploadShader(shaderBackup);
	}

	public RectRenderer(Vector position, float width, float height, Color color) {
		super(position, width, height);
		this.color = color;

	}

}

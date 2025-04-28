package com.coconut.toffee.renderer;

import java.awt.Color;

import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL30;

import com.coconut.toffee.camera.Camera;
import com.coconut.toffee.math.Vector;
import com.coconut.toffee.shader.Shader;
import com.coconut.toffee.shader.ShaderManager;

public class UIRectRenderer extends RectRenderer {

	public UIRectRenderer(Vector position, float width, float height, Color color) {
		super(position, width, height, color);
	}

	@Override
	public void glRender() {
		modelMatrix = makeModelMatrix();

		float r = (float) color.getRed() / 255, g = (float) color.getGreen() / 255, b = (float) color.getBlue() / 255,
				a = (float) color.getAlpha() / 255;

		Shader shaderBackup = ShaderManager.getCurrentShader();
		ShaderManager.shapeShader.bind();

		ShaderManager.getCurrentShader().uploadMat4f("uProjection", Camera.getProjectionMatrix());
		ShaderManager.getCurrentShader().uploadMat4f("uView", new Matrix4f().identity());
		ShaderManager.getCurrentShader().uploadMat4f("uModel", modelMatrix);
		ShaderManager.getCurrentShader().uploadVec4f("uColor", new Vector4f(r, g, b, a));

		GL30.glBindVertexArray(Shapes.rect.getVaoID());

		GL30.glDrawElements(GL30.GL_TRIANGLES, Shapes.rect.getElementArray().length, GL30.GL_UNSIGNED_INT, 0);

		shaderBackup.bind();
	}
}

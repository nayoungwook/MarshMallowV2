package com.coconut.marshmallow.renderer;

import java.awt.Color;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL30;

import com.coconut.marshmallow.Display;
import com.coconut.marshmallow.camera.Camera;
import com.coconut.marshmallow.math.Mathf;
import com.coconut.marshmallow.math.Vector;
import com.coconut.marshmallow.object.GameObject;
import com.coconut.marshmallow.shader.Shader;
import com.coconut.marshmallow.shader.ShaderManager;

public class OvalRenderer extends GameObject {

	protected Matrix4f modelMatrix;
	protected Vector3f glmAnchor;

	@Override
	public void MARSHMALLOW_RENDER() {
		modelMatrix = new Matrix4f();
		Vector3f glmAnchor = new Vector3f(anchor.getX(), anchor.getY(), 0f);

		renderPosition = Mathf.toScreen(position);
		Vector renderSize = Mathf.toScreenSize(width, height, flipX, flipY);
		renderWidth = renderSize.getX();
		renderHeight = renderSize.getY();

		modelMatrix.translate(new Vector3f(renderPosition.getX(), renderPosition.getY(), 0));

		modelMatrix.translate(new Vector3f((glmAnchor.x - 0.5f) * width, (glmAnchor.y - 0.5f) * height, 0f));
		modelMatrix.rotate(-this.rotation + Camera.rotation, new Vector3f(0.0f, 0.0f, 1.0f));
		modelMatrix.translate(new Vector3f((glmAnchor.x - 0.5f) * -1 * width, (glmAnchor.y - 0.5f) * -1 * height, 0f));

		modelMatrix.scale(renderWidth / 100, renderHeight / 100, 1);

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

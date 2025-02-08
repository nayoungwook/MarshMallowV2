package com.coconut.marshmallow.renderer;

import java.awt.Color;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL30;

import com.coconut.marshmallow.Display;
import com.coconut.marshmallow.camera.Camera;
import com.coconut.marshmallow.font.TTFont;
import com.coconut.marshmallow.math.Mathf;
import com.coconut.marshmallow.math.Vector;
import com.coconut.marshmallow.object.GameObject;

public class FontRenderer extends GameObject {

	protected Matrix4f modelMatrix;
	protected Vector3f glmAnchor;
	protected Color color = null;

	public TTFont font = null;
	public String text = "";
	public String align = "center";

	@Override
	public void MARSHMALLOW_RENDER() {
		font.bakeFont(text, color);
		sprite = font;
		font.bind();
		Display.uploadShader(shader);

		modelMatrix = new Matrix4f();
		glmAnchor = new Vector3f(anchor.getX(), anchor.getY(), 0f);

		width = font.getWidth();
		height = font.getHeight();

		renderPosition = Mathf.toScreen(position);
		Vector renderSize = Mathf.toScreenSize(width, height, flipX, flipY);
		renderWidth = renderSize.getX();
		renderHeight = renderSize.getY();

		if (align == "left")
			renderPosition.translate(renderWidth / 2, 0);
		else if (align == "right")
			renderPosition.translate(renderWidth / -2, 0);

		modelMatrix.translate(new Vector3f(renderPosition.getX(), renderPosition.getY(), 0));

		modelMatrix.translate(new Vector3f((glmAnchor.x - 0.5f) * width, (glmAnchor.y - 0.5f) * height, 0f));
		modelMatrix.rotate(-this.rotation + Camera.rotation, new Vector3f(0.0f, 0.0f, 1.0f));
		modelMatrix.translate(new Vector3f((glmAnchor.x - 0.5f) * -1 * width, (glmAnchor.y - 0.5f) * -1 * height, 0f));

		modelMatrix.scale(renderWidth / 100, renderHeight / 100, 1);

		Display.getShader().uploadMat4f("uModel", modelMatrix);

		GL30.glBindVertexArray(sprite.getVaoID());

		GL30.glDrawElements(GL30.GL_TRIANGLES, sprite.getElementArray().length, GL30.GL_UNSIGNED_INT, 0);
	}

	public FontRenderer(Vector position, float width, float height, Color color, String text, TTFont font) {
		super(position, width, height);
		this.color = color;
		this.font = font;
	}

}

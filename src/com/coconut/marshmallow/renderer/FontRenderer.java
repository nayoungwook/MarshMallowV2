package com.coconut.marshmallow.renderer;

import java.awt.Color;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL30;

import com.coconut.marshmallow.Display;
import com.coconut.marshmallow.camera.Camera;
import com.coconut.marshmallow.font.TTFont;
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
		width = font.getWidth();
		height = font.getHeight();

		if (sprite == null)
			return;

		if (Display.getShader() != shader)
			Display.uploadShader(shader);

		sprite.bind();

		modelMatrix = new Matrix4f();
		glmAnchor = new Vector3f(anchor.getX(), anchor.getY(), 0f);

		if (this.align == "left") {
			position.translate(width / 2, 0);
		} else if (this.align == "right") {
			position.translate(width / -2, 0);
		}

		Display.getShader().uploadVec2f("uPosition", new Vector2f(this.position.getX(), this.position.getY()));
		Display.getShader().uploadVec2f("uSize", new Vector2f(this.width, this.height));

		modelMatrix.translate(new Vector3f(position.getX(), position.getY(), position.getZ()));

		modelMatrix.translate(new Vector3f((glmAnchor.x - 0.5f) * width, (glmAnchor.y - 0.5f) * height, 0f));
		modelMatrix.rotate(-this.rotation + Camera.rotation, new Vector3f(0.0f, 0.0f, 1.0f));
		modelMatrix.translate(new Vector3f((glmAnchor.x - 0.5f) * -1 * width, (glmAnchor.y - 0.5f) * -1 * height, 0f));

		modelMatrix.scale(width, height, 1);

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

package com.coconut.marshmallow.object;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL30;

import com.coconut.marshmallow.Window;
import com.coconut.marshmallow.math.Vector;
import com.coconut.marshmallow.sprite.Sprite;

public class GameObject {

	public Sprite sprite;
	public Vector position;
	public float rotation = 0f;
	public float width, height;
	public Vector anchor = new Vector(0.5f, 0.5f);

	public GameObject(Vector position, float width, float height) {
		this.position = position;
		this.width = width;
		this.height = height;
	}

	public GameObject(float x, float y, float width, float height) {
		this.position = new Vector(x, y);
		this.width = width;
		this.height = height;
	}

	private Matrix4f modelMatrix;
	private Vector3f glmAnchor;

	public void render() {
		if (sprite == null)
			return;

		sprite.bind();

		modelMatrix = new Matrix4f();
		glmAnchor = new Vector3f(anchor.getX(), anchor.getY(), 0f);

		modelMatrix.translate(new Vector3f((glmAnchor.x - 0.5f) * width, (glmAnchor.y - 0.5f) * height, 0f));
		modelMatrix.rotate(this.rotation, new Vector3f(0.0f, 0.0f, 1.0f));
		modelMatrix.translate(new Vector3f((glmAnchor.x - 0.5f) * -1 * width, (glmAnchor.y - 0.5f) * -1 * height, 0f));

		modelMatrix.translate(new Vector3f(this.position.getX(), this.position.getY(), 1));
		modelMatrix.scale(width / 100, height / 100, 1);

		Window.shader.uploadMat4f("uModel", modelMatrix);

		GL30.glBindVertexArray(sprite.getVaoID());

		GL30.glDrawElements(GL30.GL_TRIANGLES, sprite.getElementArray().length, GL30.GL_UNSIGNED_INT, 0);
	}

}

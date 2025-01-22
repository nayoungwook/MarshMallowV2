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

	public void render() {

		if (sprite == null)
			return;

		// Upload texture to shader
		GL30.glActiveTexture(GL30.GL_TEXTURE0);
		sprite.bind();

		Matrix4f modelMatrix = new Matrix4f();

		Vector3f glmAnchor = new Vector3f(anchor.getX(), anchor.getY(), 0f);

		modelMatrix.translate(new Vector3f((glmAnchor.x - 0.5f) * width, (glmAnchor.y - 0.5f) * height, 0f));
		modelMatrix.rotate(this.rotation, new Vector3f(0.0f, 0.0f, 1.0f));
		modelMatrix.translate(new Vector3f((glmAnchor.x - 0.5f) * -1 * width, (glmAnchor.y - 0.5f) * -1 * height, 0f));

		modelMatrix.translate(new Vector3f(this.position.getX(), this.position.getY(), 1));
		modelMatrix.scale(width / 50, height / 50, 1);

		Window.shader.uploadMat4f("uModel", modelMatrix);

		// Bind the VAO that we're using
		GL30.glBindVertexArray(sprite.getVaoID());

		// Enable the vertex attribute pointers
		GL30.glEnableVertexAttribArray(0);
		GL30.glEnableVertexAttribArray(1);
		GL30.glEnableVertexAttribArray(2);

		GL30.glDrawElements(GL30.GL_TRIANGLES, sprite.getElementArray().length, GL30.GL_UNSIGNED_INT, 0);

		// Unbind everything
		GL30.glDisableVertexAttribArray(0);
		GL30.glDisableVertexAttribArray(1);
		GL30.glDisableVertexAttribArray(2);

		GL30.glBindVertexArray(0);
	}

}

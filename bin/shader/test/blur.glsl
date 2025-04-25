#version 330 core

uniform sampler2D TEX_SAMPLER;

in vec2 fTexCoords;

out vec4 color;

void main() {
	vec2 direction = vec2(1, 1);
	vec2 resolution = vec2(90, 90);
	vec2 tex_offset = direction / resolution;

	float weights[5] = float[] (0.227027, 0.1945946, 0.1216216, 0.054054, 0.016216);

	vec4 result = texture(TEX_SAMPLER, fTexCoords) * weights[0];

	for (int i = 1; i < 5; ++i) {
		vec2 offset = float(i) * tex_offset;
		vec4 tex1 = texture(TEX_SAMPLER, fTexCoords + offset);
		vec4 tex2 = texture(TEX_SAMPLER, fTexCoords - offset);

		result += tex1 * weights[i];
		result += tex2 * weights[i];
	}

	color = result;
}

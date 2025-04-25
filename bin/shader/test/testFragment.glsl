#version 330 core

uniform sampler2D TEX_SAMPLER;

in vec2 fTexCoords;

out vec4 color;

void main() {
	vec2 resolution = vec2(30, 30);
	float weights[5] = float[] (0.227027, 0.1945946, 0.1216216, 0.054054, 0.016216);
	vec2 tex_offset = vec2(1.0 / resolution.x, 1.0 / resolution.y); // 수평
	vec2 tex_offset2 = vec2(-1.0 / resolution.x, 1.0 / resolution.y); // 수평

	vec4 tex = texture(TEX_SAMPLER, fTexCoords);
	vec4 result = tex * weights[0];
	for (int i = 1; i < 5; ++i) {
		result += texture(TEX_SAMPLER, fTexCoords + tex_offset * float(i))
				* weights[i];
		result += texture(TEX_SAMPLER, fTexCoords - tex_offset * float(i))
				* weights[i];
		result += texture(TEX_SAMPLER, fTexCoords + tex_offset2 * float(i))
				* weights[i];
		result += texture(TEX_SAMPLER, fTexCoords - tex_offset2 * float(i))
				* weights[i];
	}

	color = (tex + result) * 2;
}

package dev.somepineaple.phantom.main.utils;

public class RotationUtils {
	public static float getAngleDiff(float alpha, float beta) {
		float phi = Math.abs(beta - alpha) % 360;
		return phi > 180 ? 360 - phi : phi;
	}
}

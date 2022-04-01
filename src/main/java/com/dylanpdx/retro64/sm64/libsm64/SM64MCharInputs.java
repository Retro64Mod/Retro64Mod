package com.dylanpdx.retro64.sm64.libsm64;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class SM64MCharInputs extends Structure {
	public float camLookX;
	public float camLookZ;
	/** C type : float[3] */
	public float[] cameraPosition = new float[3];
	public float stickX;
	public float stickY;
	public byte buttonA;
	public byte buttonB;
	public byte buttonZ;
	public byte buttonL,buttonR,buttonU,buttonD;
	public SM64MCharInputs() {
		super();
	}
	protected List<String> getFieldOrder() {
		return Arrays.asList("camLookX", "camLookZ","cameraPosition", "stickX", "stickY", "buttonA", "buttonB", "buttonZ", "buttonL", "buttonR", "buttonU", "buttonD");
	}
	public SM64MCharInputs(float camLookX, float camLookZ, float stickX, float stickY, byte buttonA, byte buttonB, byte buttonZ) {
		super();
		this.camLookX = camLookX;
		this.camLookZ = camLookZ;
		this.stickX = stickX;
		this.stickY = stickY;
		this.buttonA = buttonA;
		this.buttonB = buttonB;
		this.buttonZ = buttonZ;
	}
	public static class ByReference extends SM64MCharInputs implements Structure.ByReference {
		
	};
	public static class ByValue extends SM64MCharInputs implements Structure.ByValue {
		
	};
}

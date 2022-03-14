package com.dylanpdx.retro64.sm64.libsm64;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;
public class SM64ObjectTransform extends Structure {
	/** C type : float[3] */
	public float[] position = new float[3];
	/** C type : float[3] */
	public float[] eulerRotation = new float[3];
	public SM64ObjectTransform() {
		super();
	}
	protected List<String> getFieldOrder() {
		return Arrays.asList("position", "eulerRotation");
	}

	public SM64ObjectTransform(float position[], float eulerRotation[]) {
		super();
		if ((position.length != this.position.length)) 
			throw new IllegalArgumentException("Wrong array size !");
		this.position = position;
		if ((eulerRotation.length != this.eulerRotation.length)) 
			throw new IllegalArgumentException("Wrong array size !");
		this.eulerRotation = eulerRotation;
	}
	public static class ByReference extends SM64ObjectTransform implements Structure.ByReference {
		
	};
	public static class ByValue extends SM64ObjectTransform implements Structure.ByValue {
		
	};
}

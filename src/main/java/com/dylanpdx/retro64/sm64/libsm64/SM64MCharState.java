package com.dylanpdx.retro64.sm64.libsm64;
import com.dylanpdx.retro64.sm64.SM64MCharActionFlags;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;
public class SM64MCharState extends Structure {
	/** C type : float[3] */
	public float[] position = new float[3];
	/** C type : float[3] */
	public float[] velocity = new float[3];
	/** C type : float[3] */
	public float[] camPos = new float[3];
	/** C type : float[3] */
	public float[] camFocus = new float[3];
	public float faceAngle;
	public short health;
	public int flags;
	public int action;
	public int currentModel;
	public SM64MCharState() {
		super();
	}
	protected List<String> getFieldOrder() {
		return Arrays.asList("position", "velocity","camPos","camFocus", "faceAngle", "health", "flags", "action","currentModel");
	}

	public SM64MCharState(float position[], float velocity[], float faceAngle, short health, int flags, int action, int currentModel) {
		super();
		if ((position.length != this.position.length)) 
			throw new IllegalArgumentException("Wrong array size !");
		this.position = position;
		if ((velocity.length != this.velocity.length)) 
			throw new IllegalArgumentException("Wrong array size !");
		this.velocity = velocity;
		this.faceAngle = faceAngle;
		this.health = health;
		this.flags = flags;
		this.action = action;
		this.currentModel = currentModel;
	}
	public static class ByReference extends SM64MCharState implements Structure.ByReference {
		
	};
	public static class ByValue extends SM64MCharState implements Structure.ByValue {
		
	};

	public boolean testActionFlag(SM64MCharActionFlags flag) {
		return (action & flag.value)!=0;
	}
}

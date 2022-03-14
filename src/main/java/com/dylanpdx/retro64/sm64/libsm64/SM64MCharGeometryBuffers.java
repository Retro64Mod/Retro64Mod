package com.dylanpdx.retro64.sm64.libsm64;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.List;
public class SM64MCharGeometryBuffers extends Structure {
	/** C type : float* */
	public Pointer position;
	/** C type : float* */
	public Pointer normal;
	/** C type : float* */
	public Pointer color;
	/** C type : float* */
	public Pointer uv;
	public short numTrianglesUsed;
	public SM64MCharGeometryBuffers() {
		super();
	}
	protected List<String> getFieldOrder() {
		return Arrays.asList("position", "normal", "color", "uv", "numTrianglesUsed");
	}
	/**
	 * @param position C type : float*<br>
	 * @param normal C type : float*<br>
	 * @param color C type : float*<br>
	 * @param uv C type : float*
	 */
	public SM64MCharGeometryBuffers(Pointer position, Pointer normal, Pointer color, Pointer uv, short numTrianglesUsed) {
		super();
		this.position = position;
		this.normal = normal;
		this.color = color;
		this.uv = uv;
		this.numTrianglesUsed = numTrianglesUsed;
	}
	public static class ByReference extends SM64MCharGeometryBuffers implements Structure.ByReference {
		
	};
	public static class ByValue extends SM64MCharGeometryBuffers implements Structure.ByValue {
		
	};
}

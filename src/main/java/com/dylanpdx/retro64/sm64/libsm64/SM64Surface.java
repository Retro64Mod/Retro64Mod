package com.dylanpdx.retro64.sm64.libsm64;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class SM64Surface extends Structure {
	public short type;
	public short force;
	public short terrain;
	/** C type : int32_t[3][3] */
	public int[] vertices = new int[((3) * (3))];
	public SM64Surface() {
		super();
	}
	protected List<String> getFieldOrder() {
		return Arrays.asList("type", "force", "terrain", "vertices");
	}
	/** @param vertices C type : int32_t[3][3] */
	public SM64Surface(short type, short force, short terrain, int vertices[]) {
		super();
		this.type = type;
		this.force = force;
		this.terrain = terrain;
		if ((vertices.length != this.vertices.length)) 
			throw new IllegalArgumentException("Wrong array size !");
		this.vertices = vertices;
	}
	public static class ByReference extends SM64Surface implements Structure.ByReference {
		
	};
	public static class ByValue extends SM64Surface implements Structure.ByValue {
		
	};
}

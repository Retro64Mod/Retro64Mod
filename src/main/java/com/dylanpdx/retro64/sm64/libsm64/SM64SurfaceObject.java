package com.dylanpdx.retro64.sm64.libsm64;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class SM64SurfaceObject extends Structure {
	/** C type : SM64ObjectTransform */
	public SM64ObjectTransform transform;
	public int surfaceCount;
	/** C type : SM64Surface* */
	public SM64Surface.ByReference surfaces;
	public SM64SurfaceObject() {
		super();
	}
	protected List<String> getFieldOrder() {
		return Arrays.asList("transform", "surfaceCount", "surfaces");
	}

	public SM64SurfaceObject(SM64ObjectTransform transform, int surfaceCount, SM64Surface.ByReference surfaces) {
		super();
		this.transform = transform;
		this.surfaceCount = surfaceCount;
		this.surfaces = surfaces;
	}
	public static class ByReference extends SM64SurfaceObject implements Structure.ByReference {
		
	};
	public static class ByValue extends SM64SurfaceObject implements Structure.ByValue {
		
	};
}

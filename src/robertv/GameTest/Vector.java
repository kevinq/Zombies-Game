package robertv.GameTest;

import java.lang.Math;


/*
 * I realized after I had written this
 * that this class was provided for me already.
 * 
 * but whatever.
 */
public class Vector {
	public double x;
	public double y;
	
	public Vector(double xf, double yf) {
		x = xf;
		y = yf;
	}
	
	public Vector(double a) {
		x = a;
		y = a;
	}
	
	public Vector add(Vector v) {
		this.x += v.x;
		this.y += v.y;
		return this;
	}
	
	public Vector subtract(Vector v) {
		this.x -= v.x;
		this.y -= v.y;
		return this;
	}
	
	public static Vector getDirectionVector(double x1, double y1, double x2, double y2) {
		Vector tv = new Vector(x2 - x1, y2 - y1);
		return tv.normalize();
	}
	
	public Vector scalarMultiply(double s) {
		this.x *= s;
		this.y *= s;
		return this;
	}
	
	public double magnitude() {
		//pop pop!
		return Math.sqrt((x*x + y*y));
	}
	
	public Vector normalize() {
		this.x /= this.magnitude();
		this.y /= this.magnitude();
		return this;
	}
}

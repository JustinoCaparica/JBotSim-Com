package mathutils;

import java.io.Serializable;

import net.jafama.FastMath;

public class Vector2d extends Point2d implements Serializable {

	private double ay;
	private double ax;
	private double angle;

	public Vector2d(double x, double y) {
		super(x,y);
	}

	public Vector2d() {
		super();
	}

	public Vector2d(mathutils.Vector2d v) {
		super(v.x, v.y);
	}

        
        
    /**
     * Initializes a new vector
     * with a given orientation
     * and length = 1
     * @param angleInRadians the 
     * orientation in radians
     * in the range [-PI, PI]
     */
    public Vector2d( double angleInRadians ) {
        
        double xCoord = Math.cos( angleInRadians ) * 1.0;
        double yCoord = Math.sin( angleInRadians ) * 1.0;
        
        super.set( xCoord, yCoord );
    }

    
    
	/**  
	 * Returns the length of this vector.
	 * @return the length of this vector
	 */  
	public final double length()
	{
		return (double) FastMath.sqrtQuick(this.x*this.x + this.y*this.y);
	}


	/**  
	 * Returns the squared length of this vector.
	 * @return the squared length of this vector
	 */  
	public final double lengthSquared()
	{
		return (this.x*this.x + this.y*this.y);
	}


	public void rotate(double angle) {
		double xTemp = x * FastMath.cosQuick(angle) -  y * FastMath.sinQuick(angle);
		y = x * FastMath.sinQuick(angle) + y * FastMath.cosQuick(angle);
		x = xTemp;
//		if(Double.isNaN(x)||Double.isNaN(y))
//			System.out.println("ERROR");
	}


	// Make vec perpendicular to itself
	public void dVec2Perpendicular()
	{ 
		double tempx = x;
		x  = -y;  
		y  = tempx; 
//		if(Double.isNaN(x)||Double.isNaN(y))
//			System.out.println("ERROR");
	}

	public void sub(Vector2d B) {
		x -= B.x;
		y -= B.y;
//		if(Double.isNaN(x)||Double.isNaN(y))
//			System.out.println("ERROR");
	}


	// Returns the vector going _from_ A to B (thus B - A)
	public void subFrom(Vector2d B)   
	{                               
		x = B.x - x;       
		y = B.y - y;       
//		if(Double.isNaN(x)||Double.isNaN(y))
//			System.out.println("ERROR");
	}

	
	public void add(Vector2d v1) {
		x += v1.x;
		y += v1.y;		
//		if(Double.isNaN(x)||Double.isNaN(y))
//			System.out.println("ERROR");
	}

	/**
	 * Computes the dot product of the this vector and vector v1.
	 * @param v1 the other vector
	 */
	public final double dot(Vector2d v1)
	{
		return (this.x * v1.x + this.y * v1.y);
	}



	public double getAngle() {
		//double a=  Math.atan2(y, x) ;
		if(ax != x || ay != y){
			angle	= FastMath.atan2(y, x) ;
			ax		= x;
			ay		= y;
		}
		return angle;
//		return Math.atan2(y, x) ;
	}

        
        
        
        /**
         * Gets the signed angle between
         * two vectors
         * @param v the vector parameter 
         * @return the angle between the
         * two vectors in radians in the 
         * range ]-Pi, PI]
         */
        public double signedAngle( Vector2d v ){
            
            double signedAngle;
            signedAngle = Math.atan2( this.x*v.y - this.y*v.x, this.x*v.x + this.y*v.y );
            
            return signedAngle;
        }
        
        
        
        
        
	/**
	 *   Returns the angle in radians between this vector and the vector
	 *   parameter; the return value is constrained to the range [0,PI].
	 *   @param v1    the other vector
	 *   @return   the angle in radians in the range [0,PI]
	 */
	public final double angle(Vector2d v1)
	{
		double vDot = this.dot(v1) / ( this.length()*v1.length() );
		if( vDot < -1.0) vDot = -1.0;
		if( vDot >  1.0) vDot =  1.0;
		return((double) (FastMath.acos( vDot )));

	}

	public void negate() {
		x=-x;
		y=-y;
//		if(Double.isNaN(x)||Double.isNaN(y))
//			System.out.println("ERROR");
	}

	public final void setLength(double newLength) {
//		if(x==0 && y==0)
//			System.out.println("ERROR");
		double factor = newLength/length();
		x *= factor;
		y *= factor;
	}
	
	
	public double distanceTo(Vector2d position) {
		double x = position.x - this.x;
		double y = position.y - this.y;		
		return FastMath.sqrtQuick(x*x+y*y);
	}
	
	@Override
	public String toString() {
		return "[angle=" + angle + ", x=" + x + ", y=" + y + "]";
	}

	public void multiply(double t) {
		x*=t;
		y*=t;
	}

}

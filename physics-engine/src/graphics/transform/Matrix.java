package graphics.transform;

import physics.general.Vector2;

/**
 * Matrix arranged by row -> column so
 * [0,1]
 * [1,0]
 * 
 * matrix at (0,1) 1
 * 
 * 
 * 
 * @author Cameron
 *
 */


public class Matrix
{
	private int width, height;
	private double[][] data;
	
	/**
	 * Creates a 2d rotaion matrix
	 * @param direction
	 */
	public Matrix(double theta)
	{
		data = new double[2][2];
		double sin = Math.sin(theta);
		double cos = Math.cos(theta);
		data[0][0] = cos;
		data[0][1] = -sin;
		data[1][0] = sin;
		data[1][1] = cos;
	}
	
	public Vector2 mulvec(Vector2 vector)
	{
		Vector2 ret = new Vector2();
		
		ret.x = vector.x * data[0][0] + vector.x * data[0][1];
		ret.y = vector.y * data[1][0] + vector.y * data[1][1];
		return ret;
	}
	
	public void transform(Matrix other)
	{
		
	}
}

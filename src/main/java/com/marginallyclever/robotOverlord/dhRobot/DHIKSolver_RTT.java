package com.marginallyclever.robotOverlord.dhRobot;

import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

/**
 * Solves IK for a RTT robot
 * @author Dan Royer
 * TODO test and finish
 */
public class DHIKSolver_RTT extends DHIKSolver {
	//public double theta0,alpha1,alpha2;

	/**
	 * @return the number of double values needed to store a valid solution from this DHIKSolver.
	 */
	public int getSolutionSize() {
		return 3;
	}

	/**
	 * Starting from a known local origin and a known local hand position (link 6 {@DHrobot.endMatrix}), calculate the angles for the given pose.
	 * @param robot The DHRobot description. 
	 * @param targetPose the pose that robot is attempting to reach in this solution.
	 * @param keyframe store the computed solution in keyframe.
	 */
	@SuppressWarnings("unused")
	@Override
	public void solve(DHRobot robot,Matrix4d targetPose,DHKeyframe keyframe) {
		DHLink link0 = robot.links.get(0);
		DHLink link1 = robot.links.get(1);
		DHLink link2 = robot.links.get(2);
		DHLink link3 = robot.links.get(3);
		DHLink link4 = robot.links.get(4);
		
		Vector3d p4 = new Vector3d(
				targetPose.m03,
				targetPose.m13,
				targetPose.m23);

		// Work forward to get p1 position
		Point3d p1 = new Point3d(0,0,link0.d);
		
		// (1) theta0 = atan(y07/x07);
		keyframe.fkValues[0] = Math.toDegrees(Math.atan2(p4.x,-p4.y));  // TODO explain why this isn't Math.atan2(p7.y,p7.x)
		if(false) System.out.println("t0="+keyframe.fkValues[0]+"\t");
		
		// (2) C=z14
		double c = p4.z - p1.z;
		if(false) System.out.println("c="+c+"\t");
		
		// (3) 
		double x15 = p4.x-p1.x;
		double y15 = p4.y-p1.y;
		double d = Math.sqrt(x15*x15 + y15*y15);
		if(false) System.out.println("d="+d+"\t");
		
		// (4)
		double e = Math.sqrt(c*c + d*d);
		if(false) System.out.println("e="+e+"\t");

		// (5) phi = acos( (b^2 - a^2 - e^2) / (-2*a*e) ) 
		double a = link2.d;
		double b2 = link4.d;
		double b1 = link3.d;
		double b = Math.sqrt(b2*b2+b1*b1);
		if(false) System.out.println("b="+b+"\t");
		
		double phi = Math.acos( (b*b-a*a-e*e) / (-2*a*e) );
		if(false) System.out.println("phi="+Math.toDegrees(phi)+"\t");
		
		// (6) rho = atan2(d,c)
		double rho = Math.atan2(d,c);
		if(false) System.out.println("rho="+Math.toDegrees(rho)+"\t");
		
		// (7) alpha1 = phi-rho
		keyframe.fkValues[1] = Math.toDegrees(rho - phi);
		if(false) System.out.println("a1="+keyframe.fkValues[1]+"\t");
		
		// (8) omega = acos( (a^2-b^2-e^2) / (-2be) )
		double omega = Math.acos( (a*a-b*b-e*e) / (-2*b*e) );
		if(false) System.out.println("omega="+Math.toDegrees(omega)+"\t");

		// (9) phi3 = phi + omega
		double phi3 = phi+omega;
		if(false) System.out.println("phi3="+Math.toDegrees(phi3)+"\t");
				
		// angle of triangle j3-j2-j5 is ph4.
		// b2^2 = b^+b1^2-2*b*b1*cos(phi4)
		double phi4 = Math.acos( (b2*b2-b1*b1-b*b) / (-2*b1*b) );
		if(false) System.out.println("phi4="+Math.toDegrees(phi4)+"\t");
		
		// (10) alpha2 - phi3-phi4
		keyframe.fkValues[2] = Math.toDegrees(phi3 - phi4);
		if(false) System.out.println("alpha2="+keyframe.fkValues[2]+"\t");
		
		if(true) System.out.println("solution={"+keyframe.fkValues[0]+","+keyframe.fkValues[1]+","+keyframe.fkValues[2]+"}");
	}
}

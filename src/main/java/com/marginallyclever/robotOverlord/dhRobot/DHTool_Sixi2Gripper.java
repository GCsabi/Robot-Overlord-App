package com.marginallyclever.robotOverlord.dhRobot;

import com.jogamp.opengl.GL2;
import com.marginallyclever.robotOverlord.modelInWorld.ModelInWorld;
import com.marginallyclever.robotOverlord.physicalObject.PhysicalObject;

/**
 * DHTool is a model that has a DHLink equivalence.
 * In this way it can perform transforms and have sub-links.
 * @author Dan Royer
 *
 */
public class DHTool_Sixi2Gripper extends ModelInWorld {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * A DHLink representation of this tool for kinematic solving.
	 */
	DHLink dhlinkEquivalent;
	
	/**
	 * A PhysicalObject, if any, being held by the tool.  Assumes only one object can be held.
	 */
	PhysicalObject subjectBeingHeld;
	
	/**
	 * Who, if anyone, is holding this tool?
	 */
	DHRobot heldBy;
	
	@Override
	public void render(GL2 gl2) {
		super.render(gl2);
		dhlinkEquivalent.renderPose(gl2);
	}
}

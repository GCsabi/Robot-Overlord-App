package com.marginallyclever.robotOverlord;

import org.junit.Test;

import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;

public class JInputTest {
	/**
	 * output the current java.library.path and the current working directory.
	 */
	@Test
	public void reportJavaLibraryPath() {
		String property = System.getProperty("java.library.path");
		System.out.println("java.library.path="+property.replace(";","\n  "));
		
		System.out.println("Working Directory = " + System.getProperty("user.dir"));
	}

	/**
	 * output all controllers, their components, and some details about those components.
	 */
	@Test
	public void testControllers() {
		Controller[] ca = ControllerEnvironment.getDefaultEnvironment().getControllers();
        for(int i =0;i<ca.length;i++){
            Component[] components = ca[i].getComponents();

            System.out.println("Controller:"+ca[i].getName()+" "+ca[i].getType().toString());
            System.out.println("Component Count: "+components.length);

            // Get this controllers components (buttons and axis)
            for(int j=0;j<components.length;j++){
                System.out.print("\t"+j+": "+components[j].getName());
                System.out.print(" \""+ components[j].getIdentifier().getName()+"\" (");
                System.out.print(components[j].isRelative() ? "Relative " : "Absolute ");
                System.out.print(components[j].isAnalog()? "Analog " : "Digital ");
                System.out.println(")");
            }
        }
	}
	

	/**
	 * Figure out which input just changed.  Requires human input.
	 */
	@Test
	public void detectSingleInputChange() {
		int i,j,k;
		
		while(true) {
			// count all devices and components
			int totalComponents=0;
			Controller[] ca = ControllerEnvironment.getDefaultEnvironment().getControllers();
			int totalControllers = ca.length;
	        for(i=0;i<totalControllers;i++) {
	        	totalComponents += ca[i].getComponents().length;
	        }
	        float [] stateOld = new float[totalComponents];
	        float [] stateNew = new float[totalComponents];
	
	        // get initial state
	        InputManager.update();
	        k=0;
	        for(i=0;i<totalControllers;i++) {
	            Component[] components = ca[i].getComponents();
	            for(j=0;j<components.length;j++) {
	            	stateOld[k] = components[j].getPollData();
	            	k++;
	            }
	        }
	
	        while(true) {
	        	// get the latest state
	            InputManager.update();
	            ca = ControllerEnvironment.getDefaultEnvironment().getControllers();
	            if(ca.length != totalControllers) {
	            	// uh oh!  devide added/removed!  restart!
	            	break;
	            }
	            k=0;
	            for(i=0;i<totalControllers;i++) {
	                Component[] components = ca[i].getComponents();
	                for(j=0;j<components.length;j++) {
	                	stateNew[k] = components[j].getPollData();
	                	float diff = stateNew[k]-stateOld[k];
	                	if(Math.abs(diff)>0.5) {
	                		// change to state
	                		System.out.println("Found "+ca[i].getName()+"."+components[j].getName()+"="+diff);
	                		//return;
	                	}
	                	k++;
	                }
	            }
	        }
		}
	}
	
	/**
	 * Figure out which input just changed.  Requires human input.
	 */
	@Test
	public void detectSingleInputChangeB() {
		int i,j;
		
        while(true) {
        	// get the latest state
            InputManager.update();
			Controller[] ca = ControllerEnvironment.getDefaultEnvironment().getControllers();
            for(i=0;i<ca.length;i++) {
                Component[] components = ca[i].getComponents();
                for(j=0;j<components.length;j++) {
                	float diff = components[j].getPollData();
                	if(diff>0.5) {
                		// change to state
                		System.out.println("Found "+ca[i].getName()+"."+components[j].getName()+"="+diff);
                		//return;
                	}
                }
            }
		}
	}
}

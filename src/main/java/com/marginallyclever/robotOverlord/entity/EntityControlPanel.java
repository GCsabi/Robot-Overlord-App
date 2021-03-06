package com.marginallyclever.robotOverlord.entity;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.marginallyclever.robotOverlord.CollapsiblePanel;
import com.marginallyclever.robotOverlord.RobotOverlord;
import com.marginallyclever.robotOverlord.commands.UserCommandRemoveMe;
import com.marginallyclever.robotOverlord.commands.UserCommandSelectString;

/**
 * The user interface for an {@link Entity}.
 * @author Dan Royer
 *
 */
public class EntityControlPanel extends JPanel implements ChangeListener {
	private static final long serialVersionUID = 1L;
	private Entity entity;
	private transient UserCommandSelectString setName;

	/**
	 * @param ro the application instance
	 * @param entity The entity controlled by this panel
	 */
	public EntityControlPanel(RobotOverlord ro,Entity entity) {
		super();
		
		this.entity = entity;

		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx=0;
		c.gridy=0;
		c.weightx=1;
		c.weighty=1;
		c.fill=GridBagConstraints.HORIZONTAL;
		c.anchor=GridBagConstraints.CENTER;
		
		CollapsiblePanel oiwPanel = new CollapsiblePanel("Entity");
		this.add(oiwPanel,c);
		JPanel contents = oiwPanel.getContentPane();
		
		GridBagConstraints con1 = new GridBagConstraints();
		con1.gridx=0;
		con1.gridy=0;
		con1.weighty=1;
		con1.fill=GridBagConstraints.HORIZONTAL;
		con1.anchor=GridBagConstraints.CENTER;

		if(ro.getWorld().hasEntity(entity)) {
			contents.add(new UserCommandRemoveMe(ro,entity),con1);
			con1.gridy++;
		}
		
		contents.add(setName=new UserCommandSelectString(ro,"name",entity.getDisplayName()), con1);
		con1.gridy++;
		setName.addChangeListener(this);
	}
	
	
	/**
	 * Call by an {@link Entity} when it's details change so that they are reflected on the panel.
	 * This might be better as a listener pattern.
	 */
	public void updateFields() {
	}
	
	
	/**
	 * Called by the UI when the user presses buttons on the panel.
	 * @param e the {@link ChangeEvent} details
	 */
	@Override
	public void stateChanged(ChangeEvent e) {
		Object subject = e.getSource();
		
		if( subject==setName ) {
			String name = entity.getDisplayName();
			String newName = setName.getValue();
			if(!newName.equals(name)) {
				entity.setDisplayName(newName);
			}
		}
	}
}

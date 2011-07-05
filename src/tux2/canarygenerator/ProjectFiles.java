/* This file is a part of the CanaryPluginGenerator
 * Copyright (C) 2011  Joshua Reetz

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package tux2.canarygenerator;

import java.awt.BorderLayout;
import java.io.File;
import java.util.LinkedList;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class ProjectFiles extends JPanel implements GeneratorPane {
	
	LinkedList<ProjectDefs> listeners = new LinkedList<ProjectDefs>();
	
	public ProjectFiles() {
		initializeProjects();
		JPanel checkboxes = new JPanel();
		checkboxes.setLayout(new BoxLayout(checkboxes, BoxLayout.Y_AXIS));
		for(ProjectDefs thelistener : listeners) {
			checkboxes.add(thelistener.getCheckBox());
		}
		setLayout(new BorderLayout());
		add("North", new JLabel("Select the project type:"));
		add("Center", new JScrollPane(checkboxes));
	}
	
	void initializeProjects() {
		listeners.add(new EclipseDef());
	}

	@Override
	public String getEnable(String pluginname) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDisable(String pluginname) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getInitialize(String pluginname) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTabName() {
		return "Project Environment";
	}

	@Override
	public void createFiles(String pluginname, File location) {
		for(ProjectDefs thelistener : listeners) {
			if(thelistener.getCheckBox().isSelected()){
				thelistener.createProjectFiles(location, pluginname);
			}
		}
	}

	@Override
	public String getCustom(String pluginname) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getMain(String pluginname) {
		// TODO Auto-generated method stub
		return null;
	}

}

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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import javax.swing.JCheckBox;

public class EclipseDef extends ProjectDefs {
	
	JCheckBox cbox = new JCheckBox("Eclipse");
	
	public EclipseDef() {
		// TODO Auto-generated constructor stub
	}
	
	String classpath = "﻿<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
			"<classpath>\n" + 
			"<classpathentry kind=\"src\" path=\"src\"/>\n" + 
			"<classpathentry kind=\"con\" path=\"org.eclipse.jdt.launching.JRE_CONTAINER\"/>\n" + 
			"<classpathentry kind=\"output\" path=\"bin\"/>\n" + 
			"</classpath>";
	

	@Override
	public void createProjectFiles(File pathname, String projectname) {
		String project = "﻿<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
		"<projectDescription>\n" + 
		"<name>" + projectname + "</name>\n" + 
		"<comment></comment>\n" + 
		"<projects>\n" + 
		"</projects>\n" + 
		"<buildSpec>\n" + 
		"<buildCommand>\n" + 
		"<name>org.eclipse.jdt.core.javabuilder</name>\n" + 
		"<arguments>\n" + 
		"</arguments>\n" + 
		"</buildCommand>\n" + 
		"</buildSpec>\n" + 
		"<natures>\n" + 
		"<nature>org.eclipse.jdt.core.javanature</nature>\n" + 
		"</natures>\n" + 
		"</projectDescription>";
		File fproject = new File(pathname, ".project");
		File fclasspath = new File(pathname, ".classpath");
		try {
			BufferedWriter outChannel = new BufferedWriter(new FileWriter(fproject));
			outChannel.write(project);
			outChannel.close();
			outChannel = new BufferedWriter(new FileWriter(fclasspath));
			outChannel.write(classpath);
			outChannel.close();
		}catch (Exception e) {
			
		}

	}


	@Override
	public JCheckBox getCheckBox() {
		return cbox;
	}

}

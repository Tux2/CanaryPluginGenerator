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
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

/**
 * @author joshua
 *
 */
public class Generator extends JFrame implements ActionListener {
	
	JTextField pluginname = new JTextField("SamplePlugin");
	static String version = "1.3";
	static String canaryversion = "b8";
	JTextField pluginversion = new JTextField("0.1");
	LinkedList<GeneratorPane> panes = new LinkedList<GeneratorPane>();
	JButton saveplugin = new JButton("Create Plugin");
	
	public Generator() {
		super("Canary Plugin Generator version " + version + " [" + canaryversion + "]");
		addPanes();
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		JPanel mainoptions = new JPanel();
		mainoptions.setLayout(new GridLayout(5, 2));
		mainoptions.add(new JLabel("Plugin Name: "));
		mainoptions.add(pluginname);
		mainoptions.add(new JLabel("Plugin Version: "));
		mainoptions.add(pluginversion);
		JTabbedPane options = new JTabbedPane();
		options.add("Main", mainoptions);
		for(GeneratorPane thepane : panes) {
			options.add(thepane.getTabName(), (JPanel)thepane);
		}
		contentPane.add("Center", options);
		saveplugin.addActionListener(this);
		contentPane.add("South", saveplugin);
	}
	
	void addPanes() {
		panes.add(new Listeners());
		panes.add(new PropertiesFileCreator());
		panes.add(new ProjectFiles());
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final Generator f = new Generator();
		f.setBounds(100, 100, 600, 400);
		f.setVisible(true);
		f.setDefaultCloseOperation(EXIT_ON_CLOSE);

	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int result = chooser.showOpenDialog(this);
		File fileobj = chooser.getSelectedFile();
		if(result == JFileChooser.APPROVE_OPTION) {
			File projectdir = new File(fileobj, pluginname.getText());
			if(projectdir.exists()) {
				int result1 = JOptionPane.showConfirmDialog(this, "This project already exists, overwrite?", "Directory Already Exists", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
				if(result1 != JOptionPane.YES_OPTION) {
					return;
				}
			}
			projectdir.mkdirs();
			String onEnable = "";
			String onDisable = "";
			String onInitialize = "";
			String onCustom = "";
			String onMain = "";
			for(GeneratorPane thepane : panes) {
				String tempenable = thepane.getEnable(pluginname.getText());
				String tempdisabe = thepane.getDisable(pluginname.getText());
				String tempinitialize = thepane.getInitialize(pluginname.getText());
				String tempcustom = thepane.getCustom(pluginname.getText());
				String tempmain = thepane.getMain(pluginname.getText());
				thepane.createFiles(pluginname.getText(), projectdir);
				if(tempenable != null) {
					onEnable = onEnable + "\n\t\t" + tempenable;
				}
				if(tempdisabe != null) {
					onDisable = onDisable + "\n\t\t" + tempdisabe;
				}
				if(tempinitialize != null) {
					onInitialize = onInitialize + "\n\t\t" + tempinitialize;
				}
				if(tempcustom != null) {
					onCustom = onCustom + "\n\t" + tempcustom;
				}
				if(tempmain != null) {
					onMain = onMain + "\n\t\t" + tempmain;
				}
			}
			String mainclass = "public class " + pluginname.getText() + " extends Plugin {\n\n" + 
					"	private String name = \"" + pluginname.getText() + "\";\n" + 
					"	String version = \"" + pluginversion.getText() + "\";\n" + onCustom +
					"\n	\n" +
					"	public " + pluginname.getText() + "() {\n" + 
					"		super();\n" + 
					"		\n" + onMain +
					"		\n" + 
					"	}\n" + 
					"\n" + 
					"	public void initialize() {\n" + 
					"		// TODO: Add your initialization code here.\n" + onInitialize +
					"\n		\n" +
					"	}\n" + 
					"	\n" +
					"	public synchronized void disable() {\n" +
					"		// TODO: Add your disabling code here.\n" + onDisable +
					"\n		\n" + 
					"		System.out.println(name + \" version \" + version + \" is disabled!\");\n" + 
					"	}\n" + 
					"	\n" + 
					"	public void enable() {\n" + 
					"		// TODO Add your enable code here.\n" + onEnable +
					"\n		System.out.println( name + \" version \" + version + \" is enabled!\" );\n" + 
					"		\n" + 
					"	}" +
					"}";
			try {
				File srcdirectory = new File(projectdir, "src");
				if(!srcdirectory.exists()) {
					srcdirectory.mkdirs();
				}
				File saving = new File(srcdirectory, pluginname.getText() + ".java");
				BufferedWriter outChannel = new BufferedWriter(new FileWriter(saving));
				outChannel.write(mainclass);
				outChannel.close();
				JOptionPane.showMessageDialog(this, "Plugin created successfully!", "Plugin Created", JOptionPane.INFORMATION_MESSAGE);
			}catch (Exception e) {
				JOptionPane.showMessageDialog(this, "Plugin creation failed!", "Canary Plugin Generator", JOptionPane.ERROR_MESSAGE);
				System.out.println(e);
			}
		}
	}

}

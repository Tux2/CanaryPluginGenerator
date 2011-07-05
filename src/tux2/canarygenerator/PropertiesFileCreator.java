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
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class PropertiesFileCreator extends JPanel implements GeneratorPane, ListSelectionListener, ActionListener, CaretListener {
	
	ConcurrentHashMap<String, PropertySettings> theproperties = new ConcurrentHashMap<String, PropertySettings>();
	LinkedList<String> reservedwords = new LinkedList<String>();
	
	String converters = "	boolean getBoolean(String value) {\n" + 
			"		if(value.equalsIgnoreCase(\"true\")) {\n" + 
			"			return true;\n" + 
			"		}else {\n" + 
			"			return false;\n" + 
			"		}\n" + 
			"	}\n" + 
			"	double getDouble(String value, double default1) {\n" + 
			"		try{\n" + 
			"			return Double.parseDouble(value.trim());\n" + 
			"		}catch (Exception e) {\n" + 
			"			return default1;\n" + 
			"		}\n" + 
			"	}\n" + 
			"	float getFloat(String value, float default1) {\n" + 
			"		try{\n" + 
			"			return Float.parseFloat(value.trim());\n" + 
			"		}catch (Exception e) {\n" + 
			"			return default1;\n" + 
			"		}\n" + 
			"	}\n" + 
			"	int getInt(String value, int default1) {\n" + 
			"		try{\n" + 
			"			return Integer.parseInt(value.trim());\n" + 
			"		}catch (Exception e) {\n" + 
			"			return default1;\n" + 
			"		}\n" + 
			"	}\n" + 
			"	long getLong(String value, long default1) {\n" + 
			"		try{\n" + 
			"			return Long.parseLong(value.trim());\n" + 
			"		}catch (Exception e) {\n" + 
			"			return default1;\n" + 
			"		}\n" + 
			"	}";
	
	JList propertieslist = new JList();
	JTextArea propertiestext = new JTextArea();
	JCheckBox generateconfig = new JCheckBox("Generate Settings File");
	JTextField propertytext = new JTextField(10);
	JComboBox propertytype = new JComboBox(new String[]{"String","int","boolean","double","long","float"});
	JTextField propertydefaultvalue = new JTextField(10);
	JButton inicheck = new JButton("Check Syntax");
	JButton propertyadd = new JButton("Add/Update");
	JButton propertydelete = new JButton("Delete");
	public PropertiesFileCreator() {
		addReservedWords();
		setLayout(new BorderLayout());
		JPanel westpanel = new JPanel();
		westpanel.setLayout(new BoxLayout(westpanel, BoxLayout.Y_AXIS));
		propertieslist.setVisibleRowCount(6);
		propertieslist.setFixedCellWidth(100);
		propertieslist.addListSelectionListener(this);
		westpanel.add(new JScrollPane(propertieslist));
		propertydelete.setActionCommand("propertydelete");
		propertydelete.addActionListener(this);
		westpanel.add(propertydelete);
		add("West", westpanel);
		JPanel northpanel = new JPanel();
		northpanel.setLayout(new GridLayout(2, 4));
		northpanel.add(new JLabel("Property:"));
		northpanel.add(new JLabel("Default Value:"));
		northpanel.add(new JLabel("Type:"));
		inicheck.setActionCommand("inicheck");
		inicheck.addActionListener(this);
		northpanel.add(inicheck);
		propertytext.setActionCommand("propertyadd");
		propertytext.addActionListener(this);
		northpanel.add(propertytext);
		propertydefaultvalue.setActionCommand("propertyadd");
		propertydefaultvalue.addActionListener(this);
		northpanel.add(propertydefaultvalue);
		northpanel.add(propertytype);
		propertyadd.setActionCommand("propertyadd");
		propertyadd.addActionListener(this);
		northpanel.add(propertyadd);
		add("North", northpanel);
		propertiestext.addCaretListener(this);
		add("Center", new JScrollPane(propertiestext));
		add("South", generateconfig);
	}

	@Override
	public String getEnable(String pluginname) {
		return null;
	}

	@Override
	public String getDisable(String pluginname) {
		return null;
	}

	@Override
	public String getInitialize(String pluginname) {
		return null;
	}

	@Override
	public String getTabName() {
		return "Properties File";
	}

	@Override
	public void createFiles(String pluginname, File location) {
		if(generateconfig.isSelected()) {
			File srcdirectory = new File(location, "src");
			File saving = new File(srcdirectory, pluginname + "Config.java");
			String theconfig = "import java.io.*;\n" +
					"import java.util.*;\n" +
					"\n" +
					"public class " + pluginname + "Config {\n" +
					"	\n" +
					"	private final " + pluginname + " plugin;\n" +
					"	private static String INIFILE = \"plugins/" + pluginname + "/config.ini\";" +
					"\n" + 
					"    public " + pluginname + "Config(" + pluginname + " plugin) {\n" + 
					"        this.plugin = plugin;\n" + 
					"        loadIni();\n" + 
					"    }\n\t";
			String thevariables = "";
			String gettersandsetters = "\t";
			String loader = "\n\tpublic void loadIni() {\n" +
					"		File inifile = new File(INIFILE);\n" +
					"		if (inifile.exists()) {\n" +
					"			try {" +
					"				Properties iniSettings = new Properties();\n" + 
					"				iniSettings.load(new FileInputStream(inifile));\n" +
					"				\n" +
					"			";
			Collection<PropertySettings> settings = theproperties.values();
			for(PropertySettings thesetting : settings) {
				int type = thesetting.getType();
				String varname = thesetting.getProperty();
				if(reservedwords.contains(varname)) {
					varname = "p_" + varname;
				}
				if(type == PropertySettings.STRING) {
					thevariables = thevariables + createvariable("String", varname, thesetting.getValue());
					gettersandsetters = gettersandsetters + generateGetterSetter("String", varname, thesetting.getProperty());
					loader = loader + varname + " = iniSettings.getProperty(\"" + thesetting.getProperty() + "\", "+ varname + ");\n\t\t\t\t";
				}else if(type == PropertySettings.BOOLEAN) {
					thevariables = thevariables + createvariable("boolean", varname, thesetting.getValue());
					gettersandsetters = gettersandsetters + generateGetterSetter("boolean", varname, thesetting.getProperty());
					loader = loader + createloader("boolean", varname, thesetting.getProperty());
				}else if(type == PropertySettings.DOUBLE) {
					thevariables = thevariables + createvariable("double", varname, thesetting.getValue());
					gettersandsetters = gettersandsetters + generateGetterSetter("double", varname, thesetting.getProperty());
					loader = loader + createloader("double", varname, thesetting.getProperty());
				}else if(type == PropertySettings.FLOAT) {
					thevariables = thevariables + createvariable("float", varname, thesetting.getValue());
					gettersandsetters = gettersandsetters + generateGetterSetter("float", varname, thesetting.getProperty());
					loader = loader + createloader("float", varname, thesetting.getProperty());
				}else if(type == PropertySettings.INT) {
					thevariables = thevariables + createvariable("int", varname, thesetting.getValue());
					gettersandsetters = gettersandsetters + generateGetterSetter("int", varname, thesetting.getProperty());
					loader = loader + createloader("int", varname, thesetting.getProperty());
				}else if(type == PropertySettings.LONG) {
					thevariables = thevariables + createvariable("long", varname, thesetting.getValue());
					gettersandsetters = gettersandsetters + generateGetterSetter("long", varname, thesetting.getProperty());
					loader = loader + createloader("long", varname, thesetting.getProperty());
				}
			}
			loader = loader + "\n" +
					"			}catch (Exception e) {\n" + 
					"				System.out.println(\"" + pluginname + ": - file creation failed, using defaults.\");\n" + 
					"			}" +
					"		}else {\n" +
					"			createIni();\n" +
					"		}\n" +
					"	}\n";
			
			String inigenerating = "	public void createIni() {\n" +
					"		File inifile = new File(INIFILE);\n" +
					"		try {\n" + 
					"			inifile.getParentFile().mkdirs();\n" + 
					"			BufferedWriter outChannel = new BufferedWriter(new FileWriter(inifile));\n" + 
					"			outChannel.write(";
			String[] plines = propertiestext.getText().split("\n");
			for(String theline : plines) {
				if(theline.startsWith("#")) {
					inigenerating = inigenerating + "\"" + escapeLine(theline) + "\\n\" +\n\t\t\t\t";
				}else if(theline.trim().equals("")) {
					inigenerating = inigenerating + "\"" + escapeLine(theline) + "\\n\" +\n\t\t\t\t";
				}else if(theline.indexOf(" = ") > -1) {
					//We've got ourselves a value! Let's start playing.
					String[] split = theline.split("=");
					if(split.length >= 1) {
						//We have the correct length
						if(theproperties.containsKey(split[0].trim())) {
							PropertySettings thesetting = theproperties.get(split[0].trim());
							String varname = thesetting.getProperty();
							if(reservedwords.contains(varname)) {
								varname = "p_" + varname;
							}
							inigenerating = inigenerating + "\"" + escapeLine(split[0].trim()) + " = \" + " + varname + " + \"\\n\" +\n\t\t\t\t";
						}
					}else {
						//Just a regular string... insert it anyways...
						inigenerating = inigenerating + "\"" + escapeLine(theline) + "\\n\" +\n\t\t\t\t";
					}
				}else {
					//I don't know what this line is, but it might be important....
					inigenerating = inigenerating + "\"" + escapeLine(theline) + "\\n\" +\n\t\t\t\t";
				}
			}
			inigenerating = inigenerating + "\"\"\n\t\t\t);\n" +
					"			outChannel.close();\n" + 
					"		} catch (Exception e) {\n" + 
					"			System.out.println(\"" + pluginname + ": - file creation failed, using defaults.\");\n" + 
					"		}\n" + 
					"		\n" + 
					"	}";
			try {

				BufferedWriter outChannel = new BufferedWriter(new FileWriter(saving));
				outChannel.write(theconfig + thevariables + gettersandsetters + loader + inigenerating + converters + "\n}");
				outChannel.close();
			}catch (Exception e) {
				
			}
		}

	}
	
	String escapeLine(String string) {
		char[] cstring = string.toCharArray();
		String newstring = "";
		for(char nchar : cstring) {
			if(nchar == '\\' || nchar == '"') {
				newstring = newstring + "\\" + nchar;
			}else {
				newstring = newstring + nchar;
			}
		}
		return newstring;
	}
	
	String createvariable(String type, String name, String value) {
		if(type.equals("String")) {
			return type + " " + name + " = \"" + value + "\";\n\t";
		}
		return type + " " + name + " = " + value + ";\n\t";
	}
	
	String createloader(String type, String varname, String name) {
		return varname + " = get" + capitalCase(type) + "(iniSettings.getProperty(\"" + name + "\")," + varname + ");\n\t\t\t\t";
	}
	
	public String capitalCase(String s)
    {
        return s.toUpperCase().charAt(0) + s.toLowerCase().substring(1);
    }
	
	String generateGetterSetter(String type, String varname, String name) {
		String types = "\n\tpublic " + type + " get" + name + "() {\n\t\treturn " + varname + ";\n\t}\n\t";
		types = types + "\n\tpublic void set" + name + "(" + type + " " + varname + ") {\n\t\tthis." + varname + " = " + varname + ";\n\t}\n\t";
		return types;
	}

	@Override
	public String getCustom(String pluginname) {
		if(generateconfig.isSelected()) {
			return pluginname + "Config config = new " + pluginname + "Config(this);";
		}
		return null;
	}

	@Override
	public String getMain(String pluginname) {
		return null;
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if(propertieslist.getSelectedIndex() > -1) {
			String svalue = (String)propertieslist.getSelectedValue();
			PropertySettings ssettings = theproperties.get(svalue);
			propertytext.setText(ssettings.getProperty());
			propertydefaultvalue.setText(ssettings.getValue());
			propertytype.setSelectedIndex(ssettings.getType());
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String refinedproperty = propertytext.getText().replace(' ', '-');
		if(e.getActionCommand().equalsIgnoreCase("propertyadd")) {
			if(!generateconfig.isSelected()) {
				generateconfig.doClick();
			}
			if(theproperties.containsKey(propertytext.getText())) {
				String[] lines = propertiestext.getText().split("\n");
				String newlines = "";
				for(int i = 0; i < lines.length; i++) {
					if(lines[i].trim().startsWith(refinedproperty + " ")) {
						lines[i] = refinedproperty + " = " + propertydefaultvalue.getText();
					}
					if(i > 0) {
						newlines = newlines + "\n";
					}
					newlines = newlines + lines[i];
				}
				propertiestext.setText(newlines);
			}else {
				propertiestext.append("\n" + refinedproperty + " = " + propertydefaultvalue.getText());
			}
			theproperties.put(refinedproperty, new PropertySettings(propertytext.getText(), propertydefaultvalue.getText(), propertytype.getSelectedIndex()));
			Set<String> propertiesset = theproperties.keySet();
			String[] propertiesstrings = new String[propertiesset.size()];
			int i = 0;
			for(String key : propertiesset) {
				propertiesstrings[i] = key;
				i++;
			}
			propertieslist.setListData(propertiesstrings);
		}else if(e.getActionCommand().equalsIgnoreCase("propertydelete")) {
			if(propertieslist.getSelectedIndex() > -1) {
				String svalue = (String)propertieslist.getSelectedValue();
				String[] lines = propertiestext.getText().split("\n");
				String newlines = "";
				for(int i = 0; i < lines.length; i++) {
					if(lines[i].trim().startsWith(refinedproperty + " ")) {
						//lines[i] = refinedproperty + " = " + propertydefaultvalue.getText();
					}else {
						if(i > 0) {
							newlines =  newlines  + "\n";
						}
						newlines = newlines + lines[i];
					}
				}
				propertiestext.setText(newlines);
				theproperties.remove(svalue);
				Set<String> propertiesset = theproperties.keySet();
				String[] propertiesstrings = new String[propertiesset.size()];
				int i = 0;
				for(String key : propertiesset) {
					propertiesstrings[i] = key;
					i++;
				}
				propertieslist.setListData(propertiesstrings);
			}
		}else if(e.getActionCommand().equalsIgnoreCase("inicheck")) {
			boolean checked = verifyinput();
			if(checked) {
				JOptionPane.showMessageDialog(this, "Your configuration file passed the sanity check!", "ini checked successfully!", JOptionPane.INFORMATION_MESSAGE);
			}else {
				JOptionPane.showMessageDialog(this, "You have at least one error in your configuration file!", "ini Check Unsuccessful", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	@Override
	public void caretUpdate(CaretEvent e) {
		int dot = e.getDot();
		//System.out.println("Dot location: " + dot);
		String thepropertiestext = propertiestext.getText();
		int linebeginning = thepropertiestext.lastIndexOf("\n", dot - 1);
		int lineending = thepropertiestext.indexOf("\n", dot);
		if(linebeginning == -1) {
			linebeginning = 0;
		}
		if(lineending == -1) {
			lineending = thepropertiestext.length();
		}
		String theline = "";
		if(linebeginning == 0) {
			theline = thepropertiestext.substring(linebeginning, lineending);
		}else if (linebeginning < lineending) {
			theline = thepropertiestext.substring(linebeginning + 1, lineending);
		}
		//System.out.println("The current line: " + theline);
		//System.out.println("linebeginning: " + linebeginning + "; lineending " +lineending);
		if(theline.startsWith("#")) {
			//It's a comment, leave it alone...
		}else if(theline.trim().equals("")) {
			//The line is empty, must be a spacer
		}else if(theline.indexOf(" = ") > -1) {
			//We've got ourselves a value! Let's start playing.
			String[] split = theline.split("=");
			if(split.length >= 2) {
				//We have the correct length
				if(theproperties.containsKey(split[0].trim())) {
					PropertySettings pssettings = theproperties.get(split[0].trim());
					if(!pssettings.getValue().equals(split[1].trim())) {
						pssettings.setValue(split[1].trim());
					}
					if(propertytext.getText().equals(split[0].trim())) {
						propertydefaultvalue.setText(split[1].trim());
					}
				}
			}else if(split.length == 1) {
				PropertySettings pssettings = theproperties.get(split[0].trim());
				if(!pssettings.getValue().equals("")) {
					pssettings.setValue("");
				}
				if(propertytext.getText().equals(split[0].trim())) {
					propertydefaultvalue.setText("");
				}
			}
		}else {
			//We have a line here we need to make into a comment...
			String start = "";
			String end = "";
			if(linebeginning == 0) {
				start = thepropertiestext.substring(0, linebeginning);
				//System.out.println("Starting lines: " + start);
				end = thepropertiestext.substring(linebeginning);
			}else {
				start = thepropertiestext.substring(0, linebeginning + 1);
				//System.out.println("Starting lines: " + start);
				end = thepropertiestext.substring(linebeginning + 1);
			}
				//System.out.println("ending lines: " + end);
			String theresult = start + "# " + end;
			Runnable doHelloWorld = new UpdateTextField(theresult, dot, propertiestext);
			SwingUtilities.invokeLater(doHelloWorld);
		}
		
		
	}
	
	/**
	 * Verifies the ini file to make sure everything is done correctly.
	 * @return true if everything checks out correctly. False if there are errors.
	 */
	private boolean verifyinput() {
		Collection<PropertySettings> settings = theproperties.values();
		for(PropertySettings thesetting : settings) {
			int type = thesetting.getType();
			if(type == PropertySettings.STRING) {
				
			}else if(type == PropertySettings.BOOLEAN) {
				if(thesetting.getValue().equals("true") || thesetting.getValue().equals("false")) {
					
				}else {
					return false;
				}
			}else if(type == PropertySettings.DOUBLE) {
				try{
					Double.parseDouble(thesetting.getValue());
				}catch (Exception e) {
					return false;
				}
			}else if(type == PropertySettings.FLOAT) {
				try{
					Float.parseFloat(thesetting.getValue());
				}catch (Exception e) {
					return false;
				}
			}else if(type == PropertySettings.INT) {
				try{
					Integer.parseInt(thesetting.getValue());
				}catch (Exception e) {
					return false;
				}
			}else if(type == PropertySettings.LONG) {
				try{
					Long.parseLong(thesetting.getValue());
				}catch (Exception e) {
					return false;
				}
			}
		}
		return true;
	}
	
	void addReservedWords() {
		reservedwords.clear();
		reservedwords.add("abstract");
		reservedwords.add("boolean");
		reservedwords.add("Boolean");
		reservedwords.add("break");
		reservedwords.add("byte");
		reservedwords.add("byvalue");
		reservedwords.add("case");
		reservedwords.add("cast");
		reservedwords.add("catch");
		reservedwords.add("char");
		reservedwords.add("class");
		reservedwords.add("const");
		reservedwords.add("continue");
		reservedwords.add("default");
		reservedwords.add("do");
		reservedwords.add("double");
		reservedwords.add("Double");
		reservedwords.add("else");
		reservedwords.add("extends");
		reservedwords.add("final");
		reservedwords.add("finally");
		reservedwords.add("float");
		reservedwords.add("Float");
		reservedwords.add("for");
		reservedwords.add("future");
		reservedwords.add("generic");
		reservedwords.add("goto");
		reservedwords.add("if");
		reservedwords.add("implements");
		reservedwords.add("import");
		reservedwords.add("inner");
		reservedwords.add("instanceof");
		reservedwords.add("int");
		reservedwords.add("Integer");
		reservedwords.add("interface");
		reservedwords.add("long");
		reservedwords.add("Long");
		reservedwords.add("native");
		reservedwords.add("new");
		reservedwords.add("null");
		reservedwords.add("operator");
		reservedwords.add("outer");
		reservedwords.add("package");
		reservedwords.add("private");
		reservedwords.add("protected");
		reservedwords.add("public");
		reservedwords.add("rest");
		reservedwords.add("return");
		reservedwords.add("short");
		reservedwords.add("Short");
		reservedwords.add("static");
		reservedwords.add("super");
		reservedwords.add("switch");
		reservedwords.add("synchronized");
		reservedwords.add("this");
		reservedwords.add("throw");
		reservedwords.add("throws");
		reservedwords.add("transient");
		reservedwords.add("try");
		reservedwords.add("var");
		reservedwords.add("void");
		reservedwords.add("volatile");
		reservedwords.add("while");
	}

}

class UpdateTextField implements Runnable {
	
	String updatedstring;
	int caretposition;
	JTextArea tarea;
	
	public UpdateTextField(String string, int position, JTextArea textarea) {
		updatedstring = string;
		caretposition = position;
		tarea = textarea;
	}

	@Override
	public void run() {
		tarea.setText(updatedstring);
		tarea.setCaretPosition(caretposition + 2);
		
	}
}

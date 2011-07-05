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

import java.io.File;

public abstract interface GeneratorPane {
	
	public abstract String getEnable(String pluginname);
	public abstract String getDisable(String pluginname);
	public abstract String getInitialize(String pluginname);
	public abstract String getTabName();
	public abstract void createFiles(String pluginname, File location);
	public abstract String getCustom(String pluginname);
	public abstract String getMain(String pluginname);

}

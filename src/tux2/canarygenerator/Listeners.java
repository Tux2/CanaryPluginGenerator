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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.LinkedList;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

public class Listeners extends JPanel implements GeneratorPane {
	
	LinkedList<ListenerDefs> listeners = new LinkedList<ListenerDefs>();
	
	JCheckBox cbOnarmswing = new JCheckBox("onArmSwing()");
	JCheckBox cbOnattack = new JCheckBox("onAttack()");
	JCheckBox cbOnban = new JCheckBox("onBan()");
	JCheckBox cbOnblockbreak = new JCheckBox("onBlockBreak()");
	JCheckBox cbOnblockdestroy = new JCheckBox("onBlockDestroy()");
	JCheckBox cbOnblockphysics = new JCheckBox("onBlockPhysics()");
	JCheckBox cbOnblockplace = new JCheckBox("onBlockPlace()");
	JCheckBox cbOnblockrightclicked = new JCheckBox("onBlockRightClicked()");
	JCheckBox cbOnchat = new JCheckBox("onChat()");
	JCheckBox cbOncommand = new JCheckBox("onCommand()", true);
	JCheckBox cbOnconsolecommand = new JCheckBox("onConsoleCommand()");
	JCheckBox cbOndamage = new JCheckBox("onDamage()");
	JCheckBox cbOndisconnect = new JCheckBox("onDisconnect()");
	JCheckBox cbOnexplode = new JCheckBox("onExplode()");
	JCheckBox cbOnflow = new JCheckBox("onFlow()");
	JCheckBox cbOnhealthchange = new JCheckBox("onHealthChange()");
	JCheckBox cbOnignite = new JCheckBox("onIgnite()");
	JCheckBox cbOnipban = new JCheckBox("onIpBan()");
	JCheckBox cbOnitemdrop = new JCheckBox("onItemDrop()");
	JCheckBox cbOnitempickup = new JCheckBox("onItemPickup()");
	JCheckBox cbOnitemuse = new JCheckBox("onItemUse()");
	JCheckBox cbOnkick = new JCheckBox("onKick()");
	JCheckBox cbOnLeafDecay = new JCheckBox("onLeafDecay()");
	JCheckBox cbOnLightningStrike = new JCheckBox("onLightningStrike()");
	JCheckBox cbOnliquiddestroy = new JCheckBox("onLiquidDestroy()");
	JCheckBox cbOnlogin = new JCheckBox("onLogin()");
	JCheckBox cbOnloginchecks = new JCheckBox("onLoginChecks()");
	JCheckBox cbOnmobspawn = new JCheckBox("onMobSpawn()");
	JCheckBox cbOnopeninventory = new JCheckBox("onOpenInventory()");
	JCheckBox cbOnplayermove = new JCheckBox("onPlayerMove()");
	JCheckBox cbOnredstonechange = new JCheckBox("onRedstoneChange()");
	JCheckBox cbOnsignchange = new JCheckBox("onSignChange()");
	JCheckBox cbOnsignshow = new JCheckBox("onSignShow()");
	JCheckBox cbOntame = new JCheckBox("onTame()");
	JCheckBox cbOnteleport = new JCheckBox("onTeleport()");
	JCheckBox cbOnthunderchange = new JCheckBox("onThunderChange()");
	JCheckBox cbOnvehiclecollision = new JCheckBox("onVehicleCollision()");
	JCheckBox cbOnvehiclecreate = new JCheckBox("onVehicleCreate()");
	JCheckBox cbOnvehicledamage = new JCheckBox("onVehicleDamage()");
	JCheckBox cbOnvehicledestroyed = new JCheckBox("onVehicleDestroyed()");
	JCheckBox cbOnvehicleenter = new JCheckBox("onVehicleEnter()");
	JCheckBox cbOnvehiclepositionchange = new JCheckBox("onVehiclePositionChange()");
	JCheckBox cbOnvehicleupdate = new JCheckBox("onVehicleUpdate()");
	JCheckBox cbOnweatherchange = new JCheckBox("onWeatherChange()");
	
	
	public Listeners() {
		super();
		buildListenerHashMap();
		JPanel checkboxes = new JPanel();
		checkboxes.setLayout(new BoxLayout(checkboxes, BoxLayout.Y_AXIS));
		for(ListenerDefs thelistener : listeners) {
			checkboxes.add(thelistener.checkbox);
		}
		setLayout(new BorderLayout());
		add("North", new JLabel("Select the listeners you want below:"));
		JEditorPane listenerhelp = new JEditorPane();
		listenerhelp.setContentType("text/html");
		try {
			listenerhelp = new JEditorPane("http://docs.canarymod.net/PluginListener.html");
			listenerhelp.setEditable(false);
			//listenerhelp.setPage("http://docs.canarymod.net/PluginListener.html");
		}catch (Exception e) {
			
		}
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				new JScrollPane(listenerhelp), new JScrollPane(checkboxes));
		splitPane.setDividerLocation(340);
		splitPane.setOneTouchExpandable(true);
		add("Center", splitPane);
	}
	
	void buildListenerHashMap() {
		listeners.add(new ListenerDefs(listenerBuilder("onArmSwing(Player player)", "void"), enableBuilder("ARM_SWING"), cbOnarmswing));
		listeners.add(new ListenerDefs(listenerBuilder("onAttack(LivingEntity attacker, LivingEntity defender, Integer amount) ", "boolean"), enableBuilder("ATTACK"), cbOnattack));
		listeners.add(new ListenerDefs(listenerBuilder("onBan(Player mod, Player player, java.lang.String reason)", "void"), enableBuilder("BAN"), cbOnban));
		listeners.add(new ListenerDefs(listenerBuilder("onBlockBreak(Player player, Block block)", "boolean"), enableBuilder("BLOCK_BROKEN"), cbOnblockbreak));
		listeners.add(new ListenerDefs(listenerBuilder("onBlockDestroy(Player player, Block block)", "boolean"), enableBuilder("BLOCK_DESTROYED"), cbOnblockdestroy));
		listeners.add(new ListenerDefs(listenerBuilder("onBlockPhysics(Block block, boolean placed)", "boolean"), enableBuilder("BLOCK_PHYSICS"), cbOnblockphysics));
		listeners.add(new ListenerDefs(listenerBuilder("onBlockPlace(Player player, Block blockPlaced, Block blockClicked, Item itemInHand)", "boolean"), enableBuilder("BLOCK_PLACE"), cbOnblockplace));
		listeners.add(new ListenerDefs(listenerBuilder("onBlockRightClicked(Player player, Block blockClicked, Item itemInHand)", "void"), enableBuilder("BLOCK_RIGHTCLICKED"), cbOnblockrightclicked));
		listeners.add(new ListenerDefs(listenerBuilder("onChat(Player player, java.lang.String message)", "boolean"), enableBuilder("CHAT"), cbOnchat));
		listeners.add(new ListenerDefs(listenerBuilder("onCommand(Player player, java.lang.String[] split)", "boolean"), enableBuilder("COMMAND"), cbOncommand));
		listeners.add(new ListenerDefs(listenerBuilder("onConsoleCommand(java.lang.String[] split)", "boolean"), enableBuilder("SERVERCOMMAND"), cbOnconsolecommand));
		listeners.add(new ListenerDefs(listenerBuilder("onDamage(PluginLoader.DamageType type, BaseEntity attacker, BaseEntity defender, int amount)", "boolean"), enableBuilder("DAMAGE"), cbOndamage));
		listeners.add(new ListenerDefs(listenerBuilder("onDisconnect(Player player)", "void"), enableBuilder("DISCONNECT"), cbOndisconnect));
		listeners.add(new ListenerDefs(listenerBuilder("onExplode(Block block)", "boolean"), enableBuilder("EXPLODE"), cbOnexplode));
		listeners.add(new ListenerDefs(listenerBuilder("onFlow(Block blockFrom, Block blockTo)", "boolean"), enableBuilder("FLOW"), cbOnflow));
		listeners.add(new ListenerDefs(listenerBuilder("onHealthChange(Player player, int oldValue, int newValue)", "boolean"), enableBuilder("HEALTH_CHANGE"), cbOnhealthchange));
		listeners.add(new ListenerDefs(listenerBuilder("onIgnite(Block block, Player player)", "boolean"), enableBuilder("IGNITE"), cbOnignite));
		listeners.add(new ListenerDefs(listenerBuilder("onIpBan(Player mod, Player player, java.lang.String reason)", "void"), enableBuilder("IPBAN"), cbOnipban));
		listeners.add(new ListenerDefs(listenerBuilder("onItemDrop(Player player, Item item)", "boolean"), enableBuilder("ITEM_DROP"), cbOnitemdrop));
		listeners.add(new ListenerDefs(listenerBuilder("onItemPickUp(Player player, Item item)", "boolean"), enableBuilder("ITEM_PICK_UP"), cbOnitempickup));
		listeners.add(new ListenerDefs(listenerBuilder("onItemUse(Player player, Block blockPlaced, Block blockClicked, Item item)", "boolean"), enableBuilder("ITEM_USE"), cbOnitemuse));
		listeners.add(new ListenerDefs(listenerBuilder("onKick(Player mod, Player player, java.lang.String reason)", "void"), enableBuilder("KICK"), cbOnkick));
		listeners.add(new ListenerDefs(listenerBuilder("onLeafDecay(Block block) ", "boolean"), enableBuilder("LEAF_DECAY"), cbOnLeafDecay));
		listeners.add(new ListenerDefs(listenerBuilder("onLightningStrike(BaseEntity entity)", "boolean"), enableBuilder("LIGHTNING_STRIKE"), cbOnLightningStrike));
		listeners.add(new ListenerDefs(listenerBuilder("onLiquidDestroy(PluginLoader.HookResult currentState, int liquidBlockId, Block targetBlock)", "PluginLoader.HookResult"), enableBuilder("LIQUID_DESTROY"), cbOnliquiddestroy));
		listeners.add(new ListenerDefs(listenerBuilder("onLogin(Player player)", "void"), enableBuilder("LOGIN"), cbOnlogin));
		listeners.add(new ListenerDefs(listenerBuilder("onLoginChecks(java.lang.String user)", "String"), enableBuilder("LOGINCHECK"), cbOnloginchecks));
		listeners.add(new ListenerDefs(listenerBuilder("onMobSpawn(Mob mob)", "boolean"), enableBuilder("MOB_SPAWN"), cbOnmobspawn));
		listeners.add(new ListenerDefs(listenerBuilder("onOpenInventory(Player player, Inventory inventory)", "boolean"), enableBuilder("OPEN_INVENTORY"), cbOnopeninventory));
		listeners.add(new ListenerDefs(listenerBuilder("onPlayerMove(Player player, Location from, Location to)", "void"), enableBuilder("PLAYER_MOVE"), cbOnplayermove));
		listeners.add(new ListenerDefs(listenerBuilder("onRedstoneChange(Block block, int oldLevel, int newLevel)", "int"), enableBuilder("REDSTONE_CHANGE"), cbOnredstonechange));
		listeners.add(new ListenerDefs(listenerBuilder("onSignChange(Player player, Sign sign)", "boolean"), enableBuilder("SIGN_CHANGE"), cbOnsignchange));
		listeners.add(new ListenerDefs(listenerBuilder("onSignShow(Player player, Sign sign)", "void"), enableBuilder("SIGN_SHOW"), cbOnsignshow));
		listeners.add(new ListenerDefs(listenerBuilder("onTame(Player player, Mob wolf)", "PluginLoader.HookResult"), enableBuilder("TAME"), cbOntame));
		listeners.add(new ListenerDefs(listenerBuilder("onTeleport(Player player, Location from, Location to)", "boolean"), enableBuilder("TELEPORT"), cbOnteleport));
		listeners.add(new ListenerDefs(listenerBuilder("onThunderChange(World world, boolean newValue)", "boolean"), enableBuilder("THUNDER_CHANGE"), cbOnthunderchange));
		listeners.add(new ListenerDefs(listenerBuilder("onVehicleCollision(BaseVehicle vehicle, BaseEntity collisioner)", "Boolean"), enableBuilder("VEHICLE_COLLISION"), cbOnvehiclecollision));
		listeners.add(new ListenerDefs(listenerBuilder("onVehicleCreate(BaseVehicle vehicle)", "void"), enableBuilder("VEHICLE_CREATE"), cbOnvehiclecreate));
		listeners.add(new ListenerDefs(listenerBuilder("onVehicleDamage(BaseVehicle vehicle, BaseEntity attacker, int damage)", "boolean"), enableBuilder("VEHICLE_DAMAGE"), cbOnvehicledamage));
		listeners.add(new ListenerDefs(listenerBuilder("onVehicleDestroyed(BaseVehicle vehicle)", "void"), enableBuilder("VEHICLE_DESTROYED"), cbOnvehicledestroyed));
		listeners.add(new ListenerDefs(listenerBuilder("onVehicleEnter(BaseVehicle vehicle, HumanEntity player)", "void"), enableBuilder("VEHICLE_ENTERED"), cbOnvehicleenter));
		listeners.add(new ListenerDefs(listenerBuilder("onVehiclePositionChange(BaseVehicle vehicle, int x, int y, int z)", "void"), enableBuilder("VEHICLE_POSITIONCHANGE"), cbOnvehiclepositionchange));
		listeners.add(new ListenerDefs(listenerBuilder("onVehicleUpdate(BaseVehicle vehicle)", "void"), enableBuilder("VEHICLE_UPDATE"), cbOnvehicleupdate));
		listeners.add(new ListenerDefs(listenerBuilder("onWeatherChange(World world, boolean newValue)", "boolean"), enableBuilder("WEATHER_CHANGE"), cbOnweatherchange));
	}
	
	String listenerBuilder(String constructor, String returns) {
		String theReturn = "\n\tpublic " + returns + " " + constructor + " {\n\t\t// TODO: Add code\n\t";
		if(!returns.equals("void")) {
			if(returns.equals("boolean")) {
				theReturn = theReturn + "return false;\n\t";
			}else if(returns.equals("String")) {
				theReturn = theReturn + "return null;\n\t";
			}else if(returns.equals("int")) {
				theReturn = theReturn + "return newLevel;\n\t";
			}else if(returns.equals("PluginLoader.HookResult")) {
				theReturn = theReturn + "return PluginLoader.HookResult.DEFAULT_ACTION;\n\t";
			}else if(returns.equals("Boolean")) {
				theReturn = theReturn + "return new Boolean(false);\n\t";
			}
		}
		theReturn = theReturn + "}\n";
		return theReturn;
	}
	
	String enableBuilder(String enabler) {
		return "etc.getLoader().addListener( PluginLoader.Hook." + enabler + ", l, this, PluginListener.Priority.MEDIUM);";
	}

	@Override
	public String getEnable(String pluginname) {
		String enablingstring = pluginname + "Listener l = new " + pluginname + "Listener(this);\n\t\t";
		for(ListenerDefs thelistener : listeners) {
			if(thelistener.checkbox.isSelected()) {
				enablingstring = enablingstring + thelistener.onEnable + "\n\t\t";
			}
		}
		return enablingstring;
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
		return "Listeners";
	}

	@Override
	public void createFiles(String pluginname, File location) {
		File srcdirectory = new File(location, "src");
		File saving = new File(srcdirectory, pluginname + "Listener.java");
		String thelistener = "public class " + pluginname + "Listener extends PluginListener {\n" +
				"	\n" +
				"	private final " + pluginname + " plugin;\n" + 
				"\n" + 
				"    public " + pluginname + "Listener(" + pluginname + " plugin) {\n" + 
				"        this.plugin = plugin;\n" + 
				"    }\n\t";
		for(ListenerDefs thelistener1 : listeners) {
			if(thelistener1.checkbox.isSelected()) {
				thelistener = thelistener + thelistener1.constructor + "\n\t";
			}
		}
		thelistener = thelistener + "\n}";
		try {
			if(!srcdirectory.exists()) {
				srcdirectory.mkdirs();
			}
			BufferedWriter outChannel = new BufferedWriter(new FileWriter(saving));
			outChannel.write(thelistener);
			outChannel.close();
		}catch (Exception e) {
			
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

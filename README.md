# Tiny Reactors
Build a tiny reactor... Or a massive one!

[Check out the Trello board for an accurate situation update!](https://trello.com/b/hpjBhZXp/tiny-reactors)

![Image](https://github.com/ArclightTW/Tiny-Reactors/blob/1.12/screenshot.png?raw=true)


# Navigation

* [Changelog](https://github.com/ArclightTW/Tiny-Reactors/blob/1.12/CHANGELOG.md)
* [About](#about)
* [Reactants](#reactants)

# About

__Tiny Reactors__ is a Minecraft mod developed for Minecraft 1.12+.  It is a power generation mod that can either be used passively or actively, depending on several configuration settings modifiable by the mod user.  Valid reactor structures must contain one Reactor Controller and at least one Energy Port of any tier; the frame of the structure must be built from Reactor Casing blocks with the four side faces constructable from either Reactor Casing or Reactor Glass.  The top face of the structure can be built from either Reactor Casing blocks or Reactor Vents, which can be used to slow the temperature gain inside the reactor structure.

To get started with Tiny Reactors, you will need to construct a 3 x 3 x 3 reactor structure, meeting the above specified criteria.  This is the smallest a reactor structure can be made, as it provides 1 internal block for the power source.  The reactor structure can be increased or decreased to any size (with the minimum of 3 x 3 x 3); there is no maximum size for a reactor structure.

__N.B. Please note, the larger the reactor structure, the longer it will take to validate when opening/closing UIs.  This may lead to performance impacts in both single- and multi-player worlds.__

There are a variety of configuration settings that can modify the user's experience of Tiny Reactors.  By default, reactor structures operate passively, meaning they do not consume any power source blocks placed within the structure.  Reactor meltdowns also are disabled by default but can be modified either directly using the configuration files or using the in-game Mod Options.  Reactor Controller blocks do not output energy directly; rather, power must be pulled from the Energy Ports placed within the structure.  That being said, however, the Reactor Controller does have an internal energy buffer that will fill if a reactor is left running and there is nowhere for the power to go.  This buffer filling to capacity will trigger a reactor meltdown, if they are enabled in the configuration.


# Reactants

By default, reactor structures support the following power source blocks (all Vanilla):

<table>
	<tr>
		<th>Block</th>
		<th>Rate (RF/t)</th>
	</tr>
	<tr>
		<td>Coal Ore</td>
		<td>1</td>
	</tr>
	<tr>
		<td>Coal Block</td>
		<td>8</td>
	</tr>
	<tr>
		<td>Iron Ore</td>
		<td>2</td>
	</tr>
	<tr>
		<td>Iron Block</td>
		<td>16</td>
	</tr>
	<tr>
		<td>Lapis Ore</td>
		<td>4</td>
	</tr>
	<tr>
		<td>Lapis Block</td>
		<td>32</td>
	</tr>
	<tr>
		<td>Redstone Ore</td>
		<td>8</td>
	</tr>
	<tr>
		<td>Redstone Block</td>
		<td>64</td>
	</tr>
	<tr>
		<td>Gold Ore</td>
		<td>8</td>
	</tr>
	<tr>
		<td>Gold Block</td>
		<td>64</td>
	</tr>
	<tr>
		<td>Diamond Ore</td>
		<td>16</td>
	</tr>
	<tr>
		<td>Diamond Block</td>
		<td>128</td>
	</tr>
	<tr>
		<td>Emerald Ore</td>
		<td>32</td>
	</tr>
	<tr>
		<td>Emerald Block</td>
		<td>256</td>
	</tr>
	<tr>
		<td>Quartz Ore</td>
		<td>8</td>
	</tr>
	<tr>
		<td>Quartz Block</td>
		<td>32</td>
	</tr>
</table>

This default values can be modified, however, or removed entirely from the reactant registry.  They can also be configured to support additional blocks, whether these be from Vanilla Minecraft or another installed mod.  Reactants can be added to the registry using one of the two below lines in the configuration file (I would recommend using the in-game Mod Options to add/remove entries as these can be tested on the fly):
* [mod_id:block_name:rf/tick] (e.g. 'minecraft:furnace:8' or 'tp:netherstar_block:1024')
* [mod_id:block_name:metadata:rf/tick] (e.g. 'minecraft:wool:0:4')  
Omitting the metadata value when adding entries will permit any variant (e.g. 'minecraft:wool:4' will permit any Wool block).

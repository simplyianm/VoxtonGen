/***** CITYWORLD v0.60
* City world generator for Minecraft/Bukkit
* Copyright (C) 2011-2012 daddychurchill <http://www.virtualchurchill.com>
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program. If not, see <http://www.gnu.org/licenses/>.

Todo
Add command to level a city block
Rivers and bridges
Subway/elevated rail roads
Terrain under the city
Bring back rural blocks
Park benches and other furniture
Need to fix bugs in building roofs (including those silly air conditioning units)

v0.60 Permissions and Config file

Permissions for CityWorld command
* cityworld.command (defaults to op)

Config option file is now generated when CityWorld runs. These options only affect "new generation" and are not world specific (yet). Changing them after a world has been generated will produce some odd results at times.
* Global.BedrockIsolation (defaults to true) //obsidian or bedrock barriers// 
* Global.Plumbing (true) //plumbing between street and underworld//
* Global.Sewer (true) //sewers between street (and plumbing) and underworld//
* Global.Cistern (true) //cisterns beneath parks//
* Global.Basement (true) //basements beneath buildings//
* Global.Underworld (true) //underworld beneath the city's sewers, plumbing, cisterns, etc.//
* Global.TreasureInFountain (true) //treasure ores (coins) in the Fountains//
* Global.TreasureInPlumbing (true) //treasure blocks (stuff flushed down) in the Plumbing//
* Global.TreasureInSewer (true) //treasure chests in the sewer (items include IRON_SPADE through ROTTEN_FLESH, inclusive)//
* Global.SpawnersInSewer (true) //sewers treasure rooms might have spawners//
* Global.StreetLevel (24) //Y coordinate where the streets start (higher this is the shorter the buildings)//
* Global.MaximumFloors (100) //floor count of the tallest building (clamped to what MineCraft can actually do)//

v0.57
Quickie patch to permit support for 1.1

v0.56
Added occasional crane on unfinished buildings
Hacked my way around a number of roof issues, still more that can be done
Added nav lights onto the tallest antenna on a building, if it has any

v0.55
Theming of city blocks (highrise, midrise, lowrise, big parks, etc.)
First pass of roofs (peaks, edges, antennas, air conditioners, etc.)

v0.54
West is North... I really hope I have this issue nailed this time
Rounded buildings and doors seem to be happy at last!
Added unfinished buildings, just to add variety

v0.52
Rounded buildings are back but I still need to get doors and stairs working with them better
Doors and stairs now position themselves better
Started support for unfinished buildings

v0.51
Turned off rounded buildings... for now

v0.50
Added rounded buildings.. but there are loads of issues remaining to be dealt with

v0.40
Added cisterns and most of the tops of parks
Added the command "CityWorld", which will teleport you to (and create) "CityWorld"
Added stairs back in (they might be missing in some buildings but that is pretty rare)
Added doors (like stairs they might be missing in some buildings)
Manholes are more functional (ladders and doors are down there now)
Roundabouts now have fountains, "ART!" or a bit of both
Got BlockPopulators working
Cleaned up the code considerably

v0.30
Added sewers and plumbing
Added vaults
Added basements
Removed stairs (coming back in next release)
Better road layouts
Better building layouts

v0.20
There is still much to do, but it is good beginning. 
We now have a little bit of color. 
There are basements and stairs but no doors... doh! 
Still much to do... including: 
parks (real ones not the silly ones I have now), cisterns, 
neighborhoods, parking structures, sewers, farms and DOORS!

v0.10
wow that is stark
*/
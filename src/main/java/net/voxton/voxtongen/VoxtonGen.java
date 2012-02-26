package net.voxton.voxtongen;

import net.voxton.voxtongen.platmap.PlatMapManager;
import java.util.Random;
import net.voxton.voxtongen.gen.VChunkGenerator;
import net.voxton.voxtongen.command.CommandCreate;
import net.voxton.voxtongen.platmap.PlatMap;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

//This is just a test I'm not changing anything just to test. this text is worthless :D
//DONE Global.BedrockIsolation = obsidian or bedrock barriers (true)
//DONE Global.Plumbing = plumbing between street and underworld (true)
//DONE Global.Sewer = sewers between street (and plumbing) and underworld (true)
//DONE Global.Cistern = cisterns beneath parks (true)
//DONE Global.Basement = basements beneath buildings (true)
//DONE Global.Underworld = underworld beneath the city (true)
//DONE Global.TreasureInFountain = treasure ores in the Fountains (true)
//DONE Global.TreasureInPlumbing = treasure blocks in the Plumbing (true)
//DONE Global.TreasureInSewer = treasure chests in the sewer (true)
//DONE Global.SpawnersInSewer = sewers treasure rooms might have spawners (true)
//TODO Global.SpawnersInPlumbing = plumbing might have spawners (true)
//TODO Global.MineralsInUnderworld = sprinkle minerals in the underworld backfill (true)
//DONE Global.StreetLevel = where the streets start (24)
//DONE Global.MaximumFloors = tallest building (100)
//TODO clamp streetlevel this value to something sane!
//TODO if streetlevel gets too small turn off underworld, sewer, cistern and plumbing!
//TODO streetlevel controls the various constants found in PlatMap
//TODO move the constants in PlatMap to ContextUrban
//TODO "worldname".<option> support for world specific options
//DONE Command.CityWorld
//TODO Command.CityWorld Leave
//TODO Command.CityWorld Regenerate
//TODO Command.CityWorld Regenerate "PlatMapType"
//DONE player.hasPermission("cityworld.command") = CityWorld command enabled (true)
//TODO player.hasPermission("cityworld.cityblock") = CityWorld command block regeneration option enabled (true)
//TODO Dynamically load platmap "engines" from plugin/cityworld/*.platmaps
//TODO Autoregister platmap "generators" from code
//TODO Predictable platmaps types/seeds via noise instead of random
//TODO Oceans/lakes
//TODO Farms
//TODO Residential
//TODO Sewers more maze like
//TODO Sewers with levels
//DONE Sewers with iron bars instead of bricks sometimes
//TODO Sewers with vines coming down
//TODO Sewers with indents to remove the hallways aspect of them
//TODO Sewer treasure chests should be limited in what they can "auto-populate" with
//TODO Mob generators in Sewers.. maybe instead of treasure chests... sometimes
//TODO Treasure chests instead of chunks of ores in the sewers
//TODO Underworld with "noisy" terrain and ores
public class VoxtonGen extends JavaPlugin {
    public static final String WORLD_NAME = "CityWorld";

    private static World cityWorldPrime = null;

    private GenerationSettings settings;

    private PlatMapManager platMapManager;

    @Override
    public void onDisable() {
        // remember for the next time
        saveConfig();

        // tell the world we are out of here
        VLogger.log(getDescription().getFullName() + " has been disabled");
    }

    @Override
    public void onEnable() {
        PluginManager pm = getServer().getPluginManager();
        pm.addPermission(new Permission("cityworld.command", PermissionDefault.OP));

        getCommand("cityworld").setExecutor(new CommandCreate(this));
//		addCommand("cityblock", new CityWorldBlockCMD(this));

        settings = new GenerationSettings(this);
        platMapManager = new PlatMapManager(this);

        // configFile can be retrieved via getConfig()
        VLogger.log(getDescription().getFullName() + " is enabled");
    }

    /**
     * Gets the generation settings as specified in the config.yml.
     *
     * @return
     */
    public GenerationSettings getSettings() {
        return settings;
    }

    public PlatMapManager getPlatMapManager() {
        return platMapManager;
    }

    /**
     * Gets a platmap.
     *
     * @param world
     * @param random
     * @param chunkX
     * @param chunkZ
     * @return
     */
    public PlatMap getPlatMap(Chunk chunk, Random random) {
        return platMapManager.getPlatMap(chunk, random);
    }

    public PlatMap getPlatMap(World world, int cx, int cz, Random random) {
        return platMapManager.getPlatMap(world, cx, cz, random);
    }

    @Override
    public ChunkGenerator getDefaultWorldGenerator(String name, String style) {
        return new VChunkGenerator(this, name, style);
    }

    public World getCityWorld() {
        // created yet?
        if (cityWorldPrime == null) {

            // built yet?
            cityWorldPrime = Bukkit.getServer().getWorld(WORLD_NAME);
            if (cityWorldPrime == null) {

                // if neither then create/build it!
                WorldCreator worldCreator = new WorldCreator(WORLD_NAME);
                worldCreator.environment(World.Environment.NORMAL);
                worldCreator.generator(new VChunkGenerator(this, WORLD_NAME, ""));
                cityWorldPrime = Bukkit.getServer().createWorld(worldCreator);
            }
        }
        return cityWorldPrime;
    }
}

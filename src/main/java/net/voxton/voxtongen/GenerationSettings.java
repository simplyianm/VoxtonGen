/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.voxton.voxtongen;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

/**
 *
 * @author simplyianm
 */
public class GenerationSettings {
        private Material isolationMaterial;

    private boolean doPlumbing;

    private boolean doSewer;

    private boolean doCistern;

    private boolean doBasement;

    private boolean doUnderworld;

    private boolean doTreasureInSewer;

    private boolean doTreasureInPlumbing;

    private boolean doTreasureInFountain;

    private boolean doSpawnerInSewer;

    private int streetLevel;

    private int maximumFloors;

    public final static boolean defaultBedrockIsolation = true;

    public final static boolean defaultDoPlumbing = true;

    public final static boolean defaultDoSewer = true;

    public final static boolean defaultDoCistern = true;

    public final static boolean defaultDoBasement = true;

    public final static boolean defaultDoUnderworld = true;

    public final static boolean defaultDoTreasureInSewer = true;

    public final static boolean defaultDoTreasureInPlumbing = true;

    public final static boolean defaultDoTreasureInFountain = true;

    public final static boolean defaultDoSpawnerInSewer = true;

    public final static int defaultStreetLevel = 24;

    public final static int defaultMaximumFloors = 100;
    
    public GenerationSettings(VoxtonGen plugin) {
        setBedrockIsolation(defaultBedrockIsolation);
        setDoPlumbing(defaultDoPlumbing);
        setDoSewer(defaultDoSewer);
        setDoCistern(defaultDoCistern);
        setDoBasement(defaultDoBasement);
        setDoUnderworld(defaultDoUnderworld);
        setDoTreasureInSewer(defaultDoTreasureInSewer);
        setDoTreasureInPlumbing(defaultDoTreasureInPlumbing);
        setDoTreasureInFountain(defaultDoTreasureInFountain);
        setDoSpawnerInSewer(defaultDoSpawnerInSewer);
        setStreetLevel(defaultStreetLevel);
        
        FileConfiguration config = plugin.getConfig();
        config.options().header("CityWorld Global Options");
        config.addDefault("Global.BedrockIsolation", defaultBedrockIsolation);
        config.addDefault("Global.Plumbing", defaultDoPlumbing);
        config.addDefault("Global.Sewer", defaultDoSewer);
        config.addDefault("Global.Cistern", defaultDoCistern);
        config.addDefault("Global.Basement", defaultDoBasement);
        config.addDefault("Global.Underworld", defaultDoUnderworld);
        config.addDefault("Global.TreasureInFountain", defaultDoTreasureInFountain);
        config.addDefault("Global.TreasureInPlumbing", defaultDoTreasureInPlumbing);
        config.addDefault("Global.TreasureInSewer", defaultDoTreasureInSewer);
        config.addDefault("Global.SpawnerInSewer", defaultDoSpawnerInSewer);
        config.addDefault("Global.StreetLevel", defaultStreetLevel);
        config.addDefault("Global.MaximumFloors", defaultMaximumFloors);
        config.options().copyDefaults(true);
        plugin.saveConfig();

        // now read out the bits for real
        setBedrockIsolation(config.getBoolean("Global.BedrockIsolation"));
        setDoPlumbing(config.getBoolean("Global.Plumbing"));
        setDoSewer(config.getBoolean("Global.Sewer"));
        setDoCistern(config.getBoolean("Global.Cistern"));
        setDoBasement(config.getBoolean("Global.Basement"));
        setDoUnderworld(config.getBoolean("Global.Underworld"));
        setDoTreasureInFountain(config.getBoolean("Global.TreasureInFountain"));
        setDoTreasureInPlumbing(config.getBoolean("Global.TreasureInPlumbing"));
        setDoTreasureInSewer(config.getBoolean("Global.TreasureInSewer"));
        setDoSpawnerInSewer(config.getBoolean("Global.SpawnerInSewer"));
        setStreetLevel(config.getInt("Global.StreetLevel"));
        setMaximumFloors(config.getInt("Global.MaximumFloors"));
    }
    
    public void setBedrockIsolation(boolean doit) {
        isolationMaterial = doit ? Material.BEDROCK : Material.OBSIDIAN;
    }

    public Material getIsolationMaterial() {
        return isolationMaterial;
    }

    public boolean isDoPlumbing() {
        return doPlumbing;
    }

    public void setDoPlumbing(boolean doit) {
        doPlumbing = doit;
    }

    public boolean isDoSewer() {
        return doSewer;
    }

    public void setDoSewer(boolean doit) {
        doSewer = doit;
    }

    public boolean isDoCistern() {
        return doCistern;
    }

    public void setDoCistern(boolean doit) {
        doCistern = doit;
    }

    public boolean isDoBasement() {
        return doBasement;
    }

    public void setDoBasement(boolean doit) {
        doBasement = doit;
    }

    public boolean isDoUnderworld() {
        return doUnderworld;
    }

    public void setDoUnderworld(boolean doit) {
        doUnderworld = doit;
    }

    public boolean isDoTreasureInSewer() {
        return doTreasureInSewer;
    }

    public void setDoTreasureInSewer(boolean doit) {
        doTreasureInSewer = doit;
    }

    public boolean isDoTreasureInPlumbing() {
        return doTreasureInPlumbing;
    }

    public void setDoTreasureInPlumbing(boolean doit) {
        doTreasureInPlumbing = doit;
    }

    public boolean isDoTreasureInFountain() {
        return doTreasureInFountain;
    }

    public void setDoTreasureInFountain(boolean doit) {
        doTreasureInFountain = doit;
    }

    public boolean isDoSpawnerInSewer() {
        return doSpawnerInSewer;
    }

    public void setDoSpawnerInSewer(boolean doit) {
        doSpawnerInSewer = doit;
    }

    public int getStreetLevel() {
        return streetLevel;
    }

    public void setStreetLevel(int value) {
        streetLevel = value;
    }

    public int getMaximumFloors() {
        return maximumFloors;
    }

    public void setMaximumFloors(int value) {
        maximumFloors = value;
    }
}

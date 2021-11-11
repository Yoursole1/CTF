package me.yoursole.ctf.DataFiles;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import java.lang.Math;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class MathUtils {
    public static int getRandom(int min, int max) {
        return (int) (java.lang.Math.random() * (max - min + 1) + min);
    }

    public static double logBase(double arg, double base){
        return Math.log(arg)/Math.log(base);
    }
    public static double naturalLog(double arg){return Math.log(arg)/Math.log(2.71828);}


}

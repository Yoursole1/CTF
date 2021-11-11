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

    //Functions belongs to SurrendrX
    public static Vector rotateVector(Vector vector, double angle) {
        double sin = Math.sin(angle);
        double cos = Math.cos(angle);
        double x = vector.getX() * cos + vector.getZ() * sin;
        double z = vector.getX() * -sin + vector.getZ() * cos;
        return vector.setX(-x).setZ(-z);
    }

    public Vector getPosition(double distance, Vector origin, Vector direction) {
        return origin.clone().add(direction.clone().multiply(distance));
    }

    public List<Vector> traverse(double distance, double accuracy, Vector origin, Vector direction) {
        List<Vector> positions = new ArrayList();

        for(double d = 0.0D; d <= distance; d += accuracy) {
            positions.add(this.getPosition(d,origin,direction));
        }

        return positions;
    }

    public Block getIntersectedBlock(World world, double distance, double accuracy, Set<Material> transparent, Vector origin, Vector direction) {
        Iterator var7 = this.traverse(distance, accuracy,origin,direction).iterator();

        Block block;
        do {
            if (!var7.hasNext()) {
                return null;
            }

            Vector vector = (Vector)var7.next();
            block = world.getBlockAt(new Location(world, vector.getX(), vector.getY(), vector.getZ()));
        } while(transparent.contains(block.getType()));

        return block;
    }
    //end SurrendrX's functions

}

package me.yoursole.ctf.datafiles

//for storing the blocks to search for the pickaxes glow effect
class Point3D(var x: Int, var y: Int, var z: Int) {

    fun add(other: Point3D): Point3D {
        return Point3D(x + other.x, y + other.y, z + other.z)
    }
}
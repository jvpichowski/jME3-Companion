package com.jme3.util;

import com.jme3.math.*;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import com.jme3.scene.VertexBuffer.Format;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.scene.mesh.IndexBuffer;
import java.nio.FloatBuffer;


/**
 *  Various utility methods for easily extracting different vertex
 *  attributes for triangle corners and then performing barycentric
 *  based interpolation to find the value for some intersection point.
 *
 *  See also: https://en.wikipedia.org/wiki/Barycentric_coordinate_system
 *
 */
public class TriangleUtils {


    public static Vector2f getBarycentricInterpolation( Vector2f coord, Vector2f t0, Vector2f t1, Vector2f t2 ) {
        Vector2f tv1 = t1.subtract(t0);
        Vector2f tv2 = t2.subtract(t0);
        Vector2f result = t0.add(tv1.mult(coord.x)).add(tv2.mult(coord.y));
        return result;
    }

    public static Vector3f getBarycentricInterpolation( Vector2f coord, Vector3f t0, Vector3f t1, Vector3f t2 ) {
        Vector3f tv1 = t1.subtract(t0);
        Vector3f tv2 = t2.subtract(t0);
        Vector3f result = t0.add(tv1.mult(coord.x)).add(tv2.mult(coord.y));
        return result;
    }

    public static Vector4f getBarycentricInterpolation( Vector2f coord, Vector4f t0, Vector4f t1, Vector4f t2 ) {
        Vector4f tv1 = t1.subtract(t0);
        Vector4f tv2 = t2.subtract(t0);
        Vector4f result = t0.add(tv1.mult(coord.x)).add(tv2.mult(coord.y));
        return result;
    }

    public static void getBarycentricInterpolation( Vector2f coord, Mesh mesh, Type type, int index, Vector2f store ) {
        store.set(getBarycentricInterpolation2(coord, mesh, type, index));
    }

    public static void getBarycentricInterpolation( Vector2f coord, Mesh mesh, Type type, int index, Vector3f store ) {
        store.set(getBarycentricInterpolation3(coord, mesh, type, index));
    }

    public static void getBarycentricInterpolation( Vector2f coord, Mesh mesh, Type type, int index, Vector4f store ) {
        store.set(getBarycentricInterpolation4(coord, mesh, type, index));
    }

    public static Vector2f getBarycentricInterpolation2(Vector2f coord, Mesh mesh, Type type, int index ) {
        Vector2f t0 = new Vector2f();
        Vector2f t1 = new Vector2f();
        Vector2f t2 = new Vector2f();
        getTriangle(mesh, type, index, t0, t1, t2);
        return getBarycentricInterpolation(coord, t0, t1, t2);
    }

    public static Vector3f getBarycentricInterpolation3(Vector2f coord, Mesh mesh, Type type, int index ) {
        Vector3f t0 = new Vector3f();
        Vector3f t1 = new Vector3f();
        Vector3f t2 = new Vector3f();
        getTriangle(mesh, type, index, t0, t1, t2);
        return getBarycentricInterpolation(coord, t0, t1, t2);
    }

    public static Vector4f getBarycentricInterpolation4(Vector2f coord, Mesh mesh, Type type, int index ) {
        Vector4f t0 = new Vector4f();
        Vector4f t1 = new Vector4f();
        Vector4f t2 = new Vector4f();
        getTriangle(mesh, type, index, t0, t1, t2);
        return getBarycentricInterpolation(coord, t0, t1, t2);
    }

    public static void getTriangle( Mesh mesh, Type type, int index, Vector2f v1, Vector2f v2, Vector2f v3 ){
        VertexBuffer vb = mesh.getBuffer(type);
        IndexBuffer ib = mesh.getIndicesAsList();
        if( vb != null && vb.getFormat() == Format.Float && vb.getNumComponents() == 2 ) {
            FloatBuffer fpb = (FloatBuffer)vb.getData();

            // aquire triangle's vertex indices
            int vertIndex = index * 3;
            int vert1 = ib.get(vertIndex);
            int vert2 = ib.get(vertIndex+1);
            int vert3 = ib.get(vertIndex+2);

            BufferUtils.populateFromBuffer(v1, fpb, vert1);
            BufferUtils.populateFromBuffer(v2, fpb, vert2);
            BufferUtils.populateFromBuffer(v3, fpb, vert3);
        } else {
            throw new UnsupportedOperationException("Buffer not set for:" + type + " or "
                    + " has incompatible format: Float * 2");
        }
    }

    public static void getTriangle( Mesh mesh, Type type, int index, Vector3f v1, Vector3f v2, Vector3f v3 ){
        VertexBuffer vb = mesh.getBuffer(type);
        IndexBuffer ib = mesh.getIndicesAsList();
        if( vb != null && vb.getFormat() == Format.Float && vb.getNumComponents() == 3 ) {
            FloatBuffer fpb = (FloatBuffer)vb.getData();

            // aquire triangle's vertex indices
            int vertIndex = index * 3;
            int vert1 = ib.get(vertIndex);
            int vert2 = ib.get(vertIndex+1);
            int vert3 = ib.get(vertIndex+2);

            BufferUtils.populateFromBuffer(v1, fpb, vert1);
            BufferUtils.populateFromBuffer(v2, fpb, vert2);
            BufferUtils.populateFromBuffer(v3, fpb, vert3);
        } else {
            throw new UnsupportedOperationException("Buffer not set for:" + type + " or "
                    + " has incompatible format: Float * 3");
        }
    }

    public static void getTriangle( Mesh mesh, Type type, int index, Vector4f v1, Vector4f v2, Vector4f v3 ){
        VertexBuffer vb = mesh.getBuffer(type);
        IndexBuffer ib = mesh.getIndicesAsList();
        if( vb != null && vb.getFormat() == Format.Float && vb.getNumComponents() == 4 ) {
            FloatBuffer fpb = (FloatBuffer)vb.getData();

            // aquire triangle's vertex indices
            int vertIndex = index * 3;
            int vert1 = ib.get(vertIndex);
            int vert2 = ib.get(vertIndex+1);
            int vert3 = ib.get(vertIndex+2);

            BufferUtils.populateFromBuffer(v1, fpb, vert1);
            BufferUtils.populateFromBuffer(v2, fpb, vert2);
            BufferUtils.populateFromBuffer(v3, fpb, vert3);
        } else {
            throw new UnsupportedOperationException("Buffer not set for:" + type + " or "
                    + " has incompatible format: Float * 4");
        }
    }


    public static float getBarycentricCoordinates( Vector2f point, Vector2f p1, Vector2f p2 ){
        Vector2f t = point.subtract(p1);
        Vector2f s = p2.subtract(p1);
        t.rotateAroundOrigin(s.getAngle(), true);
        return t.getX()/s.length();
    }


    public static Vector2f getBarycentricCoordinates( Vector2f point, Vector2f tri0, Vector2f tri1, Vector2f tri2 ){
        Vector2f v0 = tri1.subtract(tri0);
        Vector2f v1 = tri2.subtract(tri0);
        Vector2f v2 = point.subtract(tri0);

        float u = (v2.getX()*v2.getY()-v2.getY()*v1.getX())/(v0.getX()*v1.getY()-v0.getY()*v1.getX());
        float v = (v0.getX()*v2.getY()-v0.getY()*v2.getX())/(v0.getX()*v1.getY()-v0.getY()*v1.getX());

        return new Vector2f(u, v);
    }


    public static Vector2f getBarycentricCoordinates( Vector3f point, Vector3f tri0, Vector3f tri1, Vector3f tri2 ) {

        // Get the triangle edges and the vector to p
        Vector3f v0 = tri1.subtract(tri0);
        Vector3f v1 = tri2.subtract(tri0);
        Vector3f v2 = point.subtract(tri0);

        float dot00 = v0.dot(v0);
        float dot01 = v0.dot(v1);
        float dot02 = v0.dot(v2);
        float dot11 = v1.dot(v1);
        float dot12 = v1.dot(v2);

        float invDenom = 1 / (dot00 * dot11 - dot01 * dot01);
        float u = (dot11 * dot02 - dot01 * dot12) * invDenom;
        float v = (dot00 * dot12 - dot01 * dot02) * invDenom;

        return new Vector2f(u, v);
    }


    public static Vector2f getBarycentricCoordinates( Vector3f p, Mesh mesh, Type type, int index ) {

        Vector3f tri0 = new Vector3f();
        Vector3f tri1 = new Vector3f();
        Vector3f tri2 = new Vector3f();

        getTriangle(mesh, type, index, tri0, tri1, tri2);

        return getBarycentricCoordinates(p, tri0, tri1, tri2);
    }

    public static Vector2f getBarycentricCoordinates( Vector2f p, Mesh mesh, Type type, int index ) {

        Vector2f tri0 = new Vector2f();
        Vector2f tri1 = new Vector2f();
        Vector2f tri2 = new Vector2f();

        getTriangle(mesh, type, index, tri0, tri1, tri2);

        return getBarycentricCoordinates(p, tri0, tri1, tri2);
    }

    public static Vector2f getBarycentricCoordinates( Vector3f p, Mesh mesh, int index ) {
        return getBarycentricCoordinates(p, mesh, Type.Position, index);
    }

}
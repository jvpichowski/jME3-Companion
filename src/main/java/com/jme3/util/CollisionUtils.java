package com.jme3.util;

import com.jme3.collision.CollisionResult;
import com.jme3.math.*;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by jan on 13.06.16.
 */
public class CollisionUtils {

    public static void getUV(CollisionResult collisionResult, Ray ray, Vector2f uv){
        Triangle triangle = new Triangle();
        collisionResult.getTriangle(triangle);

        Triangle worldTriangle = new Triangle();
        Geometry geom = collisionResult.getGeometry();
        getWorldTriangle(geom, triangle, worldTriangle);

        Vector3f loc = new Vector3f();
        ray.intersectWherePlanar(worldTriangle, loc);

        int triangleIndex = collisionResult.getTriangleIndex();
        Vector2f texCoord1 = new Vector2f();
        Vector2f texCoord2 = new Vector2f();
        Vector2f texCoord3 = new Vector2f();
        getTexCoords(geom, triangleIndex, texCoord1, texCoord2, texCoord3);

        Vector2f st = new Vector2f(loc.getY(), loc.getZ());
        getUV(texCoord1, texCoord2, texCoord3, st, uv);
    }

    public static void getUV(CollisionResult collisionResult, Vector2f uv){
        Triangle triangle = new Triangle();
        collisionResult.getTriangle(triangle);

        Triangle worldTriangle = new Triangle();
        Geometry geom = collisionResult.getGeometry();
        getWorldTriangle(geom, triangle, worldTriangle);

        Vector3f contactPoint = collisionResult.getContactPoint();
        Vector3f loc = new Vector3f();
        intersectWherePlanar(worldTriangle, contactPoint, loc);

        int triangleIndex = collisionResult.getTriangleIndex();
        Vector2f texCoord1 = new Vector2f();
        Vector2f texCoord2 = new Vector2f();
        Vector2f texCoord3 = new Vector2f();
        getTexCoords(geom, triangleIndex, texCoord1, texCoord2, texCoord3);

        Vector2f st = new Vector2f(loc.getY(), loc.getZ());
        getUV(texCoord1, texCoord2, texCoord3, st, uv);
    }



    public static void getTexCoords(Geometry geometry, int triangleIndex, Vector2f texCoord1, Vector2f texCoord2, Vector2f texCoord3){
        // Get the mesh from geometry
        Mesh mesh = geometry.getMesh();
        // Get the texCoord buffer for that mesh.
        VertexBuffer texcoords = mesh.getBuffer(VertexBuffer.Type.TexCoord);
        // Get the U/V coordinate
        if(texcoords != null) {
            VertexBuffer index = mesh.getBuffer(VertexBuffer.Type.Index);
            if (index.getData() instanceof ShortBuffer) {
                int index1 = ((ShortBuffer) index.getData()).get(triangleIndex * 3 + 0);
                int index2 = ((ShortBuffer) index.getData()).get(triangleIndex * 3 + 1);
                int index3 = ((ShortBuffer) index.getData()).get(triangleIndex * 3 + 2);
//                index.getData().rewind();

                FloatBuffer fb = (FloatBuffer) texcoords.getData();

                float s0 = fb.get(index1 * 2);
                float t0 = fb.get(index1 * 2 + 1);


                float s1 = fb.get(index2 * 2);
                float t1 = fb.get(index2 * 2 + 1);


                float s2 = fb.get(index3 * 2);
                float t2 = fb.get(index3 * 2 + 1);
                fb.rewind();

                texCoord1.set(s0,t0);
                texCoord2.set(s1,t1);
                texCoord3.set(s2,t2);
            }
        }
    }

    public static void getUV(Vector2f texCoord1, Vector2f texCoord2, Vector2f texCoord3, Vector2f st, Vector2f uv){
        uv.set(texCoord1.add(texCoord2.subtract(texCoord1).mult(st.x))
                .add(texCoord3.subtract(texCoord1).mult(st.y)));
    }

    public static void getWorldTriangle(Geometry geometry, Triangle triangle, Triangle store){
        Transform t = geometry.getWorldTransform();
        t.transformVector(triangle.get1(), store.get1());
        t.transformVector(triangle.get2(), store.get2());
        t.transformVector(triangle.get3(), store.get3());

        store.calculateCenter();
        store.calculateNormal();
    }

    public static void intersectWherePlanar(Triangle triangle, Vector3f point, Vector3f loc){
        //check if point lies on plane
        float dot = triangle.get1().subtract(point).dot(triangle.getNormal());
        if(FastMath.approximateEquals(dot, 0.0f)){
            loc.setX(0.0f);

            Vector3f u = triangle.get2().subtract(triangle.get1());
            Vector3f v = triangle.get3().subtract(triangle.get1());
            Vector3f w = point.subtract(triangle.get1());

            float factor = u.x*v.y - v.x*u.y;
            float s = (w.x*v.y-w.y*v.x)/factor;
            float t = (w.y*u.x-w.x*u.y)/factor;

            loc.setY(s);
            loc.setZ(t);
            return;
        }

        //check ray in one direction
        Ray ray = new Ray(point, triangle.getNormal());
        if(ray.intersectWherePlanar(triangle, loc)){
            return;
        }
        //check ray in other direction
        ray = new Ray(point, triangle.getNormal().negate());
        if(ray.intersectWherePlanar(triangle, loc)){
            return;
        }

        //throw new IllegalStateException("This should never happen!");
    }
}

package com.jme3.util;

import com.jme3.collision.CollisionResult;
import com.jme3.math.*;
import com.jme3.scene.Geometry;
import com.jme3.scene.VertexBuffer;


/**
 * Created by jan on 13.06.16.
 */
public class CollisionUtils {


    public static void getUV(CollisionResult collision, Vector2f store){
        Geometry geom = collision.getGeometry();
        Vector3f localContact = new Vector3f();
        geom.worldToLocal(collision.getContactPoint(), localContact);
        Vector2f bc = TriangleUtils.getBarycentricCoordinates(localContact, geom.getMesh(), collision.getTriangleIndex());
        TriangleUtils.getBarycentricInterpolation(bc, geom.getMesh(), VertexBuffer.Type.TexCoord, collision.getTriangleIndex(), store);
    }

    public static Vector2f getUV(CollisionResult collision){
        Vector2f uv = new Vector2f();
        getUV(collision, uv);
        return uv;
    }

}

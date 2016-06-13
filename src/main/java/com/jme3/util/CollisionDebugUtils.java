package com.jme3.util;

import com.jme3.asset.AssetManager;
import com.jme3.collision.CollisionResult;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Triangle;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Sphere;
import com.jme3.texture.Texture;
import com.jme3.texture.image.ImageRaster;

/**
 * Created by jan on 13.06.16.
 */
public class CollisionDebugUtils {

    public static void markTriangle(CollisionResult collisionResult, Texture texture, ColorRGBA color){
        ImageRaster raster = ImageRaster.create(texture.getImage());
        Geometry geometry = collisionResult.getGeometry();
        int triangleIndex = collisionResult.getTriangleIndex();
        Vector2f texCoord1 = new Vector2f();
        Vector2f texCoord2 = new Vector2f();
        Vector2f texCoord3 = new Vector2f();
        CollisionUtils.getTexCoords(geometry, triangleIndex, texCoord1, texCoord2, texCoord3);

        drawLine(raster, texCoord1, texCoord2, color, 10000);
        drawLine(raster, texCoord2, texCoord3, color, 10000);
        drawLine(raster, texCoord3, texCoord1, color, 10000);

    }

    public static void markTrianglePoints(CollisionResult collisionResult, Texture texture, ColorRGBA color){
        ImageRaster raster = ImageRaster.create(texture.getImage());
        Geometry geometry = collisionResult.getGeometry();
        int triangleIndex = collisionResult.getTriangleIndex();
        Vector2f texCoord1 = new Vector2f();
        Vector2f texCoord2 = new Vector2f();
        Vector2f texCoord3 = new Vector2f();
        CollisionUtils.getTexCoords(geometry, triangleIndex, texCoord1, texCoord2, texCoord3);

        raster.setPixel(Math.round(texCoord1.x*(raster.getWidth()-1)), Math.round(texCoord1.y*(raster.getHeight()-1)), color);
        raster.setPixel(Math.round(texCoord2.x*(raster.getWidth()-1)), Math.round(texCoord2.y*(raster.getHeight()-1)), color);
        raster.setPixel(Math.round(texCoord3.x*(raster.getWidth()-1)), Math.round(texCoord3.y*(raster.getHeight()-1)), color);
    }

    private static void drawLine(ImageRaster raster, Vector2f texCoord1, Vector2f texCoord2, ColorRGBA color, int resolution){
        if(texCoord1.x-texCoord2.x == 0.0f){
            for(float i = 0; i < Math.abs(texCoord1.y-texCoord2.y); i+=1f/resolution){
                float x = texCoord1.x;
                float y = (texCoord1.y-i*Math.signum(texCoord1.y-texCoord2.y));
                raster.setPixel(Math.round(x*(raster.getWidth()-1)), Math.round(y*(raster.getHeight()-1)), color);
            }
            return;
        }
        for(float i = 0; i < Math.abs(texCoord1.x-texCoord2.x); i+=1f/resolution){
            float x = (texCoord1.x-i*Math.signum(texCoord1.x-texCoord2.x));
            float y = (texCoord1.y-(texCoord1.y-texCoord2.y)*i/Math.abs(texCoord1.x-texCoord2.x));
            raster.setPixel(Math.round(x*(raster.getWidth()-1)), Math.round(y*(raster.getHeight()-1)), color);
        }
    }

    public static void markContact(CollisionResult collisionResult, Texture texture, ColorRGBA color){
        ImageRaster raster = ImageRaster.create(texture.getImage());
        Vector2f uv = new Vector2f();
        CollisionUtils.getUV(collisionResult, uv);
        raster.setPixel(Math.round(uv.getX()*(raster.getWidth()-1)), Math.round(uv.getY()*(raster.getHeight()-1)), color);
    }

    public static void addTriangleMarker(CollisionResult collisionResult, Node node, ColorRGBA color, AssetManager assetManager){
        Triangle triangle = new Triangle();
        collisionResult.getTriangle(triangle);
        Triangle worldTriangle = new Triangle();
        Geometry geom = collisionResult.getGeometry();
        CollisionUtils.getWorldTriangle(geom, triangle, worldTriangle);
        markAt(node, worldTriangle.get1(), color, assetManager);
        markAt(node, worldTriangle.get2(), color, assetManager);
        markAt(node, worldTriangle.get3(), color, assetManager);
    }

    public static void addContactMarker(CollisionResult collisionResult, Node node, ColorRGBA color, AssetManager assetManager){
        Vector3f contact = collisionResult.getContactPoint();
        markAt(node, contact, color, assetManager);
    }

    private static void markAt(Node node, Vector3f point, ColorRGBA color, AssetManager assetManager){
        Geometry mark;
        Sphere sphere = new Sphere(30, 30, 0.1f);
        mark = new Geometry("Marker", sphere);
        Material mark_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mark_mat.setColor("Color", color);
        mark.setMaterial(mark_mat);
        mark.setLocalTranslation(point);
        node.attachChild(mark);
    }


}

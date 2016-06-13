package com.jme3.util;

import com.jme3.app.FlyCamAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Cylinder;
import com.jme3.texture.Texture;
import com.jme3.texture.image.ImageRaster;

/**
 * Created by jan on 11.06.16.
 */
public class CollisionUtilsTest extends SimpleApplication {

    public static void main(String[] args){
        CollisionUtilsTest p = new CollisionUtilsTest();
        p.start();
    }

    @Override
    public void simpleInitApp() {
        getStateManager().getState(FlyCamAppState.class).setEnabled(false);

//        Box mesh = new Box( 2f,2f,01.f);
        Cylinder mesh = new Cylinder(20,20, 2, 3);
//        Sphere mesh = new Sphere(25,25,4);
        Geometry geom = new Geometry("window frame", mesh);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture texture = assetManager.loadTexture("Common/Textures/MissingTexture.png");
        mat.setTexture("ColorMap",texture);
        geom.setMaterial(mat);
        geom.move(-5.1f,0,0);
        geom.rotate(0.5f,1f,0.7f);
        rootNode.attachChild(geom);

        ImageRaster imageRaster = ImageRaster.create(texture.getImage());
        for(int x = 0; x < imageRaster.getWidth(); x++){
            for(int y = 0; y < imageRaster.getHeight(); y++){
                imageRaster.setPixel(x, y, ColorRGBA.Red);
            }
        }

        getCamera().setLocation(getCamera().getLocation().add(-5f,0,0));

        Ray ray = new Ray(getCamera().getLocation(), getCamera().getDirection());
        CollisionResults results = new CollisionResults();
        geom.collideWith(ray, results);
        CollisionResult collision = results.getClosestCollision();
        if(collision == null){
            throw new IllegalStateException();
        }

//        CollisionUtils.addTriangleMarker(collision, getRootNode(), ColorRGBA.Green, getAssetManager());
//        CollisionUtils.addContactMarker(collision, getRootNode(), ColorRGBA.Blue, getAssetManager());
//        CollisionUtils.markTriangle(collision, texture, ColorRGBA.Green);
//        CollisionUtils.markContact(collision, texture, ColorRGBA.Blue);
//        CollisionUtils.markTrianglePoints(collision, texture, ColorRGBA.Black);

        Vector2f uv = new Vector2f();
        CollisionUtils.getUV(collision, uv);
        System.out.println(uv);
        imageRaster.setPixel(Math.round(uv.getX()*(imageRaster.getWidth()-1)),
                Math.round(uv.getY()*(imageRaster.getHeight()-1)),
                ColorRGBA.Blue);

    }



//    public static void uv3(CollisionResult collisionResult, Ray ray){
//        Transform t = collisionResult.getGeometry().getWorldTransform();
//        Vector3f origin = ray.origin;
//        Vector3f dir = ray.direction;
//
//    inverse
//        t.transformVector(origin, origin);
//        t.transformVector(dir, dir);
//        Ray r = new Ray(origin, dir);
//
//        Vector3f out = new Vector3f();
//        Triangle tri = new Triangle();
//        collisionResult.getTriangle(tri);
//        r.intersectWherePlanar(tri, out);
//        System.out.println(out);
//
//    }

}

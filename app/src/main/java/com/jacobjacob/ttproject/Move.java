package com.jacobjacob.ttproject;

import android.graphics.Rect;

import com.jacobjacob.ttproject.Tile.Tile;

import java.util.ArrayList;

import static com.jacobjacob.ttproject.Util.*;


public class Move {

    Rect Tilehitbox, OldTilehitbox;
    Rect Hitbox, OldHitbox;

    public Move() {
    }

    public void Move(Vector direction) {

        direction.setZ((float) direction.getZ() * 20);


        float HitboxWidth = TILESIZE  - 4; // Hitbox has Width and Height of a Tile now
        float HitboxHeight = TILESIZE - 4; // Hitbox has Width and Height of a Tile now
        Vector Position = camera.getEye2D();


        float middleX = (float) (Position.getValue(0));
        float middleY = (float) (Position.getValue(1));

        float halfWidth = HitboxWidth / 2;
        float halfHeight = HitboxHeight / 2;

        Vector HitboxCollider = direction;
        this.Hitbox = new Rect((int) (middleX - halfWidth + HitboxCollider.getX()), (int) (middleY - halfHeight + HitboxCollider.getY()), (int) (middleX + halfWidth + HitboxCollider.getX()), (int) (middleY + halfHeight + HitboxCollider.getY()));
        this.OldHitbox = new Rect((int) (middleX - halfWidth), (int) (middleY - halfHeight), (int) (middleX + halfWidth), (int) (middleY + halfHeight));


        Rect Hitbox2 = new Rect((int) ((middleX - halfWidth) / TILESIZE), (int) ((middleY - halfHeight) / TILESIZE), (int) ((middleX + halfWidth) / TILESIZE), (int) ((middleY + halfHeight) / TILESIZE));

        int Scale = 1;
        ArrayList<Tile> closeTiles = KDTREE.getTilesInsideBoundary(new Rect((int) (Hitbox2.left) - Scale, (int) (Hitbox2.top) - Scale, (int) (Hitbox2.right) + Scale, (int) (Hitbox2.bottom) + Scale)); // TILESIZE equals 1 in the Tiles inside boundary method
        COLLISIONTILES = closeTiles;
        COLLIDEDTILES = new ArrayList<>();


        ArrayList<Rect> Hitboxes = new ArrayList<>();

        Hitboxes.add(this.Hitbox);

        HitboxCollider = new Vector(direction.getX(), 0);
        Hitboxes.add(new Rect((int) (middleX - halfWidth + HitboxCollider.getX()), (int) (middleY - halfHeight + HitboxCollider.getY()), (int) (middleX + halfWidth + HitboxCollider.getX()), (int) (middleY + halfHeight + HitboxCollider.getY())));

        HitboxCollider = new Vector(0, direction.getY());
        Hitboxes.add(new Rect((int) (middleX - halfWidth + HitboxCollider.getX()), (int) (middleY - halfHeight + HitboxCollider.getY()), (int) (middleX + halfWidth + HitboxCollider.getX()), (int) (middleY + halfHeight + HitboxCollider.getY())));


        HITBOX = new ArrayList<>();
        HITBOX.addAll(Hitboxes);
        HITBOX.add(this.OldHitbox);

        for (int j = Hitboxes.size() - 1; 0 <= j; j--) {
            for (int i = 0; i < closeTiles.size(); i++) {
                if (closeTiles.get(i).getMaterial() == SELECTEDMATERIAL) {

                    this.Tilehitbox = new Rect(TILESIZE * closeTiles.get(i).getBoundaries().left, TILESIZE * closeTiles.get(i).getBoundaries().top, TILESIZE * closeTiles.get(i).getBoundaries().right, TILESIZE * closeTiles.get(i).getBoundaries().bottom);
                    this.OldTilehitbox = this.Tilehitbox;

                    if (j == 1 || j == 2 || j == 0 && direction.getX() != 0 && direction.getY() != 0) {
                        direction = CollideRectangle2(direction, Hitboxes.get(j), closeTiles.get(i), j);
                    }
                }
            }

        }
        camera.setEye2D(direction);
    }


    public Vector CollideRectangle2(Vector direction, Rect Hitbox, Tile Collided, int count) {

        int Hitboxsize = 0;

        if (Hitbox.bottom + Hitboxsize < this.Tilehitbox.top || Hitbox.top - Hitboxsize > this.Tilehitbox.bottom || Hitbox.left - Hitboxsize > this.Tilehitbox.right || Hitbox.right + Hitboxsize < this.Tilehitbox.left) {
            return direction;
        } else {
            if (!COLLIDEDTILES.contains(Collided)) {
                COLLIDEDTILES.add(Collided);
            }

            if (count == 0 || count == 2) {

                if (direction.getY() != 0) {
                    if (Hitbox.bottom >= this.Tilehitbox.top && this.OldHitbox.bottom < this.OldTilehitbox.top) {
                        direction.setY(0);

                    }
                    if (Hitbox.top <= this.Tilehitbox.bottom && this.OldHitbox.top > this.OldTilehitbox.bottom) {
                        direction.setY(0);
                    }
                }
            }
            if (count == 0 || count == 1) {

                if (direction.getX() != 0) {
                    if (Hitbox.right >= this.Tilehitbox.left && this.OldHitbox.right < this.OldTilehitbox.left) {
                        direction.setX(0);
                    }
                    if (Hitbox.left <= this.Tilehitbox.right && this.OldHitbox.left > this.OldTilehitbox.right) {
                        direction.setX(0);
                    }
                }
            }
            return direction;
        }
    }

}

package com.jacobjacob.ttproject;

import android.graphics.Rect;

import static com.jacobjacob.ttproject.Util.camera;


public class Move {

    Rect Tilehitbox;
    Rect Hitbox;
    int Collisionscale;

    public Move() {
    }

    public void Move(Vector direction) {

        Vector directionoriginal = direction;

        directionoriginal.setX((float)(direction.getValue(0) / 2));
        directionoriginal.setY((float)(direction.getValue(1) / 2));
        directionoriginal.setZ((float)(direction.getValue(2) * 20));

        //Collisionscale = 100;
        //float HitboxWidth = 1;
        //float HitboxHeight = 1;
        ////float diagonal = (float) Math.sqrt(HitboxWidth * HitboxWidth + HitboxHeight * HitboxHeight);

        ////Log.d("Original direction", "X:" + String.valueOf(direction.getValue(0)) + "Y:" + String.valueOf(direction.getValue(1)));

        //Vector Position = new Vector().getTileCoordinatesfromScreencoordinates(WIDTHSCREEN / 2, HEIGHTSCREEN / 2);
        ////Camera is in the middle of the screen, therefore Widthscreen/2, Heightscreen/2

        //float middleX = (float) (Position.getValue(0)/**/);/*/ + 0.5 * HitboxWidth);/**/
        //float middleY = (float) (Position.getValue(1)/**/);/*/ + 0.5 * HitboxHeight);/**/

        //float halfWidth = HitboxWidth / 2;
        //float halfHeight = HitboxHeight / 2;

        //Rect Hitbox = new Rect((int) (middleX - halfWidth), (int) (middleY - halfHeight), (int) (middleX + halfWidth), (int) (middleY + halfHeight));
        //HITBOX = Hitbox;

        //ArrayList<Tile> closeTiles = KDTREE.getTilesInsideBoundary(new Rect((Hitbox.left - 1), (Hitbox.top - 1), (Hitbox.right + 1), (Hitbox.bottom + 1)));

        ////Hitbox = new Rect((int) (Collisionscale * (middleX - halfWidth)), (int) (Collisionscale * (middleY - halfHeight)), (int) (Collisionscale * (middleX + halfWidth)), (int) (Collisionscale * (middleY + halfHeight)));
        //this.Hitbox = new Rect((int) (Collisionscale * (middleX - halfWidth)), (int) (Collisionscale * (middleY - halfHeight)), (int) (Collisionscale * (middleX + halfWidth)), (int) (Collisionscale * (middleY + halfHeight)));;
        ////HITBOX = Hitbox;
        //for (int i = 0; i < closeTiles.size(); i++) {
        //    if (closeTiles.get(i).getBoundaries().intersect(Hitbox)) {

        //        Rect TileRect = new Rect(Collisionscale * closeTiles.get(i).getBoundaries().left, Collisionscale * closeTiles.get(i).getBoundaries().top, Collisionscale * closeTiles.get(i).getBoundaries().right, Collisionscale * closeTiles.get(i).getBoundaries().bottom);
        //        this.Tilehitbox = TileRect;
        //        direction = getNewDirection(direction);
        //    }

        //}

        //Log.d("New direction", "X:" + String.valueOf(direction.getValue(0)) + "Y:" + String.valueOf(direction.getValue(1)));
        //Log.d("LIST SIZE", String.valueOf(closeTiles.size()));

        //camera.setEye2D(direction);
        camera.setEye2D(directionoriginal);
    }

    private Vector getNewDirection(Vector direction) {
/*/
        Vector HitboxMiddle = new Vector((this.Hitbox.left + this.Hitbox.right) / 2, (this.Hitbox.top + this.Hitbox.bottom) / 2);
        Vector TileMiddle = new Vector((this.Tilehitbox.left + this.Tilehitbox.right) / 2, (this.Tilehitbox.top + this.Tilehitbox.bottom) / 2);

        float newValue;
        if (direction.getValue(0) != 0) {
            if (HitboxMiddle.getValue(0) < TileMiddle.getValue(0)) { // Hitbox is left of Tile
                if (direction.getValue(0) > 0) { // only matters if it wants to move right
                    newValue = LeftmovingRight();
                    if (newValue < direction.getValue(0)) {
                        direction.setX(newValue);

                    }
                }
            } else { // Hitbox is right of Tile
                if (direction.getValue(0) < 0) { // only matters if it wants to move left
                    newValue = RightmovingLeft();
                    //if (newValue > direction.getValue(0)) {
                        direction.setX(newValue);
                    //}
                }
            }
            //direction.setX((float) (direction.getValue(0) * TILESIZE));
        }
        if (direction.getValue(1) != 0) {
            if (HitboxMiddle.getValue(1) < TileMiddle.getValue(1)) { // Hitbox is above Tile
                if (direction.getValue(1) > 0) { // only matters if it wants to move down
                    newValue = UpmovingDown();
                    if (newValue < direction.getValue(1)) {
                        direction.setY(newValue);
                    }
                }
            } else {
                if (direction.getValue(1) < 0) { // only matters if it wants to move up
                    newValue = DownmovingUp();
                    //if (newValue > direction.getValue(1)) {
                        direction.setY(newValue);
                    //}
                }
            }
            //direction.setY((float) (direction.getValue(1) * TILESIZE));
        }

        int min = 2;
        int min2 = 0;
        if (direction.getValue(0) < min && 0 < direction.getValue(0)) {
            direction.setX(min2);
        }

        if (direction.getValue(1) < min && 0 < direction.getValue(1)) {
            direction.setY(min2);
        }

        if (direction.getValue(0) > -min && 0 > direction.getValue(0)) {
            direction.setX(-min2);
        }

        if (direction.getValue(1) > -min && 0 > direction.getValue(1)) {
            direction.setY(-min2);
        }/**/
        return direction;
    }

    private float LeftmovingRight() {
        return (this.Tilehitbox.left - this.Tilehitbox.right) / Collisionscale;
    }

    private float RightmovingLeft() {
        return (this.Hitbox.right - this.Tilehitbox.left) / Collisionscale;
    }

    private float UpmovingDown() {
        return (this.Tilehitbox.top - this.Hitbox.bottom) / Collisionscale;
    }

    private float DownmovingUp() {
        return (this.Hitbox.top - this.Tilehitbox.bottom) / Collisionscale;
    }
}

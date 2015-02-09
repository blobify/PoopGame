package com.antibot.food.gameobj;


public abstract class RectangularCollidableObject extends CollidableObject {

    float width, height;

    public RectangularCollidableObject(int objectType, int drawPriority)
    {
        super(objectType,drawPriority,BOUNDING_BOX_RECTANGLE);

    }


}

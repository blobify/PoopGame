package com.antibot.food.gameobj;

/**
 * Created by blob on 12/10/14.
 */
public abstract class CircularCollidableObject extends CollidableObject {

    float radius;

    public CircularCollidableObject( int objectType, int drawPriority)
    {
        super(objectType,drawPriority,BOUNDING_BOX_CIRCLE);

    }




}

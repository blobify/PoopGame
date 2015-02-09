package com.antibot.food.gameobj;

import com.antibot.food.AnimeCounter;
import com.antibot.food.Assets;
import com.antibot.food.Camera;
import com.antibot.food.ObjectHandler;
import com.antibot.food.Static;
import com.antibot.food.World;
import com.antibot.food.WorldRenderer;
import com.framework.shaderPrograms.TextureShaderProgram;
import com.game.framework.gl.SpriteBatcher;
import com.game.framework.gl.TextureRegion;
import com.game.math.Vector2;

import java.util.Random;

public class BigBaddy extends CircularCollidableObject {

    // public static final float DOWN_VERTICAL_SPEED = 1.6f;
    public static final float UP_VERTICAL_SPEED = 4f;
    public static final float ACCELERATION_X = 17f, ACCELERATION_Y = 1.5f, MAX_VEL_X = 5, MAX_VEL_Y = 4f;
    public static final float MAX_ANGLE_X = 15; // degrees
    public static final float DELTA_DISTANCE_MAX = 4;

    public static final int INDEX_NORM = 0, INDEX_WING = 3;
    public static final float SCALE_SMALL = 0.6f, SCALE_MED = 0.8f, SCALE_LARGE = 1f;
    //AnimeCounter animeCounterBody;
    public static final float SCALE_ARRAY[] = {SCALE_SMALL, SCALE_MED, SCALE_LARGE};
    public static final byte  CODE_SIZE_RAND = 3;

    public float acceleration;
    public Vector2 acc, vel;
    public float angle;
    public float angleToReach;
    public boolean inPursuit;
    public boolean indicatorDrawable = false;
    // public static final float TEXTURE_WIDTH_TO_HEIGHT_RATIO = 120f/70;
    public float scale;
    public float scaleToReach;
    //public final float SCALE_VEL = 0.75f;
    public boolean scaleReached;
    public float bodyDrawWidth;
    public float bodyDrawHeight;


    AnimeCounter animeCounterWings;
    AnimeCounter.AnimPackage animeWing;
    AnimeCounter.AnimPackage animeBody;

    public BigBaddy() {
        super(ObjectHandler.INDEX_BIG_BADDY, DRAW_PRIORITY_MED);

        animeCounterWings = new AnimeCounter();
        //animeCounterBody = new AnimeCounter();

        acc = new Vector2();
        vel = new Vector2();
    }

    public void set(float posX, float posY, byte sizeCode) {
        super.set(posX, posY);

        this.radius = getMeSizeFromIndex(sizeCode);

        Random rand = Static.rand;

        vel.set(rand.nextBoolean() ? rand.nextFloat() * MAX_VEL_X : -rand.nextFloat() * MAX_VEL_X, 0);
        acc.set(0, 0);
        angle = 180;
        inPursuit = false;
        indicatorDrawable = false;

        animeCounterWings.setAnime(new AnimeCounter.AnimPackage(Assets.bb_wing_arr, AnimeCounter.ANIME_REVERSE, -1, 3));

        acceleration = rand.nextFloat() * 8 + 13;
    }

    private float getMeSizeFromIndex(byte sizeCode) {
        int index = sizeCode;

        if (sizeCode == CODE_SIZE_RAND) {
            index = Static.rand.nextInt(SCALE_ARRAY.length);
        }

        scaleToReach = scale = SCALE_ARRAY[index];
        scaleReached = true;

        TextureRegion body = Assets.big_baddy_norm1;

        bodyDrawWidth = scale * body.drawWidth;

        bodyDrawHeight = scale * body.drawHeight;

        return bodyDrawHeight / 2; // radius

    }


    @Override
    public void update(float deltaTime) {

        if (state == STATE_ALIVE_AND_KICKING) {

            Musca musca = Static.musca;
            Camera cam = Static.cam;

            float deltaPosX = musca.pos.x - pos.x;
            // acc.x = (deltaPosX > 0)? ACCELERATION_X: -ACCELERATION_X;
            acc.x = (deltaPosX > 0) ? acceleration : -acceleration;

            vel.x += acc.x * deltaTime;

            if (Math.abs(vel.x) > MAX_VEL_X) {
                if (vel.x < 0)
                    vel.x = -MAX_VEL_X;
                else
                    vel.x = MAX_VEL_X;
            }

            pos.x += vel.x * deltaTime;

            if (inPursuit) {
                float deltaPosY = musca.pos.y - pos.y;
                acc.y = (deltaPosY > 0) ? ACCELERATION_Y : -ACCELERATION_Y;

                vel.y += acc.y * deltaTime;

                if (Math.abs(vel.y) > MAX_VEL_Y) {
                    if (vel.y < 0)
                        vel.y = -MAX_VEL_Y;
                    else
                        vel.y = MAX_VEL_Y;
                }

                pos.y += vel.y * deltaTime;

            } else {
                acc.y = -1;
                vel.y = -1;
                pos.y += vel.y * deltaTime;
            }

            updateScale(deltaTime, 0.5f);

            // calculating angle

            angle = MAX_ANGLE_X / MAX_VEL_X * vel.x;

            if (pos.y < cam.bottomPos) {
                indicatorDrawable = true;

					/*final float deltaDistance = musca.pos.y - pos.y;
                    if (deltaDistance > 0 && deltaDistance > DELTA_DISTANCE_MAX)
					{
						disable();
					}*/

            } else {
                indicatorDrawable = false;
            }

            if (!inPursuit && pos.y < musca.pos.y) {
                inPursuit = true;
            }

            teleportify(radius, true);

            animeCounterWings.udpate();


        } else if (state == STATE_DEATH_PHASE) {

            pos.x += vel.x * deltaTime;
            pos.y += vel.y * deltaTime;


            updateScale(deltaTime, 1.5f);

            if (scaleReached) {
                disable();
            }

        }


    }

    private void updateScale(float deltaTime, float scaleVel) {
        if (!scaleReached) {
            if (scale < scaleToReach) {
                scale += scaleVel * deltaTime;

                if (scale > scaleToReach) {
                    scaleReached = true;
                    scale = scaleToReach;
                }
            } else if (scale > scaleToReach) {
                scale -= scaleVel * deltaTime;

                if (scale < scaleToReach) {
                    scaleReached = true;
                    scale = scaleToReach;
                }
            }

            TextureRegion body = Assets.big_baddy_norm1;

            bodyDrawWidth = scale * body.drawWidth;

            bodyDrawHeight = scale * body.drawHeight;

            radius = bodyDrawHeight / 2;
        }

    }

    public void interactWithFood(Food food) {

        if (collidable && food.collidable && CollidableObject.checkCircleCircleCollision(pos, Food.RANGE, food.pos, 0))//checkCircleCollision(pos, RANGE, food.pos, 0))
        {
            if (CollidableObject.checkCircleCircleCollision(pos, radius, food.pos, food.radius)) {
                food.eaten(this);
                increaseScaleToReach();
            }
        }
    }

    public void interactWithDumboeba(Dumboeba dumboeba) {
        if(standard_CC_CollsionCheck(this,dumboeba))
        {
            dumboeba.deathToYou(1+Static.rand.nextInt(2));
            deflect(dumboeba.pos);
        }
    }


    public void interactWithShooter(Shooter shooter) {
        if(standard_CC_CollsionCheck(this,shooter))
        {
            shooter.deathToYou(1+Static.rand.nextInt(3));
        }
    }

    public void interactWithBullet(Bullet bullet) {

        if(standard_CC_CollsionCheck(this,bullet))
        {
                bullet.deathToYou();

                deflect(bullet.pos);
        }
    }

    public void interactWithWallBlock(WallBlock wallblock) {
        if(standard_CR_CollisionCheck(this,wallblock))
        {
            wallblock.deathToYou(1 + Static.rand.nextInt(2));

            deflect(wallblock.pos);
        }
    }

    public void interactWithBomb(Bomb bomb) {

        if (standard_CC_CollsionCheck(this, bomb)) {

            bomb.explode();
            deflect(bomb.pos);

        }

    }

    private void increaseScaleToReach() {

        scaleToReach += 0.2f;
        if (scaleToReach > SCALE_LARGE) {
            scaleToReach = SCALE_LARGE;
            return;
        }
        scaleReached = false;
    }

    private boolean decreaseScaleToReach() {
        scaleToReach -= 0.2f;
        if (scaleToReach < SCALE_SMALL) {

            deathToYou(1+Static.rand.nextInt(3));

            return false;
            // setPosAndScale disableOverlayDrawable thing here
        }
        scaleReached = false;
        return true;
    }

    public void deflect(Vector2 objectPos) {
        //Vector2 deflectionVector = Static.tempVector;
        //bigBaddyEventListener.onDeflect((pos.x+objectPos.x)/2, (pos.y + objectPos.y)/2);
        addSomeBigBaddyParticles((pos.x + objectPos.x) / 2, (pos.y + objectPos.y) / 2, Static.rand.nextInt(3) + 2);

        if (decreaseScaleToReach()) {
            if (objectPos.x > pos.x) {
                vel.add(-12, 0);
            } else {
                vel.add(12, 0);
            }

        } else {
            if (objectPos.x > pos.x) {
                vel.add(-3, 0);
            } else {
                vel.add(3, 0);
            }
        }


        //deflectionVector.setPosAndScale(pos.x - objectPos.x, pos.y - objectPos.y).nor().mul(12);


        //vel.add(deflectionVector);  // adding deflection vector to velocity so it deflects :3

        //disableOverlayDrawable = true;

    }


    private void addSomeBigBaddyParticles(float posX, float posY, int count) {

        for (int i = 0; i < count; i++) {
            Particle p = Static.objHandler.addParticle();
            if (p != null) {
                p.setBasics_Alpha(posX, posY, Static.rand.nextFloat() * 0.3f, 1, 1.3f, Assets.bigb_particle_arr[Static.rand.nextInt(Assets.big_baddy_arr.length)]);
                p.setVelRandomized(2);
                p.setAngularVel(0, Static.rand.nextFloat() * 200);
            } else
                break;
        }
    }

    @Override
    public void draw() {
        SpriteBatcher batcher = WorldRenderer.batcher;
        TextureShaderProgram texShaderProgram = WorldRenderer.texShaderProgram;
        World world = Static.world;

        TextureRegion indicator = Assets.bb_indicator;

        TextureRegion wings = animeCounterWings.arr[animeCounterWings.currentIndex];//Assets.bigBaddyRegions[animCounterWings.index];

        batcher.drawCarefulSprite(pos.x, pos.y, wings.drawWidth * scale, wings.drawHeight * scale, angle, wings, texShaderProgram);

        //TextureRegion regions[] = Assets.bigBaddyRegions;

        // TextureRegion eyeRegion = Assets.big_baddy_eye_up;

        // batcher.drawCarefulSprite(pos.x, pos.y, eyeRegion.drawWidth,
        // eyeRegion.drawHeight, angle, eyeRegion, texShaderProgram);

        //TextureRegion bodyRegion = regions[animBodyCounter.index];
        TextureRegion bodyRegion = Assets.big_baddy_norm1;
        batcher.drawCarefulSprite(pos.x, pos.y, bodyDrawWidth, bodyDrawHeight, angle, bodyRegion, texShaderProgram);

        if (indicatorDrawable) {
            final float indicatorPosY = Static.cam.bottomPos + indicator.drawWidth / 2;
            batcher.drawCarefulSprite(pos.x, indicatorPosY, indicator.drawWidth, indicator.drawHeight, indicator, texShaderProgram);
        }


        //TextureRegion circle = Assets.food1;

        //batcher.drawCarefulSprite(pos.x, pos.y, radius*2, radius*2, circle, texShaderProgram);
    }

    @Override
    public void deathToYou() {

        super.deathToYou();
        addSomeBigBaddyParticles(pos.x, pos.y, 5);

        scaleReached = false;
        scaleToReach = 0;
    }

    public void deathToYou(int numberOfCoinToSpawn)
    {
        Static.objHandler.addCoins(pos.x, pos.y, numberOfCoinToSpawn);

        deathToYou();
    }

    @Override
    public float getBottomPos() {
        return pos.y - radius;
    }

    @Override
    public float getTopPos() {
        return pos.y + radius;
    }


}

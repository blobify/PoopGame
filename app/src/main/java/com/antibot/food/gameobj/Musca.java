package com.antibot.food.gameobj;


import com.antibot.food.WorldRenderer;
import com.antibot.food.AnimeCounter;
import com.antibot.food.AnimeCounter.AnimPackage;
import com.antibot.food.Assets;
import com.antibot.food.Camera;
import com.antibot.food.ObjectHandler;
import com.antibot.food.Static;
import com.antibot.food.UpdateAndDraw;
import com.antibot.food.UpdateWork;
import com.framework.shaderPrograms.TextureShaderProgram;
import com.game.framework.gl.SpriteBatcher;
import com.game.framework.gl.TextureRegion;
import com.game.math.Vector2;

import java.util.ArrayList;

;

public class Musca extends CircularCollidableObject {

    public static final float RADIUS = 0.25f;
    public static final float ACCELERATION = 10.0f;
    public static final float HEAD_START_VEL_Y = 12f, HEAD_START_DISTANCE = 50;
    public static final float NORM_VEL_Y = 1.6f;
    public static final float NITRO_VEL = 2.5f;
    public static final float MAX_NITRO_VEL = 5f;

    public static final float MAX_VEL_X = 10f;

    public static final boolean ACCEL_MODE = true;


    private boolean movementMode;

    private static final float DEFLECT_VEL = 3f,DEFLECT_VEL_REDUCTION_RATE = 5f;

    //public float T = (MAX_VEL_Y - NORM_VEL_Y)/ACCELERATION;  //time period required for camera


    //public static final int INDEX_NORM = 0, INDEX_MOUTH_OPEN =  3, INDEX_CHEW = 6, INDEX_MOUTH_CLOSE=9, INDEX_WING = 12;

    public Vector2 vel;

    //public float velToReach;
    //public boolean velChangeable = false;
    //public AnimationCounter animCounterBody;
    //public AnimationCounter animCounterWing;
    public AnimeCounter animeCounterBody;
    public AnimeCounter animeCounterWing;
    boolean inRangeWithFood = false;  // used for closing mouth
    AnimPackage animMouthOpen;
    AnimPackage animChew;
    AnimPackage animNorm;
    AnimPackage animWing;

    volatile boolean isInNitroMode;


    MuscaEventListener muscaEventListener;
    MuscaDeathListener muscaDeathListener;

    boolean orbEnabled;
    float orbAngle;
    boolean orbDisablable;
    float orbAlpha;

    UpdateWork speedUpdateNormal;
    UpdateWork speedUpdateHeadStart;
    UpdateWork speedUpdateWork;

    boolean headStartMode;


    ArrayList<UpdateAndDraw> additionalWorkList;

    UpdateAndDraw protectiveOrbWork;

    boolean deflect;
    float deflectVel;

    public Musca(MuscaEventListener muscaEventListener, float posX, float posY) {
        super(ObjectHandler.INDEX_MUSCA, DRAW_PRIORITY_MED);

        enabled = true;
        collidable = true;

        vel = new Vector2(0, NORM_VEL_Y);


        protectiveOrbWork = createProtectiveOrbWork();

        additionalWorkList = new ArrayList<UpdateAndDraw>(2);

        //animCounterBody = new AnimationCounter();
        //animCounterWing = new AnimationCounter();
        //animCounterWing.setMode(AnimationCounter.MODE_REVERSE_AND_REPEAT, 3, 4, INDEX_WING,0);

        createAnimes();

        this.muscaEventListener = muscaEventListener;

        set(posX, posY);

        createSpeedUpdateWorks();
    }

    private void createSpeedUpdateWorks() {
        speedUpdateNormal = new UpdateWork() {
            @Override
            public void update(float deltaTime) {

                if (Static.session.gameAreaTouched) {
                    if (Static.world.nitro.amount > 0) {
                        increaseSpeedAndStuff(deltaTime);
                    } else if (vel.y > NORM_VEL_Y) {
                        decreaseSpeedAndStuff(deltaTime);
                    }
                } else if (vel.y != NORM_VEL_Y) {
                    decreaseSpeedAndStuff(deltaTime);
                }
            }
        };

        speedUpdateHeadStart = new UpdateWork() {

            int particleAddCounter = 0;

            @Override
            public void update(float deltaTime) {

                boolean stillMoreTravellingIsThere = Static.session.distanceTravelledInSession < HEAD_START_DISTANCE;
                if (stillMoreTravellingIsThere) {
                    if (vel.y < HEAD_START_VEL_Y) {
                        vel.y += ACCELERATION * deltaTime;

                        if (vel.y > HEAD_START_VEL_Y) vel.y = HEAD_START_VEL_Y;

                        if (animeCounterWing.stepSize != 1) // if not already set
                            animeCounterWing.setStepSize(1);

                        if (Static.cam.getInterpolatorId() != Camera.ID_GAP_EXPAND)
                            Static.cam.expandGap(pos.y);
                    }
                } else {
                    vel.y -= ACCELERATION * deltaTime;

                    if (vel.y <= NORM_VEL_Y) {
                        vel.y = NORM_VEL_Y;
                        setSpeedUpdateNormal();
                    }

                    if (animeCounterWing.stepSize != 3) // if not already set
                        animeCounterWing.setStepSize(3);

                    if (Static.cam.getInterpolatorId() != Camera.ID_GAP_COMPRESS)
                        Static.cam.compressGap(pos.y);

                }

                particleAddCounter++;
                if (particleAddCounter >= 2) {
                    particleAddCounter = 0;
                    addHeadStartParticle();
                    if (stillMoreTravellingIsThere) {
                        addHeadStartParticle();
                        addHeadStartParticle();
                        addHeadStartParticle();
                    }
                }

            }

            private void addHeadStartParticle() {
                Particle particle = Static.objHandler.addParticle();
                if (particle != null) {
                    particle.setBasics_Pos(pos.x, pos.y - radius / 2, Static.rand.nextFloat() * 0.24f, 1, Assets.star);
                    float velX = Static.rand.nextFloat() * 3;
                    particle.setVel((Static.rand.nextBoolean() ? velX : -velX), -0.2f);
                    float angularVel = Static.rand.nextFloat() * 400;
                    particle.setAngularVel(0, (Static.rand.nextBoolean()) ? angularVel : -angularVel);

                    particle.setAlphaVaryable(0.5f, 3);
                }
            }
        };
    }

    public void setMuscaDeathListener(MuscaDeathListener muscaDeathListener) {
        this.muscaDeathListener = muscaDeathListener;
    }

    private void createAnimes() {
        animMouthOpen = new AnimPackage(Assets.musca_mouth_arr, AnimeCounter.ANIME_ONCE, 1, 3);

        animChew = new AnimPackage(Assets.musca_chew_arr, AnimeCounter.ANIME_ONCE, 1, 3);

        animNorm = new AnimPackage(Assets.musca_norm_arr, AnimeCounter.ANIME_REVERSE, -1, 3);

        animeCounterBody = new AnimeCounter();
        animeCounterBody.setAnime(animNorm);


        animWing = new AnimPackage(Assets.musca_wing_arr, AnimeCounter.ANIME_ONCE, -1, 3);

        animeCounterWing = new AnimeCounter();
        animeCounterWing.setAnime(animWing);
    }


    private UpdateAndDraw createProtectiveOrbWork() {
        UpdateAndDraw work = new UpdateAndDraw() {
            @Override
            public void update(float deltaTime) {
                if (orbDisablable) {
                    orbAlpha -= 2 * deltaTime;

                    if (orbAlpha <= 0f) {
                        disableOrb(this);
                    }
                }

                orbAngle += ProtectiveOrb.ANGULAR_VELOCITY * deltaTime;
                if (orbAngle > 360) orbAngle -= 360;

            }

            @Override
            public void draw() {
                SpriteBatcher batcher = WorldRenderer.batcher;
                TextureShaderProgram texShaderProgram = WorldRenderer.texShaderProgram;

                //careful measures for changing alpha
                if (orbAlpha < 1) {
                    batcher.endBatch(texShaderProgram);
                    texShaderProgram.setAlpha(orbAlpha);
                    batcher.beginBatch();
                    batcher.drawCarefulSprite(pos.x, pos.y, ProtectiveOrb.RADIUS * 2, ProtectiveOrb.RADIUS * 2, orbAngle, Assets.protective_orb, texShaderProgram);
                    batcher.endBatch(texShaderProgram);
                    texShaderProgram.setAlpha(1);
                    batcher.beginBatch();
                } else {
                    batcher.drawCarefulSprite(pos.x, pos.y, ProtectiveOrb.RADIUS * 2, ProtectiveOrb.RADIUS * 2, orbAngle, Assets.protective_orb, texShaderProgram);
                }

            }
        };

        return work;
    }

    private void disableOrb(UpdateAndDraw workToRemove) {
        orbEnabled = false;
        orbDisablable = false;

        additionalWorkList.remove(workToRemove);
    }

    private void setOrbDisablableAndDeflect(Vector2 collidedObjectPos) {
        orbDisablable = true;

        //deflect code
        deflect = true;

        if (collidedObjectPos.x > pos.x) {
            deflectVel = -DEFLECT_VEL;
        } else {
            deflectVel = DEFLECT_VEL;
        }
    }

    @Override
    public void set(float posX, float posY) {
        super.set(posX, posY);
        this.radius = RADIUS;
    }


    public void resetAnimCounterToNormal() {
        //animCounterBody.setMode(AnimationCounter.MODE_REPEAT, 3, 5, INDEX_NORM,0);
        animeCounterBody.setAnime(animNorm);
        animeCounterWing.setStepSize(3);
    }


    @Override
    public void update(float deltaTime) {
        if (state == STATE_ALIVE_AND_KICKING) {

            speedUpdateWork.update(deltaTime);

            final float delY = deltaTime * vel.y;

            muscaEventListener.onMuscaDeltaY(delY);


            pos.y += delY;

            if (ACCEL_MODE) {
                vel.x = -Static.game.getInput().getAccelX();

            } else {
                if (Static.session.keyPressHandler.leftPressed)
                    vel.x = -5;
                else if (Static.session.keyPressHandler.rightPressed)
                    vel.x = 5;
                else
                    vel.x = 0;
            }

            if (vel.x > MAX_VEL_X) {
                vel.x = MAX_VEL_X;
            } else if (vel.x < -MAX_VEL_X) {
                vel.x = -MAX_VEL_X;
            }

            if(deflect)
            {
                vel.x += deflectVel;

                if(deflectVel<0)
                {
                    deflectVel += DEFLECT_VEL_REDUCTION_RATE * deltaTime;
                    if(deflectVel >= 0){
                        deflectVel = 0;
                        deflect = false;
                    }
                }
                else {
                    deflectVel -= DEFLECT_VEL_REDUCTION_RATE * deltaTime;
                    if(deflectVel <= 0)
                    {
                        deflectVel = 0;
                        deflect = false;
                    }
                }
            }

            pos.x += deltaTime * vel.x;


            teleportify(radius, true);


            animeCounterBody.udpate();
            animeCounterWing.udpate();


            //if((animCounterBody.startIndex == INDEX_CHEW  && animCounterBody.countComplete) || (animCounterBody.startIndex == INDEX_MOUTH_CLOSE && animCounterBody.countComplete))
            if ((animeCounterBody.animePack == animChew && animeCounterBody.animeComplete)) {
                resetAnimCounterToNormal();
            }

            //else if(!inRangeWithFood && animCounterBody.startIndex == INDEX_MOUTH_OPEN && animCounterBody.countComplete)
            else if (!inRangeWithFood && animeCounterBody.animePack == animMouthOpen && animeCounterBody.animeComplete) {
                //animCounterBody.setMode(AnimationCounter.MODE_ONCE, 3, 4, INDEX_MOUTH_CLOSE,0);
                animeCounterBody.setAnime(animNorm);
            }

            inRangeWithFood = false;


            for (int i = 0; i < additionalWorkList.size(); i++) {
                UpdateAndDraw work = additionalWorkList.get(i);
                work.update(deltaTime);
            }
        } else if (state == STATE_DEATH_PHASE) {

            disable();

            muscaDeathListener.onMuscaDeath();

            return;

        }

    }

    private void increaseSpeedAndStuff(float deltaTime) {
        vel.y += ACCELERATION * deltaTime;

        if (vel.y > MAX_NITRO_VEL) vel.y = MAX_NITRO_VEL;

        if (vel.y > NITRO_VEL) isInNitroMode = true;

        Static.world.nitro.decreaseFraction(deltaTime);

        //animCounterWing.setMode(AnimationCounter.MODE_REVERSE_AND_REPEAT, 3, 1, INDEX_WING,0);
        if (animeCounterWing.stepSize != 1) // if not already setPosAndScale
            animeCounterWing.setStepSize(1);

        if (Static.cam.getInterpolatorId() != Camera.ID_GAP_EXPAND)
            Static.cam.expandGap(pos.y);


    }

    private void decreaseSpeedAndStuff(float deltaTime) {

        vel.y -= ACCELERATION * deltaTime;

        if (vel.y < NORM_VEL_Y) vel.y = NORM_VEL_Y;

        else if (vel.y < NITRO_VEL) isInNitroMode = false;

        //animCounterWing.setMode(AnimationCounter.MODE_REVERSE_AND_REPEAT, 3, 4, INDEX_WING,0);
        if (animeCounterWing.stepSize != 3) // if not already setPosAndScale
            animeCounterWing.setStepSize(3);

        if (Static.cam.getInterpolatorId() != Camera.ID_GAP_COMPRESS)
            Static.cam.compressGap(pos.y);
    }

    public void clean(float posX, float posY) {
        vel.y = NORM_VEL_Y;
        isInNitroMode = false;
        set(posX, posY);
        disableOrb(protectiveOrbWork);
        resetAnimCounterToNormal();
    }


    public void interactWithDumbEnemy(Dumboeba dumbEnemy) {

        if (standard_CC_CollsionCheck(this, dumbEnemy)) {
            if (isInNitroMode || headStartMode) {
                dumbEnemy.deathToYou(1+Static.rand.nextInt(2));
            } else if (orbEnabled) {
                setOrbDisablableAndDeflect(dumbEnemy.pos);
                dumbEnemy.deathToYou(1+Static.rand.nextInt(2));
            } else {
                deathToYou();
            }
        }


    }

    public void interactWithBigBaddy(BigBaddy bigBaddy) {

        if (standard_CC_CollsionCheck(this, bigBaddy)) {

            if (isInNitroMode || headStartMode) {
                bigBaddy.deflect(pos);

            } else if (orbEnabled) {
                setOrbDisablableAndDeflect(bigBaddy.pos);
                bigBaddy.deflect(pos);
            } else {
                deathToYou();
            }
        }

    }

    public void interactWithBullet(Bullet bullet) {
        if (standard_CC_CollsionCheck(this, bullet)) {
            if (!headStartMode) {
                if (orbEnabled) {
                    orbDisablable = true;
                    //setOrbDisablableAndDeflect(bullet.pos); not calling this for bullet
                } else {
                    deathToYou();
                }
            }

            bullet.deathToYou();
        }

    }

    public void interactWithShooter(Shooter shooter) {


        if (standard_CC_CollsionCheck(this, shooter)) {
            shooter.deathToYou(1+Static.rand.nextInt(2));
        }

    }

    public void interactWithFood(Food food) {
        // if within range
        if (food.collidable && food.pos.y > pos.y && checkCircleCircleCollision(pos, Food.RANGE, food.pos, 0)) {
            //animCounterBody.setMode(AnimationCounter.MODE_ONCE, 3, 4, INDEX_MOUTH_OPEN,0);
            animeCounterBody.setAnime(animMouthOpen);
            inRangeWithFood = true;

            if (checkCircleCircleCollision(pos, radius, food.pos, food.radius)) {
                //food.disableOverlayDrawable = true;

                food.eaten(this);

                animeCounterBody.setAnime(animChew);

                Static.world.nitro.addMorsel();
            }
        }

    }

    public void interactWithCoin(Coin coin) {
        if (standard_CC_CollsionCheck(this, coin)) {
            muscaEventListener.onCoinCollect();
            coin.deathToYou();
        }
    }

    public void interactWithWallBlock(WallBlock block) {
        if (standard_CR_CollisionCheck(this, block)) {
            if (isInNitroMode || headStartMode) {
                block.deathToYou(1+Static.rand.nextInt(2));
            } else if (orbEnabled) {
                setOrbDisablableAndDeflect(block.pos);
            } else {
                deathToYou();
            }
        }
    }

    public void interactWithOrb(ProtectiveOrb orb) {
        if (standard_CC_CollsionCheck(this, orb)) {
            orb.acquireMusca(this);
        }

    }

    public void interactWithGem(Gem gem) {

        if (standard_CC_CollsionCheck(this, gem)) {
            muscaEventListener.onGemCollect();
            gem.deathToYou();
        }
    }

    public void interactWithBomb(Bomb bomb) {
        {
            if (standard_CC_CollsionCheck(this, bomb)) {
                if (headStartMode) {

                } else if (orbEnabled) {
                    setOrbDisablableAndDeflect(bomb.pos);
                } else {
                    deathToYou();
                }

                bomb.explode();
            }
        }
    }

    @Override
    public void draw() {
        SpriteBatcher batcher = WorldRenderer.batcher;
        TextureShaderProgram texShaderProgram = WorldRenderer.texShaderProgram;

        TextureRegion muscaRegion = animeCounterBody.arr[animeCounterBody.currentIndex];//Assets.muscaRegions[animCounterBody.index];
        batcher.drawCarefulSprite(pos.x, pos.y, muscaRegion.drawWidth, muscaRegion.drawHeight, muscaRegion, texShaderProgram);

        TextureRegion wingRegion = animeCounterWing.getTextureRegion();//animeCounterWing.arr[animeCounterWing.currentIndex];

        batcher.drawCarefulSprite(pos.x, pos.y, wingRegion.drawWidth, wingRegion.drawHeight, wingRegion, texShaderProgram);

        for (UpdateAndDraw work : additionalWorkList) {
            work.draw();
        }
    }

    public void setSpeedUpdateHeadStart() {


        headStartMode = true;
        speedUpdateWork = speedUpdateHeadStart;

    }

    public void setSpeedUpdateNormal() {
        headStartMode = false;
        speedUpdateWork = speedUpdateNormal;
    }

    public void enableOrb(float angle) {

        if (!orbEnabled) {
            orbEnabled = true;
            orbAngle = angle;
            additionalWorkList.add(protectiveOrbWork);
            orbAlpha = 1;
        }
    }

    @Override
    public void disable() {
        super.disable();

        additionalWorkList.clear();

        orbEnabled = false;
        orbDisablable = false;
    }

    @Override
    public float getBottomPos() {
        return pos.y - radius;
    }

    @Override
    public float getTopPos() {
        return pos.y + radius;
    }


    public interface MuscaEventListener {
        //public void onCrunch(float posX, float posY);

        public void onCoinCollect();

        public void onMuscaDeltaY(float deltaY);

        public void onGemCollect();


    }

    public void changeXbasedOnTouch(float deltaTime)
    {

    }

    public void changeXbasedOnAccel(float deltaTime)
    {

    }


    public interface MuscaDeathListener {
        public void onMuscaDeath();

    }


}

package com.antibot.poop.gameobj;

import com.antibot.poop.AnimeCounter;
import com.antibot.poop.Assets;
import com.antibot.poop.ObjectHandler;
import com.antibot.poop.Static;
import com.antibot.poop.WorldRenderer;
import com.framework.shaderPrograms.TextureShaderProgram;
import com.game.framework.gl.SpriteBatcher;
import com.game.framework.gl.TextureRegion;
import com.game.math.Vector2;
import com.antibot.poop.AnimeCounter.AnimPackage;
public class Bomb extends CircularCollidableObject {

    public static final float RADIUS = 0.18f, EXPLOSION_VEL = 3, INITIAL_EXPLOSION_ALPHA = 1f, FINAL_EXPLOSION_ALPHA = 0.05f;

    public Vector2 vel;

    float angularVel;
    float angle;

    public static final float IMAGE_WIDTH = 53, IMAGE_HEIGHT = 46;
    public static final float CENTER_X = 21, CENTER_Y = 19;
    public static final float SPARK_X = 53, SPARK_Y = 29;

    public static final float PIVOT_X = CENTER_X/IMAGE_WIDTH, PIVOT_Y = CENTER_Y/IMAGE_HEIGHT; //change this whenever needed

    public static final float SPARK_VECTOR_X = (SPARK_X-CENTER_X)/100, SPARK_VECTOR_Y = (SPARK_Y - CENTER_Y)/100; // vector for spark point from center pivot


    static AnimPackage animPackage;

    int sparkGapCount;

    AnimeCounter animeCounter;

    float explosionAlpha;



    public Bomb()
    {
        super(ObjectHandler.INDEX_BOMB, DRAW_PRIORITY_LOW);

        vel = new Vector2();

        animeCounter = new AnimeCounter();

        if(animPackage == null) {
            animPackage = new AnimPackage(Assets.bomb_exp_arr, AnimeCounter.ANIME_ONCE, 1, 2);
        }

        animeCounter.setAnime(animPackage);

    }



    public void set(float posX, float posY)
    {
        super.set(posX, posY);
        radius = RADIUS;

        angularVel = 30 + Static.rand.nextFloat()*50;

        if(Static.rand.nextBoolean()) angularVel = -angularVel;


        angle = 0;

    }

    @Override
    public float getBottomPos(){return pos.y - radius;}


    @Override
    public float getTopPos(){return pos.y + radius;}


    @Override
    public void update(float deltaTime) {

        angle += angularVel*deltaTime;
        if(angle > 360) angle -= 360;
        else if(angle < 0) angle += 360;

        sparkGapCount++;


        if(state == STATE_ALIVE_AND_KICKING)
        {
            if(sparkGapCount > 2) {
                generateSpark();
                sparkGapCount = 0;
            }
        }
        else if(state == STATE_DEATH_PHASE)
        {
            radius += EXPLOSION_VEL * deltaTime;

            explosionAlpha -= EXPLOSION_VEL * deltaTime;

            if(explosionAlpha <= 0 )
            {
                disable();
            }


            animeCounter.udpate();

        }
    }

    private void generateSpark()
    {
        if(Static.rand.nextBoolean())
        {

            Vector2 tempVector = Static.tempVector;

            tempVector.set(SPARK_VECTOR_X, SPARK_VECTOR_Y);

            tempVector.rotate(angle);

            addSparkParticle(tempVector.x+pos.x, tempVector.y + pos.y);

        }
    }

    private void addSparkParticle(float posX, float posY)
    {
        Particle p = Static.objHandler.addParticle();
        if(p != null) {
            p.setBasics_Pos(posX, posY, 0.05f + Static.rand.nextFloat() * 0.06f, 1, Assets.bomb_spark_arr[Static.rand.nextInt(Assets.bomb_spark_arr.length)]);
            p.setVelRandomized(4);
        }
    }

    @Override
    public void draw() {

        SpriteBatcher batcher = WorldRenderer.batcher;
        TextureShaderProgram texShaderProgram = WorldRenderer.texShaderProgram;

        if(state == STATE_ALIVE_AND_KICKING) {

            TextureRegion bombRegion = Assets.bomb;

            batcher.drawCarefulSpriteWithPivot(pos.x, pos.y, bombRegion.drawWidth, bombRegion.drawHeight, PIVOT_X, PIVOT_Y, angle, bombRegion, texShaderProgram);
        }
        else if(state == STATE_DEATH_PHASE)
        {
            TextureRegion explosionRegion = Assets.explosion;

            batcher.prepareForDrawingAlpha(explosionAlpha,texShaderProgram);
            batcher.drawCarefulSprite(pos.x, pos.y, radius*2, radius*2, angle, explosionRegion,texShaderProgram);
            batcher.finalizeDrawingAlpha(texShaderProgram);

            TextureRegion bombBlasRegion = animeCounter.getTextureRegion();

            batcher.drawCarefulSprite(pos.x, pos.y, bombBlasRegion.drawWidth, bombBlasRegion.drawHeight, angle, bombBlasRegion, texShaderProgram);

        }

    }

    /*
    will set state to death phase and will start increasing radius
    will be collidable during explosion phase
    will have no effect if already in death phase
     */
    public void explode()
    {
        if(state != STATE_DEATH_PHASE) {
            state = STATE_DEATH_PHASE;
            animeCounter.resetAnime();
            resetExplosion();
        }
    }


    private void resetExplosion()
    {
        explosionAlpha = INITIAL_EXPLOSION_ALPHA;
    }

    public void interactWithBomb(Bomb bomb) {

        if(state == STATE_DEATH_PHASE && bomb.state == STATE_DEATH_PHASE)  //optimisation
        {
            return;
        }

        if (CollidableObject.checkCircleCircleCollision(pos, radius, bomb.pos, bomb.radius)) {
                explode();
                bomb.explode();
            }

    }

    public void interactWithWallBlock(WallBlock wallblock) {
        if(standard_CR_CollisionCheck(this,wallblock))
        {
            explode();
            wallblock.deathToYou(1+Static.rand.nextInt(2));
        }
    }
}

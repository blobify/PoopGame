package com.antibot.poop;

import com.antibot.poop.gameobj.BigBaddy;
import com.antibot.poop.gameobj.Bomb;
import com.antibot.poop.gameobj.Bullet;
import com.antibot.poop.gameobj.Coin;
import com.antibot.poop.gameobj.CollidableObject;
import com.antibot.poop.gameobj.Dumboeba;
import com.antibot.poop.gameobj.DumboebaGroup;
import com.antibot.poop.gameobj.Food;
import com.antibot.poop.gameobj.Gem;
import com.antibot.poop.gameobj.Musca;
import com.antibot.poop.gameobj.Musca.MuscaEventListener;
import com.antibot.poop.gameobj.Particle;
import com.antibot.poop.gameobj.PathPoint;
import com.antibot.poop.gameobj.ProtectiveOrb;
import com.antibot.poop.gameobj.Shooter;
import com.antibot.poop.gameobj.Shooter.ShooterEventListener;
import com.antibot.poop.gameobj.WallBlock;
import com.antibot.poop.interactions.BigBaddyBomb;
import com.antibot.poop.interactions.BigBaddyBullet;
import com.antibot.poop.interactions.BigBaddyDumbo;
import com.antibot.poop.interactions.BigBaddyFood;
import com.antibot.poop.interactions.BigBaddyShooter;
import com.antibot.poop.interactions.BigBaddyWallBlock;
import com.antibot.poop.interactions.BombBomb;
import com.antibot.poop.interactions.BombWallBlock;
import com.antibot.poop.interactions.BulletBomb;
import com.antibot.poop.interactions.BulletBullet;
import com.antibot.poop.interactions.BulletWallBlock;
import com.antibot.poop.interactions.DumboBomb;
import com.antibot.poop.interactions.DumboBullet;

import com.antibot.poop.interactions.DumboShooter;
import com.antibot.poop.interactions.DumboWallBlock;
import com.antibot.poop.interactions.FoodBomb;
import com.antibot.poop.interactions.FoodBullet;
import com.antibot.poop.interactions.InteractWork;
import com.antibot.poop.interactions.MuscaBigBaddy;
import com.antibot.poop.interactions.MuscaBomb;
import com.antibot.poop.interactions.MuscaBullet;
import com.antibot.poop.interactions.MuscaCoin;
import com.antibot.poop.interactions.MuscaDumbo;
import com.antibot.poop.interactions.MuscaFood;
import com.antibot.poop.interactions.MuscaGem;
import com.antibot.poop.interactions.MuscaOrb;
import com.antibot.poop.interactions.MuscaShooter;
import com.antibot.poop.interactions.MuscaWall;
import com.antibot.poop.interactions.ShooterBomb;
import com.antibot.poop.interactions.ShooterBullet;
import com.framework.utils.ArrayIntList;
import com.framework.utils.Logger;
import com.framework.utils.MyArrayList;

import java.util.ArrayList;
import java.util.Iterator;


public class ObjectHandler
{
	public static final int NUMBER_OF_OBJECTS = 11;  // or columns
	
	public static final int INDEX_MUSCA = 0,INDEX_FOOD = 1, INDEX_DUMB_ENEMY=2, INDEX_BIG_BADDY = 3,INDEX_SHOOTER = 4, INDEX_BULLET = 5, INDEX_WALL_BLOCK = 6, INDEX_COIN = 7, INDEX_PROTECTIVE_ORB = 8, INDEX_BOMB = 9, INDEX_GEM = 10;
	public static final int NUMBER_OF_MUSCA = 1, NUMBER_OF_FOOD = 40, NUMBER_OF_DUMB_ENEMIES=32, NUMBER_OF_BIG_BADDIES = 30, NUMBER_OF_SHOOTERS = 30, NUMBER_OF_BULLETS = 70, NUMBER_OF_WALL_BLOCKS = 100,  NUMBER_OF_COINS = 50, NUMBER_OF_PROTECTIVE_ORBS = 1, NUMBER_OF_BOMBS = 60, NUMBER_OF_GEMS = 3;
	
	public static final int NUMBER_OF_PARTICLES = 50;
    public static final int NUMBER_OF_DUMBO_GROUPS = 8;
	

	ArrayIntList[] drawPriority;
	
	public final InteractWork interactionTable[][];

	//public static final float GRID_HEIGHT = 4.0f;
	//public static final int NUMBER_OF_GRIDS = 4;

    public static final float GRID_HEIGHT = Static.TARGET_HEIGHT/4;
    public static final int NUMBER_OF_GRIDS = 5;
    //public static final float MIN_POS = -GRID_HEIGHT;


    public ArrayIntList grid[];
	
	MyArrayList<Particle> particlePool;	
	MyArrayList<Particle> activeParticleList;		
	

	MyArrayList<CollidableObject>[] objPool;
	MyArrayList<CollidableObject> activeCollidableObjectList;
	MyArrayList<CollidableObject> activeCollidableObjectAddableList;
	
	MyArrayList<DumboebaGroup> dumboebaGroupPool;	
	MyArrayList<DumboebaGroup> activeDumboebaGroup;
	
	MyArrayList<PathPoint> pointListPool;

    MyArrayList<CollidableObject> waitingList;
	
	public ObjectHandler()
	{
		activeCollidableObjectList = new MyArrayList<CollidableObject>();		
		activeCollidableObjectAddableList = new MyArrayList<CollidableObject>(20);
		
		
		waitingList = new MyArrayList<CollidableObject>(80);
		
		createObjectArray();
		
		createGrid();
		
		createPointListPool();
		
		createDrawPriorityLists();
		
		
		interactionTable = new InteractWork[NUMBER_OF_OBJECTS][NUMBER_OF_OBJECTS];	
		
		createInteractionTable();
		
	}
	
	private void createInteractionTable()
	{
		setInteractionWork(INDEX_MUSCA,INDEX_FOOD, new MuscaFood());
        setInteractionWork(INDEX_MUSCA,INDEX_BIG_BADDY, new MuscaBigBaddy());
		setInteractionWork(INDEX_MUSCA,INDEX_DUMB_ENEMY, new MuscaDumbo());
		setInteractionWork(INDEX_MUSCA,INDEX_BULLET, new MuscaBullet());
        setInteractionWork(INDEX_MUSCA,INDEX_WALL_BLOCK, new MuscaWall());
        setInteractionWork(INDEX_MUSCA,INDEX_SHOOTER, new MuscaShooter());
		
		setInteractionWork(INDEX_BIG_BADDY,INDEX_FOOD, new BigBaddyFood());
        setInteractionWork(INDEX_BIG_BADDY,INDEX_BULLET,new BigBaddyBullet());
        setInteractionWork(INDEX_BIG_BADDY,INDEX_BOMB, new BigBaddyBomb());
        setInteractionWork(INDEX_BIG_BADDY,INDEX_WALL_BLOCK,new BigBaddyWallBlock());
        setInteractionWork(INDEX_BIG_BADDY,INDEX_SHOOTER,new BigBaddyShooter());


        setInteractionWork(INDEX_DUMB_ENEMY,INDEX_BIG_BADDY,new BigBaddyDumbo());
        setInteractionWork(INDEX_DUMB_ENEMY,INDEX_SHOOTER,new DumboShooter());
        setInteractionWork(INDEX_DUMB_ENEMY,INDEX_BULLET,new DumboBullet());
		setInteractionWork(INDEX_DUMB_ENEMY,INDEX_BOMB, new DumboBomb());
        setInteractionWork(INDEX_DUMB_ENEMY,INDEX_WALL_BLOCK, new DumboWallBlock());

        setInteractionWork(INDEX_SHOOTER,INDEX_BULLET, new ShooterBullet());
        setInteractionWork(INDEX_SHOOTER, INDEX_BOMB, new ShooterBomb());

        setInteractionWork(INDEX_BULLET,INDEX_BULLET,new BulletBullet());
        setInteractionWork(INDEX_BULLET,INDEX_WALL_BLOCK,new BulletWallBlock());
        setInteractionWork(INDEX_BULLET, INDEX_BOMB, new BulletBomb());

        setInteractionWork(INDEX_MUSCA,INDEX_COIN, new MuscaCoin());
        setInteractionWork(INDEX_MUSCA,INDEX_PROTECTIVE_ORB, new MuscaOrb());
		setInteractionWork(INDEX_MUSCA,INDEX_GEM, new MuscaGem());
        setInteractionWork(INDEX_MUSCA,INDEX_BOMB, new MuscaBomb());


        setInteractionWork(INDEX_FOOD,INDEX_BULLET, new FoodBullet());
        setInteractionWork(INDEX_FOOD,INDEX_BOMB, new FoodBomb());

        setInteractionWork(INDEX_BOMB, INDEX_WALL_BLOCK, new BombWallBlock());
        setInteractionWork(INDEX_BOMB, INDEX_BOMB,new BombBomb());


	}

    private void setInteractionWork(int i, int j, InteractWork work)
    {
        interactionTable[i][j] = interactionTable[j][i] = work;
    }
	
	
	private void createObjectArray()
	{
		objPool = new MyArrayList[NUMBER_OF_OBJECTS];  // deal with it
				
		createMuscaArray();
		createFoodArray();		
		createEnemyArrays();
		createBulletArray();
		createWallBlockArray();
		createCoinArray();
		createProtectiveOrb();
        createBombArray();
        createGemArray();

		createParticleArray();	
		
		createDumboebaGroupPool();
	}
	
	private void createGrid()
	{
		grid = new ArrayIntList[NUMBER_OF_GRIDS];
		for(int i=0; i<grid.length; i++)
		{
			grid[i] = new ArrayIntList(30);  // 30 elements per grid should be enough
		}

	}
	
	private void createPointListPool()
	{
		pointListPool = new MyArrayList<PathPoint>();
		
		for(int i=0; i<700; i++)
		{
			PathPoint p = new PathPoint();
			pointListPool.add(p);
		}
	}
	
	private void createDrawPriorityLists()
	{
		//collidableObjectLowPriorityDrawList = new ArrayList<CollidableCircularObject>();
		//collidableObjectMedPriorityDrawList = new ArrayList<CollidableCircularObject>();
		//collidableObjectHighPriorityDrawList = new ArrayList<CollidableCircularObject>();
		
		drawPriority = new ArrayIntList[3];
		
		for(int i=0; i<3; i++)
		{
			drawPriority[i] = new ArrayIntList(37);
		}
	}
	
	private void createBulletArray()
	{
		objPool[INDEX_BULLET] = new MyArrayList<CollidableObject>();//CollidableCircularObject[NUMBER_OF_BULLETS];
		
		for(int i=0; i<NUMBER_OF_BULLETS; i++)
		{
			objPool[INDEX_BULLET].add(new Bullet() );
		}
	}
	

	
	private void createEnemyArrays()
	{
		objPool[INDEX_DUMB_ENEMY] = new MyArrayList<CollidableObject>();
		
	
		for(int i=0; i<NUMBER_OF_DUMB_ENEMIES; i++)
		{
			objPool[INDEX_DUMB_ENEMY].add(new Dumboeba());
		}
		
		
		objPool[INDEX_BIG_BADDY] = new MyArrayList<CollidableObject>();
			

		for(int i=0; i<NUMBER_OF_BIG_BADDIES; i++)
		{			
			objPool[INDEX_BIG_BADDY].add(new BigBaddy());
		}
		
		
		ShooterEventListener shootEventListener = new ShooterEventListener()
		{			
			@Override
			public void onShoot(float shooterPosX, float shooterPosY, float velX, float velY)
			{
				addBullet(shooterPosX,shooterPosY,velX,velY);			
			}

			
		};
		
		objPool[INDEX_SHOOTER] = new MyArrayList<CollidableObject>();
		for(int i=0; i<NUMBER_OF_SHOOTERS; i++)
		{
			objPool[INDEX_SHOOTER].add(new Shooter(shootEventListener));
		}
	}
	
	private void createWallBlockArray()
	{		
		objPool[INDEX_WALL_BLOCK] = new MyArrayList<CollidableObject>();
			
		for(int i=0; i<NUMBER_OF_WALL_BLOCKS; i++)
		{
			objPool[INDEX_WALL_BLOCK].add(new WallBlock());
		}
	}
	
	private void createCoinArray()
	{		
		objPool[INDEX_COIN] = new MyArrayList<CollidableObject>();
			
		for(int i=0; i<NUMBER_OF_COINS; i++)
		{
			objPool[INDEX_COIN].add(new Coin());
		}
	}
	
	private void createProtectiveOrb()
	{
		objPool[INDEX_PROTECTIVE_ORB] = new MyArrayList<CollidableObject>();

		for(int i=0; i<NUMBER_OF_PROTECTIVE_ORBS; i++)
		{
			objPool[INDEX_PROTECTIVE_ORB].add(new ProtectiveOrb());
		}
	}

    private void createBombArray()
    {
        objPool[INDEX_BOMB] = new MyArrayList<CollidableObject>();
        for(int i=0; i<NUMBER_OF_BOMBS; i++)
        {
            objPool[INDEX_BOMB].add(new Bomb());
        }
    }

    private void createGemArray()
    {
        objPool[INDEX_GEM] = new MyArrayList<CollidableObject>();
        for(int i=0; i<NUMBER_OF_GEMS; i++)
        {
            objPool[INDEX_GEM].add(new Gem());
        }
    }

	private void createMuscaArray()
	{
		objPool[INDEX_MUSCA] = new MyArrayList<CollidableObject>();
		
		MuscaEventListener muscaEventListener = new MuscaEventListener()
		{
            @Override
			public void onCoinCollect()
			{
				Assets.coin.play(1);
				Static.session.coinsCollectedInSession++;
			}

            @Override
            public void onMuscaDeltaY(float deltaY) {
                Static.session.addDistance(deltaY);
                Static.levelGenerator.deltaY(deltaY);
            }

            @Override
            public void onGemCollect()
            {
                Assets.coin.play(1);
                Static.session.gemsCollectedInSession++;
            }

        };
		
		Static.musca =  new Musca(muscaEventListener,Static.TARGET_WIDTH/2,0);
		objPool[INDEX_MUSCA].add(Static.musca);
		
	}
	
	public void enableMusca()
	{
		CollidableObject musca = (CollidableObject) objPool[INDEX_MUSCA].removeLast();
		musca.enable();
		activeCollidableObjectList.add(musca);
	}
	
	private void createFoodArray()
	{
		objPool[INDEX_FOOD] = new MyArrayList<CollidableObject>();
		
		for(int i=0; i<NUMBER_OF_FOOD; i++)
		{
			objPool[INDEX_FOOD].add(new Food());
		}
	}
	
	private void createParticleArray()
	{		
		particlePool = new MyArrayList<Particle>();	
		
		for(int i=0; i < NUMBER_OF_PARTICLES; i++)
		{
			particlePool.add(new Particle());
		}
		activeParticleList = new MyArrayList<Particle>();
	}
	
	private void createDumboebaGroupPool()
	{
		dumboebaGroupPool = new MyArrayList<DumboebaGroup>();
		
		for(int i=0; i<NUMBER_OF_DUMBO_GROUPS; i++)
		{
			dumboebaGroupPool.add(new DumboebaGroup());
		}
		
		activeDumboebaGroup = new MyArrayList<DumboebaGroup>();
	}
	
	
	public void addFoodToWaitingList(float posX, float posY)
	{
		
		if(objPool[INDEX_FOOD].size() > 0)
		{
			ArrayList<CollidableObject> foodPool = objPool[INDEX_FOOD];
			Food food = (Food) foodPool.remove(foodPool.size()-1);
			
			food.set(posX, posY, Static.rand.nextInt(2));
			//activeCollidableObjectList.add(food);
            waitingList.add(food);
		}
		
		
	}


	
	public void addCoins(float posX, float posY, int number)
	{		
		for(int i=0; i<number; i++)
		{
			if(objPool[INDEX_COIN].size() > 0)
			{
				Coin coin = (Coin) objPool[INDEX_COIN].removeLast();

				coin.setWithVelocity(posX, posY);

				activeCollidableObjectAddableList.add(coin);

			}
			else
			{
				return;
			}
		}
	}
	
	public void addOrbToWaitingList(float posX, float posY)
	{
        if(objPool[INDEX_PROTECTIVE_ORB].size() > 0)
		{
			ProtectiveOrb orb = (ProtectiveOrb)objPool[INDEX_PROTECTIVE_ORB].removeLast();
			
			orb.set(posX, posY);
			

            waitingList.add(orb);
		}
	}
	
	public void addCoinToWaitingList(float posX, float posY)
	{
		if(objPool[INDEX_COIN].size() > 0)
		{
			Coin coin = (Coin) objPool[INDEX_COIN].removeLast();
			
			coin.set(posX, posY);
			
			//activeCollidableObjectList.add(coin);
            waitingList.add(coin);
			
		}
	}

	
	public void addDumboGroup(int numberOfNodes)
    {
        if(dumboebaGroupPool.size() > 0)
        {
            DumboebaGroup group = dumboebaGroupPool.removeLast();

            ArrayList<DumboebaGroup.Node> tempNodeList = DumboebaGroup.tempNodeList;

            for(int i=0; i<numberOfNodes;i++)
            {
                DumboebaGroup.Node node = tempNodeList.get(i);

                group.pathData[i*2] = node.posX;
                group.pathData[i*2 + 1] = node.posY;

               // Logger.print("i #" + i + " at x " + group.pathData[i*2] + " y " + group.pathData[i*2+1]);
                if(node.hasDumbo)
                {
                    if(objPool[INDEX_DUMB_ENEMY].size() > 0)
                    {
                        Dumboeba dumboeba = (Dumboeba) objPool[INDEX_DUMB_ENEMY].removeLast();

                        activeCollidableObjectList.add(dumboeba);

                        group.addDumboeba(dumboeba, i*2);

                    }
                }
            }

            if(numberOfNodes < DumboebaGroup.MAX_NODES_PER_GROUP) {

                group.setLastPathPointIndex(numberOfNodes*2);
            }

            group.init();

            group.setDirDataAndPathPoints(pointListPool);

            activeDumboebaGroup.add(group);
        }

    }
	
	public void addDumboGroup(float posX, float posY, int numberOfNodes)
	{
		if(dumboebaGroupPool.size() > 0)
		{
			DumboebaGroup group = dumboebaGroupPool.removeLast();
					
			int count = 1;	// + Static.randomAccessFile.nextInt(2);  // 1 or 2 dumboebas randomly
			for(int i=0; i<count; i++)
			{
				if(objPool[INDEX_DUMB_ENEMY].size() > 0)
				{
					Dumboeba dumboeba = (Dumboeba) objPool[INDEX_DUMB_ENEMY].removeLast();
										
					activeCollidableObjectList.add(dumboeba);
					
					group.dumboList.add(dumboeba);
					
				}
			}


			
			
			group.setPathAndDir(posX, posY,numberOfNodes);
			group.setPathPoints(pointListPool);
			
			group.setDumbosUp();
			
			activeDumboebaGroup.add(group);
			
		}
	}
	
	public void addBigBaddyToWaitingList(float posX, float posY, byte sizeCode)
	{
		
		if(objPool[INDEX_BIG_BADDY].size() > 0)
		{
			BigBaddy bigBaddy = (BigBaddy) objPool[INDEX_BIG_BADDY].removeLast();


			bigBaddy.set(posX, posY,sizeCode);
			
			//activeCollidableObjectList.add(bigBaddy);
            waitingList.add(bigBaddy);
		}
	}
	
	public void addShooterToWaitingList(float posX, float posY)
	{
		/*CollidableCircularObject[] shooterArr = objPool[INDEX_SHOOTER];
		for(CollidableCircularObject object : shooterArr)
		{
			if(!object.enabled)
			{
				Shooter shooter = (Shooter) object;
				shooter.setPosAndScale(posX, posY);
				
				activeCollidableObjectList.add(shooter);
				return;
			}
		}*/
		
		if(objPool[INDEX_SHOOTER].size() > 0)
		{
			Shooter shooter = (Shooter) objPool[INDEX_SHOOTER].removeLast();
			shooter.set(posX, posY);			
			//activeCollidableObjectList.add(shooter);
            waitingList.add(shooter);
		}
	}
	
	public void addBullet(float posX, float posY, float velX, float velY)
	{

		if(objPool[INDEX_BULLET].size() > 0)
		{
			Bullet bullet = (Bullet) objPool[INDEX_BULLET].removeLast();
			bullet.set(posX, posY, velX, velY);			
			activeCollidableObjectAddableList.add(bullet);
			
		}
	}
	
	public void addWallBlockToWaitingList(float posX, float posY)
	{		
		if(objPool[INDEX_WALL_BLOCK].size() > 0)
		{
			WallBlock block = (WallBlock) objPool[INDEX_WALL_BLOCK].removeLast();
			block.set(posX, posY);			
			//activeCollidableObjectList.add(block);
            waitingList.add(block);
		}
	}

    public void addBombToWaitingList(float posX, float posY)
    {
        if(objPool[INDEX_BOMB].size() > 0)
        {
            Bomb bomb = (Bomb)objPool[INDEX_BOMB].removeLast();
            bomb.set(posX, posY);
            waitingList.add(bomb);
        }
    }

    public void addGemToWaitingList(float posX, float posY)
    {
        if(objPool[INDEX_GEM].size() > 0)
        {
            Gem gem = (Gem)objPool[INDEX_GEM].removeLast();
            gem.set(posX,posY);
            waitingList.add(gem);
        }
    }

    public Particle addParticle()
    {
        Particle particle = null;
        if(particlePool.size() > 0)
        {
            particle = particlePool.removeLast();

            activeParticleList.add(particle);
        }
        return particle;
    }

	/*public void addSomeParticle(float posX, float posY)
	{/*
		int count = 0;
		for(Particle particle : particlePool)
		{
			if(!particle.enabled){
				
				Random randomAccessFile = Static.randomAccessFile;
				particle.setPosAndScale(posX, posY-0.12f, -2+randomAccessFile.nextFloat()*4, -0.5f+randomAccessFile.nextFloat(), 0.4f, randomAccessFile.nextInt(2));
							
				count++;			
			}

			if(count > 2)
				break;
		}
		int count = 0;
		while(particlePool.size() > 0)
		{
			Particle particle = particlePool.removeLast();
			
			Random rand = Static.rand;
			particle.set(posX, posY-0.12f, -2+rand.nextFloat()*4, -0.5f+rand.nextFloat(), 0.4f, rand.nextInt(2));
			
			activeParticleList.add(particle);
			
			count++;			
			if(count > 2) break;
		}
		
	*/
	
	public void update(float deltaTime)
	{					
		updateParticles(deltaTime);
		
			
		updateCollidableObjects(deltaTime);
		//updating all objects in active list
		
		updateDumboebaGroupList(deltaTime);
		
		clearDrawPriorityList();
		
		fillGridAndDrawPriorityLists();
		
		
		interactifyUsingGrids();
		//interactify();

        checkWaitingListAndAddIfNeeded();
		
	}
	
	
	
	private void updateDumboebaGroupList(float deltaTime)
	{		
		
		for(int i=0; i<activeDumboebaGroup.size(); i++)
		{
			DumboebaGroup group = activeDumboebaGroup.get(i);
			group.update(deltaTime);
			if(!group.enabled)
			{
                //Logger.print("Emptying and disabling the group because group is disabled");
				emptyTheDumboGroup(group);
				
				//removing the group itself and adding it to pool
				activeDumboebaGroup.remove(i);				
				dumboebaGroupPool.add(group);
			}
		}
	}
	
	private void emptyTheDumboGroup(DumboebaGroup group)
	{
		//removing all points from that group and adding to pool
		MyArrayList<PathPoint> groupPointList = group.pointList;
		while(groupPointList.size() > 0)
		{
			PathPoint p = groupPointList.removeLast();
			pointListPool.add(p);
		}
		
		//disabling all dumboes from group
		MyArrayList<Dumboeba> groupDumboList = group.dumboList;
		
		while(groupDumboList.size() > 0)
		{
			groupDumboList.removeLast().disable();  // it will be removed soon from activeCollidableList
		}
		
		
	}
	
	private void updateParticles(float deltaTime)
	{		
		for(int i=0; i<activeParticleList.size(); i++)
		{
			Particle particle = activeParticleList.get(i);

			particle.update(deltaTime);

            if(!particle.enabled)  // if disableOverlayDrawable
            {
                particlePool.add(particle);  // add to pool

                int sizeMinusOne = activeParticleList.size() - 1;

                if(i == sizeMinusOne)  // if last element
                {
                    activeParticleList.remove(i); // just remove it
                    break; // no more updating
                }
                else
                {
                    activeParticleList.set(i, activeParticleList.remove(sizeMinusOne));
                }
            }
		}
		
	}
	
	private void updateCollidableObjects(float deltaTime)
	{		
		for(int i=0; i<activeCollidableObjectList.size(); i++)
		{
			CollidableObject obj = activeCollidableObjectList.get(i);

			obj.update(deltaTime);  // update

            if(!obj.enabled)
            {
                objPool[obj.objectType].add(obj); // add to pool

                int sizeMinusOne = activeCollidableObjectList.size()-1;

                if(i == sizeMinusOne)  // if last element
                {
                    activeCollidableObjectList.remove(i); // just remove it
                    break; // no more updating
                }
                else
                {

                    activeCollidableObjectList.set(i, activeCollidableObjectList.remove(sizeMinusOne));
                }
            }
		}		
		
		// adding new objects created during update		
		while(activeCollidableObjectAddableList.size() > 0)
		{
			activeCollidableObjectList.add(activeCollidableObjectAddableList.removeLast());
		}
	}

    private void checkWaitingListAndAddIfNeeded()
    {
        final float baseY = Static.cam.bottomPos + Static.TARGET_HEIGHT + 0.2f; //0.2 is padding

        int i=0;
        while(i<waitingList.size())
        {
            if(baseY >= waitingList.get(i).pos.y)
            {
                if(i == waitingList.size()-1) // if last element
                {
                    //just remove and add to active list
                    activeCollidableObjectList.add(waitingList.removeLast());
                    break;  // not really needed
                }
                else //smart removal by swapping
                {
                    activeCollidableObjectList.add(waitingList.get(i)); // add first to active list
                    waitingList.set(i,waitingList.removeLast()); // swap out the element
                    continue; // because the new element in value i has to be checked too
                }
            }

            i++;
        }


    }
	
	
	/*private void removeCollidableObjectsFromActiveList()
	{	
		while(activeCollidableObjListRemovableIndex.size() > 0)
		{
			int index = activeCollidableObjListRemovableIndex.removeLast();
			CollidableCircularObject obj = activeCollidableObjectList.remove(index);
			
			
			objPool[obj.objectType].add(obj);
		}
	}*/
	
	private void fillGridAndDrawPriorityLists()
	{	
		for(int i=0; i<activeCollidableObjectList.size(); i++)
		{
			CollidableObject obj = activeCollidableObjectList.get(i);
			griddify(i);		
			
			drawPriority[obj.drawPriority].add(i);
		}
	}
	
	private void griddify(int index)
	{
		CollidableObject obj = activeCollidableObjectList.get(index);
		
		final float camBottom = Static.cam.bottomPos;
		final float bottomPos = obj.getBottomPos();//obj.pos.y - obj.radius;
		final float topPos = obj.getTopPos();
		



//		int gridIndex1 = (int)(  (bottomPos - camBottom)/GRID_HEIGHT);
//		int gridIndex2 = (int)( (topPos - camBottom )/GRID_HEIGHT );

       // float gridIndexBottomFloat = (bottomPos - camBottom)

        int gridIndex1 = (int)( (bottomPos - camBottom)/GRID_HEIGHT + 1.0f);  // +1.0 because first grid is below camBottom
        int gridIndex2 = (int)( (topPos - camBottom )/GRID_HEIGHT + 1.0f);


		int maxIndexLimit = grid.length;
		
		if(gridIndex1 < maxIndexLimit)
		{
            if(gridIndex1 >= 0)
			    grid[gridIndex1].add(index);
            else {
                activeCollidableObjectList.get(index).disable();
                //Logger.print("disabling element because it got too low :( " + activeCollidableObjectList.get(index));
            }
		}
		if(gridIndex1 != gridIndex2 && gridIndex2 < maxIndexLimit && gridIndex2 >= 0 )
		{
			grid[gridIndex2].add(index);
		}
		
	}
	
	private void clearDrawPriorityList()
	{
		for(int i=0; i<drawPriority.length; i++)
		{
			drawPriority[i].clear();
		}
		
	}
	
	
	
	
	
	private void interactifyUsingGrids()
	{
		//for(ArrayList<CollidableCircularObject> list : grid )
		for(int k=0; k<grid.length; k++)  // for each grid
		{
			ArrayIntList indexList = grid[k];
			
			for(int i=0; i<indexList.size(); i++)
			{
				CollidableObject left = activeCollidableObjectList.get(indexList.get(i));
				
				//for each combinations with other elements in the list
				for(int j = (i+1); j<indexList.size(); j++)
				{
					CollidableObject right = activeCollidableObjectList.get(indexList.get(j));
					int indexLeft = left.objectType; int indexRight = right.objectType;
					if(interactionTable[indexLeft][indexRight] != null)
					{
						interactionTable[indexLeft][indexRight].interact(left, right);
					}
				}		
				
			}
			
			indexList.clear();
		}
	}
	

	
	public void disableObjects()
	{
		clearListAndAddToPool(activeCollidableObjectList);
        clearListAndAddToPool(activeCollidableObjectAddableList);
        clearListAndAddToPool(waitingList);


		//clearing dumbo group
		Iterator <DumboebaGroup> dumboGroupIter = activeDumboebaGroup.iterator();
		
		while(dumboGroupIter.hasNext())
		{
			DumboebaGroup group = dumboGroupIter.next();
			
			emptyTheDumboGroup(group);
			
			//removing group and adding it to pool
			dumboGroupIter.remove();
			dumboebaGroupPool.add(group);
			
		}

		//clearing particles
		Iterator<Particle> iter2 = activeParticleList.iterator();
		
		while(iter2.hasNext())
		{
			Particle particle = iter2.next();
			iter2.remove();
			particlePool.add(particle);
		}
		
		
	}

    private void clearListAndAddToPool(ArrayList<CollidableObject> list)
    {
        Iterator<CollidableObject> iter1 = list.iterator();

        while(iter1.hasNext())
        {
            CollidableObject obj = iter1.next();
            iter1.remove();
            objPool[obj.objectType].add(obj);
        }
    }
	
	
	
	
	
	public void reset()
	{
		enableMusca();
		clearDrawPriorityList();
	}
	
	
	public void setEverySingleObjectBackToBase(float threshold)
	{
		for(int i=0; i<activeCollidableObjectList.size(); i++)
		{
			CollidableObject obj = activeCollidableObjectList.get(i);
			obj.setPosYToBase(threshold);
		}
		for(int i=0; i<activeParticleList.size(); i++)
		{
			Particle p = activeParticleList.get(i);
			p.pos.y -= threshold;
		}

        for(int i=0; i<waitingList.size(); i++)
        {
            CollidableObject obj = waitingList.get(i);
            obj.setPosYToBase(threshold);
        }

        for(int i=0; i<activeDumboebaGroup.size(); i++)
        {
            DumboebaGroup grp = activeDumboebaGroup.get(i);
            grp.setPosYBackToBase(threshold);
        }


	}

    public void killAllObjectsFromAllLists()
    {
        killObjectsFromList(activeCollidableObjectList);
        killObjectsFromList(activeCollidableObjectAddableList);
        killObjectsFromList(waitingList);
    }

    public void killObjectsFromList(ArrayList<CollidableObject> list)
    {
        for(int i=0; i<list.size(); i++)
        {
            CollidableObject obj = list.get(i);
            obj.deathToYou();
        }
    }

    public void killAllObjectsOfTheTypeFromAllLists(int objectType)
    {
        killObjectsOfTheTypeFromList(activeCollidableObjectList,objectType);
        killObjectsOfTheTypeFromList(activeCollidableObjectAddableList,objectType);
        killObjectsOfTheTypeFromList(waitingList,objectType);
    }

    public void killObjectsOfTheTypeFromList(ArrayList<CollidableObject> list, int objectType)
    {


        for(int i=0; i<list.size(); i++)
        {
            CollidableObject obj = list.get(i);

            if(obj.objectType == objectType)
            {
                obj.deathToYou();
            }
        }
    }


	public void printOutCollidableListPool()
    {
        Logger.print("printint out pool");
        for(int i=0; i<objPool.length; i++)
        {
            MyArrayList<CollidableObject> list = objPool[i];
            Logger.print("size of list at index " + i + " is " + list.size());
        }

        Logger.print("......end of pool printing......");
    }





}

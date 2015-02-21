package com.antibot.food;

public class LevelGenerator
{

    public static final int MODE_GEN_FROM_FILE = 100, MODE_GEN_AUTO = 101, MODE_GEN_TUTE = 102;
    private int mode;



    private LevelGeneratorInterface levelGeneratorCurrent;

    private LevelGeneratorFromFile levelGeneratorFromFile;
    private LevelGeneratorAuto levelGeneratorAuto;
    private LevelGeneratorTute levelGeneratorTute;



    public LevelGenerator()
	{

       levelGeneratorFromFile = new LevelGeneratorFromFile();
       levelGeneratorAuto = new LevelGeneratorAuto();
       levelGeneratorTute = new LevelGeneratorTute();
	}

    public void onStart()
    {
        mode = MODE_GEN_TUTE; levelGeneratorCurrent = levelGeneratorTute;  // change this later
        setMode(MODE_GEN_TUTE);
    }

    public void onEnd()
    {
        levelGeneratorCurrent.onEnd();
    }

    private void setMode(int mode)
    {
        if(this.mode != mode)
        {
            this.mode = mode;
            levelGeneratorCurrent.onEnd();

            if(mode == MODE_GEN_FROM_FILE)
            {
                levelGeneratorCurrent = levelGeneratorFromFile;
            }
            else if(mode == MODE_GEN_TUTE)
            {
                levelGeneratorCurrent = levelGeneratorTute;
            }
            else if(mode == MODE_GEN_AUTO)
            {
                levelGeneratorCurrent = levelGeneratorAuto;
            }
        }

        levelGeneratorCurrent.onStart(0);
    }

    public void update(float deltaTime)
    {
        if( levelGeneratorCurrent.update(deltaTime) > 0 )
        {
            setMode(MODE_GEN_FROM_FILE);
        }
    }

    public void deltaY(float delY)
    {
        levelGeneratorCurrent.deltaY(delY);
    }


    public interface LevelGeneratorInterface {

        public int update(float deltaTime); // the int gives details if job is complete or not
        public void onStart(float userProgress);
        public void onEnd();
        public void deltaY(float delY);

    }



	
}

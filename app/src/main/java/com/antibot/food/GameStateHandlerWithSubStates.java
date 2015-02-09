package com.antibot.food;


public abstract class GameStateHandlerWithSubStates extends GameStateHandler{


    protected boolean stateChangeFlag;
    public int indexToChange; // or data

    GameStateHandler subStateArr[];

    GameStateHandler current;

    int level;
    char msgStart, msgEnd;

    public GameStateHandlerWithSubStates(int objType) {
        super(objType);
    }

    public GameStateHandlerWithSubStates(int objType, GameStateHandlerWithSubStates parent)
    {
        super(objType,parent);
    }

    private final void changeStateIf()
    {
        if(stateChangeFlag)
        {
            if(level == 0) {
                GameStateHandler to = subStateArr[indexToChange];

                current.end(msgEnd);
                to.onStart(msgStart);
                current = to;
            }
            else if(level > 0)
            {
                parent.setChangeStateFlag(level-1,indexToChange,msgStart,msgEnd);
            }


            stateChangeFlag = false;
            //cleaning up the delivery system
            msgStart = MSG_NULL;
            msgEnd = MSG_NULL;
            level = 0;

        }
    }



    public final void setChangeStateFlag(int index)
    {
        setChangeStateFlag(index, MSG_NULL, MSG_NULL);

    }

    public final void setChangeStateFlag(int index, char msgStart, char msgEnd)
    {
        setChangeStateFlag(0, index, msgStart, msgEnd);

    }

    public final void setChangeStateFlag(int level, int index, char msgStart, char msgEnd)
    {
        stateChangeFlag = true;
        this.indexToChange = index;
        this.level = level;
        this.msgStart = msgStart;
        this.msgEnd = msgEnd;

        changeStateIf();
    }


    public abstract void receiveMessage(char someMessage);

}

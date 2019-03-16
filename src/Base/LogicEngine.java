package Base;

import Elements.Shot;

public class LogicEngine extends Thread{

    Data data;

    public LogicEngine(Data data){
        super();
        this.data = data;
    }

    @Override
    public void run() {
        //TODO running should be a shared volatile variable that's the same in Game, GE and LE.
        boolean running = true;
        while(running) {
            long beginTime = System.currentTimeMillis();
            for (Shot shot : data.shots) shot.move();
            try {
                LogicEngine.sleep(15 - System.currentTimeMillis() + beginTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

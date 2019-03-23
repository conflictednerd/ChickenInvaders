package Base;

import Elements.Shot;

public class ShotThread extends Thread {

    private Data data;
//    private boolean isFirstTurn = true;
    public ShotThread(Data data){
        super();
        this.data = data;
    }

    @Override
    public void run() {
        while(true){
//            if(isFirstTurn) {
//                isFirstTurn = false;
//                try {
//                    ShotThread.sleep(200);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
            data.shots.add(new Shot(data.rocket.getX(), data.rocket.getY()));
            try {
                ShotThread.sleep(150);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

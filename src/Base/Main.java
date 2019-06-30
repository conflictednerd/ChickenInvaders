package Base;

public class Main {

    public static void main(String[] args) {
        //todo used a thread to simplify pausing and returning to main menu process.
//        Thread t = new Thread(() -> {
//            Game game = new Game();
//        }, "game Thread");
//        t.start();
        Game game = new Game();
        game.playAsServer(2323);
    }
}

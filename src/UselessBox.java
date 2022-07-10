import java.util.concurrent.atomic.AtomicBoolean;

public class UselessBox {

    public static void main(String[] args) {
        Switch aSwitch = new Switch();
        UserThread userThread = new UserThread(aSwitch);
        UserThread.ToyThread toyThread = new UserThread.ToyThread(aSwitch);

        toyThread.start();
        userThread.start();

        try {
            userThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        toyThread.interrupt();
    }

}

class Switch {
    AtomicBoolean isOn = new AtomicBoolean(false);

    public AtomicBoolean getIsOn() {
        return isOn;
    }

    public void setIsOn(AtomicBoolean isOn) {
        this.isOn = isOn;
    }
}


    class UserThread extends Thread {
        private final Switch aSwitch;

        public UserThread(Switch aSwitch) {
            this.aSwitch = aSwitch;
        }

        @Override
        public void run() {

            int numberOfSwitchings = 3;
            for (int i = 0; i < numberOfSwitchings; i++) {
                try {
                    Thread.sleep((int) (Math.random() * 3000));
                } catch (InterruptedException e) {
                    this.interrupt();
                }
                if (!aSwitch.getIsOn().get()) {
                    aSwitch.setIsOn(new AtomicBoolean(true));
                    System.out.println("Поток-пользователь включил тумблер");
                }
            }

        }

        static class ToyThread extends Thread {
        Switch aSwitch;

        public ToyThread(Switch aSwitch) {
            this.aSwitch = aSwitch;
        }

        @Override
        public void run() {

            while (!isInterrupted()) {
                try {
                    Thread.sleep((int) (Math.random() * 2000));
                } catch (InterruptedException e) {
                    this.interrupt();
                }
                if (aSwitch.getIsOn().get()) {
                    aSwitch.setIsOn(new AtomicBoolean(false));
                    System.out.println("Поток-игрушка обнаружила включение и выключила тумблер");
                }
            }
        }
    }
}




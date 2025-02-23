
class NumberPrinter {
    private int count = 1; // Keeps track of the next number to print
    private final int n;
    private boolean printZero = true;  // To control when ZeroThread should print

    public NumberPrinter(int n) {
        this.n = n;
    }

    // Print 0
    public synchronized void printZero() {
        while (count <= n) {
            if (printZero) {
                System.out.print("0");
                printZero = false;  // Next time, don't print 0, let Odd or Even print
                notifyAll();  // Notify other threads to proceed
                try {
                    wait();  // Wait for other threads to print odd/even
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    wait();  // Wait until it's time to print 0
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Print even numbers
    public synchronized void printEven() {
        while (count <= n) {
            if (count % 2 == 0) {
                System.out.print(count);
                count++;
                printZero = true; // Allow ZeroThread to print next
                notifyAll();  // Notify other threads to proceed
                try {
                    if (count <= n) {
                        wait();  // Wait until other threads are done
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    wait();  // Wait until it's time to print an even number
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Print odd numbers
    public synchronized void printOdd() {
        while (count <= n) {
            if (count % 2 != 0) {
                System.out.print(count);
                count++;
                printZero = true; // Allow ZeroThread to print next
                notifyAll();  // Notify other threads to proceed
                try {
                    if (count <= n) {
                        wait();  // Wait until other threads are done
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    wait();  // Wait until it's time to print an odd number
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

class ZeroThread extends Thread {
    private final NumberPrinter numberPrinter;

    public ZeroThread(NumberPrinter numberPrinter) {
        this.numberPrinter = numberPrinter;
    }

    @Override
    public void run() {
        numberPrinter.printZero();
    }
}

class EvenThread extends Thread {
    private final NumberPrinter numberPrinter;

    public EvenThread(NumberPrinter numberPrinter) {
        this.numberPrinter = numberPrinter;
    }

    @Override
    public void run() {
        numberPrinter.printEven();
    }
}

class OddThread extends Thread {
    private final NumberPrinter numberPrinter;

    public OddThread(NumberPrinter numberPrinter) {
        this.numberPrinter = numberPrinter;
    }

    @Override
    public void run() {
        numberPrinter.printOdd();
    }
}

public class ThreadController {
    public static void main(String[] args) {
        int n = 8; // Specify the number up to which you want to print

        NumberPrinter numberPrinter = new NumberPrinter(n);

        System.out.println("For Input " + n);

        // Create threads
        ZeroThread zeroThread = new ZeroThread(numberPrinter);
        EvenThread evenThread = new EvenThread(numberPrinter);
        OddThread oddThread = new OddThread(numberPrinter);

        // Start threads
        zeroThread.start();
        oddThread.start();
        evenThread.start();
    }
}
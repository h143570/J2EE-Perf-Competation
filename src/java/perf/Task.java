package perf;

import java.util.ArrayList;
import java.util.List;

public class Task implements Runnable {

    private LargestDivison theTask;
    private List<String> testData;
    private List<Long> result;

    public Task(List<String> testData) {
        theTask = new LargestDivison();
        this.testData = testData;
        result = new ArrayList<>(testData.size());
    }

    @Override
    public void run() {
        try {
            for (String data : testData) {
                if (Thread.interrupted()) {
                    throw new InterruptedException();
                }
                result.add(theTask.getLargestDivison(data));
            }
        } catch (Exception e) {
            System.out.println("Test failed, exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<Long> getResult() {
        return result;
    }

}

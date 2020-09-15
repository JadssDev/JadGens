package ml.jadss.jadgens.tasks;

import ml.jadss.jadgens.JadGens;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ProduceScheduler {

    private ScheduledFuture<?> scheduledFuture;

    public ProduceScheduler(int periodInSeconds) {
        if (periodInSeconds > 0)
            scheduledFuture = JadGens.getExecutorService().scheduleAtFixedRate(new ProduceRunnable(), 0L, periodInSeconds, TimeUnit.SECONDS);
    }

    public ScheduledFuture<?> getScheduledFuture() {
        return scheduledFuture;
    }
}

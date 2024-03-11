package me.kyren223.kapi.utility;

import me.kyren223.kapi.KPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

public class Task extends BukkitRunnable {
    
    private final Consumer<BukkitRunnable> task;
    public Task(Consumer<BukkitRunnable> task) {
        this.task = task;
    }
    
    public static void runLater(Consumer<BukkitRunnable> task, long delay) {
        new Task(task).runTaskLater(KPlugin.get(), delay);
    }
    
    public static void runRepeatedly(Consumer<BukkitRunnable> task, long delay, long period) {
        new Task(task).runTaskTimer(KPlugin.get(), delay, period);
    }
    
    public static void runFor(long duration, Consumer<BukkitRunnable> task, long delay, long period) {
        AtomicLong ticks = new AtomicLong(0);
        Task.runRepeatedly(t -> {
            ticks.addAndGet(period);
            if (ticks.get() >= duration) {
                t.cancel();
            }
            task.accept(t);
        }, delay, period);
    }
    
    public static void runFor(long duration, Consumer<BukkitRunnable> task, Consumer<BukkitRunnable> onEnd, long delay, long period) {
        AtomicLong ticks = new AtomicLong(0);
        Task.runRepeatedly(t -> {
            ticks.addAndGet(1);
            if (ticks.get() >= duration) {
                t.cancel();
            }
            task.accept(t);
            if (t.isCancelled()) {
                onEnd.accept(t);
            }
        }, delay, period);
    }
    
    public static void runWhile(BooleanSupplier predicate, Consumer<BukkitRunnable> task, long delay, long period) {
        Task.runRepeatedly(t -> {
            if (!predicate.getAsBoolean()) {
                t.cancel();
            }
            task.accept(t);
        }, delay, period);
    }
    
    public static void runWhile(BooleanSupplier predicate, Consumer<BukkitRunnable> task, Consumer<BukkitRunnable> onEnd, long delay, long period, long duration) {
        AtomicLong ticks = new AtomicLong(0);
        Task.runRepeatedly(t -> {
            ticks.addAndGet(period);
            if (ticks.get() >= duration || !predicate.getAsBoolean()) {
                t.cancel();
            }
            task.accept(t);
            if (t.isCancelled()) {
                onEnd.accept(t);
            }
        }, delay, period);
    }
    
    @Override
    public void run() {
        task.accept(this);
    }
}

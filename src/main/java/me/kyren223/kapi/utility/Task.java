package me.kyren223.kapi.utility;

import me.kyren223.kapi.KPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Task extends BukkitRunnable {
    
    private final Consumer<BukkitRunnable> task;
    public Task(Consumer<BukkitRunnable> task) {
        this.task = task;
    }
    
    public static void runLater(Consumer<BukkitRunnable> task, long delay) {
        new Task(task).runTaskLater(KPlugin.i, delay);
    }
    
    public static void runRepeatedly(Consumer<BukkitRunnable> task, long delay, long period) {
        new Task(task).runTaskTimer(KPlugin.i, delay, period);
    }
    
    public static void runTaskFor(Consumer<BukkitRunnable> task, long delay, long period, long duration) {
        AtomicLong ticks = new AtomicLong(0);
        Task.runRepeatedly(t -> {
            ticks.addAndGet(period);
            if (ticks.get() >= duration) {
                t.cancel();
            }
            task.accept(t);
        }, delay, period);
    }
    
    public static void runTaskFor(Consumer<BukkitRunnable> task, Consumer<BukkitRunnable> onEnd, long delay, long period, long duration) {
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
    
    public static void runTaskWhile(Consumer<BukkitRunnable> task, long delay, long period, Supplier<Boolean> predicate) {
        Task.runRepeatedly(t -> {
            if (!predicate.get()) {
                t.cancel();
            }
            task.accept(t);
        }, delay, period);
    }
    
    public static void runTaskWhile(Consumer<BukkitRunnable> task, Consumer<BukkitRunnable> onEnd, long delay, long period, long duration, Supplier<Boolean> predicate) {
        AtomicLong ticks = new AtomicLong(0);
        Task.runRepeatedly(t -> {
            ticks.addAndGet(period);
            if (ticks.get() >= duration || !predicate.get()) {
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

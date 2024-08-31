/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the AGPLv3. See LICENSE or https://www.gnu.org/licenses/agpl-3.0 for details.
 */

package me.kyren223.kapi.utility;

import me.kyren223.kapi.annotations.Kapi;
import me.kyren223.kapi.core.KapiPlugin;
import me.kyren223.kapi.data.Option;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.jspecify.annotations.Nullable;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * A builder for easy creation and scheduling of bukkit tasks.
 */
@Kapi
public class TaskBuilder {
    
    private @Nullable Consumer<KapiTask> task;
    private boolean isAsync;
    private long delayInTicks;
    private Option<Long> intervalInTicks;
    private @Nullable Long timesToRepeat;
    private @Nullable Long durationInTicks;
    private @Nullable Predicate<KapiTask> whileCondition;
    private @Nullable Consumer<KapiTask> onFinish;
    
    private TaskBuilder(@Nullable Consumer<KapiTask> task) {
        this.task = task;
        this.isAsync = false;
        this.delayInTicks = 1;
        this.intervalInTicks = Option.none();
        this.timesToRepeat = null;
        this.durationInTicks = null;
        this.onFinish = null;
        this.whileCondition = null;
    }
    
    /**
     * Creates a new task.
     * Unless specified otherwise, the task will run once on the next server tick.
     * <p>
     * Note that you would need to use {@link #schedule(Consumer)} or {@link #schedule(Runnable)}
     * otherwise if there is no task, scheduling will throw an exception.
     *
     * @return A new task
     * @see #create(Consumer)
     * @see #create(Runnable)
     */
    @Kapi
    public static TaskBuilder create() {
        return new TaskBuilder(null);
    }
    
    /**
     * Creates a new task with the given task.
     * Unless specified otherwise, the task will run once on the next server tick.
     *
     * @see #create()
     * @see #create(Runnable)
     */
    @Kapi
    public static TaskBuilder create(Consumer<KapiTask> task) {
        return new TaskBuilder(task);
    }
    
    /**
     * Creates a new task with the given task.
     * Unless specified otherwise, the task will run once on the next server tick.
     *
     * @see #create()
     * @see #create(Consumer)
     */
    @Kapi
    public static TaskBuilder create(Runnable task) {
        return create(t -> task.run());
    }
    
    /**
     * Schedules the task for execution.
     * You may call this method multiple times.
     *
     * @throws IllegalStateException if no task was provided
     * @see #schedule(Consumer)
     * @see #schedule(Runnable)
     */
    @Kapi
    public void schedule() {
        if (task == null) {
            throw new IllegalStateException("No task was provided");
        }
        
        BukkitScheduler scheduler = KapiPlugin.get().getServer().getScheduler();
        KapiTaskImpl kapiTask = new KapiTaskImpl(onFinish, delayInTicks, intervalInTicks);
        
        Consumer<BukkitTask> taskProcessor = bukkitTask -> {
            kapiTask.bukkitTask = bukkitTask;
            
            if (whileCondition != null && !whileCondition.test(kapiTask)) {
                kapiTask.cancel();
                return;
            }
            
            task.accept(kapiTask);
            
            kapiTask.timesRan++;
            
            boolean durationReached = durationInTicks != null && kapiTask.getDuration() >= durationInTicks;
            boolean timesReached = timesToRepeat != null && kapiTask.getTimesRan() >= timesToRepeat;
            if (durationReached || timesReached) {
                kapiTask.cancel();
            }
        };
        
        boolean isSync = !isAsync;
        if (isSync && intervalInTicks.isNone()) {
            scheduler.runTaskLater(KapiPlugin.get(), taskProcessor, delayInTicks);
        } else if (isSync && intervalInTicks.isSome()) {
            scheduler.runTaskTimer(KapiPlugin.get(), taskProcessor, delayInTicks, intervalInTicks.unwrap());
        } else if (isAsync && intervalInTicks.isNone()) {
            scheduler.runTaskLaterAsynchronously(KapiPlugin.get(), taskProcessor, delayInTicks);
        } else if (isAsync && intervalInTicks.isSome()) {
            scheduler.runTaskTimerAsynchronously(
                KapiPlugin.get(), taskProcessor, delayInTicks, intervalInTicks.unwrap());
        } else {
            throw new AssertionError("Unreachable");
        }
    }
    
    /**
     * Schedules the task for execution with the given task.
     * You may call this method multiple times.
     *
     * @param task the task to schedule
     * @see #schedule()
     * @see #schedule(Runnable)
     */
    @Kapi
    public void schedule(Consumer<KapiTask> task) {
        this.task = task;
        schedule();
    }
    
    /**
     * Schedules the task for execution with the given task.
     * You may call this method multiple times.
     *
     * @param task the task to schedule
     * @see #schedule()
     * @see #schedule(Consumer)
     */
    @Kapi
    public void schedule(Runnable task) {
        this.task = t -> task.run();
        schedule();
    }
    
    // --- Builder Methods ---
    
    /**
     * <b>Asynchronous tasks should never access any API in Bukkit.</b>
     * <b>Great care should be taken to ensure the thread-safety of asynchronous tasks.</b>
     * <p>
     * Sets the task to run asynchronously.
     *
     * @return this, for chaining
     */
    @Kapi
    public TaskBuilder async() {
        this.isAsync = true;
        return this;
    }
    
    /**
     * Sets the initial delay of the task.
     * After scheduling, the task will be delayed by this amount before running.
     * <p>
     * The minimum delay is one tick, anything less will behave as if the delay was 1.
     *
     * @param ticks the number of server ticks to delay the task
     * @return this, for chaining
     * @see #delay(long, TimeUnit)
     */
    @Kapi
    public TaskBuilder delay(long ticks) {
        this.delayInTicks = ticks;
        return this;
    }
    
    /**
     * Sets the initial delay of the task.
     * After scheduling, the task will be delayed by this amount before running.
     * <p>
     * The minimum delay is one tick, anything less will behave as if the delay was 1.
     *
     * @param delay    the delay in the given time unit
     * @param timeUnit the time unit of the delay
     * @return this, for chaining
     * @see #delay(long)
     */
    @Kapi
    public TaskBuilder delay(long delay, TimeUnit timeUnit) {
        this.delayInTicks = timeUnit.toTicks(delay);
        return this;
    }
    
    /**
     * Sets the time interval between each run of the task.
     * <p>
     * The minimum interval is one tick, anything less will behave as if the interval was 1.
     * <p>
     * If no interval is set, the task will only run once.
     *
     * @param ticks the number of server ticks to wait between runs
     * @return this, for chaining
     * @see #interval(long, TimeUnit)
     */
    @Kapi
    public TaskBuilder interval(long ticks) {
        this.intervalInTicks = Option.some(ticks);
        return this;
    }
    
    /**
     * Sets the time interval between each run of the task.
     * <p>
     * The minimum interval is one tick, anything less will behave as if the interval was 1.
     * <p>
     * If no interval is set, the task will only run once.
     *
     * @param interval the interval in the given time unit
     * @param timeUnit the time unit of the interval
     * @return this, for chaining
     * @see #interval(long)
     */
    @Kapi
    public TaskBuilder interval(long interval, TimeUnit timeUnit) {
        this.intervalInTicks = Option.some(timeUnit.toTicks(interval));
        return this;
    }
    
    /**
     * Sets the callback to run when the task finishes.
     * <p>
     * This includes manual calls to {@link KapiTask#cancel()} but
     * also "automatic" cancellation such as when the task's
     * {@link #repeat(long)} has been reached.
     *
     * @param onFinish the callback to run when the task finishes
     * @return this, for chaining
     * @see #onFinish(Runnable)
     */
    @Kapi
    public TaskBuilder onFinish(Consumer<KapiTask> onFinish) {
        this.onFinish = onFinish;
        return this;
    }
    
    /**
     * Sets the callback to run when the task finishes.
     * <p>
     * This includes manual calls to {@link KapiTask#cancel()} but
     * also "automatic" cancellation such as when the task's
     * {@link #repeat(long)} has been reached.
     *
     * @param onFinish the callback to run when the task finishes
     * @return this, for chaining
     * @see #onFinish(Consumer)
     */
    @Kapi
    public TaskBuilder onFinish(Runnable onFinish) {
        this.onFinish = t -> onFinish.run();
        return this;
    }
    
    /**
     * Stops the task after the given number of runs has been reached.
     * <p>
     * Any value below 1 will be treated as 1.
     * <p>
     * The interval will be set to one server tick if it's not already set.
     * <p>
     * This is a cancellation point. When used with other cancellation points,
     * the first one to be reached will stop the task and no further runs will be made.
     *
     * @param times the number of times to repeat the task
     * @return this, for chaining
     */
    @Kapi
    public TaskBuilder repeat(long times) {
        intervalInTicks = intervalInTicks.or(Option.some(1L));
        this.timesToRepeat = times;
        return this;
    }
    
    /**
     * Stops the task after the given duration has passed.
     * Does not count the initial delay, only the intervals.
     * <p>
     * The interval will be set to one server tick if it's not already set.
     * <p>
     * This is a cancellation point. When used with other cancellation points,
     * the first one to be reached will stop the task and no further runs will be made.
     * <p>
     * Note, internally uses timesRan * intervalInTicks to calculate the total duration.
     *
     * @param ticks the number of server ticks to wait before the task finishes
     * @return this, for chaining
     */
    @Kapi
    public TaskBuilder duration(long ticks) {
        intervalInTicks = intervalInTicks.or(Option.some(1L));
        this.durationInTicks = ticks;
        return this;
    }
    
    /**
     * Sets the condition to check before each run of the task.
     * <p>
     * The interval will be set to one server tick if it's not already set.
     * <p>
     * This is a cancellation point. When used with other cancellation points,
     * the first one to be reached will stop the task and no further runs will be made.
     *
     * @param condition the condition to check, true to continue, false to stop
     * @return this, for chaining
     * @see #whileCondition(BooleanSupplier)
     */
    @Kapi
    public TaskBuilder whileCondition(Predicate<KapiTask> condition) {
        intervalInTicks = intervalInTicks.or(Option.some(1L));
        this.whileCondition = condition;
        return this;
    }
    
    /**
     * Sets the condition to check before each run of the task.
     * <p>
     * The interval will be set to one server tick if it's not already set.
     * <p>
     * This is a cancellation point. When used with other cancellation points,
     * the first one to be reached will stop the task and no further runs will be made.
     *
     * @param condition the condition to check, true to continue, false to stop
     * @return this, for chaining
     * @see #whileCondition(Predicate)
     */
    @Kapi
    public TaskBuilder whileCondition(BooleanSupplier condition) {
        intervalInTicks = intervalInTicks.or(Option.some(1L));
        this.whileCondition = t -> condition.getAsBoolean();
        return this;
    }
    
    // --- Private ---
    
    private static class KapiTaskImpl implements KapiTask {
        private final @Nullable Consumer<KapiTask> onFinish;
        private final long delay;
        private final Option<Long> interval;
        private @Nullable BukkitTask bukkitTask;
        private long timesRan;
        
        public KapiTaskImpl(@Nullable Consumer<KapiTask> onFinish, long delay, Option<Long> interval) {
            this.onFinish = onFinish;
            this.delay = delay;
            this.interval = interval;
        }
        
        @Override
        public int getTaskId() {
            assert bukkitTask != null;
            return bukkitTask.getTaskId();
        }
        
        @Override
        public Plugin getOwner() {
            assert bukkitTask != null;
            return bukkitTask.getOwner();
        }
        
        @Override
        public boolean isSync() {
            assert bukkitTask != null;
            return bukkitTask.isSync();
        }
        
        @Override
        public boolean isCancelled() {
            assert bukkitTask != null;
            return bukkitTask.isCancelled();
        }
        
        @Override
        public void cancel() {
            if (isCancelled()) {
                return;
            }
            
            assert bukkitTask != null;
            bukkitTask.cancel();
            if (onFinish != null) {
                onFinish.accept(this);
            }
        }
        
        @Override
        public long getDelay() {
            return delay;
        }
        
        @Override
        public Option<Long> getInterval() {
            return interval;
        }
        
        @Override
        public long getTimesRan() {
            return timesRan;
        }
        
        @Override
        public long getDuration() {
            return timesRan * interval.unwrapOr(0L);
        }
    }
}

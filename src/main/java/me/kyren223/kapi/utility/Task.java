/*
 * Copyright (c) 2024 Kapi Contributors. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted if the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions, the following disclaimer and the list of contributors.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation and/or
 *    other materials provided with the distribution.
 *
 * 3. The buyer of the "Kapi" API is granted the right to use this software
 *    as a dependency in their own software projects. However, the buyer
 *    may not resell or distribute the "Kapi" API, in whole or in part, to other parties.
 *
 * 4. The buyer may include the "Kapi" API in a "fat jar" along with their own code.
 *    The license for the "fat jar" is at the buyer's discretion and may allow
 *    redistribution of the "fat jar", but the "Kapi" API code inside the "fat jar"
 *    must not be modified.
 *
 * 5. Neither the name "Kapi" nor the names of its contributors may be used to endorse
 *    or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY "Kapi" API, AND ITS CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL "Kapi" API, AND CONTRIBUTORS
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Kapi Contributors:
 * - Kyren223
 */

package me.kyren223.kapi.utility;

import me.kyren223.kapi.annotations.Kapi;
import me.kyren223.kapi.core.Kplugin;
import me.kyren223.kapi.data.TimeUnit;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * A utility builder class that includes a bunch of useful methods for running tasks.
 */
@Kapi
@SuppressWarnings("JavadocDeclaration")
public class Task {
    
    private final Consumer<TaskData> task;
    private Consumer<TaskEndData> onEnd;
    private Predicate<TaskData> condition;
    private int delay;
    private int interval;
    private int effectiveDuration;
    private int totalDuration;
    private int times;
    
    private Task(Consumer<TaskData> task) {
        this.task = task;
        condition = null;
        onEnd = null;
        delay = 1;
        interval = -1;
        effectiveDuration = -1;
        totalDuration = -1;
        times = -1;
    }
    
    /**
     * Creates a new task with the specified task consumer.<br>
     * The initial state of the task is equivalent to the following:<br>
     * Task.delay(1).schedule();<br>
     * Which means the task will run once on the next server tick.
     *
     * @param task The task consumer
     * @return The task builder for chaining
     * @see #run(Runnable)
     */
    @Kapi
    public static Task run(Consumer<TaskData> task) {
        return new Task(task);
    }
    
    /**
     * Creates a new task with the specified task runnable.<br>
     * The initial state of the task is equivalent to the following:<br>
     * Task.delay(1).schedule();<br>
     * Which means the task will run once on the next server tick.
     *
     * @param task The task runnable
     * @return The task builder for chaining
     * @see #run(Consumer)
     */
    @Kapi
    public static Task run(Runnable task) {
        return Task.run(t -> task.run());
    }
    
    /**
     * Sets the initial delay for the task.<br>
     * Note: tasks cannot run before the next tick, so any values less than 1 will be treated as 1.
     *
     * @param delayInTicks The delay in ticks
     * @return The task builder for chaining
     * @see #delay(int, TimeUnit)
     */
    @Kapi
    public Task delay(int delayInTicks) {
        this.delay = delayInTicks;
        return this;
    }
    
    /**
     * Sets the initial delay for the task.<br>
     * Note: tasks cannot run before the next tick, so any values less than 1 will be treated as 1.<br>
     * <br>
     * Note: the maximum number of ticks is {@link Integer#MAX_VALUE},
     * which is about 1242 days.
     *
     * @param delay The delay
     * @param timeUnit The time unit of the delay
     * @return The task builder for chaining
     * @throws IllegalArgumentException if the delay is less than 1 or more than {@link Integer#MAX_VALUE} ticks
     * @see #delay(int)
     */
    @Kapi
    public Task delay(int delay, TimeUnit timeUnit) {
        long ticks = timeUnit.toTicks(delay);
        if (ticks < 1) {
            throw new IllegalArgumentException("Delay cannot be less than 1");
        } else if (ticks > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Delay cannot be more than " + Integer.MAX_VALUE + " ticks");
        }
        return delay((int) ticks);
    }
    
    /**
     * Sets the interval for the task to 1 tick.
     *
     * @return The task builder for chaining
     * @see #interval(int)
     * @see #interval(int, TimeUnit)
     */
    @Kapi
    public Task interval() {
        return interval(1);
    }
    
    /**
     * Sets the interval for the task.
     *
     * @param intervalInTicks The interval in ticks
     * @return The task builder for chaining
     * @throws IllegalArgumentException if the interval is less than 1
     * @see #interval(int, TimeUnit)
     * @see #interval()
     */
    @Kapi
    public Task interval(int intervalInTicks) {
        if (intervalInTicks < 1) {
            throw new IllegalArgumentException("Interval cannot be less than 1");
        }
        this.interval = intervalInTicks;
        return this;
    }
    
    /**
     * Sets the interval for the task.
     *
     * @param interval The interval
     * @param timeUnit The time unit of the interval
     * @return The task builder for chaining
     * @throws IllegalArgumentException if the interval is less than 1 or more than {@link Integer#MAX_VALUE} ticks
     * @see #interval(int)
     * @see #interval()
     */
    @Kapi
    public Task interval(int interval, TimeUnit timeUnit) {
        long ticks = timeUnit.toTicks(interval);
        if (ticks < 1) {
            throw new IllegalArgumentException("Interval cannot be less than 1");
        } else if (ticks > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Interval cannot be more than " + Integer.MAX_VALUE + " ticks");
        }
        return interval((int) ticks);
    }
    
    /**
     * Sets the delay and interval for the task.<br>
     * Identical to calling delay(delay).interval(interval).
     *
     * @param delay The delay in ticks
     * @param interval The interval in ticks
     * @return The task builder for chaining
     * @see #delay(int)
     * @see #interval(int)
     * @see #timer(int, int, TimeUnit)
     */
    @Kapi
    public Task timer(int delay, int interval) {
        return delay(delay).interval(interval);
    }
    
    /**
     * Sets the delay and interval for the task.<br>
     * Identical to calling delay(delay, timeUnit).interval(interval, timeUnit).
     *
     * @param delay The delay
     * @param interval The interval
     * @param timeUnit The time unit of the delay and interval
     * @return The task builder for chaining
     * @see #delay(int, TimeUnit)
     * @see #interval(int, TimeUnit)
     * @see #timer(int, int)
     */
    @Kapi
    public Task timer(int delay, int interval, TimeUnit timeUnit) {
        return delay(delay, timeUnit).interval(interval, timeUnit);
    }
    
    /**
     * Sets the condition for the task.<br>
     * The task will stop running when the condition is false.
     *
     * @param condition The condition
     * @return The task builder for chaining
     * @see #whileCondition(BooleanSupplier)
     * @see #doWhileCondition(Predicate)
     * @see #doWhileCondition(BooleanSupplier)
     */
    @Kapi
    public Task whileCondition(Predicate<TaskData> condition) {
        this.condition = condition;
        return this;
    }
    
    /**
     * Sets the condition for the task.<br>
     * The task will stop running when the condition is false.
     *
     * @param condition The condition
     * @return The task builder for chaining
     * @see #whileCondition(Predicate)
     * @see #doWhileCondition(BooleanSupplier)
     * @see #doWhileCondition(Predicate)
     */
    @Kapi
    public Task whileCondition(BooleanSupplier condition) {
        this.condition = t -> condition.getAsBoolean();
        return this;
    }
    
    /**
     * Sets the condition for the task.<br>
     * The task will stop running when the condition is false.<br>
     * Guaranteed to run at least once.
     *
     * @param condition The condition
     * @return The task builder for chaining
     * @see #whileCondition(BooleanSupplier)
     * @see #whileCondition(Predicate)
     * @see #doWhileCondition(BooleanSupplier)
     */
    @Kapi
    public Task doWhileCondition(Predicate<TaskData> condition) {
        this.condition = t -> {
            if (t.getTimesRan() == 0) return true;
            return condition.test(t);
        };
        return this;
    }
    
    /**
     * Sets the condition for the task.<br>
     * The task will stop running when the condition is false.<br>
     * Guaranteed to run at least once.
     *
     * @param condition The condition
     * @return The task builder for chaining
     * @see #whileCondition(Predicate)
     * @see #whileCondition(BooleanSupplier)
     * @see #doWhileCondition(Predicate)
     */
    @Kapi
    public Task doWhileCondition(BooleanSupplier condition) {
        this.condition = t -> {
            if (t.getTimesRan() == 0) return true;
            return condition.getAsBoolean();
        };
        return this;
    }
    
    
    /**
     * Sets the onEnd consumer for the task.<br>
     * This consumer will be executed immediately after (on the same tick) when the task ends.
     *
     * @param onEnd The onEnd consumer
     * @return The task builder for chaining
     * @see #onEnd(Runnable)
     */
    @Kapi
    public Task onEnd(Consumer<TaskEndData> onEnd) {
        this.onEnd = onEnd;
        return this;
    }
    
    /**
     * Sets the onEnd runnable for the task.<br>
     * This runnable will be executed immediately after (on the same tick) when the task ends.
     *
     * @param onEnd The onEnd runnable
     * @return The task builder for chaining
     * @see #onEnd(Consumer)
     */
    @Kapi
    public Task onEnd(Runnable onEnd) {
        this.onEnd = t -> onEnd.run();
        return this;
    }
    
    /**
     * Sets the effective duration for the task.<br>
     * Effective duration is the duration of the task after the initial delay.<br>
     * <br>
     * This method is one of the 5 methods that control the duration of the task.<br>
     * - {@link #effectiveDuration(int)}<br>
     * - {@link #effectiveDuration(int, TimeUnit)}<br>
     * - {@link #totalDuration(int)}<br>
     * - {@link #totalDuration(int, TimeUnit)}<br>
     * - {@link #repeat(int)}<br>
     * If you call more than one of these methods, only the last one will take effect, the rest will be ignored.
     *
     * @param durationInTicks The duration in ticks
     * @return The task builder for chaining
     * @see #effectiveDuration(int, TimeUnit)
     */
    @Kapi
    public Task effectiveDuration(int durationInTicks) {
        this.times = -1;
        this.totalDuration = -1;
        this.effectiveDuration = durationInTicks;
        return this;
    }
    
    /**
     * Sets the effective duration for the task.<br>
     * Effective duration is the duration of the task after the initial delay.<br>
     * <br>
     * This method is one of the 5 methods that control the duration of the task.<br>
     * - {@link #effectiveDuration(int)}<br>
     * - {@link #effectiveDuration(int, TimeUnit)}<br>
     * - {@link #totalDuration(int)}<br>
     * - {@link #totalDuration(int, TimeUnit)}<br>
     * - {@link #repeat(int)}<br>
     * If you call more than one of these methods, only the last one will take effect, the rest will be ignored.
     *
     * @param duration The duration
     * @param timeUnit The time unit of the duration
     * @return The task builder for chaining
     * @throws IllegalArgumentException if the duration is less than 1 or more than {@link Integer#MAX_VALUE} ticks
     * @see #effectiveDuration(int)
     */
    @Kapi
    public Task effectiveDuration(int duration, TimeUnit timeUnit) {
        long ticks = timeUnit.toTicks(duration);
        if (ticks < 1) {
            throw new IllegalArgumentException("Duration cannot be less than 1");
        } else if (ticks > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Duration cannot be more than " + Integer.MAX_VALUE + " ticks");
        }
        return effectiveDuration((int) ticks);
    }
    
    /**
     * Sets the total duration for the task.<br>
     * Total duration is the duration of the task including the initial delay.<br>
     * <br>
     * This method is one of the 5 methods that control the duration of the task.<br>
     * - {@link #effectiveDuration(int)}<br>
     * - {@link #effectiveDuration(int, TimeUnit)}<br>
     * - {@link #totalDuration(int)}<br>
     * - {@link #totalDuration(int, TimeUnit)}<br>
     * - {@link #repeat(int)}<br>
     * If you call more than one of these methods, only the last one will take effect, the rest will be ignored.
     *
     * @param durationInTicks The duration in ticks
     * @return The task builder for chaining
     * @see #totalDuration(int, TimeUnit)
     */
    @Kapi
    public Task totalDuration(int durationInTicks) {
        this.times = -1;
        this.effectiveDuration = -1;
        this.totalDuration = durationInTicks;
        return this;
    }
    
    /**
     * Sets the total duration for the task.<br>
     * Total duration is the duration of the task including the initial delay.<br>
     * <br>
     * This method is one of the 5 methods that control the duration of the task.<br>
     * - {@link #effectiveDuration(int)}<br>
     * - {@link #effectiveDuration(int, TimeUnit)}<br>
     * - {@link #totalDuration(int)}<br>
     * - {@link #totalDuration(int, TimeUnit)}<br>
     * - {@link #repeat(int)}<br>
     * If you call more than one of these methods, only the last one will take effect, the rest will be ignored.
     *
     * @param duration The duration
     * @param timeUnit The time unit of the duration
     * @return The task builder for chaining
     * @throws IllegalArgumentException if the duration is less than 1 or more than {@link Integer#MAX_VALUE} ticks
     * @see #totalDuration(int, TimeUnit)
     */
    @Kapi
    public Task totalDuration(int duration, TimeUnit timeUnit) {
        long ticks = timeUnit.toTicks(duration);
        if (ticks < 1) {
            throw new IllegalArgumentException("Duration cannot be less than 1");
        } else if (ticks > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Duration cannot be more than " + Integer.MAX_VALUE + " ticks");
        }
        return totalDuration((int) ticks);
    }
    
    /**
     * Sets the number of times to repeat the task.<br>
     * The task will stop running after the specified number of times.<br>
     * <br>
     * This method is one of the 5 methods that control the duration of the task.<br>
     * - {@link #effectiveDuration(int)}<br>
     * - {@link #effectiveDuration(int, TimeUnit)}<br>
     * - {@link #totalDuration(int)}<br>
     * - {@link #totalDuration(int, TimeUnit)}<br>
     * - {@link #repeat(int)}<br>
     * If you call more than one of these methods, only the last one will take effect, the rest will be ignored.
     *
     * @param times The number of times to repeat the task
     * @return The task builder for chaining
     */
    @Kapi
    public Task repeat(int times) {
        this.effectiveDuration = -1;
        this.totalDuration = -1;
        this.times = times;
        return this;
    }
    
    /**
     * Schedules a task with the specified parameters to the server task scheduler.<br>
     * <br>
     * If effective duration, total duration or a repeat count is set,
     * but the interval is not set, the interval will be set to 1 tick.<br>
     * <br>
     * If the delay is less than 1 tick, it will be treated as 1 tick and the task will run on the next tick.<br>
     * The earliest a task can run is on the next tick.<br>
     * <br>
     * In the case of a task ending "naturally" or being cancelled,
     * the onEnd consumer (if exists) will be executed, and the task will not run again.<br>
     * <br>
     * The task uses runTaskTimer if an interval is set, otherwise it uses runTaskLater.<br>
     */
    @Kapi
    public void schedule() {
        if (interval == -1 && (times != -1 || effectiveDuration != -1 || totalDuration != -1)) {
            // If the interval is not set, but the duration is set, set the interval to 1 tick
            interval();
        }
        
        BukkitScheduler scheduler = Kplugin.get().getServer().getScheduler();
        TaskData data = new TaskData(delay, interval);
        AtomicInteger taskId = new AtomicInteger();
        Runnable task = () -> {
            boolean meetsCondition = condition == null || condition.test(data);
            if (meetsCondition) {
                this.task.accept(data);
            }
            
            data.timesRan++;
            data.effectiveDuration += data.interval;
            data.totalDuration += data.interval;
            
            boolean shouldEnd = shouldEnd(data, times, effectiveDuration, totalDuration);
            if (!meetsCondition || data.cancel || shouldEnd) {
                scheduler.cancelTask(taskId.get());
                if (onEnd != null) {
                    onEnd.accept(new TaskEndData(data, data.cancel));
                }
            }
        };
        
        if (interval == -1) {
            taskId.set(scheduler.runTaskLater(Kplugin.get(), task, delay).getTaskId());
        } else {
            taskId.set(scheduler.runTaskTimer(Kplugin.get(), task, delay, interval).getTaskId());
        }
    }
    
    private static boolean shouldEnd(TaskData data, int times, int effectiveDuration, int totalDuration) {
        boolean repeatEnd = times != -1 && data.timesRan >= times;
        boolean effectiveDurationEnd = effectiveDuration != -1 && data.effectiveDuration >= effectiveDuration;
        boolean totalDurationEnd = totalDuration != -1 && data.totalDuration >= totalDuration;
        return repeatEnd || effectiveDurationEnd || totalDurationEnd;
    }
    
    /**
     * Task data that is passed to the task consumer.
     */
    @Kapi
    public static class TaskData {
        private final int delay;
        private final int interval;
        private boolean cancel;
        private int timesRan;
        private int effectiveDuration;
        private int totalDuration;
        
        private TaskData(int delay, int interval) {
            this.delay = delay;
            this.interval = interval;
            this.effectiveDuration = 0;
            this.totalDuration = delay;
            this.timesRan = 0;
            this.cancel = false;
        }
        
        @Kapi
        public int getDelay() {
            return delay;
        }
        
        @Kapi
        public int getInterval() {
            return interval;
        }
        
        /**
         * Gets the number of times the task has run.<br>
         * This does not include the current run, so the value will be 0 for the first run.
         *
         * @return The number of times the task has run
         */
        @Kapi
        public int getTimesRan() {
            return timesRan;
        }
        
        /**
         * Gets the effective duration of the task, which is the duration of the task after the initial delay.
         *
         * @return The effective duration of the task
         */
        @Kapi
        public int getEffectiveDuration() {
            return effectiveDuration;
        }
        
        /**
         * Gets the total duration of the task including the initial delay.
         *
         * @return The total duration of the task
         */
        @Kapi
        public int getTotalDuration() {
            return totalDuration;
        }
        
        /**
         * Cancels the task.
         */
        @Kapi
        public void cancel() {
            cancel = true;
        }
    }
    
    /**
     * Task end data that is passed to the onEnd consumer.
     */
    @Kapi
    public static class TaskEndData {
        private final TaskData taskData;
        private final boolean cancelled;
        
        private TaskEndData(TaskData taskData, boolean cancelled) {
            this.taskData = taskData;
            this.cancelled = cancelled;
        }
        
        /**
         * Returns true if the task was cancelled, otherwise false.<br>
         * A task is considered cancelled if the task consumer called {@link TaskData#cancel()}.<br>
         * The task is not considered cancelled if the task ended naturally due to the duration or condition.
         *
         * @return If the task was cancelled
         */
        @Kapi
        public boolean isCancelled() {
            return cancelled;
        }
        
        @Kapi
        public int getDelay() {
            return taskData.getDelay();
        }
        
        @Kapi
        public int getInterval() {
            return taskData.getInterval();
        }
        
        /**
         * Gets the number of times the task has run.<br>
         * This does not include the current run, so the value will be 0 for the first run.
         *
         * @return The number of times the task has run
         */
        @Kapi
        public int getTimesRan() {
            return taskData.getTimesRan();
        }
        
        /**
         * Gets the effective duration of the task, which is the duration of the task after the initial delay.
         *
         * @return The effective duration of the task
         */
        @Kapi
        public int getEffectiveDuration() {
            return taskData.getEffectiveDuration();
        }
        
        /**
         * Gets the total duration of the task including the initial delay.
         *
         * @return The total duration of the task
         */
        @Kapi
        public int getTotalDuration() {
            return taskData.getTotalDuration();
        }
    }
}

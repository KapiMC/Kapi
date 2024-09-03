/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the AGPLv3 license. See LICENSE or https://www.gnu.org/licenses/agpl-3.0 for details.
 */

package io.github.kapimc.kapi.utility;

import io.github.kapimc.kapi.annotations.Kapi;
import io.github.kapimc.kapi.data.Option;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

/**
 * A utility interface to interact with a running task.
 * <p>
 * To create tasks, use {@link TaskBuilder}.
 */
@Kapi
public sealed interface KapiTask permits TaskBuilder.KapiTaskImpl {
    
    /**
     * @return a unique task id, as returned by {@link BukkitTask#getTaskId()},
     */
    @Kapi
    int getTaskId();
    
    /**
     * @return the plugin that owns this task, as returned by {@link BukkitTask#getOwner()},
     */
    @Kapi
    Plugin getOwner();
    
    /**
     * @return true if this task is run by the main thread, as returned by {@link BukkitTask#isSync()},
     */
    @Kapi
    boolean isSync();
    
    /**
     * @return true if this task has been cancelled, as returned by {@link BukkitTask#isCancelled()},
     */
    @Kapi
    boolean isCancelled();
    
    /**
     * Cancels this task. No further tasks will be run.
     * Calls {@code onFinish()} if it exists.
     * <p>
     * Calling this on an already cancelled task has no effect.
     */
    @Kapi
    void cancel();
    
    /**
     * @return the initial delay, as was specified when the task was created.
     */
    @Kapi
    long getDelay();
    
    /**
     * @return the interval, as was specified when the task was created, or None if there is no interval.
     */
    @Kapi
    Option<Long> getInterval();
    
    /**
     * @return the number of times this task has been run,
     *     excluding the current run, the first run is 0.
     */
    @Kapi
    long getTimesRan();
    
    /**
     * For a task with no interval, this will always return 0.
     *
     * @return the number of server ticks, excluding the initial delay, that this task has been running for.
     */
    @Kapi
    long getDuration();
    
}

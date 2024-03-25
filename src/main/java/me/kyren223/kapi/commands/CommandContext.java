package me.kyren223.kapi.commands;

import me.kyren223.kapi.data.Pair;
import me.kyren223.kapi.data.Result;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class CommandContext {
    
    protected ArgumentBuilder<?> argumentBuilder;
    private final BiConsumer<CommandContext, String> failureHandler;
    
    private final CommandSender sender;
    private final Command command;
    private final String label;
    private final String[] args;
    
    private final Map<String, Object> arguments;
    private boolean cancelled;
    private int currentArgumentIndex;
    
    public CommandContext(
            ArgumentBuilder<CommandBuilder> argumentBuilder,
            BiConsumer<CommandContext, String> failureHandler,
            CommandSender sender, Command command, String label, String[] args
    ) {
        this.argumentBuilder = argumentBuilder;
        this.failureHandler = failureHandler;
        
        this.sender = sender;
        this.command = command;
        this.label = label;
        this.args = args;
        
        this.arguments = new LinkedHashMap<>();
        this.cancelled = false;
        this.currentArgumentIndex = 0;
    }
    
    public CommandSender getSender() {
        return sender;
    }
    
    public Command getCommand() {
        return command;
    }
    
    public String getLabel() {
        return label;
    }
    
    public String[] getArgs() {
        return args;
    }
    
    public int getArgsCount() {
        return args.length;
    }
    
    public String getArg(int index) {
        return args[index];
    }
    
    public boolean isSenderPlayer() {
        return sender instanceof Player;
    }
    
    public Result<Player, String> getPlayer() {
        if (sender instanceof Player player) {
            return Result.ok(player);
        } else {
            return Result.err("Sender is not a player!");
        }
    }
    
    public Player getPlayerOrNull() {
        if (sender instanceof Player player) {
            return player;
        } else {
            return null;
        }
    }
    
    public Player getPlayerOrThrow() {
        if (sender instanceof Player player) {
            return player;
        } else {
            throw new IllegalStateException("Sender is not a player!");
        }
    }
    
    public <T> Result<T, String> getArg(String name, Class<T> clazz) {
        if (!arguments.containsKey(name)) {
            return Result.err("Argument not found: " + name);
        }
        
        Object value = arguments.get(name);
        if (value != null && !clazz.isInstance(value)) {
            return Result.err("Invalid type for argument: " + name);
        }
        
        return Result.ok(clazz.cast(value));
    }
    
    public boolean isCancelled() {
        return cancelled;
    }
    
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
    
    protected <T extends CommandContext> boolean process(Consumer<T> executor, T context) {
        boolean failed = checkRequirements(context);
        if (failed) return false;
        
        // Execution / Suggestion
        executor.accept(context);
        if (isCancelled()) return false;
        
        // Sets the builder to the next argument
        failed = processNextArgument(context);
        if (failed) return false;
        
        // Process next argument
        currentArgumentIndex++;
        return true;
    }
    
    private <T extends CommandContext> boolean checkRequirements(T context) {
        argumentBuilder.getRequirements().forEach(pair -> {
            String errorMessage = pair.first;
            Predicate<CommandContext> requirement = pair.second;
            if (requirement.test(context)) return;
            failureHandler.accept(this, errorMessage);
            setCancelled(true);
        });
        return isCancelled();
    }
    
    private <T extends CommandContext> boolean processNextArgument(T context) {
        ArgumentBuilder<?> nextArgumentBuilder = null;
        String firstError = null;
        
        for (Pair<ArgumentType<?>, String> key : argumentBuilder.getArgs().keySet()) {
            ArgumentType<?> type = key.first;
            String name = key.second;
            ArgumentBuilder<?> argumentBuilder = this.argumentBuilder.getArgs().get(key);
            
            Result<?, String> result = type.parse(context.getArg(currentArgumentIndex));
            if (result.isErr()) {
                if (firstError == null) {
                    firstError = result.getErr();
                }
                continue;
            }
            
            arguments.put(name, result.getOk());
            nextArgumentBuilder = argumentBuilder;
            break;
        }
        
        // If no argument was parsed successfully
        if (nextArgumentBuilder == null) {
            /*
            IMPROVEME
            Consider the case where multiple arguments fail to parse successfully.
            In this case, the error message will be the first error that occurred.
            But it would be better to show all errors at once.
            
            Also instead of "Unknown error", maybe check if it's missing an argument
            or if it has too many arguments.
             */
            if (firstError == null) {
                firstError = "Unknown error";
            }
            setCancelled(true);
            failureHandler.accept(this, firstError);
            return true;
        }
        
        argumentBuilder = nextArgumentBuilder;
        return false;
    }
}

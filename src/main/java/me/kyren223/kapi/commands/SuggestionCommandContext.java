package me.kyren223.kapi.commands;

import me.kyren223.kapi.annotations.Kapi;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Represents the context of a command during suggestion (TabCompleter).<br>
 * See {@link ExecutionCommandContext} for "CommandExecutor" context.<br>
 * See {@link CommandContext} for a general command context.
 */
@Kapi
public class SuggestionCommandContext extends CommandContext {
    
    private List<String> returnValue;
    private Consumer<SuggestionCommandContext> suggester;
    
    public SuggestionCommandContext(
            ArgumentBuilder<CommandBuilder> argumentBuilder,
            BiConsumer<CommandContext, String> failureHandler,
            CommandSender sender, Command command, String label, String[] args
    ) {
        super(argumentBuilder, failureHandler, sender, command, label, args);
        returnValue = new ArrayList<>();
        suggester = argumentBuilder.getSuggestion();
    }
    
    public List<String> getReturnValue() {
        return returnValue;
    }
    
    /**
     * Adds a suggestion to the list of suggestions.
     *
     * @param suggestion the suggestion
     */
    public void addSuggestion(String suggestion) {
        returnValue.add(suggestion);
    }
    
    public void process() {
        boolean shouldContinue = process(suggester, this);
        if (shouldContinue) {
            this.suggester = argumentBuilder.getSuggestion();
        }
    }
}

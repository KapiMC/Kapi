package me.kyren223.kapi.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

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
    
    public void setReturnValue(List<String> returnValue) {
        this.returnValue = returnValue;
    }
    
    public void process() {
        boolean shouldContinue = process(suggester, this);
        if (shouldContinue) {
            this.suggester = argumentBuilder.getSuggestion();
        }
    }
}

package me.kyren223.kapi.commands.builtin;

import me.kyren223.kapi.annotations.Kapi;
import me.kyren223.kapi.commands.ArgumentType;
import me.kyren223.kapi.commands.SuggestionCommandContext;
import me.kyren223.kapi.data.Result;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Predicate;

/**
 * Represents a command argument type for a double.
 */
@Kapi
public class DoubleArgumentType implements ArgumentType<Double> {
    
    private double min;
    private double max;
    private Predicate<Double> predicate;
    private String errorMessage;
    
    private DoubleArgumentType() {
        this.min = Double.MIN_VALUE;
        this.max = Double.MAX_VALUE;
        this.predicate = null;
        this.errorMessage = "Decimal %f is invalid";
    }
    
    /**
     * Creates a new double argument type.
     *
     * @return the double argument type
     */
    @Kapi
    public static DoubleArgumentType decimal() {
        return new DoubleArgumentType();
    }
    
    /**
     * Sets the minimum value (inclusive) for the double.
     *
     * @param min the minimum value
     * @return this, for chaining
     */
    @Kapi
    public DoubleArgumentType min(double min) {
        this.min = min;
        return this;
    }
    
    /**
     * Sets the maximum value (inclusive) for the double.
     *
     * @param max the maximum value
     * @return this, for chaining
     */
    @Kapi
    public DoubleArgumentType max(double max) {
        this.max = max;
        return this;
    }
    
    /**
     * Sets the range for the double.
     *
     * @param min the minimum value
     * @param max the maximum value
     * @return this, for chaining
     */
    @Kapi
    public DoubleArgumentType range(double min, double max) {
        this.min = min;
        this.max = max;
        return this;
    }
    
    /**
     * Sets the predicate for the double.<br>
     * The predicate should return true if the double is valid, false otherwise.<br>
     * See {@link #predicate(Predicate, String)} for a version with an error message.
     *
     * @param predicate the predicate
     * @return this, for chaining
     */
    @Kapi
    public DoubleArgumentType predicate(Predicate<Double> predicate) {
        this.predicate = predicate;
        return this;
    }
    
    /**
     * Sets the predicate for the double.<br>
     * The predicate should return true if the double is valid, false otherwise.<br>
     * See {@link #predicate(Predicate)} for a version without an error message.
     *
     * @param predicate    the predicate
     * @param errorMessage the error message if the predicate fails, use "%d" as a placeholder for the integer
     * @return this, for chaining
     */
    @Kapi
    public DoubleArgumentType predicate(Predicate<Double> predicate, @NotNull String errorMessage) {
        this.predicate = predicate;
        this.errorMessage = errorMessage;
        return this;
    }
    
    @Kapi
    @Override
    public Result<Double, String> parse(List<String> arguments) {
        String input = arguments.remove(0);
        double value;
        try {
            value = Double.parseDouble(input);
        } catch (NumberFormatException e) {
            return Result.err("Invalid decimal: " + input);
        }
        
        if (value < min) {
            return Result.err(String.format("Decimal %f is less than the minimum value %f", value, min));
        } else if (value > max) {
            return Result.err(String.format("Decimal %f is greater than the maximum value %f", value, max));
        } else if (predicate != null && !predicate.test(value)) {
            return Result.err("Decimal " + value + " is invalid");
        } else {
            return Result.ok(value);
        }
    }
    
    @Kapi
    @Override
    public void getSuggestions(SuggestionCommandContext context) {
        // Do nothing
    }
}

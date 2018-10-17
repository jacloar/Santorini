/**
 *
 * This denotes an AI based actor. It is important to note
 * that it will be able to exercise the methods on IActor by
 * querying it's "Strategy" along with the ReadableBoard implementation
 * to come up with turns and placements. Separating the IStrategy from the Computer Actor
 * means different algorithms can be cimplemented as IStrategies, but they reuse the same
 * ComputerActor abstract class to embody the IActorm interface
 *
 */
public abstract class ComputerActor implements IActor {
    // This IStrategy used by this computer actor to make decisions
    private final IStrategy strategy;
    private final String name;

    // accept the IStrategy in the constructor
    public ComputerActor(IStrategy strategy) {
        this.strategy = strategy;

        // Formulate random name and set
        String uuid = util.UUID.randomUUID().toString();
        this.name = String.format("%s:%s", uuid, strategy.toString());
    }
}
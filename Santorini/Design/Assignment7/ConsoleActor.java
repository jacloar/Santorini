/**
 *
 * Basic implementation for an actor that would communicate via the command line
 * Utilized System.in and System.out to set up the streams.
 *
 */
public abstract class ConsoleActor extends StreamActor {
    public ConsoleActor() {
        super(System.in, System.out);
    }
}
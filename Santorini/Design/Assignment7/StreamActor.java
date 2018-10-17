/**
 *
 * Basic actor that can act off of both an input stream and an output stream.
 * This is important for both a ConsoleActor and NetworkActor to extend as they both interact
 * with streams the same way by getting input and output, however they will have their streams
 * come from different sources
 *
 */
public abstract class StreamActor implements IActor {
    // the InputStraem to read from
    private final InputStream inputStream;
    // the OutPutStream to write to
    private final OutputStream outputStream;
    // the name the player using this actor provides
    private final String name;

    // the Input and Output streams are provided in the constructor
    public StreamActor(InputStream in, OutputStream out) {
        this.inputStream = in;
        this.outputStream = out;

        // We will ask for the name within the construction
        // We will get the name and validate it (if we need to)
        // this will finalize construction of an StreamActor
    }
}
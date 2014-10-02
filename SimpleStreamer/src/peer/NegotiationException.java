package peer;
// Thrown when problem with negotiations

@SuppressWarnings("serial")
public class NegotiationException extends Exception {
	NegotiationException(String string) {
		super(string);
	}
}
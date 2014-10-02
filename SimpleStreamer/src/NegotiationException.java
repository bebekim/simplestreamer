// Thrown when problem with negotiations

@SuppressWarnings("serial")
public class NegotiationException extends Exception {
	NegotiationException(Exception e) {
		super(e);
	}
}
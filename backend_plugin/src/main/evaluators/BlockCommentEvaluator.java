package main.evaluators;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;

/**
 * Evaluates DocumentEvent changes to determine if the user is commenting out multiple sequential lines of code. If so,
 * then the user should be notified of the block comment feature in Eclipse.
 */
public class BlockCommentEvaluator extends FeatureEvaluator {

	private int lastCommentedLine;
	private long lastCommentedLineTimeStamp;
		
	/**
	 * Default constructor
	 */
	public BlockCommentEvaluator(IDocument document) {
		this.featureID = "blockCommentSuggestion";
		this.document = document;
		this.lastCommentedLine = -2;
		this.lastCommentedLineTimeStamp = -1;
	}

	/**
	 * Keeps track of DocumentEvent changes and determines of the user comments out multiple sequential lines of code. 
	 * @param event the change detected by the DocumentChange Listener
	 * @return true if the user comments two sequential lines of code, false otherwise
	 */
	public boolean evaluateDocumentChanges(DocumentEvent event) {
		try {
			
			boolean triggered = false;
			
			// Get the document the user is typing in
//			IDocument document = event.getDocument();

			// Get the line number of the change
			int line = document.getLineOfOffset(event.getOffset());

			// Check if the line is newly commented out
			if (newCommentDetected(document, event, line)) {

				// The user commented out the line. Check and see if they previously commented out an adjacent line
				// manually
				triggered = adjacentToLastCommentedLine(line);

				// Since the line has been commented out, update the information about the last commented line
				this.lastCommentedLine = line;
				this.lastCommentedLineTimeStamp = System.currentTimeMillis();
			}
			
			return triggered;
		} catch (BadLocationException e) {
			// This can happen in certain boundary positions (beginning and end) of the document. In these cases,
			// just do nothing and return false
			return false;
		}
	}

	/**
	 * Checks if the given line is commented out in the given document
	 * @param document The document the user is typing in
	 * @param line the number of the line to check
	 * @return true if the given line is commented out; false otherwise
	 */
	private boolean lineIsCommentedOut(IDocument document, int line) {
		try {
			// Check if the given line minus white space starts with a double slash
			int lineOffset = document.getLineOffset(line);
			int lineLength = document.getLineLength(line);
			String lineText = document.get(lineOffset, lineLength).trim();
			return lineText.startsWith("//");
		} catch (BadLocationException e) {
			return false;
		}
	}

	/**
	 * Checks if the line the addition was made on was already commented out
	 * @param document The document the user is typing in
	 * @param event The document change event
	 * @param line the line number the event occurred on
	 * @return boolean true if the line was already commented out before the change; false otherwise
	 */
	public boolean lineWasPreviouslyCommentedOut(IDocument document, DocumentEvent event, int line) {
		try {
			// Calculate the contents of the line that previously existed in front of the new insertion and
			// the contents of the line that previously existed beyond the new insertion
			int lineStartOffset = document.getLineOffset(line);
			int lineEndOffset = lineStartOffset + document.getLineLength(line);
			int insertOffset = event.getOffset();
			String lineBeforeNewAddition = document.get(lineStartOffset, insertOffset - lineStartOffset).trim();
			String lineAfterNewAddition = document.get(insertOffset + event.getText().length(), lineEndOffset - insertOffset - 1).trim();

			// Check all cases (an insertion could come before an existing double slash, after an existing
			// double slash, or between the slashes of an existing double slash), each indication that
			// the line was already commented out before
			return lineBeforeNewAddition.startsWith("//") ||
					(lineBeforeNewAddition.equals("") && lineAfterNewAddition.startsWith("//")) ||
					(lineBeforeNewAddition.equals("/") && lineAfterNewAddition.startsWith("/"));
		} catch (BadLocationException e) {
			return false;
		}
	}

	/**
	 * Checks if the last line that was commented out is adjacent to the given line. Also checks that
	 * the given line was commented out manually, rather than using the block comment feature
	 * @param line The line number to check
	 * @return true of the given line is adjacent to the last commented out line; false otherwise
	 */
	public boolean adjacentToLastCommentedLine(int line) {

		// Check that the last line that was commented out is adjacent to the given line
		boolean adjacentLineWasLastCommented = Math.abs(line - this.lastCommentedLine) == 1;

		// Check that enough time has passed since the last time a line was commented out.
		// This will prevent triggering when the user actually does use the block comment feature
		// by using a millisecond threshold
		boolean lastCommentWasLongEnoughAgo = System.currentTimeMillis() - this.lastCommentedLineTimeStamp > 100;
		return adjacentLineWasLastCommented && lastCommentWasLongEnoughAgo;
	}
	
	/**
	 * Checks if the given event caused the given line to be commented out
	 * @param document The document the user is typing in
	 * @param event The document change event
	 * @param line The line number the event occurred on
	 * @return
	 */
	public boolean newCommentDetected(IDocument document, DocumentEvent event, int line) {
		return lineIsCommentedOut(document, line) && !lineWasPreviouslyCommentedOut(document, event, line);
	}
}

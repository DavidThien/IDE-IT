package main.evaluators;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;

import main.interfaces.FeatureID;

/**
 * Evaluates DocumentEvent changes to determine if the user is changing the indentation of
 * multiple sequential lines of code. If so, then the user should be notified of the auto
 * indentation feature in Eclipse.
 */
public class CorrectIndentationEvaluator extends FeatureEvaluator {
	private String lineBeforeChange;
	private boolean whiteSpaceAddedOrRemoved;
	private int lastIndentChangedLine;
	private long lastIndentChangedLineTimeStamp;

	private final int INVALID_LINE = -2;

	/**
	 * Constructor
	 * @param document IDocument that this evaluator is attached to
	 */
	public CorrectIndentationEvaluator(IDocument document) {
		this.featureID = FeatureID.CORRECT_INDENTATION_FEATURE_ID;
		this.document = document;

		// Arbitrary default values to avoid special casing for the first document change
		lineBeforeChange = "";
		lastIndentChangedLine = INVALID_LINE;  // can't be -1 because we may change indent in line 0
		whiteSpaceAddedOrRemoved = false;
		this.lastIndentChangedLineTimeStamp = -1;
	}

	/**
	 * If the text about to be added to the document is either whitespace or nothing,
	 * then save the line of code in a string to reference later
	 * @param event The document change data
	 */
	@Override
	public boolean evaluateDocumentBeforeChange(DocumentEvent event) {

		// We only care about adding white space or removing characters
		// Either of those cases will have a length of 0 once the string is trimmed
		try {
			int line = document.getLineOfOffset(event.getOffset());
			if (event.getText().trim().length() == 0) {
				int lineOffset = document.getLineOffset(line);
				int lineLength = document.getLineLength(line);
				lineBeforeChange = document.get(lineOffset, lineLength);
				whiteSpaceAddedOrRemoved = true;
			} else {
				whiteSpaceAddedOrRemoved = false;
			}
		} catch (BadLocationException e) {}
		return false;
	}

	/**
	 * If the change added white space or removed some character then compare
	 * the line of code before the change and after the change to determine if
	 * whitespace was added or removed at the start of the line
	 * @param event The document change data
	 */
	@Override
	public boolean evaluateDocumentChanges(DocumentEvent event) {
		try {

			// Since we already checked that we are only adding whitespace or removing text, we just
			// check the boolean
			int line = document.getLineOfOffset(event.getOffset());
			if (whiteSpaceAddedOrRemoved) {
				if (lineIndentationChanged(event, line) && lineAdjacentToPrevious(line)) {
					return true;
				}
			} else {

				// If white space wasn't added or removed in this line, then reset the lastIndentChangedLine
				// if we made non-white spaces to it
				lastIndentChangedLine = (lastIndentChangedLine == line ||
						lastIndentChangedLine == INVALID_LINE) ? INVALID_LINE : line;
			}
		} catch (BadLocationException e) {}
		return false;
	}

	/**
	 * Checks that the only change to the document line was the addition or removal of white space
	 * @param event the DocumentEvent that occurred
	 * @param line the line number where the change occurred
	 * @return true if white space was added or removed to the front of the line, false otherwise
	 */
	private boolean lineIndentationChanged(DocumentEvent event, int line) {
		try {

			// If both before and after are identical after trim and the starting
			// character is different, then we have a changed indentation
			int lineOffset = document.getLineOffset(line);
			int lineLength = document.getLineLength(line);
			String lineAfterChange = document.get(lineOffset, lineLength);

			if (lineAfterChange.trim().length() == lineBeforeChange.trim().length() &&
					(getOffsetOfFirstCharInLine(lineAfterChange) != getOffsetOfFirstCharInLine(lineBeforeChange))) {
				return true;
			}
		} catch (BadLocationException e) {}
		return false;
	}

	/**
	 * Returns the position of the first non-white space character in the provided string
	 * @param text the string to find the first non-white space character in
	 * @return the position of the first non-white space character, or -2 if no such character was found
	 */
	private int getOffsetOfFirstCharInLine(String text) {

		// Iterate through until we hit something that's not " " or "\t"
		for (int i = 0; i < text.length(); i++) {
			if (!Character.isWhitespace(text.charAt(i))) {
				return i;
			}
		}

		// Can't return -1 since it is adjacent to 0
		return -2;
	}

	/**
	 * Checks if the previously indented line is adjacent to the current indent changed line
	 * Also updates lastIndentChangedLine
	 * @param line The number of the currently indent changed line
	 * @return true if the lines are adjacent, false otherwise
	 */
	private boolean lineAdjacentToPrevious(int line) {

		// Check that the last line that was commented out is adjacent to the given line
		boolean adjacentLineWasLastCommented = Math.abs(line - this.lastIndentChangedLine) == 1;

		// Check that enough time has passed since the last time a line was commented out.
		// This will prevent triggering when the user actually does use the block comment feature
		// by using a millisecond threshold
		boolean lastCommentWasLongEnoughAgo = System.currentTimeMillis() - this.lastIndentChangedLineTimeStamp > 100;

		// Update the stored values of the last indent changed line
		this.lastIndentChangedLine = line;
		this.lastIndentChangedLineTimeStamp = System.currentTimeMillis();

		return adjacentLineWasLastCommented && lastCommentWasLongEnoughAgo;
	}
}

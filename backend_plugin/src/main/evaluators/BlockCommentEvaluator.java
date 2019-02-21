package main.evaluators;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;

/**
 * Evaluates DocumentEvent changes to determine if the user is commenting out multiple sequential lines of code. If so,
 * then the user should be notified of the block comment feature in Eclipse.
 */
public class BlockCommentEvaluator {
		
	private boolean firstDoubleBackSlashDetected;
	private IRegion prevRegion;

	// position of the previous document change
	private int prevOffset;
	// text of the previous document change
	private String prevInsert;

	// position of the current document change
	private int currentOffset;
	// text of the current document change
	private String currentInsert;

	private static final String SINGLE_SLASH = "/";
	private static final String DOUBLE_SLASH = "//";
	
	/**
	 * Default constructor
	 */
	public BlockCommentEvaluator() {
		firstDoubleBackSlashDetected = false;
	}
	
	/**
	 * Keeps track of DocumentEvent changes and determines of the user comments out multiple sequential lines of code. 
	 * @param event the change detected by the DocumentChange Listener
	 * @return true if the user comments two sequential lines of code, false otherwise
	 */
	public boolean evaluate(DocumentEvent event) {
		// Dev Notes: John
		// If a user comments out a line with ctrl + /, then the length is 0 and the text is "//"
		// A document event can't tell the difference between ctrl + / on two consecutive lines and
		// commenting out a block of code all at once
		// ModificationStamp is just a counter that increments. So that's not helpful

		if (doubleBackSlash(event)) {
			IDocument doc = event.getDocument();
			try {
				// Verify that the double slash is at the beginning of the line
				// grab the position of the start of the line where the document change was
				int lineOffset = doc.getLineInformationOfOffset(currentOffset).getOffset();
				String textBeforeSlashes = doc.get(lineOffset, prevOffset-lineOffset);

				textBeforeSlashes.trim();
				if (textBeforeSlashes.isEmpty()) {

					// If we have two back slashes, then check if we've found two previously
					if (firstDoubleBackSlashDetected) {
						IRegion currentRegion = event.getDocument().getLineInformationOfOffset(currentOffset);

						// Check if consecutive lines
						int prevToCurrentDif = currentRegion.getOffset() - (prevRegion.getOffset() + prevRegion.getLength());
						int currentToPrevDif = prevRegion.getOffset() - (currentRegion.getOffset() + currentRegion.getLength());

						// update the prevRegion
						prevRegion = currentRegion;

						// Uses a buffer of 2 characters because sometimes IRegion doesn't count the newly inserted "//"
						if ((prevToCurrentDif >= 0 && prevToCurrentDif <= 2) ||
								(currentToPrevDif >= 0 && currentToPrevDif <= 2)) {
							return true;
						}

						// If we haven't seen a double back slash yet
					} else {
						firstDoubleBackSlashDetected = true;
						prevRegion = event.getDocument().getLineInformationOfOffset(currentOffset);
					}
				}
			// Try Catch needed in case offset passed in doc.get methods doesn't actually exist
			// We shouldn't actually get here, but since it's all asynchronous, something weird may happen
			} catch (BadLocationException e) {
				firstDoubleBackSlashDetected = false;
			}
		}
		// Update the previous event offset and character
		prevOffset = currentOffset;
		prevInsert = currentInsert;

		return false;
	}

	/**
	 * Helper function that determines if the DocumentEvent(s) provided is a double "//" inputted from the user.
	 * This will catch both manually typing "/" then "/' as well as using ctrl + / to comment out a single line
	 * @param event DocumentEvent that occurred
	 * @return true if a double backslash was placed on a single line, false otherwise
	 */
	private boolean doubleBackSlash(DocumentEvent event) {
		currentOffset = event.getOffset();
		currentInsert = event.getText();

		// Currently checking for consecutive ctrl + / has limitations with Eclipse API
		// Check if a "//" was added through copy/paste or ctrl + /
		// if (event.getText().equals(DOUBLE_SLASH)) {
		//	 return true;
		// }

		// Check if there was a single "/" typed followed by another "/"
		if (prevOffset == currentOffset - 1) {
			if (currentInsert != null && this.prevInsert != null &&
					this.prevInsert.equals(SINGLE_SLASH) && currentInsert.equals(SINGLE_SLASH)) {
				return true;
			}
		}
		return false;
	}
}

package main.evaluators;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;

import main.interfaces.FeatureID;

/**
 * Evaluates DocumentEvent changes to determine if the user is manually deleting trailing white
 * spaces. Eclipse has a setting to automatically remove trailing white spaces on save, so the
 * user could use that instead.
 */
public class TrailingWhiteSpaceEvaluator extends FeatureEvaluator {

	private String lineBeforeChange;

	/**
	 * Constructs a TrailingWhiteSpaceEvaluator
	 * @param document The document to evaluate changes in
	 */
	public TrailingWhiteSpaceEvaluator(IDocument document) {
		this.featureID = FeatureID.TRAILING_WHITE_SPACE_FEATURE_ID;
		this.document = document;
	}

	/**
	 * Evaluates document changes to see if the user is manually deleting all trailing white space
	 * from a line
	 * @param event The document change data
	 */
	@Override
	public boolean evaluateDocumentChanges(DocumentEvent event) {

		try {
			int line = document.getLineOfOffset(event.getOffset());
			int startOffset = document.getLineOffset(line);
			int length = document.getLineLength(line);

			// Need to check up to length - 1 instead of length due to line feed char at end
			String lineAfterChange = document.get(startOffset, length - 1);

			// If the change event is a deletion, check whether only the line's whitespace has
			// changed, and that the line no longer ends with whitespace
			if (event.getText().length() == 0) {
				return lineBeforeChange.trim().equals(lineAfterChange.trim()) &&
						(lineBeforeChange.endsWith(" ") || lineBeforeChange.endsWith("\t")) &&
						!(lineAfterChange.endsWith(" ") || lineAfterChange.endsWith("\t"));
			}
		} catch (BadLocationException e) {
		}
		return false;
	}

	/**
	 * Stores the contents of the line before document changes are made
	 * @param event The document change data
	 */
	@Override
	public boolean evaluateDocumentBeforeChange(DocumentEvent event) {
		try {
			int line = document.getLineOfOffset(event.getOffset());
			int startOffset = document.getLineOffset(line);
			int length = document.getLineLength(line);

			// Only check up to length - 1 due to hidden line feed char at the end of the line
			this.lineBeforeChange = document.get(startOffset, length - 1);
		} catch (BadLocationException e) {
		}
		return false;
	}
}
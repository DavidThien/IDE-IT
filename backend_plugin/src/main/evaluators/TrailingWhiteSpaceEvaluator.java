package main.evaluators;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;

/**
 * Evaluates DocumentEvent changes to determine if the user is manually deleting trailing white spaces
 * Eclipse has a setting to automatically remove trailing white spaces on save, so the user could use
 * this instead.
 */
public class TrailingWhiteSpaceEvaluator extends FeatureEvaluator {
	
	private String lineBeforeChange;
	
	/**
	 * Constructs a TrailingWhiteSpaceEvaluator
	 * @param document
	 */
	public TrailingWhiteSpaceEvaluator(IDocument document) {
		this.featureID = "trailingWhiteSpaceSuggestion";
		this.document = document;
	}

	/**
	 * Evaluates document changes to see if the user is manually deleting all trailing white space
	 * from a line
	 * @param event
	 */
	@Override
	public boolean evaluateDocumentChanges(DocumentEvent event) {
		
		try {
			int line = document.getLineOfOffset(event.getOffset());
			int startOffset = document.getLineOffset(line);
			int length = document.getLineLength(line);
			
			// Need to check up to length - 1 instead of length due to line feed char at end
			String lineAfterChange = document.get(startOffset, length - 1);
			
			// Check that only the line's whitespace has changed, and that the line no longer
			// ends with whitespace
			boolean nonWhiteSpaceCharactersUnchanged = lineBeforeChange.trim().equals(lineAfterChange.trim());
			boolean trailingWhiteSpaceBefore = lineBeforeChange.endsWith(" ") || lineBeforeChange.endsWith("\t");
			boolean trailingWhiteSpaceAfter = lineAfterChange.endsWith(" ") || lineAfterChange.endsWith("\t");		
			return nonWhiteSpaceCharactersUnchanged && trailingWhiteSpaceBefore && !trailingWhiteSpaceAfter;	
		} catch (BadLocationException e) {
		}
		return false;
	}

	/**
	 * Stores the contents of the line before document changes are made
	 */
	@Override
	public boolean evaluateDocumentBeforeChange(DocumentEvent event) {
		try {
			int line = document.getLineOfOffset(event.getOffset());
			int startOffset = document.getLineOffset(line);
			int length = document.getLineLength(line);
			
			// Need to check up to length - 1 instead of length due to line feed char at end
			this.lineBeforeChange = document.get(startOffset, length - 1);
		} catch (BadLocationException e) {
		}
		return false;
	}
}
package evaluators;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;

/**
 * Evaluates DocumentEvent changes to determine if the user is commenting out multiple sequential lines of code. If so,
 * then the user should be notified of the block comment feature in Eclipse.
 */
public class BlockCommentEvaluator {
		
	private boolean firstBackSlashDetected;
	private IRegion prevRegion;
	private int prevOffset;
	private String prevInsert;
	
	/**
	 * Default constructor
	 */
	public BlockCommentEvaluator() {
		firstBackSlashDetected = false;
	}
	
	/**
	 * Keeps track of DocumentEvent changes and determines of the user comments out multiple sequential lines of code. 
	 * @param event the change detected by the DocumentChange Listener
	 * @return true if the user comments two sequential lines of code, false otherwise
	 */
	public boolean evaluate(DocumentEvent event) {

		// Took logic Eric created to check for a double back slash
		// May be a way to optimize this, but it works
		int currentOffset = event.getOffset();
		String currentInsert = event.getText();
		if (prevOffset == currentOffset - 1) {
			if (currentInsert != null && this.prevInsert != null &&
					this.prevInsert.equals("/") && currentInsert.equals("/")) {

				// DEBUG
				System.out.println("Placed two slashes");

				// Verify that the double backslash is at the start of the line
				IDocument doc = event.getDocument();
				try {
					int lineOffset = doc.getLineInformationOfOffset(currentOffset).getOffset();
					String textBeforeSlashes = doc.get(lineOffset, prevOffset-lineOffset);

					// DEBUG
					//					System.out.println("line offset: " + lineOffset);
					//					System.out.println("Prev offset: " + prevOffset);
					//					System.out.println("Text before slashes: " + textBeforeSlashes);

					textBeforeSlashes = textBeforeSlashes.trim();
					if (textBeforeSlashes.isEmpty()) {

						// DEBUG
//						System.out.println("Start of line!");

						// If we have two back slashes, then check if we've found two previously
						if (firstBackSlashDetected) {
							IRegion currentRegion = event.getDocument().getLineInformationOfOffset(currentOffset);

							// DEBUG
							// System.out.println("Current Region pos: " + currentRegion.getOffset());
							// System.out.println("Current REgion length: " + currentRegion.getLength());
							// System.out.println("Prev Region pos: " + prevRegion.getOffset());
							// System.out.println("Prev Region length: " + prevRegion.getLength());

							// Check if consecutive lines
							int prevToCurrentDif = currentRegion.getOffset() - (prevRegion.getOffset() + prevRegion.getLength());
							int currentToPrevDif = prevRegion.getOffset() - (currentRegion.getOffset() + currentRegion.getLength());

							// Uses a buffer of 2 characters because sometimes IRegion doesn't count the newly inserted "//"
							if ((prevToCurrentDif >= 0 && prevToCurrentDif <= 2) ||
									(currentToPrevDif >= 0 && currentToPrevDif <= 2)) {
								firstBackSlashDetected = false;
								return true;
							}

							// If not consecutive, then update prevRegion
							prevRegion = currentRegion;

							// If we haven't seen a double back slash yet
						} else {
							firstBackSlashDetected = true;
							prevRegion = event.getDocument().getLineInformationOfOffset(currentOffset);
						}
					}
				// Try Catch needed in case offset passed in doc.get methods doesn't actually exist
				// We shouldn't actually get here, but since it's all asynchronous, something weird may happen
				} catch (BadLocationException e) {
					// DEBUG
					System.out.println("Bad offset location in BlockCommentEvaluator"); // shouldn't be able to get here
					firstBackSlashDetected = false;
				}
			}
		}
		// Update the previous event offset and character
		prevOffset = currentOffset;
		prevInsert = currentInsert;

		return false;
	}
}

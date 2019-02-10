package trackers;

import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocumentListener;

/**
 * This class is responsible for listening for changes made within an
 * Eclipse editor window. It contains methods that are called when text
 * is inserted into or removed from the document being edited. When changes
 * are detected, evaluation will be run on the changes to see if any Eclipse
 * features were "triggered"
 */
public class DocumentChangesTracker implements IDocumentListener {
	
	public DocumentChangesTracker() {
		super();
		
	}

	/**
	 * Runs right before the user's change to the document is
	 * actually entered into the document
	 */
	@Override
	public void documentAboutToBeChanged(DocumentEvent event) {
		// Not sure if it matters whether we use this method
		// or documentChanged to track input, we will find out
		// over time I suppose. For now I'm just keeping it blank
	}

	/**
	 * Runs after the user's change to the document is actually
	 * entered into the document
	 */
	@Override
	public void documentChanged(DocumentEvent event) {
		// For now, will just print out the text inserted into/removed
		// from the document
		
		// event has options like getText() to get the text that was added
		// or removed from the document, getOffset() to get the offset in
		// the document where the insertion/removal occured, etc.
		System.out.println(event.getText());
	}
}

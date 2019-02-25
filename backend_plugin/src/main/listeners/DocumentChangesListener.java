package main.listeners;

import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocumentListener;

import main.evaluators.Evaluator;

/**
 * This class is responsible for listening for changes made within an
 * Eclipse editor window. It contains methods that are called when text
 * is inserted into or removed from the document being edited. When changes
 * are detected, evaluation will be run on the changes to see if any Eclipse
 * features were "triggered"
 */
public class DocumentChangesListener implements IDocumentListener {
	
	private Evaluator evaluator;
	
	public DocumentChangesListener(Evaluator evaluator) {
		super();
		this.evaluator = evaluator;
	}

	/**
	 * Fires before a user's change to a document inside a document editor
	 * is actually entered into the document
	 */
	@Override
	public void documentAboutToBeChanged(DocumentEvent event) {
	}

	/**
	 * Fires after a user's change to a document inside a document editor 
	 * is actually entered into the document
	 */
	@Override
	public void documentChanged(DocumentEvent event) {
		
		// Send the document change event information to the Evaluator
		evaluator.evaluateDocumentChanges(event);
	}
}

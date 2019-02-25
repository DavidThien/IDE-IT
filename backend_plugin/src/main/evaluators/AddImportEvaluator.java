package main.evaluators;

import java.util.Iterator;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.IAnnotationModel;

public class AddImportEvaluator extends FeatureEvaluator {
	
	private boolean unresolvedVariablesExist;
	private DocumentEvent lastDocumentEvent;
	
	/**
	 * Construct an AddImportEvaluator
	 */
	public AddImportEvaluator() {
		this.featureID = "addImportStatementsSuggestion";
		this.lastDocumentEvent = null;
		this.unresolvedVariablesExist = false;
	}

	/**
	 * Tracks the user's changes, and determines whether they are fixing an
	 * "unresolved type" issue by manually typing the import statement
	 * @return boolean true if the user is manually adding an import statement
	 * 					when unresolved variables exist; false otherwise
	 */
	@Override
	public boolean evaluateDocumentChanges(DocumentEvent docEvent) {
		
		if (this.lastDocumentEvent == null) {
			
			// This is the first DocumentEvent captured by the Evaluator
			this.lastDocumentEvent = docEvent;
		} else if (this.unresolvedVariablesExist) {
			
			// Check if the last two DocumentEvents represent the user typing "t"
			// followed by " "
			String textAdded = this.lastDocumentEvent.getText() + docEvent.getText();
			int offsetDifference = docEvent.getOffset() - this.lastDocumentEvent.getOffset();
			this.lastDocumentEvent = docEvent;
			if (textAdded.equals("t ") && offsetDifference == 1) {
				
				// If the "t" is the end of an import statement, trigger the feature evaluation
				return this.checkForTypedImportStatement(docEvent);
			}
		}
		
		// The user did not manually type an import statement
		return false;
	}
	
	/**
	 * Checks the previous 6 characters before the offset of the given DocumentEvent
	 * @param docEvent the DocumentEvent representing the last text insertion
	 * @return boolean true if the previous 6 characters in the document are
	 * 					'i'-'m'-'p'-'o'-'r'-'t'; false otherwise
	 */
	private boolean checkForTypedImportStatement(DocumentEvent docEvent) {
		try {
			
			// Check if the last six characters in the document spell "import"
			int startOffset = docEvent.getOffset() - 6;
			String testing = docEvent.getDocument().get(startOffset, 6);
			return testing.equals("import");
		} catch (BadLocationException e) {
			
			// The calculated startOffset is negative. This only happens when
			// the current offset after the "t " is smaller than 6, meaning
			// there is no way the user could have typed "import", so return false
			return false;
		}
	}

	/**
	 * Updates the boolean flag of this AddImportEvaluator that tracks whether
	 * there exist any unresolved types in the document. This returns false
	 * always, as a change to the annotation model should not by itself trigger
	 * a notification to the frontend.
	 * @return boolean false
	 */
	@Override
	public boolean evaluateAnnotationModelChanges(IAnnotationModel model) {
		Iterator<Annotation> it = model.getAnnotationIterator();

		// Iterate through all annotations
		while (it.hasNext()) {
			Annotation current = it.next();
			
			// If the annotation is valid and represents an unresolved variable,
			// update the flag for unresolved variables existing
			if (current.getText().endsWith("cannot be resolved to a type") && !current.isMarkedDeleted()) {
				this.unresolvedVariablesExist = true;
				return false;
			}
		}
		
		// There are no unresolved variables in the document
		this.unresolvedVariablesExist = false;
		return false;
	}
}

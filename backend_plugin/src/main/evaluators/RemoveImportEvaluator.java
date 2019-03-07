package main.evaluators;

import java.util.Iterator;

import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.ui.texteditor.ITextEditor;

/**
 * Evaluator function to determine if there are any unused import statements in the current document. If any unused import
 * statements are found, then evaluate will return true
 *
 */
public class RemoveImportEvaluator extends FeatureEvaluator {

	private boolean activeUnusedImportStatement;

	/**
	 * Constructor
	 * @param docName the name of the document this evaluator is attached to
	 */
	public RemoveImportEvaluator(ITextEditor editor) {
		this.featureID = "removeUnusedImportStatementsSuggestion";
	}

	/**
	 * Checks the document that matches the docName to see if there are any unused import statements
	 *
	 * @param event
	 * @return true if there are unused import statements in the document, false otherwise
	 */
	@Override
	public boolean evaluateAnnotationModelChanges(IAnnotationModel model) {

		// Dev notes: John
		// String matching isn't ideal in this case, but it's the most reliable.
		// It's possible to pull out the corresponding IMarker associated with the annotation, but the IMarker is only
		// updated upon saves. The annotation is updated upon document changes.

		Iterator<Annotation> it = model.getAnnotationIterator();

		// Iterate through the annotations and update the flag indicating whether any active unused import
		// statements exist in the document window
		this.activeUnusedImportStatement = false;
		while (it.hasNext()) {
			Annotation current = it.next();

			// Check if the annotation is a valid, non-deleted unused import annotation
			if (!current.isMarkedDeleted() && current.getText() != null && current.getText().startsWith("The import") && 
					current.getText().endsWith("never used")) {
				this.activeUnusedImportStatement = true;
				break;
			}
		}
		return false;
	}

	/**
	 * Returns whether the document currently has an active unused import statement
	 * @return true if there is an active unused import; false otherwise
	 */
	public boolean hasActiveUnusedImportStatement() {
		return this.activeUnusedImportStatement;
	}
}

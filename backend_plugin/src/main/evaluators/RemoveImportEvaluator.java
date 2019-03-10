package main.evaluators;

import java.util.Iterator;

import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.IAnnotationModel;

import main.interfaces.FeatureID;

/**
 * Evaluator function to determine if there are any unused import statements in the entire workspace (any open projects).
 * If any unused import statements are found, then evaluate will return true
 *
 */
public class RemoveImportEvaluator extends FeatureEvaluator {

	private boolean activeUnusedImportStatement;

	/**
	 * Constructor
	 */
	public RemoveImportEvaluator() {
		this.featureID = FeatureID.REMOVE_IMPORT_FEATURE_ID;
	}

	/**
	 * Checks the document that matches the docName to see if there are any unused import statements
	 *
	 * @param event
	 * @return true if there are unused import statements in the document, false otherwise
	 */
	@Override
	public boolean evaluateAnnotationModelChanges(IAnnotationModel model) {

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

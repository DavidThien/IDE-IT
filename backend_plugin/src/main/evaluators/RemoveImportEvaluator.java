package main.evaluators;

import java.util.Iterator;

import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.IAnnotationModel;

import main.interfaces.FeatureID;

/**
 * Keeps track of the existence of any annotations in the document regarding unused imports.
 * When a file is saved in the Eclipse workspace, this evaluator should trigger if the document
 * window currently contains unused import annotations.
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
	 * Updates this feature evaluator's boolean flag indicating whether unused imports exist in the
	 * document. This should never trigger the feature by itself, so always returns false.
	 * @param model The annotation model attached to the document window
	 * @return false
	 */
	@Override
	public boolean evaluateAnnotationModelChanges(IAnnotationModel model) {
		this.activeUnusedImportStatement = false;

		// Iterate through the annotations in the document window
		Iterator<Annotation> it = model.getAnnotationIterator();
		while (it.hasNext()) {

			// Check if any annotation is a valid, non-deleted unused import annotation
			Annotation current = it.next();
			if (!current.isMarkedDeleted() && current.getText() != null &&
					current.getText().startsWith("The import") &&
					current.getText().endsWith("never used")) {
				this.activeUnusedImportStatement = true;
				break;
			}
		}
		return false;
	}

	/**
	 * Returns whether the Eclipse workspace currently has an active unused import statement
	 * @return true if there is an active unused import; false otherwise
	 */
	public boolean hasActiveUnusedImportStatement() {
		return this.activeUnusedImportStatement;
	}
}

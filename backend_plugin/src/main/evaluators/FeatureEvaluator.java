package main.evaluators;

import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.source.IAnnotationModel;

/**
 * An abstract class representing a feature evaluation function. Feature evaluations
 * can be added by extending this class and making use of its methods to evaluate
 * changes made within a document, then adding the new subclass to the Evaluator's
 * initializeFeatureEvaluators() method.
 */
public abstract class FeatureEvaluator {

	// The unique feature ID String associated with the feature
	protected String featureID;

	// The document in which changes are evaluated
	protected IDocument document;

	/**
	 * Evaluates changes made to the text within a document
	 * @param event The document change data
	 * @return boolean true if the changes cause the feature to be triggered; false otherwise
	 */
	public boolean evaluateDocumentChanges(DocumentEvent event) {
		return false;
	}

	/**
	 * Evaluates what changes will be made to the text within a document before the changes are
	 * applied
	 * @param event The document change data
	 * @return true if the document changes that are about to occur cause the evaluation function
	 * 			to trigger; false otherwise.
	 */
	public boolean evaluateDocumentBeforeChange(DocumentEvent event) {
		return false;
	}

	/**
	 * Evaluates changes to the annotation model of a document/editor window
	 * @param model The annotation model attached to the document window
	 * @return boolean true if the annotation model changes cause the feature to be triggered;
	 * 			false otherwise
	 */
	public boolean evaluateAnnotationModelChanges(IAnnotationModel model) {
		return false;
	}

	/**
	 * @return The String representing the unique feature ID of this feature
	 */
	public String getFeatureID() {
		return this.featureID;
	}
}

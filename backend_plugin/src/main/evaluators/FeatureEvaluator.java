package main.evaluators;

import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.source.IAnnotationModel;

public abstract class FeatureEvaluator {

	protected String featureID;
	protected IDocument document;

	/**
	 * Evaluates changes made to the text within a document
	 * @param docEvent The information about the changes that were made
	 * @return boolean true if the document changes cause the feature to be
	 *					triggered; false otherwise
	 */
	public boolean evaluateDocumentChanges(DocumentEvent docEvent) {
		return false;
	}

	/**
	 * Evaluates what changes will be made to the text within a document before the changes are applied
	 * @param docEvent The DocumentEvent containing the changes about to be made
	 * @return true if the document changes that are about to occur cause the evaluation function to trigger,
	 * 		false otherwise.
	 */
	public boolean evaluateDocumentBeforeChange(DocumentEvent docEvent) {
	    return false;
	}

	/**
	 * Evaluates changes to the annotation model of a document/editor window
	 * @param model The information about the annotations in the
	 * 					document/editor window
	 * @return boolean true if the annotation model changes cause the feature
	 * 					to be triggered; false otherwise
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

package main.evaluators;

import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.source.IAnnotationModel;

public interface FeatureEvaluator {

	/**
	 * Evaluates changes made to the text within a document
	 * @param docEvent The information about the changes that were made
	 * @return boolean true if the document changes cause the feature to be
	 *					triggered; false otherwise
	 */
	public boolean evaluateDocumentChanges(DocumentEvent docEvent);
	
	/**
	 * Evaluates changes to the annotation model of a document/editor window
	 * @param model The information about the annotations in the
	 * 					document/editor window
	 * @return boolean true if the annotation model changes cause the feature
	 * 					to be triggered; false otherwise
	 */
	public boolean evaluateAnnotationModelChanges(IAnnotationModel model);
	
	/**
	 * @return The String representing the unique feature ID of this feature
	 */
	public String getFeatureID();
}

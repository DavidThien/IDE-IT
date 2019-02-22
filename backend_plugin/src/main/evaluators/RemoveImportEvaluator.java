package main.evaluators;

import java.util.Iterator;

import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.ui.texteditor.ITextEditor;

/**
 * Evaluator function to determine if there are any unused import statements in the current document. If any unused import
 * statements are found, then evaluate will return true
 *
 */
public class RemoveImportEvaluator implements FeatureEvaluator {
	
	
	ITextEditor editor;
	
	/**
	 * Constructor
	 * @param docName the name of the document this evaluator is attached to
	 */
	public RemoveImportEvaluator(ITextEditor editor) {
		this.editor = editor;
	}
	
	/**
	 * Evaluates changes to the document text in regards to the remove import
	 * feature
	 */
	public boolean evaluateDocumentChanges(DocumentEvent event) {
		
		// TODO: Investigate ways to incorporate document changes into the
		// remove import evaluation if needed
		return false;
	}
	
	/**
	 * Checks the document that matches the docName to see if there are any unused import statements
	 * 
	 * @param event
	 * @return true if there are unused import statements in the document, false otherwise
	 */
	public boolean evaluateAnnotationModelChanges(IAnnotationModel model) {
		
		// Dev notes: John
		// String matching isn't ideal in this case, but it's the most reliable.
		// It's possible to pull out the corresponding IMarker associated with the annotation, but the IMarker is only
		// updated upon saves. The annotation is updated upon document changes.
		
		Iterator<Annotation> it = model.getAnnotationIterator();
		
		while (it.hasNext()) {
			Annotation current = it.next();
			// Check if the annotation is an unused import
			if (current.getText().startsWith("The import") && current.getText().endsWith("never used")) {
				return true;
			}
		}
		return false;
	}
	
	public String getFeatureID() {
		return "removeUnusedImportStatementsSuggestion";
	}
}

package evaluators;

import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.core.util.IAnnotation;
import org.eclipse.jdt.core.util.IAnnotationComponent;


import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.AnnotationModelEvent;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.ui.texteditor.ITextEditor;

import org.eclipse.ui.texteditor.SimpleMarkerAnnotation;


/**
 * Evaluator function to determine if there are any unused import statements in the current document. If any unused import
 * statements are found, then evaluate will return true
 *
 */
public class RemoveImportEvaluator {
	
	
	ITextEditor editor;
	
	/**
	 * Constructor
	 * @param docName the name of the document this evaluator is attached to
	 */
	public RemoveImportEvaluator(ITextEditor editor) {
		this.editor = editor;
	}
	
	/**
	 * Checks the document that matches the docName to see if there are any unused import statements
	 * 
	 * @param event
	 * @return true if there are unused import statements in the document, false otherwise
	 */
	public boolean evaluate(IAnnotationModel model) {
		
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
	
}
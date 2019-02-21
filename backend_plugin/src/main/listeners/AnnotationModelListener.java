package main.listeners;

import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.jface.text.source.IAnnotationModelListener;

import main.evaluators.Evaluator;

/**
 * Listens for any changes made to the AnnotationModel  with this listener.
 * When annotations are added, removed, or otherwise changed from the document being edited, any evaluation functions dependent
 * on annotations will be ran to see if any Eclipse features should be suggested to the user.
 */
public class AnnotationModelListener implements IAnnotationModelListener {

	// Evaluator that the AnnotationModelListener reports to when an update occurs
	private Evaluator evaluator;
	
	/**
	 * Constructor
	 * @param evaluator
	 */
	public AnnotationModelListener(Evaluator evaluator) {
		this.evaluator = evaluator;
	}
	
	/**
	 * This method is called when the AnnotationModel is changed. When called, this method passes the changed information
	 * to the Evaluator to be passed to the correct evaluation functions.
	 */
	@Override
	public void modelChanged(IAnnotationModel model) {
		evaluator.evaluateAnnotationModelChanges(model);
	}

}

package listeners;

import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.jface.text.source.IAnnotationModelListener;

import evaluators.Evaluator;

public class AnnotationModelListener implements IAnnotationModelListener {

	private Evaluator evaluator;
	
	public AnnotationModelListener(Evaluator evaluator) {
		this.evaluator = evaluator;
	}
	
	@Override
	public void modelChanged(IAnnotationModel model) {
		evaluator.evaluateAnnotationModelChanges(model);
		
	}

}

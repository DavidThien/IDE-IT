package evaluators;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.texteditor.ITextEditor;

import interfaces.FeatureSuggestion;
import listeners.DocumentChangesTracker;

/**
 * This class keeps track of all evaluators that are open across all
 * document editor windows, and provides the ability to add new
 * evaluators.
 */
public class EvaluatorManager {

	private FeatureSuggestion fs;
	private Map<IEditorPart, Evaluator> openPartEvaluators;
	
	public EvaluatorManager() {
		this.fs = new FeatureSuggestion();
		this.openPartEvaluators = new HashMap<IEditorPart, Evaluator>();
	}
	
	public FeatureSuggestion getFeatureSuggestor() {
		return this.fs;
	}
	
	public Map<IEditorPart, Evaluator> getOpenEvaluators() {
		return this.openPartEvaluators;
	}
	
	public void addEvaluator(IEditorPart part) {
		
		// Add listeners to the part, and add the evaluator
		// to the list of evaluators
		ITextEditor editor = (ITextEditor) part;
		
		// Gets the document stored inside that text editor window
		IDocument doc = editor.getDocumentProvider().getDocument(editor.getEditorInput());

		// Create an evaluator
		Evaluator newEvaluator = new Evaluator(this);
		
		// 	Adds a MyDocumentListener to the document inside the text editor window
		DocumentChangesTracker docTracker = new DocumentChangesTracker(newEvaluator);
		doc.addDocumentListener(docTracker);
		
		// Add this part->evaluator mapping to the list of open evaluators
		this.openPartEvaluators.put(part, newEvaluator);
	}
}

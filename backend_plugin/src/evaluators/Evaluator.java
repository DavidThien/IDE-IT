package evaluators;

import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.texteditor.ITextEditor;

import listeners.DocumentChangesTracker;

/**
 * The Evaluator is designed to control all of the evaluation classes. The Evaluator creates a TrackerController, which
 * it uses to create different Listeners.
 * 
 * The listeners created through the TrackerController callback to the Evaluator, which calls the appropriate evaluation
 * functions depending on what changes were detected.
 * 
 * If any evaluation functions return true, then the FeatureSuggestion is notified of which feature should be suggested
 *
 */
public class Evaluator {

	// TODO: Create an interface / abstract class for different evaluation functions
	// This would allow us to keep a set / list and then just iterate through the list / set

	private BlockCommentEvaluator blockCommentEval; 
	private EvaluatorManager em;
	private IDocument document;
	private DocumentChangesTracker documentChangesTracker;
	
	/**
	 * Constructs an Evaluator that evaluates the given IEditorPart window
	 * under the given EvaluationManager em
	 * @param em EvaluatorManager object that tracks all Evaluator instances
	 * @param editorWindow the document editor window to add an evaluator to
	 */
	public Evaluator(EvaluatorManager em, IEditorPart editorWindow) {
		// DEBUG
		System.out.println("Evaluator Started");
		
		this.em = em;
		blockCommentEval = new BlockCommentEvaluator();
		this.initializeListeners(editorWindow);
	}

	/**
	 * Creates the document change listener that this Evaluator will
	 * use to listen for changes within the given IEditorPart document
	 * editor window
	 * @param editorWindow
	 */
	private void initializeListeners(IEditorPart editorWindow) {

		// Get the document stored inside the editor window
		ITextEditor editor = (ITextEditor) editorWindow;
		IDocument doc = editor.getDocumentProvider().getDocument(editor.getEditorInput());
		this.document = doc;

		// Add a DocumentChangesTracker to the document
		DocumentChangesTracker docTracker = new DocumentChangesTracker(this);
		doc.addDocumentListener(docTracker);
		this.documentChangesTracker = docTracker;
	}

	/**
	 * Checks all evaluation functions that need DocumentEvent changes
	 * @param event
	 */
	public void evaluateDocChanges(DocumentEvent event) {
	
		// Block comment evaluation
		if (blockCommentEval.evaluate(event)) {
			this.em.notifyFeatureSuggestion("Block Comment");
		}
	}

	public void stop() {
		this.document.removeDocumentListener(this.documentChangesTracker);
	}
	
}

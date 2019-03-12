package main.evaluators;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.ui.texteditor.ITextEditor;

import main.listeners.AnnotationModelListener;
import main.listeners.DocumentChangesListener;

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

	// Evaluation objects
	private EvaluatorManager manager;
	private List<FeatureEvaluator> featureEvaluators;

	// Window content
	private IDocument document;
	private IAnnotationModel annotationModel;

	// Content change listeners
	private DocumentChangesListener documentChangesListener;
	private AnnotationModelListener annotationModelListener;

	/**
	 * Constructs an Evaluator that evaluates the given IEditorPart window
	 * under the given EvaluationManager em
	 * @param em The EvaluatorManager object tracking all Evaluator instances
	 * @param textEditor The document editor window to add an evaluator to
	 */
	public Evaluator(EvaluatorManager em, ITextEditor textEditor) {
		this.manager = em;
		this.featureEvaluators = new ArrayList<FeatureEvaluator>();
		this.initializeListeners(textEditor);
		this.initializeFeatureEvaluators();
	}

	/**
	 * Creates the feature evaluators for each feature and adds them to this
	 * Evaluator's list of feature evaluators
	 */
	private void initializeFeatureEvaluators() {
		this.featureEvaluators.add(new BlockCommentEvaluator(this.document));
		this.featureEvaluators.add(new RemoveImportEvaluator());
		this.featureEvaluators.add(new AddImportEvaluator(this.document));
		this.featureEvaluators.add(new CorrectIndentationEvaluator(this.document));
		this.featureEvaluators.add(new GetterSetterEvaluator(this.document));
		this.featureEvaluators.add(new TrailingWhiteSpaceEvaluator(this.document));
	}

	/**
	 * Creates the listeners that this Evaluator will use to listen for changes within
	 * the given IEditorPart document editor window
	 * @param editorWindow The text editor window part containing the document
	 */
	private void initializeListeners(ITextEditor textEditor) {

		// Get the document stored inside the text editor window
		IDocument doc = textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput());
		this.document = doc;

		// Add a DocumentChangesTracker to the document
		DocumentChangesListener docListener = new DocumentChangesListener(this);
		doc.addDocumentListener(docListener);
		this.documentChangesListener = docListener;

		// Create a new AnnotationModelListener
		this.annotationModelListener = new AnnotationModelListener(this);
		// Get the AnnotationModel associated with the textEditor
		this.annotationModel = textEditor.getDocumentProvider().getAnnotationModel(textEditor.getEditorInput());
		// Add a AnnotationModelListener to the AnnotationModel
		this.annotationModel.addAnnotationModelListener(this.annotationModelListener);
	}

	/**
	 * Checks all evaluation functions after a document change is applied
	 * @param event The document change data
	 */
	public void evaluateDocumentChanges(DocumentEvent event) {
		for (FeatureEvaluator featureEvaluator : this.featureEvaluators) {
			if (featureEvaluator.evaluateDocumentChanges(event)) {
				this.manager.notifyFeatureSuggestion(featureEvaluator.getFeatureID());
			}
		}
	}

	/**
	 * Checks all evaluation functions before a document change is applied
	 * @param event The document change data
	 */
	public void evaluateDocumentBeforeChange(DocumentEvent event) {
		for (FeatureEvaluator featureEvaluator : this.featureEvaluators) {
			if (featureEvaluator.evaluateDocumentBeforeChange(event)) {
				this.manager.notifyFeatureSuggestion(featureEvaluator.getFeatureID());
			}
		}
	}

	/**
	 * Checks all evaluation functions when the annotations of a document window change
	 * @param model The annotation model attached to the document window
	 */
	public void evaluateAnnotationModelChanges(IAnnotationModel model) {
		for (FeatureEvaluator featureEvaluator : this.featureEvaluators) {
			if (featureEvaluator.evaluateAnnotationModelChanges(model)) {
				this.manager.notifyFeatureSuggestion(featureEvaluator.getFeatureID());
			}
		}
	}

	/**
	 * Checks the RemoveImportEvaluator to see if unused imports exist
	 * @return true if unused imports exist in the document; false otherwise
	 */
	public boolean workspaceResourceSaved() {
		for (FeatureEvaluator fEval : this.featureEvaluators) {
			if (fEval instanceof RemoveImportEvaluator) {
				RemoveImportEvaluator eval = (RemoveImportEvaluator) fEval;
				return eval.hasActiveUnusedImportStatement();
			}
		}
		return false;
	}

	/**
	 * Stops this Evaluator by removing any listeners it created
	 */
	public void stop() {
		this.document.removeDocumentListener(this.documentChangesListener);
		this.annotationModel.removeAnnotationModelListener(this.annotationModelListener);
	}
}

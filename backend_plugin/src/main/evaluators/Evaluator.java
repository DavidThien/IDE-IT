package main.evaluators;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.ui.texteditor.ITextEditor;
import main.listeners.DocumentChangesListener;
import main.interfaces.FeatureSuggestion;
import main.listeners.AnnotationModelListener;


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

	private EvaluatorManager manager;
	private List<FeatureEvaluator> featureEvaluators;

	private IDocument document;
	private DocumentChangesListener documentChangesListener;

	private IAnnotationModel annotationModel;
	private AnnotationModelListener annotationModelListener;

	/**
	 * Constructs an Evaluator that evaluates the given IEditorPart window
	 * under the given EvaluationManager em
	 * @param em EvaluatorManager object that tracks all Evaluator instances
	 * @param editorWindow the document editor window to add an evaluator to
	 */
	public Evaluator(EvaluatorManager em, ITextEditor textEditor) {
		// DEBUG
		System.out.println("Evaluator Started");

		this.manager = em;
		this.featureEvaluators = new ArrayList<FeatureEvaluator>();
		this.initializeListeners(textEditor);
		this.initializeFeatureEvaluators(textEditor);
	}

	/**
	 * Creates the feature evaluators for each feature and adds them to this
	 * Evaluator's list of feature evaluators
	 * @param textEditor The text document editor this Evaluator is evaluating
	 */
	private void initializeFeatureEvaluators(ITextEditor textEditor) {
		this.featureEvaluators.add(new BlockCommentEvaluator(this.document));
		this.featureEvaluators.add(new RemoveImportEvaluator(textEditor));
		this.featureEvaluators.add(new AddImportEvaluator(this.document));
		this.featureEvaluators.add(new CorrectIndentationEvaluator(this.document));
		this.featureEvaluators.add(new TrailingWhiteSpaceEvaluator(this.document));
	}

	/**
	 * Creates the document change listener that this Evaluator will
	 * use to listen for changes within the given IEditorPart document
	 * editor window
	 * @param editorWindow
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
	 * Checks all evaluation functions that need DocumentEvent changes
	 * @param event
	 */
	public void evaluateDocumentChanges(DocumentEvent event) {
		for (FeatureEvaluator featureEvaluator : this.featureEvaluators) {
			if (featureEvaluator.evaluateDocumentChanges(event)) {
				this.manager.notifyFeatureSuggestion(featureEvaluator.getFeatureID());
			}
		}
	}

	public void evaluateDocumentBeforeChange(DocumentEvent event) {
	    for (FeatureEvaluator featureEvaluator : this.featureEvaluators) {
		if (featureEvaluator.evaluateDocumentBeforeChange(event)) {
			this.manager.notifyFeatureSuggestion(featureEvaluator.getFeatureID());
		}
	    }
	}

	/**
	 * Checks all evaluation functions that need IAnnotationModel changes
	 * @param model
	 */
	public void evaluateAnnotationModelChanges(IAnnotationModel model) {
		for (FeatureEvaluator featureEvaluator : this.featureEvaluators) {
			if (featureEvaluator.evaluateAnnotationModelChanges(model)) {
				this.manager.notifyFeatureSuggestion(featureEvaluator.getFeatureID());
			}
		}
	}

	/**
	 * Stops this Evaluator by removing any listeners it created
	 */
	public void stop() {
		this.document.removeDocumentListener(this.documentChangesListener);
		this.annotationModel.removeAnnotationModelListener(this.annotationModelListener);
	}

}

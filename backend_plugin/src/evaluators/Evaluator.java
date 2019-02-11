package evaluators;

import org.eclipse.jface.text.DocumentEvent;

import plugin_test.FeatureSuggestion;
import trackers.TrackerController;

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

	// Dev notes:
	// It would be great if we could use TrackerController as what determines when listeners should be created
	// i.e. on new document open, then create a new document listener
	// TODO: Create an interface / abstract class for different evaluation functions
	// This would allow us to keep a set / list and then just iterate through the list / set
	
	private TrackerController trackerController;
	private BlockCommentEvaluator blockCommentEval; 
	private FeatureSuggestion fs;
	
	/**
	 * Default constructor
	 * @param fs FeatureSuggestion object that the Evaluator updates when a feature should be suggested
	 */
	public Evaluator(FeatureSuggestion fs) {
		// DEBUG
		System.out.println("Evaluator Started");
		
		this.fs = fs;
		trackerController = new TrackerController(this);
		trackerController.createDocumentListener();
		blockCommentEval = new BlockCommentEvaluator();
	}
	
	/**
	 * Checks all evaluation functions that need DocumentEvent changes
	 * @param event
	 */
	public void evaluateDocChanges(DocumentEvent event) {
	
		// Block comment evaluation
		if (blockCommentEval.evaluate(event)) {
			fs.notifyAllObservers("Block Comment");
		}
	}
	
}

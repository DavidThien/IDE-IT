package evaluators;

import org.eclipse.jface.text.DocumentEvent;

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
	
	/**
	 * Default constructor
	 * @param fs FeatureSuggestion object that the Evaluator updates when a feature should be suggested
	 */
	public Evaluator(EvaluatorManager em) {
		// DEBUG
		System.out.println("Evaluator Started");
		
		this.em = em;
		blockCommentEval = new BlockCommentEvaluator();
	}
	
	/**
	 * Checks all evaluation functions that need DocumentEvent changes
	 * @param event
	 */
	public void evaluateDocChanges(DocumentEvent event) {
	
		// Block comment evaluation
		if (blockCommentEval.evaluate(event)) {
			this.em.getFeatureSuggestor().notifyAllObservers("Block Comment");
		}
	}
	
}

package main.interfaces;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import main.evaluators.EvaluatorManager;

/** Designed to be used in conjunction with the FeatureSuggestionObserver class.
 * Observers can register themselves with FeatureSuggestions. FeatureSuggestion will
 * notify all registered Observers with features it detects the user may want to be
 * aware of.
 */
public class FeatureSuggestion implements FeatureSuggestionInterface {

	private EvaluatorManager manager;
	private List<FeatureSuggestionObserver> observers;
	private List<String> featureIDs;
	private boolean isRunning;

	// featureID strings used to communicate between frontend and backend
	// These must match EXACTLY with the agreed upon strings
	// A list of these strings can be found in featureIDStrings.txt in the main folder of this repo
	public static final String BLOCK_COMMENT_FEATURE_ID = "blockCommentSuggestion";
	public static final String ADD_IMPORT_FEATURE_ID = "addImportStatementsSuggestion";
	public static final String CORRECT_INDENTATION_FEATURE_ID = "correctIndentationsSuggestion";
	public static final String REMOVE_IMPORT_FEATURE_ID = "removeUnusedImportStatementSuggestion";
	public static final String TRAILING_WHITE_SPACE_FEATURE_ID = "trailingWhiteSpaceSuggestion";

	public FeatureSuggestion() {
		// debug
		System.out.println("FS created");
		manager = new EvaluatorManager(this);
		observers = new ArrayList<FeatureSuggestionObserver>();
		featureIDs = generateFeatureIDs();
		isRunning = false;
	}

	/**
	 * Register an Observer to be notified upon FeatureSuggestion updates
	 * @param obs Observer to be registered
	 * @return true upon successful registration, false otherwise
	 */
	@Override
	public boolean registerObserver(FeatureSuggestionObserver obs) {
		return observers.add(obs);
	}

	/**
	 * Removes an observer from the FeatureSuggestion observer list. The observer
	 * will no longer be updated on FeatureSuggestion updates
	 * @param obs Observer to be removed
	 * @return true upon successful removal, false otherwise
	 */
	@Override
	public boolean removeObserver(FeatureSuggestionObserver obs) {
		return observers.remove(obs);
	}

	/**
	 * Provides a list of all featureIDs used by FeatureSuggestion.
	 * Recommended to use this list to verify observer featureID list matches
	 * @return List of all featureIDs used by FeatureSuggestion
	 */
	@Override
	public List<String> getAllFeatureIDs() {
		List<String> feats = new ArrayList<String>(featureIDs);
		return feats;
	}

	/**
	 * Turns the FeatureSuggestion on. All observers will be updated upon updates.
	 */
	@Override
	public void start() {
		this.manager.start();
		isRunning = true;

		// Debug
		System.out.println("Starting fs");
	}

	/**
	 * Turns the FeatureSuggestion off. Observers will not be notified upon updates.
	 */
	@Override
	public void stop() {
		this.manager.stop();
		isRunning = false;

		// Debug
		System.out.println("Stopping fs");
	}

	/**
	 * Returns true if FeatureSuggestion
	 */
	@Override
	public boolean isRunning() {
		return isRunning;
	}

	/**
	 * Generate list of FeatureIDs to be tracked
	 *
	 * @return List of FeatureIDs to be tracked
	 */
	private List<String> generateFeatureIDs() {
	    	List<String> feats = new ArrayList<String>();
	    	feats.add(BLOCK_COMMENT_FEATURE_ID);
	    	feats.add(ADD_IMPORT_FEATURE_ID);
	    	feats.add(CORRECT_INDENTATION_FEATURE_ID);
	    	feats.add(REMOVE_IMPORT_FEATURE_ID);
	    	feats.add(TRAILING_WHITE_SPACE_FEATURE_ID);
		return Collections.unmodifiableList(feats);
	}

	/**
	 * Notifies all observers that a feature should be suggested to the user
	 *
	 * @param featureID string that represents a featureID
	 */
	public void notifyAllObservers(String featureID) {
		for (FeatureSuggestionObserver o : observers) {
			o.notify(featureID);
		}
		// DEBUG
		System.out.println("Notified observers of " + featureID);
	}

}

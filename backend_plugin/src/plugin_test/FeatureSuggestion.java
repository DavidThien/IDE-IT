package plugin_test;

import java.util.ArrayList;
import java.util.List;

/** Designed to be used in conjunction with the FeatureSuggestionObserver class. 
 * Observers can register themselves with FeatureSuggestions. FeatureSuggestion will 
 * notify all registered Observers with features it detects the user may want to be 
 * aware of.
 */
public class FeatureSuggestion implements FeatureSuggestionInterface {
	
	private List<FeatureSuggestionObserver> observers;
	private List<String> featureIDs;
	private boolean isRunning;
	
	protected FeatureSuggestion() {
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
		isRunning = true;
	}

	/**
	 * Turns the FeatureSuggestion off. Observers will not be notified upon updates.
	 */
	@Override
	public void stop() {
		isRunning = false;
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
		// Either read from a file or hard code in
		// Pros and cons to both
		List<String> feats = new ArrayList<String>();
		feats.add("multiline_comment");
		return feats;
	}

}
package main.interfaces;

import java.util.List;

/** Designed to be used in conjunction with the FeatureSuggestionObserver class. 
 * Observers can register themselves with FeatureSuggestions. FeatureSuggestion will 
 * notify all registered Observers with features it detects the user may want to be 
 * aware of.
 */
public interface FeatureSuggestionInterface {
	
	/**
	 * Register an Observer to be notified upon FeatureSuggestion updates
	 * @param obs Observer to be registered
	 * @return true upon successful registration, false otherwise
	 */
	public boolean registerObserver(FeatureSuggestionObserver obs);
	
	/**
	 * Removes an observer from the FeatureSuggestion observer list. The observer
	 * will no longer be updated on FeatureSuggestion updates
	 * @param obs Observer to be removed
	 * @return true upon successful removal, false otherwise
	 */
	public boolean removeObserver(FeatureSuggestionObserver obs);
	
	/**
	 * Turns on the FeatureSuggestion. When FeatureSuggestion is on, all registered observers
	 * will be notified upon any FeatureSuggestion updates. 
	 */
	public void start();
	
	/**
	 * Turns off the FeatureSuggestion. When FeatureSuggestion is off, no updates will be provided
	 * to the registered observers.
	 */
	public void stop();
	
	/**
	 * Checks to see is FeatureSuggestion is currently on or off. This can be controlled by start()
	 * and stop()
	 * 
	 * @return true if FeatureSuggestion is currently on
	 */
	public boolean isRunning();
	
	/**
	 * Provides a list of all featureIDs used by FeatureSuggestion.
	 * Recommended to use this list to verify observer featureID list matches
	 * @return List of all featureIDs used by FeatureSuggestion
	 */
	public List<String> getAllFeatureIDs();
}

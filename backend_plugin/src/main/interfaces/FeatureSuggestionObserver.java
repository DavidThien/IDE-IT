package main.interfaces;

/** An Observer abstract class designed to work with the FeatureSuggestion API. The 
 * observer must be registered with the FeatureSuggestion back end to receive updates.
 */
public abstract class FeatureSuggestionObserver {

	/** 
	 * The observer is notified by FeatureSuggestion when it detects a built in Eclipse 
	 * feature that the user would find helpful
	 * 
	 * @param featureID The feature to be suggested to the user
	 */
	public abstract void notify(String featureID);
	
}

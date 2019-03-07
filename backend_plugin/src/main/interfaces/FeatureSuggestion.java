package main.interfaces;

import java.util.ArrayList;
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
	private boolean isRunning;

	public FeatureSuggestion() {
		// debug
		System.out.println("FS created");
		manager = new EvaluatorManager(this);
		observers = new ArrayList<FeatureSuggestionObserver>();
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
		return FeatureID.getAllFeatureIDs();
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

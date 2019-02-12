package evaluators;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import interfaces.FeatureSuggestion;
import listeners.EditorWindowListener;

/**
 * This class keeps track of all evaluators that are open across all
 * document editor windows, and provides the ability to add new
 * evaluators.
 */
public class EvaluatorManager {

	private FeatureSuggestion fs;
	private Map<IEditorPart, Evaluator> openPartEvaluators;
	
	/**
	 * Creates a new EvaluatorManager
	 */
	public EvaluatorManager(FeatureSuggestion fs) {
		this.fs = fs;
		this.openPartEvaluators = new HashMap<IEditorPart, Evaluator>();
		
		// Eric's code to set up a listener over Eclipse as a whole
		for (IWorkbenchWindow window : PlatformUI.getWorkbench().getWorkbenchWindows()) {
			for (IWorkbenchPage page : window.getPages()) {
				for (IEditorReference editRef : page.getEditorReferences()) {
					IEditorPart ePart = editRef.getEditor(false);
					if (ePart != null) {
						IEditorInput eInput = ePart.getEditorInput();
						if (eInput != null) {
							java.lang.String filename = eInput.getName();
							if (filename != null && filename.endsWith(".java") &&
									!this.getOpenEvaluators().containsKey(ePart)) {
								addEvaluator(ePart);
							}
						}
					}
				}
				EditorWindowListener windowListener = new EditorWindowListener(this);
				page.addPartListener(windowListener);
			}
		}
		
		//TODO:
		// Check if there's an open document already, if so then add a listener to that document
	}
	
	/**
	 * Returns the FeatureSuggestion object that all evaluators report to
	 * @return
	 */
	public FeatureSuggestion getFeatureSuggestor() {
		return this.fs;
	}
	
	/**
	 * Returns a map of IEditorParts as keys and their respective
	 * active evaluator objects as values
	 * @return
	 */
	public Map<IEditorPart, Evaluator> getOpenEvaluators() {
		return this.openPartEvaluators;
	}
	
	/**
	 * Takes an IEditorPart and initializes an evaluator for the
	 * IEditorPart window. Adds the evaluator to the list of open
	 * evaluators.
	 * @param editorWindow
	 */
	public void addEvaluator(IEditorPart editorWindow) {
		
		// Create an evaluator for the given editor window
		Evaluator newEvaluator = new Evaluator(this, editorWindow);
		
		// Add this part->evaluator mapping to the list of open evaluators
		this.openPartEvaluators.put(editorWindow, newEvaluator);
	}

	/**
	 * Notifies the FeatureSuggestion that a feature evaluation was triggered
	 * @param featureID
	 */
	public void notifyFeatureSuggestion(String featureID) {
		this.fs.notifyAllObservers(featureID);
	}
}
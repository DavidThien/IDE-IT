package main.evaluators;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.ITextEditor;

import main.interfaces.FeatureSuggestion;
import main.listeners.EditorWindowListener;

/**
 * This class keeps track of all evaluators that are open across all
 * document editor windows, and provides the ability to add new
 * evaluators.
 */
public class EvaluatorManager {

	private FeatureSuggestion fs;
	private Map<IEditorPart, Evaluator> openPartEvaluators;
	private Map<IWorkbenchPage, EditorWindowListener> openWindowListeners;
	
	/**
	 * Creates a new EvaluatorManager
	 */
	public EvaluatorManager(FeatureSuggestion fs) {
		this.fs = fs;
		this.openPartEvaluators = new HashMap<IEditorPart, Evaluator>();
		this.openWindowListeners = new HashMap<IWorkbenchPage, EditorWindowListener>();
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
	public void addEvaluator(ITextEditor textEditor) {
		
		// Create an evaluator for the given editor window
		Evaluator newEvaluator = new Evaluator(this, textEditor);
		
		// Add this part->evaluator mapping to the list of open evaluators
		this.openPartEvaluators.put(textEditor, newEvaluator);
	}

	/**
	 * Notifies the FeatureSuggestion that a feature evaluation was triggered
	 * @param featureID
	 */
	public void notifyFeatureSuggestion(String featureID) {
		this.fs.notifyAllObservers(featureID);
	}

	public void start() {

		// For each workbench page in Eclipse
		for (IWorkbenchWindow window : PlatformUI.getWorkbench().getWorkbenchWindows()) {
			for (IWorkbenchPage page : window.getPages()) {

				// Add a window listener to the page to listen for new windows opening
				EditorWindowListener windowListener = new EditorWindowListener(this);
				page.addPartListener(windowListener);

				// Add the listener to the list of open window listeners
				this.openWindowListeners.put(page, windowListener);

				// For all editor windows that are already open
				for (IEditorReference editRef : page.getEditorReferences()) {
					IEditorPart ePart = editRef.getEditor(false);

					// If the editor window is a text editor
					if (ePart != null && ePart instanceof ITextEditor) {
						ITextEditor textEditor = (ITextEditor) ePart;
						IEditorInput eInput = textEditor.getEditorInput();

						// If the document in the window is a .java document, add an
						// evaluator to the window
						if (eInput != null) {
							String filename = eInput.getName();
							if (filename != null && filename.endsWith(".java") &&
									!this.getOpenEvaluators().containsKey(ePart)) {
								addEvaluator(textEditor);
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Stops all Evaluators and removes any window or document listeners
	 */
	public void stop() {

		// Remove all window listeners that this EvaluatorManager created
		for (IWorkbenchPage workbenchPage : this.openWindowListeners.keySet()) {
			workbenchPage.removePartListener(this.openWindowListeners.get(workbenchPage));
		}
		this.openWindowListeners.clear();

		// Remove all evaluators that this EvaluatorManager created
		for (IEditorPart documentEditor : this.openPartEvaluators.keySet()) {
			this.openPartEvaluators.get(documentEditor).stop();
		}
		this.openPartEvaluators.clear();
	}
}

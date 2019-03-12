package main.evaluators;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.IExecutionListener;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchCommandConstants;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.texteditor.ITextEditor;

import main.interfaces.FeatureSuggestion;
import main.listeners.EditorWindowListener;
import main.listeners.SaveFileListener;

/**
 * This class keeps track of all evaluators that are open across all
 * document editor windows, and provides the ability to add new
 * evaluators.
 */
public class EvaluatorManager {

	// FeatureSuggestion to be notified when an Evaluator catches a feature trigger
	private FeatureSuggestion fs;

	// Keeps track of all open evaluators/listeners to close them later if needed
	private Map<IEditorPart, Evaluator> openPartEvaluators;
	private Map<IWorkbenchPage, EditorWindowListener> openWindowListeners;
	private Map<Command, IExecutionListener> openCommandExecutionListeners;

	/**
	 * Creates a new EvaluatorManager
	 * @param fs The FeatureSuggestion object that this EvaluatorManager should report to
	 */
	public EvaluatorManager(FeatureSuggestion fs) {
		this.fs = fs;
		this.openPartEvaluators = new HashMap<IEditorPart, Evaluator>();
		this.openWindowListeners = new HashMap<IWorkbenchPage, EditorWindowListener>();
		this.openCommandExecutionListeners = new HashMap<Command, IExecutionListener>();
	}

	/**
	 * @return FeatureSuggestion The FeatureSuggestion object that all evaluators report to
	 */
	public FeatureSuggestion getFeatureSuggestor() {
		return this.fs;
	}

	/**
	 * Returns a map of IEditorParts as keys and their respective active evaluator objects
	 * as values
	 * @return Map<IEditorPart, Evaluator> The map of text editor windows to their Evaluators
	 */
	public Map<IEditorPart, Evaluator> getOpenEvaluators() {
		return this.openPartEvaluators;
	}

	/**
	 * Takes an IEditorPart and initializes an evaluator for the IEditorPart window. Adds the
	 * evaluator to the list of open evaluators.
	 * @param textEditor The text editor window to add an evaluator to
	 */
	public void addEvaluator(ITextEditor textEditor) {

		// Create an evaluator for the given editor window
		Evaluator newEvaluator = new Evaluator(this, textEditor);

		// Add this part->evaluator mapping to the list of open evaluators
		this.openPartEvaluators.put(textEditor, newEvaluator);
	}

	/**
	 * Notifies the FeatureSuggestion that a feature evaluation was triggered
	 * @param featureID The unique String ID of the feature triggered
	 */
	public void notifyFeatureSuggestion(String featureID) {
		this.fs.notifyAllObservers(featureID);
	}

	/**
	 * Creates evaluators for each open document window, as well as listeners for additional
	 * document windows opening and for file saves across the Eclipse workspace
	 */
	public void start() {

		// Add a save file listener to the workspace
		ICommandService commandSvc = PlatformUI.getWorkbench().getAdapter(ICommandService.class);
		Command saveCommand = commandSvc.getCommand(IWorkbenchCommandConstants.FILE_SAVE);
		SaveFileListener saveListener = new SaveFileListener(this);
		saveCommand.addExecutionListener(saveListener);
		this.openCommandExecutionListeners.put(saveCommand, saveListener);

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

		// Remove all command execution listeners that this EvaluatorManager created
		for (Command command : this.openCommandExecutionListeners.keySet()) {
			command.removeExecutionListener(this.openCommandExecutionListeners.get(command));
		}
		this.openCommandExecutionListeners.clear();
	}

	/**
	 * Signals the evaluators to check for unused imports
	 */
	public void workspaceResourceSaved() {
		for (Evaluator eval : this.getOpenEvaluators().values()) {
			if (eval.workspaceResourceSaved()) {
				this.notifyFeatureSuggestion("removeUnusedImportStatementSuggestion");
				break;
			}
		}
	}
}

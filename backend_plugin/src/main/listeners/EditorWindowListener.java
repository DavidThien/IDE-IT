package main.listeners;

import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.texteditor.ITextEditor;

import main.evaluators.EvaluatorManager;

/**
 * This listener class detects actions regarding windows in the Eclipse UI,
 * such as opening a document in a new editor window, switching between editor tabs,
 * etc. Its purpose is to manage adding listeners to each document editor
 * when necessary.
 */
public class EditorWindowListener implements IPartListener2 {

	private EvaluatorManager em;

	/**
	 * Creates a new EditorWindowListener
	 * @param em the EvaluatorManager managing workspace-wide listeners
	 */
	public EditorWindowListener(EvaluatorManager em) {
		this.em = em;
	}

	/**
	 * Adds an evaluator to a window when the user switches to it (i.e. switching tabs).
	 * Only adds the evaluator if the window is a document editor containing a .java
	 * file, and the window does not already have an evaluator assigned to it.
	 * @param partRef The reference to the Eclipse part that was activated
	 */
	@Override
	public void partActivated(IWorkbenchPartReference partRef) {
		IWorkbenchPart part = partRef.getPart(false);

		// If the window is a text editor window
		if (part instanceof ITextEditor) {
			ITextEditor editor = (ITextEditor) part;
			IEditorInput input = editor.getEditorInput();
			String filename = input.getName();

			// If the document inside the text editor window is a .java file and the
			// text editor window does not already have an evaluator assigned to it,
			// Assign an evaluator to the text editor window
			if (filename.endsWith(".java") && !em.getOpenEvaluators().containsKey(editor)) {
				em.addEvaluator(editor);
			}
		}
	}

	/**
	 * Adds an evaluator to a newly opened window in the Eclipse UI. Only adds the evaluator
	 * if the window is a document editor containing a .java file.
	 * @param partRef The reference to the Eclipse part that was activated
	 */
	@Override
	public void partOpened(IWorkbenchPartReference partRef) {
		IWorkbenchPart part = partRef.getPart(false);

		// If the window is a text editor window
		if (part instanceof ITextEditor) {
			ITextEditor editor = (ITextEditor) part;
			IEditorInput input = editor.getEditorInput();
			String filename = input.getName();

			// If the document inside the text editor window is a .java file, assign an evaluator
			// to the text editor window
			if (filename.endsWith(".java")) {
				em.addEvaluator(editor);
			}
		}
	}

	/**
	 * Unused
	 */
	@Override
	public void partBroughtToTop(IWorkbenchPartReference partRef) {
	}

	/**
	 * Unused
	 */
	@Override
	public void partClosed(IWorkbenchPartReference partRef) {
	}

	/**
	 * Unused
	 */
	@Override
	public void partDeactivated(IWorkbenchPartReference partRef) {
	}

	/**
	 * Unused
	 */
	@Override
	public void partHidden(IWorkbenchPartReference partRef) {
	}

	/**
	 * Unused
	 */
	@Override
	public void partVisible(IWorkbenchPartReference partRef) {
	}

	/**
	 * Unused
	 */
	@Override
	public void partInputChanged(IWorkbenchPartReference partRef) {
	}
}

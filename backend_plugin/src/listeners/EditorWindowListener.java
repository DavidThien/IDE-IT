package listeners;

import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;

import evaluators.EvaluatorManager;

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
	 * @param em
	 */
	public EditorWindowListener(EvaluatorManager em) {
		this.em = em;
	}

	/**
	 * Adds an evaluator to a window when the user switches to it (i.e. switching tabs).
	 * Only adds the evaluator if the window is a document editor containing a .java
	 * file, and the window does not already have an evaluator assigned to it.
	 */
	@Override
	public void partActivated(IWorkbenchPartReference partRef) {
		// Called when a window is activated (i.e. user switches to a tab)
		IWorkbenchPart part = partRef.getPart(false);
		if (part instanceof IEditorPart) {
			IEditorPart editor = (IEditorPart) part;
			IEditorInput input = editor.getEditorInput();
			String filename = input.getName();		
			if (filename.endsWith(".java") && !em.getOpenEvaluators().containsKey(editor)) {
				// This window is active, but does not yet have an evaluator attached to it.
				// This might be the case when the user first opens Eclipse, and already has
				// some open documents
				em.addEvaluator(editor);
			}
		}
	}

	@Override
	public void partBroughtToTop(IWorkbenchPartReference partRef) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void partClosed(IWorkbenchPartReference partRef) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void partDeactivated(IWorkbenchPartReference partRef) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Adds an evaluator to a newly opened window in the Eclipse UI.
	 * Only adds the evaluator if the window is a document editor
	 * containing a .java file 
	 */
	@Override
	public void partOpened(IWorkbenchPartReference partRef) {
		// Called when a window is first opened.
		IWorkbenchPart part = partRef.getPart(false);
		if (part instanceof IEditorPart) {
			IEditorPart editor = (IEditorPart) part;
			IEditorInput input = editor.getEditorInput();
			String filename = input.getName();		
			if (filename.endsWith(".java")) {
				// This window is a document editor containing
				// a Java file, so assign an evaluator to it			
				em.addEvaluator(editor);
			}
		}
	}

	@Override
	public void partHidden(IWorkbenchPartReference partRef) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void partVisible(IWorkbenchPartReference partRef) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void partInputChanged(IWorkbenchPartReference partRef) {
		// TODO Auto-generated method stub
		
	}

}

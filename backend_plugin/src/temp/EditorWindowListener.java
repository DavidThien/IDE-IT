package temp;

import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;

import evaluators.Evaluator;
import plugin_test.FeatureSuggestion;

/**
 * This listener class detects actions regarding windows that open
 * in the UI, such as opening a document in a new editor window. Its
 * purpose is to manage adding listeners to each document editor
 * when it opens.
 */
public class EditorWindowListener implements IPartListener2 {
	
	private FeatureSuggestion fs;
	
	public EditorWindowListener(FeatureSuggestion fs) {
		this.fs = fs;
	}

	@Override
	public void partActivated(IWorkbenchPartReference partRef) {
		// TODO Auto-generated method stub
		
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

	@SuppressWarnings("unused")
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
				Evaluator windowEvaluator = new Evaluator(this.fs);
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

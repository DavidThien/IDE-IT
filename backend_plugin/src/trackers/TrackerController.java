package trackers;

import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.ITextEditor;

import evaluators.Evaluator;

/*
 * TrackerController is designed to be in charge of creating new listeners when appropriate. i.e. When a new editor
 * window is open, then TrackerController should create a new DocumentListner for that editor.
 */
public class TrackerController {

	private Evaluator eval;
	
	/**
	 * Default constructor
	 * @param eval Evaluator that is passed document change events 
	 */
	public TrackerController(Evaluator eval) {
		this.eval = eval;
	}
	
	/**
	 * Creates a new Document Listener for the currently selected text editor window.
	 */
	public void createDocumentListener() {
		System.out.println("Starting to listen to key input....");
	
		// 	Gets the currently selected text editor window
		IEditorPart editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		ITextEditor ite = (ITextEditor)editor;
	
		// Gets the document stored inside that text editor window
		IDocument doc = ite.getDocumentProvider().getDocument(ite.getEditorInput());
    
		// 	Adds a MyDocumentListener to the document inside the text editor window
		DocumentChangesTracker docTracker = new DocumentChangesTracker(eval);
		doc.addDocumentListener(docTracker);
	}
}

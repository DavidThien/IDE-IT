package PossibleKeyPressHandler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.jface.dialogs.MessageDialog;

public class SampleHandler extends AbstractHandler {
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		// Not sure if we need this line for anything at the moment
		// IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		
		// We might not need the KeyListenerHandler object; we might be able to
		// directly add a MyDocumentListener to the current active editor window
		KeyListenerHandler testHandler = new KeyListenerHandler();
		testHandler.startListening();
		return null;
	}
}
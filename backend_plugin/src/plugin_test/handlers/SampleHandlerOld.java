package plugin_test.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.jface.dialogs.MessageDialog;
import plugin_editor_test.*;

// new stuff


public class SampleHandlerOld extends AbstractHandler {

	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		
		// Debug info
		System.out.println("Menu item works");
		
		// Key Listener - there should be a much better way to handle this
		KeyListenerHandlerOld testHandler = new KeyListenerHandlerOld();
		testHandler.earlyStartup();
		
		// Opens a dialog box with some generic info
		MessageDialog.openInformation(
				window.getShell(),
				"Plugin_test",
				"Hello, Eclipse world");
		
		return null;
	}
}

package plugin_test.handlers;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.IStartup;

public class KeyListenerHandler implements IStartup {
	
	private String typedChars;
	
	@Override
	public void earlyStartup() {
		System.out.println("Key Listener start up");
		Display display = Display.getDefault();
		display.addFilter(SWT.KeyUp, new Listener() {
			@Override
			public void handleEvent(Event event) {
				System.out.println(event.character);
				typedChars += event.character;
			}
		});
	}
	
	String getTypedChars() {
		return typedChars;
	}
}

package main.activation;

import org.eclipse.ui.IStartup;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import main.interfaces.FeatureSuggestion;

/**
 * This class facilitates testing the backend plugin by activating the plugin automatically
 * when running it as an Eclipse application within Eclipse. This allows for testing the backend
 * functionality on its own without having to integrate it with a frontend plugin.
 */
public class Startup implements IStartup {

	@Override
	public void earlyStartup() {
		final IWorkbench workbench = PlatformUI.getWorkbench();
		workbench.getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
				if (window != null) {

					// Once the plugin is loaded, make a new FeatureSuggestion
					FeatureSuggestion fs = new FeatureSuggestion();
					fs.start();
				}
			}
		});
	}
}

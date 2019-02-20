package main.activation;

import org.eclipse.ui.IStartup;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import main.interfaces.FeatureSuggestion;

public class Startup implements IStartup {
	
	@Override
	public void earlyStartup() {
		final IWorkbench workbench = PlatformUI.getWorkbench();
		workbench.getDisplay().asyncExec(new Runnable() {
			public void run() {
				IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
				if (window != null) {
					// Once we are loaded, make a new FeatureSuggestion
					// Need to determine how FE will get the FeatureSuggestion object
					FeatureSuggestion fs = new FeatureSuggestion();
					fs.start();
				}
			}
		});
		//BundleContextImpl.this
	}
}

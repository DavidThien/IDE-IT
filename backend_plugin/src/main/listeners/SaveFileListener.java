package main.listeners;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IExecutionListener;
import org.eclipse.core.commands.NotHandledException;

import main.evaluators.EvaluatorManager;

/**
 * Listens for a file being saved within the workspace. Should only be added to
 * the FILE_SAVE command.
 */
public class SaveFileListener implements IExecutionListener {

	private EvaluatorManager em;

	/**
	 * Constructs a SaveFileListener
	 */
	public SaveFileListener(EvaluatorManager em) {
		this.em = em;
	}

	@Override
	public void notHandled(String commandId, NotHandledException exception) {
	}

	@Override
	public void postExecuteFailure(String commandId, ExecutionException exception) {
	}

	/**
	 * Notifies the EvaluatorManager that a workspace resource (file) was successfully
	 * saved
	 */
	@Override
	public void postExecuteSuccess(String commandId, Object returnValue) {
		em.workspaceResourceSaved();
	}

	@Override
	public void preExecute(String commandId, ExecutionEvent event) {
	}

}
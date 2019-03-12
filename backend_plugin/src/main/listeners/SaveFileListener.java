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

    	/** EvaluatorManager that this listener reports to when the workspace is saved */
	private EvaluatorManager em;

	/**
	 * Constructs a SaveFileListener
	 * @param em The EvaluatorManager that this reports workspace file saves to
	 */
	public SaveFileListener(EvaluatorManager em) {
		this.em = em;
	}

	/**
	 * Notifies the EvaluatorManager that a workspace resource (file) was successfully
	 * saved
	 * @param commandId The String ID of the command that was executed (FILE_SAVE in this case)
	 * @param returnValue null in this case
	 */
	@Override
	public void postExecuteSuccess(String commandId, Object returnValue) {
		em.workspaceResourceSaved();
	}

	/**
	 * Unused
	 */
	@Override
	public void notHandled(String commandId, NotHandledException exception) {
	}

	/**
	 * Unused
	 */
	@Override
	public void postExecuteFailure(String commandId, ExecutionException exception) {
	}

	/**
	 * Unused
	 */
	@Override
	public void preExecute(String commandId, ExecutionEvent event) {
	}
}
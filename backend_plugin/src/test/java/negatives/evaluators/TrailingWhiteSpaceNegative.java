package test.java.negatives.evaluators;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.junit.Before;
import org.junit.Test;

import main.evaluators.TrailingWhiteSpaceEvaluator;

public class TrailingWhiteSpaceNegative {
	
	private static final String FIRST_LINE = "One trailing space \n";
	private static final String SECOND_LINE = " Two trailing spaces  \n";
	private static final String THIRD_LINE = " One trailing tab\t\n";
	private static final String FOURTH_LINE = " Two trailing tabs\t\t\n";
	private TrailingWhiteSpaceEvaluator eval;
	private IDocument doc;
	
	/**
	 * Initialize a new document and a new evaluator
	 */
	@Before
	public void runBeforeTests() {
		this.doc = new Document(FIRST_LINE + SECOND_LINE + THIRD_LINE + FOURTH_LINE);
		this.eval = new TrailingWhiteSpaceEvaluator(doc);
	}
	
	/**
	 * Tests the user deleting the one trailing white space at the end
	 * of the first line
	 */
	@Test
	public void removeAllTrailingSpacesOneSpace() {
		try {
			
			// Start at the end of the first line
			int offset = doc.getLineOffset(1) - 1;
			
			// Remove the single white space
			assertTrue(mockUserSingleBackspace(offset--));
		} catch (BadLocationException e) {
			// Should never get here
			fail("Should never see this error in: " + this.getClass().getSimpleName() + "::" + this.getClass().getName());
			e.printStackTrace();
		}
	}
	
	/**
	 * Tests the user deleting the two trailing white spaces at the end
	 * of the second line with two single backspaces
	 */
	@Test
	public void removeAllTrailingSpacesTwoSpacesTwoBackspaces() {
		try {
			
			// Start at the end of the second line
			int offset = doc.getLineOffset(2) - 1;
			
			// Remove both white spaces (should trigger on the second)
			assertFalse(mockUserSingleBackspace(offset--));
			assertTrue(mockUserSingleBackspace(offset--));
		} catch (BadLocationException e) {
			// Should never get here
			fail("Should never see this error in: " + this.getClass().getSimpleName() + "::" + this.getClass().getName());
			e.printStackTrace();
		}
	}
	
	/**
	 * Tests the user deleting the two trailing white spaces at the end
	 * of the second line by selecting the two white spaces and hitting
	 * backspace once
	 */
	@Test
	public void removeAllTrailingSpacesTwoSpacesOneBackspace() {
		try {
			
			// Start at the end of the second line
			int offset = doc.getLineOffset(2) - 1;
			
			// Remove both white spaces with a single select/backspace combo
			assertTrue(mockUserSelectingTwoCharactersAndBackspacing(offset));
		} catch (BadLocationException e) {
			// Should never get here
			fail("Should never see this error in: " + this.getClass().getSimpleName() + "::" + this.getClass().getName());
			e.printStackTrace();
		}
	}
	
	/**
	 * Tests the user deleting the one trailing tab at the end of the third line
	 */
	@Test
	public void removeAllTrailingTabsOneTab() {
		try {
			
			// Start at the end of the third line
			int offset = doc.getLineOffset(3) - 1;
			
			// Remove the single tab with a backspace
			assertTrue(mockUserSingleBackspace(offset--));
		} catch (BadLocationException e) {
			// Should never get here
			fail("Should never see this error in: " + this.getClass().getSimpleName() + "::" + this.getClass().getName());
			e.printStackTrace();
		}
	}
	
	/**
	 * Tests the user deleting the two trailing tabs at the end of the fourth line
	 * with two single backspaces
	 */
	@Test
	public void removeAllTrailingTabsTwoTabsTwoBackspaces() {
		try {
			
			// Start at the end of the third line
			int offset = doc.getLineOffset(4) - 1;
			
			// Remove the tabs with two backspaces (should trigger on the second)
			assertFalse(mockUserSingleBackspace(offset--));
			assertTrue(mockUserSingleBackspace(offset--));
		} catch (BadLocationException e) {
			// Should never get here
			fail("Should never see this error in: " + this.getClass().getSimpleName() + "::" + this.getClass().getName());
			e.printStackTrace();
		}
	}
	
	/**
	 * Tests the user deleting the two trailing tabs at the end of the fourth line
	 * by selecting the two tabs and hitting backspace once
	 */
	@Test
	public void removeAllTrailingTabsTwoTabsOneBackspace() {
		try {
			
			// Start at the end of the third line
			int offset = doc.getLineOffset(4) - 1;
			
			// Remove the tabs with a single select/backspace combo
			assertTrue(mockUserSelectingTwoCharactersAndBackspacing(offset));
		} catch (BadLocationException e) {
			// Should never get here
			fail("Should never see this error in: " + this.getClass().getSimpleName() + "::" + this.getClass().getName());
			e.printStackTrace();
		}
	}

	/**
	 * Mocks the user typing the given string into the document at
	 * the given offset. Evaluates the result to see if the feature
	 * evaluation has been triggered
	 * @param input
	 * @param offset
	 */
	private boolean mockUserInput(String input, int offset) throws BadLocationException {
		DocumentEvent event = new DocumentEvent(doc, offset, input.length(), input);
		eval.evaluateDocumentBeforeChange(event);
		doc.replace(offset, 0, input);
		return eval.evaluateDocumentChanges(event);
	}

	private boolean mockUserSingleBackspace(int offset) throws BadLocationException {
		DocumentEvent event = new DocumentEvent(doc, offset-1, 1, "");
		eval.evaluateDocumentBeforeChange(event);
		doc.replace(offset-1, 1, "");
		return eval.evaluateDocumentChanges(event);
	}
	
	private boolean mockUserSelectingTwoCharactersAndBackspacing(int offset) throws BadLocationException {
		DocumentEvent event = new DocumentEvent(doc, offset-2, 2, "");
		eval.evaluateDocumentBeforeChange(event);
		doc.replace(offset-2, 2, "");
		return eval.evaluateDocumentChanges(event);
	}
}
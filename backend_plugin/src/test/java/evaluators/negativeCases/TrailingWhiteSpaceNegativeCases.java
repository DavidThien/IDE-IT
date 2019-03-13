package test.java.evaluators.negativeCases;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.junit.Before;
import org.junit.Test;

import main.evaluators.TrailingWhiteSpaceEvaluator;

public class TrailingWhiteSpaceNegativeCases {
	/** A string to populate one line of a mock document */
	private static final String FIRST_LINE = "One trailing space \n";
	/** A string to populate one line of a mock document */
	private static final String SECOND_LINE = " Two trailing spaces  \n";
	/** A string to populate one line of a mock document */
	private static final String THIRD_LINE = " One trailing tab\t\n";
	/** A string to populate one line of a mock document */
	private static final String FOURTH_LINE = " Two trailing tabs\t\t\n";
	/** A string to populate one line of a mock document */
	private static final String FIFTH_LINE = " No trailing spaces";

	/** Evaluator being tested */
	private TrailingWhiteSpaceEvaluator eval;
	/** Document attached to the evaluator */
	private IDocument doc;

	/**
	 * Initialize a new document and a new evaluator
	 */
	@Before
	public void runBeforeTests() {
		this.doc = new Document(FIRST_LINE + SECOND_LINE + THIRD_LINE + FOURTH_LINE + FIFTH_LINE);
		this.eval = new TrailingWhiteSpaceEvaluator(doc);
	}

	/**
	 * Tests the user adding a second space to the end of the first line, then deleting it
	 */
	@Test
	public void addExtraTrailingSpaceAfterSpaceThenDeleteOnlyLastOne() {
		try {

			// Start at the end of the first line
			int offset = doc.getLineOffset(1) - 1;

			// Add a second space to the end of the line
			assertFalse(mockUserInput(" ", offset++));

			// Remove only the second added white space (should NOT trigger)
			assertFalse(mockUserSingleBackspace(offset--));
		} catch (BadLocationException e) {
			// Should never get here
			fail("Should never see this error in: " + this.getClass().getSimpleName() + "::" + this.getClass().getName());
			e.printStackTrace();
		}
	}

	/**
	 * Tests the user deleting only one of the two trailing white spaces at the end
	 * of the second line with a single backspace
	 */
	@Test
	public void removeOneOfTwoTrailingSpacesWithBackspace() {
		try {

			// Start at the end of the second line
			int offset = doc.getLineOffset(2) - 1;

			// Remove both white spaces (should trigger on the second)
			assertFalse(mockUserSingleBackspace(offset--));
		} catch (BadLocationException e) {
			// Should never get here
			fail("Should never see this error in: " + this.getClass().getSimpleName() + "::" + this.getClass().getName());
			e.printStackTrace();
		}
	}

	/**
	 * Tests the user adding a space to the end of the third line after the tab, then deleting it
	 */
	@Test
	public void addExtraTrailingSpaceAfterTabThenDeleteOnlySpace() {
		try {

			// Start at the end of the third line
			int offset = doc.getLineOffset(3) - 1;

			// Add a second space to the end of the line
			assertFalse(mockUserInput(" ", offset++));

			// Remove only the single space with a backspace (should NOT trigger)
			assertFalse(mockUserSingleBackspace(offset--));
		} catch (BadLocationException e) {
			// Should never get here
			fail("Should never see this error in: " + this.getClass().getSimpleName() + "::" + this.getClass().getName());
			e.printStackTrace();
		}
	}

	/**
	 * Tests the user deleting the last character of a line that has no trailing white spaces
	 */
	@Test
	public void deleteLastCharacterOfLineWithNoTrailingWhiteSpaces() {
		try {

			// Start at the end of the fifth line
			int offset = doc.getLineOffset(4);

			// Remove the last character (should NOT trigger, since it is not whitespace)
			assertFalse(mockUserSingleBackspace(offset--));
		} catch (BadLocationException e) {
			// Should never get here
			fail("Should never see this error in: " + this.getClass().getSimpleName() + "::" + this.getClass().getName());
			e.printStackTrace();
		}
	}

	/**
	 * Tests the user deleting the last character of a line that has no trailing white spaces
	 */
	@Test
	public void addingWhiteSpaceToEndOfLineWithNoTrailingWhiteSpaces() {
		try {

			// Start at the end of the fifth line
			int offset = doc.getLineOffset(4);

			// Add a space to the end of the line (should NOT trigger)
			assertFalse(mockUserInput(" ", offset++));
		} catch (BadLocationException e) {
			// Should never get here
			fail("Should never see this error in: " + this.getClass().getSimpleName() + "::" + this.getClass().getName());
			e.printStackTrace();
		}
	}

	/**
	 * Tests the user deleting all leading white spaces
	 */
	@Test
	public void deleteAllLeadingWhiteSpaces() {
		try {

			// Start after the leading whitespace of the second line
			int offset = doc.getLineOffset(1) + 1;

			// Remove the last character (should NOT trigger, since it is leading whitespace)
			assertFalse(mockUserSingleBackspace(offset--));
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
	 * @param input The String to enter into the document
	 * @param offset The offset in the document to enter the String
	 * @return true if the feature evaluation is triggered by the input; false otherwise
	 * @throws BadLocationException
	 */
	private boolean mockUserInput(String input, int offset) throws BadLocationException {
		DocumentEvent event = new DocumentEvent(doc, offset, input.length(), input);
		eval.evaluateDocumentBeforeChange(event);
		doc.replace(offset, 0, input);
		return eval.evaluateDocumentChanges(event);
	}

	/**
	 * Mocks the user making a single backspace from the given offset.
	 * Evaluates the result to see if the feature evaluation has been
	 * triggered.
	 * @param offset The offset in the document to enter the String
	 * @return true if the feature evaluation is triggered by the backspace; false otherwise
	 * @throws BadLocationException
	 */
	private boolean mockUserSingleBackspace(int offset) throws BadLocationException {
		DocumentEvent event = new DocumentEvent(doc, offset-1, 1, "");
		eval.evaluateDocumentBeforeChange(event);
		doc.replace(offset-1, 1, "");
		return eval.evaluateDocumentChanges(event);
	}
}

package test.java;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.junit.Before;
import org.junit.Test;

import main.evaluators.BlockCommentEvaluator;

/**
 * Unit test for BlockCommentEvaluator
 */
public class BlockCommentEvaluatorTest {

    	/** Initial content for the mock Document */
	private static final String content = "Line1\n Line2\n Line3\n";
	/** Single slash that can be referenced from a constant */
	private static final String SINGLE_SLASH = "/";

	/** Document to be attached to the evaluator */
	private IDocument doc;
	/** Evaluator being tested */
	private BlockCommentEvaluator testEvaluator;

	/** Document event to store document changes */
	private DocumentEvent event;
	/** Offset of where the document changes occurred in the document */
	private int offset;

	/**
	 * Initialize a new document and a new evaluator
	 */
	@Before
	public void runBeforeTests() {
		// Create a document with four lines
		doc = new Document(content);
		testEvaluator = new BlockCommentEvaluator(doc);
	}

	/**
	 * Tests the block comment evaluation function returns true when line 0 then line 1 of a document are commented out
	 */
	@Test
	public void twoConsecutiveLinesDownCommentedOut() {
		// Mock a document event with a single backslash placed at the beginning of the first line
		try {
			// Mock a document event with a single backslash placed at the beginning of the first line
			offset = 0;
			doc.replace(offset, 0, SINGLE_SLASH);
			event = createDocEvent(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));

			// Place a second backslash after the first
			offset++;
			doc.replace(offset, 0, SINGLE_SLASH);
			event = createDocEvent(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));

			delayUserInput();

			// Place a single backslash at the start of the second line
			offset = doc.getLineOffset(1);
			doc.replace(offset, 0, SINGLE_SLASH);
			event = createDocEvent(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));

			// Place a second backslash after the first
			offset++;
			doc.replace(offset, 0, SINGLE_SLASH);
			event = createDocEvent(offset, SINGLE_SLASH);

			// Now the evaluation function should trigger
			assertTrue(testEvaluator.evaluateDocumentChanges(event));
		} catch (BadLocationException e) {
			// Should never get here
			fail("Should never see this error in: " + this.getClass().getSimpleName() + "::" + this.getClass().getName());
			e.printStackTrace();
		}
	}

	/**
	 * Tests the block comment evaluation function returns true when line 2 then line 1 of a document are commented out
	 */
	@Test
	public void twoConsecutiveLinesUpCommentedOut() {
		// Create mock document event changes
		try {
			// Mock a document event with a single backslash placed at the beginning of the third line
			offset = doc.getLineOffset(2);
			doc.replace(offset, 0, SINGLE_SLASH);
			event = createDocEvent(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));

			// Place a second backslash after the first
			offset++;
			doc.replace(offset, 0, SINGLE_SLASH);
			event = createDocEvent(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));

			delayUserInput();

			// Mock a document event with a single backslash placed at the beginning of the second line
			offset = doc.getLineOffset(1);
			doc.replace(offset, 0, SINGLE_SLASH);
			event = createDocEvent(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));

			// Place another backslash after the previous one
			offset++;
			doc.replace(offset, 0, SINGLE_SLASH);
			event = createDocEvent(offset, SINGLE_SLASH);
			// Now the evaluation function should trigger
			assertTrue(testEvaluator.evaluateDocumentChanges(event));
		} catch (BadLocationException e) {
			// Should never get here
			fail("Should never see this error in: " + this.getClass().getSimpleName() + "::" + this.getClass().getName());
			e.printStackTrace();
		}
	}

	/**
	 * Verify that evaluation function triggers if two consecutive lines are commented out and triggers again
	 * if another consecutive line is triggered
	 */
	@Test
	public void threeConsecutiveLinesDownCommented() {
		try {
			// Send mock data
			// Mock a document event with a single backslash placed at the beginning of the first line
			offset = 0;
			doc.replace(offset, 0, SINGLE_SLASH);
			event = createDocEvent(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));
			// Place a second backslash after the first

			offset++;
			doc.replace(offset, 0, SINGLE_SLASH);
			event = createDocEvent(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));

			delayUserInput();

			// Pull the offset for the start of the second line so we aren't guessing
			// Place a single backslash at the start of the second line
			offset = doc.getLineOffset(1);
			doc.replace(offset, 0, SINGLE_SLASH);
			event = createDocEvent(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));

			// Place another backslash after the previous one
			offset++;
			doc.replace(offset, 0, SINGLE_SLASH);
			event = createDocEvent(offset, SINGLE_SLASH);
			// Now the evaluation function should trigger
			assertTrue(testEvaluator.evaluateDocumentChanges(event));

			delayUserInput();

			// Comment out the third line
			// Place a single backslash at the start of the third line
			offset = doc.getLineOffset(2);
			doc.replace(offset, 0, SINGLE_SLASH);
			event = createDocEvent(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));

			// Place another backslash after the previous one
			offset++;
			doc.replace(offset, 0, SINGLE_SLASH);
			event = createDocEvent(offset, SINGLE_SLASH);
			// Now the evaluation function should trigger
			assertTrue(testEvaluator.evaluateDocumentChanges(event));
		} catch (BadLocationException e) {
			// Should never get here
			fail("Should never see this error in: " + this.getClass().getSimpleName() + "::" + this.getClass().getName());
			e.printStackTrace();
		}
	}

	/**
	 * Tests the block comment evaluation function returns true when line 3 then line 2 then line 1 are commented out
	 */
	@Test
	public void threeConsecutiveLinesUpCommentedOut() {
		try {
			// Mock a document event with a single backslash placed at the beginning of the third line
			offset = doc.getLineOffset(2);
			doc.replace(offset, 0, SINGLE_SLASH);
			event = createDocEvent(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));

			// Place a second backslash after the first
			offset++;
			doc.replace(offset, 0, SINGLE_SLASH);
			event = createDocEvent(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));

			delayUserInput();

			// Mock a document event with a single backslash placed at the beginning of the second line
			offset = doc.getLineOffset(1);
			doc.replace(offset, 0, SINGLE_SLASH);
			event = createDocEvent(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));

			// Place another backslash after the previous one
			offset++;
			doc.replace(offset, 0, SINGLE_SLASH);
			event = createDocEvent(offset, SINGLE_SLASH);
			// Now the evaluation function should trigger
			assertTrue(testEvaluator.evaluateDocumentChanges(event));

			delayUserInput();

			// Mock a document event with a single backslash placed at the beginning of the first line
			offset = doc.getLineOffset(0);
			doc.replace(offset, 0, SINGLE_SLASH);
			event = createDocEvent(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));

			// Place another backslash after the previous one
			offset++;
			doc.replace(offset, 0, SINGLE_SLASH);
			event = createDocEvent(offset, SINGLE_SLASH);
			// Now the evaluation function should trigger
			assertTrue(testEvaluator.evaluateDocumentChanges(event));
		} catch (BadLocationException e) {
			// Should never get here
			fail("Should never see this error in: " + this.getClass().getSimpleName() + "::" + this.getClass().getName());
			e.printStackTrace();
		}
	}

	/**
	 * Verifies that the evaluation function will not trigger when the user types "//" anywhere but the start
	 * of a line
	 */
	@Test
	public void commentNotAtStartOfLineDown() {
		try {
			// Mock a document event with a single backslash placed at the beginning of the first line
			offset = 0;
			doc.replace(offset, 0, SINGLE_SLASH);
			event = createDocEvent(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));
			// Place a second backslash after the first
			offset++;
			doc.replace(offset, 0, SINGLE_SLASH);
			event = createDocEvent(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));

			delayUserInput();

			// Pull the offset for the start of the second line so we aren't guessing
			offset = doc.getLineOffset(1);
			// Add two to the offset so we're in the middle of the line
			offset += 2;
			doc.replace(offset, 0, SINGLE_SLASH);
			event = createDocEvent(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));

			// Place another backslash after the previous one
			offset++;
			doc.replace(offset, 0, SINGLE_SLASH);
			event = createDocEvent(offset, SINGLE_SLASH);
			// Now the evaluation function should not trigger
			assertFalse(testEvaluator.evaluateDocumentChanges(event));
		} catch (BadLocationException e) {
			// Should never get here
			fail("Should never see this error in: " + this.getClass().getSimpleName() + "::" + this.getClass().getName());
			e.printStackTrace();
		}
	}

	/**
	 * Verifies that the evaluation function will not trigger when the user types "//" anywhere but the start
	 * of a line
	 */
	@Test
	public void commentNotAtStartOfLineUp() {
		try {
			// Mock a document event with a single backslash placed at the beginning of the third line
			offset = doc.getLineOffset(2);
			doc.replace(offset, 0, SINGLE_SLASH);
			event = createDocEvent(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));

			// Place a second backslash after the first
			offset++;
			doc.replace(offset, 0, SINGLE_SLASH);
			event = createDocEvent(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));

			delayUserInput();

			// Mock a document event with a single backslash placed at the beginning of the second line
			offset = doc.getLineOffset(1);
			// Add two to get to mid line
			offset += 2;
			doc.replace(offset, 0, SINGLE_SLASH);
			event = createDocEvent(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));

			// Place another backslash after the previous one
			offset++;
			doc.replace(offset, 0, SINGLE_SLASH);
			event = createDocEvent(offset, SINGLE_SLASH);
			// Now the evaluation function should trigger
			assertFalse(testEvaluator.evaluateDocumentChanges(event));
		} catch (BadLocationException e) {
			// Should never get here
			fail("Should never see this error in: " + this.getClass().getSimpleName() + "::" + this.getClass().getName());
			e.printStackTrace();
		}
	}

	/**
	 * Helper method to create a new document event given the offset and text to be added
	 * @param offset position in the document to add the text
	 * @param text to be added to the document
	 * @return
	 */
	private DocumentEvent createDocEvent(int offset, String text) {
		return new DocumentEvent(doc, offset, text.length(), text);
	}

	/**
	 * Helper method to delay 100 ms between document changes. This allows us to more realistically mock user input
	 */
	private void delayUserInput() {
		// Wait for 101 ms to simulate the time it takes the user to manually move to the line above
		long startTime = System.currentTimeMillis();
		while (System.currentTimeMillis() - startTime <= 100) {}
	}
}

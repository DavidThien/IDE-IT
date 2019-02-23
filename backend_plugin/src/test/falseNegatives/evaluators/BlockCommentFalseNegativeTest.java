package test.falseNegatives.evaluators;

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
 * Tests against all known sequences of actions a user can take to manually attempt to comment out multiple lines
 * of code in their document
 */
public class BlockCommentFalseNegativeTest {
	
	private static final String content = "Line1\n Line2\n Line3\n";
	private static final String SINGLE_SLASH = "/";
	
	private IDocument doc;
	private BlockCommentEvaluator testEvaluator;
	
	// Used to store mock event data
	private DocumentEvent event;
	private int offset;

	/**
	 * Initialize a new document and a new evaluator
	 */
	@Before
	public void runBeforeTests() {
		// Create a document with four lines
		doc = new Document(content);
		testEvaluator = new BlockCommentEvaluator();
	}
	
	/**
	 * Tests the block comment evaluation function returns true when line 0 then line 1 of a document are commented out
	 */
	@Test
	public void twoConsecutiveLinesDownCommentedOut() {

		// Mock a document event with a single backslash placed at the beginning of the first line
		offset = 0;
		event = createDocEvent(offset, SINGLE_SLASH);
		assertFalse(testEvaluator.evaluate(event));
		// Place a second backslash after the first
		offset++;
		event = createDocEvent(offset, SINGLE_SLASH);
		assertFalse(testEvaluator.evaluate(event));
		
		// Pull the offset for the start of the second line so we aren't guessing
		try {
			// Place a single backslash at the start of the second line
			offset = doc.getLineOffset(1);
			event = createDocEvent(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluate(event));
			
			// Place another backslash after the previous one
			offset++;
			event = createDocEvent(offset, SINGLE_SLASH);
			// Now the evaluation function should trigger
			assertTrue(testEvaluator.evaluate(event));
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
			event = createDocEvent(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluate(event));
		
			// Place a second backslash after the first
			offset++;
			event = createDocEvent(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluate(event));
		
			// Mock a document event with a single backslash placed at the beginning of the second line
			offset = doc.getLineOffset(1);
			event = createDocEvent(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluate(event));
			
			// Place another backslash after the previous one
			offset++;
			event = createDocEvent(offset, SINGLE_SLASH);
			// Now the evaluation function should trigger
			assertTrue(testEvaluator.evaluate(event));
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

		// Send mock data
		// Mock a document event with a single backslash placed at the beginning of the first line
		offset = 0;
		event = createDocEvent(offset, SINGLE_SLASH);
		assertFalse(testEvaluator.evaluate(event));
		// Place a second backslash after the first
		
		offset++;
		event = createDocEvent(offset, SINGLE_SLASH);
		assertFalse(testEvaluator.evaluate(event));

		// Pull the offset for the start of the second line so we aren't guessing
		try {
			// Place a single backslash at the start of the second line
			offset = doc.getLineOffset(1);
			event = createDocEvent(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluate(event));

			// Place another backslash after the previous one
			offset++;
			event = createDocEvent(offset, SINGLE_SLASH);
			// Now the evaluation function should trigger
			assertTrue(testEvaluator.evaluate(event));

			// Comment out the third line
			// Place a single backslash at the start of the third line
			offset = doc.getLineOffset(2);
			event = createDocEvent(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluate(event));

			// Place another backslash after the previous one
			offset++;
			event = createDocEvent(offset, SINGLE_SLASH);
			// Now the evaluation function should trigger
			assertTrue(testEvaluator.evaluate(event));
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
			event = createDocEvent(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluate(event));

			// Place a second backslash after the first
			offset++;
			event = createDocEvent(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluate(event));

			// Mock a document event with a single backslash placed at the beginning of the second line
			offset = doc.getLineOffset(1);
			event = createDocEvent(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluate(event));

			// Place another backslash after the previous one
			offset++;
			event = createDocEvent(offset, SINGLE_SLASH);
			// Now the evaluation function should trigger
			assertTrue(testEvaluator.evaluate(event));

			// Mock a document event with a single backslash placed at the beginning of the first line
			offset = doc.getLineOffset(0);
			event = createDocEvent(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluate(event));

			// Place another backslash after the previous one
			offset++;
			event = createDocEvent(offset, SINGLE_SLASH);
			// Now the evaluation function should trigger
			assertTrue(testEvaluator.evaluate(event));
		} catch (BadLocationException e) {
			// Should never get here
			fail("Should never see this error in: " + this.getClass().getSimpleName() + "::" + this.getClass().getName());
			e.printStackTrace();
		}
	}

	/**
	 * Tests for /// one one line, then // on the next line
	 */
	@Test
	public void threeSlashesConsecutiveLinesDown() {
		// Mock a document event with a single backslash placed at the beginning of the first line
		offset = 0;
		event = createDocEvent(offset, SINGLE_SLASH);
		assertFalse(testEvaluator.evaluate(event));
		// Place a second backslash after the first
		offset++;
		event = createDocEvent(offset, SINGLE_SLASH);
		assertFalse(testEvaluator.evaluate(event));
		// Place a third backslash after the second
		offset++;
		event = createDocEvent(offset, SINGLE_SLASH);
		assertFalse(testEvaluator.evaluate(event));

		// Pull the offset for the start of the second line so we aren't guessing
		try {
			// Place a single backslash at the start of the second line
			offset = doc.getLineOffset(1);
			event = createDocEvent(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluate(event));

			// Place another backslash after the previous one
			offset++;
			event = createDocEvent(offset, SINGLE_SLASH);
			// Now the evaluation function should trigger
			assertTrue(testEvaluator.evaluate(event));
		} catch (BadLocationException e) {
			// Should never get here
			fail("Should never see this error in: " + this.getClass().getSimpleName() + "::" + this.getClass().getName());
			e.printStackTrace();
		}
	}
	
	/**
	 * Tests for /// one one line, then // on the previous line
	 */
	@Test
	public void threeSlashesConsecutiveLinesUp() {
		try {
			// Mock a document event with a single backslash placed at the beginning of the third line
			offset = doc.getLineOffset(2);
			event = createDocEvent(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluate(event));

			// Place a second backslash after the first
			offset++;
			event = createDocEvent(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluate(event));

			// Place a third backslash after the second
			offset++;
			event = createDocEvent(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluate(event));
			
			// Mock a document event with a single backslash placed at the beginning of the second line
			offset = doc.getLineOffset(1);
			event = createDocEvent(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluate(event));

			// Place another backslash after the previous one
			offset++;
			event = createDocEvent(offset, SINGLE_SLASH);
			// Now the evaluation function should trigger
			assertTrue(testEvaluator.evaluate(event));
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
}

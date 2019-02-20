package test.java;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.junit.Test;

import main.evaluators.BlockCommentEvaluator;

/**
 * Unit test for BlockCommentEvaluator
 */
public class BlockCommentEvaluatorTest {
	
	private static final String content = "Line1\n Line2\n Line3\n";

	/**
	 * Tests the block comment evaluation function returns true when line 0 then line 1 of a document are commented out
	 */
	@Test
	public void twoConsecutiveLinesDownCommentedOut() {
		int offset;
		int length;
		String textAdded;
		DocumentEvent event;
		
		// Create a document with four lines
		// Line count starts at 0
		IDocument doc = new Document(content);
		
		BlockCommentEvaluator testEvaluator = new BlockCommentEvaluator();

		// Send mock data
		// Mock a document event with a single backslash placed at the beginning of the first line
		offset = 0;
		length = 1;
		textAdded = "/";
		event = new DocumentEvent(doc, offset, length, textAdded);
		assertFalse(testEvaluator.evaluate(event));
		// Place a second backslash after the first
		offset++;
		event = new DocumentEvent(doc, offset, length, textAdded);
		assertFalse(testEvaluator.evaluate(event));
		
		// Pull the offset for the start of the second line so we aren't guessing
		try {
			// Place a single backslash at the start of the second line
			offset = doc.getLineOffset(1);
			event = new DocumentEvent(doc, offset, length, textAdded);
			assertFalse(testEvaluator.evaluate(event));
			
			// Place another backslash after the previous one
			offset++;
			event = new DocumentEvent(doc, offset, length, textAdded);
			// Now the evaluation function should trigger
			assertTrue(testEvaluator.evaluate(event));
		} catch (BadLocationException e) {
			// Should never get here
			// fail included as sanity check
			fail("BadLocationException thrown in consecutiveLinesDownCommentedOut Test");
			e.printStackTrace();
		}
	}

	/**
	 * Tests the block comment evaluation function returns true when line 2 then line 1 of a document are commented out
	 */
	@Test
	public void twoConsecutiveLinesUpCommentedOut() {
		int offset;
		int length;
		String textAdded;
		DocumentEvent event;
		
		// Create a document with four lines
		// Line count starts at 0
		IDocument doc = new Document(content);
		
		BlockCommentEvaluator testEvaluator = new BlockCommentEvaluator();
		length = 1;
		textAdded = "/";
		// Create mock document event changes
		try {
			// Mock a document event with a single backslash placed at the beginning of the third line
			offset = doc.getLineOffset(2);
			event = new DocumentEvent(doc, offset, length, textAdded);
			assertFalse(testEvaluator.evaluate(event));
		
			// Place a second backslash after the first
			offset++;
			event = new DocumentEvent(doc, offset, length, textAdded);
			assertFalse(testEvaluator.evaluate(event));
		
			// Mock a document event with a single backslash placed at the beginning of the second line
			offset = doc.getLineOffset(1);
			event = new DocumentEvent(doc, offset, length, textAdded);
			assertFalse(testEvaluator.evaluate(event));
			
			// Place another backslash after the previous one
			offset++;
			event = new DocumentEvent(doc, offset, length, textAdded);
			// Now the evaluation function should trigger
			assertTrue(testEvaluator.evaluate(event));
		} catch (BadLocationException e) {
			// Should never get here
			// fail included as sanity check
			fail("BadLocationException thrown in consecutiveLinesUpCommentedOut Test");
			e.printStackTrace();
		}
	}

	/**
	 * Verify that evaluation function triggers if two consecutive lines are commented out and triggers again
	 * if another consecutive line is triggered
	 */
	@Test
	public void threeConsecutiveLinesDownCommented() {
		int offset;
		int length;
		String textAdded;
		DocumentEvent event;

		// Create a document with four lines
		// Line count starts at 0
		IDocument doc = new Document(content);
		BlockCommentEvaluator testEvaluator = new BlockCommentEvaluator();

		// Send mock data
		// Mock a document event with a single backslash placed at the beginning of the first line
		offset = 0;
		length = 1;
		textAdded = "/";
		event = new DocumentEvent(doc, offset, length, textAdded);
		assertFalse(testEvaluator.evaluate(event));
		// Place a second backslash after the first
		offset++;
		event = new DocumentEvent(doc, offset, length, textAdded);
		assertFalse(testEvaluator.evaluate(event));

		// Pull the offset for the start of the second line so we aren't guessing
		try {
			// Place a single backslash at the start of the second line
			offset = doc.getLineOffset(1);
			event = new DocumentEvent(doc, offset, length, textAdded);
			assertFalse(testEvaluator.evaluate(event));

			// Place another backslash after the previous one
			offset++;
			event = new DocumentEvent(doc, offset, length, textAdded);
			// Now the evaluation function should trigger
			assertTrue(testEvaluator.evaluate(event));
		} catch (BadLocationException e) {
			// Should never get here
			// fail included as sanity check
			fail("BadLocationException thrown in consecutiveLinesDownCommentedOut Test");
			e.printStackTrace();
		}

		// Comment out the third line
		try {
			// Place a single backslash at the start of the third line
			offset = doc.getLineOffset(2);
			event = new DocumentEvent(doc, offset, length, textAdded);
			assertFalse(testEvaluator.evaluate(event));

			// Place another backslash after the previous one
			offset++;
			event = new DocumentEvent(doc, offset, length, textAdded);
			// Now the evaluation function should trigger
			assertTrue(testEvaluator.evaluate(event));
		} catch (BadLocationException e) {
			// Should never get here
			// fail included as sanity check
			fail("BadLocationException thrown in consecutiveLinesDownCommentedOut Test");
			e.printStackTrace();
		}
	}

	/**
	 * Tests the block comment evaluation function returns true when line 3 then line 2 then line 1 are commented out
	 */
	@Test
	public void threeConsecutiveLinesUpCommentedOut() {
		int offset;
		int length;
		String textAdded;
		DocumentEvent event;

		// Create a document with four lines
		// Line count starts at 0
		IDocument doc = new Document(content);
		BlockCommentEvaluator testEvaluator = new BlockCommentEvaluator();
		length = 1;
		textAdded = "/";
		// Create mock document event changes
		try {
			// Mock a document event with a single backslash placed at the beginning of the third line
			offset = doc.getLineOffset(2);
			event = new DocumentEvent(doc, offset, length, textAdded);
			assertFalse(testEvaluator.evaluate(event));

			// Place a second backslash after the first
			offset++;
			event = new DocumentEvent(doc, offset, length, textAdded);
			assertFalse(testEvaluator.evaluate(event));

			// Mock a document event with a single backslash placed at the beginning of the second line
			offset = doc.getLineOffset(1);
			event = new DocumentEvent(doc, offset, length, textAdded);
			assertFalse(testEvaluator.evaluate(event));

			// Place another backslash after the previous one
			offset++;
			event = new DocumentEvent(doc, offset, length, textAdded);
			// Now the evaluation function should trigger
			assertTrue(testEvaluator.evaluate(event));

			// Mock a document event with a single backslash placed at the beginning of the first line
			offset = doc.getLineOffset(0);
			event = new DocumentEvent(doc, offset, length, textAdded);
			assertFalse(testEvaluator.evaluate(event));

			// Place another backslash after the previous one
			offset++;
			event = new DocumentEvent(doc, offset, length, textAdded);
			// Now the evaluation function should trigger
			assertTrue(testEvaluator.evaluate(event));
		} catch (BadLocationException e) {
			// Should never get here
			// fail included as sanity check
			fail("BadLocationException thrown in consecutiveLinesUpCommentedOut Test");
			e.printStackTrace();
		}
	}

	/**
	 * Verifies that the evaluation function will not trigger when the user types "//" anywhere but the start
	 * of a line
	 */
	@Test
	public void commentNotAtStartOfLineDown() {
		int offset;
		int length;
		String textAdded;
		DocumentEvent event;

		// Create a document with four lines
		// Line count starts at 0
		IDocument doc = new Document(content);
		BlockCommentEvaluator testEvaluator = new BlockCommentEvaluator();

		// Send mock data
		// Mock a document event with a single backslash placed at the beginning of the first line
		offset = 0;
		length = 1;
		textAdded = "/";
		event = new DocumentEvent(doc, offset, length, textAdded);
		assertFalse(testEvaluator.evaluate(event));
		// Place a second backslash after the first
		offset++;
		event = new DocumentEvent(doc, offset, length, textAdded);
		assertFalse(testEvaluator.evaluate(event));

		try {
			// Pull the offset for the start of the second line so we aren't guessing
			offset = doc.getLineOffset(1);
			// Add two to the offset so we're in the middle of the line
			offset += 2;
			event = new DocumentEvent(doc, offset, length, textAdded);
			assertFalse(testEvaluator.evaluate(event));

			// Place another backslash after the previous one
			offset++;
			event = new DocumentEvent(doc, offset, length, textAdded);

			// Now the evaluation function should not trigger
			assertFalse(testEvaluator.evaluate(event));
		} catch (BadLocationException e) {
			// Should never get here
			// fail included as sanity check
			fail("BadLocationException thrown in consecutiveLinesDownCommentedOut Test");
			e.printStackTrace();
		}
	}

	/**
	 * Verifies that the evaluation function will not trigger when the user types "//" anywhere but the start
	 * of a line
	 */
	@Test
	public void commentNotAtStartOfLineUp() {
		int offset;
		int length;
		String textAdded;
		DocumentEvent event;

		// Create a document with four lines
		// Line count starts at 0
		IDocument doc = new Document(content);
		BlockCommentEvaluator testEvaluator = new BlockCommentEvaluator();
		length = 1;
		textAdded = "/";
		// Create mock document event changes
		try {
			// Mock a document event with a single backslash placed at the beginning of the third line
			offset = doc.getLineOffset(2);
			event = new DocumentEvent(doc, offset, length, textAdded);
			assertFalse(testEvaluator.evaluate(event));

			// Place a second backslash after the first
			offset++;
			event = new DocumentEvent(doc, offset, length, textAdded);
			assertFalse(testEvaluator.evaluate(event));

			// Mock a document event with a single backslash placed at the beginning of the second line
			offset = doc.getLineOffset(1);
			// Add two to get to mid line
			offset += 2;
			event = new DocumentEvent(doc, offset, length, textAdded);
			assertFalse(testEvaluator.evaluate(event));

			// Place another backslash after the previous one
			offset++;
			event = new DocumentEvent(doc, offset, length, textAdded);
			// Now the evaluation function should trigger
			assertFalse(testEvaluator.evaluate(event));
		} catch (BadLocationException e) {
			// Should never get here
			// fail included as sanity check
			fail("BadLocationException thrown in consecutiveLinesUpCommentedOut Test");
			e.printStackTrace();
		}
	}
}

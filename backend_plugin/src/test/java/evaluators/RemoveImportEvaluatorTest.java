package test.java.evaluators;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.AnnotationModel;
import org.junit.Before;
import org.junit.Test;

import main.evaluators.RemoveImportEvaluator;

public class RemoveImportEvaluatorTest {

	/** Mock unused import annotation */
	private static final Annotation ANNOTATION = new Annotation("org.eclipse.jdt.ui.warning",
			false, "The import java.util.Map is never used");
	/** Position used with the mock Annotation */
	private static final Position POSITION = new Position(0);

	/** Evaluator being tested */
	private RemoveImportEvaluator eval;
	/** AnnotationModel to be attached to the evaluator */
	private AnnotationModel am;

	/**
	 * Initialize a new document and a new evaluator
	 */
	@Before
	public void runBeforeTests() {
		this.am = new AnnotationModel();
		this.eval = new RemoveImportEvaluator();
	}

	/**
	 * Tests that a document being saved in the workspace while this document has an unused
	 * import causes the feature evaluation to trigger
	 */
	@Test
	public void saveEventWithUnusedImports() {
		am.addAnnotation(ANNOTATION, POSITION);
		eval.evaluateAnnotationModelChanges(am);
		assertTrue(eval.hasActiveUnusedImportStatement());
	}

	/**
	 * Tests that a document being saved in the worspace while this document does not have any
	 * unused imports does not trigger the feature evaluation
	 */
	@Test
	public void saveEventWithoutUnusedImports() {
		assertFalse(eval.hasActiveUnusedImportStatement());
	}
}

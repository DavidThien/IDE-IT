package main.evaluators;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.VariableDeclaration;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;

import main.ASTVisitors.VariableDeclarationFinder;
import main.interfaces.FeatureID;

/**
 * Evaluates DocumentEvent changes to determine if the user is manually creating getter and setter methods
 * for previously declared variables. Parses the AST of a document whenever the user is manually typing
 * new method declarations
 */
public class GetterSetterEvaluator extends FeatureEvaluator {

    private Set<String> varNames;

    /**
     * Constructor
     * @param document IDocument that this evaluator is attached to
     */
    public GetterSetterEvaluator(IDocument document) {
	this.featureID = FeatureID.GETTER_SETTER_FEATURE_ID;
	this.document = document;
	this.varNames = new HashSet<String>();
    }


    /**
     * Checks if the event added a get or set method regarding a previously declared variable
     */
    @Override
    public boolean evaluateDocumentChanges(DocumentEvent event) {
	try {
	    return checkMethodDeclaration(event, this.document.getLineOfOffset(event.getOffset()));
	} catch (BadLocationException e) {}
	return false;
    }

    /**
     * Checks that the document event added get[varName], get_[varName], set[varName], or set_[varName]
     * @param event the DocumentEvent that occurred
     * @param line the line number where the change occurred
     * @return true if a get or set method was added for a variable, false otherwise
     */
    private boolean checkMethodDeclaration(DocumentEvent event, int line) {
	try {
	    int lineOffset = document.getLineOffset(line);
	    int lineLength = document.getLineLength(line);
	    String lineText = document.get(lineOffset, lineLength).trim().toLowerCase();

	    // check for public or protected at the start of the line
	    if (lineText.startsWith("public ") || lineText.startsWith("protected ")) {
		// If so, then update the variable names
		// This operation is costly, so we limit it to only when it's necessary
		updateKnownVariableNames();
		return checkGetterOrSetter(lineText);
	    }
	} catch (BadLocationException e) {}
	return false;
    }

    /**
     * Creates an AST from the current document values and stores all current variable names in varNames
     */
    private void updateKnownVariableNames() {

	// clear out all previous variable names
	this.varNames = new HashSet<String>();

	// Create the AST through the ASTParser and find all variable declarations
	ASTParser parser = ASTParser.newParser(AST.JLS11);
//	parser.setBindingsRecovery(true);
	parser.setSource(document.get().toCharArray());
//	parser.setResolveBindings(true);
	CompilationUnit cu = (CompilationUnit)parser.createAST(null);
	cu.recordModifications();

	VariableDeclarationFinder varFinder = new VariableDeclarationFinder();

	cu.accept(varFinder);
	for(VariableDeclaration var : varFinder.getVariables()) {
	    // Skip empty strings
	    if (var.getName().toString().length() > 0) {
		varNames.add(var.getName().toString().toLowerCase());
	    }
	}

    }

    /**
     * Checks if the method declaration contains get, get_, set, set_ and a variable name
     * @param lineText the text of the current line edited in the document
     * @return true if the line appears to be a get or set method for a declared variable
     */
    private boolean checkGetterOrSetter(String lineText) {
	// first check that it starts with get
	String prefix = "";
	boolean found = false;
	if (lineText.contains("get")) {
	    prefix = "get";
	    found = true;
	}

	if (lineText.contains("set")) {
	    prefix = "set";
	    found = true;
	}

	if (found) {
	    // check if it contains any of the known variable names
	    for (String v : varNames) {
		if (lineText.endsWith(prefix + v) || lineText.endsWith(prefix + "_" + v)) {
		    return true;
		}
	    }
	}
	return false;
    }
}

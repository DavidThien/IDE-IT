package main.evaluators;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclaration;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.texteditor.ITextEditor;

import main.ASTVisitors.VariableDeclarationFinder;

/**
 * Evaluates DocumentEvent changes to determine if the user is changing the indentation of multiple sequential lines of code. If so,
 * then the user should be notified of the auto indentation feature in Eclipse.
 */
public class GetterSetterEvaluator extends FeatureEvaluator {
    private Set<String> varNames;
    /**
     * Constructor
     * @param document IDocument that this evaluator is attached to
     */
    public GetterSetterEvaluator(IDocument document) {
	this.featureID = "getterSetterSuggestion";
	this.document = document;
	// arbitrary default values to avoid special casing for the first document change
	this.varNames = new HashSet<String>();
	// Need to add all names as lower case
    }

    /**
     * If the text about to be added to the document is either whitespace or nothing, then save the line
     * of code in a string to reference later
     */
    @Override
    public boolean evaluateDocumentBeforeChange(DocumentEvent event) {

//	IWorkspace workspace = ResourcesPlugin.getWorkspace();
//	IWorkspaceDescription desc = workspace.getDescription();
//
//
//	ASTParser parser = ASTParser.newParser(AST.JLS11);
//	parser.setBindingsRecovery(true);
//	parser.setSource(document.get().toCharArray());
//	parser.setResolveBindings(true);
//	CompilationUnit cu = (CompilationUnit)parser.createAST(null);
//	cu.recordModifications();
//
//	VariableDeclarationFinder varFinder = new VariableDeclarationFinder();
//
//	cu.accept(varFinder);
//	for(VariableDeclaration var : varFinder.getVariables()) {
//	    System.out.println("Var name: " + var.getName());
//	    System.out.println("Node Type: " + var.getNodeType());
//	    System.out.println("Properties:");
//	    Map m = var.properties();
//	    for(Object p : m.keySet()) {
//		System.out.println((String)p);
//	    }
//
//	    IVariableBinding b =  var.resolveBinding();
//	    if (b != null) {
//		System.out.println("BinaryName: " + b.getName());
//	    }
//
//
//	}


//	List<AbstractTypeDeclaration> types = cu.types();
//	for(AbstractTypeDeclaration type : types) {
//	    if(type.getNodeType() == ASTNode.TYPE_DECLARATION) {
//	        // Class def found
//	        List<BodyDeclaration> bodies = type.bodyDeclarations();
//	        for(BodyDeclaration body : bodies) {
//	            if(body.getNodeType() == ASTNode.METHOD_DECLARATION) {
//	                MethodDeclaration method = (MethodDeclaration)body;
//	                System.out.println("method declaration: ");
//	                System.out.println("name: " + method.getName().getFullyQualifiedName());
//	                System.out.println("modifiers: " + method.getModifiers());
//	                System.out.println("return type: " + method.getReturnType2().toString());
//	            }
//	            if (body.getNodeType() == ASTNode.VARIABLE_DECLARATION_EXPRESSION) {}
//	        }
//	    }
//	}



//	IProject proj[] = workspace.getRoot().getProjects();
//
//	for(IProject p : proj) {
//	    System.out.println("Project name: " + p.getName());
//	    try {
//		IJavaProject jpro = JavaCore.create(p);
//		List<IFile> projectfiles = findAllProjectFiles(p);
//		for (IResource res : projectfiles) {
//		    System.out.println("Resource: " + res.getName());
//		    if (res.getName().equals(textEditor.getTitle())) {
//			System.out.println("Found a match!");
//		    }
//		}
//
//	    } catch (CoreException e) {
//
//	    }
//	}

//	if (proj != null) {
//	    System.out.println("Project name: " + proj.getName());
//	} else {
//	    System.out.println("Project is null");
//	}

//	System.out.println("Workspace: ");
//	System.out.println(workspace.toString());
//	System.out.println("Workspace desc: ");
//	System.out.println(desc.toString());

	// Check if the user types
	// public **** getVarName( || public ** get_varname(
	// We only care about adding white space or removing characters
	// Either of those cases will have a length of 0 once the string is trimmed

	return false;
    }

    /**
     * If the change added white space or removed some character then compare the line of code before the change
     * and after the change to determine if whitespace was added or removed at the start of the line
     */
    @Override
    public boolean evaluateDocumentChanges(DocumentEvent event) {
	try {
	    return checkForGetterOrSetter(event, this.document.getLineOfOffset(event.getOffset()));
	} catch (BadLocationException e) {}
	return false;
    }

    /**
     * Checks that the only change to the document line was the addition or removal of white space
     * @param event the DocumentEvent that occurred
     * @param line the line number where the change occurred
     * @return true if white space was added or removed to the front of the line, false otherwise
     */
    private boolean checkForGetterOrSetter(DocumentEvent event, int line) {
	try {
	    // if both before and after are identical after trim
	    // and the starting character is different, then we have a changed indentation
	    int lineOffset = document.getLineOffset(line);
	    int lineLength = document.getLineLength(line);
	    String lineText = document.get(lineOffset, lineLength).trim().toLowerCase();

	    // if the line starts with public, then it can be a getter or setter
	    // no point in checking for private getter and setters
	    if (lineText.startsWith("public")) {
		updateKnownVariableNames();
		return checkGetterOrSetter(lineText);
	    }

	} catch (BadLocationException e) {}
	return false;
    }

    private void updateKnownVariableNames() {

	// clear out all previous var names
	this.varNames = new HashSet<String>();

	ASTParser parser = ASTParser.newParser(AST.JLS11);
	parser.setBindingsRecovery(true);
	parser.setSource(document.get().toCharArray());
	parser.setResolveBindings(true);
	CompilationUnit cu = (CompilationUnit)parser.createAST(null);
	cu.recordModifications();

	VariableDeclarationFinder varFinder = new VariableDeclarationFinder();

	cu.accept(varFinder);
	for(VariableDeclaration var : varFinder.getVariables()) {
//	    System.out.println("Var name: " + var.getName());
//	    System.out.println("Var qualified name: " + var.getName().getFullyQualifiedName());
//	    System.out.println("Var identifier: " + var.getName().getIdentifier());
	    // Skip empty strings
	    if (var.getName().toString().length() > 0) {
		varNames.add(var.getName().toString().toLowerCase());
	    }
//	    System.out.println("Var Type: " + var.getName().getNodeType());
//	    System.out.println("Var start position: " + var.getName().getStartPosition());
//	    System.out.println("Node Type: " + var.getNodeType());
//	    System.out.println("Properties:");
//	    Map m = var.properties();
//	    for(Object p : m.keySet()) {
//		System.out.println((String)p);
//	    }

//	    IVariableBinding b =  var.resolveBinding();
//	    if (b != null) {
//		System.out.println("BinaryName: " + b.getName());
//	    }


	}

    }
    /**
     * Returns the position of the first non-white space character in the provided string
     * @param text the string to find the first non-white space character in
     * @return the position of the first non-white space character, or -2 if no such character was found
     */
    private int getOffsetOfFirstCharInLine(String text) {
	// iterate through until we hit something that's not " " or "\t"
	for (int i = 0; i < text.length(); i++) {
	    char c = text.charAt(i);
	    if (c != ' ' && c != '\t') {
		return i;
	    }
	}
	return -2;   // can't return -1 since it is adjacent to 0
    }

    /**
     * Checks if the previously indented line is adjacent to the current indent changed line
     * Also updates lastIndentChangedLine
     * @param line number of the currently indent changed line
     * @return true if the lines are adjacent, false otherwise
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

    private List<IFile> findAllProjectFiles(IContainer container) throws CoreException {
        IResource[] members = container.members();
        List<IFile> list = new ArrayList<>();

        for (IResource member : members) {
            if (member instanceof IContainer) {
                IContainer c = (IContainer) member;
                list.addAll(findAllProjectFiles(c));
            } else if (member instanceof IFile) {
                list.add((IFile) member);
            }
        }
        return list;
    }

}

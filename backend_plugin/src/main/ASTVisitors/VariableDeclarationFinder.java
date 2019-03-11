package main.ASTVisitors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.VariableDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

/**
 * ASTVisitor class that finds any VariableDeclaration nodes in the AST
 */
public final class VariableDeclarationFinder extends ASTVisitor {
  private final List<VariableDeclaration> vars = new ArrayList<>();

  /**
   * If any VariableDeclarationFragments are found in the AST, then save them
   */
  @Override
  public boolean visit(VariableDeclarationFragment fragment) {
    vars.add(fragment);
    return super.visit(fragment);
  }

  /**
   * @return an immutable list view of the variables discovered by this visitor
   */
  public List<VariableDeclaration> getVariables() {
    return Collections.unmodifiableList(vars);
  }
}
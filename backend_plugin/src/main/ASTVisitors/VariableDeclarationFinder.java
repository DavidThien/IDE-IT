package main.ASTVisitors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.VariableDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

public final class VariableDeclarationFinder extends ASTVisitor {
  private final List <VariableDeclaration> vars = new ArrayList <> ();

  public static List<VariableDeclaration> perform(ASTNode node) {
      VariableDeclarationFinder finder = new VariableDeclarationFinder();
      node.accept(finder);
      return finder.getVariables();
  }

  @Override
  public boolean visit (VariableDeclarationFragment fragment) {
    vars.add (fragment);
    return super.visit(fragment);
  }

  /**
   * @return an immutable list view of the methods discovered by this visitor
   */
  public List <VariableDeclaration> getVariables() {
    return Collections.unmodifiableList(vars);
  }
}
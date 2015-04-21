package com.siberika.idea.pascal;

import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase;
import com.siberika.idea.pascal.lang.psi.PasInvalidScopeException;
import com.siberika.idea.pascal.lang.psi.PasStatement;
import com.siberika.idea.pascal.lang.psi.impl.PasField;
import com.siberika.idea.pascal.lang.psi.impl.PascalExpression;
import com.siberika.idea.pascal.lang.psi.impl.PascalModuleImpl;
import com.siberika.idea.pascal.lang.references.PasReferenceUtil;

import java.util.List;

public class CalcTypeTest extends LightPlatformCodeInsightFixtureTestCase {
    @Override
    protected String getTestDataPath() {
        return "testData/annotator";
    }

    public void testFindSymbol() throws PasInvalidScopeException {
        myFixture.configureByFiles("structTypes.pas", "calcTypeTest.pas");
        PascalModuleImpl mod = (PascalModuleImpl) PasReferenceUtil.findUnit(myFixture.getProject(),
                PasReferenceUtil.findUnitFiles(myFixture.getProject(), myModule), "calcTypeTest");
        for (PasField field : mod.getAllFields()) {
            printIdent(field);
        }
    }

    public void testExprType() throws Exception {
        myFixture.configureByFiles("structTypes.pas", "calcTypeTest.pas");
        PascalModuleImpl mod = (PascalModuleImpl) PasReferenceUtil.findUnit(myFixture.getProject(),
                PasReferenceUtil.findUnitFiles(myFixture.getProject(), myModule), "calcTypeTest");
        PasStatement stmt = PsiTreeUtil.findChildOfType(mod, PasStatement.class);
        PascalExpression expr = PsiTreeUtil.findChildOfType(stmt, PascalExpression.class);
        PsiElement par = expr.getParent();
        par = par != null ? par.getFirstChild() : null;
        if (par instanceof PascalExpression) {
            List<PasField.ValueType> types = PascalExpression.getType((PascalExpression) par);
            for (PasField.ValueType type : types) {
                System.out.println(String.format("%s: %s", type.field != null ? type.field.name : "<anon>",
                        type.kind != null ? type.kind.name() : "-"));
            }
        }
    }

    private void printIdent(PasField field) throws PasInvalidScopeException {
        PasReferenceUtil.retrieveFieldTypeScope(field);
        System.out.println(String.format("%s: %s", field.name, field.getValueType()));
        System.out.println("Scope: " + field.getValueType().getTypeScope());
    }
}
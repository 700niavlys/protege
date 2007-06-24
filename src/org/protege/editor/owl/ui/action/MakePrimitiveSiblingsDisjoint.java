package org.protege.editor.owl.ui.action;

import java.awt.event.ActionEvent;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.protege.editor.owl.model.hierarchy.OWLObjectHierarchyProvider;
import org.semanticweb.owl.model.AddAxiom;
import org.semanticweb.owl.model.OWLAxiom;
import org.semanticweb.owl.model.OWLClass;


/**
 * Author: Matthew Horridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: 30-Jun-2006<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class MakePrimitiveSiblingsDisjoint extends SelectedOWLClassAction {

    private static final Logger logger = Logger.getLogger(MakePrimitiveSiblingsDisjoint.class);


    protected void initialiseAction() throws Exception {

    }


    public void actionPerformed(ActionEvent e) {
        OWLClass selCls = getOWLClass();
        if (selCls == null) {
            return;
        }
        // TODO: Extract this and make less dependent on hierarchy provider
        OWLObjectHierarchyProvider<OWLClass> provider = getOWLModelManager().getOWLClassHierarchyProvider();
        Set<OWLClass> clses = new HashSet<OWLClass>();
        for (OWLClass par : provider.getParents(selCls)) {
            clses.addAll(provider.getChildren(par));
        }
        OWLAxiom ax = getOWLDataFactory().getOWLDisjointClassesAxiom(clses);
        getOWLModelManager().applyChange(new AddAxiom(getOWLModelManager().getActiveOntology(), ax));

        // 2) Get the named subs

//        try {
//            OWLOntology owlOntology = getOWLModelManager().getActiveOntology();
//            DisjointAxiomCreator creator = new DisjointAxiomCreator(getOWLModelManager().getOWLClassHierarchyProvider(),
//                                                                    owlOntology,
//                                                                    getOWLModelManager().getActiveOntologies());
//
//            getOWLModelManager().applyChanges(creator.getChanges());
//        } catch (OWLException ex) {
//            logger.error(ex);
//        }
    }
}

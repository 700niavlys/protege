package org.protege.editor.owl.ui.frame;

import java.util.Arrays;
import java.util.List;

import org.protege.editor.owl.OWLEditorKit;
import org.semanticweb.owl.model.OWLDescription;
import org.semanticweb.owl.model.OWLObjectProperty;
import org.semanticweb.owl.model.OWLObjectPropertyDomainAxiom;
import org.semanticweb.owl.model.OWLOntology;


/**
 * Author: Matthew Horridge<br>
 * The University Of Manchester<br>
 * Bio-Health Informatics Group<br>
 * Date: 29-Jan-2007<br><br>
 */
public class OWLObjectPropertyDomainFrameSectionRow extends AbstractOWLFrameSectionRow<OWLObjectProperty, OWLObjectPropertyDomainAxiom, OWLDescription> {


    public OWLObjectPropertyDomainFrameSectionRow(OWLEditorKit owlEditorKit, OWLFrameSection section,
                                                  OWLOntology ontology, OWLObjectProperty rootObject,
                                                  OWLObjectPropertyDomainAxiom axiom) {
        super(owlEditorKit, section, ontology, rootObject, axiom);
    }


    protected OWLFrameSectionRowObjectEditor<OWLDescription> getObjectEditor() {
        return new OWLClassDescriptionEditor(getOWLEditorKit(), getAxiom().getDomain());
    }


    protected OWLObjectPropertyDomainAxiom createAxiom(OWLDescription editedObject) {
        return getOWLDataFactory().getOWLObjectPropertyDomainAxiom(getRootObject(), editedObject);
    }


    /**
     * Gets a list of objects contained in this row.  These objects
     * could be placed on the clip board during a copy operation,
     * or navigated to etc.
     */
    public List getManipulatableObjects() {
        return Arrays.asList(getAxiom().getDomain());
    }
}

package org.protege.editor.owl.ui.view;

import java.awt.BorderLayout;

import javax.swing.JScrollPane;

import org.protege.editor.owl.ui.frame.OWLDataPropertyDescriptionFrame;
import org.protege.editor.owl.ui.framelist.OWLFrameList2;
import org.semanticweb.owl.model.OWLDataProperty;


/**
 * Author: Matthew Horridge<br>
 * The University Of Manchester<br>
 * Bio-Health Informatics Group<br>
 * Date: 16-Feb-2007<br><br>
 */
public class OWLDataPropertyDescriptionViewComponent extends AbstractOWLDataPropertyViewComponent {

    private OWLFrameList2<OWLDataProperty> list;


    public void initialiseView() throws Exception {
        list = new OWLFrameList2<OWLDataProperty>(getOWLEditorKit(),
                                                  new OWLDataPropertyDescriptionFrame(getOWLEditorKit()));
        setLayout(new BorderLayout());
        add(new JScrollPane(list));
    }


    protected OWLDataProperty updateView(OWLDataProperty property) {
        list.setRootObject(property);
        return property;
    }


    public void disposeView() {
        super.disposeView();
        list.dispose();
    }
}

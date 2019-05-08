package org.protege.editor.owl.ui.lang;

import org.protege.editor.core.ui.util.AugmentedJTextField;
import org.protege.editor.core.ui.util.ComponentFactory;
import org.protege.editor.owl.model.lang.LangCode;
import org.protege.editor.owl.model.lang.LangCodeRegistry;

import javax.annotation.Nonnull;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-05-07
 */
public class LangTagEditor extends JComponent {

    private static final int POPUP_WIDTH = 300;

    private static final int POPUP_HEIGHT = 150;

    private final JTextField textField;

    private final JList<LangCode> langCodeList = new JList<>();

    private final LangCodeRegistry registry;

    private JWindow window;

    private boolean completing = false;

    public LangTagEditor(LangCodeRegistry registry) {
        this.registry = registry;
        langCodeList.setCellRenderer(new LangCodeRenderer());
        this.textField = new AugmentedJTextField("Language Tag");
        setLayout(new BorderLayout());
        add(textField, BorderLayout.NORTH);
        KeyListener keyListener = new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                processKeyPressed(e);
            }
        };
        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                hidePopupWindow();
            }
        });
        langCodeList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if(e.getClickCount() == 2) {
                    completeWithPopupSelection();
                }
            }
        });
        textField.addKeyListener(keyListener);
        textField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                handleDocumentChanged(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                handleDocumentChanged(e);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });

    }

    private void handleDocumentChanged(DocumentEvent e) {
        if(completing) {
            return;
        }
        String enteredText = textField.getText().toLowerCase();
        if(enteredText.isEmpty()) {
            hidePopupWindow();
            return;
        }
        LangCode[] matchedLangCodes = registry
                .getLangCodes()
                .stream()
                .filter(lc -> matches(enteredText, lc))
                .toArray(LangCode[]::new);
        langCodeList.setListData(matchedLangCodes);
        if(matchedLangCodes.length > 0) {
            showPopupWindow();
        }
    }

    private static boolean matches(String enteredText, LangCode lc) {
        Pattern pattern = Pattern.compile(String.format("^.*\\b%s.*$", Pattern.quote(enteredText)), Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(lc.getLangCode() + " " + lc.getDescription());
        return matcher.find();
    }

    private void hidePopupWindow() {
        getPopupWindow().ifPresent(w -> w.setVisible(false));
    }

    private void showPopupWindow() {
        Optional<JWindow> popupWindow = getPopupWindow();
        popupWindow.ifPresent(w -> {
            Point location = new Point(0, 0);
            SwingUtilities.convertPointToScreen(location, this);
            location.y = location.y + getHeight() + 2;
            w.setLocation(location);
            w.setVisible(true);
        });
    }

    private void processKeyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_TAB) {
            e.consume();
            completeWithPopupSelection();
        }
        else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            getPopupWindow().ifPresent(w -> {
                e.consume();
                completeWithPopupSelection();
            });
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            getPopupWindow().ifPresent(w -> {
                e.consume();
                incrementSelection();
            });
        }
        else if (e.getKeyCode() == KeyEvent.VK_UP) {
            getPopupWindow().ifPresent(w -> {
                e.consume();
                decrementSelection();
            });
        }
    }

    private void completeWithPopupSelection() {
        try {
            completing = true;
            hidePopupWindow();
            LangCode sel = langCodeList.getSelectedValue();
            if(sel == null) {
                return;
            }
            textField.setText(sel.getLangCode());
        } finally {
            completing = false;
        }
    }

    @Nonnull
    private Optional<JWindow> getPopupWindow() {
        if(window != null) {
            return Optional.of(window);
        }

        Window topLevelAncestor = (Window) this.textField.getTopLevelAncestor();
        if(topLevelAncestor == null) {
            return Optional.empty();
        }
        window = new JWindow(topLevelAncestor);
        window.setFocusableWindowState(false);
        window.getContentPane().setLayout(new BorderLayout());
        window.getContentPane().add(ComponentFactory.createScrollPane(langCodeList));
        window.setSize(new Dimension(POPUP_WIDTH, POPUP_HEIGHT));
        return Optional.of(window);
    }

    public Optional<LangCode> getLangCode() {
        return registry.getLangCode(textField.getText().trim().toLowerCase());
    }

    public void setLangCode(LangCode langCode) {
        textField.setText(langCode.getLangCode());
        window.setVisible(false);
    }

    private void incrementSelection() {
        int size = langCodeList.getModel().getSize();
        if(size == 0) {
            return;
        }
        int selIndex = langCodeList.getSelectedIndex() + 1;
        if(selIndex >= size) {
            selIndex = size - 1;
        }
        langCodeList.setSelectedIndex(selIndex);
    }

    private void decrementSelection() {
        int size = langCodeList.getModel().getSize();
        if(size == 0) {
            return;
        }
        int selIndex = langCodeList.getSelectedIndex() - 1;
        if(selIndex < 0) {
            selIndex = 0;
        }
        langCodeList.setSelectedIndex(selIndex);
    }

    public void clear() {
        textField.setText("");
        hidePopupWindow();
    }
}

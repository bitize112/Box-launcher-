package com.boxlauncher.ui;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;

public class LauncherConfiguratorFrame extends JFrame {
    private final JComboBox<WorkloadProfile> workloadBox = new JComboBox<>(WorkloadProfile.values());
    private final JComboBox<HookEngine> backendBox = new JComboBox<>();
    private final JTextArea resultArea = new JTextArea();

    public LauncherConfiguratorFrame() {
        super("Box Launcher - Bedrock Hook Backend Configurator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(760, 450));

        backendBox.addItem(null);
        for (HookEngine engine : HookEngine.values()) {
            backendBox.addItem(engine);
        }

        var title = new JLabel("Bedrock Mod Launcher (Java UI)");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 20f));

        var hint = new JLabel("Primary mod API: JavaScript. Optional native modules: C/C++/Rust/WASM.");

        var controls = new JPanel();
        controls.setLayout(new BoxLayout(controls, BoxLayout.Y_AXIS));
        controls.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        controls.add(new JLabel("Workload profile"));
        controls.add(workloadBox);
        controls.add(new JLabel("Backend override (optional)"));
        controls.add(backendBox);

        var chooseButton = new JButton("Select Backend");
        chooseButton.addActionListener(event -> onChooseBackend());
        controls.add(chooseButton);

        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        resultArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Result"),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));

        var top = new JPanel(new BorderLayout());
        top.setBorder(BorderFactory.createEmptyBorder(12, 12, 0, 12));
        top.add(title, BorderLayout.NORTH);
        top.add(hint, BorderLayout.SOUTH);

        add(top, BorderLayout.NORTH);
        add(controls, BorderLayout.WEST);
        add(resultArea, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
        onChooseBackend();
    }

    private void onChooseBackend() {
        HookEngine preferred = (HookEngine) backendBox.getSelectedItem();
        WorkloadProfile workload = (WorkloadProfile) workloadBox.getSelectedItem();

        HookEngine selected = EngineSelector.choose(preferred, workload == null ? WorkloadProfile.BALANCED : workload);

        String selectionMode = preferred == null ? "Auto selection" : "Developer override";
        String workloadLabel = workload == null ? WorkloadProfile.BALANCED.label() : workload.label();

        resultArea.setText(
                "Mode: " + selectionMode + "\n" +
                "Workload: " + workloadLabel + "\n" +
                "Selected backend: " + selected.label() + " (" + selected.id() + ")\n\n" +
                selected.summary() + "\n\n" +
                "This Java UI is a configurator. Mods can still be written primarily in JavaScript."
        );
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LauncherConfiguratorFrame().setVisible(true));
    }
}

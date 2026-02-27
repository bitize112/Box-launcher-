package com.boxlauncher.android;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private Spinner workloadSpinner;
    private Spinner backendSpinner;
    private TextView resultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        workloadSpinner = findViewById(R.id.workloadSpinner);
        backendSpinner = findViewById(R.id.backendSpinner);
        resultText = findViewById(R.id.resultText);
        Button selectButton = findViewById(R.id.selectButton);

        ArrayAdapter<WorkloadProfile> workloadAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                WorkloadProfile.values()
        );
        workloadAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        workloadSpinner.setAdapter(workloadAdapter);

        HookEngine[] backendChoices = {null, HookEngine.FRIDA_GUM, HookEngine.AND64INLINEHOOK, HookEngine.SHADOWHOOK};
        ArrayAdapter<HookEngine> backendAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                backendChoices
        ) {
            @Override
            public View getView(int position, View convertView, android.view.ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view;
                HookEngine item = getItem(position);
                tv.setText(item == null ? "Auto (recommended)" : item.label());
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, android.view.ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                HookEngine item = getItem(position);
                tv.setText(item == null ? "Auto (recommended)" : item.label());
                return view;
            }
        };
        backendAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        backendSpinner.setAdapter(backendAdapter);

        selectButton.setOnClickListener(v -> updateSelection());
        updateSelection();
    }

    private void updateSelection() {
        WorkloadProfile profile = (WorkloadProfile) workloadSpinner.getSelectedItem();
        HookEngine preferred = (HookEngine) backendSpinner.getSelectedItem();
        HookEngine selected = EngineSelector.choose(preferred, profile == null ? WorkloadProfile.BALANCED : profile);

        String mode = preferred == null ? "Auto" : "Manual override";
        resultText.setText(
                "Mode: " + mode + "\n" +
                "Workload: " + (profile == null ? WorkloadProfile.BALANCED : profile) + "\n" +
                "Selected backend: " + selected.label() + " (" + selected.id() + ")\n\n" +
                "JavaScript remains the primary mod language; C/C++/Rust can be loaded as native modules."
        );
    }
}

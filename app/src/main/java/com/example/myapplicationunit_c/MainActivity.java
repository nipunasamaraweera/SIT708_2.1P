package com.example.myapplicationunit_c;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    //Declare UI components
    private Spinner sourceUnitSpinner, destinationUnitSpinner;
    private EditText valueEditText;
    private Button convertButton;
    private TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initiate UI components
        sourceUnitSpinner = findViewById(R.id.sourceUnitSpinner);
        destinationUnitSpinner = findViewById(R.id.destinationUnitSpinner);
        valueEditText = findViewById(R.id.valueEditText);
        convertButton = findViewById(R.id.convertButton);
        resultTextView = findViewById(R.id.resultTextView);

        //set up the adapter
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.units_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sourceUnitSpinner.setAdapter(adapter);
        destinationUnitSpinner.setAdapter(adapter);

        //set up button listenr
        convertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performConversion();
            }
        });
    }

    //Unit conversion by the data that entered by the user
    private void performConversion() {
        String sourceUnit = sourceUnitSpinner.getSelectedItem().toString();
        String destinationUnit = destinationUnitSpinner.getSelectedItem().toString();
        String inputValue = valueEditText.getText().toString();
        //checking weather input value is empty
        if (inputValue.isEmpty()) {
            showToast("Please enter a value.");
            return;
        }

        double value;
        try {
            value = Double.parseDouble(inputValue);
        } catch (NumberFormatException e) {
            showToast("Please enter a valid number.");
            return;
        }
        //cheking for negative values
        if (value < 0 && !sourceUnit.equals("Celsius") && !sourceUnit.equals("Fahrenheit") && !sourceUnit.equals("Kelvin")) {
            showToast("Negative values are cannot be added this unit.");
            return;
        }
        //checking for source and the destination values
        if (sourceUnit.equals(destinationUnit)) {
            showToast("Source and destination units cannot be the same.");
            return;
        }

        double result = 0;
        //Unit conversion based on the source unit
        switch (sourceUnit) {
            case "Inches":
            case "Feet":
            case "Yards":
            case "Miles":
                result = convertLength(value, sourceUnit, destinationUnit);
                break;
            case "Pounds":
            case "Ounces":
            case "Tons":
                result = convertWeight(value, sourceUnit, destinationUnit);
                break;
            case "Celsius":
            case "Fahrenheit":
            case "Kelvin":
                result = convertTemperature(value, sourceUnit, destinationUnit);
                break;
            default:
                showToast("Invalid units selected.");
                return;
        }
        // Display the result
        resultTextView.setText(String.valueOf(result));
    }

    // Convert length units
    private double convertLength(double value, String sourceUnit, String destinationUnit) {
        double result = 0;
        switch (sourceUnit) {
            case "Inches":
                result = value * getLengthConversionFactor(destinationUnit, "Inches");
                break;
            case "Feet":
                result = value * getLengthConversionFactor(destinationUnit, "Feet");
                break;
            case "Yards":
                result = value * getLengthConversionFactor(destinationUnit, "Yards");
                break;
            case "Miles":
                result = value * getLengthConversionFactor(destinationUnit, "Miles");
                break;
        }
        return result;
    }

    // Convert weight units
    private double convertWeight(double value, String sourceUnit, String destinationUnit) {
        double result = 0;
        switch (sourceUnit) {
            case "Pounds":
                result = value * getWeightConversionFactor(destinationUnit, "Pounds");
                break;
            case "Ounces":
                result = value * getWeightConversionFactor(destinationUnit, "Ounces");
                break;
            case "Tons":
                result = value * getWeightConversionFactor(destinationUnit, "Tons");
                break;
        }
        return result;
    }

    // Convert temperature units
    private double convertTemperature(double value, String sourceUnit, String destinationUnit) {
        double result = 0;
        switch (sourceUnit) {
            case "Celsius":
                if (destinationUnit.equals("Fahrenheit"))
                    result = (value * 1.8) + 32;
                else if (destinationUnit.equals("Kelvin"))
                    result = value + 273.15;
                break;
            case "Fahrenheit":
                if (destinationUnit.equals("Celsius"))
                    result = (value - 32) / 1.8;
                else if (destinationUnit.equals("Kelvin"))
                    result = (value - 32) / 1.8 + 273.15;
                break;
            case "Kelvin":
                if (destinationUnit.equals("Celsius"))
                    result = value - 273.15;
                else if (destinationUnit.equals("Fahrenheit"))
                    result = (value - 273.15) * 1.8 + 32;
                break;
        }
        return result;
    }

    // Get conversion factor for length units
    private double getLengthConversionFactor(String toUnit, String fromUnit) {
        switch (toUnit) {
            case "Inches":
                switch (fromUnit) {
                    case "Inches":
                        return 1;
                    case "Feet":
                        return 12;  // 1 foot = 12 inches
                    case "Yards":
                        return 36;  // 1 yard = 36 inches
                    case "Miles":
                        return 63360;  // 1 mile = 63,360 inches
                }
                break;
            case "Feet":
                switch (fromUnit) {
                    case "Inches":
                        return 1.0 / 12;  // 1 inch = 1/12 feet
                    case "Feet":
                        return 1;
                    case "Yards":
                        return 3;  // 1 yard = 3 feet
                    case "Miles":
                        return 5280;  // 1 mile = 5280 feet
                }
                break;
            case "Yards":
                switch (fromUnit) {
                    case "Inches":
                        return 1.0 / 36;  // 1 inch = 1/36 yards
                    case "Feet":
                        return 1.0 / 3;  // 1 foot = 1/3 yards
                    case "Yards":
                        return 1;
                    case "Miles":
                        return 1760;  // 1 mile = 1760 yards
                }
                break;
            case "Miles":
                switch (fromUnit) {
                    case "Inches":
                        return 1.0 / 63360;  // 1 inch = 1/63,360 miles
                    case "Feet":
                        return 1.0 / 5280;  // 1 foot = 1/5280 miles
                    case "Yards":
                        return 1.0 / 1760;  // 1 yard = 1/1760 miles
                    case "Miles":
                        return 1;
                }
                break;
        }
        return 0; 
    }


    // Get conversion factor for weight units
    private double getWeightConversionFactor(String toUnit, String fromUnit) {
        switch (toUnit) {
            case "Pounds":
                switch (fromUnit) {
                    case "Pounds":
                        return 1;
                    case "Ounces":
                        return 1.0 / 16;  // 1 pound = 16 ounces
                    case "Tons":
                        return 1.0 / 2000;  // 1 ton = 2000 pounds
                }
                break;
            case "Ounces":
                switch (fromUnit) {
                    case "Pounds":
                        return 16;  // 1 pound = 16 ounces
                    case "Ounces":
                        return 1;
                    case "Tons":
                        return 16 * 2000;  // 1 ton = 16 * 2000 ounces
                }
                break;
            case "Tons":
                switch (fromUnit) {
                    case "Pounds":
                        return 2000;  // 1 ton = 2000 pounds
                    case "Ounces":
                        return 1.0 / (16 * 2000);  // 1 ton = 16 * 2000 ounces
                    case "Tons":
                        return 1;
                }
                break;
        }
        return 0;
    }

    // Display the toast message
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}

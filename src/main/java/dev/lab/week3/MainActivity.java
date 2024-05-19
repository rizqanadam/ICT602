package dev.lab.week3;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private EditText estUnitUsed, estRebate;
    private Button btnCalc, btnReset;
    private TextView result, errorMessage; // Declare errorMessage
    private Switch switchRebate;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        // Initialize errorMessage
        errorMessage = findViewById(R.id.errorMessage);

        estUnitUsed = findViewById(R.id.estUnitUsed);
        estRebate = findViewById(R.id.estRebate);
        btnCalc = findViewById(R.id.btnCalc);
        btnReset = findViewById(R.id.btnReset);
        result = findViewById(R.id.result);
        switchRebate = findViewById(R.id.switchRebate);

        btnCalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateBill(null);
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                estUnitUsed.setText("");
                estRebate.setText("");
                result.setText("");
                switchRebate.setChecked(false);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int selected = item.getItemId();

        if (selected == R.id.info) {
            Intent intent = new Intent(MainActivity.this, info.class);
            startActivity(intent);
            return true;
        } else if (selected == R.id.about) {
            Intent intent = new Intent(MainActivity.this,AboutMe.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void calculateBill(View view) {
        if (estUnitUsed.getText().toString().isEmpty()) {
            errorMessage.setText("Error: Please insert number of units used");
            errorMessage.setVisibility(View.VISIBLE); // Display error message
            return;
        }

        if (estRebate.getText().toString().isEmpty() && switchRebate.isChecked()) {
            errorMessage.setText("Error: Please insert rebate percentage");
            errorMessage.setVisibility(View.VISIBLE); // Display error message
            return;
        }

        errorMessage.setVisibility(View.GONE); // Hide error message if no errors

        double unitsUsed = Double.parseDouble(estUnitUsed.getText().toString());
        double rebate = switchRebate.isChecked() ? Double.parseDouble(estRebate.getText().toString()) : 0;

        double totalCost = calculateTotalCost(unitsUsed);
        double finalCost = totalCost - (totalCost * (rebate / 100));

        result.setText(String.format("Units Used: %.2f kWh\nTotal Charges: RM %.2f\nTotal Bill: RM %.2f", unitsUsed, totalCost, finalCost));
    }

    public void resetFields(View view) {
        estUnitUsed.setText("");
        estRebate.setText("");
        result.setText("");
        switchRebate.setChecked(false);
        errorMessage.setVisibility(View.GONE); // Hide error message when resetting fields
    }

    private double calculateTotalCost(double unitsUsed) {
        double totalCost = 0;

        if (unitsUsed <= 200) {
            totalCost = unitsUsed * 0.218;
        } else if (unitsUsed <= 300) {
            totalCost = (200 * 0.218) + ((unitsUsed - 200) * 0.334);
        } else if (unitsUsed <= 600) {
            totalCost = (200 * 0.218) + (100 * 0.334) + ((unitsUsed - 300) * 0.516);
        } else if (unitsUsed > 600) {
            totalCost = (200 * 0.218) + (100 * 0.334) + (300 * 0.516) + ((unitsUsed - 600) * 0.546);
        }

        return totalCost;
    }
}

package com.example.driverproject.driver_slip;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Slip extends AppCompatActivity implements View.OnClickListener{

    private EditText vehicleNumber;
    private TextView date_entry;
    private EditText startkms;
    private EditText endkms;
    EditText vehicle;
    DatabaseReference databaseVehicle;
    FirebaseAuth mauth;
    private Calendar calender;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slip);

        long date = System.currentTimeMillis();



        databaseVehicle= FirebaseDatabase.getInstance().getReference("Vehicle");

        vehicle=(EditText)findViewById(R.id.editTextVehicleType);

        vehicleNumber=(EditText)findViewById(R.id.editTextVehicleNumber);

        date_entry=(TextView) findViewById(R.id.editTextDate);

        startkms=(EditText)findViewById(R.id.editTextKMSS);

        endkms=(EditText)findViewById(R.id.editTextKMSP);


        SimpleDateFormat sdf = new SimpleDateFormat(" MM/dd/yyyy");
        String dateString = sdf.format(date);

        date_entry.setText("Date:"+dateString);


        findViewById(R.id.buttonSave).setOnClickListener(this);

        mauth=FirebaseAuth.getInstance();


    }

    public void saveData(boolean submitState)
    {


        String vehicle_Type=vehicle.getText().toString().trim();
        String vehicle_Number=vehicleNumber.getText().toString();
        String date_journey=date_entry.getText().toString();
        String start_kms=startkms.getText().toString();
        String end_kms=endkms.getText().toString();

        if(vehicle_Type.isEmpty())
        {
            vehicle.setError("Enter Vehicle Type");
            vehicle.requestFocus();
            return;
        }
        if(vehicle_Number.isEmpty())
        {
            vehicleNumber.setError("Enter Vehicle Number");
            vehicleNumber.requestFocus();
            return;
        }
        if(start_kms.isEmpty())
        {
            startkms.setError("Fill start kms");
            startkms.requestFocus();
            return;
        }
        if(end_kms.isEmpty())
        {
            endkms.setError("Fill end kms");
            endkms.requestFocus();
            return;
        }

        int s=Integer.parseInt(start_kms);
        int e=Integer.parseInt(end_kms);
        if(s>e)
        {

            startkms.setError("");
            endkms.setError("");


        }
        else
        {
            String user_id = mauth.getCurrentUser().getUid();
            String id = databaseVehicle.push().getKey();
            Vehicle v = new Vehicle(user_id, vehicle_Type, vehicle_Number, date_journey, start_kms, end_kms,submitState);
            databaseVehicle.child(id).setValue(v);
            Toast.makeText(this, "Added details", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View view)
    {
        switch(view.getId())
        {
            case R.id.buttonSave:
                saveData(false);
                break;
            case R.id.buttonSubmit:
                saveData(true);
        }

    }
}

package com.interswitchng.mobilesdk;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.interswitchng.iswmobilesdk.IswMobileSdk;
import com.interswitchng.iswmobilesdk.shared.models.core.IswPaymentInfo;
import com.interswitchng.iswmobilesdk.shared.models.core.IswPaymentResult;

import java.util.Date;

public class MainActivity extends AppCompatActivity implements IswMobileSdk.IswPaymentCallback {

    private static final String TAG = "MAIN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn = findViewById(R.id.btnPay);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get customer details
                String customerId = "12345",
                        customerName = "James Emmanuel",
                        customerEmail = "kenneth.ngedo@gmail.com",
                        customerMobile = "08031149929",
                        reference = "12345678" + new Date().getTime();

                EditText etAmount = findViewById(R.id.amount);


                long amount;
                String amtString = etAmount.getText().toString();

                // initialize amount
                if (amtString.isEmpty()) amount = 2500 * 100;
                else amount = Integer.parseInt(amtString) * 100;

                // create payment info
                IswPaymentInfo iswPaymentInfo = new IswPaymentInfo(customerId,
                        customerName, customerEmail, customerMobile, reference, amount);

                Log.d(TAG, "onClick: About to Pay");
                Log.d(TAG, "onClick: amount: "+ iswPaymentInfo.amountString);
                Log.d(TAG, "onClick: email: "+ iswPaymentInfo.customerEmail);
                Log.d(TAG, "onClick: mobile: "+ iswPaymentInfo.customerMobile);
                Log.d(TAG, "onClick: name: "+ iswPaymentInfo.customerName);
                // trigger payment
                IswMobileSdk.getInstance().pay(iswPaymentInfo, MainActivity.this);
            }
        });
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onUserCancel() {
        toast("You cancelled payment, please try again.");
    }

    @Override
    public void onPaymentCompleted(IswPaymentResult result) {
        if (result.isSuccessful)
            toast("your payment was successful, using: " + result.channel.name());
        else toast("unable to complete payment at the moment, try again later");
    }
}

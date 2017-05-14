// Copyright (c) 2017 Alexander Håkansson
//
// This software is released under the MIT License.
// https://opensource.org/licenses/MIT
package se.creotec.chscardbalance2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import se.creotec.chscardbalance2.service.BalanceService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(this, BuildConfig.BACKEND_URL, Toast.LENGTH_LONG).show();
        Intent updateBalanceIntent = new Intent(this, BalanceService.class);
        updateBalanceIntent.setAction(Constants.ACTION_UPDATE_BALANCE);
        System.out.println("Starting service");
        this.startService(updateBalanceIntent);
    }
}

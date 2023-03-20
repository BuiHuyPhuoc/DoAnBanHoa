package edu.huflit.project;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {
    DatabaseHelper db;
    Button mRegisterBack, mRegisterSubmit;
    CheckBox mRegisterAcceptTerm;
    EditText mregisterAddress, mregisterGmail, mregisterPhonenumber, mregisterName, mregisterPassword, mregisterUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        db = new DatabaseHelper(this, "FlowerShop.sqlite", null, 1);
        mRegisterBack = (Button) findViewById(R.id.RegisterBack);
        mRegisterSubmit = (Button) findViewById(R.id.RegisterSubmit);
        mRegisterAcceptTerm = (CheckBox) findViewById(R.id.RegisterAcceptTerm)  ;
        mregisterAddress = (EditText) findViewById(R.id.registerAddress);
        mregisterGmail = (EditText) findViewById(R.id.registerGmail);
        mregisterPhonenumber = (EditText) findViewById(R.id.registerPhonenumber);
        mregisterName = (EditText) findViewById(R.id.registerName);
        mregisterPassword = (EditText) findViewById(R.id.registerPassword);
        mregisterUser = (EditText) findViewById(R.id.registerUser);
        mRegisterBack.setOnClickListener(onClick_RegisterBack);
        mRegisterSubmit.setOnClickListener(onClick_mRegisterSubmit);
    }
    public View.OnClickListener onClick_mRegisterSubmit = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String taikhoan = mregisterUser.getText().toString();
            String matkhau = mregisterPassword.getText().toString();
            String hoten = mregisterName.getText().toString();
            String sdt = mregisterPhonenumber.getText().toString();
            String gmail = mregisterGmail.getText().toString();
            String diachi = mregisterAddress.getText().toString();
            if (taikhoan.length() == 0 || matkhau.length() == 0 || hoten.length() == 0){
                Toast.makeText(RegisterActivity.this, "Username, Password and Name is required", Toast.LENGTH_LONG).show();
                return;
            }
            if (mRegisterAcceptTerm.isChecked() == false){
                Toast.makeText(RegisterActivity.this, "You must accepted the Terms and Conditions once.", Toast.LENGTH_LONG).show();
                return;
            }
            Cursor listAccount = db.GetData("Select* from ACCOUNT");
            while (listAccount.moveToNext()){
                if (taikhoan.equals(listAccount.getString(0))){
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setMessage("Username already exists.");
                    builder.setPositiveButton("Forgot Password?", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(RegisterActivity.this, ForgotPassword.class);
                            startActivity(intent);
                        }
                    });
                    builder.setNegativeButton("New Username", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
                    // Create the AlertDialog object
                    builder.create().show();
                }
            }
            db.AddAccount(taikhoan, matkhau, "customer", hoten, sdt, gmail, diachi);
            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
            builder.setMessage("Create Account Success.");
            builder.setPositiveButton("Login", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("user", mregisterUser.getText().toString());
                    bundle.putString("pass", mregisterPassword.getText().toString());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            builder.setNegativeButton("Back", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
            // Create the AlertDialog object
            builder.create().show();
        }
    };
    public View.OnClickListener onClick_RegisterBack = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent i = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(i);
        }
    };

}
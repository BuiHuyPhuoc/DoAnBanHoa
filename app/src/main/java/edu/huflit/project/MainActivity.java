package edu.huflit.project;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper db; //Khởi tạo database
    EditText medtUser, medtPassword;
    Button mbtnLogin;
    TextView mtvRegister, mtvForgotpass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        medtUser = (EditText) findViewById(R.id.edtUser);
        medtPassword = (EditText) findViewById(R.id.edtPassword);
        mtvForgotpass = (TextView) findViewById(R.id.tvForgotpass);
        mtvRegister = (TextView) findViewById(R.id.tvRegister);
        mbtnLogin = (Button) findViewById(R.id.btnLogin);
        //Tạo database
        db = new DatabaseHelper(this, "FlowerShop.sqlite", null, 1);
        //Tạo bảng ROLE: Quyền hạn
        String x =  "CREATE TABLE IF NOT EXISTS [ROLE] (" +
                    "QUYENHAN VARCHAR PRIMARY KEY NOT NULL," +
                    "NOIDUNG Text NOT NULL);";
        db.WriteQuery(x);
        //Thêm dữ liệu vào bảng [ROLE]
        db.AddRole("admin", "quan tri vien");
        db.AddRole("customer", "khach hang");
        db.AddRole("staff", "nhan vien");
        //Tạo bảng ACCOUNT: chứa các tài khoản
        String y = "CREATE TABLE IF NOT EXISTS ACCOUNT (\n" +
                "\tTAIKHOAN VARCHAR PRIMARY KEY NOT NULL,\n" +
                "\tMATKHAU VARCHAR NOT NULL,\n" +
                "\tQUYENHAN VARCHAR NOT NULL, \n" +
                "\tTEN VARCHAR NOT NULL,\n" +
                "\tSDT VARCHAR,\n" +
                "\tGMAIL VARCHAR,\n" +
                "\tDIACHI VARCHAR,\n" +
                "\tFOREIGN KEY (QUYENHAN) REFERENCES [ROLE](QUYENHAN)\n" +
                ");";
        db.WriteQuery(y);
        //Thêm tài khoản admin và khách hàng mẫu để test
        db.AddAccount("123", "123", "admin", "Nguyen Van A", "", "", "");
        db.AddAccount("1234", "1234", "customer", "Nguyen Thi B", "0334379439", "", "119");
        //

        //Chuyển từ Activity Register qua MainActivity
        //Nếu đăng kí thành công, tự động nhập tài khoản mật khẩu khi quay về trang login
        Bundle  bundle = getIntent().getExtras();
        if (bundle != null){
            medtUser.setText(bundle.getString("user"));
            medtPassword.setText(bundle.getString("pass"));
        }
        //Xử lí onClick
        mtvRegister.setOnClickListener(onClick_tvRegister); //Hàm onClick đổi sang Activity Đăng kí
        mbtnLogin.setOnClickListener(onClick_btnLogin); //Hàm onClick xử lí đăng nhập
    }
    public View.OnClickListener onClick_tvRegister = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent i = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(i);
        }
    };
    public View.OnClickListener onClick_btnLogin = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String taikhoan = medtUser.getText().toString();
            String matkhau = medtPassword.getText().toString();
            if (taikhoan.length() == 0){
                Toast.makeText(MainActivity.this, "Nhập tài khoản", Toast.LENGTH_LONG).show();
                medtUser.requestFocus();
                return;
            }
            if (matkhau.length() == 0){
                Toast.makeText(MainActivity.this, "Nhập mật khẩu", Toast.LENGTH_LONG).show();
                medtPassword.requestFocus();
                return;
            }
            Cursor listAccount = db.GetData(
                    "Select*" +
                            "From ACCOUNT"
            );
            boolean existAccount = false;
            while (listAccount.moveToNext()){
                if (taikhoan.equals(listAccount.getString(0)) && matkhau.equals(listAccount.getString(1))){
                    existAccount = true;
                    break;
                }
            }
            if(existAccount){
                Intent i = new Intent(MainActivity.this, LobbyActivity.class);
                startActivity(i);
            } else {
                Toast.makeText(MainActivity.this, "Sai mật khẩu hoặc tài khoản", Toast.LENGTH_SHORT).show();
            }
        }
    };
}
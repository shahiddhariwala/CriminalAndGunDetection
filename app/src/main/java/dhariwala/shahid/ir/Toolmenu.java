package dhariwala.shahid.ir;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;

public class Toolmenu extends AppCompatActivity {

    Button btn1,btn2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toolmenu);
        btn1 = (Button) findViewById(R.id.button1);
        btn2 = (Button) findViewById(R.id.button2);
        //Clearing Compared Images Stored
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteImage(Constants.comparedFolder);
            }
        });
        //Clearing Criminals Database
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteImage(Constants.databaseFolder);
            }
        });
    }
    //Function to Delete
    public void deleteImage(String dirName) {
        File file1= new File(Environment.getExternalStorageDirectory(),dirName);
        if (file1.isDirectory())
        {
            int i;
            File[] listFile = file1.listFiles();
            for (i = 0; i < listFile.length; i++)
            {
                listFile[i].delete();
            }
            Toast.makeText(getApplicationContext(),"Images Successfully Deleted !!",Toast.LENGTH_SHORT).show();

        }
    }


}

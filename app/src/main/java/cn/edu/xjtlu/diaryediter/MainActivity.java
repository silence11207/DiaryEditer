package cn.edu.xjtlu.diaryediter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
//示例主活动
public class MainActivity extends AppCompatActivity {
    public final String TAG="MainActivity";
    TextView textView;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DiaryEditerActivity.class);
                startActivityForResult(intent, 0);
            }
        });
        textView =findViewById(R.id.diary_content);
    }
    //从DiaryEditerActivity返回的数据目前只让其显示在R.layout.activity_main中
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String content = data.getStringExtra("content");
        String time = data.getStringExtra("time");
        textView.setText(content + "\n" + time);
        //这里是数据库内容对接时商量
        //Diary diary = new Diary(content, time,1);
    }
}
package cn.edu.xjtlu.diaryediter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.KeyboardShortcutGroup;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DiaryEditerActivity extends AppCompatActivity {
    //定位相关代码
    public LocationClient mLocationClient;
    //定位城市名
    public TextView positionText;
    //日记编辑文本框
    EditText editText;
    //日期时间
    TextView timeInEditer;
    //字数
    TextView wordCount;
    //字体对象
    Typeface dancingScript;
    Typeface amaticSC;
    Typeface comforterBrush;
    Typeface indieFlower;
    Typeface shadowsIntoLight;
    Typeface caveatVariableFont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dairy_editer);
        //定位代码使用的是百度定位（libs下的第三方jar包以及main/jniLibs下的源码）
        //定位相关代码（跳出需要定位权限Dialog）
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new DiaryEditerActivity.MyLocationListener());
        positionText = (TextView) findViewById(R.id.position_text_view);
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(DiaryEditerActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (!permissionList.isEmpty()) {
            String [] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(DiaryEditerActivity.this, permissions, 1);
        } else {
            requestLocation();
        }

        //Toolbar初始化
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //字体初始化文件在main/assests下
        dancingScript = Typeface.createFromAsset(getAssets(),"DancingScript-VariableFont_wght.ttf");
        amaticSC = Typeface.createFromAsset(getAssets(),"AmaticSC-Regular.ttf");
        comforterBrush = Typeface.createFromAsset(getAssets(),"ComforterBrush-Regular.ttf");
        indieFlower= Typeface.createFromAsset(getAssets(),"IndieFlower-Regular.ttf");
        shadowsIntoLight = Typeface.createFromAsset(getAssets(),"ShadowsIntoLight-Regular.ttf");
        caveatVariableFont = Typeface.createFromAsset(getAssets(),"Caveat-VariableFont_wght.ttf");

        editText = findViewById(R.id.diary);
        wordCount = findViewById(R.id.word_count);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //同步计算word count
                wordCount.setText("word count: " + s.length());
            }
        });
        timeInEditer = findViewById(R.id.time_in_editer);
        timeInEditer.setText(dataToStr());

    }
    //以下3个方法是定位代码，分别为2个方法和一个onDestory重写方法
    private void requestLocation() {
        initLocation();
        mLocationClient.start();
    }

    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
    }
    //获取定位权限结果
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                    }
                    requestLocation();
                } else {
                    Toast.makeText(this, "Unknown error occurred", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }

    //设置返回地理位置细节
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
//            StringBuilder currentPosition = new StringBuilder();
//            currentPosition.append("纬度：").append(location.getLatitude()).append("\n");
//            currentPosition.append("经线：").append(location.getLongitude()).append("\n");
//            currentPosition.append("国家：").append(location.getCountry()).append("\n");
//            currentPosition.append("省：").append(location.getProvince()).append("\n");
//            currentPosition.append("市：").append(location.getCity()).append("\n");
//            currentPosition.append("区：").append(location.getDistrict()).append("\n");
//            currentPosition.append("街道：").append(location.getStreet()).append("\n");
//            currentPosition.append("定位方式：");
//            if (location.getLocType() == BDLocation.TypeGpsLocation) {
//                currentPosition.append("GPS");
//            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
//                currentPosition.append("网络");
//            }
            String city = location.getCity();
            positionText.setText(city);

        }

    }
    //点击系统返回键时将数据返回到MainActivity等同于保存
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_HOME){
            return true;
        }else if(keyCode == KeyEvent.KEYCODE_BACK){
            Intent intent = new Intent();
            intent.putExtra("content", editText.getText().toString());
            intent.putExtra("time",dataToStr());
            setResult(RESULT_OK, intent);
            finish();
            return true;
        }
       return super.onKeyDown(keyCode, event);
    }
    //将菜单键显示在toolbar中
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }
    //toolbar中各个键位的功能
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()){
            //左上角的返回键将内容和时间返回等同于保存
            case android.R.id.home:
                intent = new Intent();
                intent.putExtra("content", editText.getText().toString());
                intent.putExtra("time",dataToStr());
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.save:
                //保存并将键盘关闭
                Toast.makeText(this, "You saved the diary", Toast.LENGTH_SHORT).show();
                closeKeyboard();
                intent = new Intent();
                intent.putExtra("content", editText.getText().toString());
                intent.putExtra("time",dataToStr());
                setResult(RESULT_OK, intent);
                break;
            case R.id.delete:
                Toast.makeText(this, "You deleted the diary", Toast.LENGTH_SHORT).show();
                intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.change_font:
                showFontChoices();
                break;
            default:
        }
        return true;
    }

    //返回日期字符串
    public String dataToStr(){
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(date);
    }

    //显示字体选项
    public void showFontChoices(){
        AlertDialog.Builder builder = new AlertDialog.Builder(DiaryEditerActivity.this);
        builder.setTitle("Choose a font");
        final String[] fonts ={"Default","Serif", "Sans", "Monospace" ,"DancingScript",
                "AmaticSC", "ComforterBrush", "IndieFlower", "ShadowsIntoLight", "CaveatVariableFont"};

        builder.setItems(fonts, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(DiaryEditerActivity.this, "You chose the " + fonts[which], Toast.LENGTH_SHORT).show();

                switch (which){
                    case 0:
                        editText.setTypeface(Typeface.DEFAULT);
                        break;
                    case 1:
                        editText.setTypeface(Typeface.SERIF);
                        break;
                    case 2:
                        editText.setTypeface(Typeface.SANS_SERIF);
                        break;
                    case 3:
                        editText.setTypeface(Typeface.MONOSPACE);
                        break;
                    case 4:
                        editText.setTypeface(dancingScript);
                        break;
                    case 5:
                        editText.setTypeface(amaticSC);
                        break;
                    case 6:
                        editText.setTypeface(comforterBrush);
                        break;
                    case 7:
                        editText.setTypeface(indieFlower);
                        break;
                    case 8:
                        editText.setTypeface(shadowsIntoLight);
                        break;
                    case 9:
                        editText.setTypeface(caveatVariableFont);
                        break;
                }
            }
        });
        builder.show();
    }

    //关闭键盘
    public void closeKeyboard(){
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        View v = getWindow().peekDecorView();
        if (null != v) {
            //强制隐藏键盘
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }


}
package com.kosmo.softlock;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import static com.kosmo.softlock.MainActivity.context;

public class HpSearchMap extends AppCompatActivity  {

    private static final String TAG = "KOSMO";

    SupportMapFragment mapFragment;
    GoogleMap map;
    MarkerOptions myLocationMarker;

    Button btn_search;
    String hp_name="";
    String isNightChecked="";
    String isWeekendChecked="";
    String hp_type="";

    ProgressDialog dialog;

    GoogleMap googleMap;
    Geocoder geocoder;

    EditText edit_hpName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hpsearchmap);



        geocoder = new Geocoder(this);

        // 검색이름 가져오기
        btn_search = (Button)findViewById(R.id.btn_search);

        // 검색이름 가져오기
        edit_hpName = (EditText)findViewById(R.id.hp_name);

        String[] typeList = {"전체과목", "내과", "안과", "치과", "산부인과", "이비인후과", "피부과"};
        final Spinner spinner = (Spinner)findViewById(R.id.spinner_hpType);
        SpinnerAdapter spinnerAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, typeList);
        spinner.setAdapter(spinnerAdapter);

        // 체크박스 클릭이벤트
        final CheckBox checkNight = (CheckBox) findViewById(R.id.checkNight);
        checkNight.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkNight.isChecked()) {
                    isNightChecked = "y";
                } else {
                    isNightChecked = "";
                }
            }
        });
        final CheckBox checkWeekend = (CheckBox) findViewById(R.id.checkWeekend);
        checkWeekend.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkWeekend.isChecked()) {
                    isWeekendChecked = "y";
                } else {
                    isWeekendChecked = "";
                }
            }
        });

        // 스피너 클릭이벤트
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(HpSearchMap.this,"선택된 아이템 : " + spinner.getItemAtPosition(position),Toast.LENGTH_SHORT).show();
                hp_type = (String)spinner.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        FragmentTabHost fth = (FragmentTabHost) findViewById(android.R.id.tabhost);

        fth.setup(this, getSupportFragmentManager(), R.id.realcontent);


        Bundle bundle = new Bundle();
        bundle.putString("msgStr", "메인엑티비티에서 번들객체를 통해" + " 전달하는 데이터입니다.");
        //4.탭메뉴 추가 : 전달할 데이터는 마지막 매개변수를 사용하고 없으면 null을 쓰면 됨
        /*fth.addTab(fth.newTabSpec("tab1").setIndicator("전체 진료과목"), Myres.class, bundle);
        fth.addTab(fth.newTabSpec("tab2").setIndicator("금천구 가산동"), Myinfo.class, bundle);*/

        //권한체크 후 사용자에 의해 취소되었다면 다시 요청한다.
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            //map.setMyLocationEnabled(true);
        }

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                Log.d(TAG, "GoogleMap is ready...");

                map = googleMap;
                map.getUiSettings().setZoomControlsEnabled(true);

                requestMyLocation();
                //우측 상단에 위치 버튼
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    map.setMyLocationEnabled(true);
                    map.getUiSettings().setMyLocationButtonEnabled(true);
                }
            }
        });
        try {
            MapsInitializer.initialize(this);
        } catch (Exception e) {
            e.printStackTrace();
        }



        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hp_name = edit_hpName.getText().toString();


                new AsyncHttpRequest().execute(
                        // 아이디, 성별, 이메일, 생년월일, 전화번호
                        "http://192.168.0.40:8080/softlock/Android/searchHp"
                        , "hp_type=" + hp_type
                        , "hp_night=" + isNightChecked
                        , "hp_weekend=" + isWeekendChecked
                        , "hp_name=" + hp_name
                );
            }
        });

        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setIcon(android.R.drawable.ic_dialog_alert);
        dialog.setTitle("병원 검색중");
        dialog.setMessage("서버로부터 응답을 기다리고 있습니다.");
    }

    private void requestMyLocation() {
        //내위치로
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        try {
            long minTime = 100000000;
            float minDistance = 0;

            manager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    minTime,
                    minDistance,
                    new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            showCurrentLocation(location);
                        }

                        @Override
                        public void onStatusChanged(String porvider, int status, Bundle extras) {

                        }

                        @Override
                        public void onProviderEnabled(String provider) {

                        }

                        @Override
                        public void onProviderDisabled(String provider) {

                        }

                    });

            Location lastLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastLocation != null) {
                showCurrentLocation(lastLocation);
            }
            manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    minTime,
                    minDistance,
                    new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            showCurrentLocation(location);
                        }

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {

                        }

                        @Override
                        public void onProviderEnabled(String provider) {

                        }

                        @Override
                        public void onProviderDisabled(String provider) {

                        }
                    });
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    // 병원검색을하면 실행하는 함수(마커찍기)
    public void mapMaker(GoogleMap googleMap, Geocoder geocoder, String searchList){



        Log.d("searchList", searchList);

        map.clear();

        //우측 상단에 위치 버튼
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
            map.getUiSettings().setMyLocationButtonEnabled(true);

            //Marker[] markers = {};

            // json데이터 파싱
            try {
                JSONArray jsonArray = new JSONArray(searchList.toString());

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String address = jsonObject.getString("hp_address");
                    String hp_name = jsonObject.getString("hp_name");
                    String hp_type = jsonObject.getString("hp_type");


                    // 주소로 위경도값 받아와 마커찍기(지오코딩)
                    List<Address> list = null;

                    try {
                        list = geocoder.getFromLocationName(
                                address, // 지역 이름
                                100); // 읽을 개수
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("test","입출력 오류 - 서버에서 주소변환시 에러발생");
                    }

                    if (list != null) {
                        if (list.size() == 0) {
                        } else {
                            //Toast.makeText(context, list.get(0).toString(), Toast.LENGTH_SHORT).show();
                            //          list.get(0).getCountryName();  // 국가명
                            double lat = list.get(0).getLatitude();    // 위도
                            double lon = list.get(0).getLongitude();   // 경도

                            final MarkerOptions markerOptions = new MarkerOptions()
                                    .position(new LatLng(lat, lon))
                                    .title(hp_name)
                                    .snippet(hp_type);
                            map.addMarker(markerOptions);
                        }
                    }
                }
                dialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }

            //정보창 클릭에 대한 리스너
            map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {

                 /* String hp_idx = getIntent().getExtras().getString("hp_idx");

                    //Log.d("아아", hp_idx);

                    new AsyncHttpRequest2().execute(

                            "http://192.168.0.40:8080/softlock/Android/info_hp"
                            , "hp_idx=" + hp_idx

                    );
*/
                    //입력된 주소에서 받아오기
                    new AsyncHttpRequest2().execute(
                            "http://192.168.0.40:8080/softlock/Android/info_hp"
                            , "hp_name=" + marker.getTitle()
                    );

                }
            });

            dialog = new ProgressDialog(this);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setIcon(android.R.drawable.ic_dialog_alert);
            dialog.setTitle("상세보기 진입 중");
            dialog.setMessage("서버로부터 응답을 기다리고 있습니다.");

        }
    }

    public void onBtnClicked(View v) {
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        try {
            long minTime = 10000;
            float minDistance = 0;

            manager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    minTime,
                    minDistance,
                    new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            showCurrentLocation(location);
                        }

                        @Override
                        public void onStatusChanged(String s, int i, Bundle bundle) {

                        }

                        @Override
                        public void onProviderEnabled(String s) {

                        }

                        @Override
                        public void onProviderDisabled(String s) {

                        }

                    });

            Location lastLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastLocation != null) {
                showCurrentLocation(lastLocation);
            }
            manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    minTime,
                    minDistance,
                    new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            showCurrentLocation(location);
                        }

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {

                        }

                        @Override
                        public void onProviderEnabled(String provider) {

                        }

                        @Override
                        public void onProviderDisabled(String provider) {

                        }
                    });
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    /*private void showMyLocationMarker(Location location) {
        if (myLocationMarker == null) {
            myLocationMarker = new MarkerOptions();
            myLocationMarker.position(new LatLng(location.getLatitude(),
                    location.getLongitude()));
            myLocationMarker.title("내 위치\n");
            myLocationMarker.snippet("GPS로 확인한 위치");
            myLocationMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.mylocation));
            map.addMarker(myLocationMarker);
        } else {
            myLocationMarker.position(new LatLng(location.getLatitude(),
                    location.getLongitude()));
        }
    }*/

    private void showCurrentLocation(Location location) {
        LatLng curPoint = new LatLng(location.getLatitude(), location.getLongitude());

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(curPoint, 16));

        //showMyLocationMarker(location);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (map != null) {
            try {
                map.setMyLocationEnabled(false);
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (map != null) {
            try {
                map.setMyLocationEnabled(true);
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
    }

    class AsyncHttpRequest extends AsyncTask<String, Void, String> {
        // doInBackground 함수 호출전에 미리 호출하는 메소드
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // 서버로 요청하는 시점에 프로그레스 대화창을 띄워준다.
            if (!dialog.isShowing()) {
                dialog.show();
            }
        }

        // execute()가 호출되면 자동으로 호출되는 메소드(실제동작을 처리)
        @Override
        protected String doInBackground(String... params) {
            // execute()를 호출할때 전달한 3개의 파라미터를 가변인자로 전달받는다.
            // 함수 내부에서는 배열로 사용한다.

            // 파라미터 확인용
            for (String s : params) {
                Log.i("AsyncTask Class", "파라미터 : " + s);
            }

            // 서버의 응답데이터를 저장할 변수(디버깅용)
            StringBuffer sBuffer = new StringBuffer();

            try {
                // 요청주소로 URL객체 생성
                URL url = new URL(params[0]);
                // 위 참조변수로 URL연결
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                // 전송방식은 POST로 설정한다. (디폴트는 GET방식)
                connection.setRequestMethod("POST");
                // OutputStream으로 파라미터를 전달하겠다는 설정
                connection.setDoOutput(true);
                connection.setDoInput(true);
                /*
                요청 파라미터를 OutputStream으로 조립후 전달한다.
                - 파라미터는 쿼리스트링 형태로 지정한다.
                - 한국어를 전송하는 경우에는 URLEncode를 해야한다.
                - 아래와 같이 처리하면 Request Body에 데이터를 담을 수 있다.
                 */
                OutputStream out = connection.getOutputStream();
                out.write(params[1].getBytes());
                out.write("&".getBytes());
                out.write(params[2].getBytes());
                out.write("&".getBytes());
                out.write(params[3].getBytes());
                out.write("&".getBytes());
                out.write(params[4].getBytes());
                out.flush();
                out.close();

                /*
                getResponseCode()를 호출하면 서버로 요청이 전달된다.
                 */
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    // 서버로부터 응답이 온 경우
                    // 응답데이터를 StringBuffer변수에 저장한다.
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(connection.getInputStream(), "UTF-8")
                    );
                    String responseData;
                    while ((responseData = reader.readLine()) != null) {
                        sBuffer.append(responseData + "\n\r");
                    }
                    reader.close();
                } else {
                    // 서버로부터 응답이 없는경우
                    Log.i("AsyncTask Class", "HTTP_OK 안됨");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            // 눌러진 버튼이 로그인이면 파싱후 결과를 출력함
            /*
            [{"pass":"1234","regidate":2018-11-20,"name":"오수민","id":"test1"}, ... ]
             */

            try {

                //JSONArray jsonArray = new JSONArray(sBuffer.toString());

                // sBuffer 초기화
                /*sBuffer.setLength(0);

                Toast.makeText(getApplicationContext(), jsonArray.length(), Toast.LENGTH_LONG).show();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String hp_id = jsonObject.getString("hp_id");
                    String hp_pw = jsonObject.getString("hp_pw");
                    String hp_name = jsonObject.getString("hp_name");
                    String hp_num = jsonObject.getString("hp_num");
                    String hp_username = jsonObject.getString("hp_username");
                    String hp_email = jsonObject.getString("hp_email");
                    String hp_phone = jsonObject.getString("hp_phone");
                    String hp_address = jsonObject.getString("hp_address");
                    String hp_night = jsonObject.getString("hp_night");
                    String hp_wkend = jsonObject.getString("hp_wkend");
                    String hp_intro = jsonObject.getString("hp_intro");
                    String hp_notice = jsonObject.getString("hp_notice");
                    String hp_ori_filename = jsonObject.getString("hp_ori_filename");
                    String hp_perm = jsonObject.getString("hp_perm");
                    String hp_regidate = jsonObject.getString("hp_regidate");
                    String hp_hpphone = jsonObject.getString("hp_hpphone");
                    String hp_address2 = jsonObject.getString("hp_address2");
                    String hp_type = jsonObject.getString("hp_type");

                }*/
            } catch (Exception e) {
                e.printStackTrace();
            }
            return sBuffer.toString();
        }

        /*
        doInBackground메소드가 정상적으로 완료되면 onPostExecute()실행.
        onPostExecute메소드가 doInBackground의 리턴값을 받음.
         */
        @Override
        protected void onPostExecute(String searchList) {
            super.onPostExecute(searchList);
            // 진행대화창 닫기


            // sBuffer를 SearchList로 넘김
            /*Intent intent = new Intent(getApplicationContext(), SearchList.class);
            Log.d("야!!","ㅇㅇ");
            Log.d("sBuffer1", s);
            intent.putExtra("sBuffer", s);
            startActivity(intent);*/

            Log.d("sBuffer", searchList);
            requestMyLocation();
            mapMaker(googleMap, geocoder, searchList);
        }
    }

    class AsyncHttpRequest2 extends AsyncTask<String, Void, String> {
        // doInBackground 함수 호출전에 미리 호출하는 메소드

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        // execute()가 호출되면 자동으로 호출되는 메소드(실제동작을 처리)
        @Override
        protected String doInBackground(String... params) {
            // execute()를 호출할때 전달한 3개의 파라미터를 가변인자로 전달받는다.
            // 함수 내부에서는 배열로 사용한다.

            // 파라미터 확인용
            for (String s : params) {
                Log.i("AsyncTask Class", "파라미터 : " + s);
            }

            // 서버의 응답데이터를 저장할 변수(디버깅용)
            StringBuffer sBuffer = new StringBuffer();

            try {
                // 요청주소로 URL객체 생성
                URL url = new URL(params[0]);
                // 위 참조변수로 URL연결
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                // 전송방식은 POST로 설정한다. (디폴트는 GET방식)
                connection.setRequestMethod("POST");
                // OutputStream으로 파라미터를 전달하겠다는 설정
                connection.setDoOutput(true);
                connection.setDoInput(true);

                OutputStream out = connection.getOutputStream();
                out.write(params[1].getBytes());
                out.flush();
                out.close();

                /*
                getResponseCode()를 호출하면 서버로 요청이 전달된다.
                 */
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    // 서버로부터 응답이 온 경우

                    // 응답데이터를 StringBuffer변수에 저장한다.
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(connection.getInputStream(), "UTF-8")
                    );
                    String responseData;
                    while ((responseData = reader.readLine()) != null) {
                        sBuffer.append(responseData + "\n\r");
                    }
                    reader.close();
                    Log.i("AsyncTask Class", "HTTP_OK 됨");
                } else {
                    // 서버로부터 응답이 없는경우
                    Log.i("AsyncTask Class", "HTTP_OK 안됨");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
           try {
                JSONObject jsonObject = new JSONObject(sBuffer.toString());
                hp_name = jsonObject.getString("hp_name");

            } catch (Exception e) {
                e.printStackTrace();

            }
            Log.d("맴", hp_name);
            return sBuffer.toString();

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            // 진행대화창 닫기

            Log.d("야이따식아",s);
            // sBuffer를 SearchList로 넘김
            Intent intent = new Intent(getApplicationContext(), Hp_Info.class);
            intent.putExtra("sBuffer", s);
            intent.putExtra("hp_name", hp_name);
            startActivity(intent);

        }
    }
}
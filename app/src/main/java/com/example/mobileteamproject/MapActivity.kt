package com.example.mobileteamproject

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.NaverMapSdk
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.util.FusedLocationSource

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private val LOCATION_PERMISSION_REQUEST = 1000

    private lateinit var locationSource: FusedLocationSource
    private var naverMapObj: NaverMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 레이아웃 이름 확인: R.layout.map 가 실제 파일이면 그대로 두면 됨
        setContentView(R.layout.map)

        findViewById<Button>(R.id.backBtn).setOnClickListener { finish() }

        findViewById<Button>(R.id.homeBtn).setOnClickListener {
            // 현재 화면이 "지도 홈"이면 굳이 이동할 필요 없음
            // (원하면 MainActivity로 보내기: startActivity(Intent(this, MainActivity::class.java)))
            Toast.makeText(this, "현재 홈(지도) 화면입니다.", Toast.LENGTH_SHORT).show()
        }

        findViewById<Button>(R.id.searchBtn).setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }

        findViewById<Button>(R.id.myPageBtn).setOnClickListener {
            startActivity(Intent(this, MyPageActivity::class.java))
        }

        // 네이버 지도 클라이언트 키
        NaverMapSdk.getInstance(this).client =
            NaverMapSdk.NcpKeyClient("7irpb4rlyd")

        // 위치 소스
        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST)

        // MapFragment 세팅
        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map_fragment) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map_fragment, it).commit()
            }

        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(naverMap: NaverMap) {
        naverMapObj = naverMap

        val center = LatLng(37.2751, 127.0090)
        naverMap.moveCamera(CameraUpdate.scrollTo(center))

        naverMap.locationSource = locationSource
        naverMap.uiSettings.isLocationButtonEnabled = true

        // 권한이 있으면 바로 따라가기, 없으면 요청
        if (hasPermission()) {
            naverMap.locationTrackingMode = LocationTrackingMode.Follow
        } else {
            requestPermission()
        }
    }

    private fun hasPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            LOCATION_PERMISSION_REQUEST
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            val granted = grantResults.isNotEmpty() &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED

            if (granted) {
                naverMapObj?.locationTrackingMode = LocationTrackingMode.Follow
            } else {
                Toast.makeText(this, "위치 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

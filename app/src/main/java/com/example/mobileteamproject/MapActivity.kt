package com.example.mobileteamproject   // ← 프로젝트 패키지에 맞게

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.util.FusedLocationSource

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private val LOCATION_PERMISSION_REQUEST = 1000

    private lateinit var locationSource: FusedLocationSource
    private var naverMapObj: NaverMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ⚠ 레이아웃 이름 확인: activity_map, map 중에 실제 있는 걸로 바꿔줘
        setContentView(R.layout.map)

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

        // 권한이 있으면 바로 따라가기, 없으면 한 번만 요청
        if (hasPermission()) {
            naverMap.locationTrackingMode = LocationTrackingMode.Follow
        } else {
            requestPermission()
        }
    }

    // 위치 권한 체크
    private fun hasPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    // 권한 요청
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

    // 권한 결과 처리
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (grantResults.isNotEmpty() &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                // 권한 허용됨 → 위치 따라가기 켜기
                naverMapObj?.locationTrackingMode = LocationTrackingMode.Follow
            } else {
                // 권한 거부됨 → 위치 기능 사용 안 함 (토스트 띄우고 끝내도 됨)
                Toast.makeText(this, "위치 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }


}


# CLINIC
1.프로젝트 의의
=============
이전에는 배달을 시킬때 시킬 곳의 전화번호를 필수로 알아야 했었다. 이러한 불편했던 점을 배달의 민족, 요기요에서 효과적인 방식으로 해결을 하였고 
이를 벤치마킹하여 세탁소 및 수선소에서도 비슷한 프로그램을 만들고자 하였다.

2.사용방법
=============
### 2-1) 로그인
<img src="/readme_images/login.jpg" width="200px" height="300px" title="login" alt="login"></img><br/>
__- 로그인 화면__<br>
로그인은 총 2개의 방법으로 구현하였습니다.<br>
1. 내부 Database에서 유저정보를 가져와서 자체적으로 로그인한 방식<br>
2. 카카오톡 API를 이용하여 로그인한 방식.
<br>

### 2-2) 메인화면
<img src="/readme_images/1.jpg" width="200px" height="300px" title="Main" alt="Main"></img><br/>
__- 메인 화면__<br>
메인화면에서는 총 3가지의 Main기능을 확인하실 수 있으며, 네비게이션 바를 입력하면 세부 기능까지 조회하실 수 있습니다. 수선소 버튼이나 
세탁소 버튼이나 둘이 비슷한 UI로 구성이 되어 있으며 activity_clean, activity_fix 레이아웃을 보시면 확인하실 수 있습니다.

### 2-3) 주소 입력 화면
<img src="/readme_images/map.jpg" width="200px" height="300px" title="Address" alt="Address"></img><br/>
__- Google Map__<br>
구글맵을 통하여 좌표를 찍는 것을 구현하였고, 좌표를 인풋으로 받는 Google Geoencoder를 이용하여 주소를 뽑아내었습니다.


### 2-4) 리스트 출력 화면
<img src="/readme_images/list.jpg" width="200px" height="300px" title="list" alt="list"></img><br/>
__- 세탁소에 대한 리스트 출력__<br>

이전 단계에서 설정한 주소를 중심으로 사용자가 설정한 반경 범위 안에 있는 모든 세탁소를 리스트로 보여주었습니다.


### 2-5) 날짜 설정 및 전할 말 입력
<img src="/readme_images/take.jpg" width="200px" height="300px" title="take" alt="take"></img><br/>
__- 세탁소에 대한 리스트 출력__<br>

세탁소가 세탁물을 수거할 날짜 및 배달해주는 날짜를 유저가 직접 설정하며, 유저가 맡길 세탁물들의 수량 또한 입력이 가능합니다.
또한 옷을 맡기는 것 이므로 요구사항이 많을 것이라 예상하여 요구사항란 또한 추가하였습니다.

3.환경 및 구동방법.
=============

| 구분             | 환경              |
|:-------------|:--------------|
|**IDE** | PyCharm, AndroidStudio |
|**Language** | Python3.7, JAVA |
|**Framework** | Django |
|**Front-end** | JAVA |
|**DB** | SQLite3 |
